package org.ssh.pm.nurse.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.nurse.entity.TimePoint;

@Repository("timePointDao")
public class TimePointDao extends HibernateDao<TimePoint, Long> {

}
