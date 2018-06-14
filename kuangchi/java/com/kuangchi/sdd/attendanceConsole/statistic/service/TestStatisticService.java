
package com.kuangchi.sdd.attendanceConsole.statistic.service;

import java.util.List;
/**
 * 测试 每日考勤用的接口
 * @author gengji.yang
 *
 */
public interface TestStatisticService {
	public void insertRecord(String staffNum,String  staffName,List checkTimes);
}
