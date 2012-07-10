package org.ssh.tool.thread;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 使用Semaphore，限制可以执行的线程数，空闲资源放到队列中等待
 *
 * @author liuyan
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        Runnable limitedCall = new Runnable() {

            // 随机生成数
            final Random rand = new Random();

            // 限制只有3个资源是活动的,第二个参数为true则是按照标准“队列”结构先进先出
            final Semaphore available = new Semaphore(5, true);
            int count = 0;

            public void run() {
                int time = rand.nextInt(10);
                int num = count++;

                try {

                    // 请求资源
                    available.acquire();

                    int needTime = time * 2000;

                    System.out.println("乘客" + num + "买票需要[ " + needTime
                            + " 秒]... #");

                    Thread.sleep(needTime);

                    System.out.println("乘客" + num + "买完了 # !");

                    // 运行完了就释放
                    available.release();
                } catch (InterruptedException intEx) {
                    intEx.printStackTrace();
                }
            }
        };

        for (int i = 0; i < 25; i++)
            new Thread(limitedCall).start();
    }
}