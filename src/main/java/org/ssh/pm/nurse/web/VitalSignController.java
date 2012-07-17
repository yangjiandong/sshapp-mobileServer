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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ssh.pm.common.utils.JSONResponseUtil;
import org.ssh.pm.nurse.entity.MeasureType;
import org.ssh.pm.nurse.entity.Patient;
import org.ssh.pm.nurse.entity.TimePoint;
import org.ssh.pm.nurse.entity.VitalSignData;
import org.ssh.pm.nurse.entity.VitalSignItem;
import org.ssh.pm.nurse.service.VitalSignService;
import org.ssh.pm.orm.hibernate.CustomerContextHolder;
import org.ssh.sys.web.CommonController;

@Controller
@RequestMapping("/vital_sign")
public class VitalSignController {
    private static Logger logger = LoggerFactory.getLogger(VitalSignController.class);

    @Autowired
    private VitalSignService vitalSignService;

    @RequestMapping("/query_time_point")
    public void queryTimePoint(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        List<TimePoint> data = vitalSignService.queryTimePoint();

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_time_point")
    public void saveTimePoint(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String name = request.getParameter("name");

        try {

            String error = vitalSignService.validPoint(id, code, name);
            if (error.length() == 0) {
                vitalSignService.saveTimePoint(id, code, name);
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

    @RequestMapping("/delete_time_point")
    public void deleteTimePoint(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        try {

            vitalSignService.deleteTimePoint(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/query_measure_type")
    public void queryMeasureType(ModelMap map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        List<MeasureType> data = vitalSignService.queryMeasureType();

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_measure_type")
    public void saveMeasureType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String name = request.getParameter("name");

        try {

            String error = vitalSignService.validMeasureType(id, code, name);
            if (error.length() == 0) {
                vitalSignService.saveMeasureType(id, code, name);
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

    @RequestMapping("/delete_measure_type")
    public void deleteMeasureType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        try {

            vitalSignService.deleteMeasureType(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/query_vitalsign_item")
    public void queryVitalSignItem(ModelMap map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        List<VitalSignItem> data = vitalSignService.queryVitalSignItem();

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_vitalsign_item")
    public void saveVitalSignItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String typeCode = request.getParameter("typeCode");

        try {

            String error = vitalSignService.validVitalSignItem(id, code, name);
            if (error.length() == 0) {
                vitalSignService.saveVitalSignItem(id, code, name, unit, typeCode);
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

    @RequestMapping("/delete_vitalsign_item")
    public void deleteVitalSignItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        try {

            vitalSignService.deleteVitalSignItem(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/get_patient")
    public void getPatient(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));

        try {
            String patientId = request.getParameter("patientId");
            Patient p = null;
            if (StringUtils.isNotBlank(patientId)) {
                p = vitalSignService.getPatient(patientId);
            }

            if (p != null) {
                map.put("success", true);
                map.put("message", "");
                map.put("Patient", p);
            } else {
                map.put("success", false);
                map.put("message", "病人信息不存在");
            }

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/get_vitalsign_data_all")
    public @ResponseBody
    Map<String, Object> getVitalSignData_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));

        try {
            String patientId = request.getParameter("patientId");
            String busDate = request.getParameter("busDate");
            List<VitalSignData> list = vitalSignService.getVitalSignData_all(patientId, busDate);
            if (list != null && list.size() > 0) {
                map.put("success", true);
                map.put("message", "");
                map.put("VitalSignData", list);
            }

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/get_vitalsign_data_one")
    public @ResponseBody
    Map<String, Object> getVitalSignData_one(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));

        try {

            List<VitalSignData> list = vitalSignService.getVitalSignData(request);
            if (list != null && list.size() > 0) {
                map.put("success", true);
                map.put("message", "");
                map.put("VitalSignData", list);
            }

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/save_vitalsign_data")
    public @ResponseBody
    Map<String, Object> saveVitalSignData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));

        try {

            vitalSignService.saveVitalSignData(request);

            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/get_vitalsign_item")
    public @ResponseBody
    Map<String, Object> getVitalSignItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<VitalSignItem> data = null;
        try {
            data = vitalSignService.getVitalSignItem(request);
            map.put("success", true);
            map.put("message", "");
            map.put("VitalSignItem", data);

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

}
