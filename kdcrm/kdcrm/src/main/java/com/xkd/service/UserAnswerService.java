package com.xkd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.UserAnswerMapper;
import com.xkd.model.UserAnswer;

@Service
public class UserAnswerService {

	@Autowired
	UserAnswerMapper mapper;
	
	
	public List<UserAnswer> getUserAnswerListByObj(UserAnswer obj){
		
		return mapper.getUserAnswerListByObj(obj);
	}
	
	public int saveUserAnswer(UserAnswer obj){
		
		return mapper.saveUserAnswer(obj);
	}
	
	
	public List<UserAnswer> getDuoXuanAnser(String qid, String uid) {
		// TODO Auto-generated method stub
		return mapper.getDuoXuanAnser(qid,uid);
	}
}
