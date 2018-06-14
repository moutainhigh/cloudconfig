package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.QuestionOption;
import com.xkd.model.UserAnswer;

public interface QuestionOptionMapper {

	List<QuestionOption> getQuerstionOptList(@Param("qid")String qid,@Param("cssType")String cssType);
	
	int saveQuerstionOpt(QuestionOption obj);

	List<QuestionOption> getQuerstionOptAnswerList(String string);

	List<QuestionOption> getQuerstionOptAnswerList(@Param("qid") String qid, @Param("examId") String examId, @Param("orderBy") String orderBy);

	List<QuestionOption> getQuerstionOptChart(Map<String, Object> map);//@Param("qid")String qid,@Param("cnt")String cnt

	List<QuestionOption> getQuerstionOpt();
	
	int editQuerstionOpt(QuestionOption obj);
	
	int delOptById(Object id);
	
	List<QuestionOption> getPaiXTTongJi(@Param("qid") String qid, @Param("cnt") String cnt);
	
	List<QuestionOption> getDeFTTongJi(@Param("qid") String qid, @Param("cnt") String cnt);
	
	

}
