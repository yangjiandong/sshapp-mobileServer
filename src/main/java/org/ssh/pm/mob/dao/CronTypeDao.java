package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.CronType;

@Repository("cronTypeDao")
public class CronTypeDao extends HibernateDao<CronType, Long> {

}
