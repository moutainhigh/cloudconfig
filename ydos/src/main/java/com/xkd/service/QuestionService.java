package com.xkd.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.QuestionMapper;
import com.xkd.model.Question;

@Service
public class QuestionService {

	
	
	@Autowired
	QuestionMapper mapper;
	
	public List<Question> getQuerstionList(String eid){
		return mapper.getQuerstionList(eid);
	}
	
	public List<Map<String,Object>> getQuerstionListByTranData(String eid){
		return mapper.getQuerstionListByTranData(eid);
	}
	
	public int saveQuerstion(Question obj){
		return mapper.saveQuerstion(obj);
		
	}
	
	public int delQuerstionByEid(String eid){
		return mapper.delQuerstionByEid(eid);
	}

}
