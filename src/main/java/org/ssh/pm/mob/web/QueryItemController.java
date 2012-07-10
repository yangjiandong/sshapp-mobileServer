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
import org.springside.modules.utils.JsonViewUtil;
import org.ssh.pm.common.utils.JSONResponseUtil;
import org.ssh.pm.hcost.web.UserSession;
import org.ssh.pm.mob.entity.HospitalType;
import org.ssh.pm.mob.entity.QueryItem;
import org.ssh.pm.mob.entity.ItemSource;
import org.ssh.pm.mob.service.QueryItemService;
import org.ssh.pm.orm.hibernate.CustomerContextHolder;
import org.ssh.sys.web.CommonController;

@Controller
@RequestMapping("/query_item")
public class QueryItemController {
    private static Logger logger = LoggerFactory.getLogger(QueryItemController.class);

    @Autowired
    private QueryItemService queryItemService;

    @RequestMapping("/query_hospital")
    public void queryHospital(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        List<HospitalType> data = queryItemService.queryHospital();

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_hospital")
    public void saveHospital(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        String name = request.getParameter("name");

        UserSession u = (UserSession) request.getSession().getAttribute("userSession");

        try {

            String error = queryItemService.validHospital(id, name);
            if (error.length() == 0) {
                queryItemService.saveHospital(id, name);
                map.put("success", true);
                map.put("message", "");
            } else {
                map.put("success", false);
                map.put("message", error);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/delete_hospital")
    public void deleteHospital(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        try {

            queryItemService.deleteHospital(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取全部(tree使用)
     */
    @RequestMapping("/query_tree")
    public void queryItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));

            List<JSONObject> data = queryItemService.queryItem();
            map.put("success", true);
            map.put("data", data);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/query_item2")
    public void queryItem2(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        List<QueryItem> data = queryItemService.queryItem2(id);

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_item")
    public void saveItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));

        String id = request.getParameter("id");
        String itemName = request.getParameter("itemName");
        String codeLevel = request.getParameter("codeLevel");
        String parentId = request.getParameter("parentId");

        try {

            String error = queryItemService.valid(id, itemName);
            if (error.length() == 0) {
                QueryItem entity = queryItemService.saveItem(id, itemName, codeLevel, parentId);
                map.put("success", true);
                map.put("result", entity);
                map.put("message", "");
            } else {
                map.put("success", false);
                map.put("message", error);
            }

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/delete_item")
    public void deleteItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        try {

            queryItemService.deleteItem(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/query_source")
    public void querySource(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("itemId");
        List<ItemSource> data = queryItemService.querySource(id);

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_source")
    public void saveSource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));

        String id = request.getParameter("id");
        String itemId = request.getParameter("itemId");
        String spName = request.getParameter("spName");

        try {

            String error = queryItemService.validSource(id, itemId, spName);
            if (error.length() == 0) {
                queryItemService.saveSource(id, itemId, spName);
                map.put("success", true);
                map.put("message", "");
            } else {
                map.put("success", false);
                map.put("message", error);
            }

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/delete_source")
    public void deleteSource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        try {

            queryItemService.deleteSource(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }
}
