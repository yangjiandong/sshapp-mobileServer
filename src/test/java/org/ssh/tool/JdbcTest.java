package org.ssh.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcTest {

    //java程序跨服务器跨数据库批量导入导出百万级数据
    public static void mains(String args[]) throws Exception {
        ResultSet rs = null;
        Statement s1 = null;
        Statement s2 = null;
        Connection cn1 = null;
        Connection cn2 = null;
        long startTime = 0;//开始时间
        long endTime = 0; //结束时间
        int count = 1; //计数
        int onerun = 0; //执行的最大数
        int datanum = 0; //总条数
        int num = 0; //取整最大数据

        try {
            startTime = System.currentTimeMillis();
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            cn1 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.16:1521:oracleDb", "test", "test2012");
            cn2 = DriverManager
                    .getConnection("jdbc:sqlserver://192.168.1.18:1433;databaseName=dbsql;user=sa;password=db2012");
            cn1.setAutoCommit(false);//插入oracle数据库时使用事务批量提交
            if (cn2 != null) {
                s1 = cn1.createStatement();
                s2 = cn2.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rs = s2.executeQuery("select count(vcBusiFlowNo) from tb_His2_Ask_Pay_Fee");
                if (rs.next()) {//先查询sqlserver数据源表，获取总数据条数
                    datanum = rs.getInt(1);
                }
                System.out.println("总记录数：" + datanum + "条");
                onerun = 10000;//执行的最大数
                num = datanum / onerun * onerun;//计算出总条数符合每批10000的数量是多少
                s2.setMaxRows(datanum);
                s2.setFetchSize(onerun);//每批执行的数据条数
                rs = s2.executeQuery("select nSerialNo,vcBusiFlowNo,vcBusiTypeID,vcBusiNumber,vcUserName,vcUserAddress,vcUserLinkTel,nPayFee,vcBusiMonth,vcComments,dOperDate,vcOperID,vcDealerID,vcAreaCode,vcDealerGradeID from tb_His2_Ask_Pay_Fee");
                while (rs.next()) {
                    s1.addBatch("insert into tb_His2_Ask_Pay_Fee(nSerialNo,vcBusiFlowNo,vcBusiTypeID,vcBusiNumber,vcUserName,vcUserAddress,vcUserLinkTel,nPayFee,vcBusiMonth,vcComments,dOperDate,vcOperID,vcDealerID,vcAreaCode,vcDealerGradeID) values('"
                            + rs.getString("nSerialNo")
                            + "','"
                            + rs.getString("vcBusiFlowNo")
                            + "','"
                            + rs.getString("vcBusiTypeID")
                            + "','"
                            + rs.getString("vcBusiNumber")
                            + "','"
                            + rs.getString("vcUserName")
                            + "','"
                            + rs.getString("vcUserAddress")
                            + "','"
                            + rs.getString("vcUserLinkTel")
                            + "','"
                            + rs.getString("nPayFee")
                            + "','"
                            + rs.getString("vcBusiMonth")
                            + "','"
                            + rs.getString("vcComments")
                            + "','"
                            + rs.getString("dOperDate")
                            + "','"
                            + rs.getString("vcOperID")
                            + "','"
                            + rs.getString("vcDealerID")
                            + "','"
                            + rs.getString("vcAreaCode")
                            + "','"
                            + rs.getString("vcDealerGradeID") + "')");
                    if (count > num) {//10000取整后剩余的小数据量就顺序插入
                        s1.executeBatch();
                        cn1.commit();
                        s1.clearBatch();
                    } else {//数据够批次的就按批量插入
                        if (count % onerun == 0) {//10000条一批插入
                            s1.executeBatch();
                            cn1.commit();
                            s1.clearBatch();
                        }
                    }
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            s2.close();
            s1.close();
            cn2.close();
            cn1.close();
        }
        endTime = System.currentTimeMillis();
        System.out.println("成功移植数据：" + (count - 1) + "条，耗时" + (endTime - startTime) / 1000 + "秒");
    }
}
