package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface DepartmentDao {
	public int update(Map<String, Object> map);
	
	public int insert(Map<String, Object> map);
	
	public List<Map<String,Object>>  selectAllDepartment();
	
	public int delete(@Param("id") String id);
	
	public List<Map<String,Object>> selectDepartmentByName(@Param("departmentName") String departmentName);

	public Map<String,Object> selectDepartmentById(@Param("id")String id);

    List<Map<String,Object>> getTreeByPid(@Param("id") String id);

	int getChildTreeUserSum(@Param("childUserId") List<String> childUserId);


	public List<Map<String ,Object>>  selectDepartmentByIds(@Param("idList")List<String> idList);

	public void  updateDataDepartmentId(@Param("table")String  table,@Param("newDepartmentId")String  newDepartmentId ,@Param("departmentIdList")List<String>  departmentIdList);

	public int selectDataCount(@Param("table")String  table,@Param("departmentIdList")List<String>  departmentIdList);


	public List<Map<String,Object>> selectSuperRoleDepartment();
}
