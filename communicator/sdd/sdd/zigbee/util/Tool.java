package com.kuangchi.sdd.zigbee.util;

public class Tool {
	//将某一个字节变成二个字母表示的十六进制字符串
	public static String byteToHexString(byte b){
		int bInt=(((int)b)&0xff);
		if (bInt<16) {
			return "0"+Integer.toHexString(bInt);
		}else{
			return Integer.toHexString(bInt);
		}
	}
	
	//将十六进制字符串变成字节组，字符串大小必须为偶数
	public static byte[] hexStringtoBytes(String str){
		byte[] bytes=new byte[str.length()/2];  
		for (int i = 0; i < bytes.length; i++) {
			bytes[i]=(byte) Integer.parseInt(str.substring(i*2,(i+1)*2), 16);
		}
      return bytes;
	}
	
	public static void main(String[] args) {
	//System.out.println(byteToHexString((byte)15));	
		
		System.out.println(hexStringtoBytes("8C135202004B1200"));;
	}
}
