package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.MyResource;

@Repository("myResourceDao")
public class MyResourceDao extends SysdataHibernateDao<MyResource, Long> {

}
