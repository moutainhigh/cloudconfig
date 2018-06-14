package com.kuangchi.sdd.util.pdf;

/**
 * Created by ccjjxl on 2014/11/2.
 */
public abstract class AbstractProcessor<T> implements Processor<T> {

    private String name;//线程名称
    private MultiProcessor<T> multiProcessor;

    private ExceptionCallBack exceptionCallBack;

    private T res;

    public AbstractProcessor(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setMultiProcessor(MultiProcessor<T> multiProcessor) {
        this.multiProcessor = multiProcessor;
    }

    protected abstract T action() throws Exception;

    @Override
    public void run()  {
        try {
            res = action();
        } catch (Exception e) {
            e.printStackTrace();
            if (null != exceptionCallBack){
                exceptionCallBack.callBack(this,e);
            }
        }finally {
            multiProcessor.threadDone();
        }

    }

    @Override
    public void setExceptionCallBack(ExceptionCallBack exceptionCallBack) {
        this.exceptionCallBack  = exceptionCallBack;
    }

    @Override
    public T  getResut() {
        return res;
    }
}