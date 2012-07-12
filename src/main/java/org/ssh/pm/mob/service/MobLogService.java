package org.ssh.pm.mob.service;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.mob.entity.MobLog;

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
}
