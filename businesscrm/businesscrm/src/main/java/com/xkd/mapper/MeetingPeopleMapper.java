package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface MeetingPeopleMapper {

    Integer updatePeople(Map<String, Object> paramMap);

    Integer savePeople(Map<String, Object> paramMap);

    Map<String,Object> selectUserByMobile(@Param("mobile") String mobile);

    Map<String,Object> selectUserByMeetingMobile(@Param("mobile")String mobile,@Param("meetingId") String meetingId);
}
