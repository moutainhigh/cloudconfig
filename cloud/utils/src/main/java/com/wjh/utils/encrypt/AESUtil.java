package com.wjh.utils.encrypt;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtil {
	final static String algrithm="AES";
	public static String  encrypt(String data,String key){
		BASE64Encoder encoder=new BASE64Encoder();
		try {
			StringBuffer sb=new StringBuffer(key);
			if (key.length()<16) {
			   for (int i = key.length(); i <16; i++) {
				sb.append("0");
			}
			}
			key=sb.toString();
			SecretKey secretKey=new SecretKeySpec(key.getBytes(), algrithm);
			Cipher cipher=Cipher.getInstance(algrithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] resultBytes=cipher.doFinal(data.getBytes("UTF-8"));
			return new String(encoder.encode(resultBytes));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	
	public static String  decrypt(String data,String key){
		BASE64Decoder base64Decoder=new BASE64Decoder();
		try {
			StringBuffer sb=new StringBuffer(key);

			if (key.length()<16) {
				   for (int i = key.length(); i <16; i++) {
					sb.append("0");
				}
				}
			key=sb.toString();
			SecretKey secretKey=new SecretKeySpec(key.getBytes(), algrithm);
			Cipher cipher=Cipher.getInstance(algrithm);
			cipher.init(Cipher.DECRYPT_MODE,secretKey);
			byte[] encryptBytes=base64Decoder.decodeBuffer(data);
			byte[] decryptBytes=cipher.doFinal(encryptBytes);
			return new String(decryptBytes,"UTF-8");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static void main(String[] args) {
		long time1=System.currentTimeMillis();
		
		for (int i = 0; i < 100000; i++) {
			String enc=encrypt("abcd", "12345678");
			System.out.println(enc);
			String res=decrypt(enc, "12345678");
			System.out.println(res);	
		}
		long time2=System.currentTimeMillis();
		System.out.println((time2-time1)/1000.0);
	
	}
}
