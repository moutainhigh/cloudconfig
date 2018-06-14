package com.xkd.service;

import com.xkd.mapper.MeetingPeopleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MeetingPeopleService {


    @Autowired
    private MeetingPeopleMapper meetingPeopleMapper;

    public Integer updatePeople(Map<String, Object> paramMap) {
        return meetingPeopleMapper.updatePeople(paramMap);
    }

    public Integer savePeople(Map<String, Object> paramMap) {
        return meetingPeopleMapper.savePeople(paramMap);
    }

    public Map<String,Object> selectUserByMobile(String mobile) {
        return meetingPeopleMapper.selectUserByMobile(mobile);
    }

    public Map<String,Object> selectUserByMeetingMobile(String mobile, String meetingId) {
        return meetingPeopleMapper.selectUserByMeetingMobile(mobile,meetingId);
    }
}
