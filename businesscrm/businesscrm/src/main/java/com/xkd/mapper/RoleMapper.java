package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Role;

public interface RoleMapper {

	Integer saveRole(Role role);



	Integer updateRole(Map<String, Object> roleMap);


	List<Map<String, Object>> selectRoles(@Param("content") String content, @Param("currentPage") int currentPage, @Param("pageSize") int pageSize);

	Integer deleteRolesByIds(@Param("idList") List<String> idList);
	List<Map<String,Object>>  selectRolesByIds(@Param("idList") List<String> idList);

	Map<String,Object> selectRoleById(@Param("id") String id);

	List<Map<String,Object>> selectRolesUnderCompany(@Param("pcCompanyId")String pcCompanyId,@Param("includeSuperAdmin")  Integer includeSuperAdmin );

 
	Integer selectRolesCount(@Param("content") String content);


	
	
	int deleteRole(@Param("id") String id);
	
	public List<Map<String, Object>> selectRoleByName(@Param("roleName") String roleName);

    List<Map<String,Object>> selectOperateIdRoleId(@Param("roleId")String roleId);

}
