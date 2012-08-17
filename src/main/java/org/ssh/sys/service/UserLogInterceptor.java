package org.ssh.sys.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.sys.entity.UserLog;

/**
 *
 * 拦截器,用于记录用户操作.
 *
 */

public class UserLogInterceptor implements MethodInterceptor, InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(UserLogInterceptor.class);

    //
    //No Hibernate Session bound to thread, and configuration does not allow creation of non-transactional one here
    //private UserLogService userLogService;

    @Autowired
    private AccountManager accountManager;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        //String targetName = invocation.getThis().getClass().getName();
        //String methodName = invocation.getMethod().getName();
        //Object[] arguments = invocation.getArguments();

        Object result = invocation.proceed();

        //方法参数类型，转换成简单类型
        Class[] params = invocation.getMethod().getParameterTypes();
        String[] simpleParams = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            simpleParams[i] = params[i].getSimpleName();
        }

        String infos = "Takes:  [" + invocation.getThis().getClass().getName() + "." + invocation.getMethod().getName()
                + "(" + StringUtils.join(simpleParams, ",") + ")] ";
        if (invocation.getMethod().isAnnotationPresent(UserLoginMethodTraced.class)) {
            UserLoginMethodTraced u = invocation.getMethod().getAnnotation(UserLoginMethodTraced.class);
            infos = u.methodInfo();
        }

        UserLogService userLogService = SpringContextHolder.getBean("userLogService");

        UserLog uLog = new UserLog();
        uLog.setUserId(99L);
        uLog.setCreateTime(accountManager.getNowString());
        uLog.setEvents(infos);
        //uLog.setNetIp("xx");
        userLogService.save(uLog);

        logger.debug(infos);

        return result;

    }

    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
    }
}