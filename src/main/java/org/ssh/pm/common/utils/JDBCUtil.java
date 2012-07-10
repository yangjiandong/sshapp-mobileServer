package org.ssh.pm.common.utils;

/*
 * JDBCUtil.java
 *
 * Created on November 9, 2002, 4:27 PM
 */
//ResultSet columnRs = metaData.getColumns(null,"%", "USERTABLE", "%");
//返回USERTABLE表信息
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.type.NullableType;

@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
public class JDBCUtil {
    private static final Log logger = LogFactory.getLog(JDBCUtil.class);

    public static class Column {
        public String name;
        public int sqlType;
        public int sqlColumnLength;
        public int sqlDecimalLength;
        public boolean sqlNotNull;
        public boolean sqlReadOnly;
        public NullableType hibernateType;
        public Class javaType;
        public String sqlTypeName;
        public String defalutValue;

        public boolean equals(Object o) {
            boolean rv = false;
            if (o != null && o instanceof JDBCUtil.Column) {
                rv = (name.equals(((JDBCUtil.Column) o).name));
            }
            return rv;
        }

        public int hashCode() {
            return (name != null) ? name.hashCode() : 0;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public NullableType getHibernateType() {
            return hibernateType;
        }

        public void setHibernateType(NullableType hibernateType) {
            this.hibernateType = hibernateType;
        }

        public Class getJavaType() {
            return javaType;
        }

        public void setJavaType(Class javaType) {
            this.javaType = javaType;
        }

        public int getSqlColumnLength() {
            return sqlColumnLength;
        }

        public void setSqlColumnLength(int sqlColumnLength) {
            this.sqlColumnLength = sqlColumnLength;
        }

        public int getSqlDecimalLength() {
            return sqlDecimalLength;
        }

        public void setSqlDecimalLength(int sqlDecimalLength) {
            this.sqlDecimalLength = sqlDecimalLength;
        }

        public boolean isSqlNotNull() {
            return sqlNotNull;
        }

        public void setSqlNotNull(boolean sqlNotNull) {
            this.sqlNotNull = sqlNotNull;
        }

        public boolean isSqlReadOnly() {
            return sqlReadOnly;
        }

        public void setSqlReadOnly(boolean sqlReadOnly) {
            this.sqlReadOnly = sqlReadOnly;
        }

        public int getSqlType() {
            return sqlType;
        }

        public void setSqlType(int sqlType) {
            this.sqlType = sqlType;
        }

        public String getSqlTypeName() {
            return sqlTypeName;
        }

        public void setSqlTypeName(String sqlTypeName) {
            this.sqlTypeName = sqlTypeName;
        }

        public String getDefalutValue() {
            return defalutValue;
        }

        public void setDefalutValue(String defalutValue) {
            this.defalutValue = defalutValue;
        }
    };

    public static List getCatalogs(Connection c) throws SQLException {
        DatabaseMetaData dmd = c.getMetaData();
        ResultSet rs = null;
        try {
            rs = dmd.getCatalogs();
            List l = new LinkedList();
            while (rs.next()) {
                l.add(rs.getString(1));
            }
            return l;
        } finally {
            if (rs != null)
                rs.close();
        }
    }

    public static Map getSchemas(Connection c) throws SQLException {
        DatabaseMetaData dmd = c.getMetaData();
        ResultSet rs = null;
        try {
            rs = dmd.getSchemas();
            Map map = new HashMap();
            List l;
            while (rs.next()) {
                String schema = rs.getString(1);
                String catalog = null;
                if (rs.getMetaData().getColumnCount() > 1) {
                    catalog = rs.getString(2);
                }
                ;
                l = (List) map.get(catalog);
                if (l == null) {
                    l = new LinkedList();
                    map.put(catalog, l);
                }
                l.add(schema);
            }
            return map;
        } finally {
            if (rs != null)
                rs.close();
        }
    }

    public static List getTables(Connection c, String catalog, String schema, String tablePattern) throws SQLException {
        logger.debug("catalog='" + catalog + "'");
        logger.debug("schema='" + schema + "'");
        logger.debug("table='" + tablePattern + "'");
        DatabaseMetaData dmd = c.getMetaData();
        ResultSet rs = null;
        try {
            rs = dmd.getTables(catalog, schema, tablePattern, new String[] { "TABLE", "VIEW", "SYNONYM", "ALIAS" });
            List l = new LinkedList();
            while (rs.next()) {
                l.add(rs.getString(3));
            }
            return l;
        } finally {
            if (rs != null)
                rs.close();
        }
    }

    public static Set getForeignKeyColumns(Connection c, String catalog, String schema, String table)
            throws SQLException {
        logger.debug("catalog='" + catalog + "'");
        logger.debug("schema='" + schema + "'");
        logger.debug("table='" + table + "'");
        DatabaseMetaData dmd = c.getMetaData();
        ResultSet rs = null;
        try {
            rs = dmd.getImportedKeys(catalog, schema, table);
            HashSet columns = new HashSet();
            while (rs.next()) {
                columns.add(rs.getString(8));
            }
            return columns;
        } finally {
            if (rs != null)
                rs.close();
        }
    }

    public static List getPrimaryKeyColumns(Connection c, String catalog, String schema, String table)
            throws SQLException {
        logger.debug("catalog='" + catalog + "'");
        logger.debug("schema='" + schema + "'");
        logger.debug("table='" + table + "'");
        DatabaseMetaData dmd = c.getMetaData();
        ResultSet rs = null;
        try {
            rs = dmd.getPrimaryKeys(catalog, schema, table);

            List pkColumns = new LinkedList();
            ;
            while (rs.next()) {
                List tmp = getTableColumns(c, catalog, schema, table, rs.getString(4));
                Column pkColumn = (Column) tmp.get(0);
                pkColumns.add(pkColumn);
            }
            return pkColumns;
        } finally {
            if (rs != null)
                rs.close();
        }
    }

    public static List getTableColumns(Connection c, String catalog, String schema, String table) throws SQLException {
        return getTableColumns(c, catalog, schema, table, null);
    }

    public static List getTableColumns(Connection c, String catalog, String schema, String table, String columnPattern)
            throws SQLException {
        logger.debug("catalog='" + catalog + "'");
        logger.debug("schema='" + schema + "'");
        logger.debug("table='" + table + "'");
        logger.debug("column='" + columnPattern + "'");
        DatabaseMetaData dmd = c.getMetaData();
        ResultSet rs = null;
        try {
            rs = dmd.getColumns(catalog, schema, table, columnPattern);
            List columns = new LinkedList();
            while (rs.next()) {
                JDBCUtil.Column aCol = new JDBCUtil.Column();
                aCol.sqlTypeName = rs.getString(6);
                aCol.defalutValue = rs.getString(13);
                aCol.name = rs.getString(4);
                aCol.sqlType = rs.getShort(5);
                aCol.sqlColumnLength = rs.getInt(7);
                aCol.sqlDecimalLength = rs.getInt(9);
                aCol.sqlNotNull = ("NO".equals(rs.getString(18)));
                aCol.hibernateType = getHibernateType(aCol.sqlType, aCol.sqlColumnLength, aCol.sqlDecimalLength);
                aCol.javaType = getJavaType(aCol.sqlType, aCol.sqlColumnLength, aCol.sqlDecimalLength);
                columns.add(aCol);
            }
            return columns;
        } finally {
            if (rs != null)
                rs.close();
        }

    }

    public static NullableType getHibernateType(int sqlType, int columnSize, int decimalDigits) {
        logger.debug("sqlType=" + sqlType);
        logger.debug("columnSize=" + columnSize);
        logger.debug("decimalDigits=" + decimalDigits);
        NullableType rv = Hibernate.SERIALIZABLE;
        if (sqlType == Types.CHAR || sqlType == Types.VARCHAR) {
            rv = Hibernate.STRING;
        } else if (sqlType == Types.FLOAT || sqlType == Types.REAL) {
            rv = Hibernate.FLOAT;
        } else if (sqlType == Types.INTEGER) {
            rv = Hibernate.INTEGER;
        } else if (sqlType == Types.DOUBLE) {
            rv = Hibernate.DOUBLE;
        } else if (sqlType == Types.DATE) {
            rv = Hibernate.DATE;
        } else if (sqlType == Types.TIMESTAMP) {
            rv = Hibernate.TIMESTAMP;
        } else if (sqlType == Types.TIME) {
            rv = Hibernate.TIME;
        }
        // commented to support JDK version < 1.4
        /*
         * else if (sqlType == Types.BOOLEAN) { rv = Hibernate.BOOLEAN; }
         */
        else if (sqlType == Types.SMALLINT) {
            rv = Hibernate.SHORT;
        } else if (sqlType == Types.BIT) {
            rv = Hibernate.BYTE;
        } else if (sqlType == Types.BIGINT) {
            rv = Hibernate.LONG;
        } else if (sqlType == Types.NUMERIC || sqlType == Types.DECIMAL) {
            if (decimalDigits == 0) {
                if (columnSize == 1) {
                    rv = Hibernate.BYTE;
                } else if (columnSize < 5) {
                    rv = Hibernate.SHORT;
                } else if (columnSize < 10) {
                    rv = Hibernate.INTEGER;
                } else {
                    rv = Hibernate.LONG;
                }
            } else {
                if (columnSize < 9) {
                    rv = Hibernate.FLOAT;
                } else {
                    rv = Hibernate.DOUBLE;
                }
            }
        }
        return rv;
    }

    public static Class getJavaType(int sqlType, int columnSize, int decimalDigits) {
        logger.debug("sqlType=" + sqlType);
        logger.debug("columnSize=" + columnSize);
        logger.debug("decimalDigits=" + decimalDigits);
        Class rv = String.class;
        if (sqlType == Types.CHAR || sqlType == Types.VARCHAR) {
            rv = String.class;
        } else if (sqlType == Types.FLOAT || sqlType == Types.REAL) {
            rv = Float.class;
        } else if (sqlType == Types.INTEGER) {
            rv = Integer.class;
        } else if (sqlType == Types.DOUBLE) {
            rv = Double.class;
        } else if (sqlType == Types.DATE) {
            // rv = java.util.Date.class;
            rv = String.class;
        } else if (sqlType == Types.TIMESTAMP) {
            // rv = java.util.Date.class;
            rv = String.class;
        } else if (sqlType == Types.TIME) {
            // rv = java.util.Date.class;
            rv = String.class;
        }
        // commented to support JDK version < 1.4
        /*
         * else if (sqlType == Types.BOOLEAN) { rv = Boolean.class; }
         */
        else if (sqlType == Types.SMALLINT) {
            rv = Short.class;
        } else if (sqlType == Types.BIT) {
            // rv = Byte.class;
            rv = Integer.class;
        } else if (sqlType == Types.BIGINT) {
            rv = Long.class;
        } else if (sqlType == Types.NUMERIC || sqlType == Types.DECIMAL) {
            if (decimalDigits == 0) {
                if (columnSize == 1) {
                    // rv = Byte.class;
                    rv = Integer.class;
                } else if (columnSize < 5) {
                    rv = Short.class;
                } else if (columnSize < 10) {
                    rv = Integer.class;
                } else {
                    rv = Long.class;
                }
            } else {
                if (columnSize < 9) {
                    rv = Float.class;
                } else {
                    rv = Double.class;
                }
            }
        }
        return rv;
    }

    // 测试getMetaData
    public static void main(String[] args) throws Exception {
        Connection conn = getMsSqlConnection();
        System.out.println("Got Connection.");
        Statement st = conn.createStatement();
        try {
            st.executeUpdate("drop table survey;");
        } catch (SQLException se) {
            logger.error("表不存在:", se);
        }

        st.executeUpdate("create table survey (id int,ssname varchar(30));");
        st.executeUpdate("insert into survey (id,ssname ) values (1,'nameValue')");

        st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM t_org");

        ResultSetMetaData rsMetaData = rs.getMetaData();

        int numberOfColumns = rsMetaData.getColumnCount();
        System.out.println("resultSet MetaData column Count=" + numberOfColumns);
        List<String> columns = new ArrayList<String>();
        for (int i = 1; i <= numberOfColumns; i++) {
            // if (!first){
            // sql.append(rsMetaData.getColumnName(i) + " varchar(100)");
            // first = true;
            // }else{
            // sql.append("," +rsMetaData.getColumnName(i) + " varchar(100)");
            // }

            columns.add(rsMetaData.getColumnName(i));

            System.out.println("column MetaData ");
            System.out.println("column number " + i);
            // indicates the designated column's normal maximum width in
            // characters
            System.out.println(rsMetaData.getColumnDisplaySize(i));
            // gets the designated column's suggested title
            // for use in printouts and displays.
            System.out.println(rsMetaData.getColumnLabel(i));
            // get the designated column's name.
            System.out.println(rsMetaData.getColumnName(i));

            // get the designated column's SQL type.
            System.out.println(rsMetaData.getColumnType(i));

            // get the designated column's SQL type name.
            System.out.println(rsMetaData.getColumnTypeName(i));

            // get the designated column's class name.
            System.out.println(rsMetaData.getColumnClassName(i));

            // get the designated column's table name.
            System.out.println(rsMetaData.getTableName(i));

            // get the designated column's number of decimal digits.
            System.out.println(rsMetaData.getPrecision(i));

            // gets the designated column's number of
            // digits to right of the decimal point.
            System.out.println(rsMetaData.getScale(i));

            // indicates whether the designated column is
            // automatically numbered, thus read-only.
            System.out.println(rsMetaData.isAutoIncrement(i));

            // indicates whether the designated column is a cash value.
            System.out.println(rsMetaData.isCurrency(i));

            // indicates whether a write on the designated
            // column will succeed.
            System.out.println(rsMetaData.isWritable(i));

            // indicates whether a write on the designated
            // column will definitely succeed.
            System.out.println(rsMetaData.isDefinitelyWritable(i));

            // indicates the nullability of values
            // in the designated column.
            System.out.println(rsMetaData.isNullable(i));

            // Indicates whether the designated column
            // is definitely not writable.
            System.out.println(rsMetaData.isReadOnly(i));

            // Indicates whether a column's case matters
            // in the designated column.
            System.out.println(rsMetaData.isCaseSensitive(i));

            // Indicates whether a column's case matters
            // in the designated column.
            System.out.println(rsMetaData.isSearchable(i));

            // indicates whether values in the designated
            // column are signed numbers.
            System.out.println(rsMetaData.isSigned(i));

            // Gets the designated column's table's catalog name.
            System.out.println(rsMetaData.getCatalogName(i));

            // Gets the designated column's table's schema name.
            System.out.println(rsMetaData.getSchemaName(i));
        }

        // 简单信息
        for (int i = 0; i < numberOfColumns; i++) {
            System.out.print(rsMetaData.getColumnName(i + 1) + "  \t");
            System.out.print(rsMetaData.getColumnDisplaySize(i + 1) + "\t");
            System.out.println(rsMetaData.getColumnTypeName(i + 1));
        }

        Connection conn2 = getMsSqlConnection("jdbc:jtds:sqlserver://192.168.1.20:1433/sshapp2", "sa", "123");
        st = conn2.createStatement();
        try {
            st.executeUpdate("drop table demo_survey;");
        } catch (SQLException se) {
            logger.error("表不存在:", se);
        }

        StringBuffer sql = new StringBuffer();
        StringBuffer insert_sql = new StringBuffer();
        StringBuffer value_sql = new StringBuffer();
        sql.append("create table demo_survey(");
        insert_sql.append("insert into demo_survey(");
        boolean first = true;
        for (String oneColumn : columns) {
            if (first) {
                sql.append(oneColumn + " varchar(100)");
                insert_sql.append(oneColumn);
                first = false;
            } else {
                sql.append("," + oneColumn + " varchar(100)");
                insert_sql.append("," + oneColumn);
            }
        }
        sql.append(")");
        insert_sql.append(") ");

        try {
            st.executeUpdate(sql.toString());
        } catch (SQLException se) {
            logger.error("新建表:" + sql.toString(), se);
        }

        while (rs.next()) {
            value_sql.setLength(0);
            first = true;
            for (String oneColumn : columns) {
                if (first) {
                    value_sql.append(" values(");
                    value_sql.append("'" + rs.getString(oneColumn) + "' ");

                    first = false;
                } else {
                    value_sql.append(",'" + rs.getString(oneColumn) + "' ");

                }
            }
            value_sql.append(")");
            try {
                st.executeUpdate(insert_sql.toString() + value_sql.toString());
            } catch (SQLException se) {
                logger.error("插记录:" + insert_sql.toString() + value_sql.toString(), se);
            }
        }

        st.close();
        conn.close();
    }

    private static Connection getHSQLConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        System.out.println("Driver Loaded.");
        String url = "jdbc:hsqldb:data/tutorial";
        return DriverManager.getConnection(url, "sa", "");
    }

    public static Connection getMySqlConnection() throws SQLException, ClassNotFoundException {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/demo2s";
        String username = "oost";
        String password = "oost";

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static Connection getOracleConnection() throws SQLException, ClassNotFoundException {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "mp";
        String password = "mp2";

        Class.forName(driver); // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static Connection getMsSqlConnection() throws SQLException, ClassNotFoundException {
        String driver = "net.sourceforge.jtds.jdbc.Driver";
        String url = "jdbc:jtds:sqlserver://192.168.1.20:1433/sshapp";
        String username = "sa";
        String password = "123";

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static Connection getMsSqlConnection(String url, String user, String passwd) throws SQLException,
            ClassNotFoundException {
        String driver = "net.sourceforge.jtds.jdbc.Driver";
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, passwd);
        return conn;
    }

    //使用微软的sqljdbc4驱动
    public static Connection getSqlConnection(String url, String user, String passwd) throws SQLException,
            ClassNotFoundException {
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, passwd);
        return conn;
    }

    public static Connection getOracleConnection(String url, String user, String passwd) throws SQLException,
            ClassNotFoundException {
        String driver = "oracle.jdbc.driver.OracleDriver";
        Class.forName(driver); // load Oracle driver
        Connection conn = DriverManager.getConnection(url, user, passwd);
        return conn;
    }

    public static Connection getCacheConnection(String url, String user, String passwd) throws SQLException,
            ClassNotFoundException {
        String driver = "com.intersys.jdbc.CacheDriver";
        Class.forName(driver); // load Oracle driver
        Connection conn = DriverManager.getConnection(url, user, passwd);
        return conn;
    }
}