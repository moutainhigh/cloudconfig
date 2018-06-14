package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserGiftMapper {


    Integer saveUserGift(Map<String, Object> map);

    List<Map<String,Object>> selectUserGiftByMobileOrOpenId(@Param("activityId")String activityId, @Param("mobile")String mobile, @Param("openId")String openId);

    List<Map<String,Object>> selectUserGiftsByActivityId(@Param("activityId")String activityId);

    Integer updateUserGiftById(@Param("id")String id, @Param("gift")String gift);
}
