package com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.dao.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.dao.HolidaySetUpDao;
import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.model.HolidaySetUp;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;


@Repository("holidaySetUpDaoImpl")
public class HolidaySetUpDaoImpl extends BaseDaoImpl<HolidaySetUp> implements HolidaySetUpDao{

	@Override
	public String getNameSpace() {
		return "common.holidaySetUp";
	}

	@Override
	public String getTableName() {
		return null;
	}
	@Override
	public List<HolidaySetUp> getHolidayTimesOfDevice(Map map) {
		return getSqlMapClientTemplate().queryForList("getHolidayTimesOfDevice", map);
	}

	@Override
	public Integer getHolidayTimesOfDeviceCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getHolidayTimesOfDeviceCount", map);
	}
	@Override
	public List<HolidaySetUp> getByHolidayNum(String device_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("holiday_times_num",holiday_times_num);
		map.put("device_num",device_num);
		return this.getSqlMapClientTemplate().queryForList("selectByHolidayNum",map);
	}
	@Override
	public Integer countHolidayTimes(HolidaySetUp holidaySetUp, String exist_nums) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start_date",holidaySetUp.getBegin_time());
		param.put("end_date",holidaySetUp.getEnd_time());
		param.put("exist_nums", exist_nums);
		return queryCount("holidayTimesCount",param);
	}
	@Override
	public boolean updateHolidayTimes(HolidaySetUp holidaySetUp) {
		return update("updateHolidayTimesOfDevice",holidaySetUp);
	}
	@Override
	public boolean delHolidayTimes(String device_num) {
		return delete("delHolidayTimesOfDevice",device_num);
	}
	@Override
	public boolean getMaxHolidayTimesNum(HolidaySetUp holidaySetUp) {
		int maxNum = 0;
		if(find("getMaxHolidayTimesNum", holidaySetUp)!=null){
			maxNum = (Integer) find("getMaxHolidayTimesNum", holidaySetUp);
			if(maxNum == 127){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean addHolidayTimes(HolidaySetUp holidaySetUp) {
		int maxNum = 0;
		if(find("getMaxHolidayTimesNum", holidaySetUp)!=null){
			maxNum = (Integer) find("getMaxHolidayTimesNum", holidaySetUp);
			if(maxNum == 127){
				return false;
			}
			holidaySetUp.setHoliday_times_num(String.valueOf(maxNum+1));
		}else{
			holidaySetUp.setHoliday_times_num(String.valueOf(maxNum));
		} 
		
		return insert("addHolidayTimesOfDevice", holidaySetUp);
	}

	@Override
	public Map getDeviceMacAndType(String device_num) {
		return (Map) getSqlMapClientTemplate().queryForObject("getDeviceMacAndType", device_num);
	}

	@Override
	public Integer getHolidayDateOfDevice(HolidaySetUp holidaySetUp) {
		return (Integer) find("getHolidayDateOfDevice", holidaySetUp);
	}

	@Override
	public boolean delHolidayById(String ids){
		return delete("delHolidayById", ids);
	}
	
}
