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
import org.ssh.pm.mob.MobConstants;
import org.ssh.pm.mob.MobUtil;
import org.ssh.pm.nurse.entity.MeasureType;
import org.ssh.pm.nurse.entity.Patient;
import org.ssh.pm.nurse.entity.SkinTest;
import org.ssh.pm.nurse.entity.TimePoint;
import org.ssh.pm.nurse.entity.VitalSignData;
import org.ssh.pm.nurse.entity.VitalSignItem;
import org.ssh.pm.nurse.service.VitalSignService;
import org.ssh.pm.orm.hibernate.CustomerContextHolder;
import org.ssh.sys.web.CommonController;

//生命体征
@Controller
@RequestMapping("/vital_sign")
public class VitalSignController {
    private static Logger logger = LoggerFactory.getLogger(VitalSignController.class);

    @Autowired
    private VitalSignService vitalSignService;

    @RequestMapping("/query_time_point")
    public void queryTimePoint(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<TimePoint> data = vitalSignService.queryTimePoint();
        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_time_point")
    public void saveTimePoint(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
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
        String id = request.getParameter("id");
        try {
            vitalSignService.deleteTimePoint(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
    public @ResponseBody
    Map<String, Object> getPatient(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String patientId = request.getParameter("patientId");
            String userId = request.getParameter("userId");
            Patient p = null;
            if (StringUtils.isNotBlank(patientId)) {
                p = vitalSignService.getPatient(userId, patientId);
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

    @RequestMapping("/get_vitalsign_data_all")
    public @ResponseBody
    Map<String, Object> getVitalSignData_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String patientId = request.getParameter("patientId");
            String busDate = request.getParameter("busDate");
            String userId = request.getParameter("userId");
            List<VitalSignData> list = vitalSignService.getVitalSignData_all(userId, patientId, busDate);
            //if (list != null && list.size() > 0) {
                map.put("success", true);
                map.put("message", "");
                map.put("vitalSignData", list);
            //}

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

        String patientId = request.getParameter("patientId");
        String busDate = request.getParameter("busDate");
        String itemCode = request.getParameter("itemCode");
        String timePoint = request.getParameter("timePoint");
        String userId = request.getParameter("userId");
        List<VitalSignData> list = null;
        try {
            list = vitalSignService.getVitalSignData(userId, patientId, busDate, itemCode, timePoint);

            map.put("success", true);
            map.put("message", "");
            map.put("vitalSignData", list);

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

        String userId = request.getParameter("userId");

        String patientId = request.getParameter("patientId");
        String busDate = request.getParameter("busDate");
        String itemName = request.getParameter("itemName");
        String timePoint = request.getParameter("timePoint");

        String itemCode = request.getParameter("itemCode");
        String timeCode = request.getParameter("timeCode");
        String value1 = request.getParameter("value1");
        String value2 = request.getParameter("value2");
        String unit = request.getParameter("unit");
        String measureTypeCode = request.getParameter("measureTypeCode");

        try {

            vitalSignService.saveVitalSignData(userId, patientId, busDate, itemName, timePoint, itemCode, timeCode,
                    value1, value2, unit, measureTypeCode);

            map.put("success", true);
            map.put("message", "");

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

        String patientId = request.getParameter("patientId");
        String busDate = request.getParameter("busDate");

        try {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("userId", Long.valueOf(userId));
            map1.put("patientId", patientId);
            map1.put("busDate", busDate);

            MobUtil.execSp(MobConstants.MOB_SPNAME_COMMIT_VITALSIGN, map1);

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
        String typeCode = request.getParameter("typeCode");
        try {
            data = vitalSignService.getVitalSignItem(typeCode);
            map.put("success", true);
            map.put("message", "");
            map.put("vitalSignItem", data);

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/get_timepoint")
    public @ResponseBody
    Map<String, Object> getTimePoint(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<TimePoint> data = null;
        try {
            data = vitalSignService.queryTimePoint();
            map.put("success", true);
            map.put("message", "");
            map.put("timePoint", data);

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;

    }

    @RequestMapping("/get_measuretype")
    public @ResponseBody
    Map<String, Object> getMeasureType(ModelMap map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        List<MeasureType> data = null;
        try {
            data = vitalSignService.queryMeasureType();
            map.put("success", true);
            map.put("message", "");
            map.put("measureType", data);

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/get_patient_all")
    public @ResponseBody
    Map<String, Object> getPatientAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String deptCode = request.getParameter("deptCode");
            String userId = request.getParameter("userId");
            List<Patient> list = null;
            if (StringUtils.isNotBlank(deptCode)) {
                list = vitalSignService.getPatientAll(userId, deptCode);
            }

            if (list != null && list.size() > 0) {
                map.put("success", true);
                map.put("message", "");
                map.put("patients", list);
            } else {
                map.put("success", false);
                map.put("message", "病人信息不存在");
            }

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/query_skintest")
    public void querySkinTest(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        List<SkinTest> data = vitalSignService.querySkinTest();

        JSONResponseUtil.buildJSONDataResponse(response, data, (long) data.size());
    }

    @RequestMapping("/save_skintest")
    public void saveSkinTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String name = request.getParameter("name");

        try {

            String error = vitalSignService.validSkinTest(id, code, name);
            if (error.length() == 0) {
                vitalSignService.saveSkinTest(id, code, name);
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

    @RequestMapping("/delete_skintest")
    public void deleteSkinTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        String id = request.getParameter("id");
        try {

            vitalSignService.deleteSkinTest(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");

        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JSONResponseUtil.buildCustomJSONDataResponse(response, map);
    }

    @RequestMapping("/get_skintest")
    public @ResponseBody
    Map<String, Object> getSkinTest(ModelMap map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
        List<SkinTest> data = null;
        try {
            data = vitalSignService.querySkinTest();
            map.put("success", true);
            map.put("message", "");
            map.put("skinTest", data);

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
