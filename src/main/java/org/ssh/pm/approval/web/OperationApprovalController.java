package org.ssh.pm.approval.web;

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
import org.ssh.pm.approval.entity.OperationApprovalData;
import org.ssh.pm.approval.service.OperationApprovalService;
import org.ssh.pm.mob.MobConstants;
import org.ssh.pm.mob.MobUtil;

//手术审核
@Controller
@RequestMapping("/oper_approval")
public class OperationApprovalController {
    private static Logger logger = LoggerFactory.getLogger(OperationApprovalController.class);

    @Autowired
    private OperationApprovalService operationApprovalService;

    @RequestMapping("/commit_his")
    public @ResponseBody
    Map<String, Object> commitHis(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            Map<String, Object> map1 = new HashMap<String, Object>();

            MobUtil.execSp(MobConstants.MOB_SPNAME_COMMIT_OPERAPPROVAL, map1);

            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/save_oper_approval")
    public @ResponseBody
    Map<String, Object> saveOperationApproval(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        String userId = request.getParameter("userId");
        String id = request.getParameter("id");
        String result = request.getParameter("result");
        String note = request.getParameter("note");

        try {

            if (StringUtils.isBlank(userId)) {
                map.put("success", false);
                map.put("message", "用户编号不存在");
            } else {
                if (StringUtils.isBlank(id)) {
                    map.put("success", false);
                    map.put("message", "审批编号不存在");
                } else {
                    operationApprovalService.saveOperationApproval(userId, id, result, note);

                    map.put("success", true);
                    map.put("message", "");
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/get_all")
    public @ResponseBody
    Map<String, Object> getAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        String userId = request.getParameter("userId");
        try {
            List<OperationApprovalData> list = operationApprovalService.getAll(userId);
            if (list != null && list.size() > 0) {
                map.put("success", true);
                map.put("message", "");
                map.put("operationApprovalDatas", list);
            } else {
                map.put("success", false);
                map.put("message", "");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/get_one")
    public @ResponseBody
    Map<String, Object> getOne(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String id = request.getParameter("id");
            OperationApprovalData data = operationApprovalService.getOne(id);

            map.put("success", true);
            map.put("message", "");
            map.put("operationApprovalData", data);

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

}
