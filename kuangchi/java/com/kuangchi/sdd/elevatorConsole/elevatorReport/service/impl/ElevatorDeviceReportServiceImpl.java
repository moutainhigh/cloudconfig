package com.kuangchi.sdd.elevatorConsole.elevatorReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.ElevatorDeviceReportDao;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorDeviceInfo;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.service.ElevatorDeviceReportService;

@Transactional
@Service("elevatorDeviceReportServiceImpl")
public class ElevatorDeviceReportServiceImpl extends BaseServiceSupport implements ElevatorDeviceReportService {

	@Resource(name="elevatorDeviceReportDaoImpl")
	private ElevatorDeviceReportDao elevatorDeviceReportDao;
	
	@Override
	public Grid<Map<String, Object>> getElevatorDeviceinfo(Map<String, Object> map) {
		Grid<Map<String, Object>> grid = new Grid<Map<String, Object>>();
		List<Map<String, Object>> list =elevatorDeviceReportDao.getElevatorDeviceinfo(map);
		grid.setRows(list);
		if(null!=list){
		Integer count =	elevatorDeviceReportDao.getElevatorDeviceinfoCount(map);
		grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public List<Map<String, Object>> exportElevatorDeviceinfo(Map map) {
		return elevatorDeviceReportDao.exportElevatorDeviceinfo(map);
	}

}
