package com.xkd.service;

import com.xkd.mapper.UserGiftMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserGiftService {


    @Autowired
    private UserGiftMapper userGiftMapper;

    public Integer saveUserGift(Map<String, Object> map) {

        return userGiftMapper.saveUserGift(map);
    }

    public List<Map<String,Object>> selectUserGiftByMobileOrOpenId(String activityId, String mobile, String openId) {

        return userGiftMapper.selectUserGiftByMobileOrOpenId(activityId,mobile,openId);
    }

    public List<Map<String,Object>> selectUserGiftsByActivityId(String activityId) {

        return userGiftMapper.selectUserGiftsByActivityId(activityId);
    }

    public Integer updateUserGiftById(String id, String gift) {
        return userGiftMapper.updateUserGiftById(id,gift);
    }
}
