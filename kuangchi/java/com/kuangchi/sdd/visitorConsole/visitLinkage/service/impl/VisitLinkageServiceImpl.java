package com.kuangchi.sdd.visitorConsole.visitLinkage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.visitorConsole.visitLinkage.dao.VisitLinkageDao;
import com.kuangchi.sdd.visitorConsole.visitLinkage.service.VisitLinkageService;
@Service("visitLinkageService")
public class VisitLinkageServiceImpl implements VisitLinkageService {

	@Autowired
	private VisitLinkageDao visitLinkageDao;

	@Override
	public Grid getFkDevDors(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitLinkageDao.getFkDevDors(map));
		return grid;
	}

	@Override
	public Grid getFkDoorSysAuths(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitLinkageDao.getFkDoorSysAuths(map));
		grid.setTotal(visitLinkageDao.countFkDoorSysAuths(map));
		return grid;
	}
	
	@Override
	public Grid getFkTkSysAuths(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitLinkageDao.getFkTkSysAuths(map));
		grid.setTotal(visitLinkageDao.countFkTkSysAuths(map));
		return grid;
	}

	@Override
	public void addFkDoorSysAuth(List<Map> list) {
		for(Map map:list){
			map.put("deviceType","0");
			String deviceNum=map.get("deviceNum").toString();
			String mac=visitLinkageDao.getDorDeviceMac(deviceNum);
			map.put("deviceMac", mac);
			visitLinkageDao.delFkRepeatDoorSysAuth(map);
			visitLinkageDao.addFkDoorSysAuth(map);
		}
	}

	@Override
	public void delFkDoorSysAuth(Map map) {
		visitLinkageDao.delFkDoorSysAuth(map);
	}

	@Override
	public Grid getFkTkDevs(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitLinkageDao.getFkTkDevs(map));
		return grid;
	}

	@Override
	public void addFkTkSysAuth(List<Map> list) {
		for(Map map:list){
			map.put("deviceType", "1");
			String deviceNum=map.get("deviceNum").toString();
			String mac=visitLinkageDao.getTkDeviceMac(deviceNum);
			map.put("deviceMac", mac);
			//visitLinkageDao.delFkTkAuthByDev(map);
			visitLinkageDao.addFkTkSysAuth(map);
		}
	}

	@Override
	public void saveFkTkAuths(List<Map> list) {
		for(Map map:list){
			visitLinkageDao.saveFkTkAuth(map);
		}
	}

	@Override
	public Grid getFkDoorSysAuthsNoPage(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitLinkageDao.getFkDoorSysAuthsNoPage(map));
		return grid;
	}
	
	@Override
	public Grid getFkTkSysAuthsNoPage(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitLinkageDao.getFkTkSysAuthsNoPage(map));
		return grid;
	}

	@Override
	public Grid getAuthGroupInfoByGroupNum(Map map) {
		List<Map> groupNumList=visitLinkageDao.getAllAuthGroupNum(map);
		List<Map> resultList=new ArrayList<Map>();
		Grid grid=new Grid();
		for(Map m:groupNumList){//分别查询每个权限组的门禁权限
			Map newMap=new HashMap();
			newMap.putAll(makeDoorAuthMap(m));
			newMap.putAll(makeFloorAuthMap(m));
			resultList.add(newMap);
		}
		grid.setRows(resultList);
		grid.setTotal(visitLinkageDao.countAllAuthGroupNum(map));
		return grid;
	}
	
	/**
	 * 通过权限组编号查询门禁权限，并做好拼装,
	 * 将一个组对应的多个门禁权限拼成一个map并返回
	 * by gengji.yang
	 */
	private Map makeDoorAuthMap(Map map){
		map.put("deviceType", "0");
		map.put("groupNum",map.get("groupNum"));
		List<Map> list=visitLinkageDao.getAuthByGroupNum(map);
		Map doorMap=new HashMap();
		String doorPowerNum="";
		String objectNum1="";
		if(list.size()>0){
			for(Map m:list){
				doorMap.put("authGroupNum",m.get("authGroupNum"));
				doorMap.put("authGroupName",m.get("authGroupName"));
				doorPowerNum+=m.get("powerNum")+"|";
				String deviceName=m.get("deviceName")!=null?(String)m.get("deviceName"):"";
				String objectNum=m.get("objectNum")!=null?(String) m.get("objectNum"):"";
				if(!"".equals(deviceName)&&!"".equals(objectNum)){
					objectNum1+=(deviceName+":"+objectNum)+"|";
					doorMap.put("doorPowerNum",doorPowerNum.substring(0, doorPowerNum.length()-1));
					doorMap.put("objectNum1",objectNum1.substring(0,objectNum1.length()-1));
				}
			}
		}
		return doorMap;
	}

	/**
	 * 通过权限组编号查询梯控权限，并做好拼装,
	 * 将一个组对应的多个梯控权限拼成一个map并返回
	 * by gengji.yang
	 */
	private Map makeFloorAuthMap(Map map){
		map.put("deviceType", "1");
		map.put("groupNum",map.get("groupNum"));
		List<Map> list=visitLinkageDao.getAuthByGroupNumA(map);
		Map floorMap=new HashMap();
		String floorPowerNum="";
		String objectNum="";
		if(list.size()>0){
			for(Map m:list){
				floorMap.put("authGroupNum",m.get("authGroupNum"));
				floorMap.put("authGroupName",m.get("authGroupName"));
				floorPowerNum+=m.get("powerNum")+"|";
				String deviceName=m.get("deviceName")!=null?(String)m.get("deviceName"):"";
				String objectNum1=m.get("objectNum")!=null?(String) m.get("objectNum"):"";
				if(!"".equals(deviceName)&&!"".equals(objectNum1)){
					objectNum+=(deviceName+":"+objectNum1)+"|";
					floorMap.put("floorPowerNum",floorPowerNum.substring(0,floorPowerNum.length()-1));
					floorMap.put("objectNum",objectNum.substring(0,objectNum.length()-1));
				}
			}
		}
		return floorMap;
	}

	@Override
	public void addAuthGroup(List<Map> list) {
		for(Map map:list){
			if("".equals(map.get("authGroupNum"))||null==map.get("authGroupNum")){//未保存的记录
				handleAndInsert(map);
			}else{//已保存的记录，则直接修改权限组信息
				handleAndUpdate(map);
			}
		}
	}
	
	/**
	 * 拆封Map,分为门禁和梯控两个List
	 * 然后插入表中
	 * by gengji.yang
	 */
	private void handleAndInsert(Map map){
		String groupNum=visitLinkageDao.getAuthGroupNum();
		String groupName=(String)map.get("authGroupName");
		String doorPowerNum=(String)map.get("doorPowerNum");
		String floorPowerNum=(String)map.get("floorPowerNum");
		String[] doorArr=doorPowerNum.split("\\|");
		String[] floorArr=floorPowerNum.split("\\|");
		List<Map> doorList=getList(doorArr,groupNum,groupName,"0");
		List<Map> floorList=getList(floorArr,groupNum,groupName,"1");
		sendToTable(doorList);
		sendToTable(floorList);
	}
	
	/**
	 * 拆封Map,分为门禁和梯控两个List
	 * 然后更新权限组表
	 * by gengji.yang
	 */
	private void handleAndUpdate(Map map){
		//先删除权限组信息
		Map m=new HashMap();
		String groupNum=map.get("authGroupNum").toString();
		m.put("groupNums","\'"+groupNum+"\'");
		visitLinkageDao.delAuthGroup(m);
		String groupName=(String)map.get("authGroupName");
		String doorPowerNum=map.get("doorPowerNum")!=null?(String)map.get("doorPowerNum"):"";
		String floorPowerNum=map.get("floorPowerNum")!=null?(String)map.get("floorPowerNum"):"";
		String[] doorArr=doorPowerNum.split("\\|");
		String[] floorArr=floorPowerNum.split("\\|");
		List<Map> doorList=getList(doorArr,groupNum,groupName,"0");
		List<Map> floorList=getList(floorArr,groupNum,groupName,"1");
		sendToTable(doorList);
		sendToTable(floorList);
	}
	
	/**
	 * 抽象一个方法
	 * by gengji.yang
	 */
	public List<Map> getList(String[] strArr,String groupNum,String groupName,String groupIdentity){
		List<Map> list=new ArrayList<Map>();
		for(String str:strArr){
			Map m=new HashMap();
			m.put("groupNum", groupNum);
			m.put("groupName", groupName);
			m.put("powerNum",str);
			m.put("groupIdentity", groupIdentity);
			list.add(m);
		}
		return list;
	}
	
	/**
	 * 插入权限组表
	 * by gengji.yang
	 */
	public void sendToTable(List<Map> list){
		for(Map map:list){
			visitLinkageDao.addAuthGroup(map);
		}
	}

	@Override
	public void delAuthGroup(Map map) {
		visitLinkageDao.delAuthGroup(map);
	}

	
	/* 判断权限是否已被分配到权限组  by huixian.pan */
	public boolean  ifAuthExitInGroup(Map map){
		if(visitLinkageDao.ifAuthExitInGroup(map)>0){
			return true;
		}else{
			return false;
		}
	}
}
