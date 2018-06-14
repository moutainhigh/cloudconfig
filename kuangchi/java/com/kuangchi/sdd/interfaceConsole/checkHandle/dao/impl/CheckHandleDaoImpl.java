package com.kuangchi.sdd.interfaceConsole.checkHandle.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attend.model.AttendModel;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventModel;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventWarningModel;
import com.kuangchi.sdd.interfaceConsole.checkHandle.dao.ICheckHandleDao;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-5-19 下午2:20:14
 * @功能描述: 对外接口-dao实现类
 */
@Repository("checkHandleDaoImpl")
public class CheckHandleDaoImpl extends BaseDaoImpl<Object> implements ICheckHandleDao {

	@Override
	public String getNameSpace() {
		return "interfaceConsole.CheckHandle";
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	@Override
	public boolean addCheckInfo(Map<String, Object> map) {
		
		// 根据条件封装考勤记录model, 如果model为空，说明数据不合法，则不插入
		AttendModel attendModel = (AttendModel) this.getSqlMapClientTemplate().queryForObject("iGetAttendRecordModel", map);
		
		if(attendModel != null){
			
			attendModel.setChecktime((String)map.get("checktime"));
			attendModel.setFlag_status((String)map.get("flag_status"));
			attendModel.setDoor_num(map.get("door_num").toString());
			attendModel.setDevice_mac(map.get("device_mac").toString());
			
			String doorName = (String) this.getSqlMapClientTemplate().queryForObject("iGetDoorNameByNum", map);
			if(doorName != null){
				attendModel.setDoor_name(doorName);
			}
			return insert("iAddCheckInfo", attendModel);
			
		} else {
			return false;
		}
	}

	@Override
	public List<DeviceEventModel> getEventInfo(String deviceNum, String doorNum,
			String cardNum, String eventDm) {
		Map<String, String> map=new HashMap<String, String>();
	    map.put("deviceNum", deviceNum);
	    map.put("doorNum", doorNum);
	    map.put("cardNum", cardNum);
	    map.put("eventDm", eventDm);
		return this.getSqlMapClientTemplate().queryForList("iGetEventInfo", map);
	}

	@Override
	public List<DeviceEventWarningModel> getEventWarnInfo(String deviceNum,
			String doorNum, String cardNum, String eventDm) {
		Map<String, String> map=new HashMap<String, String>();
	    map.put("deviceNum", deviceNum);
	    map.put("doorNum", doorNum);
	    map.put("cardNum", cardNum);
	    map.put("eventDm", eventDm);
		return this.getSqlMapClientTemplate().queryForList("iGetEventWarnInfo", map);
	}

	@Override
	public boolean addAttendLog(Map<String, Object> map) {
		String device_num = (String)this.getSqlMapClientTemplate().queryForObject("iGetNumByMac", map.get("device_mac"));
		String wordbook_name = (String)this.getSqlMapClientTemplate().queryForObject("iGetWordnameByValue", map.get("event_dm"));
		
		map.put("device_num", device_num);
		map.put("od_type", wordbook_name);
		return insert("iAddAttendLog", map);
	}
	
	@Override
	public boolean addWarningLog(Map<String, Object> map) {
		String device_num = (String)this.getSqlMapClientTemplate().queryForObject("iGetNumByMac", map.get("device_mac"));
		String wordbook_name = (String)this.getSqlMapClientTemplate().queryForObject("iGetWordnameByValue", map.get("event_dm"));
		
		map.put("device_num", device_num);
		map.put("od_type", wordbook_name);
		return insert("iAddWarningLog", map);
	}

	@Override
	public boolean handleEventWarning(String device_mac, String doorNum,
			String eventDms) {
		
		String device_num = (String)this.getSqlMapClientTemplate().queryForObject("iGetNumByMac", device_mac);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("device_num", device_num);
		map.put("door_num", doorNum);
		map.put("event_dms", eventDms);
		
		return update("iHandleEventWarning", map);
	}
	
	@Override
	public boolean insertCheckInfo(List<Map<String, Object>> list){
		return insert("insertCheckInfo", list);
	}
	
	@Override
	public boolean insertAttendLog(List<Map<String, Object>> list){
		for(Map<String, Object> map:list){
			String wordbook_name = (String)this.getSqlMapClientTemplate().queryForObject("iGetWordnameByValue", map.get("event_dm"));
			map.put("od_type", wordbook_name);
		}
		return insert("insertAttendLog", list);
	}
	
	@Override
	public boolean insertWarningLog(Map map){
		try{
			String wordbook_name = (String)this.getSqlMapClientTemplate().queryForObject("iGetWordnameByValue", map.get("event_dm"));
			map.put("od_type", wordbook_name);
			return insert("iAddWarningLog", map);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public String getAttendRecordModel(Map map){
		return (String) this.getSqlMapClientTemplate().queryForObject("getAttendRecordModel", map);
	}
	
	
}
