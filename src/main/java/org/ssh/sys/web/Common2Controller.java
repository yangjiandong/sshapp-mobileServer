package org.ssh.sys.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.Element;
import org.jfree.chart.ChartUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.charts.ChartParameters;
import org.springside.modules.charts.plugin.XradarChart;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.JsonViewUtil;
import org.springside.modules.utils.ServiceException;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.SysConfigData;
import org.ssh.pm.common.mapping.other.AccountBean;
import org.ssh.pm.common.mapping.other.Fruit;
import org.ssh.pm.common.mapping.other.ListBean;
import org.ssh.pm.common.mapping.other.MapBean;
import org.ssh.pm.common.mapping.xml.Role;
import org.ssh.pm.common.utils.AppUtil;
import org.ssh.pm.common.utils.JSONResponseUtil;
import org.ssh.pm.common.utils.JdbcPage;
import org.ssh.pm.common.utils.XMLUtil;
import org.ssh.pm.common.webservice.rs.client.UserResourceClient;
import org.ssh.pm.common.webservice.rs.dto.UserDTO;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.enums.SetupCode;
import org.ssh.pm.hcost.web.PageUtils;
import org.ssh.pm.hcost.web.UserSession;
import org.ssh.pm.orm.hibernate.CustomerContextHolder;
import org.ssh.pm.orm.hibernate.DynamicDataSource;
import org.ssh.sys.entity.AppSetup;
import org.ssh.sys.entity.Hz;
import org.ssh.sys.entity.PartDB;
import org.ssh.sys.entity.Resource;
import org.ssh.sys.entity.User;
import org.ssh.sys.service.AccountManager;
import org.ssh.sys.service.AppSetupService;
import org.ssh.sys.service.HzService;
import org.ssh.sys.service.PartDBService;
import org.ssh.sys.service.ResourcesService;
import org.ssh.sys.service.UserLoginMethodTraced;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 公开访问
 */
@Controller
@RequestMapping("/common")
public class Common2Controller {
    public static final String COOK_USERNAME = "OOUsername";
    public static final String COOK_PASSWORD = "OOPassword";

    private static Logger logger = LoggerFactory.getLogger(Common2Controller.class);
    @Autowired
    private PartDBService partDBService;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private ResourcesService resourcesService;
    @Qualifier("hzService")
    @Autowired
    private HzService hzService;
    @Autowired
    private AppSetupService appSetupService;

    //@Value("#{systemProperties.hostNames}")
    @Value("#{systemProperties['java.version']}")
    private String hostNames;

    @RequestMapping(value = "/allsubsystems")
    public ModelAndView getAllSubSystems(HttpServletRequest request, HttpServletResponse response) {
        List<Resource> allRes = this.resourcesService.loadAllModule();
        for (Resource resource : allRes) {
            resource.setUrl("#");
        }
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");

        //只有用户登录了,并且修改了密码才装载授权模块
        if (u != null && (u.getAccount().getUpdatePwd().equals(CoreConstants.USER_UPDATED_PASSWD))) {
            allRes = this.resourcesService.loadAuthModule(u);
        }
        return JsonViewUtil.getModelMap(allRes);
    }

    /**
     * 获取系统版本等信息
     */
    @RequestMapping(value = "/getsystemconfig")
    public void getSystemConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = PageUtils.getApplicationInfos();
        JSONResponseUtil.buildCustomJSONDataResponseLzay(response, map, "roleList", "partDBList");//lazy
    }

    //进入分系统
    @RequestMapping(value = "/module")
    public String gotoModuel(HttpServletRequest request, HttpServletResponse response) {
        String module = request.getParameter("parentId");
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        if (u == null) {
            return "index";
        } else {

            if (module.equals("7")) {
                return "redirect:../query";
            }

            u.setModuleId(Long.parseLong(module));
            Resource res = this.resourcesService.getResource(new Long(module));
            u.setResource(res);
            u.setModuleName(res.getText());
        }
        return "redirect:../hcost";
    }

    @RequestMapping(value = "/getalluser")
    public ModelAndView showAllUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long start = System.currentTimeMillis();

        List<User> u = accountManager.getAllUserBySp();

        logger.info(" exec sp 执行共计:" + (System.currentTimeMillis() - start) + " ms");

        return JsonViewUtil.getModelMap(u);
    }

    @RequestMapping("/index")
    public ModelAndView index(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        String userName = AppUtil.decryptBasedDes(getCookieByName(request, COOK_USERNAME));
        String passWord = getCookieByName(request, COOK_PASSWORD);
        request.setAttribute("remember_user_name", userName);
        //request.setAttribute("remember_password", passWord);

        return new ModelAndView("index");
    }

    protected static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                cookieMap.put(cookies[i].getName(), cookies[i]);
            }
        }
        return cookieMap;
    }

    public static String getCookieByName(HttpServletRequest request, String cookName) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(cookName)) {
            Cookie cookie = (Cookie) cookieMap.get(cookName);
            return cookie.getValue();
        } else {
            return "";
        }
    }

    /**
     * 检查用户登录信息的合法性
     */
    @RequestMapping("/logon")
    public void logon(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = accountManager.userLogin(request);

        if ((Boolean) map.get("success")) {
            String userName = request.getParameter("username");
            String password = request.getParameter("password");

            saveCookie(response, COOK_USERNAME, userName);

            //saveCookies(response, userName, password);
            //boolean rememberMe = ((Boolean) PropertyUtils.getSimpleProperty(
            //		form, "rememberMe")).booleanValue( );

            //mobile
            //String type = request.getParameter("type");
            //if (!org.apache.commons.lang3.StringUtils.isBlank(type)){
            //}
        }

        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    private void saveCookies(HttpServletResponse response, String username, String password) {
        Cookie userNameCookie = new Cookie(COOK_USERNAME, AppUtil.encryptBasedDes(username));
        //usernameCookie.setPath("/");
        //usernameCookie.setDomain("sshapp");
        userNameCookie.setMaxAge(60 * 60 * 24 * 30); // 30 day expiration
        response.addCookie(userNameCookie);
        Cookie passwordCookie = new Cookie(COOK_PASSWORD, AppUtil.encryptBasedDes(password));
        passwordCookie.setMaxAge(60 * 60 * 24 * 30); // 30 day expiration
        //response.addCookie(passwordCookie);
    }

    private void saveCookie(HttpServletResponse response, String cookName, String cookValue) {
        Cookie cook = new Cookie(cookName, AppUtil.encryptBasedDes(cookValue));
        //usernameCookie.setPath("/");
        //usernameCookie.setDomain("sshapp");
        cook.setMaxAge(60 * 60 * 24 * 30); // 30 day expiration
        response.addCookie(cook);
    }

    private void removeCookie(HttpServletResponse response, String cookName) {
        // expire the username cookie by setting maxAge to 0
        // (actual cookie value is irrelevant)
        Cookie cook = new Cookie(cookName, "expired");
        cook.setMaxAge(0);
        response.addCookie(cook);
    }

    private void removeCookies(HttpServletResponse response) {
        // expire the username cookie by setting maxAge to 0
        // (actual cookie value is irrelevant)
        Cookie unameCookie = new Cookie("OOUsername", "expired");
        unameCookie.setMaxAge(0);
        response.addCookie(unameCookie);

        // expire the password cookie by setting maxAge to 0
        // (actual cookie value is irrelevant)
        Cookie pwdCookie = new Cookie("OOPassword", "expired");
        pwdCookie.setMaxAge(0);
        response.addCookie(pwdCookie);
    }

    /**
     * 用户注销
     */
    @RequestMapping("/logout")
    public void logout2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = accountManager.logout(request);
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    //@RequestMapping("/logout2")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();

    }

    public static class DataSourceBean {
        private boolean success;
        private String msg;
        private String name;
        //最大连接
        private int max;
        //当前连接
        private int active;
        //空闲连接
        private int idle;

        public DataSourceBean(boolean success, String msg, String info, int max, int active, int idle) {
            this.success = success;
            this.msg = msg;
            this.name = info;
            this.max = max;
            this.active = active;
            this.idle = idle;
        }

        public boolean getSuccess() {
            return success;
        }

        public String getMsg() {
            return msg;
        }

        public String getName() {
            return name;
        }

        //
        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public int getIdle() {
            return idle;
        }

        public void setIdle(int idle) {
            this.idle = idle;
        }
    }

    @RequestMapping("/datasource")
    public @ResponseBody
    Map<String, Object> getDatabaseInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        long start = System.currentTimeMillis();

        List<DataSourceBean> data = new ArrayList<DataSourceBean>();

        DataSource ds = (DataSource) SpringContextHolder.getBean("sysDataSource");

        try {
            BasicDataSource basicDataSource = (BasicDataSource) ds;
            data.add(new DataSourceBean(true, "系统管理", "系统管理", basicDataSource.getMaxActive(), basicDataSource
                    .getNumActive(), basicDataSource.getNumIdle()));

        } catch (ClassCastException e) {
        }
        try {
            ComboPooledDataSource cpDataSource = (ComboPooledDataSource) ds;
            data.add(new DataSourceBean(true, "系统管理", "系统管理", cpDataSource.getNumConnections(), cpDataSource
                    .getNumBusyConnectionsAllUsers(), cpDataSource.getNumIdleConnections()));
        } catch (ClassCastException e) {
        } catch (SQLException se) {

        }

        //DynamicDataSource
        DataSource ds2 = (DataSource) SpringContextHolder.getBean("dataSource");
        DynamicDataSource dynamicDataSource = (DynamicDataSource) ds2;
        DataSource target = (DataSource) dynamicDataSource.returnTargetDataSource();

        try {
            BasicDataSource basicDataSource = (BasicDataSource) target;
            data.add(new DataSourceBean(true, "业务数据库", "业务数据库", basicDataSource.getMaxActive(), basicDataSource
                    .getNumActive(), basicDataSource.getNumIdle()));

        } catch (ClassCastException e) {
        }

        try {
            ComboPooledDataSource cpDataSource = (ComboPooledDataSource) target;
            data.add(new DataSourceBean(true, "业务数据库", "业务数据库", cpDataSource.getNumConnections(), cpDataSource
                    .getNumBusyConnectionsAllUsers(), cpDataSource.getNumIdleConnections()));
        } catch (ClassCastException e) {
        } catch (SQLException se) {

        }

        List<PartDB> list = partDBService.getAllDb();
        String partdb;
        for (PartDB a : list) {
            partdb = a.getDbcode();
            partdb += "DataSource";

            //配置的数据源
            DataSource ds3 = (DataSource) SpringContextHolder.getBean(partdb);

            try {

                BasicDataSource basicDataSource = (BasicDataSource) ds3;
                data.add(new DataSourceBean(true, partdb, partdb, basicDataSource.getMaxActive(), basicDataSource
                        .getNumActive(), basicDataSource.getNumIdle()));

            } catch (ClassCastException e) {
            }
            try {
                ComboPooledDataSource cpDataSource = (ComboPooledDataSource) ds3;
                data.add(new DataSourceBean(true, partdb, partdb, cpDataSource.getNumConnections(), cpDataSource
                        .getNumBusyConnectionsAllUsers(), cpDataSource.getNumIdleConnections()));

            } catch (ClassCastException e) {
            } catch (SQLException se) {

            }
        }

        map.put("datas", data);
        map.put("times", (System.currentTimeMillis() - start) + " ms");
        map.put("totalCount", data.size());
        return map;
    }

    @RequestMapping("/datatime")
    public @ResponseBody
    Map<String, Object> getDatabaseTime(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        String time = this.accountManager.getDataTime();
        map.put("datatime", time);

        return map;
    }

    //jdbc 直接输出json
    @RequestMapping("/getUserByJdbc")
    public @ResponseBody
    Map<String, Object> getUserByJdcb(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = this.accountManager.getUserByJdbc(2L);
        return resultMap;
    }

    @RequestMapping("/getUserListByJdbc")
    public @ResponseBody
    Map<String, Object> getUserListByJdcb(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> resultList = accountManager.getUserListByJdbc();

        Map<String, Object> re2 = new HashMap<String, Object>();
        re2.put("all", resultList.size());
        re2.put("records", resultList);
        return re2;
    }

    @RequestMapping("/getUserListByJdbcPage")
    public @ResponseBody
    Map<String, Object> getUserListByJdcbPage(HttpServletRequest request, HttpServletResponse response) {
        JdbcPage<User> resultList = accountManager.getUserListByJdbcPage(3, 10);

        List<User> alls = resultList.getPageItems();
        Map<String, Object> re2 = new HashMap<String, Object>();
        re2.put("all", alls.size());
        re2.put("records", alls);
        return re2;
    }

    /**
     * 获取服务器的当前日期
     */
    @RequestMapping("/get_sys_cur_datetime")
    public void getCurrentDateTime(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String time = this.accountManager.getNowString("yyyy.MM.dd HH:mm:ss");
        map.put("datetime", time);
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取服务器的当前日期
     */
    @RequestMapping("/get_sys_cur_date")
    public void getCurrentDate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String time = this.accountManager.getNowString("yyyy.MM.dd");
        map.put("datetime", time);
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/getStatuValues")
    public @ResponseBody
    Map<String, Object> getStatuValues(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        Map<String, String> flag = new HashMap<String, String>();

        flag.put("breaked", CoreConstants.STATE_BREAK + "");
        flag.put("doing", CoreConstants.STATE_DOING + "");
        flag.put("fail", CoreConstants.STATE_FAIL + "");
        flag.put("nothing", CoreConstants.STATE_NOTHING + "");
        flag.put("succeed", CoreConstants.STATE_SUCCEED + "");
        flag.put("run_succeed", CoreConstants.STATE_RUNSUCCEED + "");
        flag.put("valid_fail", CoreConstants.STATE_VALID_FAIL + "");

        re.put("success", true);
        re.put("data", flag);

        return re;
    }

    @RequestMapping("/get_hours")
    public @ResponseBody
    Map<String, Object> getHours(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        List<Map<String, String>> flag = new ArrayList<Map<String, String>>();

        String one;
        Map<String, String> o;
        for (int i = 1; i <= 24; i++) {
            one = StringUtils.leftPad(i + "", 2, "0");
            o = new HashMap<String, String>();
            o.put("name", one + "时");
            o.put("value", i + "");
            flag.add(o);
        }

        re.put("success", true);
        re.put("rows", flag);
        re.put("totalCount", flag.size());

        return re;
    }

    @RequestMapping("/get_mins")
    public @ResponseBody
    Map<String, Object> getMins(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        List<Map<String, String>> flag = new ArrayList<Map<String, String>>();

        String one;
        Map<String, String> o;
        for (int i = 1; i <= 60; i++) {
            one = StringUtils.leftPad(i + "", 2, "0");
            o = new HashMap<String, String>();
            o.put("name", one + "分");
            o.put("value", i + "");
            flag.add(o);
        }

        re.put("success", true);
        re.put("rows", flag);
        re.put("totalCount", flag.size());

        return re;
    }

    //
    //    @Autowired
    //    private GatherService gatherService;
    //
    //    @RequestMapping("/get_valid_result")
    //    public @ResponseBody
    //    Map<String, Object> getValidResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //        Integer expuid = 2;
    //
    //        List<Map<String, Object>> resultList = gatherService.getValidResult(expuid);
    //
    //        Map<String, Object> re2 = new HashMap<String, Object>();
    //        re2.put("all", resultList.size());
    //        re2.put("records", resultList);
    //        return re2;
    //    }

    @RequestMapping("/get_co")
    public @ResponseBody
    Map<String, Object> doExecuteSqlRowSetRemoteImpl(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        List<Map<String, String>> flag = this.accountManager.doExecuteSqlRowSet();

        re.put("success", true);
        re.put("rows", flag);
        re.put("totalCount", flag.size());
        return re;
    }

    //example:<img src="<c:url value="../common/chart" />"
    @RequestMapping("/chart")
    public void chart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = "w=140&g=0.25&v=100.0,98.0,100.0,93.799999999999997,99.5&c=777777|F8A036&ck=xradar&l=质量.,Mai.,Por.,Rel.,Usa.&m=100&h=110";
        XradarChart radar = new XradarChart();
        BufferedImage img = radar.generateImage(new ChartParameters(url));

        response.setContentType("image/png");
        response.getOutputStream().write(ChartUtilities.encodeAsPNG(img));
    }

    @RequestMapping(value = "/getAllHz")
    public ModelAndView showAllHz(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("test method memcahced...");
        long start = System.currentTimeMillis();

        List<Hz> books = hzService.getHzsOnMethodCache();

        logger.info(" method cache 执行共计:" + (System.currentTimeMillis() - start) + " ms");

        return JsonViewUtil.getModelMap(books);
    }

    @RequestMapping("/chart_xml")
    public @ResponseBody
    Map<String, Object> getChartDataXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();

        String q = request.getParameter("query");
        logger.debug("" + q);

        XMLUtil xml = new XMLUtil();
        Element graph = xml.addRoot("graph");
        xml.addAttribute(graph, "caption", "访问统计");
        xml.addAttribute(graph, "subCaption", "浏览器类型统计");
        xml.addAttribute(graph, "basefontsize", "12");
        xml.addAttribute(graph, "xAxisname", "浏览器类型");
        xml.addAttribute(graph, "formatNumberScale", "0");//不采用k，m来替代数据
        xml.addAttribute(graph, "decimalPrecision", "0");// 小数精确度，0为精确到个位
        xml.addAttribute(graph, "showValues", "0");// 在报表上不显示数值

        DataSource dataSource = (DataSource) SpringContextHolder.getBean("dataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuffer sql = new StringBuffer(512);
        sql.append("select b.orgName as no, sum(money)/10000 as nums from t_reportdata a, t_org b ");
        sql.append("where busdate='2010.01.01' ");
        sql.append("and a.orgid=b.orgid and b.codelevel=2 ");
        sql.append("group by b.orgName");
        //sql.append("select no, sum(money) as nums from Test_chart group by no");

        List browserList = jdbcTemplate.queryForList(sql.toString());
        for (int i = 0; i < browserList.size(); i++) {
            Map item = (HashMap) browserList.get(i);
            Element set = xml.addNode(graph, "set");
            set.addAttribute("name", (String) item.get("no"));
            set.addAttribute("value", item.get("nums").toString());
            set.addAttribute("color", Integer.toHexString((int) (Math.random() * 255 * 255 * 255)).toUpperCase());
        }
        re.put("success", true);
        re.put("xmldata", xml.getXML());

        return re;
        //String xmlStr = xml.getXML();
        //response.setContentType(ServletUtils.HTML_TYPE +";charset=UTF-8");
        //ServletUtils.setNoCacheHeader(response);

        //PrintWriter out = response.getWriter();
        //logger.debug(xmlStr);
        //out.write("<graph xaxisname='月度' yaxisname='销售' subcaption='机构本季度销售统计'><set name='1月' value='10' color='D2626' /><set name='2月' value='10' color='D64646' /><set name='3月' value='11' color='AFD8F8' /></graph>");
    }

    @RequestMapping("/chart_xml2")
    public @ResponseBody
    Map<String, Object> getChartDataXml2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();

        String q = request.getParameter("query");
        logger.debug("" + q);

        String s = "<graph xaxisname='月度' yaxisname='销售' subcaption='机构本季度销售统计'><set name='1月' value='10' color='D2626' /><set name='2月' value='10' color='D64646' /><set name='3月' value='11' color='AFD8F8' /></graph>";
        s = "";
        re.put("success", true);
        re.put("xmldata", s);

        return re;
        //String xmlStr = xml.getXML();
        //response.setContentType(ServletUtils.HTML_TYPE +";charset=UTF-8");
        //ServletUtils.setNoCacheHeader(response);

        //PrintWriter out = response.getWriter();
        //logger.debug(xmlStr);
        //out.write("<graph xaxisname='月度' yaxisname='销售' subcaption='机构本季度销售统计'><set name='1月' value='10' color='D2626' /><set name='2月' value='10' color='D64646' /><set name='3月' value='11' color='AFD8F8' /></graph>");
    }

    @RequestMapping("/chart_xml3")
    public @ResponseBody
    Map<String, Object> getChartDataXml3(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();

        String q = request.getParameter("query");
        logger.debug("" + q);

        File resourcetxt = new File(this.getClass().getResource("/data/fcf/Pyramid4.xml").getFile());

        String star[] = {};
        StringBuilder sb = new StringBuilder();

        // use google guava
        List<String> lines = Files.readLines(resourcetxt, Charsets.UTF_8);

        for (String thisLine : lines) {
            sb.append(thisLine);
        }
        re.put("success", true);
        re.put("xmldata", sb.toString());

        return re;
        //String xmlStr = xml.getXML();
        //response.setContentType(ServletUtils.HTML_TYPE +";charset=UTF-8");
        //ServletUtils.setNoCacheHeader(response);

        //PrintWriter out = response.getWriter();
        //logger.debug(xmlStr);
        //out.write("<graph xaxisname='月度' yaxisname='销售' subcaption='机构本季度销售统计'><set name='1月' value='10' color='D2626' /><set name='2月' value='10' color='D64646' /><set name='3月' value='11' color='AFD8F8' /></graph>");
    }

    @RequestMapping("/chart_xml4")
    public @ResponseBody
    Map<String, Object> getChartDataXml4(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();

        String q = request.getParameter("query");
        logger.debug("" + q);

        File resourcetxt = new File(this.getClass().getResource("/data/fcf/AngularGauge.xml").getFile());

        String star[] = {};
        StringBuilder sb = new StringBuilder();

        // use google guava
        List<String> lines = Files.readLines(resourcetxt, Charsets.UTF_8);

        for (String thisLine : lines) {
            sb.append(thisLine);
        }
        re.put("success", true);
        re.put("xmldata", sb.toString());

        return re;
        //String xmlStr = xml.getXML();
        //response.setContentType(ServletUtils.HTML_TYPE +";charset=UTF-8");
        //ServletUtils.setNoCacheHeader(response);

        //PrintWriter out = response.getWriter();
        //logger.debug(xmlStr);
        //out.write("<graph xaxisname='月度' yaxisname='销售' subcaption='机构本季度销售统计'><set name='1月' value='10' color='D2626' /><set name='2月' value='10' color='D64646' /><set name='3月' value='11' color='AFD8F8' /></graph>");
    }

    @RequestMapping("/get_rs_client")
    public @ResponseBody
    Map<String, Object> getUserResourceClient(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        final String BASE_URL = "http://localhost:8080/sshapp";
        UserResourceClient client;
        client = new UserResourceClient();
        client.setBaseUrl(BASE_URL + "/rs");

        UserDTO json = client.searchUserReturnJson("管理员");
        map.put("datatime", json);

        return map;
    }

    //http://www.cnblogs.com/hoojo/archive/2011/04/29/2032571.html
    @RequestMapping("/get_user_xml")
    public ModelAndView getUserXml(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("xmlView");
        List<Object> alls = new ArrayList<Object>();

        org.ssh.pm.common.mapping.xml.User u = new org.ssh.pm.common.mapping.xml.User();
        u.setName("实施");
        alls.add(u);
        u = new org.ssh.pm.common.mapping.xml.User();
        u.setName("涠洲岛");
        alls.add(u);

        ListBean list = new ListBean();
        list.setList(alls);
        mav.addObject("Root", list);

        return mav;
    }

    @RequestMapping("/get_user_map_xml")
    public ModelAndView doMapXMLJaxb2View() {
        //System.out.println("#################ViewController doMapXMLJaxb2View##################");
        ModelAndView mav = new ModelAndView("xmlView");

        MapBean mapBean = new MapBean();

        HashMap<String, AccountBean> map = new HashMap<String, AccountBean>();
        AccountBean bean = new AccountBean();
        bean.setAddress("北京");
        bean.setEmail("email");
        bean.setId(1);
        bean.setName("jack");
        map.put("NO1", bean);

        bean = new AccountBean();
        bean.setAddress("china");
        bean.setEmail("tom@125.com");
        bean.setId(2);
        bean.setName("tom");
        map.put("NO2", bean);

        mapBean.setMap(map);
        mav.addObject(mapBean);

        return mav;
    }

    @RequestMapping("/get_user_xml2")
    public ModelAndView getUserXml2(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("xmlView");
        org.ssh.pm.common.mapping.xml.User user = new org.ssh.pm.common.mapping.xml.User();
        user.setId(1L);
        user.setName("calvin");

        user.getRoles().add(new Role(1L, "admin"));
        user.getRoles().add(new Role(2L, "user"));
        user.getInterests().add("movie");
        user.getInterests().add("sports");

        user.getHouses().put("bj", "house1");
        user.getHouses().put("gz", "house2");
        mav.addObject(user);
        return mav;
    }

    @RequestMapping("/import_chargeitem_from_xml")
    public @ResponseBody
    Map<String, Object> importChargeItemFromXml(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        final String BASE_URL = "http://localhost:8080/sshapp";
        UserResourceClient client;
        client = new UserResourceClient();
        client.setBaseUrl(BASE_URL + "/rs");

        UserDTO json = client.searchUserReturnJson("管理员");
        map.put("datatime", json);

        return map;
    }

    @RequestMapping("/excel")
    public ModelAndView getExcel(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("excel");

        return mav;
    }

    @RequestMapping("/pdf")
    public ModelAndView getPDF(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("pdf");

        return mav;
    }

    @RequestMapping("/get_rss")
    public ModelAndView getRSSXml(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("rss");
        Fruit fruit = new Fruit("ssssw", 1000);
        mav.addObject("model", fruit);
        return mav;
    }

    @RequestMapping("/get_sys_data")
    public @ResponseBody
    Map<String, Object> getSysConfigData() {
        SysConfigData manager = (SysConfigData) SpringContextHolder.getBean("sysConfigData");
        Map<String, Object> map = new HashMap<String, Object>();

        try {

            String dir = manager.getSshConfigDir();
            File resourcetxt = new File(dir + "/data/sumupitem.txt");
            String star[] = {};
            List<String> lines = Files.readLines(resourcetxt, Charsets.UTF_8);
            int line = 1;
            for (String thisLine : lines) {
                //第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }

                Iterable<String> s = Splitter.on(",").split(thisLine);
                star = Iterables.toArray(s, String.class);

                if (star[0].trim().equals(""))
                    continue;
            }
        } catch (FileNotFoundException fe) {
            logger.error("装载数据出错,没有定义外部文件:", fe);
        } catch (Exception e) {

            logger.error("装载数据出错:", e);
            throw new ServiceException("归集项数据载入过程，服务器发生异常");
        } finally {

        }

        map.put("success", true);
        map.put("datas", manager);
        if (hostNames != null && hostNames.length() > 0) {
            map.put("java.version", this.hostNames);
        }
        return map;
    }

    @UserLoginMethodTraced(methodInfo = "测试，取时间")
    @RequestMapping("/get_mins_2")
    public @ResponseBody
    Map<String, Object> getMinsOnUserLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        List<Map<String, String>> flag = new ArrayList<Map<String, String>>();

        String one;
        Map<String, String> o;
        for (int i = 1; i <= 60; i++) {
            one = StringUtils.leftPad(i + "", 2, "0");
            o = new HashMap<String, String>();
            o.put("name", one + "分");
            o.put("value", i + "");
            flag.add(o);
        }

        re.put("success", true);
        re.put("rows", flag);
        re.put("totalCount", flag.size());

        return re;
    }

    @RequestMapping("/get_appsetupcode")
    public @ResponseBody
    Map<String, Object> getAppSetup(@RequestParam("setupCode") String code) throws Exception {
        boolean su = true;
        Map<String, Object> re = new HashMap<String, Object>();
        AppSetup a = this.appSetupService.getAppSetup(code);
        if (a == null) {
            su = false;
        } else {
            re.put("data", a.getSetupValue());
        }
        re.put("success", su);
        return re;
    }

    @RequestMapping("/get_default_page")
    public @ResponseBody
    Map<String, Object> getAppSetupDefaultPage() throws Exception {
        boolean su = true;
        Map<String, Object> re = new HashMap<String, Object>();
        AppSetup a = this.appSetupService.getAppSetup(SetupCode.DEFAULT_PAGE.value());
        if (a == null) {
            re.put("data", "20");
        } else {
            re.put("data", a.getSetupValue());
        }
        re.put("success", su);
        return re;
    }

    @RequestMapping("/all_setupinfo")
    public @ResponseBody
    Map<String, Object> getAllAppSetup2(@RequestParam("keyword") String code) throws Exception {
        CustomerContextHolder.setCustomerType("default");
        boolean su = true;
        Map<String, Object> re = new HashMap<String, Object>();

        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        if (StringUtils.isNotEmpty(code)) {
            PropertyFilter filter = new PropertyFilter("LIKES_setupCode_OR_setupValue_OR_descrip", code);
            filters.add(filter);
        }
        List<JSONObject> data = appSetupService.getAllAppSetup(filters);
        re.put("data", data);
        re.put("success", su);

        return re;
    }

    @RequestMapping("/get_sys_os_info")
    public @ResponseBody
    Map<String, Object> querySysOsInfo() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> re = new HashMap<String, Object>();
        boolean su = true;
        try {
            Properties props = System.getProperties(); //获得系统属性集
            re.put("osName", props.getProperty("os.name")); //操作系统名称
            re.put("osArch", props.getProperty("os.arch")); //操作系统构架
            re.put("osVersion", props.getProperty("os.version")); //操作系统构架
            re.put("javaVersion", props.getProperty("java.version"));

            TimeZone tz = Calendar.getInstance().getTimeZone();
            re.put("timeZone", tz.getDisplayName());
            re.put("timeId", tz.getID());

        } catch (Exception e) {
            logger.error("", e);
            su = false;
        }
        map.put("data", re);
        map.put("success", su);

        return map;
    }

    //返回给手机客户端信息
    @RequestMapping("/host_info")
    public String manage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "host_info";
    }
}
