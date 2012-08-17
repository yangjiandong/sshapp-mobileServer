package org.ssh.pm.mob.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.mob.dao.AutoRunLogDao;
import org.ssh.pm.mob.dao.CronTypeDao;
import org.ssh.pm.mob.dao.ItemSourceDao;
import org.ssh.pm.mob.entity.AutoRunLog;
import org.ssh.pm.mob.entity.CronType;
import org.ssh.pm.mob.entity.ItemSource;

@Service
@Transactional
public class AutoRunSetupService {
    private static Logger logger = LoggerFactory.getLogger(AutoRunSetupService.class);

    @Autowired
    private ItemSourceDao itemSourceDao;
    @Autowired
    private CronTypeDao cronTypeDao;
    @Autowired
    private AutoRunLogDao autoRunLogDao;

    // 初始化,导入自动任务执行方式
    public void initData() throws ServiceException {
        // 判断新增
        logger.debug("开始装载自动任务执行方式初始数据");
        this.cronTypeDao.batchExecute("delete from CronType");

        File resourcetxt = new File(this.getClass().getResource("/data/crontype.txt").getFile());
        String star[] = {};
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            CronType re;
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

                if (cronTypeDao.findUniqueBy("id", Long.valueOf(star[0].trim())) != null)
                    continue;

                re = new CronType();
                re.setId(Long.valueOf(star[0].trim()));
                re.setExpression(star[1].trim());
                re.setCron(star[2].trim());
                this.cronTypeDao.save(re);
            }
        } catch (Exception e) {
            logger.error("str[]:" + StringUtils.join(star, ","));
            logger.error("装载自动任务执行方式数据出错:" + e);
            throw new ServiceException("导入自动任务执行方式时，服务器发生异常");
        } finally {

        }
    }

    public List<ItemSource> query(String typeCode) {

        List<ItemSource> list = itemSourceDao.find(" from ItemSource where typeCode = ? ", typeCode);
        return list;

    }

    public List<CronType> getCron() {

        List<CronType> list = cronTypeDao.getAll();
        return list;

    }

    public void saveCron(String id, String cronId) {
        try {
            ItemSource item = itemSourceDao.findUniqueBy("id", Long.valueOf(id));
            CronType ct = cronTypeDao.findUniqueBy("id", Long.valueOf(cronId));
            item.setCron(ct.getCron());
            item.setCronId(ct.getId());
            item.setExpression(ct.getExpression());
            itemSourceDao.save(item);

        } catch (Exception e) {
            logger.error("saveCron:", e.getMessage());
            throw new ServiceException("设置自动任务执行方式失败");
        }

    }

    public CronType getCronType(Long cronId) {

        CronType list = cronTypeDao.findUniqueBy("id", cronId);
        return list;

    }

    public List<ItemSource> getItemSource(Long cronId) {

        List<ItemSource> list = itemSourceDao.find(" from ItemSource where cronId = ? ", cronId);
        return list;

    }

    public void saveLog(AutoRunLog entity) {
        try {
            autoRunLogDao.save(entity);

        } catch (Exception e) {
            logger.error("saveLog", e.getMessage());
            throw new ServiceException("自动任务保存日志失败");
        }
    }
}