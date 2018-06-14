package com.kuangchi.sdd.util.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

/**
 * Created by ccjjxl on 2014/11/2.
 */
public class MultiProcessor<T> {
	
	public static final Logger LOG = Logger.getLogger(MultiProcessor.class);

    private String name;

    private CountDownLatch countDownLatch;

    private List<Processor<T>> processors = new ArrayList<Processor<T>>();

    private ExceptionCallBack exceptionCallBack;


    public MultiProcessor(String name) {
        this.name = name;
    }

    public void threadDone(){
        countDownLatch.countDown();
    }


    public void addProcessors(Processor<T>[] processors){
        for (Processor<T> processor : processors){
            addProcessor(processor);
        }
    }


    public void setExceptionCallBack(ExceptionCallBack exceptionCallBack) {
        this.exceptionCallBack = exceptionCallBack;
    }

    public void addProcessor(Processor<T> processor){
        processors.add(processor);
        processor.setMultiProcessor(this);
        processor.setExceptionCallBack(exceptionCallBack);
    }

    public void start(){

        countDownLatch = new CountDownLatch(processors.size());

        long start = System.currentTimeMillis();

        for (Processor<T> processor : processors){

            Thread thread = new Thread(processor);
            thread.setName(name + " ==> " + processor.getName());

            thread.start();
        }


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        LOG.info(end -start);

        List<T> res = new ArrayList<T>(processors.size());

        for (Processor<T> processor : processors){
            res.add(processor.getResut());

            LOG.info(processor.getResut());
        }
    }


}
