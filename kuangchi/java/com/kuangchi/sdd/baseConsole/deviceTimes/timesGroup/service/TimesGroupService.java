package com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.service;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.model.TimesGroup;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.sun.mail.iap.ConnectionException;



/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-4-6 15:15:26
 * @功能描述: 时段组-业务类
 */

public interface TimesGroupService {

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组
	 * @return
	 */
	List<TimesGroup> getTimesGroupByParam(Map<String, Object> map);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14 下午15:32:05
	 * @功能描述: 查询时段组总数
	 */
	Integer getTimesGroupCount(Map<String, Object> map);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 根据编号查询时段组（只包含时段编号）
	 */
	List<TimesGroup> getDeviceTimesGroupByNum(Map<String, Object> map);
	
	/**
	 * 根据编号查询时段组（包括时段详细信息）
	 * @author yuman.gao
	 */
	List<TimesGroup> getTimesGroupDetailByNum(Map<String, Object> map);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 设置时段组
	 * @param timesGroup
	 * @return
	 */
	boolean modifyTimesGroup(List<TimesGroup> timesGroupList,String loginUser);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7下午15:32:05
	 * @功能描述: 查找所有时段，并按开始时间排序
	 * @return
	 */
	List<DeviceTimes> getAllTimesSortByBeginTime(Map<String, Object> map);
	
	/**
	 * 下发时段组
	 * @author yuman.gao
	 * @throws ConnectionException 
	 * @throws Exception 
	 */
	boolean issuedTimesGroup(String device_num, String device_mac, String device_type) throws ConnectionException, Exception;
	
	/**
	 * 读取已下发时段组信息
	 * @author yuman.gao
	 * @throws ConnectionException 
	 * @throws Exception 
	 */
	List<TimesGroup> getUserGroup(String device_num, String device_mac, String group_num) throws ConnectionException, Exception;
	
	/**
	 * 检查设备是否在线
	 * @author yuman.gao
	 * @throws ConnectionException 
	 */
	boolean isConnected(String device_mac) throws ConnectionException;
	
	/**
	 * 根据设备查询组名称是否存在
	 * @author yuman.gao
	 */
	Integer getTimesGroupByName(Map<String, Object> map);
	
	/**
	 * 根据设备编号查询设备
	 * @author yuman.gao
	 */
	Map<String, Object> getDeviceInfoByNum(String device_num);
	
	/**
	 * 复制时段组
	 * @author yuman.gao
	 */
	boolean copyTimesGroup(String source_num, String target_num, String loginUser);
	
	/**
	 * 复制并下发时段组
	 * @author yuman.gao
	 * @throws Exception 
	 * @throws ConnectionException 
	 */
	boolean copyIssuedTimesGroup(String source_num, String target_num, String target_mac, String target_type, String loginUser) throws ConnectionException, Exception;

	/**
	 * 复制时段
	 * @author yuman.gao
	 * @throws Exception 
	 */
	boolean copyTimes(String source_num, String target_num,String loginUser);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-12-26 下午7:15:58
	 * @功能描述: 根据设备编号查找用户时段组
	 * @参数描述:
	 */
	List<TimesGroup> getTimesGroupByDevice1(String device_num);

	List<TimesGroup> getTimesGroupsByParam1(String device_num, String group_name);
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-4-7下午15:32:05
	 * @功能描述: 查找所有时段，并按开始时间排序
	 * @return
	 */
	List<DeviceTimes> getTimesByParamPageSortByBeginTime1(DeviceTimes times);
	
}
