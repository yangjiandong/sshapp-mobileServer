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
import org.ssh.pm.mob.entity.MobVersion;
import org.ssh.pm.nurse.service.VitalSignService;
import org.ssh.pm.orm.hibernate.EntityService;

@Service
@Transactional
public class MobVersionService extends EntityService<MobVersion, Long> {
    private static Logger logger = LoggerFactory.getLogger(VitalSignService.class);

    HibernateDao<MobVersion, Long> mobVersionDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        mobVersionDao = new HibernateDao<MobVersion, Long>(sessionFactory, MobVersion.class);
    }

    @Override
    protected HibernateDao<MobVersion, Long> getEntityDao() {
        return mobVersionDao;
    }

    @Transactional(readOnly = true)
    public Page<MobVersion> query(Page<MobVersion> page, List<PropertyFilter> filters) {
        Page<MobVersion> curPage = mobVersionDao.findPage(page, filters);
        return curPage;
    }

    public void deletesAll(List<Long> ids) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ids", ids);

            mobVersionDao.batchExecute("delete from MobVersion where id in (:ids))", map);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException("删除失败");
        }
    }

    @Transactional(readOnly = true)
    public String getLastVersion() {
        return mobVersionDao.findUnique("select max(version) from MobVersion");
    }

}
