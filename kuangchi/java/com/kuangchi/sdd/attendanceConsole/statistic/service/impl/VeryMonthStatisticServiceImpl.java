package com.kuangchi.sdd.attendanceConsole.statistic.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendMonthModel;
import com.kuangchi.sdd.attendanceConsole.statistic.dao.VeryMonthStatisticDao;
import com.kuangchi.sdd.attendanceConsole.statistic.service.VeryMonthStatisticService;
@Service("veryMonthStatisticService")
public class VeryMonthStatisticServiceImpl implements VeryMonthStatisticService {
	
	@Autowired
	private VeryMonthStatisticDao veryMonthStatisticDao;
	
	@Override
	public void updateVeryMonthStatistic(String staffNum, String yearMonthDay,String dept_num) {
			String everyMonth="";
			if(null!=yearMonthDay){
				everyMonth=yearMonthDay.substring(0, 7);
			}
			BigDecimal actualWorksDays=veryMonthStatisticDao.veryMonthActualWorkDays(staffNum, everyMonth,dept_num);
			Integer cardNot=veryMonthStatisticDao.veryMonthCardNotTimes(staffNum, everyMonth,dept_num);
			Integer cardStatus=veryMonthStatisticDao.veryMonthCardStatusTimes(staffNum, everyMonth,dept_num);
			BigDecimal holidayOverTimes=veryMonthStatisticDao.veryMonthHolidayOverTimes(staffNum, everyMonth,dept_num);
			
			Integer kgTimes=veryMonthStatisticDao.veryMonthKgTimes(staffNum, everyMonth,dept_num);
			BigDecimal leaveTimes=veryMonthStatisticDao.veryMonthLeaveTimes(staffNum, everyMonth,dept_num);
			BigDecimal normalOverTimes=veryMonthStatisticDao.veryMonthNormalOverTimes(staffNum, everyMonth,dept_num);
			BigDecimal normalWorkTimes=veryMonthStatisticDao.veryMonthNormalWorkTimes(staffNum, everyMonth,dept_num);
			BigDecimal repeatTimes=veryMonthStatisticDao.veryMonthRepeatTimes(staffNum, everyMonth,dept_num);
			BigDecimal normalWorkDays=veryMonthStatisticDao.veryMonthNormalWorkDays(staffNum, everyMonth,dept_num);
			
			BigDecimal weekendOverTimes=veryMonthStatisticDao.veryMonthWeekendOverTimes(staffNum, everyMonth,dept_num);
			BigDecimal workDays=veryMonthStatisticDao.veryMonthWorkDays(staffNum, everyMonth,dept_num);
			Integer ztcdTimes=veryMonthStatisticDao.veryMonthZtcdTimes(staffNum, everyMonth,dept_num);
			BigDecimal outWorkTotalTime=veryMonthStatisticDao.veryMonthOutTotalTime(staffNum, everyMonth,dept_num);
			BigDecimal workTimeAll=normalWorkTimes.add(holidayOverTimes).add(normalOverTimes).add(weekendOverTimes).subtract(repeatTimes);
			BigDecimal workTimeAvg;
			if(actualWorksDays.doubleValue()!=0){
				workTimeAvg=workTimeAll.divide(actualWorksDays,2);
			}else{
				workTimeAvg=new BigDecimal(0);
			}

			List<Map> maps=veryMonthStatisticDao.getBaseInfo(staffNum);
			if(maps!=null&&maps.size()!=0){
				AttendMonthModel attendMonthModel=new AttendMonthModel();
				
				attendMonthModel.setWorkDay(actualWorksDays);
				attendMonthModel.setWorkTimeNormal(normalWorkTimes);
				attendMonthModel.setOverWorkTimeNormal(normalOverTimes);
				attendMonthModel.setOverWorkTimeWeekend(weekendOverTimes);
				attendMonthModel.setOverWorkTimeHoliday(holidayOverTimes);
				attendMonthModel.setWorkDayNormal(normalWorkDays);
				
				attendMonthModel.setWorkTimeAll(outWorkTotalTime);
				attendMonthModel.setLeaveTime(leaveTimes);
				attendMonthModel.setCardNot(cardNot);
				attendMonthModel.setCardStatus(cardStatus);
				attendMonthModel.setEveryMonth(everyMonth);
				
				attendMonthModel.setInZtcd(ztcdTimes);
				attendMonthModel.setInKg(kgTimes);
				attendMonthModel.setStaffNum(staffNum);
				attendMonthModel.setWorkDaysTotal(workDays);
				attendMonthModel.setWorkTimeAll(workTimeAll);
				
				attendMonthModel.setWorkTimeAvg(workTimeAvg);
				attendMonthModel.setSex((String) maps.get(0).get("sex"));
				attendMonthModel.setStaffName((String) maps.get(0).get("staffName"));
				attendMonthModel.setOutTotalTime(outWorkTotalTime);
				attendMonthModel.setDeptNum(dept_num);
				
				veryMonthStatisticDao.delOldAttendMonthModel(staffNum, everyMonth,dept_num);
				veryMonthStatisticDao.insertAttendMonth(attendMonthModel);
			}
			
			
		
		
	}

	@Override
	public List<String> getAllStaff() {
		return veryMonthStatisticDao.getAllStaff();
	}
	
	


}
