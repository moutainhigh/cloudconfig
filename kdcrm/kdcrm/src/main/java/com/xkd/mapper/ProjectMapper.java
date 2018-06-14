package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ProjectMapper {

	public int insertProject(Map<String, Object> map);

	public int updateProject(Map<String, Object> map);
	
	public List<Map<String,Object>> selectProjectByCompanyId(@Param("companyId") String companyId);
	
	public Map<String,Object> selectProjectById(@Param("id") String id);
	
	public int deleteProjectByCompanyIds(@Param("companyId") String companyId);
	
	
 	
	
}
