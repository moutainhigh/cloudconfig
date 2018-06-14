package com.xkd.service;

import com.xkd.mapper.AllowHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by dell on 2018/3/3.
 */
@Service
public class AllowHistoryService {
    @Autowired
    AllowHistoryMapper allowHistoryMapper;
    public int insert(Map<String,Object> map ){
        return allowHistoryMapper.insert(map);
    }

    public int update(Map<String,Object> map){
        return  allowHistoryMapper.update(map);
    }

    public Map<String,Object> selectById(String id){
        return allowHistoryMapper.selectById(id);
    }


}
