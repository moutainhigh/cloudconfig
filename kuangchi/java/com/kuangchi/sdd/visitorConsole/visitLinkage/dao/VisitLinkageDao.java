package com.kuangchi.sdd.visitorConsole.visitLinkage.dao;

import java.util.List;
import java.util.Map;

public interface VisitLinkageDao {

	public List<Map> getFkDevDors(Map map);
	
	public List<Map> getFkDoorSysAuths(Map map);
	
	public List<Map> getFkDoorSysAuthsNoPage(Map map);
	
	public Integer countFkDoorSysAuths(Map map);
	
	public void addFkDoorSysAuth(Map map);
	
	public void delFkDoorSysAuth(Map map);
	
	public void delFkRepeatDoorSysAuth(Map map);
	
	public List<Map> getFkTkDevs(Map map);
	
	public void addFkTkSysAuth(Map map);
	
	public void saveFkTkAuth(Map map);
	
	public void delFkTkAuthByDev(Map map);
	
	public String getAuthGroupNum();
	
	public List<Map> getAllAuthGroupNum(Map map);
	
	public Integer countAllAuthGroupNum(Map map);
	
	public List<Map> getAuthByGroupNum(Map map);
	
	public void addAuthGroup(Map map);
	
	public void delAuthGroup(Map map);
	
	/* 判断权限是否已被分配到权限组  by huixian.pan */
	public Integer  ifAuthExitInGroup(Map map);
	
	public String getDorDeviceMac(String deviceNum);
	
	public String getTkDeviceMac(String deviceNum);
	
	public List<Map> getAuthByGroupNumA(Map map);
	
	public List<Map> getFkTkSysAuths(Map map);
	
	public Integer countFkTkSysAuths(Map map);
	
	public List<Map> getFkTkSysAuthsNoPage(Map map);
	
}
