package com.kuangchi.sdd.ZigBeeConsole.device.util;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class ObjectSephamorePool {
       public static final ConcurrentHashMap<String, Semaphore> semaphoreHashMap=new ConcurrentHashMap<String, Semaphore>();
       public static Semaphore getSemaphore(String deviceId){
    	   Semaphore semaphore=semaphoreHashMap.get(deviceId);
    	   if (null==semaphore) {
    		   	semaphoreHashMap.put(deviceId, new Semaphore(1));
		    } 
    	  return semaphoreHashMap.get(deviceId);  
       }
       
       
       
}
