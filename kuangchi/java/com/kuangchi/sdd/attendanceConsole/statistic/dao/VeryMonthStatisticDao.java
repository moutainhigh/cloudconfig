package com.kuangchi.sdd.attendanceConsole.statistic.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendMonthModel;

public interface VeryMonthStatisticDao {
	
	
	public BigDecimal veryMonthWorkDays(String staffNum,String month,String dept_num);
	public BigDecimal veryMonthActualWorkDays(String staffNum,String month,String dept_num);
	public BigDecimal veryMonthNormalWorkTimes(String staffNum,String month,String dept_num);
	
	public BigDecimal veryMonthNormalOverTimes(String staffNum,String month,String dept_num);
	public BigDecimal veryMonthWeekendOverTimes(String staffNum,String month,String dept_num);
	public BigDecimal veryMonthHolidayOverTimes(String staffNum,String month,String dept_num);
	
	public BigDecimal veryMonthRepeatTimes(String staffNum,String month,String dept_num);
	public BigDecimal veryMonthLeaveTimes(String staffNum,String month,String dept_num);
	public BigDecimal veryMonthOutTotalTime(String staffNum,String month,String dept_num);
	public Integer veryMonthKgTimes(String staffNum,String month,String dept_num);
	
	public Integer veryMonthZtcdTimes(String staffNum,String month,String dept_num);
	public Integer veryMonthCardNotTimes(String staffNum,String month,String dept_num);
	public Integer veryMonthCardStatusTimes(String staffNum,String month,String dept_num);
	public List<Map> getBaseInfo(String staffNum);
	public void insertAttendMonth(AttendMonthModel attendMonthModel);
	public void delOldAttendMonthModel(String staffNum,String month,String dept_num);
	public List<String> getAllStaff();
	
	public BigDecimal veryMonthNormalWorkDays(String staffNum,String month,String dept_num);
	
}
