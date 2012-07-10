package org.ssh.sys.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssh.pm.common.log.LogAction;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.hcost.web.UserSession;
import org.ssh.pm.mob.service.AutoRunSetupService;
import org.ssh.pm.mob.service.ModuleDictService;
import org.ssh.pm.orm.hibernate.CustomerContextHolder;
import org.ssh.sys.entity.Resource;
import org.ssh.sys.service.AccountManager;
import org.ssh.sys.service.AppSetupService;
import org.ssh.sys.service.HzService;
import org.ssh.sys.service.MigrationService;
import org.ssh.sys.service.ResourcesService;

/**
 * 公开访问
 */
@Controller
@RequestMapping("/common")
public class CommonController {
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private AppSetupService appSetupService;
    @Autowired
    private HzService hzService;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private AutoRunSetupService autoRunSetupService;
    @Autowired
    private ModuleDictService moduleDictService;

    @Autowired
    private MigrationService migrationService;

    //判断当前用户是否能维护基础字典的code和name属性,仅供前台判断权限
    @RequestMapping("/checkUserEdit")
    public @ResponseBody
    Map<String, Object> checkUserEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        boolean flag = true;

        try {
            String value = appSetupService.getValue("user_edit.by.default");
            String code = getUserCurrentPartDB(request);
            if (code.equals("default")) {
                flag = true;
            } else {
                if (value.equals(CoreConstants.ACTIVE)) {
                    flag = false;
                }
            }
            re.put("success", true);
            re.put("check", flag);
        } catch (Exception e) {
            logger.error("", e);
        }

        return re;
    }

    //判断保存和删除基础数据时是否要更新分院数据,只有系统参数中选则全院模式,且用户选择全院时,才更新分院数据
    public static boolean checkUserEdit(HttpServletRequest request) {
        boolean flag = false;
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");

        try {

            if (u == null) {
                throw new Exception("用户会话期失效");
            }
            String value = u.getUsereditbydefault();
            String code = getUserCurrentPartDB(request);

            if (value.equals(CoreConstants.ACTIVE) && code.equals("default")) {
                flag = true;
            }

        } catch (Exception e) {
            logger.error("", e);
        }
        return flag;
    }

    //取出当前用户所选分院
    public static String getUserCurrentPartDB(HttpServletRequest request) throws Exception {
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        //System.out.println(u.getPartDb().getDbcode());
        if (u == null) {
            throw new Exception("用户会话期失效");
        } else {
            if (u.getPartDb() == null)
                return "default";

        }
        return u.getPartDb().getDbcode();
        //return "default";
    }

    @RequestMapping("/init")
    public @ResponseBody
    Map<String, Object> initData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);

        List<Bean> data = new ArrayList<Bean>();

        Logger dblogger = LoggerFactory.getLogger(LogAction.DB_LOGGER_NAME);

        int width = 30;
        String title = "开始初始化系统基础数据";
        String stars = StringUtils.repeat("*", width);
        String centered = StringUtils.center(title, width, "*");
        String heading = StringUtils.join(new Object[] { stars, centered, stars }, "\n");

        logger.info(heading + "{}", new Object[] { new Date() });
        dblogger.info("开始初始化系统基础数据...");

        long start = System.currentTimeMillis();

        this.accountManager.initData();
        data.add(new Bean(true, "用户初始数据成功!", this.hzService.getClass().getName()));

        try {
            this.hzService.initDataByBatch();
            data.add(new Bean(true, "hzk初始数据成功!", ""));
        } catch (Exception se) {
            logger.error("cuco ...");
            data.add(new Bean(false, "hzk初始数据失败!" + se.toString(), ""));
        }

        try {
            this.resourcesService.initData(Resource.TYPE_SYS, "/data/resource.txt");
            data.add(new Bean(true, "资源初始数据成功!", this.resourcesService.toString()));
        } catch (Exception se) {
            logger.error("cuco ...");
            data.add(new Bean(false, "资源初始数据失败!" + se.toString(), this.resourcesService.toString()));
        }

        try {
            this.migrationService.initData();
            data.add(new Bean(true, "后台更新脚本初始成功!", this.migrationService.toString()));
        } catch (Exception se) {
            logger.error("migrationService", se);
            data.add(new Bean(false, "后台更新脚本初始失败!" + se.toString(), this.migrationService.toString()));
        }

        try {
            this.appSetupService.initData();
            data.add(new Bean(true, "系统参数初始数据成功!", ""));
        } catch (Exception se) {
            logger.error("cuco ...");
            data.add(new Bean(false, "系统参数初始数据失败!" + se.toString(), ""));
        }
        try {
            this.moduleDictService.initData();
            data.add(new Bean(true, "模块管理初始数据成功!", ""));
        } catch (Exception se) {
            logger.error("cuco ...");
            data.add(new Bean(false, "模块管理初始数据失败!" + se.toString(), ""));
        }
        try {
            this.autoRunSetupService.initData();
            data.add(new Bean(true, "执行方式初始数据成功!", ""));
        } catch (Exception se) {
            logger.error("cuco ...");
            data.add(new Bean(false, "执行方式初始数据失败!" + se.toString(), ""));
        }
        logger.info(" 执行共计:" + (System.currentTimeMillis() - start) + " ms");
        dblogger.info(" 执行共计:" + (System.currentTimeMillis() - start) + " ms");

        map.put("datas", data);
        map.put("times", (System.currentTimeMillis() - start) + " ms");
        map.put("totalCount", data.size());
        return map;
    }

    public static class Bean {
        private boolean success;
        private String msg;
        private String name;
        private String notes;

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Bean(boolean success, String msg, String info) {
            this.success = success;
            this.msg = msg;
            this.name = info;
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
    }

    //判断是否为军队医院
    @RequestMapping("/checkArmy")
    public @ResponseBody
    Map<String, Object> checkArmy(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        boolean flag = true;

        try {
            String value = appSetupService.getValue("user_hospital.is.army");
            if (value.equals("Y")) {
                flag = true;
            } else {
                flag = false;
            }
            re.put("success", true);
            re.put("check", flag);
        } catch (Exception e) {
            throw new Exception("请在系统参数中设置是否为军队医院");
        }

        return re;
    }

    //验证用户是否是系统管理员
    @RequestMapping("/checkIsAdmin")
    public @ResponseBody
    Map<String, Object> checkIsAdmin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        boolean flag = false;

        try {
            if (u == null) {
                throw new Exception("用户会话期失效");
            }
            if (u.getAccount().getId() == 1L)
                flag = true;
            re.put("success", true);
            re.put("check", flag);
        } catch (Exception e) {
            throw new Exception("验证用户是否是系统管理员失败");
        }

        return re;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void exportExecl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType("default");

        String coloums = request.getParameter("coloum");
        String datas = request.getParameter("data");
        String fileName = request.getParameter("fileName");

        String[] cm = coloums.substring(1).split("\\|");
        String[] data = datas.substring(1).split("\\|");

        ArrayList titleList = new ArrayList();
        ArrayList dataList = new ArrayList();

        for (String a : cm) {
            titleList.add(a);
        }
        for (String b : data) {

            String[] row = b.substring(1).split(",");
            ArrayList detailList = new ArrayList();
            for (String c : row) {
                detailList.add(c);
            }
            dataList.add(detailList);
        }

        try {

            fileName = new String(fileName.getBytes("GBK"), "iso8859-1");

            response.reset();
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            response.setContentType("application/octet-stream");
            ServletOutputStream outputStream = response.getOutputStream();
            WritableWorkbook wwb = Workbook.createWorkbook(outputStream);
            String sheetName = "data";
            WritableSheet ws = wwb.createSheet(sheetName, 0);
            Label labelC;
            int labelPos = 0;
            int titleListSize = titleList.size();
            for (int i = 0; i < titleListSize; i++) {
                labelC = new Label(labelPos, 0, (String) titleList.get(i));
                ws.addCell(labelC);
                labelPos++;
            }
            // Map<String, String> map = new HashMap<String, String>();

            for (int i = 0; i < dataList.size(); i++) {
                ArrayList rowList = (ArrayList) dataList.get(i);
                labelPos = 0;
                for (int j = 0; j < rowList.size(); j++) {
                    Object property = null;
                    property = rowList.get(j);
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
            throw new Exception(e.getMessage());
        }
    }

    @RequestMapping("/exportexcel")
    public void exportexcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType("default");

        response.reset();
        response.setHeader("Content-Type", "application/force-download");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.print(request.getParameter("exportContent"));

    }
}
