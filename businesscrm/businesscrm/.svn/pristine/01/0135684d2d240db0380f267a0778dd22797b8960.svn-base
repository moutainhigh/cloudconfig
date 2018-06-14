package com.xkd.service;

import com.xkd.mapper.BankProjectUserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by dell on 2017/12/15.
 */
@Service
public class BankProjectUserService {
    @Autowired
    BankProjectUserMapper bankProjectUserMapper;


    public int insertBankProjectUserList(String bankProjectId,String pointId,List<String>  userIdList){
        for (int i = 0; i <userIdList.size() ; i++) {
            List<Map<String,Object>>  mapList=new ArrayList<>();
            Map<String,Object> map=new HashMap<>();
            String id= UUID.randomUUID().toString();
            map.put("id",id);
            map.put("bankProjectId",bankProjectId);
            map.put("pointId",pointId);
            map.put("userId",userIdList.get(i));
            mapList.add(map);
            bankProjectUserMapper.deleteByBankIdAndProjectId(bankProjectId, pointId,userIdList.get(i));
             bankProjectUserMapper.insertBankProjectUserList(mapList);

        }
        return userIdList.size();
    }

    public List<Map<String,Object>> selectUserNotUnderProject(String bankProjectId,String pointId,String uname,String sex,Integer currenPage,Integer pageSize){
        Integer start=0;
        if (currenPage==null||currenPage<0){
            currenPage=1;
        }
        start=(currenPage-1)*pageSize;
        return  bankProjectUserMapper.selectUserNotUnderProject(bankProjectId,pointId,uname,  sex,start,pageSize);
    }

    public int selectUserNotUnderProjectCount(String bankProjectId,String pointId,String uname,String sex){
        return  bankProjectUserMapper.selectUserNotUnderProjectCount(bankProjectId,pointId,uname,sex);
    }




    public List<Map<String ,Object>>  selectUserUnderProject(String bankProjectId,String pointId,String uname,String sex,Integer currenPage,Integer pageSize){
        Integer start=0;
        if (currenPage==null||currenPage<0){
            currenPage=1;
        }
        start=(currenPage-1)*pageSize;
        return  bankProjectUserMapper.selectUserUnderProject(bankProjectId, pointId,uname,sex,start,pageSize);
    }

    public  Integer selectUserUnderProjectCount(String bankProjectId,String pointId,String uname,String sex){
        return  bankProjectUserMapper.selectUserUnderProjectCount(bankProjectId,pointId,uname,sex);
    }

    public int deleteBankProjectUser(List<String> idList){
        if (idList.size()>0){
            return bankProjectUserMapper.deleteBankProjectUser( idList);

        }
        return 0;
    }






}
