package com.kuangchi.sdd.util.algorithm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class BASE64 {

    /** 动态密码 */
    public static String encode(String str) {
    	byte[] bytestr=null;
        try {
        	bytestr=str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return new BASE64Encoder().encode(bytestr);
    }

    public static String decode(String str) {
        if (str == null)
            return null;
        try {
            return new String(new BASE64Decoder().decodeBuffer(str),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String arg[]) {
        System.out.println(BASE64.decode("admin"));
    }
}