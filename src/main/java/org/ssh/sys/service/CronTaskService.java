package org.ssh.sys.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.orm.hibernate.EntityService;
import org.ssh.sys.dao.CronTaskDao;
import org.ssh.sys.entity.CronTask;

@Service
public class CronTaskService extends EntityService<CronTask, Long> {

    @Autowired
    CronTaskDao cronTaskDao;

    @Override
    protected CronTaskDao getEntityDao() {
        return cronTaskDao;
    }

    public void initData() {
        logger.debug("开始装载自动任务");

        File resourcetxt = new File(this.getClass().getResource("/data/crontask.txt").getFile());
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            CronTask re = new CronTask();
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                //第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                String star[] = thisLine.split(",");
                if (star[1].trim().equals(""))
                    continue;

                if (cronTaskDao.findUniqueBy("taskName", star[1]) != null)
                    continue;

                re = new CronTask();
                re.setId(Long.valueOf(star[0]));
                re.setTaskName(star[1]);
                re.setCronExpression(star[2]);
                re.setNote(star[3]);
                this.cronTaskDao.save(re);
            }
        } catch (Exception e) {
            logger.error("装载自动任务出错:" + e);
            throw new ServiceException("导入自动任务时，服务器发生异常");
        } finally {

        }
    }
}
