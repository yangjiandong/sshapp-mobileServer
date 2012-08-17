import org.ssh.pm.imports.def.*;
import org.ssh.pm.imports.ImportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import groovy.sql.*;

//住院药品
class ImportInDrug implements BeforeImportProcess {
  static Logger logger = LoggerFactory.getLogger("org.ssh.pm.script.ImportInDrug");

  void processImport(String startDate, String endDate) throws ImportException{
     println "it ok groovy";
     logger.debug("住院药品-processImport");
  }

  void processOneDayImport(String oneDay) throws ImportException{
        logger.debug("住院药品-processOneDayImport-"+oneDay);

        //r(oneDay);
   }

  void r(String oneDay) throws ImportException{
      def startDate  = '2010.01.01';
      def endDate  = '2010.01.31';
      def records = 1000;
      def fetchSize = 100;

      try{

      def his = Sql.newInstance(
        "jdbc:jtds:sqlserver://192.168.1.148:1433/hcostdata-sl",
        "sa",
        "123",
        "net.sourceforge.jtds.jdbc.Driver"          );

      def hcost = Sql.newInstance(
        "jdbc:jtds:sqlserver://192.168.1.148:1433/hcostdata-sl1",
        "sa",
        "123",
        "net.sourceforge.jtds.jdbc.Driver"          );
      //hcost.connection.autoCommit = false;

      def start = System.currentTimeMillis();

      hcost.execute("delete from g_indrugincome2");

      //def sql = "insert into g_indrugincome2(busdate) values ($startDate)"
      //hcost.execute(sql);

      def sql = """
          insert into g_indrugincome2(busdate,specialDeptCode,OrgCode,OrgCode1,orgCode2,orgcode3,orgcode4,opsdeptCode,employeeid)
                            values(?,?,?,?,?,?,?,?,?)"""

      def hcostConn = hcost.getConnection()
      def hcostStmt = hcostConn.prepareStatement(sql);
      hcostConn.setAutoCommit(false)

      def alls=[];
      def index=0;
      his.query("select * from g_indrugincome where busdate>=${startDate} and busdate<=${endDate}"){
        rs ->
          rs.setFetchSize(fetchSize);
          while (rs.next()) {

              hcostStmt.setString(1, rs.getString("busDate"));
              hcostStmt.setString(2, rs.getString("specialDeptCode"));
              hcostStmt.setString(3, rs.getString("OrgCode"));
              hcostStmt.setString(4, rs.getString("OrgCode1"));
              hcostStmt.setString(5, rs.getString("orgCode2"));
              hcostStmt.setString(6, rs.getString("orgcode3"));
              hcostStmt.setString(7, rs.getString("orgcode4"));
              hcostStmt.setString(8, rs.getString("opsdeptCode"));
              hcostStmt.setString(9, rs.getString("employeeid"));
              //hcostStmt.executeUpdate();
              hcostStmt.addBatch();
              ////logger.info("importIndrug2 query:" + rs.getString("busDate") + "---" );
              index ++;

              if((index == records)){
                  //logger.info("importIndrug3 done:" + index + " records," + (System.currentTimeMillis() - start) + " ms");
                  hcostStmt.executeBatch();
                  hcostConn.commit();
                  index =0;
              }
              //logger.info("importIndrug3 : records," + rs.getString("busDate") + rs.getString("orgcode"));

          }
      }
      hcostStmt.executeBatch();
      hcostConn.commit();

      logger.info("住院药品-processOneDayImport-" + index + " records," + (System.currentTimeMillis() - start) + " ms");

      }catch(Exception dd){
          logger.error("住院药品-processOneDayImport-" + dd);
          System.out.println(dd);
          throw new ImportException(dd);
      }
  }

  String getInfo() {
    logger.debug("住院药品-getInfo");
    return "住院药品-getInfo";
  }
}

//new File (filename).eachLine { line ->
//  .. some processing here ..
//  db.execute "insert ..."
//}

//def updateCounts = sql.withBatch { stmt ->
//     stmt.addBatch("insert into TABLENAME ...")
//     stmt.addBatch("insert into TABLENAME ...")
//     stmt.addBatch("insert into TABLENAME ...")
//     ...
// }
//
//Performs the closure within a batch using a cached connection. The closure will be called with a single argument; the statement associated with this batch. Use it like this for batchSize of 20:
//
// def updateCounts = sql.withBatch(20) { stmt ->
//     stmt.addBatch("insert into TABLENAME ...")
//     stmt.addBatch("insert into TABLENAME ...")
//     stmt.addBatch("insert into TABLENAME ...")
//     ...
// }
