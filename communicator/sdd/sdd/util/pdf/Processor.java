package com.kuangchi.sdd.util.pdf;


/**
 * Created by ccjjxl on 2014/11/2.
 */
public interface Processor<T> extends Runnable {

    void setMultiProcessor(MultiProcessor<T> multiProcessor);

    void setExceptionCallBack(ExceptionCallBack exceptionCallBack);

    String getName();

    T getResut();

}
