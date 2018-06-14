package com.xkd.service;

import com.xkd.mapper.UserDynamicMapper;
import com.xkd.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDynamicService {

	
	@Autowired
	private UserDynamicMapper userDynamicMapper;
	
	@Autowired
	private UserService userService;
	
	public List<Map<String,Object>> selectUserDynamicByGroupId(String groupId,Integer currentPage,Integer pageSize,String orderFlag){
		
		if (currentPage<1) {
			currentPage=1;
		}
		List<Map<String,Object>> maps = userDynamicMapper.selectUserDynamicByGroupId(groupId,(currentPage-1)*pageSize,pageSize,orderFlag);
		
		return maps;
		
	}
	public Integer selectUserDynamicCountByCompanyId(String groupId){
		return userDynamicMapper.selectUserDynamicCountByGroupId(groupId);
	}
	
	

	public Integer saveUserDynamic(Map<String, Object> paramMap) {
		
		Integer num = userDynamicMapper.saveUserDynamic(paramMap);
		
		return num;
	}

	public Integer deleteUserDynamicByIds(List<String> ids) {
		
		Integer num = userDynamicMapper.deleteUserDynamicByIds(ids);
		
		return num;
	}
	
	
	/**
	 * 添加动态
	 * @param loginUserId 登录用户ID
	 * @param groupId 修改对象记录ID   
	 * @param groupName 修改对象记录名称
	 * @param content 内容 
	 * @param ttype 0 系统产生  1 用户添加
	 * @param followingType 跟进类型：电话跟进， 邮件跟进， 信息跟进，  客户面谈，  微信跟进

	 *
	 */
	public String addUserDynamic(String loginUserId,String groupId,String groupName,String operateType,String content,Integer ttype,String followingType,String contactee,String imageUrl){
		if (!StringUtils.isBlank(loginUserId)) {
			Map<String, Object> loginUser = userService.selectUserById(loginUserId);
            if (null!=loginUser) {
				Map<String, Object> paramMap = new HashMap<>();
				String id = UUID.randomUUID().toString();
				paramMap.put("id", id);
				paramMap.put("groupId", groupId);
				paramMap.put("groupName", groupName);
				paramMap.put("operateType", operateType);
				paramMap.put("createdBy", loginUserId);
				paramMap.put("updatedBy", loginUserId);
				paramMap.put("contentValue", loginUser.get("uname") + content);
				paramMap.put("ttype", ttype);
				String date = DateUtils.dateToString(new Date(), "yyyy-MM-dd : HH:mm:ss");
				paramMap.put("updateDate", date);
				paramMap.put("createDate", date);
				paramMap.put("followingType",followingType);
				paramMap.put("contactee",contactee);
				paramMap.put("imageUrl",imageUrl);
				saveUserDynamic(paramMap);
				return id;
			}
			return null;
		}else{
			return null;
		}
	}

    public void changeUserDynamic(Map<String, String> userDynamic) {
		userDynamicMapper.changeUserDynamic(userDynamic);
    }
}
