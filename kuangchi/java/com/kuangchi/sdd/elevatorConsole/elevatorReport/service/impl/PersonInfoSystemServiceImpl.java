package com.kuangchi.sdd.elevatorConsole.elevatorReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.PersonInfoSystemDao;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.PersonInfoSystem;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.service.PersonInfoSystemService;

@Transactional
@Service("personInfoSystemServiceImpl")
public class PersonInfoSystemServiceImpl extends BaseServiceSupport implements PersonInfoSystemService {

	@Resource(name="personInfoSystemDaoImpl")
	private PersonInfoSystemDao personInfoSystemDao;
	
	@Override
	public Grid<PersonInfoSystem> getPersonInfoSystem(Map map) {
		Grid<PersonInfoSystem> grid = new Grid<PersonInfoSystem>();
		List<PersonInfoSystem> list =personInfoSystemDao.getPersonInfoSystem(map);
		grid.setRows(list);
		if(null!=list){
		Integer count =	personInfoSystemDao.getPersonInfoSystemCount(map);
		grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public List<PersonInfoSystem> exportPersonInfoSystem(Map map) {
		
		return personInfoSystemDao.exportPersonInfoSystem(map);
	}

}
