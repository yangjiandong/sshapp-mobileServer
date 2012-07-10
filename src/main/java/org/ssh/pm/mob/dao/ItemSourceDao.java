package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.ItemSource;

@Repository("queryItemSourceDao")
public class ItemSourceDao extends HibernateDao<ItemSource, Long> {

}
