package com.xkd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.WjUserExamMapper;

@Service
public class WjUserExamService {

	@Autowired
	private WjUserExamMapper wjUserExamMapper;
	
	public Integer deleteWjUserExam(String sql){
		
		Integer num = wjUserExamMapper.deleteWjUserExam(sql);
		
		return num;
	}
}
