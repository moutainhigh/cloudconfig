package com.xkd.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.SuggestionMapper;
import com.xkd.model.Suggestion;

@Service
public class SuggestionService {

	@Autowired
	private SuggestionMapper suggestionMapper;
	
	public List<HashMap<String,Object>> selectSuggestionsByMeetingId(String meetingId){
		
		
		List<HashMap<String,Object>> suggestions = suggestionMapper.selectSuggestionsByMeetingId(meetingId);
		
		return suggestions;
		
	}

	public List<HashMap<String, Object>> selectSuggestionsNeedAnswer(String meetingId) {
		
		
		List<HashMap<String,Object>> suggestions = suggestionMapper.selectSuggestionsNeedAnswer(meetingId);
		
		return suggestions;
		
	}

	public Integer deleteSuggestionById(String answerId) {
		
		Integer num = suggestionMapper.deleteSuggestionById(answerId);
		
		return num;
		
	}

	public Integer answerSuggestion(Suggestion suggestion) {
		System.out.println("SuggestionService.answerSuggestion()----------------------------");
		Integer num = suggestionMapper.answerSuggestion(suggestion);
		
		return num;
		
	}
}
