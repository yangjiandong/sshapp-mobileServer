package org.ssh.pm.orm.hibernate.customId;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class IdDao {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private DataSource dataSource;

    private String identityTable = "sys_table_identity";

    private long begin_num = 0;//开始值
    /**
     * 根据实体名称获取最新的滚动号，并且自动按照递增长度设置最后的滚动号
     *
     * @param entityName
     * @param size
     * @return
     */
    public long getLastNumber(String entityName, int size) {
        //从数据库取得记录
        Connection connection = null;
        long _latestNumber = begin_num;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityName", entityName);
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            Long lastestNumber = getLastestNumber(connection, params);
            if (lastestNumber != null) {
                _latestNumber = lastestNumber;
            }
            //如果查询不到任何结果就插入一条新的记录
            else {
                insertNewRecord(connection, params);
            }
            params.put("newNumber", _latestNumber + size);
            //更新最后的记录
            updateRecord(connection, params);
            //提交事务
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    log.error(e.getMessage(), e1);
                }
            }
            log.error(e.getMessage(), e);
        } finally {
            clearConnection(connection);
        }
        return _latestNumber;
    }

    /**
     * 获取当前表的最后主键序列
     *
     * @param connection
     * @return
     * @throws java.sql.SQLException
     */
    private Long getLastestNumber(Connection connection, Map<String, Object> params) throws SQLException {
        String selectSql = "select last_number from " + identityTable + " where entity_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setString(1, params.get("entityName").toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        try{
        if (resultSet.first()) {
            return resultSet.getLong("last_number");
        }}catch(SQLException se){
            //jtds 处理有问题，有可能没记录
            log.error(se);
        }
        return null;
    }

    /**
     * 插入一条表的主键记录
     *
     * @param connection
     * @throws java.sql.SQLException
     */
    private void insertNewRecord(Connection connection, Map<String, Object> params) throws SQLException {
        String insertSql = "insert into "
                + identityTable + "(table_name, entity_name, last_number)" +
                "values(?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, params.get("tableName") == null ? null : params.get("tableName").toString());
        preparedStatement.setString(2, params.get("entityName").toString());
        preparedStatement.setLong(3, begin_num);
        preparedStatement.execute();
    }

    /**
     * 更新一条记录
     *
     * @param connection
     * @param params
     * @throws java.sql.SQLException
     */
    private void updateRecord(Connection connection, Map<String, Object> params) throws SQLException {
        //更新最后的记录
        String updateSql = "update " + identityTable + " set last_number = ? where entity_name=?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
        preparedStatement.setLong(1, Long.valueOf(params.get("newNumber").toString()));
        preparedStatement.setString(2, params.get("entityName").toString());
        preparedStatement.execute();
    }

    /**
     * 释放连接资源
     *
     * @param connection
     */
    public void clearConnection(Connection connection) {
        //关闭resultset
//		if (resultSet != null) {
//			try {
//				resultSet.close();
//			} catch (SQLException e) {
//				resultSet = null;
//				e.printStackTrace();
//			}
//		}
//		//关闭preparedStatement
//		if (preparedStatement != null) {
//			try {
//				preparedStatement.close();
//			} catch (SQLException e) {
//				preparedStatement = null;
//				log.error(e.getMessage(), e);
//			}
//		}
        //关闭连接
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                connection = null;
                log.error(e.getMessage(), e);
            }
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getIdentityTable() {
        return identityTable;
    }

    public void setIdentityTable(String identityTable) {
        this.identityTable = identityTable;
    }
}
