package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Question;

public interface QuestionMapper {

	List<Question> getQuerstionList(String eid);
	
	List<Map<String ,Object>> getQuerstionListByTranData(String eid);
	
	int saveQuerstion(Question obj);
	
	int editQuerstion(Question obj);
	
	int delQuerstionByEid(String eid);
	
	int delQuerstionOptByQid(String qid);

	List<Question> getQuerstionAnswerList(@Param("eid") String eid, @Param("examId") String examId);

	List<HashMap<String, Object>> getUserAnswerZhiZhuTu(String examId);

    List<Question> getShowQuerstionList(String id);
}
