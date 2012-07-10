package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.QueryItem;

@Repository("queryItemDao")
public class QueryItemDao extends HibernateDao<QueryItem, Long> {

}
