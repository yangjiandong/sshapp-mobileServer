package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.MeasureType;

@Repository("measureTypeDao")
public class MeasureTypeDao extends HibernateDao<MeasureType, Long> {

}
