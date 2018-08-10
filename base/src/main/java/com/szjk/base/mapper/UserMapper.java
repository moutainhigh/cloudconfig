package com.szjk.base.mapper;

import com.szjk.base.model.user.UserPo;
import com.szjk.base.model.user.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    public UserVo detailByMobile(@Param("mobile") String mobile);


    public int insert(UserPo user);


    public int update(UserPo user);


    public UserVo selectByLogin(@Param("mobile") String mobile, @Param("password") String password);


    public int delete(@Param("id") long id);

    public List<UserVo> selectByIds(@Param("idList") List<Long> userIdList);


    public List<UserVo> list();

}
