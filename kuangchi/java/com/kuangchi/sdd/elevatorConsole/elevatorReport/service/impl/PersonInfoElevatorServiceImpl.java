package com.kuangchi.sdd.elevatorConsole.elevatorReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.PersonInfoElevatorDao;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.PersonInfoElevator;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.service.PersonInfoElevatorService;

@Transactional
@Service("personInfoElevatorServiceImpl")
public class PersonInfoElevatorServiceImpl extends BaseServiceSupport implements PersonInfoElevatorService {

	@Resource(name="personInfoElevatorDaoImpl")
	private PersonInfoElevatorDao personInfoElevatorDao;
	
	@Override
	public Grid<PersonInfoElevator> getPersonInfoElevator(Map map) {
		Grid<PersonInfoElevator> grid = new Grid<PersonInfoElevator>();
		List<PersonInfoElevator> list =personInfoElevatorDao.getPersonInfoElevator(map);
		grid.setRows(list);
		if(null!=list){
		Integer count =	personInfoElevatorDao.getPersonInfoElevatorCount(map);
		grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public List<PersonInfoElevator> exportPersonInfoElevator(Map map) {
		return personInfoElevatorDao.exportPersonInfoElevator(map);
	}

}
