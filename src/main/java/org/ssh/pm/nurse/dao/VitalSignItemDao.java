package org.ssh.pm.nurse.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.nurse.entity.VitalSignItem;

@Repository("vitalSignItemDao")
public class VitalSignItemDao extends HibernateDao<VitalSignItem, Long> {

}
