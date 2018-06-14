package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserPayMapper {

    public Map<String,String> getUserPayByOpenId(@Param("openId")String openId);

    void saveUserPay(Map<String, String> userInfo);

    void saveUserPayLogger(Map<String, String> csOrder);

    void editUserPay(Map<String, String> csOrder);

    Map<String,String> getUserPayAdviser(@Param("mobile")String mobile);

    List<Map<String,String>> getAllAdviser();

    List<Map<String,String>> getPayUserList(@Param("adviserName")String adviserName);
}
