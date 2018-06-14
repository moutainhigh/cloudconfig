package com.kuangchi.sdd.commConsole.userGroup.service;

import java.util.List;



public interface IUserGroupService {
	
	public String setUserGroup(String mac,String deviceType,List<String> userGroup);//设置用户时段组
	
	public String getUserGroup(String mac,String deviceType, String block);//获取用户时段组
	
}
