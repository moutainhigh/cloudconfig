package com.xkd.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.MeetingMapper;
import com.xkd.model.Meeting;

@Service
public class MeetingService {

	@Autowired
	private MeetingMapper meetingMapper;
	
	
	public List<Meeting> selectMeetingByParam(String startTime, String endTime,
                                              String meetingType, String meetingStatus, String meetingName, String address, String teacherIds, String mname,
                                              int currentPageInt, int pageSizeInt, List<String> list, String pcCompanyId){
		
		List<Meeting> meetings = meetingMapper.selectMeetingByParam(startTime,endTime,
				meetingType,meetingStatus,meetingName,address,teacherIds,mname,currentPageInt,pageSizeInt,list,pcCompanyId);
		
		return meetings;
	}


	public Integer updateMeetingById(Meeting meeting) {
		
		Integer num = meetingMapper.updateMeetingById(meeting);
		
		return num;
	}


	public Integer saveMeeting(Meeting meeting) {
		
		Integer num = meetingMapper.saveMeeting(meeting);
		
		return num;
	}


	public Meeting selectMeetingById(String meetingId) {
		
		Meeting meeting = meetingMapper.selectMeetingById(meetingId);
		
		return meeting;
	}


	public Integer getMeetingCountByParam(String meetingType, String meetingStatus, String meetingName,
										  String address, String teacherIds, String mname, String startTime,
										  String endTime, List<String> list, String pcCompanyId) {
		
		Integer num  = meetingMapper.getMeetingCountByParam(meetingType,meetingStatus,meetingName,address,teacherIds,mname,startTime,endTime,list,pcCompanyId);
		
		return num;
	}


	public Integer deleteMeetingById(String ids) {
		
		Integer num  = meetingMapper.deleteMeetingById(ids);
		
		return num;
	}


	public List<Map<String, Object>> selectCompanysLogo(String meetingId) {
		
		List<Map<String, Object>> maps = meetingMapper.selectCompanysLogo(meetingId);
		
		return maps;
	}

	
	public List<Map<String,Object>> selectMeetingByCompanyId(  String companyId){
		return meetingMapper.selectMeetingByCompanyId(companyId);
	}
	
	public List<Map<String,Object>> selectUserExcerciseByCompanyId(String companyId){
		return meetingMapper.selectUserExcerciseByCompanyId(companyId);
	}
	
	
	public void deleteMeetingUserByCompanyIds(String companyIds){
		meetingMapper.deleteMeetingUserByCompanyIds(companyIds);
	}

	public Integer saveUserSpread(Map<String, Object> map) {
		return meetingMapper.saveUserSpread(map);
	}

	public List<Map<String,Object>> selectUserSpreadsByMeetingId(String meetingId) {
		return meetingMapper.selectUserSpreadsByMeetingId(meetingId);
	}

	public Integer deleteUserSpreadsByIds(List<String> idList) {
		return meetingMapper.deleteUserSpreadsByIds(idList);
	}

	public List<Map<String,Object>> selectDetailByspreadId(String userSpreadId) {
		return meetingMapper.selectDetailByspreadId(userSpreadId);
	}

	public Integer selectCountDetailByspreadId(String userSpreadId) {
		return meetingMapper.selectCountDetailByspreadId(userSpreadId);
	}

	public Integer selectTotalUserSpreadsByMeetingId(String meetingId) {
		return meetingMapper.selectTotalUserSpreadsByMeetingId(meetingId);
	}

	public Map<String,Object> selectUserSpreadByMeetingIdUserId(String meetingId, String userId) {
		return meetingMapper.selectUserSpreadByMeetingIdUserId(meetingId,userId);
	}
}
