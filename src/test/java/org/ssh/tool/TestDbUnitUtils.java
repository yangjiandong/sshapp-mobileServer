package org.ssh.tool;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springside.modules.utils.PropertiesUtils;

public class TestDbUnitUtils {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    /**
     * 清除并插入XML数据文件到H2数据库.
     *
     * XML数据文件中涉及的表在插入数据前会先进行清除.
     *
     * @param xmlFilePaths 符合Spring Resource路径格式的文件列表.
     */
	public static void loadData(DataSource h2DataSource, String... xmlFilePaths) throws Exception {
        execute(DatabaseOperation.CLEAN_INSERT, h2DataSource, xmlFilePaths);
    }

    /**
     * 插入XML数据文件到H2数据库.
     */
    public static void appendData(DataSource h2DataSource, String... xmlFilePaths) throws Exception {
        execute(DatabaseOperation.INSERT, h2DataSource, xmlFilePaths);
    }

    /**
     * 在H2数据库中删除XML数据文件中涉及的表的数据.
     */
    public static void removeData(DataSource h2DataSource, String... xmlFilePaths) throws Exception {
        execute(DatabaseOperation.DELETE_ALL, h2DataSource, xmlFilePaths);
    }

    /**
     * 按DBUnit Operation执行XML数据文件的数据.
     *
     * @param xmlFilePaths 符合Spring Resource路径格式的文件列表.
     */
    private static void execute(DatabaseOperation operation, DataSource dataSource, String... xmlFilePaths)
            throws DatabaseUnitException, SQLException {

        Connection con = DataSourceUtils.getConnection(dataSource);
        IDatabaseConnection connection = new DatabaseConnection(con);

        //QueryDataSet partialDataSet = new QueryDataSet(connection);
        //Specify the SQL to run to retrieve the data
        //partialDataSet.addTable("person", " SELECT * FROM person WHERE name='Test name' ");
        //partialDataSet.addTable("address", " SELECT * FROM address WHERE addressid=1 ");

        //Specify the location of the flat file(XML)
        //FlatXmlWriter datasetWriter = new FlatXmlWriter(new FileOutputStream("temp.xml"));

        //Export the data
        //datasetWriter.write( partialDataSet );

        //IDatabaseConnection connection = new H2Connection(h2DataSource.getConnection(), "");
        for (String xmlPath : xmlFilePaths) {
            try {
                InputStream input = resourceLoader.getResource(xmlPath).getInputStream();
                IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build(input);
                operation.execute(connection, dataSet);
            } catch (Exception e) {
                e.printStackTrace();
                //logger.warn(xmlPath + " file not found", e);
            }
        }
    }
}
