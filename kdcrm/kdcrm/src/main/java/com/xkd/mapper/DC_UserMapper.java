package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.DC_User;

public interface DC_UserMapper {

	DC_User getUserByObj(@Param("id")String id, @Param("ttype")String ttype);
	
	int saveUser(DC_User obj);
	
	int saveUserDetail(DC_User obj);

	int editUser(DC_User obj);

	DC_User getUserById(Object userid);

	//------------------会务相关-------------------begin
	void saveMeetingUser(HashMap<String, Object> obj);
	
	void editMeetingUser(@Param("id")Object id,@Param("status")String status);

	HashMap<String, Object> checkUserToMeeting(@Param("uid")String uid, @Param("meetingId")String meetingId);

	void saveCompany(HashMap<String, String> company);
	
	void editCompany(HashMap<String, Object> company);
	
	HashMap<String, Object> getCompanyById(@Param("id")String id,@Param("companyName")String companyName);
	
	void saveUserCompany(HashMap<String, Object> obj);
	
	void saveCompanyDetail(HashMap<String, String> company);
	//------------------会务相关-------------------end
	

	DC_User getAnswerUser(@Param("id")String id, @Param("userId")String userid);

	Map<String, Object> selectUserAttendMeetingBaseInfos(@Param("meetingId")String meetingId, @Param("mobile")String mobile, @Param("uid")String uid);

	HashMap<String, Object> getSignInfo(@Param("userId")String userId, @Param("meetingId")String meetingId);

	List<HashMap<String, Object>> userCompanyList(String id);

	List<Map<String, Object>> selectUserCompanys(@Param("userId")String userId);

	Map<String,Object> selectUserAndHotelSetingByMobile(@Param("meetingId")String meetingId, @Param("mobile")String mobile);

    void editUserDetail(DC_User user);
}
