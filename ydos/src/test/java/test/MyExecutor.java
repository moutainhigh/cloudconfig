package test;

import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by think on 2018/4/3.
 */
public class MyExecutor extends ThreadPoolExecutor {

    ThreadLocal<Date> dateThreadLocal=new ThreadLocal<>();

    public MyExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {

        dateThreadLocal.set(new Date());
        System.out.println("===============================================before execute"+Thread.currentThread().getName());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
         System.out.println("===============================================after execute"+ Thread.currentThread().getName()+"..........."+(new Date().getTime()-dateThreadLocal.get().getTime()));
    }


}
