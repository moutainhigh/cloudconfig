package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Exercise;

public interface ExerciseMapper {

	Exercise getExercise(@Param("id") String id, @Param("uid") String uid);
	
	int saveExercise(Exercise obj);
	
	int editExercise(Exercise obj);
	
	int delExerciseById(@Param("list") List<String> id);

	List<Exercise> getUserExercise(Map<String,Object> map);

	int getUserExerciseTotal(Map<String,Object> map);

	List<HashMap<String, Object>> getExerciseUserAll(@Param("id") String id,
													 @Param("uname") String uname,
													 @Param("pageNo") int pageNo,
													 @Param("pageSize") int pageSize);
	
	int getExerciseUserAllTotal(@Param("id") String id,
								@Param("uname") String uname);

	List<Exercise> getMeetingExercise(@Param("ttype") List<String> ttypes,
									  @Param("title") String title,
									  @Param("meetingId") String meetingId,
									  @Param("pageNo") int pageNo,
									  @Param("pageSize") int pageSize);

	int getMeetingExerciseTotal(@Param("ttype") List<String> ttypes,
								@Param("title") String title,
								@Param("meetingId") String meetingId,
								@Param("pageNo") int pageNo,
								@Param("pageSize") int pageSize);

	

}
