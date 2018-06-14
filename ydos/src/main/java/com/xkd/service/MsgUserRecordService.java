package com.xkd.service;

import com.xkd.mapper.MsgUserRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-3-6;
 */

@Service
public class MsgUserRecordService {

    @Autowired
    MsgUserRecordMapper msgUserRecordMapper;

    public List<Map<String, Object>> selectRecordListByUserId(String userId, Integer currentPage, Integer pageSize){
        Integer start = (currentPage - 1)*pageSize;
        List<Map<String, Object>> msgRecordList =  msgUserRecordMapper.selectRecordByUserId(userId, start, pageSize);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> msgRecord : msgRecordList){
            Timestamp datetime = (Timestamp) msgRecord.get("datetime");
            msgRecord.put("datetime", format.format(datetime));
        }
        return msgRecordList;
    }

    public Integer countRecordByUserId(String userId){
        return msgUserRecordMapper.countRecordByUserId(userId);
    }

}
