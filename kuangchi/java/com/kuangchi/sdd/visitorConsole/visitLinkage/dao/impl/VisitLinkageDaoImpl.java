package com.kuangchi.sdd.visitorConsole.visitLinkage.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.visitorConsole.visitLinkage.dao.VisitLinkageDao;

@Repository("visitLinkageDao")
public class  VisitLinkageDaoImpl extends BaseDaoImpl<Map>  implements VisitLinkageDao {

	@Override
	public String getNameSpace() {
		return "visitConsole.visitLinkage";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Map> getFkDevDors(Map map) {
		return getSqlMapClientTemplate().queryForList("getFkDevDors", map);
	}

	@Override
	public List<Map> getFkDoorSysAuths(Map map) {
		return getSqlMapClientTemplate().queryForList("getFkDoorSysAuths", map);
	}

	@Override
	public Integer countFkDoorSysAuths(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countFkDoorSysAuths", map);
	}

	@Override
	public void addFkDoorSysAuth(Map map) {
		getSqlMapClientTemplate().insert("addFkDoorSysAuth", map);
	}

	@Override
	public void delFkDoorSysAuth(Map map) {
		getSqlMapClientTemplate().delete("delFkDoorSysAuth", map);
	}

	@Override
	public void delFkRepeatDoorSysAuth(Map map) {
		getSqlMapClientTemplate().delete("delFkRepeatDoorSysAuth", map);
	}

	@Override
	public List<Map> getFkTkDevs(Map map) {
		return getSqlMapClientTemplate().queryForList("getFkTkDevs", map);
	}

	@Override
	public void addFkTkSysAuth(Map map) {
		getSqlMapClientTemplate().insert("addFkTkSysAuth", map);
	}

	@Override
	public void saveFkTkAuth(Map map) {
		getSqlMapClientTemplate().update("saveFkTkAuth", map);
	}

	@Override
	public void delFkTkAuthByDev(Map map) {
		getSqlMapClientTemplate().delete("delFkTkAuthByDev", map);
	}

	@Override
	public List<Map> getFkDoorSysAuthsNoPage(Map map) {
		return getSqlMapClientTemplate().queryForList("getFkDoorSysAuthsNoPage", map);
	}

	@Override
	public String getAuthGroupNum() {
		return (String)getSqlMapClientTemplate().queryForObject("getAuthGroupNum");
	}

	@Override
	public List<Map> getAllAuthGroupNum(Map map) {
		return getSqlMapClientTemplate().queryForList("getAllAuthGroupNum",map);
	}
	
	public Integer countAllAuthGroupNum(Map map){
		return (Integer) getSqlMapClientTemplate().queryForObject("countAllAuthGroupNum", map);
	}

	@Override
	public List<Map> getAuthByGroupNum(Map map) {
		return getSqlMapClientTemplate().queryForList("getAuthByGroupNum", map);
	}

	@Override
	public void addAuthGroup(Map map) {
		getSqlMapClientTemplate().insert("addAuthGroup", map);
	}

	@Override
	public void delAuthGroup(Map map) {
		getSqlMapClientTemplate().delete("delAuthGroup", map);
	}
	
	/* 判断权限是否已被分配到权限组  by huixian.pan */
	public Integer  ifAuthExitInGroup(Map map){
		return (Integer) getSqlMapClientTemplate().queryForObject("ifAuthExitInGroup", map);
	}

	@Override
	public String getDorDeviceMac(String deviceNum) {
		return (String)getSqlMapClientTemplate().queryForObject("getDorDeviceMac", deviceNum);
	}

	@Override
	public String getTkDeviceMac(String deviceNum) {
		return (String)getSqlMapClientTemplate().queryForObject("getTkDeviceMac", deviceNum);
	}
	
	@Override
	public List<Map> getAuthByGroupNumA(Map map) {
		return getSqlMapClientTemplate().queryForList("getAuthByGroupNumA", map);
	}

	@Override
	public List<Map> getFkTkSysAuths(Map map) {
		return getSqlMapClientTemplate().queryForList("getFkTkSysAuths", map);
	}

	@Override
	public Integer countFkTkSysAuths(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countFkTkSysAuths", map);
	}

	@Override
	public List<Map> getFkTkSysAuthsNoPage(Map map) {
		return getSqlMapClientTemplate().queryForList("getFkTkSysAuthsNoPage", map);
	}

}
