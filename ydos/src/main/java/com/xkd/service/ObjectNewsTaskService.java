package com.xkd.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xkd.mapper.ObjectNewsTaskMapper;
import com.xkd.task.PushNewsTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Created by dell on 2018/3/7.
 */
@Service
public class ObjectNewsTaskService {

    @Autowired
    ObjectNewsTaskMapper objectNewsTaskMapper;


    @Autowired
    CompanyContactorService companyContactorService;
    @Autowired
    UserService userService;

    @Autowired
    ObjectNewsService objectNewsService;

    public int insert(Map map) {
        return objectNewsTaskMapper.insert(map);
    }

    public int update(Map map) {
        return objectNewsTaskMapper.update(map);
    }

    public Map select( String id){
        return objectNewsTaskMapper.select(id);
    }

    public List<Map<String, Object>> selectList(String pcCompanyId,Integer currentPage,Integer pageSize) {
        Integer start=0;
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }
        start=(currentPage-1)*pageSize;
        return objectNewsTaskMapper.selectList(pcCompanyId,start,pageSize);
    }


    public Integer selectListCount(String pcCompanyId) {
        return objectNewsTaskMapper.selectListCount(pcCompanyId);
    }



    public List<String> selectUndoneTaskId( ){
        return objectNewsTaskMapper.selectUndoneTaskId();
    }


    public void generateTask(Map<String, Object> map) {
        objectNewsTaskMapper.insert(map);
        PushNewsTask.taskIdQueue.push((String) map.get("id"));
    }



    public void rePush( String taskId) {
        Map<String,Object> map=select(taskId);
        PushNewsTask.taskIdQueue.push((String) map.get("id"));
     }




}
