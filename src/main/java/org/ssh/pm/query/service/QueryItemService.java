package org.ssh.pm.query.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.mob.MobConstants;
import org.ssh.pm.mob.dao.ItemSourceDao;
import org.ssh.pm.mob.entity.ItemSource;
import org.ssh.pm.query.dao.HospitalTypeDao;
import org.ssh.pm.query.dao.QueryItemDao;
import org.ssh.pm.query.entity.HospitalType;
import org.ssh.pm.query.entity.QueryItem;

@Service
@Transactional
public class QueryItemService {
    private static Logger logger = LoggerFactory.getLogger(QueryItemService.class);

    @Autowired
    private HospitalTypeDao hospitalTypeDao;
    @Autowired
    private QueryItemDao queryItemDao;
    @Autowired
    private ItemSourceDao queryItemSourceDao;

    // 初始化,导入全院概况指标
    public void initData() throws ServiceException {
        // 判断新增
        logger.debug("开始装载全院概况指标初始数据");

        File resourcetxt = new File(this.getClass().getResource("/data/queryitem.txt").getFile());
        String star[] = {};
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            QueryItem re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                // 第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                star = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                if (queryItemDao.findUniqueBy("id", Long.valueOf(star[0].trim())) != null)
                    continue;

                re = new QueryItem();
                re.setId(Long.valueOf(star[0].trim()));
                re.setItemName(star[1].trim());
                re.setCodeLevel(Long.valueOf(star[2].trim()));
                re.setIsleaf(star[3].trim());
                re.setParentId(Long.valueOf(star[4].trim()));
                this.queryItemDao.save(re);
            }
            initData2();
        } catch (Exception e) {
            logger.error("str[]:" + StringUtils.join(star, ","));
            logger.error("装载全院概况指标数据出错:" + e);
            throw new ServiceException("导入全院概况指标时，服务器发生异常");
        } finally {

        }
    }

    // 初始化,导入全院概况指标数据源
    public void initData2() throws ServiceException {
        // 判断新增
        logger.debug("开始装载全院概况指标数据源初始数据");

        queryItemSourceDao.batchExecute("delete from ItemSource ");
        File resourcetxt = new File(this.getClass().getResource("/data/itemsource.txt").getFile());
        String star[] = {};
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            ItemSource re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                // 第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                star = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                re = new ItemSource();
                re.setItemId(Long.valueOf(star[0].trim()));
                re.setItemName(star[1].trim());
                re.setSpName(star[2].trim());
                re.setTypeCode(star[3].trim());
                this.queryItemSourceDao.save(re);
            }
        } catch (Exception e) {
            logger.error("str[]:" + StringUtils.join(star, ","));
            logger.error("装载全院概况指标数据源数据出错:" + e);
            throw new ServiceException("导入全院概况指标数据源时，服务器发生异常");
        } finally {

        }
    }

    public List<HospitalType> queryHospital() {

        List<HospitalType> list = hospitalTypeDao.getAll();
        return list;

    }

    @Transactional(readOnly = true)
    public String validHospital(String id, String name) {

        StringBuffer error = new StringBuffer();
        if (StringUtils.isNotBlank(id)) {

            List<HospitalType> list = hospitalTypeDao.find(" from HospitalType where id != ? and name = ? ",
                    Long.valueOf(id), name);
            if (list != null && list.size() > 0)
                error.append("医院名称不能重复");

        } else {
            List<HospitalType> list = hospitalTypeDao.find(" from HospitalType where name = ? ", name);
            if (list != null && list.size() > 0)
                error.append("医院名称不能重复");
        }

        return error.toString();
    }

    public void saveHospital(String id, String name) {

        try {
            if (StringUtils.isNotBlank(id)) {
                HospitalType item = hospitalTypeDao.findUniqueBy("id", Long.valueOf(id));
                item.setName(name);
                hospitalTypeDao.save(item);
            } else {
                HospitalType item = new HospitalType();
                item.setName(name);
                hospitalTypeDao.save(item);
            }
        } catch (Exception e) {
            logger.error("saveItem:", e.getMessage());
            throw new ServiceException("医院名称定义失败");
        }

    }

    public void deleteHospital(Long id) {
        try {

            queryItemDao.batchExecute("delete from HospitalType where id = ? ", id);

        } catch (Exception e) {
            logger.error("deleteHospital:", e.getMessage());
            throw new ServiceException("删除失败");
        }
    }

    /**
     * 获取全部项目(tree使用)
     */
    @Transactional(readOnly = true)
    public List<JSONObject> queryItem() throws ServiceException {
        String hql = null;
        JSONObject json;
        List<QueryItem> list = new ArrayList<QueryItem>();
        List<JSONObject> result = new ArrayList<JSONObject>();

        hql = "from QueryItem  order by id ";
        list = queryItemDao.find(hql);

        for (QueryItem a : list) {
            json = new JSONObject();
            json.put("id", a.getId());
            json.put("codeLevel", a.getCodeLevel());
            json.put("itemName", a.getItemName().trim());
            json.put("IsLeaf", a.getIsleaf().trim().equalsIgnoreCase("Y") ? true : false);
            json.put("parentId", a.getParentId());

            result.add(json);
        }
        return result;
    }

    public List<QueryItem> queryItem2(String id) {

        List<QueryItem> list = queryItemDao.findBy("id", Long.valueOf(id));
        return list;

    }

    public String valid(String id, String itemName) {
        QueryItem item = null;
        StringBuffer error = new StringBuffer();

        if (StringUtils.isBlank(id)) {
            item = queryItemDao.findUniqueBy("itemName", itemName);
            if (item != null) {
                error.append("查询指标不能重复");
            }

        } else {
            List<QueryItem> list = queryItemDao.find(" from QueryItem where id != ? and itemName = ? ",
                    Long.valueOf(id), itemName);
            if (list != null && list.size() > 0)
                error.append("查询指标不能重复");

        }

        return error.toString();
    }

    public QueryItem saveItem(String id, String itemName, String codeLevel, String parentId) {
        QueryItem entity = null;
        try {
            if (StringUtils.isBlank(id)) {
                Long newId = queryItemDao.getNewId();
                QueryItem a = queryItemDao.findUniqueBy("id", Long.valueOf(parentId));
                if (a == null) {
                    entity = new QueryItem();
                    entity.setId(newId);
                    entity.setItemName(itemName);
                    entity.setIsleaf(CoreConstants.ACTIVE);
                    entity.setCodeLevel(1L);
                    entity.setParentId(0L);
                    queryItemDao.save(entity);
                } else {
                    entity = new QueryItem();
                    entity.setId(newId);
                    entity.setItemName(itemName);
                    entity.setIsleaf(CoreConstants.ACTIVE);
                    entity.setCodeLevel(a.getCodeLevel() + 1L);
                    entity.setParentId(a.getId());
                    queryItemDao.save(entity);

                    a.setIsleaf(CoreConstants.INACTIVE);
                    queryItemDao.save(a);

                }

            } else {

                entity = queryItemDao.findUniqueBy("id", Long.valueOf(id));
                entity.setItemName(itemName);
                queryItemDao.save(entity);
            }

        } catch (Exception e) {
            throw new ServiceException("保存失败");
        }

        return entity;
    }

    public void deleteItem(Long id) {
        try {
            QueryItem q = queryItemDao.findUniqueBy("id", id);
            List<QueryItem> list = queryItemDao.find(" from QueryItem where parentId = ? and id != ? ",
                    q.getParentId(), id);
            if (list.size() == 0) {
                QueryItem p = queryItemDao.findUniqueBy("id", q.getParentId());
                if (p != null) {
                    p.setIsleaf(CoreConstants.ACTIVE);
                    queryItemDao.save(p);
                }

            }
            queryItemSourceDao.batchExecute("delete from ItemSource where itemId = ? ", id);

            queryItemDao.batchExecute("delete from QueryItem where id = ? or parentId = ? ", id, id);

        } catch (Exception e) {
            logger.error("deleteItem:", e.getMessage());
            throw new ServiceException("删除失败");
        }
    }

    public List<ItemSource> querySource(String itemId) {

        List<ItemSource> list = queryItemSourceDao.findBy("itemId", Long.valueOf(itemId));

        return list;

    }

    public String validSource(String id, String itemId, String spName) {
        StringBuffer error = new StringBuffer();

        if (StringUtils.isNotBlank(id)) {
            List<ItemSource> list = queryItemSourceDao.find(
                    " from QueryItemSource where id!= ? and itemId = ? and spName = ? ", Long.valueOf(id),
                    Long.valueOf(itemId), spName);
            if (list != null && list.size() > 0)
                error.append("数据源重复");
        }

        return error.toString();
    }

    public void saveSource(String id, String itemId, String spName) {
        ItemSource entity = null;
        try {

            QueryItem q = queryItemDao.findUniqueBy("id", Long.valueOf(itemId));

            if (StringUtils.isBlank(id)) {

                entity = new ItemSource();
                entity.setItemId(Long.valueOf(itemId));
                entity.setItemName(q.getItemName());
                entity.setSpName(spName);
                entity.setTypeCode(MobConstants.MOB_QUERY);
                queryItemSourceDao.save(entity);

            } else {

                entity = queryItemSourceDao.findUniqueBy("id", Long.valueOf(id));
                entity.setSpName(spName);
                queryItemSourceDao.save(entity);
            }

        } catch (Exception e) {
            throw new ServiceException("保存失败");
        }

    }

    public void deleteSource(Long id) {
        try {

            queryItemSourceDao.batchExecute("delete from ItemSource where id = ? ", id);

        } catch (Exception e) {
            throw new ServiceException("删除失败");
        }
    }
}