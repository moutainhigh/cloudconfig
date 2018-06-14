package com.xkd.mapper;

import com.xkd.model.Hotel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HotelMapper {

	Integer updateHotelById(Hotel hotel);

	Integer deleteUserHotelByMeetingUserId(@Param("meetingUserId") String meetingUserId);


    Integer saveUserHotel(Map<String, Object> userHotelMap);

	Integer updateUserHotel(Map<String, Object> userHotelMap);

    Integer deleteUserHotelByIds(@Param("idList") List<String> idList);
}
