package com.kuangchi.sdd.zigbee.pool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;

/**
 * 闭锁池,闭锁池的作用是保证发送一个指令之后，发送线程在规定时间内等待回复
 * 
 * */
public  class ObjectCountDownLatchPool  {
	public static final Logger LOG = Logger.getLogger(ObjectCountDownLatchPool.class);
     static ConcurrentMap<String, CountDownLatch>  objectCountLatchPool=new ConcurrentHashMap<String, CountDownLatch>();
     public static void saveCountDownLatchPool(String headerCmdLockId,CountDownLatch countDownLatch){
    	 objectCountLatchPool.put(headerCmdLockId, countDownLatch);
     }

     
     public static void release(String headerCmdLockId){
    	CountDownLatch countDownLatch= objectCountLatchPool.get(headerCmdLockId);
    	LOG.info("release---"+countDownLatch);
    	if (null!=countDownLatch) {
			countDownLatch.countDown();
			objectCountLatchPool.remove(headerCmdLockId);
		}
     }
}
