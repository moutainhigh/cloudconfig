package com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.dao;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.model.TimesGroup;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;



/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-4-6 15:15:26
 * @功能描述: 时段组-Dao类
 */
public interface TimesGroupDao {

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 根据参数查询时段组
	 */
	List<TimesGroup> getTimesGroupByParam(Map<String, Object> map);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14 下午15:32:05
	 * @功能描述: 根据参数查询时段组总数
	 */
	Integer getTimesGroupCount(Map<String, Object> map);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 根据编号查询时段组（查看详情）
	 */
	List<TimesGroup> getDeviceTimesGroupByNum(Map<String, Object> map);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 设置时段组
	 */
	boolean modifyTimesGroup(TimesGroup timesGroup);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7下午15:32:05
	 * @功能描述: 查找所有时段，并按开始时间排序
	 * @return
	 */
	List<DeviceTimes> getAllTimesSortByBeginTime(Map<String, Object> map);
	
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
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-12-26 下午7:13:39
	 * @功能描述: 根据设备编号查找时段组
	 * @参数描述:
	 */
	List<TimesGroup> getTimesGroupByDevice1(String device_num);

	List<TimesGroup> getDeviceTimesGroupByParam1(Map map);

	List<DeviceTimes> getTimesByParamPageSortByBeginTime1(DeviceTimes times);
}
