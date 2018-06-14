package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.UserInfo;

public interface UserInfoMapper {

	Map<String,Object> selectUserInfoById(@Param("id") String id);

	List<Map<String,Object>> selectUserInfoByCompanyId(@Param("companyId") String companyId);


	Integer insertUserInfo(Map<String, Object> userInfo);

	int updateUserInfoById(Map<String,Object> map);



	public int deleteUserInfo(@Param("id") String id);
	


	List<Map<String,Object>> searchUserInfo(@Param("companyId")String companyId,@Param("searchValue")String searchValue,@Param("departmentIdList")List<String>  departmentIdList,@Param("publicFlag")Integer publicFlag,@Param("loginUserId")String loginUserId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

	int searchUserInfoCount(@Param("companyId")String companyId,@Param("searchValue")String searchValue,@Param("departmentIdList")List<String>  departmentIdList,@Param("publicFlag")Integer publicFlag,@Param("loginUserId")String loginUserId);


	Map<String,Object> selectUserInfoByMobileAndCompanyId(@Param("companyId")String companyId,@Param("mobile")String mobile);


}
