package org.ssh.pm.common.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.orm.grid.GridColumnMeta;
import org.springside.modules.orm.grid.GridMetaUtil;
import org.ssh.pm.common.utils.JSONResponseUtil;

@Controller
@RequestMapping("/core")
public class CoreController {
    private static Logger logger = LoggerFactory.getLogger(CoreController.class);

    /**
     * 获取实体类Grid元数据
     */
    @RequestMapping("/get_grid_meta")
    public void getGridMeta(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        String c = "";
        try {
            //直级用 entityClassName
            //TODO js脚本
            String entityClassName = request.getParameter("entityClassName");
            if (!StringUtils.isBlank(entityClassName)) {
                c = entityClassName;
            } else {
                String entityName = request.getParameter("entityName");
                if (StringUtils.isBlank(entityName)) {
                    throw new Exception("实体类名称不能为空");
                }
                String str = request.getParameter("packageName");
                c = "org.ssh." + str + ".entity." + entityName;
            }

            List<GridColumnMeta> meta = GridMetaUtil.getGridMeta(c);

            map.put("success", true);
            map.put("data", meta);
        } catch (Exception e) {
            logger.error("getGridMeta", e);
            map.put("success", false);
            map.put("data", null);
        }

        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }
}
