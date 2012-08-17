package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.AutoRunLog;

@Repository("autoRunLogDao")
public class AutoRunLogDao extends HibernateDao<AutoRunLog, Long> {

}
