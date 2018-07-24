package com.wjh.userroleservice.mapper;

import com.wjh.userroleservicemodel.model.UserRolePo;
import com.wjh.userroleservicemodel.model.UserRoleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {
    public int insertList(@Param("list") List<UserRolePo> userRolePoList);

    public int deleteByUserId(@Param("userId") Long userId);

    public int deleteByRoleId(@Param("roleId") Long roleId);


    public List<UserRoleVo> listByUserId(@Param("userId")Long userId);

}
