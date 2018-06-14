package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.UserRole;

public interface UserRoleDao {

	/**
	 * 添加用户角色
	 * @param userRole
	 */
	public void addUserRole(UserRole userRole);
	/**
	 * 删除用户角色
	 * @param userName
	 */
	public void delUserRole(String userName);
	
}
