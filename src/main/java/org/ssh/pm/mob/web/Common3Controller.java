package org.ssh.pm.mob.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.ssh.pm.mob.service.AutoRunSetupService;
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
}
