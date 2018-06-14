package com.xkd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.MeetingExerciseMapper;
import com.xkd.model.Exercise;

@Service
public class MeetingExerciseService {

	@Autowired
	private MeetingExerciseMapper meetingExerciseMapper;
	

	public List<Exercise> getExerciseByMid(String meetingId) {
		return meetingExerciseMapper.getExerciseByMid(meetingId);
	}

	public List<Map<String, Object>> getExerciseByCompanyid(String companyId) {
		return meetingExerciseMapper.getExerciseByCompanyid(companyId);
	}

	public void editMeetingExerciseList(Object eid,Object ttype, String meetingId, String userid) {
		meetingExerciseMapper.editExerciseMeetingIsNoull(meetingId);
		meetingExerciseMapper.editMeetingExerciseList(eid,ttype,meetingId,userid);
		
	}
	
	public void editExerciseMeetingIsNoull(String meetingId) {
		
		meetingExerciseMapper.editExerciseMeetingIsNoull(meetingId);
		
	}

	public List<HashMap<String, Object>> getCompanyUserExercise(Object meetingId,String companyId, Object id) {
		
		return meetingExerciseMapper.getCompanyUserExercise(meetingId,companyId,id);
	}
}
