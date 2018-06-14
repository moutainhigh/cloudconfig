package com.kuangchi.sdd.attendanceConsole.statistic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.statistic.dao.TestStatisticDao;
import com.kuangchi.sdd.attendanceConsole.statistic.service.TestStatisticService;
/**
 * 测试每日考勤用的接口实现类
 * @author gengji.yang
 *
 */
@Service("testStatisticService")
public class TestStatisticServiceImpl implements TestStatisticService{
	
	@Autowired
	private TestStatisticDao testStatisticDao;
	
	@Override
	public void insertRecord(String staffNum, String staffName, List checkTimes) {
		for(int i=0;i<checkTimes.size();i++){
			testStatisticDao.insertAttendRecord(staffNum, staffName, (String)checkTimes.get(i));
		}
	}

}
