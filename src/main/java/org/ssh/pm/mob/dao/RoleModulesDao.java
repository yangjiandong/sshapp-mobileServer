package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.RoleModules;

@Repository("roleModulesDao")
public class RoleModulesDao extends HibernateDao<RoleModules, Long> {

}
