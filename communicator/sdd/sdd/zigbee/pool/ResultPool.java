package com.kuangchi.sdd.zigbee.pool;

import java.util.concurrent.ConcurrentHashMap;
/**
 * 平台请求zigBee网关后收到的网关的回复存在这个地方
 * 
 * */
public class ResultPool {
     public static ConcurrentHashMap<String, String> resultMap=new ConcurrentHashMap<String, String>();
     
     public static void clear(String headerCmdLockId){
			resultMap.remove(headerCmdLockId);
     }
     public static String getResult(String headerCmdLockId){
    	 return resultMap.get(headerCmdLockId);
     }
     public static void putResult(String headerCmdLockId,String result){
    	 resultMap.put(headerCmdLockId, result);
     }
     
     
}
