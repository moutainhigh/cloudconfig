package com.xkd.service;

import com.xkd.mapper.FieldShowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by dell on 2018/4/17.
 */
@Service
public class FieldShowService {

    @Autowired
    FieldShowMapper fieldShowMapper;

    public List<Map<String,Object>> selectFieldShow(String module){
        return fieldShowMapper.selectFieldShow(module);
    }




    public List<Map<String,Object>> selectUserFieldShow(  String userId,String module){
        return fieldShowMapper.selectUserFieldShow(userId,module);
    }

    public 	void updateFieldUserShow( String userId,List<Map<String,Object>> fieldList,String module){
        fieldShowMapper.deleteByUserId(userId,module);
        if (fieldList.size()>0){
            for (int i = 0; i <fieldList.size() ; i++) {
                Map<String,Object> map=fieldList.get(i);
                map.put("id", UUID.randomUUID().toString());
                map.put("userId",userId);
                map.put("module",module);
            }
            fieldShowMapper.insertFieldUserList(fieldList);
        }

    }


}
