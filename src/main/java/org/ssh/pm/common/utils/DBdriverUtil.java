package org.ssh.pm.common.utils;

public class DBdriverUtil {

    /**
     * MYSQL JDBC DRIVER
     */
    public static final String MYSQL_DRIVER = "MySQL";

    /**
     * POSTGRESQL JDBC DRIVER
     */
    public static final String POSTGRESQL_DRIVER = "PostgreSQL";

    /**
     * INFORMIX JDBC DERIVER
     */
    public static final String INFORMIX_DRIVER = "Informix Dynamic Server";

    /**
     * MSSQL 2000 JDBC DRIVER
     */
    public static final String MSSQL2000_DRIVER = "Microsoft SQL Server";

    /**
     * MSSQL 7 JDBC DRIVER
     */
    public static final String MSSQL7_DRIVER = "Microsoft SQL Server";

    /**
     * Hypersonic Database
     */
    public static final String HYSQL_DRIVER = "org.hsqldb.jdbcDriver";

    /**
     * ORACLE JDBC DRIVER
     */
    public static final String ORACLE_DRIVER = "Oracle";

    /**
     * db2 7.2 jdbc type 3
     */
    public static final String DB2_TYPE3_DRIVER = "DB2/";

    /**
    * 判断数据库是否为 isMySQ
    */
    public static boolean isMySQL(String driver) {
        boolean isMySQL = false;
        try {
            if (driver.equals(MYSQL_DRIVER)) {
                isMySQL = true;
            }

        } catch (Exception e) {
            isMySQL = false;
        }

        return isMySQL;
    }

    public static boolean isMSSQL(String driver) {
        boolean isMsSQL = false;
        try {
            if (driver.equals(MSSQL2000_DRIVER) || driver.equals(MSSQL7_DRIVER)) {
                isMsSQL = true;
            }

        } catch (Exception e) {
            isMsSQL = false;
        }

        return isMsSQL;
    }

    public static boolean isOracle(String driver) {
        boolean isOracle = false;
        try {
            if (driver.equals(ORACLE_DRIVER)) {
                isOracle = true;
            }

        } catch (Exception e) {
            isOracle = false;
        }

        return isOracle;
    }

    public static boolean isDB2(String driver) {
        boolean isOracle = false;
        try {
            if (driver.equals(DB2_TYPE3_DRIVER)) {
                isOracle = true;
            }

        } catch (Exception e) {
            isOracle = false;
        }

        return isOracle;
    }
}
