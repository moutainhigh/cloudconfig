package com.kuangchi.sdd.util.commonUtil;

import java.util.UUID;

public class UUIDUtil {

	public static String uuidStr(){
		return  UUID.randomUUID().toString().toUpperCase();
	}
	
	public static String getUUID32() {
	     String uuid = UUID.randomUUID().toString(); 
		return uuid.replaceAll("-", "").toUpperCase();
	}
}
