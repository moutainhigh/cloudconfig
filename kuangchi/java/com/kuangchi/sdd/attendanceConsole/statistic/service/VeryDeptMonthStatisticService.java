package com.kuangchi.sdd.attendanceConsole.statistic.service;

import java.util.List;

public interface VeryDeptMonthStatisticService {
	/**
	 * 部门每月统计 
	 * @param deptNum
	 * @param month '2016-05-06'
	 */
	public void updateDeptMonthStatistic(String deptNum,String month);
	public List<String> getAllDept();
		
}
