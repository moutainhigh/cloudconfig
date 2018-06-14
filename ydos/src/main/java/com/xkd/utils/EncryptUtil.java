package com.xkd.utils;

/**
 * Created by dell on 2018/2/28.
 */
public class EncryptUtil {

    /**
     * 有电用户密码加密方式
     * @param str
     * @return
     */
    public  static String encryptPassword(String str){
       return MD5Util.MD5Encode(str+"You__DI_AN__",null).toUpperCase();
    }



}
