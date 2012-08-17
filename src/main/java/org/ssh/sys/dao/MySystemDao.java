package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.MySystem;

@Repository
public class MySystemDao extends SysdataHibernateDao<MySystem, Long> {
}
