package org.ssh.pm.orm.hibernate.customId;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

/**
 * @auther: XXX
 * Date: 11-5-9
 * Time: 下午11:28
 * @version: 1.0
 */

//<bean id="sessionFactory"
//    class="com.yangtao.framework.hibernate.CustomSessionFactoryBean">
//</bean>

//CREATE TABLE `sys_table_identity` (
//		  `table_name` varchar(200) DEFAULT NULL,
//		  `entity_name` varchar(200) NOT NULL DEFAULT '',
//		  `last_number` bigint(20) DEFAULT NULL,
//		  PRIMARY KEY (`entity_name`)
//		)
//or
//CREATE TABLE sys_table_identity (
//		  table_name varchar(200) DEFAULT NULL,
//		  entity_name varchar(200) NOT NULL DEFAULT '',
//		  last_number int DEFAULT NULL,
//		  PRIMARY KEY (entity_name)
//		)
//@Id
//@GeneratedValue(generator = "custgenerator")
//@GenericGenerator(strategy = "pool", name = "custgenerator")
//@Column(length = 20)
//private String id;
public class CustomSessionFactoryBean extends AnnotationSessionFactoryBean {
    protected Configuration newConfiguration() throws HibernateException {
        Configuration config = super.newConfiguration();
        //增加自己定义的主键生成器
        config.getIdentifierGeneratorFactory().register("pool", CustomIdGenerator.class);
        return config;
    }
}
