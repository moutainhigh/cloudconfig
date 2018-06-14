package com.xkd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.ProjectMapper;

@Service
public class ProjectService {

	
	@Autowired
	ProjectMapper projectMapper;
	
	public int insertProject(Map<String,Object> map){
	return projectMapper.insertProject(map);	
	}

	public int updateProject(Map<String,Object> map){
		return projectMapper.updateProject(map);
	}
	
	public List<Map<String,Object>> selectProjectByCompanyId(  String  companyId){
		return projectMapper.selectProjectByCompanyId(companyId);
	}
	
	public Map<String,Object> selectProjectById(  String id){
		return projectMapper.selectProjectById(id);
	}
	
	public void deleteProject(String id){
		Map<String,Object> map=new HashMap<>();
		map.put("id", id);
		map.put("status", "2");
		projectMapper.updateProject(map);		
	}
	
	
	public int deleteProjectByCompanyIds(String companyId){
		return projectMapper.deleteProjectByCompanyIds(companyId);
	}
}
