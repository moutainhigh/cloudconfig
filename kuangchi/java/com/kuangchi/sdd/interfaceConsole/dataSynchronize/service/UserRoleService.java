package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.UserRole;

public interface UserRoleService {

	/**
	 * 添加用户角色
	 */
	public void addUserRole(UserRole userRole);
	/**
	 * 删除用户角色
	 * @param userName
	 */
	public void delUserRole(String userName);
	
}
