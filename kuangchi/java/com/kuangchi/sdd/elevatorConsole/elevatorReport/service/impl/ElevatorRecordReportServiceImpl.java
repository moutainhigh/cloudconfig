package com.kuangchi.sdd.elevatorConsole.elevatorReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.impl.ElevatorRecordReportDaoImpl;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorRecordInfo;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.service.ElevatorRecordReportService;

@Transactional
@Service("elevatorRecordReportServiceImpl")
public class ElevatorRecordReportServiceImpl extends BaseServiceSupport implements ElevatorRecordReportService {

	@Resource(name="elevatorRecordReportDaoImpl")
	private ElevatorRecordReportDaoImpl elevatorRecordReportDao;

	@Override
	public Grid<ElevatorRecordInfo> getElevatorRecordinfo(Map map) {
			Grid<ElevatorRecordInfo> grid = new Grid<ElevatorRecordInfo>();
			List<ElevatorRecordInfo> list =elevatorRecordReportDao.getElevatorRecordinfo(map);
			grid.setRows(list);
			if(null!=list){
			Integer count =	elevatorRecordReportDao.getElevatorRecordinfoCount(map);
			grid.setTotal(count);
			}else{
				grid.setTotal(0);
			}
			return grid;
		}

	@Override
	public List<ElevatorRecordInfo> exportElevatorRecordinfo(Map map) {
		return elevatorRecordReportDao.exportElevatorRecordinfo(map);
	}
	
	@Override
	public List<ElevatorRecordInfo> latestElevatorRecord() {
		try{
			return elevatorRecordReportDao.latestElevatorRecord();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}



