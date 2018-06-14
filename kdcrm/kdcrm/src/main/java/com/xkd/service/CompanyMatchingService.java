package com.xkd.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.CompanyMatchingMapper;
import com.xkd.model.CompanyMatching;

@Service
public class CompanyMatchingService {

	@Autowired
	private CompanyMatchingMapper companyMatchingMapper;
	
	public List<Map<String,Object>> selectMatchingsByCompanyId(String companyId, int pageSizeInt, int currentPageInt){
	
		List<Map<String,Object>> maps = companyMatchingMapper.selectMatchingsByCompanyId(companyId,pageSizeInt,currentPageInt);
		
		return maps;
		
	}

	public List<Map<String,Object>> selectNotMatchingsByCompanyId(Map<String,Object> map){
	
		List<Map<String,Object>> maps = companyMatchingMapper.selectNotMatchingsByCompanyId(map);
		
		return maps;
		
	}

	public Integer saveMatchingCompanys(CompanyMatching companyMatch) {
		
		Integer num = companyMatchingMapper.saveMatchingCompanys(companyMatch);
		
		return num;
	}

	public Integer deleteMatchingCompanys(String idString) {
		

		
		Integer num = companyMatchingMapper.deleteMatchingCompanys(idString);
		
		return num;
		
	}

	public Integer selectTotalMatchingsByCompanyId(String companyId) {
		
		Integer num = companyMatchingMapper.selectTotalMatchingsByCompanyId(companyId);
		
		return num;
		
	}

	public Integer selectTotalNotMatchingsByCompanyId(Map<String,Object> map) {
		
		Integer num = companyMatchingMapper.selectTotalNotMatchingsByCompanyId( map);
		
		return num;
		
	}
	
}
