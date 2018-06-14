package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Exercise;

public interface MeetingExerciseMapper {



	List<Exercise> getExerciseByMid(@Param("meetingId") String meetingId);

	List<Map<String, Object>> getExerciseByCompanyid(@Param("companyId") String companyId);

	void editMeetingExerciseList(@Param("eid") Object eid, @Param("ttype") Object ttype, @Param("meetingId") String meetingId, @Param("userid") String userid);

	List<HashMap<String, Object>> getCompanyUserExercise(@Param("meetingId") Object meetingId, @Param("companyId") String companyId, @Param("eid") Object eid);

	void editExerciseMeetingIsNoull(@Param("meetingId") Object meetingId);
	
}
