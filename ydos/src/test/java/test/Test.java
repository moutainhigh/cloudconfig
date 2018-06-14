package test;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dell on 2018/3/5.
 */
public class Test {
    public static void main(String[] args) {


      ThreadPoolExecutor threadPoolExecutor= new MyExecutor(2,4,1, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
        for (int i = 0; i <10 ; i++) {
            threadPoolExecutor.submit(new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(".............running,,,,"+Thread.currentThread().getName());
                }
            });
        }


        threadPoolExecutor.shutdown();
    }
}
