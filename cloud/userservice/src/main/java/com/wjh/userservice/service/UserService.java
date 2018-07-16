package com.wjh.userservice.service;


import com.wjh.userservice.mapper.UserMapper;
import com.wjh.userservicemodel.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private IdService idService;

    public User detailByUser(String mobile) {
        return userMapper.detailByMobile(mobile);
    }


    public User insert(User user) {
        long id = idService.generateId();
        user.setId(id);
        userMapper.insert(user);
        return user;
    }


    public User update(User user) {
        //密码置空，以免误改
        user.setPassword(null);
        userMapper.update(user);
        return user;

    }


    public User selectByLogin( String mobile,  String password){
        return userMapper.selectByLogin(mobile,password);
    }

    public void delete( long id){
          userMapper.delete(id);
     }


}
