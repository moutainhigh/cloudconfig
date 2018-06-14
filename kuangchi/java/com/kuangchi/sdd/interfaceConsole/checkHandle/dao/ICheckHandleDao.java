package com.kuangchi.sdd.interfaceConsole.checkHandle.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.event.model.DeviceEventModel;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventWarningModel;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-5-19 下午2:21:44
 * @功能描述: 对外接口-dao类
 */
public interface ICheckHandleDao {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-18 上午10:32:18
	 * @功能描述: 增加打卡信息
	 * @参数描述:
	 */
	public boolean addCheckInfo(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-29 下午7:51:35
	 * @功能描述: 根据参数查询事件
	 * @参数描述:
	 */
	public List<DeviceEventModel> getEventInfo(String deviceNum, String doorNum, String cardNum, String eventDm);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-29 下午7:55:17
	 * @功能描述: 根据参数查询告警事件
	 * @参数描述:
	 */
	public List<DeviceEventWarningModel> getEventWarnInfo(String deviceNum, String doorNum, String cardNum, String eventDm);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-5 下午1:56:05
	 * @功能描述: 增加出入统计记录
	 * @参数描述:
	 */
	public boolean addAttendLog(Map<String, Object> map);
	
	/**
	 * 增加出入报警记录
	 * @author yuman.gao
	 */
	public boolean addWarningLog(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-13 上午9:44:07
	 * @功能描述: 处理告警事件
	 * @参数描述:
	 */
	public boolean handleEventWarning(String device_mac, String doorNum, String event_dms);
	
	/**
	 * 新增打卡信息（考勤）
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean insertCheckInfo(List<Map<String, Object>> list);
	
	/**
	 * 新增出入统计
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean insertAttendLog(List<Map<String, Object>> list);
	
	/**
	 * 新增告警日志
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean insertWarningLog(Map map);
	
	/**
	 * 是否存在该员工
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public String getAttendRecordModel(Map map);
}
