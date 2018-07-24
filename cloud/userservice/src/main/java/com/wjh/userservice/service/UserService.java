package com.wjh.userservice.service;


import com.wjh.userservice.mapper.UserMapper;
import com.wjh.userservicemodel.model.UserPo;
import com.wjh.userservicemodel.model.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private IdService idService;

    public UserVo detailByUser(String mobile) {
        return userMapper.detailByMobile(mobile);
    }

    public UserPo insert(UserPo user,Long loginUserId) {
        long id = idService.generateId();
        Date date=new Date();
        user.setId(id);
        user.setCreateDate(date);
        user.setUpdateDate(date);
        user.setCreatedBy(loginUserId);
        user.setUpdatedBy(loginUserId);
        userMapper.insert(user);
        return user;
    }


    public UserPo update(UserPo user,Long loginUserId) {
        Date date=new Date();
        //密码置空，以免误改
        user.setPassword(null);
        user.setUpdateDate(date);
        user.setUpdatedBy(loginUserId);
        userMapper.update(user);
        return user;

    }


    public UserVo selectByLogin(String mobile, String password){
        return userMapper.selectByLogin(mobile,password);
    }

    public void delete( long id){
          userMapper.delete(id);
     }


    public List<UserVo> selectByIds( List<Long> userIdList){
        if (userIdList.size()==0){
            return new ArrayList<>(0);
        }
        return userMapper.selectByIds(userIdList);
    }


}
