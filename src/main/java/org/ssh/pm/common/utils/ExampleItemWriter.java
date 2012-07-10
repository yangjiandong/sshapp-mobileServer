package org.ssh.pm.common.utils;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Dummy {@link ItemWriter} which only logs data it receives.
 */
public class ExampleItemWriter
//implements ItemWriter<Object>
{

    private static final Log log = LogFactory.getLog(ExampleItemWriter.class);

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @see ItemWriter#write(Object)
     */
    public void write(List<? extends Object> data) throws Exception {
        TestBean rs=null;
        for(Object line : data){
            rs=(TestBean)line;  //actual just one
        }

        final Object[] params=new Object[2];
        params[0]=rs.getName();
        params[1]=rs.getAge();
        System.out.println(ToStringBuilder.reflectionToString(rs));


        TransactionTemplate transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus arg0) {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.update("insert into BATCH_TEST(NAME,AGE) VALUES(?,?)",params);
                return null;
            }
        });

    }

}

