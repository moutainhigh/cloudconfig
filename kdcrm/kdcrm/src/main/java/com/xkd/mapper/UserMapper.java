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
	

	Integer saveUser(Map<String, Object> paramMap);

	Integer updateUserById(Map<String, Object> paramMap);

	Integer deleteUserRolesByRoles(@Param("roleIds")String roleIds);

	Integer repeatPcUserPasswordsByIds(@Param("ids")String ids, @Param("encodeRepeatPassWord")String encodeRepeatPassWord, @Param("updateBy")String updateBy);

	Integer updateTeacherInfoById(Map<String, Object> paramMap);

	Integer saveTeacherInfo(Map<String, Object> paramMap);

	Integer deleteUserByIds(@Param("idList")List<String> idList);

	Integer updateStationByCompanyIdUserId(@Param("companyId")String companyId, @Param("userId")String userId, @Param("station")String station);

	List<Map<String, Object>> selectTeacherByTeacherName(@Param("teacherName")String teacherName,@Param("pcCompanyId")String pcCompanyId);
	
	public List<Map<String,Object>>  selecUserByDepartmentIds(@Param("departmentIds") List<String>  departmentIds ,@Param("userName") String userName, @Param("start") Integer start,@Param("end") Integer end);
	
	public Integer selecTotalUserByDepartmentIds(@Param("departmentIds") List<String> departmentIds,@Param("userName") String userName);

	public List<Map<String,Object>> selectUsersUnderRole(@Param("roleId")String roleId ,@Param("userName")String userName,@Param("start")Integer start,@Param("pageSize") Integer pageSize );
	
	public Integer selectTotalUsersCountUnderRole(@Param("roleId")String roleId,@Param("userName")String userName );
	
	
	public List<Map<String,Object>> searchPcUserbyName(@Param("userName") String userName,@Param("pcCompanyId") String pcCompanyId);
	
	public int changeDepartmentByUserIds(@Param("userIds") List<String> userIds,@Param("departmentId") String departmentId,@Param("pcCompanyId")String pcCompanyId );
	
	
	public List<Map<String,Object>> selectUsersNotUnderRole(@Param("roleId")String roleId ,@Param("userName")String userName,@Param("start")Integer start,@Param("pageSize") Integer pageSize,@Param("departmentIds") List<String> departmentIds,@Param("excludeSuperRoleUser") Integer excludeSuperRoleUser);
	
	public int selectTotalUsersCountNotUnderRole(@Param("roleId")String roleId,@Param("userName")String userName,@Param("departmentIds") List<String> departmentIds,@Param("excludeSuperRoleUser") Integer excludeSuperRoleUser);
	
	public List<Map<String,Object>> selectUserDepartmentStatistic(@Param("departmentIds") List<String> departmentIds);


	public Integer deleteUserAdviserByIds(@Param("ids")String ids);


	List<Map<String, Object>> searchUserNoTitleByName(@Param("userName") String userName, @Param("currentPage")int currentPage, @Param("pageSize")int pageSize,@Param("pcCompanyId")String pcCompanyId);


	Integer searchUserNoTitleByNameTotal(@Param("userName")String userName,@Param("pcCompanyId")String pcCompanyId);


    List<Map<String,Object>> selectUsers(@Param("pcCompanyId")String pcCompanyId,@Param("content")String content);

    int getPrivatePermission(@Param("userId")String loginUserId,@Param("url") String url);
}
