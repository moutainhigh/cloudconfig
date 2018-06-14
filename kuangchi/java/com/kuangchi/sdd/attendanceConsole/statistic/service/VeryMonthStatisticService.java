package com.kuangchi.sdd.attendanceConsole.statistic.service;

import java.util.List;

public interface VeryMonthStatisticService {
	/**
	 * 员工每月统计，传入   员工编号 以及日期 如“2016-05-06”
	 * 则可统计 该员工 该月份 的 考勤信息
	 * @param staffNum
	 * @param yearMonthDay
	 */
	public void updateVeryMonthStatistic(String staffNum,String yearMonthDay,String dept_num);
	/**
	 * 获取所有的员工工号 staffNum
	 * 	 by gengji.yang
	 * @return
	 */
	public List<String> getAllStaff();
	
}
