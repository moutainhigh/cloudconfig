package com.kuangchi.sdd.util.algorithm;

import java.security.MessageDigest;

public class MD5Util {
	public static String MD5(byte[] bytes)  {
    	String result=null;
    	try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] resultBytes=md.digest(bytes);
			StringBuilder sb=new StringBuilder();			
			 for (int i = 0; i < resultBytes.length; i++) {
				sb.append(byte2HexString(resultBytes[i]));
			}
			 return sb.toString();
			
		} catch ( Exception e) {
			e.printStackTrace();
		}  
    	return result;	
    }
	 private static char[] HexCode = {'0', '1', '2', '3', '4', '5', '6', '7',  
         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};  
	 public static String byte2HexString(byte b) {  
	        StringBuffer buffer = new StringBuffer();  
	        buffer.append(HexCode[(b >>> 4) & 0x0f]);  
	        buffer.append(HexCode[b & 0x0f]);  
	        return buffer.toString();  
	    }
}
