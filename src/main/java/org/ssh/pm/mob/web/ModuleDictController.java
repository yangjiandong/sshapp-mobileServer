package org.ssh.pm.mob.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ssh.pm.common.utils.JSONResponseUtil;
import org.ssh.pm.hcost.web.UserSession;
import org.ssh.pm.mob.service.ModuleDictService;
import org.ssh.pm.orm.hibernate.CustomerContextHolder;
import org.ssh.sys.web.CommonController;

@Controller
@RequestMapping("/module_dict")
public class ModuleDictController {
    private static Logger logger = LoggerFactory.getLogger(ModuleDictController.class);

    @Autowired
    private ModuleDictService moduleDictService;

    @RequestMapping("/query_module")
    public void queryModule(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String roleId = request.getParameter("roleId");
        List<JSONObject> data = moduleDictService.queryModule(roleId);

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_module")
    public void saveRoleModule(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String roleId = request.getParameter("roleId");
        String moduleCode = request.getParameter("moduleCode");

        UserSession u = (UserSession) request.getSession().getAttribute("userSession");

        try {

            moduleDictService.saveRoleModule(roleId, moduleCode);
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/del_module")
    public void delRoleModule(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String roleId = request.getParameter("roleId");
        String moduleCode = request.getParameter("moduleCode");

        UserSession u = (UserSession) request.getSession().getAttribute("userSession");

        try {

            moduleDictService.delRoleModule(roleId, moduleCode);
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

}
