package org.ssh.tool.thread;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * 时间频率调度
 * @author liuyan
 */
public class ScheduledFutureDemo {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        // 线程池开辟2个线程
        final ScheduledExecutorService scheduler = Executors
                .newScheduledThreadPool(2);

        // 将军
        final Runnable general = new Runnable() {
            int count = 0;

            public void run() {
                System.out.println(Thread.currentThread().getName() + ":"
                        + new Date() + "赵云巡视来了 " + (++count));
            }
        };

        // 士兵
        final Runnable soldier = new Runnable() {
            int count = 0;

            public void run() {
                System.out.println(Thread.currentThread().getName() + ":"
                        + new Date() + "士兵巡视来了 " + (++count));
            }
        };

        // 1秒钟后运行，并每隔2秒运行一次
        final ScheduledFuture beeperHandle1 = scheduler.scheduleAtFixedRate(
                soldier, 1, 1, SECONDS);

        // 5秒钟后运行，并每隔2秒运行一次
        final ScheduledFuture beeperHandle2 = scheduler.scheduleWithFixedDelay(
                general, 5, 5, SECONDS);

        // 30秒后结束关闭任务，并且关闭Scheduler
        scheduler.schedule(new Runnable() {
            public void run() {
                beeperHandle1.cancel(true);
                beeperHandle2.cancel(true);
                scheduler.shutdown();
            }
        }, 60, SECONDS);
    }

}