package com.kuangchi.sdd.businessConsole.user.dao;

import java.sql.Timestamp;
import java.util.List;

import com.kuangchi.sdd.businessConsole.user.model.User;

public interface IUserDao {

	User getLoginUser(User user);

	List<User> getUsers(User userPage);

	int countUsers(User userPage);

	void addNewUser(User pageUser);

	String[] getUserAdditionMenu(String userId);

	/**
	 * 删除用户额外菜单
	 * @param userId
	 */
	void deleteUserAdditionMenu(String userId);

	/**
	 * 新增用户额外菜单
	 * @param userId
	 * @param cdDm
	 * @param lrryDm
	 */
	void addUserAdditionMenu(String userId, String[] cdDm,String lrryDm,Timestamp start,Timestamp end);

	User getUserByYhDm(String yhDm);

	void updateUser(User user);

	void modifyUserPwd(String yhDm, String pwd);

	/**
	 * 删除用户
	 * @param yhDms
	 */
	void deleteUsers(String yhDms);

	int isContainYhDM(String yhDm);

	int isContainYhMc(String yhMc);
	/**
	 * 通过用户名称拿用户角色
	 * 				By gengji.yang
	 * @param yhMc
	 * @return
	 */
	public User getLoginUserByYhMc(String yhMc);
	
	/**
	 * 查询用户代码是否存在
	 * @param yhDm
	 * @return
	 * @author minting.he
	 */
	public Integer selectYhDm(String yhDm);
	/**
	 * 查询系统配置参数---CS登陆之后是否显示退出按钮
	 * by gengji.yang
	 * @return
	 */
	public String isSeeLoginOutBtn();
	
	/**
	 * by gengji.yang 
	 * @return
	 */
	public String getCurrentRoleNameByStaffNum(String staffNum);
	
	/**
	 * 判断是否像CS一样登陆
	 * by gengji.yang
	 */
	public String isLikeCSLogin();
	
	/**
	 * 密码验证
	 * @author yuman.gao
	 */
	Integer validPwd(String userName, String password);
	
	/**
	 * 根据录入人员查询用户
	 */
	public List<User> getUsersByLrryDm(String lrryDm);
	
	/**
	 * 获取角色的系统权限
	 * by gengji.yang
	 */
	public List<String> getRoleXtAuths(String roleDm);
	
}
