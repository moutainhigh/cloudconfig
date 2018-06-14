package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Hotel;

public interface HotelSettingMapper {

	Integer saveHotelSetting(Map<String, Object> map);

	Integer updateHotelSetting(Map<String, Object> map);

	List<Map<String, Object>> selectHotelSettingsByMeetingId(@Param("meetingId") String meetingId);

	Hotel selectUserInfosByMobileByInvitation(@Param("meetingId") String meetingId, @Param("userId") String userId);

	Map<String, Object> selectHotelSettingsById(@Param("id") String id);

	
}
