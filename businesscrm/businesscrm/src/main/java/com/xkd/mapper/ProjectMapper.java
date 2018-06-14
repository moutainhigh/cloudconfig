package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface ProjectMapper {





	List<Map<String, Object>> selectProjectsByContent(Map<String, Object> paramMap);

	int deleteProjectByIdList(@Param("idList") List<String> idList);




	Map<String, Object> selectProjectById(@Param("id") String id);


	int insertProject(Map<String,Object> map);


	int selectTotalProjectsByContent(Map<String, Object> map);

	Map<String,Object> selectProjectUnDeleted(@Param("projectCode")String projectCode);

	List<Map<String,Object>> selectProjectByIds(@Param("idList")List<String> idList);

	int updateProject(Map<String,Object> map);




}