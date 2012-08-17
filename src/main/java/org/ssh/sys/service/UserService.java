package org.ssh.sys.service;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.security.utils.DigestUtils;
import org.springside.modules.utils.ServiceException;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.sys.dao.HisUserDao;
import org.ssh.sys.dao.PartDBDao;
import org.ssh.sys.dao.RoleDao;
import org.ssh.sys.dao.UserDao;
import org.ssh.sys.entity.PartDB;
import org.ssh.sys.entity.Role;
import org.ssh.sys.entity.User;
import org.ssh.sys.entity.his.HisUser;

@Service
@Transactional
public class UserService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PartDBDao partDBDao;
    @Autowired
    private HisUserDao hisUserDao;

    /**
     * 获取全部用户
     */
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Page<User> page, List<PropertyFilter> filters) {
        Page<User> curPage = userDao.findPage(page, filters);
        List<User> list = curPage.getResult();
        curPage.setResult(list);
        return curPage;
    }

    /**
     * 根据用户id获取角色
     */
    @Transactional(readOnly = true)
    public List<String> getRoleIdsByUserId(Long userId) {
        List<String> result = new ArrayList<String>();
        User user = userDao.findUniqueBy("id", userId);
        List<Role> roles = user.getRoleList();
        for (Role role : roles) {
            result.add(role.getId().toString());
        }
        return result;
    }

    /**
     * 根据用户id获取该用户可以使用的数据库和不可以使用的数据库
     */
    @Transactional(readOnly = true)
    public List<JSONObject> getSelDb(Long userId) {
        List<JSONObject> ret = new ArrayList<JSONObject>();
        List<PartDB> list = partDBDao.find("from PartDB");
        User user = userDao.findUniqueBy("id", userId);
        List<PartDB> list2 = user.getPartDBList();
        JSONObject json;
        for (PartDB entity : list) {
            json = new JSONObject();
            if (list2.contains(entity)) {
                json.put("sel", true);
            } else {
                json.put("sel", false);
            }
            json.put("dbname", entity.getDbname());
            json.put("dbcode", entity.getDbcode());
            ret.add(json);
        }
        return ret;
    }

    /**
     * 根据用户id获取该用户可以使用的数据库
     */
    @Transactional(readOnly = true)
    public List<PartDB> getUserDBS(Long userId) {
        List<User> list = userDao.getUserDBS(userId);
        if (list != null && list.size() > 0) {
            return list.get(0).getPartDBList();
        }
        return null;
    }

    /**
     * 保存用户
     */
    public void saveUser(User user) throws ServiceException {
        User o = null;
        List<PartDB> dbs = new ArrayList<PartDB>();
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            if (user.isTransient()) {
                //PasswordEncoder encoder = new ShaPasswordEncoder();
                //String shaPassword = encoder.encodePassword(CoreConstants.INIT_PASSWORD, null);
                String shaPassword = DigestUtils.sha1ToHex(CoreConstants.INIT_PASSWORD);
                user.setStatus(CoreConstants.USER_ENABLED);
                user.setPassword(shaPassword);
                user.setUpdatePwd(CoreConstants.USER_NOUPDATE_PASSWD);
                o = userDao.findUniqueBy("name", user.getName());
            } else {
                o = userDao.findUnique("from User where name = ? and id <> ? ", user.getName(), user.getId());
            }
            if (o != null) {
                throw new Exception();
            }

            if (user.getDbCodes() != null && user.getDbCodes().length() > 0) {
                String[] codes = StringUtils.split(user.getDbCodes(), "|");
                PartDB db = null;
                for (String code : codes) {
                    db = partDBDao.findUniqueBy("dbcode", code);
                    dbs.add(db);
                }
            }
            if (user.isTransient()) {
                user.setPartDBList(dbs);
                userDao.save(user);
            } else {
                User old = userDao.findUniqueBy("id", user.getId());
                old.setName(user.getName());
                old.setLoginName(user.getLoginName());
                old.setEmail(user.getEmail());
                old.setMobileNo(user.getMobileNo());
                old.setPartDBList(dbs);
                userDao.save(old);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            if (o == null) {
                logger.error("保存用户失败：", e);
                throw new ServiceException("保存用户失败");
            } else {
                throw new ServiceException("此用户名已存在，不能重复添加!");
            }
        }
    }

    /**
     * 删除用户
     */
    public void delete(Long userId) throws ServiceException {
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            User user = userDao.findUniqueBy("id", userId);
            if (user != null) {
                if (user.getName().equals("管理员")) {
                    logger.warn("操作员{}尝试删除超级管理员用户", "当前用户");
                    throw new Exception("不能删除超级管理员");
                }
                userDao.delete(user);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("删除用户失败:" + e);
            throw new ServiceException();
        }
    }

    /**
     * 保存指定用户的角色权限
     */
    public void saveUserRoles(Long userId, String roleIds) throws ServiceException {
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            // 当前的授权
            String[] ids = StringUtils.split(roleIds, ",");
            User user = userDao.findUniqueBy("id", userId);
            List<Role> newRoles = new ArrayList<Role>();
            Role role = null;
            for (String roleId : ids) {
                role = roleDao.findUniqueBy("id", Long.valueOf(roleId));
                newRoles.add(role);
            }
            user.setRoleList(newRoles);
            userDao.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("保存用户的角色权限失败:" + e);
            throw new ServiceException(e.getMessage());
        }

    }

    /**
     * 重置用户密码
     */
    public void resetUser(Long userId) throws ServiceException {
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            User user = userDao.findUniqueBy("id", userId);
            ///PasswordEncoder encoder = new ShaPasswordEncoder();
            //String shaPassword = encoder.encodePassword(CoreConstants.INIT_PASSWORD, null);
            String shaPassword = DigestUtils.sha1ToHex(CoreConstants.INIT_PASSWORD);
            user.setPassword(shaPassword);
            user.setUpdatePwd(CoreConstants.USER_NOUPDATE_PASSWD);
            userDao.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("重置用户密码失败：" + e);
            throw new ServiceException();
        }
    }

    /**
     * 启用用户
     */
    public void startUser(Long userId) throws ServiceException {
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            User user = userDao.findUniqueBy("id", userId);
            user.setStatus(CoreConstants.USER_ENABLED);
            userDao.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("启用用户失败：" + e);
            throw new ServiceException();
        }
    }

    /**
     * 停用用户
     */
    public void stopUser(Long userId) throws ServiceException {
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            User user = userDao.findUniqueBy("id", userId);
            user.setStatus(CoreConstants.USER_DISABLED);
            userDao.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("停用用户失败：" + e);
            throw new ServiceException();
        }
    }

    //导入his用户
    public void importHisUser() throws ServiceException {
        DataSource dataSource = (DataSource) SpringContextHolder.getBean("sysDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try {
            // 存储过程
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
            jdbcCall.withProcedureName("sp_import_his_user");
            jdbcCall.execute();

            List<PartDB> dbs = new ArrayList<PartDB>();
            PartDB db = partDBDao.findUniqueBy("dbcode", "default");
            dbs.add(db);

            List<HisUser> alls = this.hisUserDao.find("from " + HisUser.class.getName()
                    + " where code not in(select userNo from User where userNo is not null)");
            for (HisUser employee : alls) {
                User u = new User();
                u.setUserNo(employee.getCode());
                u.setLoginName(employee.getLoginName());
                u.setName(employee.getName());
                u.setDepartNo(employee.getDepartNo());
                u.setDepartName(employee.getDepartName());
                u.setMobileNo(employee.getMobileNo());
                u.setTitle(employee.getTitle());
                u.setJob(employee.getJob());

                String shaPassword = DigestUtils.sha1ToHex(CoreConstants.INIT_PASSWORD);
                u.setStatus(CoreConstants.USER_ENABLED);
                u.setPassword(shaPassword);
                u.setUpdatePwd(CoreConstants.USER_UPDATED_PASSWD);
                u.setPartDBList(dbs);
                this.userDao.save(u);
            }
        } catch (Exception e) {
            logger.error(getClass().getName(), e);
            throw new ServiceException();
        }
    }
}
