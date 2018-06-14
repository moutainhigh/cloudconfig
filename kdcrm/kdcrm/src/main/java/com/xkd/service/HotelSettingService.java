package com.xkd.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.HotelSettingMapper;
import com.xkd.model.Hotel;

@Service
public class HotelSettingService {

	
	@Autowired
	HotelSettingMapper hotelSettingMapper;
	
	public Integer saveHotelSetting(Map<String,Object> map){
		
		if(map.get("id") == null){
			map.put("id", UUID.randomUUID().toString());
			return hotelSettingMapper.saveHotelSetting(map);
		}else{
			return hotelSettingMapper.updateHotelSetting(map);
		}
		
	}

	public List<Map<String, Object>> selectHotelSettingsByMeetingId(String meetingId) {
		
		List<Map<String, Object>> maps = hotelSettingMapper.selectHotelSettingsByMeetingId(meetingId);
		
		return maps;
	}

	public Hotel selectUserInfosByMobileByInvitation(String meetingId,String userId) {
		
		
		return hotelSettingMapper.selectUserInfosByMobileByInvitation(meetingId,userId);
	}

	public Map<String, Object> selectHotelSettingsById(String id) {
		
		Map<String, Object> map = hotelSettingMapper.selectHotelSettingsById(id);
		
		return map;
	}

}
