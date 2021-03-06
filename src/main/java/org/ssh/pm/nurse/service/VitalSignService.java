package org.ssh.pm.nurse.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.mob.MobConstants;
import org.ssh.pm.mob.MobUtil;
import org.ssh.pm.nurse.dao.MeasureTypeDao;
import org.ssh.pm.nurse.dao.PatientDao;
import org.ssh.pm.nurse.dao.SkinTestDao;
import org.ssh.pm.nurse.dao.TimePointDao;
import org.ssh.pm.nurse.dao.VitalSignDataDao;
import org.ssh.pm.nurse.dao.VitalSignItemDao;
import org.ssh.pm.nurse.entity.MeasureType;
import org.ssh.pm.nurse.entity.Patient;
import org.ssh.pm.nurse.entity.SkinTest;
import org.ssh.pm.nurse.entity.TimePoint;
import org.ssh.pm.nurse.entity.VitalSignData;
import org.ssh.pm.nurse.entity.VitalSignItem;

@Service
@Transactional
public class VitalSignService {
    private static Logger logger = LoggerFactory.getLogger(VitalSignService.class);

    @Autowired
    private TimePointDao timePointDao;
    @Autowired
    private MeasureTypeDao measureTypeDao;
    @Autowired
    private SkinTestDao skinTestDao;
    @Autowired
    private VitalSignItemDao vitalSignItemDao;
    @Autowired
    private PatientDao patientDao;
    @Autowired
    private VitalSignDataDao vitalSignDataDao;

    // 初始化,导入生命体征指标
    public void initData() throws ServiceException {
        // 判断新增
        logger.debug("开始装载生命体征指标初始数据");

        File resourcetxt = new File(this.getClass().getResource("/data/vitalsignitem.txt").getFile());
        String star[] = {};
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            VitalSignItem re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                // 第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                star = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                if (vitalSignItemDao.findUniqueBy("name", star[1].trim()) != null)
                    continue;

                re = new VitalSignItem();
                re.setCode(star[0].trim());
                re.setName(star[1].trim());
                re.setUnit(star[2].trim());
                re.setTypeCode(star[3].trim());
                this.vitalSignItemDao.save(re);
            }
            initData2();
        } catch (Exception e) {
            logger.error("str[]:" + StringUtils.join(star, ","));
            logger.error("装载生命体征指标数据出错:" + e);
            throw new ServiceException("导入生命体征指标时，服务器发生异常");
        } finally {

        }
    }

    // 初始化,导入生命体征时间点
    public void initData2() throws ServiceException {
        // 判断新增
        logger.debug("开始装载生命体征时间点初始数据");

        File resourcetxt = new File(this.getClass().getResource("/data/timepoint.txt").getFile());
        String star[] = {};
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            TimePoint re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                // 第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                star = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                if (timePointDao.findUniqueBy("name", star[1].trim()) != null)
                    continue;
                re = new TimePoint();
                re.setCode(star[0].trim());
                re.setName(star[1].trim());
                this.timePointDao.save(re);
            }
            initData3();
        } catch (Exception e) {
            logger.error("str[]:" + StringUtils.join(star, ","));
            logger.error("装载生命体征时间点数据出错:" + e);
            throw new ServiceException("导入生命体征时间点时，服务器发生异常");
        } finally {

        }
    }

    public void initData3() throws ServiceException {
        // 判断新增
        logger.debug("开始装载生命体征测量类别初始数据");

        File resourcetxt = new File(this.getClass().getResource("/data/measuretype.txt").getFile());
        String star[] = {};
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            MeasureType re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                // 第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                star = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                if (measureTypeDao.findUniqueBy("name", star[1].trim()) != null)
                    continue;
                re = new MeasureType();
                re.setCode(star[0].trim());
                re.setName(star[1].trim());
                this.measureTypeDao.save(re);
            }
            initData4();
        } catch (Exception e) {
            logger.error("str[]:" + StringUtils.join(star, ","));
            logger.error("装载生命体征测量类别数据出错:" + e);
            throw new ServiceException("导入生命体征测量类别时，服务器发生异常");
        } finally {

        }
    }

    public void initData4() throws ServiceException {
        // 判断新增
        logger.debug("开始装载皮试初始数据");

        File resourcetxt = new File(this.getClass().getResource("/data/skintest.txt").getFile());
        String star[] = {};
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            SkinTest re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                // 第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                star = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                if (skinTestDao.findUniqueBy("name", star[1].trim()) != null)
                    continue;
                re = new SkinTest();
                re.setCode(star[0].trim());
                re.setName(star[1].trim());
                this.skinTestDao.save(re);
            }
        } catch (Exception e) {
            logger.error("str[]:" + StringUtils.join(star, ","));
            logger.error("装载皮试数据出错:" + e);
            throw new ServiceException("导入皮试时，服务器发生异常");
        } finally {

        }
    }

    @Transactional(readOnly = true)
    public List<TimePoint> queryTimePoint() {

        List<TimePoint> list = timePointDao.getAll();
        return list;

    }

    @Transactional(readOnly = true)
    public String validPoint(String id, String code, String name) {

        StringBuffer error = new StringBuffer();
        if (StringUtils.isNotBlank(id)) {

            List<TimePoint> list = timePointDao.find(" from TimePoint where id != ? and name = ? ", Long.valueOf(id),
                    name);
            if (list != null && list.size() > 0)
                error.append("时间点不能重复");

            list = timePointDao.find(" from TimePoint where id != ? and code = ? ", Long.valueOf(id), code);
            if (list != null && list.size() > 0)
                error.append("时间点编号不能重复");
        } else {
            List<TimePoint> list = timePointDao.find(" from TimePoint where name = ? ", name);
            if (list != null && list.size() > 0)
                error.append("时间点不能重复");
            list = timePointDao.find(" from TimePoint where code = ? ", code);
            if (list != null && list.size() > 0)
                error.append("时间点编号不能重复");
        }

        return error.toString();
    }

    public void saveTimePoint(String id, String code, String name) {

        try {
            if (StringUtils.isNotBlank(id)) {
                TimePoint item = timePointDao.findUniqueBy("id", Long.valueOf(id));
                item.setName(name);
                item.setCode(code);
                timePointDao.save(item);
            } else {
                TimePoint item = new TimePoint();
                item.setName(name);
                item.setCode(code);
                timePointDao.save(item);
            }
        } catch (Exception e) {
            logger.error("saveItem:", e.getMessage());
            throw new ServiceException("时间点定义失败");
        }

    }

    public void deleteTimePoint(Long id) {
        try {

            timePointDao.batchExecute("delete from TimePoint where id = ? ", id);

        } catch (Exception e) {
            logger.error("deleteTimePoint:", e.getMessage());
            throw new ServiceException("删除失败");
        }
    }

    public List<MeasureType> queryMeasureType() {

        List<MeasureType> list = measureTypeDao.getAll();
        return list;

    }

    @Transactional(readOnly = true)
    public String validMeasureType(String id, String code, String name) {

        StringBuffer error = new StringBuffer();
        if (StringUtils.isNotBlank(id)) {

            List<MeasureType> list = measureTypeDao.find(" from MeasureType where id != ? and name = ? ",
                    Long.valueOf(id), name);
            if (list != null && list.size() > 0)
                error.append("测量类别不能重复");

            list = measureTypeDao.find(" from MeasureType where id != ? and code = ? ", Long.valueOf(id), code);
            if (list != null && list.size() > 0)
                error.append("测量类别编号不能重复");
        } else {
            List<MeasureType> list = measureTypeDao.find(" from MeasureType where name = ? ", name);
            if (list != null && list.size() > 0)
                error.append("测量类别不能重复");
            list = measureTypeDao.find(" from MeasureType where code = ? ", code);
            if (list != null && list.size() > 0)
                error.append("测量类别编号不能重复");
        }

        return error.toString();
    }

    public void saveMeasureType(String id, String code, String name) {

        try {
            if (StringUtils.isNotBlank(id)) {
                MeasureType item = measureTypeDao.findUniqueBy("id", Long.valueOf(id));
                item.setName(name);
                item.setCode(code);
                measureTypeDao.save(item);
            } else {
                MeasureType item = new MeasureType();
                item.setName(name);
                item.setCode(code);
                measureTypeDao.save(item);
            }
        } catch (Exception e) {
            logger.error("saveItem:", e.getMessage());
            throw new ServiceException("测量类别定义失败");
        }

    }

    public void deleteMeasureType(Long id) {
        try {

            measureTypeDao.batchExecute("delete from MeasureType where id = ? ", id);

        } catch (Exception e) {
            logger.error("deleteTimePoint:", e.getMessage());
            throw new ServiceException("删除失败");
        }
    }

    @Transactional(readOnly = true)
    public List<VitalSignItem> queryVitalSignItem() {

        List<VitalSignItem> list = vitalSignItemDao.getAll();
        return list;

    }

    @Transactional(readOnly = true)
    public String validVitalSignItem(String id, String code, String name) {

        StringBuffer error = new StringBuffer();
        if (StringUtils.isNotBlank(id)) {

            List<VitalSignItem> list = vitalSignItemDao.find(" from VitalSignItem where id != ? and name = ? ",
                    Long.valueOf(id), name);
            if (list != null && list.size() > 0)
                error.append("生命体征指标不能重复");

            list = vitalSignItemDao.find(" from VitalSignItem where id != ? and code = ? ", Long.valueOf(id), code);
            if (list != null && list.size() > 0)
                error.append("生命体征指标编号不能重复");
        } else {
            List<VitalSignItem> list = vitalSignItemDao.find(" from VitalSignItem where name = ? ", name);
            if (list != null && list.size() > 0)
                error.append("生命体征指标不能重复");
            list = vitalSignItemDao.find(" from VitalSignItem where code = ? ", code);
            if (list != null && list.size() > 0)
                error.append("生命体征指标编号不能重复");
        }

        return error.toString();
    }

    public void saveVitalSignItem(String id, String code, String name, String unit, String typeCode) {

        try {
            if (StringUtils.isNotBlank(id)) {
                VitalSignItem item = vitalSignItemDao.findUniqueBy("id", Long.valueOf(id));
                item.setName(name);
                item.setCode(code);
                item.setUnit(unit);
                item.setTypeCode(typeCode);
                vitalSignItemDao.save(item);
            } else {
                VitalSignItem item = new VitalSignItem();
                item.setName(name);
                item.setCode(code);
                item.setUnit(unit);
                item.setTypeCode(typeCode);
                vitalSignItemDao.save(item);
            }
        } catch (Exception e) {
            logger.error("saveItem:", e.getMessage());
            throw new ServiceException("生命体征指标定义失败");
        }

    }

    public void deleteVitalSignItem(Long id) {
        try {

            vitalSignItemDao.batchExecute("delete from VitalSignItem where id = ? ", id);

        } catch (Exception e) {
            logger.error("deleteVitalSignItem:", e.getMessage());
            throw new ServiceException("删除失败");
        }
    }

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

    public List<VitalSignData> getVitalSignData_all(String userId, String patientId, String busDate) throws Exception {
        List<VitalSignData> list = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(userId));
            map.put("patientId", patientId);
            map.put("busDate", busDate);

            MobUtil.execSp(MobConstants.MOB_SPNAME_GET_VITALSIGN, map);
            list = vitalSignDataDao.find(" from VitalSignData where userId = ?  ", Long.valueOf(userId));
        } catch (Exception e) {
            logger.error("getPatient:", e.getMessage());
            throw new Exception(e);
        }
        return list;
    }

    public List<VitalSignData> getVitalSignData(String userId, String patientId, String busDate, String itemCode,
            String timePoint) throws Exception {

        List<VitalSignData> list = null;

        try {

            if (StringUtils.isBlank(timePoint)) {
                //一个生命体征
                if (StringUtils.isNotBlank(itemCode)) {
                    list = vitalSignDataDao
                            .find(" from VitalSignData where userId = ? and  patientId = ? and addDate = ? and itemCode = ? ",
                                    Long.valueOf(userId), patientId, busDate, itemCode);
                }

            } else {
                //一个时间点一个生命体征
                if (StringUtils.isNotBlank(itemCode)) {
                    list = vitalSignDataDao.find(
                            " from VitalSignData where userId = ? and patientId = ? and addDate = ? "
                                    + " and itemCode = ? and timePoint = ? ", Long.valueOf(userId), patientId, busDate,
                            itemCode, timePoint);
                } else {
                    //一个时间点所有生命体征
                    list = vitalSignDataDao.find(
                            " from VitalSignData where userId = ? and patientId = ? and addDate = ? "
                                    + " and timePoint = ? ", Long.valueOf(userId), patientId, busDate, timePoint);

                }

            }

        } catch (Exception e) {
            logger.error("getVitalSignData_one:", e.getMessage());
            throw new Exception(e);
        }

        return list;

    }

    //修改后直接提交给his保存,存储过程成功后,把当前记录的state修改为N
    public void saveVitalSignData(String userId, String patientId, String busDate, String itemName, String timePoint,
            String itemCode, String timeCode, String value1, String value2, String unit, String measureTypeCode)
            throws Exception {

        List<VitalSignData> list = null;
        VitalSignData entity = null;

        try {
            VitalSignItem item = vitalSignItemDao.findUniqueBy("code", itemCode);
            if (item.getTypeCode().equals(com.ek.mobileapp.model.MobConstants.MOB_VITALSIGN_MORE)) {
                list = vitalSignDataDao.find(" from VitalSignData where userId = ? and patientId = ? and addDate = ? "
                        + " and itemName = ? and timePoint = ? ", Long.valueOf(userId), patientId, busDate,
                        item.getName(), timePoint);
            } else {
                list = vitalSignDataDao.find(" from VitalSignData where userId = ? and patientId = ? and addDate = ? "
                        + " and itemName = ?  ", Long.valueOf(userId), patientId, busDate, item.getName());

            }
            TimePoint tp = timePointDao.findUniqueBy("name", timePoint);

            if (list.size() > 0) {
                entity = list.get(0);
                entity.setUserId(Long.valueOf(userId));
                if (StringUtils.isBlank(value1)) {
                    entity.setValue1(null);
                } else {
                    entity.setValue1(value1);
                }
                if (StringUtils.isBlank(value2)) {
                    entity.setValue2(null);
                } else {
                    entity.setValue2(value2);
                }
                entity.setUnit(unit);
                entity.setMeasureTypeCode(measureTypeCode);
                entity.setState(MobConstants.MOB_VITALSIGN_STATE_UPDATE);
                vitalSignDataDao.save(entity);

            } else {
                entity = new VitalSignData();
                entity.setUserId(Long.valueOf(userId));
                entity.setPatientId(patientId);
                entity.setAddDate(busDate);
                entity.setItemName(item.getName());
                entity.setTimePoint(timePoint);
                entity.setItemCode(itemCode);
                entity.setTimeCode(tp.getCode());
                if (StringUtils.isBlank(value1)) {
                    entity.setValue1(null);
                } else {
                    entity.setValue1(value1);
                }
                if (StringUtils.isBlank(value2)) {
                    entity.setValue2(null);
                } else {
                    entity.setValue2(value2);
                }
                entity.setUnit(unit);
                entity.setVisitId("1");
                entity.setMeasureTypeCode(measureTypeCode);
                entity.setState(MobConstants.MOB_VITALSIGN_STATE_UPDATE);
                vitalSignDataDao.save(entity);
            }

        } catch (Exception e) {
            logger.error("saveVitalSignData:", e.getMessage());
            throw new Exception(e);
        }

    }

    public List<VitalSignItem> getVitalSignItem(String typeCode) {
        if (org.apache.commons.lang3.StringUtils.isBlank(typeCode)) {
            return vitalSignItemDao.find(" from VitalSignItem order by id ");
        } else {
            return vitalSignItemDao.find(" from VitalSignItem where typeCode = ? order by id", typeCode);
        }

    }

    public List<Patient> getPatientAll(String userId, String deptCode) throws Exception {
        List<Patient> list = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(userId));
            map.put("deptCode", deptCode);

            MobUtil.execSp(MobConstants.MOB_SPNAME_GET_PATIENT_ALL, map);
            list = patientDao.find(" from Patient where userId = ? ", Long.valueOf(userId));

        } catch (Exception e) {
            logger.error("getPatientAll:", e.getMessage());
            throw new Exception(e);
        }

        return list;

    }

    public List<SkinTest> querySkinTest() {

        List<SkinTest> list = skinTestDao.getAll();
        return list;

    }

    @Transactional(readOnly = true)
    public String validSkinTest(String id, String code, String name) {

        StringBuffer error = new StringBuffer();
        if (StringUtils.isNotBlank(id)) {

            List<SkinTest> list = skinTestDao
                    .find(" from SkinTest where id != ? and name = ? ", Long.valueOf(id), name);
            if (list != null && list.size() > 0)
                error.append("皮试名称不能重复");

            list = skinTestDao.find(" from SkinTest where id != ? and code = ? ", Long.valueOf(id), code);
            if (list != null && list.size() > 0)
                error.append("皮试编号不能重复");
        } else {
            List<SkinTest> list = skinTestDao.find(" from SkinTest where name = ? ", name);
            if (list != null && list.size() > 0)
                error.append("皮试名称不能重复");
            list = skinTestDao.find(" from SkinTest where code = ? ", code);
            if (list != null && list.size() > 0)
                error.append("皮试编号不能重复");
        }

        return error.toString();
    }

    public void saveSkinTest(String id, String code, String name) {

        try {
            if (StringUtils.isNotBlank(id)) {
                SkinTest item = skinTestDao.findUniqueBy("id", Long.valueOf(id));
                item.setName(name);
                item.setCode(code);
                skinTestDao.save(item);
            } else {
                SkinTest item = new SkinTest();
                item.setName(name);
                item.setCode(code);
                skinTestDao.save(item);
            }
        } catch (Exception e) {
            logger.error("saveItem:", e.getMessage());
            throw new ServiceException("皮试定义失败");
        }

    }

    public void deleteSkinTest(Long id) {
        try {

            skinTestDao.batchExecute("delete from SkinTest where id = ? ", id);

        } catch (Exception e) {
            logger.error("deleteTimePoint:", e.getMessage());
            throw new ServiceException("删除失败");
        }
    }
}