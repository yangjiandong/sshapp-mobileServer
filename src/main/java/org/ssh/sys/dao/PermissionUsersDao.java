package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.PermissionUsers;

@Repository("permissionUsersDao")
public class PermissionUsersDao extends SysdataHibernateDao<PermissionUsers, Long> {
}
