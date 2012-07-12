package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.VitalSignItem;

@Repository("vitalSignItemDao")
public class VitalSignItemDao extends HibernateDao<VitalSignItem, Long> {

}
