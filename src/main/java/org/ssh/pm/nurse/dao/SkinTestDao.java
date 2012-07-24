package org.ssh.pm.nurse.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.nurse.entity.SkinTest;

@Repository("skinTestDao")
public class SkinTestDao extends HibernateDao<SkinTest, Long> {

}
