package com.kuangchi.sdd.attendanceConsole.attendAnalysis.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attendAnalysis.dao.IAttendAnalysisDao;
import com.kuangchi.sdd.attendanceConsole.attendAnalysis.model.AttendanceRateModel;
import com.kuangchi.sdd.attendanceConsole.attendAnalysis.service.IAttendAnalysisService;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDataDate;

@Service("attendAnalysisService")
public class AttendAnalysisServiceImpl implements IAttendAnalysisService {
	
	@Resource(name="attendAnalysisDao")	
	private IAttendAnalysisDao attendAnalysisDao;

	//查询员工考勤比率
	public AttendanceRateModel getAttendanceRate(
			Map<String, Object> map) {
		
		Double all_work_time = 0.0;
		Double work_time = 0.0;
		Double over_time = 0.0;
		Double later_time = 0.0;
		Double early_time = 0.0;
		Double kg_time = 0.0;
		Double leave_time = 0.0;
		
		List<AttendanceDataDate> attendanceDatas = attendAnalysisDao.getAttendanceData(map);
		for (AttendanceDataDate attendanceData : attendanceDatas) {
			all_work_time += attendanceData.getShould_work_time() + attendanceData.getOver_time();
			work_time += attendanceData.getWork_time() + attendanceData.getOut_total_time();
			over_time += attendanceData.getOver_time();
			later_time += attendanceData.getLater_time();
			early_time += attendanceData.getEarly_time();
			kg_time += attendanceData.getKg_time();
			leave_time += attendanceData.getLeave_total_time();
		}
		
		AttendanceRateModel rate = new AttendanceRateModel();
		if(all_work_time == 0){
			rate.setTotal_rate("0");
			return rate;
		}
		rate.setWork_rate(Double.toString(work_time/all_work_time));
		rate.setOver_rate(Double.toString(over_time/all_work_time));
		rate.setLater_rate(Double.toString(later_time/all_work_time));
		rate.setEarly_rate(Double.toString(early_time/all_work_time));
		rate.setKg_rate(Double.toString(kg_time/all_work_time));
		rate.setLeave_rate(Double.toString(leave_time/all_work_time));
		
		return rate;
	}


}
