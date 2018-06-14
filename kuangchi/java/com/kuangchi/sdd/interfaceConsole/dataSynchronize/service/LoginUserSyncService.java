package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.LoginUserSyncModel;


public interface LoginUserSyncService {

	/**
	 * 添加登录用户
	 * @param loginUserSync
	 */
	public void addLoginUserSync(LoginUserSyncModel loginUserSync);
	
	/**
	 * 修改登录用户
	 * @param loginUserSync
	 */
	public void modifyLoginUserSync(LoginUserSyncModel loginUserSync);
	/**
	 * 删除登录用户
	 * @param userName
	 */
	public void delLoginUserSync(String userName);
	 /**
	 * 在指派/添加用户角色时，在用户表中更改是否是管理员
	 * @param supperRole  1,0
	 */
	public void modifyLoginUser_Role(String yh_mc,String supperRole);
	
}
