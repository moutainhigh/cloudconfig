package com.kuangchi.sdd.elevator.dllInterfaces;


import com.sun.jna.Native;
/**
 * 
 * 梯控调用DLL的接口工厂类
 * 
 * 
 * **/
public class TKInterfacesFactory {
          public static TKInterfaces tkInterfaces=null;
          
          public static TKInterfaces getTkInterfaces(){
        	  if (null==tkInterfaces) {
        		String bitType=  System.getProperty("sun.arch.data.model");
        		if ("64".equals(bitType)) {
    				tkInterfaces=(TKInterfaces) Native.loadLibrary("dll/dll64bit/TK_DLL",TKInterfaces.class);
				}else{
    				tkInterfaces=(TKInterfaces) Native.loadLibrary("dll/dll32bit/TK_DLL",TKInterfaces.class);

				}
			  }
        	  return tkInterfaces;
          }
}
