package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.Migration;

@Repository
public class MigrationDao extends SysdataHibernateDao<Migration, Long> {
    public Long getCount() {
        return findUnique("select count(db) from Migration db where status=?", "0");
    }
}
