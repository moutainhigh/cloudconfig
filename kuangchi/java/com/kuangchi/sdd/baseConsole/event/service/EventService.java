package com.kuangchi.sdd.baseConsole.event.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventModel;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventWarningModel;
import com.kuangchi.sdd.baseConsole.event.model.SearchEventModel;
import com.kuangchi.sdd.baseConsole.log.model.Log;

public interface EventService {
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
	public void addEventInfo(String eventType,String deviceNum,String doorNum,String cardNum,String eventDm,String eventDescription,String eventDate, String login_User);
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
    public void addEventWarningInfo(String eventType,String deviceNum,String doorNum,String cardNum,String eventDm,String eventDescription,String eventDate, String login_User);
	/*
	 * 删除设备事件
	 * eventIds 事件id   格式 : '1','2','3'
	 * **/
    public void deleteEventInfo(String eventIds, String login_User);
	/*
	 * 删除告警事件
	 * eventIds 事件id   格式 : '1','2','3'
	 * **/
    public void deleteEventWarningInfo(String eventIds, String login_User);   
	/*
	 * 查询设备事件
	 * deviceName  设备名称
	 * doorName    门名称
	 * cardNum     卡名称
	 * startDate  从什么时间开始
	 * endDate    到什么时间结束
     * skip  从第几行开始
	 * rows  往后获取多少行
	 * **/
    public Grid<DeviceEventModel> searchEventInfo(SearchEventModel model, Integer skip, Integer rows);

	/*
	 * 查询告警事件
	 * deviceName  设备名称
	 * doorName    门名称
	 * cardNum     卡名称
	 * startDate  从什么时间开始
	 * endDate    到什么时间结束
	 * skip  从第几行开始
	 * rows  往后获取多少行
	 * **/
    public Grid<DeviceEventWarningModel> searchEventWarningInfo(String deviceName,
			String doorName, String cardNum, String startDate,String endDate,Integer skip, Integer rows);
    public Grid<DeviceEventWarningModel> searchEventWarningInfo2(String deviceName,
			String doorName, String cardNum, String startDate,String endDate,Integer skip, Integer rows);
    /*
	 * 将告警事件变为已处理
	 * eventId  事件ID
	 * **/
    
    public boolean dealEventWarning(String eventId);
  
    /*
     *备份事件
     * 
     * 
     * **/
    public void backupEvent(String absolutePath);
    
    /*
     * 备份告警事件
     * 
     * ***/
    
    public void backupEventWarning(String absolutePath);
    
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
    public List<DeviceEventWarningModel> exportEventWarningInfo(String deviceName,String doorName,String cardNum,String startDate,String endDate);
    
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
 	public List<DeviceEventModel> getEventInterval(Map map);
 	
 	/**
 	 * 删除时间区间备份的读头事件
 	 * @author minting.he
 	 * @param map
 	 * @return
 	 */
 	public boolean delEventInterval(Map map, String login_user);
 	
 	/**
 	 * 按时间区间备份告警事件
 	 * @author minting.he
 	 * @param map
 	 * @return
 	 */
 	public List<DeviceEventWarningModel> getWarningEventInterval(Map map);
 	
 	/**
 	 * 删除时间区间备份的告警事件
 	 * @author minting.he
 	 * @param map
 	 * @return
 	 */
 	public boolean delWarningEventInterval(Map map, String login_user);
 	
 	/**
 	 * 获取员工头像
 	 * @author minting.he
 	 * @param card_num
 	 * @return
 	 */
 	public String getStaffImg(String card_num);
 	
 	
 	/**
	 * 查询一周内各刷卡事件类型数量
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public List<Map> getEventByDate();
 	
	/**
	 * 查询某天的告警次数 
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public Map getWarningEventByDate();
	
	/**
	 * 查询当前设备数，卡数，人员数
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public Map getCourrentDeviceCardEmpCount();
    
    
}
