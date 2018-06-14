package com.kuangchi.sdd.interfaceConsole.checkHandle.service;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;

/**
 * @创建时间: 2016-5-19 下午2:23:53
 * @功能描述: 对外接口-业务类
 */
public interface ICheckHandleService {
	
	/**
	 * 打卡记录上报
	 * @throws Exception 
	 */
	public boolean checkRecordReport(String record_id, String record_type, String device_mac,
			String door_num, String card_num, String event_type, String checktime) throws Exception;
	
	/**
	 * 增加打卡信息
	 * @author yuman.gao
	 */
	public boolean addCheckInfo(Map<String, Object> map);
	
	/**
	 * 增加读头事件
	 * @author yuman.gao
	 */
	public boolean addEventInfo(String device_mac,String eventType,
			String doorNum, String cardNum, String eventDm, String eventDate);
	
	/**
	 * 增加告警事件
	 * @author yuman.gao
	 */
	public boolean addEventWarningInfo(String device_mac,String eventType,
			String doorNum, String cardNum, String eventDm, String eventDate);
	
	/**
	 * 增加出入统计记录
	 * @author yuman.gao
	 */
	public boolean addAttendLog(Map<String, Object> map);
	
	/**
	 * 增加出入报警记录
	 * @author yuman.gao
	 */
	public boolean addWarningLog(Map<String, Object> map);

	/**
	 * 处理告警事件
	 * @author yuman.gao
	 */
	public boolean handleEventWarning(String device_mac, String doorNum, String eventDms);
	
	
	
	
	
	/**
	 * 读JedisPool最新10条记录（有参数）
	 * @author minting.he
	 * @param devices
	 * @param doors
	 * @return
	 */
	public List<Object> readCacheEvent(String devices, String doors);
	
	/**
	 * 读取JedisPool的告警事件
	 * @author minting.he
	 * @param devices
	 * @param doors
	 * @return
	 */
	public List<Object> readCacheWarningEvent(String devices, String doors);
	
	/**
	 * 上报设备状态
	 * @author minting.he
	 */
	public void reportDeviceState(String device_mac, String doorNum, String doorLockState, String doorDoorState, 
			String doorKeyState, String doorSkidState, String fireState, String loginUser);
	
	/**
	 * 上报后处理告警事件
	 * @author minting.he
	 * @param device_mac
	 * @param door_num
	 * @param event_dm
	 */
	public void handleWarningEvent(String device_mac, String door_num, String event_dm);
	
	/**
	 * 读取JedisPool的设备状态
	 * @author minting.he
	 * @param device_num
	 * @param door_num
	 * @return
	 */
	public Map<String, Object> readCacheDeviceState(String device_num, String door_num);
	
	/**
	 * 获取缓存中所有状态
	 * @author minting.he
	 * @return
	 */
	public List<Map<String, Object>> readCacheAllState(String devices, String doors);
	
	/**
	 * 获取redis全部刷卡事件(无参数)
	 * @author minting.he
	 * @return
	 */
	public List<Object> readAllEvent();
	
	/**
	 * 根据MAC查询设备
	 * @param device_mac
	 * @return
	 */
	public DeviceInfo getInfoByMac(String device_mac);
	
	/**
	 * 刷卡事件写数据库
	 * @author minting.he
	 * @return
	 * @throws Exception 
	 */
	public void cacheEvent(List<Map<String, Object>> nowList, List<Map<String, Object>> staffList) throws Exception;
	
}
