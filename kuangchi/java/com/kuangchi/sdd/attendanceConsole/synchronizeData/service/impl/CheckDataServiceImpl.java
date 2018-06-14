package com.kuangchi.sdd.attendanceConsole.synchronizeData.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.dao.CheckDataDao;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerBrushCardLog;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.CheckDataService;

@Service("checkDataService")
public class CheckDataServiceImpl implements CheckDataService {
   @Resource(name="checkDataDao")
   CheckDataDao checkDataDao;

@Override
public  List<SqlServerBrushCardLog> getBrushCardLog(String startDate, String endDate,String employeeId) {
	return checkDataDao.getBrushCardLog(startDate, endDate,employeeId);
	
}
	

}
