package com.wjh.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {

  public   enum ALGRITH {
        MD5("md5"), SHA_512("sha-512");
        String value;

        ALGRITH(String value) {
            this.value = value;
        }
    }

    final static String[] strs = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String md5(String str, ALGRITH algrith) {
        String result = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algrith.value);
            byte[] bytes = messageDigest.digest(str.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(getHexStringByByte(bytes[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;

    }


    public static String getHexStringByByte(byte b) {
        StringBuffer sb = new StringBuffer();
        int high = (b >> 4) & 0x0f;
        int low = b & (0x0f);
        sb.append(strs[high]).append(strs[low]);
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println(getHexStringByByte((byte) 128));
        System.out.println(md5("这是一美丽的地方", ALGRITH.SHA_512));
    }

}
