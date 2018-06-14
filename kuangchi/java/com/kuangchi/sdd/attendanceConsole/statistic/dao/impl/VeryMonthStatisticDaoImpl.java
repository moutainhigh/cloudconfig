package com.kuangchi.sdd.attendanceConsole.statistic.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendMonthModel;
import com.kuangchi.sdd.attendanceConsole.statistic.dao.VeryMonthStatisticDao;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("veryMonthStatisticDao")
public class VeryMonthStatisticDaoImpl extends BaseDaoImpl<Object> implements VeryMonthStatisticDao {

	@Override
	public BigDecimal veryMonthWorkDays(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthWorkDays", map);
	}

	@Override
	public BigDecimal veryMonthActualWorkDays(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthActualWorkDays", map);
	}

	@Override
	public BigDecimal veryMonthNormalWorkTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthNormalWorkTimes", map);
	}

	@Override
	public BigDecimal veryMonthNormalOverTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthNormalOverTimes", map);
	}

	@Override
	public BigDecimal veryMonthWeekendOverTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthWeekendOverTimes", map);
	}

	@Override
	public BigDecimal veryMonthHolidayOverTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthHolidayOverTimes", map);
	}

	@Override
	public BigDecimal veryMonthRepeatTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthRepeatTimes", map);
	}

	@Override
	public BigDecimal veryMonthLeaveTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthLeaveTimes", map);
	}

	@Override
	public Integer veryMonthKgTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (Integer) getSqlMapClientTemplate().queryForObject("veryMonthKgTimes", map);
	}

	@Override
	public Integer veryMonthZtcdTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (Integer) getSqlMapClientTemplate().queryForObject("veryMonthZtcdTimes", map);
	}

	@Override
	public Integer veryMonthCardNotTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (Integer) getSqlMapClientTemplate().queryForObject("veryMonthCardNotTimes", map);
	}

	@Override
	public Integer veryMonthCardStatusTimes(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (Integer) getSqlMapClientTemplate().queryForObject("veryMonthCardStatusTimes", map);
	}

	@Override
	public String getNameSpace() {
		return "attendanceConsole.VeryMonthStatistic";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public BigDecimal veryMonthOutTotalTime(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthOutTotalTime", map);
	}

	@Override
	public List<Map> getBaseInfo(String staffNum) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		return getSqlMapClientTemplate().queryForList("getBaseInfo", map);
	}

	@Override
	public void insertAttendMonth(AttendMonthModel attendMonthModel) {
		getSqlMapClientTemplate().insert("insertAttendMonth", attendMonthModel);
	}

	@Override
	public void delOldAttendMonthModel(String staffNum, String month,String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		getSqlMapClientTemplate().delete("delOldAttendMonthModel", map);
	}

	@Override
	public List<String> getAllStaff() {
		return getSqlMapClientTemplate().queryForList("getAllStaff");
	}

	@Override
	public BigDecimal veryMonthNormalWorkDays(String staffNum, String month, String dept_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("month", month);
		map.put("dept_num", dept_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("veryMonthNormalWorkDays", map);
	}
	
}
