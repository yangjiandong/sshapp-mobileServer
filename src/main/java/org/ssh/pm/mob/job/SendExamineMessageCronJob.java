package org.ssh.pm.mob.job;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import org.springside.modules.utils.ThreadUtils;
import org.springside.modules.utils.UtilDateTime;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.enums.SetupCode;
import org.ssh.pm.mob.service.SendSMSService;
import org.ssh.sys.entity.CronTask;
import org.ssh.sys.service.AppSetupService;
import org.ssh.sys.service.CronTaskService;

/**
 * 定时执行的任务类.
 */
public class SendExamineMessageCronJob implements Runnable {

    private static Logger logger = LoggerFactory.getLogger("org.ssh.pms.auto");

    CronTaskService cronTaskService;
    AppSetupService appSetupService;
    SendSMSService sendSMSService;

    private String cronExpression;
    private int shutdownTimeout = Integer.MAX_VALUE;
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @PostConstruct
    public void start() {

        //
        CronTask c = this.cronTaskService.findByUnique("taskName", SendExamineMessageCronJob.class.getName());
        if (c != null) {
            setCronExpression(c.getCronExpression());
        } else {
            //每1分钟
            setCronExpression("0 0/1 * * * ?");
        }

        Assert.hasText(cronExpression);

        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadNamePrefix("SendExamineMessageCronJob");
        threadPoolTaskScheduler.initialize();

        threadPoolTaskScheduler.schedule(this, new CronTrigger(cronExpression));
    }

    @PreDestroy
    public void stop() {
        ScheduledExecutorService scheduledExecutorService = threadPoolTaskScheduler.getScheduledExecutor();

        ThreadUtils.normalShutdown(scheduledExecutorService, shutdownTimeout, TimeUnit.SECONDS);

    }

    //只做科级
    public void run() {
        if (!this.appSetupService.getValue(SetupCode.AUTO_RUNJOB.value()).equals(CoreConstants.ACTIVE)) {
            logger.info("{} - 系统不允许自动任务。", UtilDateTime.nowDateString("yyyy-MM-dd HH:mm:ss"));
            return;
        }

        //logger.info("{}开始一次任务。", UtilDateTime.nowDateString("yyyy-MM-dd HH:mm:ss"));
        this.sendSMSService.execute();
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

    @Autowired
    public void setCronTaskService(CronTaskService cronTaskService) {
        this.cronTaskService = cronTaskService;
    }

    @Autowired
    public void setAppSetupService(AppSetupService appSetupService) {
        this.appSetupService = appSetupService;
    }

    @Autowired
    public void setSendSMSService(SendSMSService sendSMSService) {
        this.sendSMSService = sendSMSService;
    }
}
