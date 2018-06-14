package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.LoginUserSyncModel;

public interface LoginUserSyncDao {
	/**
	 * 添加登录用户
	 * @param loginUserSync
	 */
	public void addLoginUserSync(LoginUserSyncModel loginUserSync);
	/**
	 *如果新增用户名重复，则更改密码，id，备注 ,用户角色
	 */
	public void add_modifyLoginUserSync(LoginUserSyncModel loginUserSync);
	/**
	 * 如果修改用户名重复，则更改密码，id，备注 
	 */
	public void modifyLoginUser_NoRole(LoginUserSyncModel loginUserSync);
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
	  * Description:根据用户名称查询数据
	  */
	 List<LoginUserSyncModel> getCountByYh_Mc(String yh_mc);
	 
	 /**
	 * 在指派/添加用户角色时，在用户表中更改是否是管理员
	 * @param supperRole  1,0
	 */
	public void modifyLoginUser_Role(String yh_mc,String supperRole);
}
