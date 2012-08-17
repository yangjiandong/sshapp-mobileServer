package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.CronTask;

@Repository
public class CronTaskDao extends SysdataHibernateDao<CronTask, Long>{
    private static final String COUNTS = "select count(db) from CronTask db";
    //final int batchSize = 20;//same as the JDBC batch size

    public Long getCount() {
        return findUnique(COUNTS);
    }
}
