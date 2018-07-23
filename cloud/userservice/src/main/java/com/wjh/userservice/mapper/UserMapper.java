package com.wjh.userservice.mapper;

import com.wjh.userservicemodel.model.UserPo;
import com.wjh.userservicemodel.model.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    public UserVo detailByMobile(@Param("mobile") String mobile);


    public int insert(UserPo user);


    public int update(UserPo user);


    public UserVo selectByLogin(@Param("mobile") String mobile, @Param("password") String password);


    public int delete(@Param("id") long id);

}
