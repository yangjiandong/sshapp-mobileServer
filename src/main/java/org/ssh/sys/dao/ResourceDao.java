package org.ssh.sys.dao;

import org.springframework.stereotype.Repository;
import org.ssh.sys.entity.Resource;

@Repository("resourcesDao")
public class ResourceDao extends SysdataHibernateDao<Resource, Long> {

    public void deleteResources(String type) {
        //Map<String, List<Long>> map = Collections.singletonMap("ids", ids);
        batchExecute("DELETE Resource WHERE type=?", type);
    }
}
