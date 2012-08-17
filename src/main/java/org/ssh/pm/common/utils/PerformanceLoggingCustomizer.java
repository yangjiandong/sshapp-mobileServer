package org.ssh.pm.common.utils;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.GroovyObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scripting.groovy.GroovyObjectCustomizer;

//监测groovy脚本执行
public class PerformanceLoggingCustomizer implements GroovyObjectCustomizer {
    private static Logger logger = LoggerFactory.getLogger(PerformanceLoggingCustomizer.class);

    public void customize(GroovyObject goo) {
        DelegatingMetaClass metaClass = new DelegatingMetaClass(goo.getMetaClass()) {
            @Override
            public Object invokeMethod(Object object, String method, Object[] args) {
                long start = System.currentTimeMillis();
                Object result = super.invokeMethod(object, method, args);
                long elapsed = System.currentTimeMillis() - start;
                //System.out.printf("%s took %d milliseconds on object %s\n", method, elapsed, object);
                logger.debug("{} took {} milliseconds on object {}", new Object[]{method, elapsed, object});
                return result;
            }
        };
        metaClass.initialize();
        goo.setMetaClass(metaClass);
    }
}