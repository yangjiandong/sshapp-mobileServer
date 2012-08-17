package org.ssh.pm.common.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ssh.pm.common.entity.Category;
import org.ssh.pm.common.service.CategoryService;

//解决长时间执行没反应问题
//http://sunnylocus.iteye.com/blog/1190872
public class FuncCourser {

    // for test
    CategoryService categoryService;
    
    private FuncCourser() {

    }

    private static ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {

        int ncount = 0;

        @Override
        public Thread newThread(Runnable task) {
            ncount++;
            Thread invokeThread = new Thread(task);
            invokeThread.setName("Invoker-thread-" + ncount);
            //父线程退出时,调用线程也要退出
            invokeThread.setDaemon(true);
            return invokeThread;
        }
    });

    /**
     * 目标方法有返回值
     * 
     * @param task 调用代码
     * @param unit 超时时间类型
     * @param timeout 时间
     * @return 被调用函数的返回值
     * @throws java.util.concurrent.TimeoutException 调用超过指定时间时抛出此异常
     */
    public static <T> T call(Callable<?> task, TimeUnit unit, long timeout)
            throws java.util.concurrent.TimeoutException {

        java.util.concurrent.Future<?> futureResult = executor.submit(task);
        Object callRet = null;
        try {
            callRet = futureResult.get(timeout, unit);
        } catch (Exception e) {
            if (e instanceof java.util.concurrent.TimeoutException) {
                throw new TimeoutException("invoke timeout!");
            }
            throw new RuntimeException(e);
        }
        return (T) callRet;
    }

    //
    //目标方法无返回值
    public static void call(Runnable task, TimeUnit unit, long timeout) throws java.util.concurrent.TimeoutException {

        java.util.concurrent.Future<?> futureResult = executor.submit(task);
        try {
            futureResult.get(timeout, unit);
        } catch (Exception e) {
            if (e instanceof java.util.concurrent.TimeoutException) {
                throw new TimeoutException("invoke timeout!");
            }
            throw new RuntimeException(e);
        }

    }
    
    //example
    private void test(){
        Callable<Category> task = new Callable<Category>() {
            public Category call() throws Exception{
                //Category result = categoryService.
                Category result = null;
                return result;
            }
        };
        
        try{
            Category result = FuncCourser.call(task, TimeUnit.SECONDS, 10);
            
        }catch (TimeoutException ce) {
            String causeInfo = "超时Failed ....";
            //logger.error
            //throw new otherException;
        }catch (Exception ce) {
            String causeInfo = "Failed ....";
            //logger.error
            //throw new otherException;
        }
    }
}
