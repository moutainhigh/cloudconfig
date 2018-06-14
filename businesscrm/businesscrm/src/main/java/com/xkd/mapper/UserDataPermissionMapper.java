package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/14.
 */
public interface UserDataPermissionMapper {
    public List<String>   selectDepartmentIdsByUserId(@Param("userId") String userId);

    public int insertUserDataPermissionList(List<Map<String,Object>> mapList);

    public int deleteUserDataPermissionByUserId(@Param("userId")String userId);

    public int deleteUserDataPermissionByUserIds(@Param("userIdList")List<String> userIdList);

    public  int deleteUserDataPermissionByUserIdAndDepartmentId(@Param("userId") String userId,@Param("departmentId") String departmentId);





}
