package org.ssh.pm.orm.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;

//Mysql 的convert函数,现现在数据库的字符集是utf-8,如果想实现中文排序,就需要用convert(filedName using gbk) 实现,但现有的hibernate的hql不能支持此函数,
//在HQL中使用convert方法, 例如: convert(fieldName, 'gbk') , "GBK"也可以是其他字符集
//
public class MySQL5LocalDialect extends MySQL5Dialect {
    public MySQL5LocalDialect() {
        super();
        registerFunction("convert", new SQLFunctionTemplate(Hibernate.STRING, "convert(?1 using ?2)"));
    }
}
