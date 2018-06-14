package com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.dao.TimesGroupDao;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.model.TimesGroup;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;


/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-4-6 15:15:26
 * @功能描述: 时段组-Dao实现类
 */

@Repository("deviceTimesGroupDao")
public class TimesGroupDaoImp extends BaseDaoImpl<TimesGroup> implements
		TimesGroupDao {

	public String getNameSpace() {
		return "common.DeviceTimesGroup";
	}

	public String getTableName() {
		return null;
	}

	@Override
	public List<TimesGroup> getTimesGroupByParam(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceTimesGroupByParam", map);
	}
	
	@Override
	public Integer getTimesGroupCount(Map<String, Object> map) {
		return (Integer) this.find("getDeviceTimesGroupCount",map);
	}

	@Override
	public List<TimesGroup> getDeviceTimesGroupByNum(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceTimesGroupByNum", map);
	}
	
	@Override
	public boolean modifyTimesGroup(TimesGroup timesGroup) {
		return update("modifyDeviceTimesGroup", timesGroup);
	}
	
	@Override
	public List<DeviceTimes> getAllTimesSortByBeginTime(Map<String, Object> map) {		
		return this.getSqlMapClientTemplate().queryForList("getAllTimesSortByBeginTime", map);
	}

	@Override
	public Integer getTimesGroupByName(Map<String, Object> map) {
		return (Integer) this.find("getDeviceTimesGroupByName",map);
	}
	
	@Override
	public Map<String, Object> getDeviceInfoByNum(String device_num) {
		return (Map) this.getSqlMapClientTemplate().queryForObject("getDeviceByNum",device_num);
	}

	@Override
	public List<TimesGroup> getTimesGroupByDevice1(String device_num) {
		return getSqlMapClientTemplate().queryForList("getTimesGroupByDevice1", device_num);
	}

	@Override

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组(不分组)
	 */
	public List<TimesGroup> getDeviceTimesGroupByParam1(Map map) {
		return this.getSqlMapClientTemplate().queryForList(
				"getDeviceTimesGroupByParam1", map);
	}

	@Override
	public List<DeviceTimes> getTimesByParamPageSortByBeginTime1(DeviceTimes times) {
		return this.getSqlMapClientTemplate().queryForList("getTimesByParamPageSortByBeginTime1",times);
	}

	
}
