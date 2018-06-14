package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.DC_PC_User;

public interface DC_PC_UserMapper {


	List<HashMap<String, Object>> selectUserInfo(@Param("uname") String uname);

	
	DC_PC_User getUserById(String id);

	Integer savePcUserMap(Map<String, Object> map);

	Integer updatePcUserMap(Map<String, Object> map);

	Integer deletePcUserByIds(@Param("ids") String ids);

	List<Map<String, Object>> selectPcUsersByContent(@Param("content") String content, @Param("currentPage") int currentPage, @Param("pageSize") int pageSize);

	Map<String, Object> selectPcUserById(@Param("id") String id);

	Integer repeatPcUserPasswordsByIds(@Param("ids") String ids, @Param("encodeRepeatPassWord") String encodeRepeatPassWord, @Param("updateBy") String updateBy);

	Integer updateUserPasswordById(@Param("id") String id, @Param("password") String password);

	Map<String, Object> selectUserByEmail(@Param("email") String email);

	DC_PC_User getUserByTel(@Param("mobile") String mobile);

	Integer selectPcUsersByContentCount(@Param("content") String content);

	Integer deletePcUserRolesByRoles(@Param("roleIds") String roleIds);

	Integer updatePcUserInfoById(Map<String, Object> map);


}
