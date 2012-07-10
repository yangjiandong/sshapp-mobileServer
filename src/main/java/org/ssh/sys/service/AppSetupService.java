package org.ssh.sys.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.ServiceException;
import org.ssh.sys.dao.AppSetupDao;
import org.ssh.sys.entity.AppSetup;

@Service
public class AppSetupService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AppSetupDao appSetupDao;

    /**
     * 获取全部系统设置信息
     */
    @Transactional(readOnly = true)
    public List<JSONObject> getAllAppSetup(List<PropertyFilter> filters) {
        List<AppSetup> setups = appSetupDao.find(filters);
        List<JSONObject> result = new ArrayList<JSONObject>();
        JSONObject json = null;
        for (AppSetup setup : setups) {
            json = new JSONObject();
            json.put("setupCode", setup.getSetupCode());
            json.put("setupValue", setup.getSetupValue() == null ? "" : setup.getSetupValue());
            json.put("descrip", setup.getDescrip() == null ? "" : setup.getDescrip());
            if (setup.getSetupCode().startsWith("sys")) {
                json.put("type", "系统参数");
            } else {
                json.put("type", "定制参数");
            }
            result.add(json);
        }
        return result;
    }

    /**
     * 保存系统设置信息
     */
    @Transactional
    public void saveSetupInfo(String jsonStr) throws ServiceException {
        AppSetup appSetup = null;
        Session session = this.appSetupDao.getSession();
        try {
            session.beginTransaction();
            JSONObject json = JSONObject.fromObject(jsonStr);
            String setupCode = json.getString("setupCode");
            String setupValue = json.getString("setupValue");
            String descrip = json.getString("descrip");
            appSetup = appSetupDao.findUniqueBy("setupCode", setupCode);
            appSetup.setDescrip(descrip);
            appSetup.setSetupValue(setupValue);
            appSetupDao.save(appSetup);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("保存系统设置信息失败：", e);
            throw new ServiceException("保存系统设置信息失败");
        }
    }

    //初始化,导入系统参数
    @Transactional
    public void initData() throws ServiceException {
        logger.debug("开始装载系统参数初始数据");

        File resourcetxt = new File(this.getClass().getResource("/data/appsetup.txt").getFile());
        Session session = null;
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            session = this.appSetupDao.getSession();
            session.beginTransaction();

            AppSetup re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                //第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                String star[] = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;
                if (this.appSetupDao.findOneBy("setupCode", star[0].trim()) != null){
                    continue;
                }

                re = new AppSetup();
                re.setSetupCode(star[0].trim());
                re.setSetupValue(star[1].trim());
                re.setDescrip(star[2].trim());

                this.appSetupDao.save(re);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("装载系统参数数据出错:" + e);
            throw new ServiceException("导入资源时，服务器发生异常");
        } finally {

        }
    }

    //初始化,导入其他系统参数
    @Transactional
    public void initData2(String prefix, String dataFileName) throws ServiceException {
        logger.debug("开始装载系统参数初始数据");

        File resourcetxt = new File(this.getClass().getResource("/data/" + dataFileName).getFile());
        Session session = null;
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            session = this.appSetupDao.getSession();
            session.beginTransaction();

            AppSetup re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                //第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                String star[] = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                if (this.appSetupDao.findUniqueBy("setupCode", star[0].trim())!=null) continue;

                re = new AppSetup();
                re.setSetupCode(star[0].trim());
                re.setSetupValue(star[1].trim());
                re.setDescrip(star[2].trim());

                this.appSetupDao.save(re);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("装载系统参数数据出错:" + e);
            throw new ServiceException("导入资源时，服务器发生异常");
        } finally {
            //br.close();
        }
    }

    //返回系统参数的值
    @Transactional(readOnly = true)
    public String getValue(String code) {
        AppSetup app = appSetupDao.findUniqueBy("setupCode", code);
        if (app == null) {
            return null;
        } else {
            return app.getSetupValue();
        }
    }

    @Transactional(readOnly = true)
    public AppSetup getAppSetup(String code) {
        return appSetupDao.findUniqueBy("setupCode", code);
    }

    @Transactional
    public void saveAppSetup(AppSetup entity) {
        Session session = this.appSetupDao.getSession();
        try {
            session.beginTransaction();
            appSetupDao.save(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("保存系统设置信息失败：", e);
            throw new ServiceException("保存系统设置信息失败");
        }
    }

    @Transactional(readOnly = true)
    public boolean checkArmy() {
        boolean flag = false;

        String value = getValue("user_hospital.is.army");
        if (value.equals("Y")) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }
}
