package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface UserMapper {

	Map<String, Object> selectUserByMobile(@Param("mobile")String mobile , @Param("platform")Integer platform);



	Map<String,Object> selectUserByOnlyMobile(@Param("mobile") String mobile);
	
	Map<String, Object> selectUserByEmail(@Param("email")String email , @Param("platform")Integer platform);

	Map<String, Object> selectUserById(@Param("id")String id);

	List<Map<String, Object>> selectUserByTeacherType(@Param("teacherType")String teacherType,@Param("pcCompanyId")String pcCompanyId);

	List<Map<String, Object>> selectTeachers(@Param("pageSizeInt")int pageSizeInt, @Param("currentPageInt")int currentPageInt,@Param("pcCompanyId")String pcCompanyId);

	Integer selectTeacherCount(@Param("pcCompanyId")String pcCompanyId);

	List<Map<String, Object>> selectUsersByContent(@Param("content")String content, @Param("currentPageInt")int currentPageInt, @Param("pageSizeInt")int pageSizeInt);

	Integer selectUsersByContentCount(@Param("content")String content);

	Integer updateUserPasswordById(@Param("id")String id, @Param("password")String password);
	
	Integer updateDcUserDetail(Map<String,Object> user);
	Integer insertDcUserDetail(Map<String,Object> user);
	Integer updateDcUser(Map<String,Object> user);
	Integer insertDcUser(Map<String,Object> user);
	



	Integer deleteUserRolesByRoles(@Param("roleIdList")List<String> roleIdList);

	List<String> selectUserIdByRoleIds(@Param("roleIdList") List<String > roleIdList);

	List<String > selectUserIdByDepartmentIds(@Param("departmentIdList")List<String > departmentIdList);

	List<String> selectUserIdByPcCompanyIds(@Param("pcCompanyIdList")List<String> pcCompanyIdList);

	Integer repeatPcUserPasswordsByIds(@Param("idList")List<String> idList, @Param("encodeRepeatPassWord")String encodeRepeatPassWord, @Param("updateBy")String updateBy);



	Integer deleteUserByIds(@Param("idList")List<String> idList);



	public List<Map<String,Object>>  selecUserByDepartmentIds(@Param("departmentIds") List<String>  departmentIds ,@Param("userName") String userName, @Param("start") Integer start,@Param("end") Integer end);
	
	public Integer selecTotalUserByDepartmentIds(@Param("departmentIds") List<String> departmentIds,@Param("userName") String userName);

	public List<Map<String,Object>> selectUsersUnderRole(@Param("roleId")String roleId ,@Param("userName")String userName,@Param("start")Integer start,@Param("pageSize") Integer pageSize );
	
	public Integer selectTotalUsersCountUnderRole(@Param("roleId")String roleId,@Param("userName")String userName );
	
	
	public List<Map<String,Object>> searchPcUserbyName(@Param("userName") String userName,@Param("departmentIdList") List<String> departmentIdList);
	
	public int changeDepartmentByUserIds(@Param("userIds") List<String> userIds,@Param("departmentId") String departmentId,@Param("pcCompanyId")String pcCompanyId );
	
	
	public List<Map<String,Object>> selectUsersNotUnderRole(@Param("roleId")String roleId ,@Param("userName")String userName,@Param("start")Integer start,@Param("pageSize") Integer pageSize,@Param("departmentIds") List<String> departmentIds,@Param("excludeSuperRoleUser") Integer excludeSuperRoleUser);
	
	public int selectTotalUsersCountNotUnderRole(@Param("roleId")String roleId,@Param("userName")String userName,@Param("departmentIds") List<String> departmentIds,@Param("excludeSuperRoleUser") Integer excludeSuperRoleUser);
	
	public List<Map<String,Object>> selectUserDepartmentStatistic(@Param("departmentIds") List<String> departmentIds);




	List<Map<String, Object>> searchUserNoTitleByName(@Param("userName") String userName, @Param("currentPage")int currentPage, @Param("pageSize")int pageSize,@Param("pcCompanyId")String pcCompanyId);


	Integer searchUserNoTitleByNameTotal(@Param("userName")String userName,@Param("pcCompanyId")String pcCompanyId);


    List<Map<String,Object>> selectUsers(@Param("pcCompanyId")String pcCompanyId,@Param("content")String content);


	List<Map<String,Object>> selectAllUsers();




}
