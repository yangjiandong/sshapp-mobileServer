package org.ssh.pm.orm.hibernate.customId;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class CustomIdGenerator implements IdentifierGenerator, Configurable {
    private Log log = LogFactory.getLog(getClass());

    private static final Map<String, IdPool> ID_MAP = new ConcurrentHashMap<String, IdPool>();

    private int poosize;

    /**
     * Generate a new identifier.
     *
     * @param session
     * @param object  the entity or toplevel collection for which the id is being generated
     * @return a new identifier
     * @throws org.hibernate.HibernateException
     *
     */
    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        String entityName = object.getClass().getName();
        if (log.isDebugEnabled()) {
            log.debug("需要获取主键的实体名:[" + entityName + "]");
        }
        if (ID_MAP.get(entityName) == null) {
            initalize(entityName);
        }
        String id = ID_MAP.get(entityName).getNextId();
        if (log.isDebugEnabled()) {
            log.debug("线程:[" + Thread.currentThread().getName() + "] " +
                    "实体:[" + entityName + "] 生成的主键为:[" + id + "]");
        }
        return id;
    }

    /**
     * 初始化对象的主键
     *
     * @param entityName 对象名称
     */
    private void initalize(String entityName) {
        IdPool pool = new IdPool(entityName, poosize);
        ID_MAP.put(entityName, pool);
    }

    /**
     * Configure this instance, given the value of parameters
     * specified by the user as <tt>&lt;param&gt;</tt> elements.
     * This method is called just once, following instantiation.
     *
     * @param params param values, keyed by parameter name
     */
    @Override
    public void configure(Type type, Properties params, Dialect d) throws MappingException {
        Object poosizeObj = params.get("poolsize");
        if (poosizeObj != null) {
            poosize = Integer.parseInt(poosizeObj.toString());
        } else {
            poosize = 50;
        }
    }

    public int getPoosize() {
        return poosize;
    }

    public void setPoosize(int poosize) {
        this.poosize = poosize;
    }
}
