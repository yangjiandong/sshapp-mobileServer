package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.HospitalType;

@Repository("hospitalTypeDao")
public class HospitalTypeDao extends HibernateDao<HospitalType, Long> {

}
