package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.RoleResource;

/**
 * 角色-资源对象的泛型Hibernate Dao.
 *
 * @author calvin
 */
@Repository("roleResourceDao")
public class RoleResourceDao extends SysdataHibernateDao<RoleResource, Long> {
}
