package com.xkd.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.DC_PC_UserMapper;
import com.xkd.model.DC_PC_User;

@Service
public class DC_PC_UserService {

	@Autowired
	DC_PC_UserMapper mapper;



	public DC_PC_User getUserById(String id) {

		return mapper.getUserById(id);
	}

	public List<HashMap<String, Object>> selectUserInfo(String uname) {
		if (StringUtils.isNotBlank(uname)) {
			uname = "%" + uname + "%";
		}
		List<HashMap<String, Object>> maps = mapper.selectUserInfo(uname);

		return maps;
	}

	public Integer savePcUserMap(Map<String, Object> map) {
		
		Integer num = mapper.savePcUserMap(map);

		return num;
	}

	public Integer updatePcUserMap(Map<String, Object> map) {
		
		Integer num = mapper.updatePcUserMap(map);

		return num;
	}

	public Integer deletePcUserByIds(String ids) {
		
		Integer num = mapper.deletePcUserByIds(ids);

		return num;
	}

	public List<Map<String, Object>> selectPcUsersByContent(String content, int currentPage, int pageSize) {
		
		List<Map<String, Object>> maps = mapper.selectPcUsersByContent(content,currentPage,pageSize);
		
		return maps;
	}

	public Map<String, Object> selectPcUserById(String id) {
		
		Map<String, Object> map = mapper.selectPcUserById(id);
		
		return map;
	}

	public Integer repeatPcUserPasswordsByIds(String ids, String encodeRepeatPassWord, String updateBy) {
		
		Integer num = mapper.repeatPcUserPasswordsByIds(ids,encodeRepeatPassWord,updateBy);

		return num;
	}

	public Integer updateUserPasswordById(String id, String password) {
		
		Integer num = mapper.updateUserPasswordById(id,password);

		return num;
	}

	public Map<String, Object> selectUserByEmail(String email) {
		
		Map<String, Object> map = mapper.selectUserByEmail(email);
		
		return map;
	}

	public DC_PC_User getUserByTel(String tel) {
		return mapper.getUserByTel(tel);
	}

	public Integer selectPcUsersByContentCount(String contentStr) {
		
		Integer num = mapper.selectPcUsersByContentCount(contentStr);

		return num;
	}

	public Integer deletePcUserRolesByRoles(String roleIds) {
		
		Integer num = mapper.deletePcUserRolesByRoles(roleIds);
		
		return num;
		
	}

	public Integer updatePcUserInfoById(Map<String, Object> map) {
		
		Integer num = mapper.updatePcUserInfoById(map);
		
		return num;
		
	}


}

