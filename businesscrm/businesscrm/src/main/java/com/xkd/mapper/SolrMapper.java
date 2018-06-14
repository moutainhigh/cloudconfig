package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SolrMapper {


	/**
	 * 根据公司Ids 查询公司需要建立索引的字段
	 * 
	 * @param ids 主键Ids
	 * @return
	 */

	public List<Map<String, Object>> selectCompanyIndexInfoByIds(@Param("ids") List<String> ids);


	





	



}
