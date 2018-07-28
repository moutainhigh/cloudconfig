package com.wjh.utils.encrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESUtil {
	final static String algrithm="DES";
	//DES加密函数 key为8个字节字符串
	public static String encrypt(String data,String  key ){	
		try {
			StringBuffer sb=new StringBuffer(key);
			for (int i = key.length(); i <8; i++) {
				sb.append("0");
			}
			key=sb.toString();
			SecureRandom sr=new SecureRandom();			
			DESKeySpec dks=new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory=SecretKeyFactory.getInstance(algrithm);
			SecretKey secureKey=keyFactory.generateSecret(dks);
			Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, secureKey,sr);
			byte[] bytes= cipher.doFinal(data.getBytes());
			BASE64Encoder base64Encoder=new BASE64Encoder();
			return base64Encoder.encode(bytes);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//DES解密函数key为8个字节字符串
	public static String decrypt(String data,String key){
		try {
			StringBuffer sb=new StringBuffer(key);
			for (int i = key.length(); i <8; i++) {
				sb.append("0");
			}
			key=sb.toString();
			BASE64Decoder base64Decoder=new BASE64Decoder();
			byte[] bytes= base64Decoder.decodeBuffer(data);
			SecureRandom sr=new SecureRandom();
			DESKeySpec dks=new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory=SecretKeyFactory.getInstance(algrithm);
			SecretKey secureKey=keyFactory.generateSecret(dks);
			Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, secureKey,sr);
			byte[] decodeBytes= cipher.doFinal(bytes);
			return new String( decodeBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void main(String[] args) {
       long time1=System.currentTimeMillis();
		
		for (int i = 0; i < 100000; i++) {
			String enc=encrypt("abcd", "12345678");
			//System.out.println(enc);
			String res=decrypt(enc, "12345678");
			//System.out.println(res);	
		}
		long time2=System.currentTimeMillis();
		System.out.println((time2-time1)/1000.0);
	
	}
}
