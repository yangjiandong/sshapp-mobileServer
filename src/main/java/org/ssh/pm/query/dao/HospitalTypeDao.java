package org.ssh.pm.query.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.query.entity.HospitalType;

@Repository("hospitalTypeDao")
public class HospitalTypeDao extends HibernateDao<HospitalType, Long> {

}
