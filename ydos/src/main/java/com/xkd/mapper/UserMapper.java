package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.dev.OOXMLLister;

public interface UserMapper {

	Map<String, Object> selectUserByMobile(@Param("mobile")String mobile , @Param("platform")Integer platform);



	Map<String,Object> selectUserByOnlyMobile(@Param("mobile") String mobile);
	
	Map<String, Object> selectUserByEmail(@Param("email")String email , @Param("platform")Integer platform);

	Map<String, Object> selectUserById(@Param("id")String id);




	List<Map<String, Object>> selectUsersByContent(@Param("content")String content, @Param("currentPageInt")int currentPageInt, @Param("pageSizeInt")int pageSizeInt);

	Integer selectUsersByContentCount(@Param("content")String content);

	Integer updateUserPasswordById(@Param("id")String id, @Param("password")String password);
	
	Integer updateDcUserDetail(Map<String,Object> user);
	Integer insertDcUserDetail(Map<String,Object> user);
	Integer updateDcUser(Map<String,Object> user);
	Integer insertDcUser(Map<String,Object> user);
	

	Integer saveUser(Map<String, Object> paramMap);

	Integer updateUserById(Map<String, Object> paramMap);

	Integer deleteUserRolesByRoles(@Param("roleIds")String roleIds);

	Integer repeatPcUserPasswordsByIds(@Param("ids")String ids, @Param("encodeRepeatPassWord")String encodeRepeatPassWord, @Param("updateBy")String updateBy);



	Integer deleteUserByIds(@Param("idList")List<String> idList);



	public List<Map<String,Object>>  selecUserByDepartmentIds(@Param("departmentIds") List<String>  departmentIds ,@Param("userName") String userName, @Param("start") Integer start,@Param("end") Integer end);
	
	public Integer selecTotalUserByDepartmentIds(@Param("departmentIds") List<String> departmentIds,@Param("userName") String userName);

	public List<Map<String,Object>> selectUsersUnderRole(@Param("roleId")String roleId ,@Param("userName")String userName,@Param("start")Integer start,@Param("pageSize") Integer pageSize );
	
	public Integer selectTotalUsersCountUnderRole(@Param("roleId")String roleId,@Param("userName")String userName );
	
	
	public List<Map<String,Object>> searchPcUserbyName(@Param("userName") String userName,@Param("pcCompanyId") String pcCompanyId);
	
	public int changeDepartmentByUserIds(@Param("userIds") List<String> userIds,@Param("departmentId") String departmentId,@Param("pcCompanyId")String pcCompanyId );
	
	
	public List<Map<String,Object>> selectUsersNotUnderRole(@Param("roleId")String roleId ,@Param("userName")String userName,@Param("start")Integer start,@Param("pageSize") Integer pageSize,@Param("departmentIds") List<String> departmentIds,@Param("excludeSuperRoleUser") Integer excludeSuperRoleUser);
	
	public int selectTotalUsersCountNotUnderRole(@Param("roleId")String roleId,@Param("userName")String userName,@Param("departmentIds") List<String> departmentIds,@Param("excludeSuperRoleUser") Integer excludeSuperRoleUser);
	
	public List<Map<String,Object>> selectUserDepartmentStatistic(@Param("departmentIds") List<String> departmentIds);




	List<Map<String, Object>> searchUserNoTitleByName(@Param("userName") String userName, @Param("currentPage")int currentPage, @Param("pageSize")int pageSize,@Param("pcCompanyId")String pcCompanyId);


	Integer searchUserNoTitleByNameTotal(@Param("userName")String userName,@Param("pcCompanyId")String pcCompanyId);


    List<Map<String,Object>> selectUsers(@Param("pcCompanyId")String pcCompanyId,@Param("content")String content);

	List<Map<String,Object>> searchUserByMobile(@Param("mobile")String mobile);

	List<Map<String,Object>> searchTeamers(@Param("pcCompanyId")String pcCompanyId,@Param("uname") String uname,@Param("level")Integer level,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

	Integer searchTeamersCount(@Param("pcCompanyId")String pcCompanyId,@Param("uname") String uname,@Param("level")Integer level);


	List<Map<String,Object>> searchAllUserByMobileAndRole(@Param("roleId") String roleId,@Param("mobile")String mobile,@Param("excludeExists") Integer excludeExists,@Param("pcCompanyId") String pcCompanyId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

	Integer searchAllUserCountByMobileAndRole(@Param("roleId") String roleId,@Param("mobile")String mobile,@Param("excludeExists") Integer excludeExists,@Param("pcCompanyId") String pcCompanyId);

	List<Map<String,Object>> selectStaff(@Param("pcCompanyId") String pcCompanyId,@Param("uname") String uname,@Param("level")Integer level,@Param("departmentIdList")List<String> departmentIdList,@Param("roleId")Integer roleId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

	Integer selectStaffCount(@Param("pcCompanyId") String pcCompanyId,@Param("uname") String uname,@Param("level")Integer level,@Param("departmentIdList")List<String> departmentIdList,@Param("roleId")Integer roleId);


	Integer deleteStaff(@Param("id")String id);

   Map<String,Object> selectUserByMobileAndappFlag(@Param("tel")String tel, @Param("platform")int platform, @Param("appFlag")String appFlag);


	List<Map<String,Object>> selectCustomerContactor(@Param("companyIdList")List<String> companyIdList,@Param("pcCompanyId")String pcCompanyId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);
	int selectCustomerContactorCount(@Param("companyIdList")List<String> companyIdList,@Param("pcCompanyId")String pcCompanyId);

	List<Map<String,Object>> selectAllUserIdByPcCompanyId(@Param("pcCompanyId")String pcCompanyId);

	List<Map<String, String>> selectAllRoleId3();

	List<String> getUserId();

	List<String> selectUserByPcCompanyIdAndRoleId(@Param("pcCompanyId") String pcCompanyId, @Param("roleId") String roleId);

	List<Map<String,Object>> selectAllUsers();

}
