package org.ssh.pm.mob.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssh.pm.common.log.LogAction;
import org.ssh.pm.mob.entity.MobVersion;
import org.ssh.pm.mob.service.AutoRunSetupService;
import org.ssh.pm.mob.service.MobVersionService;
import org.ssh.sys.service.AppSetupService;
import org.ssh.sys.web.CommonController;
import org.ssh.sys.web.CommonController.Bean;

/**
 * 公开访问
 */
@Controller
@RequestMapping("/common")
public class Common3Controller {
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private AutoRunSetupService autoRunSetupService;
    @Autowired
    MobVersionService mobVersionService;
    @Autowired
    AppSetupService appSetupService;

    @RequestMapping("/init3")
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

    //返回给手机客户端信息
    @RequestMapping("/host_info")
    public String manage(Map<String, Object> map,HttpServletRequest request, HttpServletResponse response) throws Exception {
        //user_Hospital.Info
        String info = appSetupService.getValue("user_Hospital.Info");
        map.put("home", info);

        String lastVersion = mobVersionService.getLastVersion();
        map.put("lastVersion", lastVersion);

        return "host_info";
    }

    @RequestMapping("/get_last_deploy")
    public @ResponseBody
    Map<String, Object> getLastDeploy() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean su = true;
        try {
            String lastVersion = mobVersionService.getLastVersion();
            map.put("lastVersion", lastVersion);
        } catch (Exception e) {
            logger.error("", e);
            su = false;
        }
        map.put("success", su);
        return map;
    }

    @RequestMapping("downloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String lastVersion = mobVersionService.getLastVersion();
        if (org.apache.commons.lang3.StringUtils.isBlank(lastVersion))
            return;
        MobVersion mobversion = mobVersionService.findByUnique("version", lastVersion);

        String fileName = mobversion.getFileName();
        String path = null;
        Map map = System.getenv();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals("SSH_CONFIG_DIR")) //读取SSH_CONFIG_DIR的环境变量
                path = entry.getValue().toString();

        }

        path = path + "/upload";//+ upTime.getYearMonth();
        String filePath = path;//request.getParameter("filePath");

        fileName = URLDecoder.decode(fileName, "utf-8");
        filePath = URLDecoder.decode(filePath, "utf-8");

        try {
            //以流的形式下载文件
            InputStream fis = new FileInputStream(new File(filePath + "/" + fileName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            response.reset();
            response.setHeader("content-disposition", "attachment;filename="
                    + new String(fileName.getBytes("gbk"), "iso8859-1"));
            response.setContentType("application/octet-stream");
            ServletOutputStream outputStream = response.getOutputStream();

            outputStream.write(buffer);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
