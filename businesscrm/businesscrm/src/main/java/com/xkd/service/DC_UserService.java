package com.xkd.service;

import com.xkd.mapper.DC_UserMapper;
import com.xkd.model.DC_User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DC_UserService {

	@Autowired
	DC_UserMapper mapper;
	@Autowired
	SolrService solrService;
	@Autowired
	UserDynamicService userDynamicService;

	@Autowired
	CompanyService companyService;



	
	//1mobile,2weixin,3unionid
	public DC_User getUserByObj(String id, String ttype){
		System.out.println("DC_UserService.getUserByObj()"+id+"  "+ttype);
		return mapper.getUserByObj(id,ttype);
	}
	
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






}
