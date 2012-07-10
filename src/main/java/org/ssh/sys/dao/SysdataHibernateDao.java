package org.ssh.sys.dao;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springside.modules.orm.hibernate.HibernateDao;

public class SysdataHibernateDao<T, PK extends Serializable> extends HibernateDao<T, PK> {
    @Autowired
    public void setSessionFactory(@Qualifier("sysdataSessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 取得当前Session.
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
