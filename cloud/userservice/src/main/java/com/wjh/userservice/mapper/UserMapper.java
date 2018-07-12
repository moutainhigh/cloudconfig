package com.wjh.userservice.mapper;

 import com.wjh.userserviceapi.model.User;
 import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
 public interface UserMapper {

    public User detailByMobile(@Param("mobile")  String mobile);


    public int insert(User user);



}
