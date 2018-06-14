package com.kuangchi.sdd.baseConsole.event.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.event.dao.EventDao;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventModel;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventWarningModel;
import com.kuangchi.sdd.baseConsole.event.model.SearchEventModel;
import com.kuangchi.sdd.baseConsole.log.model.Log;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.model.SearchModel;

@Repository("eventDao")
public class EventDaoImpl  extends BaseDaoImpl<Object> implements EventDao{

	@Override
	public boolean addEventInfo(String eventType, String deviceNum,String device_mac,
			String doorNum, String cardNum, String eventDm,
			String eventDescription, String EventDate) {
		
	      Map<String, String> map=new HashMap<String, String>();
	      map.put("eventType", eventType);
	      map.put("deviceNum", deviceNum);
	      map.put("doorNum", doorNum);
	      map.put("cardNum", cardNum);
	      map.put("eventDm", eventDm);
	      map.put("eventDescription", eventDescription);
	      map.put("EventDate", EventDate);
	      map.put("deviceMac", device_mac);
	      map.put("deviceName", (String) this.getSqlMapClientTemplate().queryForObject("getDeviceNameByNum", deviceNum));
	      map.put("staffName", (String) this.getSqlMapClientTemplate().queryForObject("getStaffNameByCard", cardNum));
	      map.put("doorName", (String) this.getSqlMapClientTemplate().queryForObject("getDoorNameByNum", map));
	      map.put("eventName", (String) this.getSqlMapClientTemplate().queryForObject("getEventNameByDm", eventDm));
	      
	      return insert("addEventInfo",map);
	      
		
	}

	@Override
	public boolean addEventWarningInfo(String eventType, String deviceNum,String device_mac,
			String doorNum, String cardNum, String eventDm,
			String eventDescription, String EventDate) {
		  Map<String, String> map=new HashMap<String, String>();
	      map.put("eventType", eventType);
	      map.put("deviceNum", deviceNum);
	      map.put("doorNum", doorNum);
	      map.put("cardNum", cardNum);
	      map.put("eventDm", eventDm);
	      map.put("eventDescription", eventDescription);
	      map.put("EventDate", EventDate);
	      map.put("deviceMac", device_mac);
	      map.put("deviceName", (String) this.getSqlMapClientTemplate().queryForObject("getDeviceNameByNum", deviceNum));
	      map.put("staffName", (String) this.getSqlMapClientTemplate().queryForObject("getStaffNameByCard", cardNum));
	      map.put("doorName", (String) this.getSqlMapClientTemplate().queryForObject("getDoorNameByNum", map));
	      map.put("eventName", (String) this.getSqlMapClientTemplate().queryForObject("getEventNameByDm", eventDm));
	      return insert("addEventWarningInfo",map);
		
	}

	@Override
	public boolean deleteEventInfo(String eventIds) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("eventIds", eventIds);
		return delete("deleteEventInfo",map);
		
	}

	@Override
	public boolean deleteEventWarningInfo(String eventIds) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("eventIds", eventIds);
		return delete("deleteEventWarningInfo",map);		
	}

	@Override
	public  List<DeviceEventModel> searchEventInfo(SearchEventModel model, Integer skip,Integer rows) {
		
		Map<String, Object> map=new HashMap<String, Object>();
		if(model!=null){
			map.put("deviceName", model.getDeviceName());
			map.put("doorName", model.getDoorName());
			map.put("cardNum", model.getCardNum());
			map.put("startDate", model.getStartDate());
			map.put("endDate", model.getEndDate());
		}
		map.put("skip", skip);
 		map.put("rows", rows);
		return  getSqlMapClientTemplate().queryForList("searchEventInfo",map);
		
	}

	@Override
	public  List<DeviceEventWarningModel> searchEventWarningInfo(String deviceName, String doorName,
			String cardNum,String startDate,String endDate, Integer skip,Integer rows) {
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("deviceName", deviceName);
		map.put("doorName", doorName);
		map.put("cardNum", cardNum);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("skip", skip);
		map.put("rows", rows);
		return getSqlMapClientTemplate().queryForList("searchEventWarningInfo",map);
		
	}
	
	@Override
	public  List<DeviceEventWarningModel> searchEventWarningInfo2(String deviceName, String doorName,
			String cardNum,String startDate,String endDate, Integer skip,Integer rows) {
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("deviceName", deviceName);
		map.put("doorName", doorName);
		map.put("cardNum", cardNum);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("skip", skip);
		map.put("rows", rows);
		return getSqlMapClientTemplate().queryForList("searchEventWarningInfo2",map);
		
	}

	
	
	
	@Override
	public Integer searchEventInfoCount(SearchEventModel model) {
		
		Map<String, String> map=new HashMap<String, String>();
		if(model!=null){
			map.put("deviceName", model.getDeviceName());
			map.put("doorName", model.getDoorName());
			map.put("cardNum", model.getCardNum());
			map.put("startDate", model.getStartDate());
			map.put("endDate", model.getEndDate());
		}
		return (Integer) getSqlMapClientTemplate().queryForObject("searchEventInfoCount",map);
	}

	@Override
	public Integer searchEventWarningInfoCount(String deviceName,
			String doorName, String cardNum,String startDate,String endDate) {
		
		Map<String, String> map=new HashMap<String, String>();
		map.put("deviceName", deviceName);
		map.put("doorName", doorName);
		map.put("cardNum", cardNum);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return (Integer) getSqlMapClientTemplate().queryForObject("searchEventWarningInfoCount",map);
	}
	@Override
	public Integer searchEventWarningInfoCount2(String deviceName,
			String doorName, String cardNum,String startDate,String endDate) {
		
		Map<String, String> map=new HashMap<String, String>();
		map.put("deviceName", deviceName);
		map.put("doorName", doorName);
		map.put("cardNum", cardNum);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return (Integer) getSqlMapClientTemplate().queryForObject("searchEventWarningInfoCount2",map);
	}
	
	@Override
	public boolean dealEventWarning(String eventId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("eventId", eventId);
		return update("dealEventWarning",map);	
	}
	
	@Override
	public String getNameSpace() {
		return "common.Event";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Map<String, Object>> getAllEvents() {
	
		return getSqlMapClientTemplate().queryForList("getAllEvents");
	}

	@Override
	public List<Map<String, Object>> getAllEventWarnings() {
	
		return getSqlMapClientTemplate().queryForList("getAllEventWarnings");
	}

	@Override
	public boolean deleteEvent(Map<String, Object> map) {
		return delete("deleteEvent",map);
	}

	@Override
	public boolean deleteEventWarning(Map<String, Object> map) {
		return delete("deleteEventWarning",map);
	}

	@Override
	public List<String> getEventColumns() {
		
		return getSqlMapClientTemplate().queryForList("getEventColumns");
	}

	@Override
	public List<String> getEventWarningColumns() {
		
		return getSqlMapClientTemplate().queryForList("getEventWarningColumns");
	}

	@Override
	public List<DeviceEventModel> exportEventInfo(String deviceName,
			String doorName, String cardNum, String startDate, String endDate) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("deviceName", deviceName);
		map.put("doorName", doorName);
		map.put("cardNum", cardNum);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return  getSqlMapClientTemplate().queryForList("exportEventInfo",map);
	}

	@Override
	public List<DeviceEventWarningModel> exportWarningEventInfo(String deviceName,
			String doorName, String cardNum, String startDate, String endDate) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("deviceName", deviceName);
		map.put("doorName", doorName);
		map.put("cardNum", cardNum);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return  getSqlMapClientTemplate().queryForList("exportEventWarningInfo",map);
	}

	@Override
	public boolean updateEventInfo(String eventType, String deviceNum,
			String doorNum, String cardNum, String eventDm,
			String eventDescription, String eventDate) {
		
		 Map<String, String> map=new HashMap<String, String>();
	     map.put("eventType", eventType);
	     map.put("deviceNum", deviceNum);
	     map.put("doorNum", doorNum);
	     map.put("cardNum", cardNum);
	     map.put("eventDm", eventDm);
	     map.put("eventDescription", eventDescription);
	     map.put("EventDate", eventDate);
	     return update("updateEventInfo",map);
		
	}

	@Override
	public boolean updateEventWarningInfo(String eventType, String deviceNum,
			String doorNum, String cardNum, String eventDm,
			String eventDescription, String eventDate) {
		 Map<String, String> map=new HashMap<String, String>();
	     map.put("eventType", eventType);
	     map.put("deviceNum", deviceNum);
	     map.put("doorNum", doorNum);
	     map.put("cardNum", cardNum);
	     map.put("eventDm", eventDm);
	     map.put("eventDescription", eventDescription);
	     map.put("EventDate", eventDate);
	     return update("updateEventWarningInfo",map);
	}

	@Override
	public List<DeviceEventModel> latestEventInfo(){
		return  getSqlMapClientTemplate().queryForList("latestEventInfo");
	}

	@Override
	public List<DeviceEventModel> getEventInterval(Map<String, Object> map){
		return getSqlMapClientTemplate().queryForList("getEventInterval", map);
	}
	
	@Override
	public boolean delEventInterval(Map<String, Object> map){
		return delete("delEventInterval", map);
	}

	@Override
	public List<DeviceEventWarningModel> getWarningEventInterval(Map<String, Object> map){
		return getSqlMapClientTemplate().queryForList("getWarningEventInterval", map);
	}
	
	@Override
	public boolean delWarningEventInterval(Map<String, Object> map){
		return delete("delWarningEventInterval", map);
	}
	
	@Override
	public String getDeviceNameByNum(String deviceNum){
		return (String) this.getSqlMapClientTemplate().queryForObject("getDeviceNameByNum", deviceNum);
	}

	@Override
	public Map<String, Object> getStaffNameByCard(String card_num) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("getStaffNameByCard", card_num);
	}

	@Override
	public String getDoorNameByNum(Map<String, Object> map) {
		return (String) this.getSqlMapClientTemplate().queryForObject("getDoorName", map);
	}

	@Override
	public String getEventNameByDm(String event_dm) {
		return (String) this.getSqlMapClientTemplate().queryForObject("getEventNameByDm", event_dm);
	}
	
	@Override
	public boolean insertEvent(List<Map<String, Object>> list){
		return this.insert("insertEvent", list);
	}
	
	@Override
	public Integer ifExistWarning(Map<String, Object> map){
		try{
			return (Integer) this.getSqlMapClientTemplate().queryForObject("ifExistWarning", map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean updateWarningInfo(Map<String, Object> map){
		try{
			return update("updateWarningInfo", map);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean insertWarningEvent(Map<String, Object> map){
		try{
			return insert("insertWarningEvent", map);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public List<Map<String, Object>> getNowWarning(){
		return this.getSqlMapClientTemplate().queryForList("getNowWarning");
	}
	
	@Override
	public boolean handleWarningEvent(Map<String, Object> map){
		try{
			return this.update("handleWarningEvent", map);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public Map<String, Object> getVisitorName(String card_num){
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("getVisitorName", card_num);
	}
	
	@Override
	public String getStaffImg(String card_num){
		return (String) this.getSqlMapClientTemplate().queryForObject("getStaffImg", card_num);
	}
	
	@Override
	public String getStaffNumByCardNum(String card_num){
		return (String) this.getSqlMapClientTemplate().queryForObject("getStaffNumByCardNum", card_num);
	}

	@Override
	public Integer ifMonitorDoor(Map<String, Object> map){
		return (Integer) getSqlMapClientTemplate().queryForObject("ifMonitorDoor", map);
	}
	
	
	/**
	 * 查询一周内各刷卡事件类型数量
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	@Override
	public List<Map> getEventByDate(Map map){
		return getSqlMapClientTemplate().queryForList("getEventByDate", map);
	}
	
	/**
	 * 查询某天的告警次数 
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	@Override
	public Integer getWarningEventByDate(Map map){
		return (Integer) getSqlMapClientTemplate().queryForObject("getWarningEventByDate", map);
	}
	
	/**
	 * 查询当前设备数，卡数，人员数
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	@Override
	public Map getCourrentDeviceCardEmpCount(){
		return (Map) getSqlMapClientTemplate().queryForObject("getCourrentDeviceCardEmpCount");
	}
	
}
