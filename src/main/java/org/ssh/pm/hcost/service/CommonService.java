package org.ssh.pm.hcost.service;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springside.modules.utils.ServiceException;
import org.springside.modules.utils.UtilDateTime;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.SysConfigData;
import org.ssh.pm.common.utils.AppUtil;
import org.ssh.pm.common.utils.html.table.Table;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.hcost.web.UserSession;

@Component
public class CommonService {
    protected Logger logger;

    public static UserSession getUserSession() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        UserSession u = (UserSession) session.getAttribute("userSession");
        return u;
    }

    public static void setUserSession(UserSession u) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute("userSession", u);
    }

    public Map<String, Object> getApplicationInfos() {
        SysConfigData manager = (SysConfigData) SpringContextHolder.getBean("sysConfigData");

        Map<String, Object> alls = new HashMap<String, Object>();

        alls.put("buildId", manager.getBuildId());
        alls.put("version", manager.getVersion());
        alls.put("application_name", manager.getProductName());
        alls.put("run_mode", manager.getRunMode());
        alls.put("vendor", manager.getVendor());
        alls.put(
                "copyright",
                "Copyright &copy; " + manager.getYear() + " - "
                        + StringUtils.substring(UtilDateTime.nowDateString("yyyy.MM.dd"), 0, 4) + " &nbsp;"
                        + manager.getVendor());
        alls.put("website", manager.getWebsite());
        // 当前用户
        //Subject currentUser = SecurityUtils.getSubject();
        //Session session = currentUser.getSession();
        //UserSession u = (UserSession) session.getAttribute("userSession");

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        alls.put("user_name", (u == null) ? "" : u.getAccount().getUserName());
        alls.put("resource", (u == null) ? null : u.getResource());
        alls.put("current_user", (u == null) ? null : u.getAccount());

        return alls;
    }


    @SuppressWarnings("static-access")
    public static void createMyDate(String startDate, String endDate, Integer expuid) throws SQLException {

        DataSource dataSource = (DataSource) SpringContextHolder.getBean("dataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuffer sql = new StringBuffer(512);

        sql.append("DELETE T_mydate where expuid=?");
        jdbcTemplate.update(sql.toString(), expuid);

        int lyear = Integer.parseInt(AppUtil.getDateYear(startDate));
        int lmonth = Integer.parseInt(AppUtil.getDateMonth(startDate));
        int lday = Integer.parseInt(AppUtil.getDateDay(startDate));
        Calendar startCal = new GregorianCalendar(lyear, lmonth - 1, lday);

        lyear = Integer.parseInt(AppUtil.getDateYear(endDate));
        lmonth = Integer.parseInt(AppUtil.getDateMonth(endDate));
        lday = Integer.parseInt(AppUtil.getDateDay(endDate));
        Calendar endCal = new GregorianCalendar(lyear, lmonth - 1, lday);

        long days = AppUtil.differenceInDays(endCal, startCal) + 1;
        Calendar oneDateCal = (Calendar) startCal.clone();
        for (long i = 1; i <= days; i++) {
            String oneDate = AppUtil.getDateStringFromDate(oneDateCal.getTime());

            sql.setLength(0);
            sql.append("INSERT INTO T_mydate (expuid, mydate) VALUES(?,?)");
            jdbcTemplate.update(sql.toString(), expuid, oneDate);

            oneDateCal.add(oneDateCal.DATE, 1);
        }
    }

    public static void createMyOrgs(Integer expuid) throws SQLException {

        DataSource dataSource = (DataSource) SpringContextHolder.getBean("dataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuffer sql = new StringBuffer(512);

        sql.setLength(0);
        sql.append(" DELETE FROM va_myorg where expuid = ? ");
        jdbcTemplate.update(sql.toString(), expuid);

        sql.setLength(0);
        sql.append(" insert into va_myorg (expuid,orgId,orgName,orgCode,orgCode1,orgCode2,orgCode3,orgCode4,orgCode5,orgCode6,orgCode7,orgLookupCode,fiscalCode,");
        sql.append(" hasOrgName,hasOrgCode,externals,hasOrgId,isLeaf,orgTypeCode)");
        sql.append(" select ? , a.orgId, a.orgName, a.orgCode, a.orgCode1, a.orgCode2, a.orgCode3, a.orgCode4, a.orgCode5, a.orgCode6, a.orgCode7, c.orgLookupCode, c.fiscalCode,");
        sql.append(" c.orgName AS hasOrgName, c.orgCode AS hasOrgCode, a.externals, c.orgId AS hasOrgId, a.isLeaf, a.orgTypeCode");
        sql.append(" FROM  TA_Org AS a LEFT OUTER JOIN");
        sql.append(" TA_hasOrgs AS b ON a.orgId = b.orgId LEFT OUTER JOIN");
        sql.append(" T_Org AS c ON b.hasOrgId = c.orgId");

        jdbcTemplate.update(sql.toString(), expuid);
    }

    // 人员和核算单元之间关系查询条件表达式
    public static final String DATE_CONDITION = "StartDate <= ? AND (EndDate >= ? OR EndDate IS NULL OR EndDate = '')";
    public static final String DATE_CONDITION_A = "a.StartDate <= ? AND (a.EndDate >= ? OR a.EndDate IS NULL OR a.EndDate = '')";

    //统计核算单元人数
    public int getEmployeeNumsOfOrgLike(String orgCode, String date) {
        int count = 0;

        DataSource dataSource = (DataSource) SpringContextHolder.getBean("dataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuffer sql = new StringBuffer(512);
        sql.setLength(0);
        sql.append("SELECT COUNT(1) FROM T_EmployeeOrgs ");
        sql.append("  WHERE orgIn in( select orgId from t_org where orgCode like ?) ");
        sql.append("    AND " + DATE_CONDITION);
        count = jdbcTemplate.queryForInt(sql.toString(), orgCode + "%");
        return count;
    }

    //create excel export data
    @SuppressWarnings("rawtypes")
    public List<Map> getExcelExportData(String sql) {

        DataSource dataSource = (DataSource) SpringContextHolder.getBean("dataSource");

        Connection conn = null;
        Statement st;
        ResultSet rs;
        ResultSetMetaData rsMetaData;
        List<String> columns = new ArrayList<String>();
        List<Map> resultList = new ArrayList<Map>();
        List<String> headNameList = new ArrayList<String>();

        try {
            conn = DataSourceUtils.getConnection(dataSource);

            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rsMetaData = rs.getMetaData();
            int numberOfColumns = rsMetaData.getColumnCount();

            for (int i = 1; i <= numberOfColumns; i++) {
                columns.add(rsMetaData.getColumnName(i));
            }
            Map<String, List> headMap = new HashMap<String, List>();
            Map<String, String> rowMap = null;
            int count = 0;
            while (rs.next()) {
                rowMap = new HashMap<String, String>();
                for (String oneColumn : columns) {
                    if (count == 0) {
                        headNameList.add(oneColumn);
                    }
                    rowMap.put(oneColumn, rs.getString(oneColumn));
                }
                resultList.add(rowMap);
                count++;
            }

            headMap.put(CoreConstants.HEADLIST, headNameList);
            resultList.add(headMap);

            rs.close();
        } catch (Exception se) {
            this.logger.error("sql:" + sql.toString());

            se.printStackTrace();
            logger.error("getCostReportData:", se);
            throw new ServiceException("取数发生错误");
        } finally {
            // 安全释放
            DataSourceUtils.releaseConnection(conn, dataSource);
        }

        return resultList;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void createExcelFile(HttpServletResponse response, List<Map> list) {
        Map otherColumnNames = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        int listSize = list.size();
        try {
            response.reset();
            response.setHeader("content-disposition", "attachment;filename=" + System.currentTimeMillis() + ".xls");
            response.setContentType("application/octet-stream");
            ServletOutputStream outputStream = response.getOutputStream();
            WritableWorkbook wwb = Workbook.createWorkbook(outputStream);
            String sheetName = "data";
            WritableSheet ws = wwb.createSheet(sheetName, 0);
            Label labelC;
            int labelPos = 0;
            List<String> titleList = (List<String>) otherColumnNames.get(CoreConstants.HEADLIST);
            int titleListSize = titleList.size();
            for (int i = 0; i < titleListSize; i++) {
                labelC = new Label(labelPos, 0, titleList.get(i));
                ws.addCell(labelC);
                labelPos++;
            }
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < listSize; i++) {
                map = (Map<String, String>) list.get(i);
                labelPos = 0;
                for (int j = 0; j < titleListSize; j++) {
                    Object property = null;
                    property = map.get(titleList.get(j));
                    if (property == null || property.toString().trim().equals("")) {
                        property = "";
                    }
                    labelC = new Label(labelPos, i + 1, property.toString());
                    ws.addCell(labelC);
                    labelPos++;
                }
            }
            wwb.write();
            wwb.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public static StringWriter getHtmlFromVelocityTemplate(Table table) {
        Properties p = new Properties();
        p.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.put("input.encoding", "UTF-8");
        p.put("default.contentType", "text/html; charset/=UTF-8");
        p.put("output.encoding", "UTF-8");
        //html compress
        p.put("userdirective", "com.googlecode.htmlcompressor.velocity.HtmlCompressorDirective");
        Velocity.init(p);
        Template t1 = Velocity.getTemplate("velocity.vm");

        VelocityContext ctx2 = new VelocityContext();
        ctx2.put("table", table);
        StringWriter writer = new StringWriter();
        t1.merge(ctx2, writer);
        return writer;
    }
}
