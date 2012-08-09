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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssh.pm.approval.entity.ApprovalNote;
import org.ssh.pm.approval.entity.DrugApprovalData;
import org.ssh.pm.approval.service.DrugApprovalService;
import org.ssh.pm.common.utils.JSONResponseUtil;
import org.ssh.pm.mob.MobConstants;
import org.ssh.pm.mob.MobUtil;

//合理用药审核
@Controller
@RequestMapping("/drug_approval")
public class DrugApprovalController {
    private static Logger logger = LoggerFactory.getLogger(DrugApprovalController.class);

    @Autowired
    private DrugApprovalService drugApprovalService;

    @RequestMapping("/commit_his")
    public @ResponseBody
    Map<String, Object> commitHis(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            Map<String, Object> map1 = new HashMap<String, Object>();

            MobUtil.execSp(MobConstants.MOB_SPNAME_COMMIT_DRUGAPPROVAL, map1);

            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/save_drug_approval")
    public @ResponseBody
    Map<String, Object> saveDrugApproval(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        String userId = request.getParameter("userId");
        String appNo = request.getParameter("appNo");
        String result = request.getParameter("result");
        String note = request.getParameter("note");

        try {

            if (StringUtils.isBlank(userId)) {
                map.put("success", false);
                map.put("message", "用户编号不存在");
            } else {
                if (StringUtils.isBlank(appNo)) {
                    map.put("success", false);
                    map.put("message", "审批编号不存在");
                } else {
                    drugApprovalService.saveDrugApproval(userId, appNo, result, note);

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

        try {
            List<DrugApprovalData> list = drugApprovalService.getAll();
            if (list != null && list.size() > 0) {
                map.put("success", true);
                map.put("message", "");
                map.put("drugApprovalDatas", list);
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
            String appNo = request.getParameter("appNo");
            DrugApprovalData data = drugApprovalService.getOne(appNo);

            map.put("success", true);
            map.put("message", "");
            map.put("drugApprovalData", data);

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/query_note")
    public void queryNote(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ApprovalNote> data = drugApprovalService.queryNote();
        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_note")
    public void saveNote(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String typeCode = request.getParameter("typeCode");

        try {

            String error = drugApprovalService.validNote(id, code, name, typeCode);
            if (error.length() == 0) {
                drugApprovalService.saveNote(id, code, name, typeCode);
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

    @RequestMapping("/delete_note")
    public void deleteNote(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String id = request.getParameter("id");
        try {
            drugApprovalService.deleteNote(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/get_note")
    public @ResponseBody
    Map<String, Object> getNote(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String typeCode = request.getParameter("typeCode");

        try {
            List<ApprovalNote> list = drugApprovalService.getNote(typeCode);
            if (list != null && list.size() > 0) {
                map.put("success", true);
                map.put("message", "");
                map.put("approvalNotes", list);
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

}
