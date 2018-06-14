package com.kuangchi.sdd.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class PropertyUtils {
		public static Properties readProperties(String absoluteFilePath){
			Properties properties=new Properties();
			FileInputStream fis=null;
		    try {
				
		    	fis=new FileInputStream(new File(absoluteFilePath));
		        properties.load(fis);
			} catch (Exception e) {
				 e.printStackTrace();
				return null;
			}finally{
				if(null!=fis){
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return properties;
			
		}
		
		/*
		 * 给某一个属性文件添加属性
		 * abosolutePath  绝对路径
		 * key  属性键
		 * value 属性值
		 * comments 需要添加到属性文件的备注
		 * 
		 * 
		 * ***/
		public static boolean setProperties(String absoluteFilePath,String key,String value,String comments){
			 FileOutputStream fos=null;
			 try {
				Properties properties= readProperties(absoluteFilePath);
				 fos=new FileOutputStream(new File(absoluteFilePath));
				 properties.setProperty(key, value);
				 properties.store(fos, comments);
				 return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}finally{
				if(null!=fos){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		
		
}
