package com.xkd.service;

import com.xkd.mapper.DC_UserMapper;
import com.xkd.model.DC_User;
import com.xkd.model.UserAction;
import com.xkd.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DC_UserService {

	@Autowired
	DC_UserMapper mapper;



	@Autowired
	CompanyService companyService;


	

	
	public int saveUser(DC_User obj){
		if(StringUtils.isNotBlank(obj.getId())){
			
			return mapper.editUser(obj);
		}else{
			String userId = UUID.randomUUID().toString();
			obj.setId(userId);
			mapper.saveUserDetail(obj);
			return mapper.saveUser(obj);
		}
	}


	public DC_User getUserById(Object userid) {
		return mapper.getUserById(userid);
	}

	public HashMap<String, Object> getSignInfo(String meetingId,String userId) {
		return mapper.getSignInfo(userId,meetingId);
	}

	public DC_User getAnswerUser(String id, String userid) {
		
		return mapper.getAnswerUser(id,userid);
	}

	public Map<String, Object> selectUserAttendMeetingBaseInfos(String meetingId, String mobile, String uid) {
		
		Map<String, Object> map = mapper.selectUserAttendMeetingBaseInfos(meetingId,mobile,uid);
		
		return map;
	}

	public List<Map<String, Object>> selectUserCompanys(String userId) {

		return mapper.selectUserCompanys(userId);

	}

	public Map<String,Object> selectUserAndHotelSetingByMobile(String meetingId, String mobile) {

		return mapper.selectUserAndHotelSetingByMobile(meetingId,mobile);

	}

	public void editUserDetail(DC_User user) {
		mapper.editUserDetail(user);
	}
}
