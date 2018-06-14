package com.kuangchi.sdd.interfaceConsole.checkHandle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.attendanceConsole.statistic.service.impl.StatisticServiceImpl;
import com.kuangchi.sdd.baseConsole.device.dao.DeviceDao;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.event.dao.EventDao;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventWarningModel;
import com.kuangchi.sdd.interfaceConsole.checkHandle.dao.ICheckHandleDao;
import com.kuangchi.sdd.interfaceConsole.checkHandle.service.ICheckHandleService;
import com.kuangchi.sdd.util.cacheUtils.CacheUtils;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.datastructure.BoundedQueueUsedForDisplay;

/**
 * @创建时间: 2016-5-19 下午2:24:54
 * @功能描述: 对外接口-业务实现类
 */
@Transactional
@Service("checkHandleServiceImpl")
public class CheckHandleServiceImpl implements ICheckHandleService {
	
//	public static final Logger LOG = Logger.getLogger(CheckHandleServiceImpl.class);

	@Resource(name = "checkHandleDaoImpl")
	private ICheckHandleDao checkHandleDao;
	@Resource(name="eventDao")
	private EventDao eventDao;
	@Resource(name="deviceDao")
	private DeviceDao deviceDao;
	
	@Value("${redisConnectIp}")
	private String redisConnectIp;
	@Value("${redisConnectPort}")
	private String redisConnectPort;
	
	@Override
	public boolean checkRecordReport(String record_id, String record_type, String device_mac,
			String door_num, String card_num, String event_type, String checktime) throws Exception{
		try{
			
			CacheUtils<Map<String, Object>> cacheEvent = new CacheUtils<Map<String,Object>>(redisConnectIp, Integer.valueOf(redisConnectPort));
			CacheUtils<List<Map<String, Object>>> cacheList = new CacheUtils<List<Map<String,Object>>>(redisConnectIp, Integer.valueOf(redisConnectPort));
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("card_num", card_num);
			map.put("checktime", checktime);
			map.put("device_mac", device_mac);
			map.put("record_id", record_id);
			map.put("flag_status","0");
			DeviceInfo device = deviceDao.getInfoByMac(device_mac);
			map.put("device_num", device.getDevice_num());
			map.put("device_ip", device.getLocal_ip_address());
			map.put("device_name", device.getDevice_name());
			map.put("event_dm",event_type);
			map.put("event_type", event_type);
			map.put("event_desc", null);
			String event_name = eventDao.getEventNameByDm(event_type);
			map.put("event_name", event_name);
			if(event_name==null || "".equals(event_name)){
				map.put("event_name", "非法卡");
			}else {
				map.put("event_name", event_name);
			}
			Map<String, Object> staffMap = eventDao.getStaffNameByCard(card_num);
			if(staffMap!=null && staffMap.size()!=0){
				map.put("staff_name", staffMap.get("staff_name").toString());
				map.put("staff_num", staffMap.get("staff_num").toString());
				map.put("staff_no", staffMap.get("staff_no").toString());
				map.put("dept_no", staffMap.get("bm_no").toString());
				map.put("dept_name", staffMap.get("bm_mc").toString());
				map.put("log_type", "0");
			}else {
				Map<String, Object> visitorMap = eventDao.getVisitorName(card_num);
				if(visitorMap!=null && visitorMap.size()!=0){
					map.put("staff_name", "【访客】"+visitorMap.get("m_visitor_name").toString());
					map.put("staff_num", visitorMap.get("m_visitor_num").toString());
					map.put("staff_no", "");
					map.put("dept_no", "");
					map.put("dept_name", "");
					map.put("log_type", "1");
				}else {
					map.put("staff_name", "");
					map.put("staff_num", "");
					map.put("staff_no", "");
					map.put("dept_no", "");
					map.put("dept_name", "");
					map.put("log_type", "");
				}
			}
			
			if("2".equals(record_type)){	//读头事件
				CacheUtils<BoundedQueueUsedForDisplay> cacheQueue = new CacheUtils<BoundedQueueUsedForDisplay>(redisConnectIp, Integer.valueOf(redisConnectPort));
				
				map.put("door_num", door_num);
				map.put("door_name", eventDao.getDoorNameByNum(map));
				
				cacheEvent.pushObject("eventList", map);

				BoundedQueueUsedForDisplay q = cacheQueue.getObject("doorSysQueue");
				if(q==null){
					q = new BoundedQueueUsedForDisplay(50);
				}
				q.offer(map);
				cacheQueue.saveObject("doorSysQueue", q);
				BoundedQueueUsedForDisplay l = cacheQueue.getObject("doorSysLatest");
				if(l==null){
					l = new BoundedQueueUsedForDisplay(5);
				}
				l.offer(map);
				cacheQueue.saveObject("doorSysLatest", l);
				
			} else if("0".equals(record_type)){		//告警事件
				
				// 如果为消防事件，则每个门都增加告警事件；否则只为指定门号增加告警事件
				List<String> doorNums = new ArrayList<String>();
				if("81".equals(event_type) || "82".equals(event_type)){
					for(int i=1; i<=Integer.valueOf(device.getDevice_type()); i++){
						doorNums.add(i+"");
					}
				} else {
					doorNums.add(door_num);
				}
				
				//遍历门号，新增告警事件
				for (String num : doorNums) {
					map.put("door_num", num);
					map.put("door_name", eventDao.getDoorNameByNum(map));
					
					List<Map<String, Object>> warningEventList = cacheList.getObject("warningEventList");	//告警事件
					if(warningEventList==null){
						warningEventList = eventDao.getNowWarning();								//当前未处理的告警事件
					}
					List<Map<String, Object>> warningLog = cacheList.getObject("warningLog");		//告警日志
					if(warningLog==null){
						warningLog = new ArrayList<Map<String, Object>>();
					}
					
					for(int i=0; i<warningEventList.size(); i++){		//如果告警事件缓存中已存在，则删除旧的再新增
						Map<String, Object> record = warningEventList.get(i);
						if(record.get("device_num").toString().equals(map.get("device_num").toString()) && 
								record.get("door_num").toString().equals(map.get("door_num").toString()) && 
								record.get("card_num").toString().equals(map.get("card_num").toString()) && 
								record.get("event_dm").toString().equals(map.get("event_dm").toString()) ){
							warningEventList.remove(i);
							break;
						}
					}
					warningEventList.add(map);	
					warningLog.add(map);
					cacheList.saveObject("warningEventList", warningEventList);
					cacheList.saveObject("warningLog", warningLog);
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	@Override
	public boolean addCheckInfo(Map<String, Object> map) {
		return checkHandleDao.addCheckInfo(map);
	}

	@Override
	public boolean addEventInfo(String device_mac,String eventType,
			String doorNum, String cardNum, String eventDm, String eventDate) {
		
		String deviceNum = deviceDao.getDeviceNumByMac(device_mac);
		eventDao.addEventInfo(eventType, deviceNum, device_mac, doorNum, cardNum, eventDm, null, eventDate);
		return true;
	}

	@Override
	public boolean addEventWarningInfo(String device_mac, String eventType,
			String doorNum, String cardNum, String eventDm, String eventDate) {
		String deviceNum = deviceDao.getDeviceNumByMac(device_mac);
		// 如果为消防事件，则每个门都增加告警事件；否则只为指定门号增加告警事件
		List<String> doorNums = new ArrayList<String>();
		if(eventDm.equals("81")||eventDm.equals("82")){
			doorNums.add("1");
			doorNums.add("2");
			doorNums.add("3");
			doorNums.add("4");
		} else {
			doorNums.add(doorNum);
		}
		//遍历门号，判断该数据是否存在，是则更新，否则增加
		for (String door : doorNums) {
			List<DeviceEventWarningModel> existWarningEvent = checkHandleDao.getEventWarnInfo(deviceNum, door, cardNum, eventDm);
			if(existWarningEvent != null && existWarningEvent.size() != 0){
				eventDao.updateEventWarningInfo(eventType, deviceNum, door, cardNum, eventDm, null, eventDate);
			} else {
				eventDao.addEventWarningInfo(eventType, deviceNum, device_mac, door, cardNum, eventDm, null, eventDate);
			}
		}
		return true;
	}
	
	@Override
	public boolean addAttendLog(Map<String, Object> map) {
		return checkHandleDao.addAttendLog(map);
	}
	
	@Override
	public boolean addWarningLog(Map<String, Object> map){
		return checkHandleDao.addWarningLog(map);
	}
	
	@Override
	public boolean handleEventWarning(String device_mac, String doorNum,
			String event_dms) {
		return checkHandleDao.handleEventWarning(device_mac, doorNum, event_dms);
	}



	
	
	
	@Override
	public List<Object> readCacheEvent(String devices, String doors){
		List<Object> list2 = new ArrayList<Object>();
		List<Object> list = new ArrayList<Object>();
		try{
			CacheUtils<BoundedQueueUsedForDisplay> cacheQueue = new CacheUtils<BoundedQueueUsedForDisplay>(redisConnectIp, Integer.valueOf(redisConnectPort));
			BoundedQueueUsedForDisplay q = cacheQueue.getObject("doorSysQueue");
			if(q==null){
				q = new BoundedQueueUsedForDisplay(50);
				cacheQueue.saveObject("doorSysQueue", q);
			}
			List<Object> queue = q.list();
			if(queue!=null && queue.size()!=0){
				if("".equals(devices) || "".equals(doors)){
					list2 = queue;
				}else {
					String[] device = devices.split(",");
					String[] door = doors.split(",");
					for (int i=queue.size()-1; i>=0; i--) {	
						Map<String, Object> map = (Map<String, Object>) queue.get(i);
						if(map!=null){
							for(int j=0; j<device.length; j++){
								if(device[j].equals(map.get("device_num").toString()) && 
										door[j].equals(map.get("door_num").toString())){
									list.add(map);
									break;
								}
							}
						}
					}
					for (int i = list.size()-1; i >= 0; i--) {
						list2.add(list.get(i));
					}
				}
			}
			return list2;
		}catch(Exception e){
			e.printStackTrace();
			return list2;
		}
	}
	
	@Override
	public List<Object> readCacheWarningEvent(String devices, String doors){
		List<Object> list = new ArrayList<Object>();
		try{	
			CacheUtils<List<Map<String, Object>>> cacheList = new CacheUtils<List<Map<String,Object>>>(redisConnectIp, Integer.valueOf(redisConnectPort));
			List<Map<String, Object>> warningEventList = cacheList.getObject("warningEventList");
			if(warningEventList==null){
				warningEventList = eventDao.getNowWarning();
				cacheList.saveObject("warningEventList", warningEventList);
			}
			/*if(warningEventList!=null){
				for (int i=warningEventList.size()-1; i>=0; i--) {
					if(warningEventList.get(i)!=null){
						list.add(warningEventList.get(i));
					}
				}
			}*/
			if(warningEventList!=null && warningEventList.size()!=0){
				if("".equals(devices) || devices==null){
					for(int i=warningEventList.size()-1; i>=0; i--) {
						if(warningEventList.get(i)!=null){
							list.add(warningEventList.get(i));
						}
					}
				}else {
					String[] device = devices.split(",");
					String[] door = doors.split(",");
					for (int i=warningEventList.size()-1; i>=0; i--) {
						if(warningEventList.get(i)!=null){
							for(int j=0; j<device.length; j++){	
								if(device[j].equals(warningEventList.get(i).get("device_num").toString()) && 
										door[j].equals(warningEventList.get(i).get("door_num").toString())){
									list.add(warningEventList.get(i));
									break;
								}
							}
						}
					}
				}
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return list;
		}
	}
	
	@Override
	public void reportDeviceState(String device_mac, String doorNum, String doorLockState, String doorDoorState, 
			String doorKeyState, String doorSkidState, String fireState, String loginUser){
		try{
			CacheUtils<List<Map<String, Object>>> cacheList = new CacheUtils<List<Map<String,Object>>>(redisConnectIp, Integer.valueOf(redisConnectPort));
			DeviceInfo device = deviceDao.getInfoByMac(device_mac);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("device_mac", device_mac);
			map.put("device_num", device.getDevice_num());
			map.put("door_num", doorNum);
			map.put("lock_state", doorLockState);
			map.put("door_state", doorDoorState);
			map.put("key_state", doorKeyState);
			map.put("skid_state", doorSkidState);
			map.put("fire_state", fireState);
			
			List<Map<String, Object>> deviceState = cacheList.getObject("deviceState");
			if(deviceState==null || deviceState.size()==0){
				deviceState = deviceDao.getNowDeviceState();
			}
			for(int i=0; i<deviceState.size(); i++){			//缓存中已存在，则删除旧的再新增
				Map<String, Object> record = deviceState.get(i);
				if(record!=null){
					if(record.get("device_num").toString().equals(map.get("device_num").toString()) && 
							record.get("door_num").toString().equals(map.get("door_num").toString()) ){		//设备号和门号确定一个门
						deviceState.remove(i);
						break;
					}
				}
			}
			deviceState.add(map);
			cacheList.saveObject("deviceState", deviceState);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void handleWarningEvent(String device_mac, String door_num, String event_dm){
		try{
			CacheUtils<List<Map<String, Object>>> cacheList = new CacheUtils<List<Map<String,Object>>>(redisConnectIp, Integer.valueOf(redisConnectPort));
			String[] dms  = event_dm.split(",");								//事件代码不为空，即有状态为0
			if(dms!=null && dms.length!=0){
				List<Map<String, Object>> warningEventList = cacheList.getObject("warningEventList");
				List<Map<String, Object>> handleList = cacheList.getObject("handleList");
				if(handleList==null){
					handleList = new ArrayList<Map<String, Object>>();
				}
				if(warningEventList!=null){
					for (int i=0; i<warningEventList.size(); i++) {
						Map<String, Object> record1 = warningEventList.get(i);
						if(record1!=null && record1.get("device_mac").toString().equalsIgnoreCase(device_mac) && record1.get("door_num").toString().equals(door_num)){
							for(String dm1 : dms){
								if(record1.get("event_dm").toString().equals(dm1)){
									warningEventList.remove(i);					//移除缓存中的告警事件，不再显示
									cacheList.saveObject("warningEventList", warningEventList);
									for (int j=0; j<handleList.size(); j++) {
										Map<String, Object> record2 = handleList.get(j);
										if(record2!=null && record2.get("device_mac").toString().equalsIgnoreCase(device_mac) && record2.get("door_num").toString().equals(door_num)){
											if(record2.get("event_dm").toString().equals(dm1)){
												handleList.remove(j);			
												break;
											} 
										}
									}
									handleList.add(record1);					//放进处理缓存中，等待处理
									cacheList.saveObject("handleList", handleList);
								}
							}
						}
					}
				}else {
					warningEventList = eventDao.getNowWarning();
					cacheList.saveObject("warningEventList", warningEventList);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public Map<String, Object> readCacheDeviceState(String device_num, String door_num){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			CacheUtils<List<Map<String, Object>>> cacheList = new CacheUtils<List<Map<String,Object>>>(redisConnectIp, Integer.valueOf(redisConnectPort));
			List<Map<String, Object>> deviceState = cacheList.getObject("deviceState");
			if(deviceState==null || deviceState.size()==0){
				deviceState = deviceDao.getNowDeviceState();
				cacheList.saveObject("handleList", deviceState);
			}
			for(int i=0; i<deviceState.size(); i++){
				Map<String, Object> record = deviceState.get(i);
				if(record!=null){
					if(record.get("device_num").toString().equals(device_num) && 
							record.get("door_num").toString().equals(door_num) ){		//设备号和门号确定一个门
						map = record;
						break;
					}
				}
			}
			return map;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Map<String, Object>> readCacheAllState(String devices, String doors){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try{	
			if(devices!=null && !"".equals(devices)){
				CacheUtils<List<Map<String, Object>>> cacheList = 
						new CacheUtils<List<Map<String,Object>>>(redisConnectIp, Integer.valueOf(redisConnectPort));
				List<Map<String, Object>> deviceState = cacheList.getObject("deviceState");
				if(deviceState==null || deviceState.size()==0){
					deviceState = deviceDao.getNowDeviceState();
					cacheList.saveObject("deviceState", deviceState);
				}
				String[] device =  devices.split(",");
				String[] door =  doors.split(",");
				//找到正在被监控的门状态
				for(int i=0; i<device.length; i++){
					for(int j=0; j<deviceState.size(); j++){
						if(device[i].equals(deviceState.get(j).get("device_num")) && 
								door[i].equals(deviceState.get(j).get("door_num"))){
							list.add(deviceState.get(j));
							break;
						}
					}
				}
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return list;
		}
	}
	
	@Override
	public List<Object> readAllEvent(){
		List<Object> list = new ArrayList<Object>();
		try{
			CacheUtils<BoundedQueueUsedForDisplay> cacheQueue = new CacheUtils<BoundedQueueUsedForDisplay>(redisConnectIp, Integer.valueOf(redisConnectPort));
			BoundedQueueUsedForDisplay q = cacheQueue.getObject("doorSysQueue");
			if(q==null){
				q = new BoundedQueueUsedForDisplay(50);
				cacheQueue.saveObject("doorSysQueue", q);
			}
			List<Object> queue = q.list();
			if(queue!=null){
				list = queue;
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return list;
		}
	}

	@Override
	public DeviceInfo getInfoByMac(String device_mac) {
		return deviceDao.getInfoByMac(device_mac);
	}

	@Override
	public void cacheEvent(List<Map<String, Object>> nowList, List<Map<String, Object>> staffList) throws Exception{
		try{
			eventDao.insertEvent(nowList);
			checkHandleDao.insertAttendLog(nowList);
			if(staffList!=null && staffList.size()!=0){
				checkHandleDao.insertCheckInfo(staffList);
			}
		}catch(Exception e){
			throw e;
		}
	}
	
}
