package org.ssh.pm.mob.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.mob.entity.MobLog;
import org.ssh.pm.nurse.service.VitalSignService;

@Service
@Transactional
public class MobLogService {
    private static Logger logger = LoggerFactory.getLogger(VitalSignService.class);

    HibernateDao<MobLog, Long> mobLogDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        mobLogDao = new HibernateDao<MobLog, Long>(sessionFactory, MobLog.class);
    }

    public void save(MobLog entity) {

        try {
            this.mobLogDao.save(entity);
        } catch (Exception e) {
            logger.error("saveItem:", e.getMessage());
            throw new ServiceException("失败");
        }
    }

    @Transactional(readOnly = true)
    public Page<MobLog> query(Page<MobLog> page, List<PropertyFilter> filters) {
        Page<MobLog> curPage = mobLogDao.findPage(page, filters);
        return curPage;
    }

    public void deletesAll(List<Long> ids) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ids", ids);

            mobLogDao.batchExecute("delete from MobLog where id in (:ids))", map);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException("删除失败");
        }
    }
}
