package org.ssh.pm.query.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.query.entity.QueryItem;

@Repository("queryItemDao")
public class QueryItemDao extends HibernateDao<QueryItem, Long> {

    public Long getNewId() {
        Long newId = 0L;

        List list = this.find("select max(id) from QueryItem ");
        if (list == null || list.size() == 0) {
            return newId;
        } else {
            newId = (Long) list.get(0) + 1L;
        }

        return newId;

    }
}
