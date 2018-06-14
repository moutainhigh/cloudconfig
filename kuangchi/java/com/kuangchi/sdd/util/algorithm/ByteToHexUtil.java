package com.kuangchi.sdd.util.algorithm;

public class ByteToHexUtil {
	 private static char[] HexCode = {'0', '1', '2', '3', '4', '5', '6', '7',  
         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};  
	 public static String byteToHexString(byte b) {  
	        StringBuffer buffer = new StringBuffer();  
	        buffer.append(HexCode[(b >>> 4) & 0x0f]);  
	        buffer.append(HexCode[b & 0x0f]);  
	        return buffer.toString();  
	    }

    
    public static String bytesToHexString( byte[] bytes){
    	StringBuffer stringBuffer=new StringBuffer();
    	for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append(byteToHexString(bytes[i]));
		}
    	return stringBuffer.toString();
    }
}
