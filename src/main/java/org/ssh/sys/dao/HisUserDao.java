package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.his.HisUser;

@Repository
public class HisUserDao extends SysdataHibernateDao<HisUser, String> {
}
