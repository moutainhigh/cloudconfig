package com.xkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.HotelMapper;
import com.xkd.model.Hotel;

import java.util.*;

@Service
public class HotelService {

	@Autowired
	private HotelMapper hotelMapper;
	@Autowired
	private  UserService userService;
	
	public Integer updateHotelById(Hotel hotel){
		
		Integer num = hotelMapper.updateHotelById(hotel);
		
		return num;
	}

	public Integer deleteUserHotelByMeetingUserId(String meetingUserId) {
		
		Integer num = hotelMapper.deleteUserHotelByMeetingUserId(meetingUserId);
		
		return num;
	}


    public Integer saveUserHotel(Map<String, Object> userHotelMap) {
		return hotelMapper.saveUserHotel(userHotelMap);
    }

	/**
	 *
	 * @param loginUserId
	 * @param uname
	 * @param mobile
	 * @param hotelName
	 * @param startTime
	 * @param endTime
	 * @param hotelNumber（房间类型和房间数量）
	 * @param userId  用户ID(新增是为空)
	 * @param userHotelId 用户订房ID（新增时为空）
	 * @return
	 * @throws Exception
	 */
	public boolean changeUserHotelMessage(String loginUserId, String uname, String mobile, String hotelName,
										String startTime, String endTime, String hotelNumber,String userId,String userHotelId,String meetingId) throws  Exception{


		Map<String, Object> userMap = new HashMap<>();
		Map<String, Object> userHotelMap = new HashMap<>();

		if(userId == null && userHotelId == null){
			Map<String, Object>  map =  userService.selectUserByMobile(mobile,null);

			String existsUserId = null;
			String insertUserId = null;
			//如果这个手机号已经存在，就将这个用户绑定到用户企业关联表中
			if(map != null && map.size() > 0){
				existsUserId = (String)map.get("id");
				userMap.put("id",existsUserId);
				userMap.put("uname", uname);
				userMap.put("mobile", mobile);
				userMap.put("platform", "1");
				userMap.put("status", "0");
				userMap.put("createdBy",loginUserId);
				userMap.put("createDate", new Date());

				userService.updateDcUser(userMap);
				userService.updateDcUserDetail(userMap);
			}else {
				insertUserId = UUID.randomUUID().toString();
				userMap.put("id",insertUserId);
				userMap.put("uname", uname);
				userMap.put("mobile", mobile);
				userMap.put("platform", "1");
				userMap.put("status", "0");
				userMap.put("createdBy",loginUserId);
				userMap.put("createDate", new Date());

				userService.insertDcUser(userMap);
				userService.insertDcUserDetail(userMap);
			}

			List<Map<String, Object>> maps  = JSON.parseObject(hotelNumber, new TypeReference<List<Map<String, Object>>>() {});

			if(maps != null && maps.size() > 0){
				for(Map<String, Object> mapp : maps){
					String roomType = mapp.get("roomType") == null?"":(String)mapp.get("roomType");
					String roomNumber = mapp.get("roomNumber") == null?"":(String)mapp.get("roomNumber");
					if("普通房".equals(roomType)){
						userHotelMap.put("roomNumber",roomNumber);
					}else if("大床房".equals(roomType)){
						userHotelMap.put("bigRoomNumber",roomNumber);
					}
				}
			}

			String insertUserHotelId = UUID.randomUUID().toString();
			userHotelMap.put("id",insertUserHotelId);
			userHotelMap.put("startTime",startTime);
			userHotelMap.put("meetingUserId",UUID.randomUUID().toString());
			userHotelMap.put("endTime",endTime);
			userHotelMap.put("createdBy",loginUserId);
			userHotelMap.put("updatedBy",loginUserId);
			userHotelMap.put("createDate",new Date());
			userHotelMap.put("updateDate",new Date());
			userHotelMap.put("hotelName",hotelName);
			userHotelMap.put("userId",existsUserId == null?insertUserId:existsUserId);
			userHotelMap.put("status",0);
			userHotelMap.put("meetingId",meetingId);

			saveUserHotel(userHotelMap);

		}else{

			userMap.put("id",userId);
			userMap.put("uname", uname);
			userMap.put("mobile", mobile);
			userMap.put("platform", "1");
			userMap.put("status", "0");
			userMap.put("createdBy",loginUserId);
			userMap.put("createDate", new Date());

			userService.updateDcUser(userMap);
			userService.updateDcUserDetail(userMap);

			List<Map<String, Object>> maps  = JSON.parseObject(hotelNumber, new TypeReference<List<Map<String, Object>>>() {});

			if(maps != null && maps.size() > 0){
				for(Map<String, Object> mapp : maps){
					String roomType = mapp.get("roomType") == null?"":(String)mapp.get("roomType");
					String roomNumber = mapp.get("roomNumber") == null?"":(String)mapp.get("roomNumber");
					if("普通房".equals(roomType)){
						userHotelMap.put("roomNumber",roomNumber);
					}else if("大床房".equals(roomType)){
						userHotelMap.put("bigRoomNumber",roomNumber);
					}
				}
			}

			userHotelMap.put("id",userHotelId);
			userHotelMap.put("startTime",startTime);
			userHotelMap.put("endTime",endTime);
			userHotelMap.put("createdBy",loginUserId);
			userHotelMap.put("updatedBy",loginUserId);
			userHotelMap.put("updateDate",new Date());
			userHotelMap.put("hotelName",hotelName);
			userHotelMap.put("userId",userId);
			userHotelMap.put("status",0);
			userHotelMap.put("meetingId",meetingId);
			updateUserHotel(userHotelMap);
		}

		return true;
	}

	private Integer updateUserHotel(Map<String, Object> userHotelMap) {
		return hotelMapper.updateUserHotel(userHotelMap);
	}

	public Integer deleteUserHotelByIds(List<String> idList) {
		return hotelMapper.deleteUserHotelByIds(idList);
	}
}
