package org.ssh.pm.nurse.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.pm.mob.MobConstants;
import org.ssh.pm.mob.MobUtil;
import org.ssh.pm.nurse.dao.DrugCheckDao;
import org.ssh.pm.nurse.dao.PatientDao;
import org.ssh.pm.nurse.entity.DrugCheckData;
import org.ssh.pm.nurse.entity.Patient;

@Service
@Transactional
public class DrugCheckService {
    private static Logger logger = LoggerFactory.getLogger(DrugCheckService.class);

    @Autowired
    private DrugCheckDao drugCheckDao;
    @Autowired
    private PatientDao patientDao;

    public Patient getPatient(String userId, String patientId) throws Exception {
        Patient p = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(userId));
            map.put("patientId", patientId);

            MobUtil.execSp(MobConstants.MOB_SPNAME_GET_PATIENT, map);
            List<Patient> list = patientDao.find(" from Patient where userId = ? and patientId = ?  ",
                    Long.valueOf(userId), patientId);
            if (list.size() > 0)
                p = list.get(0);

        } catch (Exception e) {
            logger.error("getPatient:", e.getMessage());
            throw new Exception(e);
        }

        return p;

    }

    public List<DrugCheckData> getDrugCheckData_all(String userId, String patientId, String barCode) throws Exception {
        List<DrugCheckData> list = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(userId));
            map.put("patientId", patientId);
            map.put("barCode", barCode);

            MobUtil.execSp(MobConstants.MOB_SPNAME_GET_DRUGCHECK, map);
            list = drugCheckDao.find(" from DrugCheckData where userId = ? and patientId = ? order by id desc ",
                    Long.valueOf(userId), patientId);
        } catch (Exception e) {
            logger.error("getDrugCheckData_all:", e.getMessage());
            throw new Exception(e);
        }
        return list;
    }

    public List<DrugCheckData> queryDrugCheckData_all(String userId, String patientId) throws Exception {
        List<DrugCheckData> list = null;
        try {

            list = drugCheckDao.find(" from DrugCheckData where userId = ? and patientId = ? order by id desc ",
                    Long.valueOf(userId), patientId);
        } catch (Exception e) {
            logger.error("queryDrugCheckData_all:", e.getMessage());
            throw new Exception(e);
        }
        return list;
    }

}