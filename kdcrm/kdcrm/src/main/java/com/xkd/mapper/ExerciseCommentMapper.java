package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ExerciseCommentMapper {

	
	int saveComment(Map<String, Object> obj);
	
	List<Map<String, Object>> getCommentListByEid(String eid);

	void deleteCommentByEid(String id);

	Map<String, Object> getUserComment(@Param("eid") String eid, @Param("uid") String uid);
	
	Map<String, Object> getPaixutiComment(@Param("userExamId") String userExamId, @Param("exerciseId") String exerciseId);

	Map<String, Object> getCommentByGrade(@Param("grade")String grade,@Param("exerciseId") String exerciseId);

	int getPaixutiGrade(@Param("userExamId")String examId,@Param("exerciseId") String exerciseId);

	Map<String,String> getCommentGrade(@Param("exerciseId")String exerciseId, @Param("openId")String openId);

}
