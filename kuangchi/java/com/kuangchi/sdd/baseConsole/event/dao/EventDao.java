package com.kuangchi.sdd.baseConsole.event.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.event.model.DeviceEventModel;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventWarningModel;
import com.kuangchi.sdd.baseConsole.event.model.SearchEventModel;

public interface EventDao {
	
	/*
	 * 添加设备事件
	 * eventType 事件类型
	 * deviceNum 设备编号
	 * doorNum 门编号
	 * cardNum 门编号
	 * eventNum 事件代码
	 * eventDescription 事件描述
	 * eventDate 事件日期  2012-10-11 12:12:12 格式 
	 * **/
	public boolean addEventInfo(String eventType,String deviceNum,String device_mac,String doorNum,String cardNum,String eventDm,String eventDescription,String EventDate);
	/*
	 * 添加告警事件
	 * eventType 事件类型
	 * deviceNum 设备编号
	 * doorNum 门编号
	 * cardNum 门编号
	 * eventNum 事件代码
	 * eventDescription 事件描述
	 * eventDate 事件日期  2012-10-11 12:12:12 格式 
	 * **/
    public boolean addEventWarningInfo(String eventType,String deviceNum,String device_mac,String doorNum,String cardNum,String eventDm,String eventDescription,String EventDate);
    	/*
    	 * 删除设备事件
    	 * eventIds 事件id   格式 : '1','2','3'
    	 * **/
    public boolean deleteEventInfo(String eventIds);
	/*
	 * 删除告警事件
	 * eventIds 事件id   格式 : '1','2','3'
	 * **/
    public boolean deleteEventWarningInfo(String eventIds);   
	/*
	 * 查询设备事件
	 * deviceName  设备名称
	 * doorName    门名称
	 * skip  从第几行开始
     * rows  往后获取多少行
	 * cardNum     卡名称
	 *startDate  从什么时间开始
      * endDate    到什么时间结束
	 * **/
    public List<DeviceEventModel> searchEventInfo(SearchEventModel model, Integer skip,Integer rows);
	/*
	 * 查询设备事件总记录数
	 * deviceName  设备名称
	 * doorName    门名称 	
	* cardNum     卡名称
	* startDate  从什么时间开始
      * endDate    到什么时间结束
	 * **/
    public Integer searchEventInfoCount(SearchEventModel model);
	/*
	 * 查询告警事件
	 * deviceName  设备名称
	 * doorName    门名称
	 * cardNum     卡名称
	 *  startDate  从什么时间开始
     * endDate    到什么时间结束
     * skip  从第几行开始
     * rows  往后获取多少行
	 * **/
    public List<DeviceEventWarningModel> searchEventWarningInfo(String deviceName,String doorName,String cardNum,String startDate,String endDate,Integer skip,Integer rows);
    
    public List<DeviceEventWarningModel> searchEventWarningInfo2(String deviceName,String doorName,String cardNum,String startDate,String endDate,Integer skip,Integer rows);

    /*
	 * 查询告警事件总记录数
	 * deviceName  设备名称
	 * doorName    门名称
	 * cardNum     卡名称
    * startDate  从什么时间开始
    * endDate    到什么时间结束
	 * **/
    public Integer searchEventWarningInfoCount(String deviceName,String doorName,String cardNum,String startDate,String endDate);
    public Integer searchEventWarningInfoCount2(String deviceName,String doorName,String cardNum,String startDate,String endDate);

    
    /*
	 * 将告警事件变为已处理
	 * eventId  事件ID

	 * **/
    
    public boolean dealEventWarning(String eventId);
    
    public List<Map<String, Object>> getAllEvents();
    
    public List<Map<String, Object>> getAllEventWarnings();
    
    
    public boolean deleteEvent(Map<String, Object> map);
    
    
    public boolean deleteEventWarning(Map<String, Object> map);
    
    public List<String> getEventColumns();
   
   
    public List<String> getEventWarningColumns();
       
    /**
     * 导出读头事件
     * @param deviceName
     * @param doorName
     * @param cardNum
     * @param startDate
     * @param endDate
     * @return
     */
    public List<DeviceEventModel> exportEventInfo(String deviceName,String doorName,String cardNum,String startDate,String endDate);
    /**
     * 导出告警事件
     * @param deviceName
     * @param doorName
     * @param cardNum
     * @param startDate
     * @param endDate
     * @return
     */
    public List<DeviceEventWarningModel> exportWarningEventInfo(String deviceName,String doorName,String cardNum,String startDate,String endDate);
    
    /**
     * @创建人　: 高育漫
     * @创建时间: 2016-6-29 下午8:02:36
     * @功能描述: 更新读头事件
     * @参数描述:
     */
    public boolean updateEventInfo(String eventType,String deviceNum,String doorNum,String cardNum,String eventDm,String eventDescription,String eventDate);
   
    /**
     * @创建人　: 高育漫
     * @创建时间: 2016-6-29 下午8:03:18
     * @功能描述: 更新告警事件
     * @参数描述:
     */
    public boolean updateEventWarningInfo(String eventType,String deviceNum,String doorNum,String cardNum,String eventDm,String eventDescription,String eventDate);
    
    /**
     * 显示最新5条读头事件
     * @author minting.he
     * @return
     */
    public List<DeviceEventModel> latestEventInfo();
    
    /**
     * 按时间区间备份读头事件
     * @author minting.he
     * @param map
     * @return
     */
    public List<DeviceEventModel> getEventInterval(Map<String, Object> map);
    
    /**
     * 删除备份的读头事件
     * @author minting.he
     * @param map
     * @return
     */
    public boolean delEventInterval(Map<String, Object> map);
    
    /**
     * 按时间区间备份告警事件
     * @author minting.he
     * @param map
     * @return
     */
    public List<DeviceEventWarningModel> getWarningEventInterval(Map<String, Object> map);
    
    /**
     * 删除备份的告警事件
     * @author minting.he
     * @param map
     * @return
     */
    public boolean delWarningEventInterval(Map<String, Object> map);
    
    /**
     * 根据设备号查设备名称
     * @author minting.he
     * @param deviceNum
     * @return
     */
    public String getDeviceNameByNum(String deviceNum);
    
    /**
     * 根据卡号查员工姓名
     * @author minting.he
     * @param card_num
     * @return
     */
    public Map<String, Object> getStaffNameByCard(String card_num);
    
    /**
     * 查门名称
     * @author minting.he
     * @param map
     * @return
     */
    public String getDoorNameByNum(Map<String, Object> map);
    
    /**
     * 查事件名
     * @author minting.he
     * @param event_dm
     * @return
     */
    public String getEventNameByDm(String event_dm);
    
    /**
     * 定时插入读头事件
     * @author minting.he
     * @param list
     * @return
     */
    public boolean insertEvent(List<Map<String, Object>> list);
    
    /**
     * 告警事件是否已存在
     * @author minting.he
     * @param map
     * @return
     */
    public Integer ifExistWarning(Map<String, Object> map);
    
    /**
     * 定时告警事件更新
     * @author minting.he
     * @param map
     * @return
     */
    public boolean updateWarningInfo(Map<String, Object> map);
    
    /**
     * 定时插入告警事件
     * @author minting.he
     * @param map
     * @return
     */
    public boolean insertWarningEvent(Map<String, Object> map);
    
    /**
     * 获取当前未处理的告警事件
     * @author minting.he
     * @return
     */
    public List<Map<String, Object>> getNowWarning();
    
    /**
     * 定时处理告警事件
     * @author minting.he
     * @param map
     * @return
     */
    public boolean handleWarningEvent(Map<String, Object> map);
    
    /**
     * 查询访客姓名
     * @author minting.he
     * @param card_num
     * @return
     */
    public Map<String, Object> getVisitorName(String card_num);
    
    /**
     * 获取员工头像
     * @author minting.he
     * @param card_num
     * @return
     */
    public String getStaffImg(String card_num);
    
    /**
     * 查询卡绑定的员工代码
     * @author minting.he
     * @param card_num
     * @return
     */
    public String getStaffNumByCardNum(String card_num);
    

	/**
	 * 查询门是否在被监控
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public Integer ifMonitorDoor(Map<String, Object> map);
	
	/**
	 * 查询一周内各刷卡事件类型数量
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public List<Map> getEventByDate(Map map);
	
	/**
	 * 查询某天的告警次数 
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public Integer getWarningEventByDate(Map map);
	
	/**
	 * 查询当前设备数，卡数，人员数
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public Map getCourrentDeviceCardEmpCount();
   
}
