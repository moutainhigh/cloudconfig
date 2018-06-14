package com.kuangchi.sdd.util.commonUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.kuangchi.sdd.util.algorithm.BASE64;
import com.kuangchi.sdd.util.algorithm.DES;
import com.kuangchi.sdd.util.algorithm.MD5;

public class EncodeUtil {

    public static String encryptMD5(String str) {
        return encrypt(str, "MD5");
    }

    public static String encryptSHA(String str) {
        return encrypt(str, "SHA");
    }

    public static String encrypt(String str, String algorithm) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
            digest.update(str.getBytes("utf-8"));
            return toHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        	return null;
		}
    }

    public static String toHex(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 3);
        for (int i = 0; i < b.length; ++i) {
            sb.append(Character.forDigit((b[i] & 0xF0) >> 4, 16));
            sb.append(Character.forDigit(b[i] & 0xF, 16));
        }
        return ((sb != null) ? sb.toString().toUpperCase() : null);
    }

    public static String encode(String str) {
        return MD5.getInstance().encryption(str);
        // return DES.encode(str);
        // return BASE64.encode(str);
    }

    public static String decode(String str) {
        return MD5.getInstance().encryption(str);
        // return DES.decode(str);
        // return BASE64.decode(str);
    }

    public static String encode(String str, String algorithm) {
        if ("BASE64".equals(algorithm))
            return BASE64.encode(str);
        if ("DES".equals(algorithm))
            return DES.encode(str);
        return new NoSuchAlgorithmException().getMessage();
    }

    public static String decode(String str, String algorithm) {
        if ("BASE64".equals(algorithm))
            return BASE64.decode(str);
        if ("DES".equals(algorithm))
            return DES.decode(str);
        return new NoSuchAlgorithmException().getMessage();
    }

    public static void main(String arg[]) {
        
        System.out.println(DES.decode("piggifibmihmaoefppdobbmbpejflliiblpgoohjcfkcmdjleompbpfilnpakplmdpciknmhggkaehio"));
        System.out.println(DES.encode("getPersonXml"));
//        System.out.println(new BigDecimal(123456789.02).toString());
//        System.out.println(new BigDecimal("123456789.02").toString());


        // int[] seeds = new int[3];
        // for(int i=0;i<seeds.length;i++) {
        // System.out.println(i);
        // }
        // String s1 = new String("hello");
        // String s2 = new String("hello");
        // if(s1.equals(s2))
        // System.out.println("equals");
        // else
        // System.out.println("no equals");
        //        
        // StringBuffer sb1 = new StringBuffer("hello");
        // StringBuffer sb2 = new StringBuffer("hello");
        // if(sb1.equals(sb2))
        // System.out.println("equals");
        // else
        // System.out.println("no equals");

//        Queue<String> queue = new PriorityQueue<String>();
//        queue.add("A");
//        queue.add("B");
//        queue.add("C");
//        queue.add("D");
//        for (String q : queue) {
//            System.out.println(q);
//        }
//        queue.remove();
//        queue.add("F");
//        System.out.println("*******");
//        for (String q : queue) {
//            System.out.println(q);
//        }
//
//        System.out.println(EncodeUtil.encode("test"));

//        SimpleObservable observable01 = new SimpleObservable("SimpleObservable01");
//        SimpleObservable observable02 = new SimpleObservable("SimpleObservable02");
//        SimpleObserver observer1_0 = new SimpleObserver("SimpleObserver1-0");
////        SimpleObserver observer1_1 = new SimpleObserver("SimpleObserver1-1");
//        observable01.addObserver(observer1_0);
////        observable01.addObserver(observer1_1);
//        observable02.addObserver(observer1_0);
////        observable02.addObserver(observer1_1);
//        observable01.setData(1);
//        observable01.setData(2);
//        observable01.setData(2);
//        observable01.setData(3);
//        observable02.setData(100);
//        observable02.setData(110);
        // System.out.println(DES.decode("kcknocfgieejpokd"));
    }
}