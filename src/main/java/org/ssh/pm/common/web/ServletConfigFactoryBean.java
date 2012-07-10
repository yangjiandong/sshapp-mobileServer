package org.ssh.pm.common.web;

import javax.servlet.ServletConfig;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
* <p/>
* PreSetInstanceFactoryBean
* </p>
* <p/>
* <p/>
* </p>
*
* @author <a href="mailto:weaver@apache.org">Scott T. Weaver</a>
* @version $Id$
*/
public class ServletConfigFactoryBean extends AbstractFactoryBean {

    private static ServletConfig servletConfig;

    /**
    * <p/>
    * createInstance
    * </p>
    *
    * @return
    * @throws Exception
    * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
    */
    protected final Object createInstance() throws Exception {
        verifyState();
        return servletConfig;
    }

    /**
    * <p/>
    * getObjectType
    * </p>
    *
    * @return
    * @see org.springframework.beans.factory.FactoryBean#getObjectType()
    */
    public final Class getObjectType() {
        return ServletConfig.class;
    }

    public final static void setServletConfig(ServletConfig servletConfig) {
        ServletConfigFactoryBean.servletConfig = servletConfig;
    }

    protected final void verifyState() throws IllegalStateException {
        if (servletConfig == null) {
            throw new IllegalStateException("You invoke the ServletConfigFactoryBean.setServletConfig() "
                    + "method prior to attempting to get the ServletConfig.");
        }
    }
}