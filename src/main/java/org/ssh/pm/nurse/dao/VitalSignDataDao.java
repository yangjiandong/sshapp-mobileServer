package org.ssh.pm.nurse.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.nurse.entity.VitalSignData;

@Repository("vitalSignDataDao")
public class VitalSignDataDao extends HibernateDao<VitalSignData, Long> {

}
