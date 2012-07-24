package org.ssh.pm.nurse.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.nurse.entity.DrugCheckData;

@Repository("drugCheckDao")
public class DrugCheckDao extends HibernateDao<DrugCheckData, Long> {

}
