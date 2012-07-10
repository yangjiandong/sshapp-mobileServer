package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.Role;

/**
 * 角色对象的泛型Hibernate Dao.
 *
 * @author calvin
 */
@Repository
public class RoleDao extends SysdataHibernateDao<Role, Long> {
}
