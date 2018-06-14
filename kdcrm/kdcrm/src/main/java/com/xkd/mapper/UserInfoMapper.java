package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.UserInfo;

public interface UserInfoMapper {

	UserInfo selectUserInfoById(@Param("id") String id);

	List<Map<String,Object>> selectUserInfoByCompanyId(@Param("companyId") String companyId);


	Integer insertUserInfo(Map<String, Object> userInfo);

	Integer updateUserInfoById(UserInfo userInfo);


	Integer deleteUserInfo(@Param("userId") String userId);


 

	Integer deleteUserInfoByCompanyId(@Param("companyId") String companyId);


	Integer updateUserInfoByIdByInvitation(UserInfo userInfo);
	
	
	List<Map<String,Object>> selectUserCompanyByMobileAndCompanyId(@Param("mobile") String mobile, @Param("companyId") String companyId);
	
	Map<String,Object> selectUserCompanyById(@Param("id") String id);
	
	public int deleteUserCompany(@Param("id") String id);
	
	public int updateUserStationById(@Param("id") String id, @Param("station") String station) ;
	
	public int deleteByUserIdAndCompanyId(@Param("userId") String userId, @Param("companyId") String companyId);

	int deleteUserCompanyByUserIds(@Param("userIdList") List<String> userIdList);
	
	
	List<Map<String,Object>> selectUserCompanyByCompanyIds(@Param("companyIds") String companyIds);


	List<String>  selectCompanyIdsByContactUserId(@Param("userId") String userId);

	List<String> selectCompanyIdsByUseInfoUserId(@Param("userId")String userId);

    Map<String,Object> selectUserInfoByUserIdAndCompanyId(@Param("userId")String userId, @Param("companyId")String companyId);

	Integer updateUserInfoByMap(Map<String, Object> existMap);

    void replaceUserInfo(Map<String, Object> userInfoMap);
}
