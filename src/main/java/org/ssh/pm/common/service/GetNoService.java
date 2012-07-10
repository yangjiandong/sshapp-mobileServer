package org.ssh.pm.common.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.pm.common.dao.GetNoDao;
import org.ssh.pm.common.entity.GetNo;
import org.ssh.pm.orm.hibernate.EntityService;


@Service("getNoService")
@Transactional
public class GetNoService extends EntityService<GetNo, String> implements Serializable {
    private static final long serialVersionUID = -2974344463929082172L;

    private GetNoDao getNoDao;

    @Autowired
    public void setGetNoDao(GetNoDao getNoDao) {
        this.getNoDao = getNoDao;
    }

    public GetNoDao getEntityDao() {
        return getNoDao;
    }
}