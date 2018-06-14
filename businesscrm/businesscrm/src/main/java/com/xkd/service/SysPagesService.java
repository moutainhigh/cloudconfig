package com.xkd.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.SysPagesMapper;

@Service
public class SysPagesService {

	@Autowired
	private SysPagesMapper mapper;
	
	public List<Map<String, Object>> selectColumnsByPageID(String pageId) {
		
		List<Map<String, Object>> list = mapper.selectColumnsByPageID(pageId);
		
		return list;
	}
	
}
