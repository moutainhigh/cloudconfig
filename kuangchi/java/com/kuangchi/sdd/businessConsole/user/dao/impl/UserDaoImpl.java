package com.kuangchi.sdd.businessConsole.user.dao.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.user.dao.IUserDao;
import com.kuangchi.sdd.businessConsole.user.model.User;


@Repository("userDaoImpl")
public class UserDaoImpl extends BaseDaoImpl<Object> implements IUserDao{

	public User getLoginUser(User user) {
		 return ((User) getSqlMapClientTemplate().queryForObject("getLoginUser", user));
	}

	@Override
	public String getNameSpace() {	
		return "common.User";
	}

	@Override
	public String getTableName() {
		return null;
	}

	public List<User> getUsers(User userPage) {
		int skip = (userPage.getPage() - 1)* userPage.getRows();	
		return this.getSqlMapClientTemplate().queryForList("getUsers", userPage, skip, userPage.getRows());
	}

	public int countUsers(User userPage) {
		return this.queryCount("countUsers", userPage);
	}

	public void addNewUser(User pageUser) {
		this.getSqlMapClientTemplate().insert("addNewUser", pageUser);
		
	}

	@Override
	public String[] getUserAdditionMenu(String userId) {
		  List<String> result = getSqlMapClientTemplate().queryForList("getUserAdditionMenu", userId);
	      return result.toArray(new String[]{});
	}

	@Override
	public void deleteUserAdditionMenu(String userId) {
		this.getSqlMapClientTemplate().delete("deleteUserAdditionMenu", userId);
		
	}

	@Override
	public void addUserAdditionMenu(String userId, String[] cdDm, String lrryDm,Timestamp start,Timestamp end) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("cdDm", Arrays.asList(cdDm));
		params.put("lrryDm", lrryDm);
		params.put("start", start);
		params.put("end", end);
		this.getSqlMapClientTemplate().insert("addUserAdditionMenu", params);

	}

	@Override
	public User getUserByYhDm(String yhDm) {		
		return  ((User) getSqlMapClientTemplate().queryForObject("getUserByYhDm", yhDm));
	}

	@Override
	public void updateUser(User user) {
		getSqlMapClientTemplate().update("updateUser", user);
		
	}

	@Override
	public void modifyUserPwd(String yhDm, String pwd) {
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("yhDm", yhDm);
		params.put("pwd", pwd);
		
		getSqlMapClientTemplate().update("modifyUserPwd",params);
		
	}

	@Override
	public void deleteUsers(String yhDms) {	
		getSqlMapClientTemplate().update("deleteUsers",yhDms);
		
	}

	@Override
	public int isContainYhDM(String yhDm) {
		return queryCount("isContainYhDM", yhDm);
	}

	@Override
	public int isContainYhMc(String yhMc) {
		return queryCount("isContainYhMc", yhMc);
	}

	@Override
	public User getLoginUserByYhMc(String yhMc) {
		return (User) getSqlMapClientTemplate().queryForObject("getLoginUserByYhMc", yhMc);
	}

	@Override
	public Integer selectYhDm(String yhDm) {
		return (Integer) getSqlMapClientTemplate().queryForObject("selectYhDm", yhDm);
	}

	@Override
	public String isSeeLoginOutBtn() {
		return (String) getSqlMapClientTemplate().queryForObject("isSeeLoginOutBtn");
	}

	@Override
	public String getCurrentRoleNameByStaffNum(String staffNum) {
		return (String) getSqlMapClientTemplate().queryForObject("getCurrentRoleNameByStaffNum", staffNum);
	}

	@Override
	public String isLikeCSLogin() {
		return (String) getSqlMapClientTemplate().queryForObject("isLikeCSLogin");
	}

	@Override
	public Integer validPwd(String userName, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("password", password);
		return (Integer) getSqlMapClientTemplate().queryForObject("validPwd",map);
	}

	@Override
	public List<User> getUsersByLrryDm(String lrryDm) {
		return this.getSqlMapClientTemplate().queryForList("getUsersByLrryDm", lrryDm);
	}

	@Override
	public List<String> getRoleXtAuths(String roleDm) {
		return getSqlMapClientTemplate().queryForList("getRoleXtAuths",roleDm);
	}

}
