package com.kuangchi.sdd.interfaceConsole.checkHandle.quardz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.kuangchi.sdd.baseConsole.device.dao.DeviceDao;
import com.kuangchi.sdd.baseConsole.event.dao.EventDao;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.interfaceConsole.checkHandle.dao.ICheckHandleDao;
import com.kuangchi.sdd.interfaceConsole.checkHandle.service.ICheckHandleService;
import com.kuangchi.sdd.util.cacheUtils.CacheUtils;

/**
 * 将缓存记录写进数据库定时器
 * @author minting.he
 *
 */
public class DoorSysEventQuardz {

	private ICronService cronService;
	private EventDao eventDao;
	private ICheckHandleDao checkHandleDao;
	private ICheckHandleService checkHandleService;
	private DeviceDao deviceDao;
	
	public ICronService getCronService() {
		return cronService;
	}
	public void setCronService(ICronService cronService) {
		this.cronService = cronService;
	}
	public EventDao getEventDao() {
		return eventDao;
	}
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	public ICheckHandleDao getCheckHandleDao() {
		return checkHandleDao;
	}
	public void setCheckHandleDao(ICheckHandleDao checkHandleDao) {
		this.checkHandleDao = checkHandleDao;
	}
	public ICheckHandleService getCheckHandleService() {
		return checkHandleService;
	}
	public void setCheckHandleService(ICheckHandleService checkHandleService) {
		this.checkHandleService = checkHandleService;
	}
	public DeviceDao getDeviceDao() {
		return deviceDao;
	}
	public void setDeviceDao(DeviceDao deviceDao) {
		this.deviceDao = deviceDao;
	}
	
	@Value("${redisConnectIp}")
	private String redisConnectIp;
	@Value("${redisConnectPort}")
	private String redisConnectPort;
	
	
	/**
	 * 缓存记录写进数据库
	 * @author minting.he
	 * @throws Exception 
	 */
	public void doorSysEvent() throws Exception{
		//集群访问时，只有与数据库中相同的IP地址可以执行定时器的业务操作
		boolean r = cronService.compareIP();	
		if(r){
			List<Map<String, Object>> nowList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> staffList = new ArrayList<Map<String, Object>>();
			CacheUtils<Map<String, Object>> cacheEvent = new CacheUtils<Map<String,Object>>(redisConnectIp, Integer.valueOf(redisConnectPort));
			try{
				CacheUtils<List<Map<String, Object>>> cacheList = new CacheUtils<List<Map<String,Object>>>(redisConnectIp, Integer.valueOf(redisConnectPort));
				List<Map<String, Object>> warningEventList = cacheList.getObject("warningEventList");
				List<Map<String, Object>> warningLog = cacheList.getObject("warningLog");	
				List<Map<String, Object>> deviceState = cacheList.getObject("deviceState");
				List<Map<String, Object>> handleList = cacheList.getObject("handleList");
				
				/*
				 * 写读头事件
				 */
				Map<String, Object> map1 = cacheEvent.pullObject("eventList");
				do{
					if(map1!=null){
						//全部数据：出入统计、读头事件
						nowList.add(map1);
						//员工数据：考勤表
						if(map1.get("log_type")!=null && "0".equals(map1.get("log_type").toString())){	
							String device_id = checkHandleDao.getAttendRecordModel(map1);
							if(device_id!=null && !"".equals(device_id)){
								map1.put("device_id", device_id);
							}
							staffList.add(map1);
						}
						if(nowList.size()==100){
							checkHandleService.cacheEvent(nowList, staffList);
							nowList.clear();
							staffList.clear();
						}
						map1 = cacheEvent.pullObject("eventList");
					}
				}while(map1!=null);
				
				if(nowList!=null && nowList.size()!=0){
					checkHandleService.cacheEvent(nowList, staffList);
					nowList.clear();
					staffList.clear();
				}
	
				
				/*
				 * 写告警日志
				 */
				if(warningLog!=null){
					for(int i=0; i<warningLog.size(); i++){
						Map<String, Object> map = warningLog.get(i);
						map.put("deal_state", "1");
						
						//如果存在告警事件的缓存中，则未处理，默认已处理
						if(warningEventList!=null){
							for(Map<String, Object> warningMap : warningEventList){
								if(warningMap.get("device_mac").toString().equalsIgnoreCase(map.get("device_mac").toString()) && 
										warningMap.get("door_num").toString().equals(map.get("door_num").toString()) && 
												warningMap.get("event_dm").toString().equals(map.get("event_dm").toString())){
									map.put("deal_state", "0"); 
									break;
								}
							}
						}
						
						//告警事件：判断该数据是否存在，是则更新，否则增加
						Integer count = eventDao.ifExistWarning(map);
						if(count!=null){
							if(count!=0){
								eventDao.updateWarningInfo(map);
							} else {
								eventDao.insertWarningEvent(map);
							}
						}
						//报警统计
						boolean r1 = checkHandleDao.insertWarningLog(map);
						if(r1){
							warningLog.remove(i);
							--i;
						}else {
							continue;
						}
					}
				}else {
					warningLog = new ArrayList<Map<String, Object>>();
				}
				cacheList.saveObject("warningLog", warningLog);
				
				/*
				 * 更新设备状态
				 */
				if(deviceState!=null){
					for(Map<String, Object> map : deviceState){
						deviceDao.updateEveryDeviState(map);
					}
				}else {
					deviceState = deviceDao.getNowDeviceState();
				}
				cacheList.saveObject("deviceState", deviceState);
				
				/*
				 * 处理告警事件
				 */
				if(handleList!=null){
					for(int i=0; i<handleList.size(); i++){
						Map<String, Object> map = handleList.get(i);
						boolean r1 = eventDao.handleWarningEvent(map);
						if(r1){
							handleList.remove(i);
							--i;
						}else {
							continue;
						}
					}
				}else {
					handleList = new ArrayList<Map<String, Object>>();
				}
				cacheList.saveObject("handleList", handleList);
				
			}catch(Exception e){
				if(nowList!=null && nowList.size()!=0){
					for(Map<String, Object> nowMap : nowList){
						cacheEvent.pushObject("eventList", nowMap);
					}
				}
				e.printStackTrace();
				throw e;
			}
		}
	}
	
}
