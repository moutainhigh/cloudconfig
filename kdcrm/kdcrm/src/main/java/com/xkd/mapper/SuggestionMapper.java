package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Suggestion;

public interface SuggestionMapper {

	List<HashMap<String,Object>> selectSuggestionsByMeetingId(@Param("meetingId") String meetingId);

	List<HashMap<String, Object>> selectSuggestionsNeedAnswer(@Param("meetingId") String meetingId);

	Integer deleteSuggestionById(@Param("id") String id);

	Integer answerSuggestion(Suggestion suggestion);
	

}
