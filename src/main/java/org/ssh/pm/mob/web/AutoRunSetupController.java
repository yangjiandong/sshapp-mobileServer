package org.ssh.pm.mob.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ssh.pm.common.utils.JSONResponseUtil;
import org.ssh.pm.mob.entity.CronType;
import org.ssh.pm.mob.entity.ItemSource;
import org.ssh.pm.mob.service.AutoRunSetupService;
import org.ssh.pm.orm.hibernate.CustomerContextHolder;
import org.ssh.sys.web.CommonController;

@Controller
@RequestMapping("/autorun_setup")
public class AutoRunSetupController {
    private static Logger logger = LoggerFactory.getLogger(AutoRunSetupController.class);

    @Autowired
    private AutoRunSetupService autoRunSetupService;

    @RequestMapping("/query")
    public void query(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String typeCode = request.getParameter("typeCode");
        List<ItemSource> data = autoRunSetupService.query(typeCode);

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/get_cron")
    public void getCron(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));

        List<CronType> data = autoRunSetupService.getCron();

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_cron")
    public void saveCron(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        String cronId = request.getParameter("cronId");
        try {

            autoRunSetupService.saveCron(id, cronId);
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

}
