package org.ssh.pm.common.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.TestTable;

@Component
public class TestTableDao extends HibernateDao<TestTable, String> {
    private static final String COUNTS = "select count(b) from " +TestTable.class.getName() + " b";

    /**
     * count .
     */
    public Long getCategoryCount() {
        return findUnique(COUNTS);
    }
}
