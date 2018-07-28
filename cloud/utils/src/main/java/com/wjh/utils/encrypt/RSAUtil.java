package com.wjh.utils.encrypt;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class RSAUtil {
	final static String algrithm="RSA";
	//获得密钥对 
	public static Map<String, String>   generateKeyPaires(){
		HashMap<String, String> keyMap=new HashMap<String, String>();
		try {
			KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(algrithm);
			keyPairGenerator.initialize(1024);
			KeyPair rsaKeyPair=keyPairGenerator.generateKeyPair();
			RSAPublicKey publicKey=(RSAPublicKey) rsaKeyPair.getPublic();
			RSAPrivateKey privateKey=(RSAPrivateKey) rsaKeyPair.getPrivate();
			BASE64Encoder base64Encoder=new BASE64Encoder();
			String privateKeyString=base64Encoder.encode(privateKey.getEncoded());
			String publicKeyString=base64Encoder.encode(publicKey.getEncoded());
			
			keyMap.put("public", publicKeyString);
			keyMap.put("private", privateKeyString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyMap;
	}
	
	//用公钥加密
	public static String encryptWithPublicKey(String data,String key){
		BASE64Decoder base64Decoder=new BASE64Decoder();
		BASE64Encoder base64Encoder=new BASE64Encoder();
		try {
			byte[] keyBytes=base64Decoder.decodeBuffer(key);
			X509EncodedKeySpec x509EncodedKeySpec=new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory=KeyFactory.getInstance(algrithm);
			Key publicKey=keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] bytes= cipher.doFinal(data.getBytes("UTF-8"));
			return base64Encoder.encode(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	//用私钥解密
	public static String decryptWithPrivateKey(String data,String key){
		BASE64Decoder base64Decoder=new BASE64Decoder();
		try {
			byte[] keyBytes=base64Decoder.decodeBuffer(key);
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory=KeyFactory.getInstance(algrithm);
			Key privateKey=keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Cipher cipher=Cipher.getInstance(privateKey.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] bytes=base64Decoder.decodeBuffer(data);			
			return new String(cipher.doFinal(bytes),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	//使用私钥加密
	public static String encryptWithPrivateKey(String data,String key){
		BASE64Decoder base64Decoder=new BASE64Decoder();
		BASE64Encoder base64Encoder=new BASE64Encoder();
		try {
			byte[] keyBytes= base64Decoder.decodeBuffer(key);
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory=KeyFactory.getInstance(algrithm);
			Key privateKey=keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Cipher cipher=Cipher.getInstance(privateKey.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encryptBytes=cipher.doFinal(data.getBytes("UTF-8"));
			return new String(base64Encoder.encode(encryptBytes));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	//使用公钥解密
	public static String decryptWithPublicKey(String data,String key){
		BASE64Decoder base64Decoder=new BASE64Decoder();
		try {
			byte[] keyBytes=base64Decoder.decodeBuffer(key);
			X509EncodedKeySpec x509EncodedKeySpec=new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory=KeyFactory.getInstance(algrithm);
			PublicKey publicKey=keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher=Cipher.getInstance(publicKey.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] decryptBytes= cipher.doFinal(base64Decoder.decodeBuffer(data) );
			return new String(decryptBytes);
 		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static void main(String[] args) {
		Map<String, String> map=generateKeyPaires(); 
		String publicKey=map.get("public");
		String privateKey=map.get("private");
		String s=encryptWithPublicKey("abcdggggggggggggggg", publicKey);
		System.out.println(s);
		String s2=decryptWithPrivateKey(s, privateKey);
		System.out.println(s2);
		
		
		
		
		String s3=encryptWithPrivateKey("bbbbb", privateKey);
		System.out.println(s3);
		String s4=decryptWithPublicKey(s3, publicKey);
		System.out.println(s4);
	}
}
