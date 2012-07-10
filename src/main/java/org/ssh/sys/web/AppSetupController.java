package org.ssh.sys.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.JsonViewUtil;
import org.ssh.pm.orm.hibernate.CustomerContextHolder;
import org.ssh.sys.service.AppSetupService;

@Controller
@RequestMapping("/appsetup")
public class AppSetupController {
    private static Logger logger = LoggerFactory.getLogger(AppSetupController.class);
    @Autowired
    private AppSetupService appSetupService;

    /**
     * 获取全部系统设置信息
     */
    @RequestMapping("/querysetupinfo")
    public void getAllAppSetup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType("default");

        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        String keyword = request.getParameter("keyword");
        if (StringUtils.isNotEmpty(keyword)) {
            PropertyFilter filter = new PropertyFilter("LIKES_setupCode_OR_setupValue_OR_descrip", keyword);
            filters.add(filter);
        }
        List<JSONObject> data = appSetupService.getAllAppSetup(filters);
        JsonViewUtil.buildJSONDataResponse(response, data, (long) data.size());
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

    /**
     * 保存系统设置信息
     */
    @RequestMapping("/savesetupinfo")
    public void saveSetupInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String jsonStr = request.getParameter("jsonStr");
            appSetupService.saveSetupInfo(jsonStr);
            map.put("success", true);
            map.put("message", "保存成功");
        } catch (Exception e) {
            logger.error("", e);
            map.put("success", false);
            map.put("message", "保存失败");
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }
}
