package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.TimePoint;

@Repository("timePointDao")
public class TimePointDao extends HibernateDao<TimePoint, Long> {

}
