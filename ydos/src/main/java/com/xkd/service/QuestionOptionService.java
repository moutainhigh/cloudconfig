package com.xkd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.QuestionOptionMapper;
import com.xkd.model.QuestionOption;

@Service
public class QuestionOptionService {

	@Autowired
	QuestionOptionMapper mapper;
	
	public List<QuestionOption> getQuerstionOptList(String i){
		return mapper.getQuerstionOptList(i,null);
	}
	
	public int saveQuerstionOpt(QuestionOption obj){
		return mapper.saveQuerstionOpt(obj);
	}

	
	
	
	
	
}
