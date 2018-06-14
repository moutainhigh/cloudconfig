package com.xkd.service;

import com.xkd.mapper.MsgDeviceCurrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-3-5;
 */
@Service
public class MsgDeviceCurrService {

    @Autowired
    private MsgDeviceCurrMapper msgDeviceCurrMapper;

    public Map<String, Object> selectAbnormalDeviceMsg(String userId, String boxId){
        return msgDeviceCurrMapper.selectAbnormalDeviceMsg(userId, boxId);
    }

    public Integer ignoreMessage(String messageId){
        return msgDeviceCurrMapper.ignoreMessage(messageId);
    }
}
