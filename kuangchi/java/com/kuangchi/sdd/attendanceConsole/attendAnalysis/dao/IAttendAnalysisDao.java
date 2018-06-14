package com.kuangchi.sdd.attendanceConsole.attendAnalysis.dao;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDataDate;

public interface IAttendAnalysisDao {
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-13 下午3:51:17
	 * @功能描述: 查询员工每月考勤数据
	 * @参数描述:
	 */
	List<AttendanceDataDate> getAttendanceData(Map<String, Object> map);
 	
}
