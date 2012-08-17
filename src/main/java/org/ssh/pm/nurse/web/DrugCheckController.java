package org.ssh.pm.nurse.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssh.pm.mob.MobConstants;
import org.ssh.pm.mob.MobUtil;
import org.ssh.pm.nurse.entity.DrugCheckData;
import org.ssh.pm.nurse.entity.Patient;
import org.ssh.pm.nurse.service.DrugCheckService;

//用药核对
@Controller
@RequestMapping("/drug_check")
public class DrugCheckController {
    private static Logger logger = LoggerFactory.getLogger(DrugCheckController.class);

    @Autowired
    private DrugCheckService drugCheckService;

    @RequestMapping("/get_patient")
    public @ResponseBody
    Map<String, Object> getPatient(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String patientId = request.getParameter("patientId");
            String userId = request.getParameter("userId");
            Patient p = null;
            if (StringUtils.isNotBlank(patientId)) {
                p = drugCheckService.getPatient(userId, patientId);
            }

            if (p != null) {
                map.put("success", true);
                map.put("message", "");
                map.put("patient", p);
            } else {
                map.put("success", false);
                map.put("message", "病人信息不存在");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/get_drugcheck_data")
    public @ResponseBody
    Map<String, Object> getDrugCheckData_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String patientId = request.getParameter("patientId");
            String barCode = request.getParameter("barCode");
            String userId = request.getParameter("userId");
            List<DrugCheckData> list = drugCheckService.getDrugCheckData_all(userId, patientId, barCode);
            if (list != null && list.size() > 0) {
                map.put("success", true);
                map.put("message", "");
                map.put("drugCheckData", list);
            } else {
                map.put("success", false);
                map.put("message", "");
            }

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/query_drugcheck_data")
    public @ResponseBody
    Map<String, Object> queryDrugCheckData_all(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String patientId = request.getParameter("patientId");
            String userId = request.getParameter("userId");
            List<DrugCheckData> list = drugCheckService.queryDrugCheckData_all(userId, patientId);

            map.put("success", true);
            map.put("message", "");
            map.put("drugCheckData", list);

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/commit_his")
    public @ResponseBody
    Map<String, Object> commitHis(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        String userId = request.getParameter("userId");
        String deviceId = request.getParameter("deviceId");

        try {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("userId", Long.valueOf(userId));
            map1.put("deviceId", deviceId);
            map1.put("type", MobConstants.MOB_WORKLOAD_TYPE_DRUGCHECK);

            MobUtil.execSp(MobConstants.MOB_SPNAME_COMMIT_DRUGCHECK, map1);

            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

}
