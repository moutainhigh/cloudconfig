package com.kuangchi.sdd.attendanceConsole.attendCount.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attendCount.dao.AttendMonthDao;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendMonthModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.service.AttendMonthService;
import com.kuangchi.sdd.base.model.easyui.Grid;

@Service("attendMonthService")
public class AttendMonthServiceImpl implements AttendMonthService {

	@Resource(name="attendMonthDao")
	private AttendMonthDao attendMonthDao;

	@Override
	public Grid<AttendMonthModel> getAllAttendMonth(Map<String, Object> map) {
		String month = (String) map.get("month");
		if(null != month){
			month=month.substring(0, 7);
			map.put("month", month);
		}
		List<AttendMonthModel> monthCountList = attendMonthDao.getAllAttendMonth(map);
		// 设置工作时间和加班时间等
		for (AttendMonthModel attendMonthModel : monthCountList) {
			BigDecimal workTimeNormal = attendMonthModel.getWorkTimeNormal();
			BigDecimal overWorkTimeNormal = attendMonthModel.getOverWorkTimeNormal();
			BigDecimal overWorkTimeHoliday = attendMonthModel.getOverWorkTimeHoliday();
			BigDecimal overWorkTimeWeekend = attendMonthModel.getOverWorkTimeWeekend();
			
			BigDecimal normalWorkAll = workTimeNormal.add(overWorkTimeNormal);
//					attendMonthModel.setWorkTimeNormal(normalWorkAll);
			
			BigDecimal workTimeAll = (normalWorkAll.add(overWorkTimeHoliday)).add(overWorkTimeWeekend);
			attendMonthModel.setWorkTimeAll(workTimeAll);
			
			// 设置加班天数
			BigDecimal workDay = attendMonthModel.getWorkDay();
			BigDecimal workDayNormal = attendMonthModel.getWorkDayNormal();		
			BigDecimal overDay = workDay.subtract(workDayNormal);
			attendMonthModel.setOverDay(overDay);
		}
		
		
		Integer total=attendMonthDao.countAllAttendMonth(map);
		Grid<AttendMonthModel> grid=new Grid<AttendMonthModel>();
		grid.setRows(monthCountList);
		grid.setTotal(total);
		
		return grid;
	}

	@Override
	public List<AttendMonthModel> exportAllToExcel(Map<String, Object> map) {
		String month = (String) map.get("month");
		if(null!=month){
			month=month.substring(0, 7);
			map.put("month", month);
		}
		return attendMonthDao.exportAllMonthToExcel(map);
	}


	


}
