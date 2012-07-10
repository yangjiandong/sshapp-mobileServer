package org.ssh.pm.mob.job;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import org.springside.modules.utils.ThreadUtils;
import org.springside.modules.utils.UtilDateTime;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.mob.dao.CronTypeDao;
import org.ssh.pm.mob.dao.ItemSourceDao;
import org.ssh.pm.mob.entity.AutoRunLog;
import org.ssh.pm.mob.entity.CronType;
import org.ssh.pm.mob.entity.ItemSource;
import org.ssh.pm.mob.service.AutoRunSetupService;

/**
 * 定时执行的任务类.
 */
public class QueryDataCronJob2 implements Runnable {

    private static Logger logger = LoggerFactory.getLogger("org.ssh.pms.query.2");

    @Autowired
    AutoRunSetupService autoRunSetupService;

    private String cronExpression;
    private int shutdownTimeout = Integer.MAX_VALUE;
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private Long cronId = 2L;

    @PostConstruct
    public void start() {

        //
        CronType c = this.autoRunSetupService.getCronType(this.cronId);
        if (c != null) {
            setCronExpression(c.getCron());
        } else {
            //每半个小时
            setCronExpression("0 0/30 * * * *");

        }

        Assert.hasText(cronExpression);

        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadNamePrefix("autoRunCronJob2");
        threadPoolTaskScheduler.initialize();

        threadPoolTaskScheduler.schedule(this, new CronTrigger(cronExpression));
    }

    @PreDestroy
    public void stop() {
        ScheduledExecutorService scheduledExecutorService = threadPoolTaskScheduler.getScheduledExecutor();

        ThreadUtils.normalShutdown(scheduledExecutorService, shutdownTimeout, TimeUnit.SECONDS);

    }

    public void run() {

        logger.info("{}开始自动任务。", UtilDateTime.nowDateString("yyyy-MM-dd HH:mm:ss"));
        List<ItemSource> list = autoRunSetupService.getItemSource(this.cronId);
        for (ItemSource a : list) {
            String spName = a.getSpName();
            try {
                execOne(spName);

                AutoRunLog entity = new AutoRunLog();
                entity.setItemId(a.getItemId());
                entity.setItemName(a.getItemName());
                entity.setSpName(a.getSpName());
                entity.setState(CoreConstants.ACTIVE);
                entity.setAddDate(UtilDateTime.nowDateString());
                autoRunSetupService.saveLog(entity);
            } catch (Exception e) {
                logger.error("自动任务执行失败:" + e.getMessage());
                AutoRunLog entity = new AutoRunLog();
                entity.setItemId(a.getItemId());
                entity.setItemName(a.getItemName());
                entity.setSpName(a.getSpName());
                entity.setState(CoreConstants.INACTIVE);
                entity.setAddDate(UtilDateTime.nowDateString());
                autoRunSetupService.saveLog(entity);
            }
        }

    }

    protected void execOne(String spName) throws Exception {
        DataSource dataSource = (DataSource) SpringContextHolder.getBean("dataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try {

            // 存储过程
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
            jdbcCall.withProcedureName(spName);
            jdbcCall.execute();

        } catch (Exception e) {
            logger.error(spName + ":", e.getMessage());
            throw new Exception(e);
        }
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * 设置gracefulShutdown的等待时间,单位秒.
     */
    public void setShutdownTimeout(int shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

}
