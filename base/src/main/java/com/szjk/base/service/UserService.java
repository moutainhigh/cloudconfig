package com.szjk.base.service;

import com.szjk.base.mapper.UserMapper;
import com.szjk.base.model.user.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public List<UserVo> list(){
        return userMapper.list();
    }
}
