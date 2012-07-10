package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.UserSessionIds;

@Repository("userSessionIdsDao")
public class UserSessionIdsDao extends SysdataHibernateDao<UserSessionIds, Long> {

}
