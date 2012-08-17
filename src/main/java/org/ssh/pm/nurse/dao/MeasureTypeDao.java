package org.ssh.pm.nurse.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.nurse.entity.MeasureType;

@Repository("measureTypeDao")
public class MeasureTypeDao extends HibernateDao<MeasureType, Long> {

}
