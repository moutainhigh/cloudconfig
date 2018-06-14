package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.UserExam;

public interface UserExamMapper {

	
	int saveUserExam(UserExam obj);
	
	UserExam getNewsExer(@Param("eid") String eid, @Param("uid") String uid, @Param("meetingId") String meetingId);

	void deleteExamAnswer(@Param("id") String id, @Param("uid") String uid);

	void deleteByMeetingId(@Param("meetingId")String meetingId);


}
