package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.Permissions;

@Repository("permissionsDao")
public class PermissionsDao extends SysdataHibernateDao<Permissions, Long> {
}
