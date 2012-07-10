package org.ssh.sys.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.Hz;

@Repository
public class HzDao extends SysdataHibernateDao<Hz, Long> {
    private static final String COUNTS = "select count(hz) from Hz u";
    int batchSize = 20;//same as the JDBC batch size

    public Long getHzCount() {
        return findUnique(COUNTS);
    }

    public int batchCreate(final List<Hz> entityList) {
        int insertedCount = 0;
        for (int i = 0; i < entityList.size(); ++i) {
            save(entityList.get(i));
            if (++insertedCount % batchSize == 0) {
                flushAndClear();
            }
        }
        flushAndClear();
        return insertedCount;
    }

    protected void flushAndClear() {
        if (getSession().isDirty()) {
            getSession().flush();
            getSession().clear();
        }
    }

    //返回第一条
    public Hz findOne(String one) {
        Criteria criteria = getSession().createCriteria(entityClass);
        return (Hz)criteria.add( Restrictions.eq("hz", one)).setMaxResults(1).uniqueResult();
    }
}
