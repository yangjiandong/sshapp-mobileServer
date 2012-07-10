package org.ssh.pm.mob.service;

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
import org.ssh.pm.mob.dao.HospitalTypeDao;
import org.ssh.pm.mob.dao.QueryItemDao;
import org.ssh.pm.mob.dao.ItemSourceDao;
import org.ssh.pm.mob.entity.HospitalType;
import org.ssh.pm.mob.entity.QueryItem;
import org.ssh.pm.mob.entity.ItemSource;

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
                QueryItem a = queryItemDao.findUniqueBy("id", Long.valueOf(parentId));
                if (a == null) {
                    entity = new QueryItem();
                    entity.setItemName(itemName);
                    entity.setIsleaf(CoreConstants.ACTIVE);
                    entity.setCodeLevel(1L);
                    entity.setParentId(0L);
                    queryItemDao.save(entity);
                } else {
                    entity = new QueryItem();
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