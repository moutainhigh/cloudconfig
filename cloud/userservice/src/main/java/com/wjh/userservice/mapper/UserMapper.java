package com.wjh.userservice.mapper;

import com.wjh.userservicemodel.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    public User detailByMobile(@Param("mobile") String mobile);


    public int insert(User user);


    public int update(User user);


    public User selectByLogin(@Param("mobile") String mobile, @Param("password") String password);


    public int delete(@Param("id") long id);

}
