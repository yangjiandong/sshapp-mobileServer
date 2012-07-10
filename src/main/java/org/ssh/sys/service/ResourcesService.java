package org.ssh.sys.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.hcost.web.UserSession;
import org.ssh.sys.dao.AppSetupDao;
import org.ssh.sys.dao.MyResourceDao;
import org.ssh.sys.dao.MySystemDao;
import org.ssh.sys.dao.ResourceDao;
import org.ssh.sys.dao.RoleResourceDao;
import org.ssh.sys.dao.UserDao;
import org.ssh.sys.entity.MyResource;
import org.ssh.sys.entity.MySystem;
import org.ssh.sys.entity.Resource;
import org.ssh.sys.entity.Role;
import org.ssh.sys.entity.RoleResource;
import org.ssh.sys.entity.User;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;

@Service
@Transactional
public class ResourcesService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static Logger logger2 = LoggerFactory.getLogger("org.ssh.pms.times");

    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private AppSetupDao appSetupDao;
    @Autowired
    private RoleResourceDao roleResourceDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MySystemDao mySystemDao;
    @Autowired
    private MyResourceDao myResourceDao;

    //登录用户,返回授权的模块
    @Transactional
    public List<Resource> loadAuthModule(UserSession us) throws ServiceException {
        User u = us.getAccount();
        List<Resource> result = new ArrayList<Resource>();
        Map<String, Object> values = new HashMap<String, Object>();

        //重新加载user
        User user = userDao.findOneBy("id", u.getId());
        userDao.initUser(user);
        List<Long> roles = new ArrayList<Long>();
        for (Role role : user.getRoleList()) {
            roles.add(role.getId());
        }
        Map<String, List<Long>> userRoles = Collections.singletonMap("roles", roles);

        //有可能没角色
        if (roles.size() == 0)
            return result;

        List<RoleResource> alist = this.roleResourceDao.find("from RoleResource where roleId in (:roles) ", userRoles);
        List<Long> res = new ArrayList<Long>();
        for (RoleResource roleResource : alist) {
            res.add(roleResource.getResourceId());
        }

        //实施现场开放的模块
        String hql = "";
        if (res.size() == 0) {
            //没有授权菜单
            if (u.getLoginName().equalsIgnoreCase(CoreConstants.ADMIN_NAME)) {
                //系统管理员
                hql = "select a from Resource a ,MySystem b where resourceType = :type and parentId = 0  "
                        + "and state = :state and a.resourceId = b.resourceId order by orderNo";
                values.put("type", "1");
                values.put("state", "1");
            } else {
                res.add(0L);//装载个不存在的id
                hql = "select a from Resource a,MySystem b where resourceType = :type and parentId = 0  "
                        + "and state = :state and a.resourceId in(:res) and a.resourceId = b.resourceId "
                        + "order by orderNo";
                values.put("type", "1");
                values.put("state", "1");
                values.put("res", res);
            }
        } else {
            hql = "select a from Resource a,MySystem b where resourceType = :type and parentId = 0  "
                    + "and state = :state and a.resourceId in(:res) and a.resourceId = b.resourceId "
                    + "order by orderNo";
            values.put("type", "1");
            values.put("state", "1");
            values.put("res", res);

        }
        result = resourceDao.find(hql, values);

        Long start = System.currentTimeMillis();
        //生成用户授权菜单
        //根据角色分配，有可能菜单重复
        if (!us.isHasResource()) {
            createUserMenus(u, result, roles);
            us.setHasResource(true);
        }
        logger2.info("授权菜单:" + String.valueOf((System.currentTimeMillis() - start)) + "ms");

        return result;
    }

    // 需产生codeLevel,重新生成MyResource数据
    private void createUserMenus2(User u, List<Resource> result, List<Long> roles) {
        myResourceDao.batchExecute("delete from MyResource where expuid = ? ", u.getId());

        createOne(u, result, roles, 0, null);
    }

    private void createOne(User u, List<Resource> result2, List<Long> roles, int i, Long m) {
        for (Resource a : result2) {
            MyResource entity = new MyResource();

            entity.setCodeLevel(i);
            entity.setExpuid(u.getId());
            entity.setIconCls(a.getIconCls());
            entity.setLeaf(a.isLeaf());
            entity.setOrderNo(a.getOrderNo());
            entity.setResourceId(a.getResourceId());
            entity.setParentId(a.getParentId());
            entity.setResourceType(a.getResourceType());
            entity.setState(a.getState());
            entity.setText(a.getText());
            entity.setType(a.getType());
            entity.setUrl(a.getUrl());
            entity.setNote(a.getNote());
            if (i == 0) {
                entity.setMcode(a.getResourceId());
            } else {
                entity.setMcode(m);
            }
            myResourceDao.save(entity);
        }

        List<MyResource> list = myResourceDao.find(" select a from MyResource a where expuid = ? "
                + " and codeLevel = ? ", u.getId(), i);
        for (MyResource my : list) {
            if (!my.isLeaf()) {

                Map<String, Object> res = new HashMap<String, Object>();
                res.put("parentId", my.getResourceId());
                res.put("expuid", u.getId());
                res.put("roles", roles);

                String hql = "";
                if (u.getLoginName().equalsIgnoreCase(CoreConstants.ADMIN_NAME)) {
                    hql = "select a from Resource a where a.parentId = :parentId and not exists "
                            + "(select resourceId from MyResource where expuid= :expuid and resourceId=a.resourceId)";
                } else {
                    hql = "select distinct a from Resource a,RoleResource b where a.parentId = :parentId "
                            + " and a.resourceId = b.resourceId and b.roleId in (:roles) and not exists "
                            + "(select resourceId from MyResource where expuid= :expuid and resourceId=a.resourceId) order by orderNo";
                }
                List<Resource> rList = resourceDao.find(hql, res);

                if (!rList.isEmpty()) {
                    //Long start = System.currentTimeMillis();
                    createOne(u, rList, roles, i + 1, my.getMcode());
                    //logger2.info("授权菜单:" + my.getText() + String.valueOf((System.currentTimeMillis() - start)) + "ms");
                }
            }
        }
    }

    // 需产生codeLevel,重新生成MyResource数据
    private void createUserMenus(User u, List<Resource> result, List<Long> roles) {
        myResourceDao.batchExecute("delete from MyResource where expuid = ? ", u.getId());

        for (Resource a : result) {
            MyResource entity = new MyResource();
            //if ((Long) this.myResourceDao.findUnique("select count(h) from " + MyResource.class.getName()
            //		+ " h where resourceId=? and expuid=?", a.getResourceId(), u.getId()) != 0) {
            //	continue;
            //}

            entity.setCodeLevel(0);
            entity.setExpuid(u.getId());
            entity.setIconCls(a.getIconCls());
            entity.setLeaf(a.isLeaf());
            entity.setOrderNo(a.getOrderNo());
            entity.setResourceId(a.getResourceId());
            entity.setParentId(a.getParentId());
            entity.setResourceType(a.getResourceType());
            entity.setState(a.getState());
            entity.setText(a.getText());
            entity.setType(a.getType());
            entity.setUrl(a.getUrl());
            entity.setNote(a.getNote());
            entity.setMcode(a.getResourceId());
            myResourceDao.save(entity);

        }

        //暂时不会超过10级菜单
        for (int i = 0; i < 10; i++) {

            //Long start = System.currentTimeMillis();

            List<MyResource> list = myResourceDao.find(" select a from MyResource a where expuid = ? "
                    + " and codeLevel = ? ", u.getId(), i);
            for (MyResource my : list) {
                if (u.getLoginName().equalsIgnoreCase(CoreConstants.ADMIN_NAME)) {

                    List<Resource> rList = resourceDao
                            .find("select a from Resource a where a.parentId = ? and not exists "
                                    + "(select resourceId from MyResource where expuid= ? and resourceId=a.resourceId)",
                                    my.getResourceId(), u.getId());

                    for (Resource a : rList) {

                        MyResource entity = new MyResource();
                        entity.setCodeLevel(i + 1);
                        entity.setExpuid(u.getId());
                        entity.setIconCls(a.getIconCls());
                        entity.setLeaf(a.isLeaf());
                        entity.setOrderNo(a.getOrderNo());
                        entity.setResourceId(a.getResourceId());
                        if (i == 0) {
                            entity.setParentId(0L);
                        } else {
                            entity.setParentId(a.getParentId());
                        }
                        entity.setResourceType(a.getResourceType());
                        entity.setState(a.getState());
                        entity.setText(a.getText());
                        entity.setType(a.getType());
                        entity.setUrl(a.getUrl());
                        entity.setNote(a.getNote());
                        entity.setMcode(my.getMcode());
                        myResourceDao.save(entity);
                    }

                } else {
                    Map<String, Object> res = new HashMap<String, Object>();
                    res.put("parentId", my.getResourceId());
                    res.put("expuid", u.getId());
                    res.put("roles", roles);

                    List<Resource> rList = resourceDao
                            .find("select distinct a from Resource a,RoleResource b where a.parentId = :parentId"
                                    + " and a.resourceId = b.resourceId and b.roleId in (:roles) and not exists "
                                    + "(select resourceId from MyResource where expuid= :expuid and resourceId=a.resourceId)",
                                    res);

                    for (Resource a : rList) {

                        MyResource entity = new MyResource();
                        entity.setCodeLevel(i + 1);
                        entity.setExpuid(u.getId());
                        entity.setIconCls(a.getIconCls());
                        entity.setLeaf(a.isLeaf());
                        entity.setOrderNo(a.getOrderNo());
                        entity.setResourceId(a.getResourceId());
                        if (i == 0) {
                            entity.setParentId(0L);
                        } else {
                            entity.setParentId(a.getParentId());
                        }
                        entity.setResourceType(a.getResourceType());
                        entity.setState(a.getState());
                        entity.setText(a.getText());
                        entity.setType(a.getType());
                        entity.setUrl(a.getUrl());
                        entity.setMcode(my.getMcode());
                        myResourceDao.save(entity);
                    }
                }
            }

            //logger2.info("授权菜单:" + i + " " + String.valueOf((System.currentTimeMillis() - start)) + "ms");
        }
    }

    //返回所有模块
    @Transactional(readOnly = true)
    public List<Resource> loadAllModule() throws ServiceException {
        List<Resource> result = new ArrayList<Resource>();
        Map<String, Object> values = new HashMap<String, Object>();

        String hql = "from Resource where resourceType = :type and parentId = 0  and state = :state and resourceId in(select resourceId from MySystem)  order by orderNo";
        if (!this.appSetupDao.checkArmy()) {
            hql = "from Resource where resourceType = :type and parentId = 0  and state = :state  and resourceId in(select resourceId from MySystem) and note not like '%army%' order by orderNo";
        }
        values.put("type", "1");
        values.put("state", "1");

        result = resourceDao.find(hql, values);
        return result;
    }

    /**
     * 指定模块下,获取当前用户授权的菜单,注意,此处强制为第二级,?多余
     */
    @Transactional(readOnly = true)
    public List<Resource> loadGrantedSubSystems(Long moduleId, User u) throws ServiceException {
        List<Resource> result = new ArrayList<Resource>();
        String hql = "";
        try {
            Map<String, Object> values = new HashMap<String, Object>();
            if (u.getLoginName().equals("Admin")) {
                hql = "from Resource where resourceType = :type and parentId = :parentId order by orderNo";
                values.put("parentId", moduleId);
                values.put("type", "2");
            } else {
                hql = "from Resource where resourceType = :type and parentId = :parentId order by orderNo";
                values.put("parentId", moduleId);
                values.put("type", "2");
            }
            //            if (user.getLoginName().equals("admin")) {
            //                hql = "from Resource where resourceType = :type and parentId = 0 order by orderNo";
            //                values.put("type", 1);
            //            } else {
            //                hql = "select a from Resource a,RoleResource b,UserRoles c where a.parentId =0 and c.userId = :userId and c.roleId = b.roleId and b.resourceId = a.oid and a.resourceType.typeName = :typeName order by a.orderNo";
            //                values.put("type", 1);
            //                values.put("userId", userId);
            //            }

            result = resourceDao.find(hql, values);
        } catch (Exception e) {
            logger.error("指定模块下,获取当前用户授权的菜单,出错：" + e);
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    /**
     * 指定模块下,获取当前用户授权的菜单
     */
    @Transactional(readOnly = true)
    public List<Resource> loadGrantedMenus(Long parentId, Long moduleId, User u) throws ServiceException {
        List<Resource> result = new ArrayList<Resource>();

        //重新加载user
        User user = userDao.findOneBy("id", u.getId());

        if (user.getLoginName().equalsIgnoreCase("admin")) {
            if (parentId.longValue() == 0) {
                parentId = moduleId;
            }
            Map<String, Object> values = new HashMap<String, Object>();
            String hql = "from Resource where parentId = :parentId  order by orderNo ";
            if (!this.appSetupDao.checkArmy()) {
                hql = "from Resource where parentId = :parentId  and note not like '%army%' order by orderNo";
            }
            values.put("parentId", parentId);
            result = resourceDao.find(hql, values);
            return result;
        }

        userDao.initUser(user);
        List<Long> roles = new ArrayList<Long>();
        for (Role role : user.getRoleList()) {
            roles.add(role.getId());
        }
        Map<String, List<Long>> map = Collections.singletonMap("roles", roles);

        List<RoleResource> alist = this.roleResourceDao.find("from RoleResource where roleId in (:roles) ", map);
        List<Long> res = new ArrayList<Long>();
        for (RoleResource roleResource : alist) {
            res.add(roleResource.getResourceId());
        }

        String hql = "";
        if (parentId.longValue() == 0) {
            parentId = moduleId;
        }
        try {
            Map<String, Object> values = new HashMap<String, Object>();
            hql = "from Resource where parentId = :parentId and resourceId in (:res) order by orderNo ";
            if (!this.appSetupDao.checkArmy()) {
                hql = "from Resource where parentId = :parentId and resourceId in (:res) and note not like '%army%' order by orderNo";
            }
            values.put("parentId", parentId);
            values.put("res", res);
            result = resourceDao.find(hql, values);
        } catch (Exception e) {
            logger.error("获取用户授权的模块：" + e);
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<MyResource> loadGrantedMenus2(Long moduleId, User u) throws ServiceException {
        List<MyResource> result = new ArrayList<MyResource>();
        String hql = "from MyResource where expuid = ? and mcode = ? and codeLevel>0 order by orderNo ";
        if (!this.appSetupDao.checkArmy()) {
            hql = "from MyResource where expuid = ? and mcode = ? "
                    + " and codeLevel>0 and (note is null or note not like '%army%') order by orderNo";
        }
        result = resourceDao.find(hql, u.getId(), moduleId);
        return result;
    }

    /**
     * 获取全部模块
     */
    @Transactional(readOnly = true)
    public List<Resource> loadModules() throws ServiceException {
        return resourceDao.find("from Resource where parentId = 0 and resourceType.typeName = ? order by orderNo", 1);
    }

    /**
     * 获取全部资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadAllResources() throws ServiceException {

        List<MySystem> mList = mySystemDao.getAll();
        List<Resource> list = new ArrayList<Resource>();
        for (MySystem a : mList) {
            String resourceId = String.valueOf(a.getResourceId());
            //convert(char(10),resourceId )
            List<Resource> rList = resourceDao.find(" from Resource where cast(resourceId as string) like ? ",
                    resourceId + "%");
            for (Resource r : rList) {
                list.add(r);
            }
        }
        return list;

    }

    /**
     * 获取资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadResources(Long parentId) throws ServiceException {
        return resourceDao.find("from Resources where parentId = ? order by orderNo", parentId);

    }

    /**
     * 获取类型为子系统的系统资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadSubSystems(Long parentId) throws ServiceException {
        String hql = "from Resources where parentId = ? order by orderNo";
        if (!this.appSetupDao.checkArmy()) {
            hql = "from Resources where parentId = ? and note not like '%army%' order by orderNo";
        }
        return resourceDao.find(hql, parentId);

    }

    /**
     * 获取指定oid的子资源信息
     */
    @Transactional(readOnly = true)
    public List<Resource> getChildrenResource(Long parentId) throws ServiceException {
        return resourceDao.find("from Resource where parentId = ? order by orderNo", parentId);
    }

    /**
     * 保存系统资源
     */
    public Resource save(String jsonEntity) throws ServiceException {
        Resource result = null;

        try {
            if (StringUtils.isNotEmpty(jsonEntity)) {
                JSONObject json = JSONObject.fromObject(jsonEntity);
                Resource entity = (Resource) JSONObject.toBean(json, Resource.class);

                //                if (entity.isTransient()) {
                //                    entity.setLeaf("Y");
                //                } else {
                //                    if (resourceDao.findBy("parentId", entity.getId()).size() > 0)
                //                        entity.setLeaf("N");
                //                    else
                //                        entity.setLeaf("Y");
                //                }

                Long parentId = entity.getParentId();
                if (parentId != null) {
                    Resource parentEntity = resourceDao.findUnique("id", parentId);
                    //parentEntity.setLeaf(false);
                    //parentEntity.setLeaf("N");
                    resourceDao.save(parentEntity);
                }

                //result = resourceDao.saveNew(entity);
            } else {
                throw new Exception();
            }

        } catch (Exception e) {
            result = null;
            logger.error("保存资源信息失败：" + e);
            throw new ServiceException();
        }

        return result;
    }

    public void initData(String type, String fileName) throws ServiceException {
        this.resourceDao.deleteResources(type);

        logger.debug("开始装载资源初始数据");

        File resourcetxt = new File(this.getClass().getResource(fileName).getFile());

        String star[] = {};
        try {
            // use google guava
            List<String> lines = Files.readLines(resourcetxt, Charsets.UTF_8);

            Resource re;
            int line = 1;
            for (String thisLine : lines) {
                //第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }

                Iterable<String> s = Splitter.on(",").split(thisLine);
                //Iterable<String> s = Splitter.on(",").omitEmptyStrings().trimResults().split(testString);
                star = Iterables.toArray(s, String.class);

                if (star[0].trim().equals(""))
                    continue;

                re = new Resource();
                re.setResourceId(Long.valueOf(star[0].trim()));
                re.setText(star[1].trim());
                re.setUrl(star[2].trim());
                re.setLeaf(star[3].trim().equals("1") ? true : false);
                re.setParentId(Long.valueOf(star[4].trim()));
                re.setResourceType(star[5].trim());
                re.setOrderNo(Long.valueOf(star[6].trim()));
                re.setState(star[7].trim());
                re.setIconCls(star[8].trim());
                re.setType(type);
                if (star.length > 9) {
                    re.setNote(star[9].trim());
                } else {
                    re.setNote(star[1].trim());
                }

                this.resourceDao.save(re);
            }
        } catch (Exception e) {
            logger.error("str[]:" + StringUtils.join(star, ","));
            logger.error("装载资源数据出错:" + e);
            throw new ServiceException("导入资源时，服务器发生异常");
        } finally {
            //br.close();
        }
    }

    @Transactional(readOnly = true)
    public Resource getResource(Long id) {
        return resourceDao.findUniqueBy("resourceId", id);
    }

    @Transactional(readOnly = true)
    public Long countData(Long moduleId) {
        return this.mySystemDao.findUnique("select count(db) from MySystem db where resourceId=?", moduleId);
    }

}
