package com.wjh.userservice.service;


import com.wjh.userservice.mapper.UserMapper;
import com.wjh.userservicemodel.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;
    public User detailByUser(String mobile){
        return userMapper.detailByMobile(mobile);
    }


    public int insert(User user){
        return userMapper.insert(user);
    }


}
