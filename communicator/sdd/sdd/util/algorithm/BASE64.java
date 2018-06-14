package com.kuangchi.sdd.util.algorithm;

import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class BASE64 {

    /** 动态密码 */
    public static String encode(String str) {
        return new BASE64Encoder().encode(str.getBytes());
    }

    public static String decode(String str) {
        if (str == null)
            return null;
        try {
            return new String(new BASE64Decoder().decodeBuffer(str));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String arg[]) {
        System.out.println(BASE64.decode("admin"));
    }
}