package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.CompanyMatching;

public interface CompanyMatchingMapper {

	List<Map<String,Object>> selectMatchingsByCompanyId(@Param("companyId") String companyId, @Param("pageSizeInt") int pageSizeInt, @Param("currentPageInt") int currentPageInt);

	
	
	
	List<Map<String,Object>> selectNotMatchingsByCompanyId(Map<String, Object> map);

	Integer saveMatchingCompanys(CompanyMatching companyMatch);

	Integer deleteMatchingCompanys(@Param("idString") String idString);

	Integer selectTotalMatchingsByCompanyId(@Param("companyId") String companyId);

	Integer selectTotalNotMatchingsByCompanyId(Map<String, Object> map);
	
	
}
