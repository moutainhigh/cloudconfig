package com.kuangchi.sdd.zigbee.pool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
/**
 * 信号 量池  让某一个设备的某一个命令在同一时刻串行执行
 * 
 * */
public class ObjectSephamorePool {
       public static ConcurrentHashMap<String, Semaphore> semaphoreHashMap=new ConcurrentHashMap<String, Semaphore>();
       public static Semaphore getSemaphore(String headerCmdLockId){
    	   Semaphore semaphore=semaphoreHashMap.get(headerCmdLockId);
    	   if (null==semaphore) {
    		   	semaphoreHashMap.put(headerCmdLockId, new Semaphore(1));
		    } 
    	  return semaphoreHashMap.get(headerCmdLockId);  
       }
       
       
       
}
