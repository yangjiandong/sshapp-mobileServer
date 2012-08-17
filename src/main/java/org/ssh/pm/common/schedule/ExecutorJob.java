package org.ssh.pm.common.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ssh.sys.service.AccountManager;

/**
 * 被ScheduledThreadPoolExecutor定时执行的任务类.
 */
public class ExecutorJob implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ExecutorJob.class);

    @Autowired
    private AccountManager accountManager;

    /**
     * 定时打印当前用户数到日志.
     */
    public void run() {
        long userCount = accountManager.getUserCount();
        logger.info("There are {} user in database.", userCount);
    }
}
