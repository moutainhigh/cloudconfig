package com.kuangchi.sdd.attendanceConsole.attendAnalysis.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendAnalysis.model.AttendanceRateModel;



public interface IAttendAnalysisService {
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-13 下午3:51:17
	 * @功能描述: 查询员工考勤率
	 * @参数描述:
	 */
	AttendanceRateModel getAttendanceRate(Map<String, Object> map);
 	
	
}
