package org.ssh.pm.mob.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.mob.entity.ModuleDict;

@Repository("moduleDictDao")
public class ModuleDictDao extends HibernateDao<ModuleDict, Long> {

}
