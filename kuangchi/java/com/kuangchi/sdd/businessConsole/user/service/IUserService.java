package com.kuangchi.sdd.businessConsole.user.service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.station.model.Station;
import com.kuangchi.sdd.businessConsole.user.model.User;

public interface IUserService {

	User getLoginUser(User user);

	Grid<User> getUsers(User userPage);

	/**
	 * 新增用户
	 * @param user
	 * @param jsDm 新增时默认角色
	 */
	void addNewUser(User user,String jsDm);
	
	/**
	 * 将员工设为管理员
	 * @param user
	 * @param jsDm 新增时默认角色
	 */
	void setUser(User user,String jsDm);
	

	/**
	 * 获取用户额外菜单
	 * @param userId 
	 * @return
	 */
	String[] getUserAdditionMenu(String userId);

	/**
	 * 重新设置用户额外菜单
	 * @param userId
	 * @param cdDm
	 * @param start 有效时间
	 * @param end
	 */
	void changeUserAdditionMenu(String userId, String[] cdDm,String lrryDm,Timestamp start,Timestamp end);

	/**
	 * 用户代码查询用户
	 * @param yhDm
	 * @return
	 */
	User getUserByYhDm(String yhDm);

	/**
	 * 更新用户
	 * @param user
	 */
	void updateUser(User user);

	/**
	 * 密码验证
	 * @author yuman.gao
	 */
	Integer validPwd(String userName, String password);
	
	/**
	 * 修改用户密码
	 * @param yhDm
	 * @param pwd
	 */
	void modifyUserPwd(String yhDm, String pwd);

	/**
	 * 删除用户
	 * @param yhDms
	 */
	void deleteUser(String yhDms);

	/**
	 * 变更用户部门
	 * @param yhDm 用户代码
	 * @param bmDmS 部门代码
	 * @param lryhDm 录入人员代码
	 */
	void changeUserDepartment(String yhDm, String[] bmDmS, String lryhDm);

	/**
	 * 验证用户
	 * @param yhDm 用户代码
	 * @param yhMc 用户名称
	 * @return
	 */
	JsonResult validUser(String yhDm, String yhMc);

    /**
     * 用户所在部门所有岗位
     * @param yhDm
     * @return
     */
    Map<String,List<Station>> getUserDepartStations(String yhDm);

    /**
     * 用户所有岗位
     * @param yhDm
     * @return
     */
    String getUserGws(String yhDm);

    /**
     * 更新人员岗位
     * @param yhDm
     * @param gwds
     * @param lrryDm
     */
    void updateUserStations(String yhDm, String[] gwds, String lrryDm);
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
	 * 判断是否像CS一样登陆
	 * by gengji.yang
	 */
	public String isLikeCSLogin();
	
	/**
     * 查询录入人（用于分层显示）
     * @author yuman.gao
     */
	public String getLrryDm(String yhDm);
	
	/**
	 * 获取角色的系统权限
	 * by gengji.yang
	 */
	public List<String> getRoleXtAuths(String roleDm);
    	
	/**
	 * 授权码日志
	 * @author minting.he
	 * @param trigger 0 激活 1 修改
	 * @param flag 0 成功 1 失败
	 */
	public void licenseLog(String trigger, String flag, String login_user);

	void addLogger(String userMc,String flag);
	

}
