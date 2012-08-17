package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.PartDB;

@Repository
public class PartDBDao extends SysdataHibernateDao<PartDB, String>{
    private static final String COUNTS = "select count(db) from PartDB db";
    //final int batchSize = 20;//same as the JDBC batch size

    public Long getPartDBCount() {
        return findUnique(COUNTS);
    }
}
