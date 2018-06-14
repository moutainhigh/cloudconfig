package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface CompanyNeedMapper {

	/**
	 * 插入公司需求
	 * @param map
	 * @return
	 */
	public int insertCompanyNeed(Map<String, Object> map);
	
	
	 /**
	  * 更新公司需求
	  * @param map
	  * @return
	  */
	public int updateCompanyNeed(Map<String, Object> map);
	
	/**
	 * 查询公司需求
	 * @return
	 */
	public List<Map<String,Object>> selectCompanyNeedByCompanyId(@Param("companyId") String companyId);
	
	/**
	 * 删除需求
	 * @param id
	 * @return
	 */
	public int deleteCompanyNeedById(@Param("id") String id);
	
	public Map<String,Object> selectComopanyNeedById(@Param("id") String id);
	
	
}
