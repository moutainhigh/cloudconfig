package com.kuangchi.sdd.comm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Util {
	public static int getUnsignedInt(byte num){
		int result = num&0xff;
		return result;
	}
	
	public static String getStringHex(int num){
		return Integer.toHexString(num);
	}
	
	public static int getIntHex(String numStr){
		return Integer.parseInt(numStr, 16);
	}
	
	public static Long getLongHex(String numStr){
		return Long.parseLong(numStr, 16);
	}
	
	public static int getUnsignedIntHex(byte num){
		return getIntHex(getStringHex(getUnsignedInt(num)));
	}
	/**
	 * 在字符串前端补齐零
	 * @param str 被补零的字符串
	 * @param len 需要对齐的位数
	 * @return
	 */
	public static String lpad(String str,int len){
		if(str.length()<len){
			String zero = "0";
			for(int i=1;i<len-1;i++){
				zero+="0";
			}
			str=zero+str;
		}
		return str;
	}
	/**
	 * 解码IP
	 * @param maskIP
	 * @return
	 */
	public static List<String> unMaskIP(String maskIP){
		StringTokenizer st = new StringTokenizer(maskIP,".");
        List<String> strs = new ArrayList<String>();
        while(st.hasMoreTokens()){
        	strs.add(st.nextToken());
        }
        return strs;
	}
	/**
	 * 解码IP为字符串格式
	 * @param maskIP
	 * @return
	 */
	public static String unMaskIpToString(String maskIP){
		StringTokenizer st = new StringTokenizer(maskIP,".");
        StringBuffer buffer = new StringBuffer();
        while(st.hasMoreTokens()){
        	buffer.append(st.nextToken());
        }
        return buffer.toString();
	}
	
	/**
	 * 解码IP为十六进制字符串格式
	 * @param maskIP
	 * @return
	 */
	public static String unMaskIpToHexString(String maskIP){
		StringTokenizer st = new StringTokenizer(maskIP,".");
        StringBuffer buffer = new StringBuffer();
        while(st.hasMoreTokens()){
        	buffer.append(lpad(Integer.toHexString(Integer.valueOf(st.nextToken())),2));
        }
        return buffer.toString();
	}
	
	// 屏蔽大小端产生的异常
	public static short[] translateParamToShortArrayForPort(String str) {
		int t = Integer.parseInt(str);
		short m1 = (short) (t >> 8);// 低位
		short m2 = (short) (t & 0x00ff);// 高位
		return new short[] { m2, m1 };
	}

	public static List<Integer> unmaskIp2Collection(String str) {
		StringTokenizer st = new StringTokenizer(str, ".");
		List<Integer> result = new ArrayList<Integer>();
		while (st.hasMoreTokens()) {
			result.add(Integer.valueOf(st.nextToken()));
		}
		return result;
	}
	//统一系统中的空串处理
	public static String getNullStr(){
		return "";
	}
}
