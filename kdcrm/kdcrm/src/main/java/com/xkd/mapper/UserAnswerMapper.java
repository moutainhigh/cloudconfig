package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.UserAnswer;

public interface UserAnswerMapper {

	List<UserAnswer> getUserAnswerListByObj(UserAnswer obj);
	
	int saveUserAnswer(UserAnswer obj);
	
	List<UserAnswer> getDuoXuanAnser(@Param("qid") String qid, @Param("examId") String examId);
	
	List<UserAnswer> getUserAnswerByQid(String qid);

	List<HashMap<String, Object>> getUserListAnswer(String id);
	
	List<HashMap<String, Object>> getUserLists(Object id);

	List<HashMap<String, Object>> getExerciseZhengQueLv(String id);

	

	

}
