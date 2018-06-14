package com.kuangchi.sdd.attendanceConsole.statistic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDeptModel;
import com.kuangchi.sdd.attendanceConsole.statistic.dao.VeryDeptMonthStatisticDao;
import com.kuangchi.sdd.attendanceConsole.statistic.service.VeryDeptMonthStatisticService;
@Service("veryDeptMonthStatisticService")
public class VeryDeptMonthStatisticServiceImpl implements
		VeryDeptMonthStatisticService {
	
	@Autowired
	private VeryDeptMonthStatisticDao veryDeptMonthStatisticDao;

	@Override
	public void updateDeptMonthStatistic(String deptNum, String month) {
		String everyMonth="";
			if(null!=month){
				everyMonth=month.substring(0, 7);
			}
		AttendDeptModel attendDeptModel=veryDeptMonthStatisticDao.calculateDeptAttendInfo(deptNum, everyMonth);
		veryDeptMonthStatisticDao.delOldAttendDeptInfo(deptNum, everyMonth);
		veryDeptMonthStatisticDao.insertAttendDept(attendDeptModel);
	}

	@Override
	public List<String> getAllDept() {
		return veryDeptMonthStatisticDao.getAllDept();
	}

}
