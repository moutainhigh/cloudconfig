package com.xkd.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.CompanyNeedMapper;

@Service
public class CompanyNeedService {

	@Autowired
	CompanyNeedMapper companyNeedMapper;
	/**
	 * 插入公司需求
	 * @param map
	 * @return
	 */
	public int insertCompanyNeed(Map<String,Object> map){
		return companyNeedMapper.insertCompanyNeed(map);
	}
	
	
	 /**
	  * 更新公司需求
	  * @param map
	  * @return
	  */
	public int updateCompanyNeed(Map<String,Object> map){
		return companyNeedMapper.updateCompanyNeed(map);
	}
	
	
	
	/**
	 * 查询公司需求
	 * @return
	 */
	public List<Map<String,Object>> selectCompanyNeedByCompanyId(String companyId){
		return companyNeedMapper.selectCompanyNeedByCompanyId(companyId);
				
	}
	
	
	/**
	 * 删除需求
	 * @param id
	 * @return
	 */
	public int deleteCompanyNeedById(  String id){
		return companyNeedMapper.deleteCompanyNeedById(id);
	}
	
	
	
	public Map<String,Object> selectComopanyNeedById(@Param("id") String id){
		return companyNeedMapper.selectComopanyNeedById(id);
	}
}
