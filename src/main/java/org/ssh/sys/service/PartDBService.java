package org.ssh.sys.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.sys.dao.PartDBDao;
import org.ssh.sys.entity.PartDB;

@Service
public class PartDBService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PartDBDao partDBDao;

    public Long getPartDBCount() {
        return partDBDao.getPartDBCount();
    }

    /**
     * 获取全部数据库
     */
    @Transactional(readOnly = true)
    public List<PartDB> getAllDb() {
        return partDBDao.find("from PartDB");
    }

    /**
     * 根据dbcode获取数据库
     */
    @Transactional(readOnly = true)
    public PartDB getDBByCode(String dbcode) {
        return partDBDao.findUniqueBy("dbcode", dbcode);
    }

    @Transactional
    public void initData() {
        if (partDBDao.getPartDBCount() != 0)
            return;

        PartDB p = new PartDB();
        p.setDbcode("default");//默认
        p.setDbname("全院");
        this.partDBDao.save(p);
    }
}
