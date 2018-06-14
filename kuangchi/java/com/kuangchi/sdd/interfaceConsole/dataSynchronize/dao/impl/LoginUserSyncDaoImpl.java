package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.LoginUserSyncDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.LoginUserSyncModel;

@Repository("loginUserSyncDaoImpl")
public class LoginUserSyncDaoImpl extends BaseDaoImpl<LoginUserSyncModel> implements LoginUserSyncDao {

	@Override
	public String getNameSpace() {
		return "common.LoginUserSync";
	}
	@Override
	public String getTableName() {
		return null;
	}
	@Override
	public void addLoginUserSync(LoginUserSyncModel loginUserSync) {
		getSqlMapClientTemplate().insert("addLoginUserSync", loginUserSync);
	}
	@Override
	public void add_modifyLoginUserSync(LoginUserSyncModel loginUserSync) {
		getSqlMapClientTemplate().insert("add_modifyLoginUserSync", loginUserSync);
	}
	@Override
	public void modifyLoginUserSync(LoginUserSyncModel loginUserSync) {
		getSqlMapClientTemplate().update("modifyLoginUserSync", loginUserSync);
	}

	@Override
	public void delLoginUserSync(String userName) {
		//单个删除
		getSqlMapClientTemplate().delete("delLoginUserSync",userName);
	}

	@Override
	public List<LoginUserSyncModel> getCountByYh_Mc(String yh_mc) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("yh_mc", yh_mc);
		return this.getSqlMapClientTemplate().queryForList("getCountByYh_Mc", mapParam);
	}

	@Override
	public void modifyLoginUser_NoRole(LoginUserSyncModel loginUserSync) {
		getSqlMapClientTemplate().insert("modifyLoginUser_NoRole", loginUserSync);
		
	}
	@Override
	public void modifyLoginUser_Role(String yh_mc,String supperRole) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("yh_mc", yh_mc);
		mapParam.put("gly_bj", supperRole);
		getSqlMapClientTemplate().insert("modifyLoginUser_Role", mapParam);
		
	}

	

	

}
