package com.kuangchi.sdd.baseConsole.list.whitelist.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.list.whitelist.dao.WhiteListDao;
import com.kuangchi.sdd.baseConsole.list.whitelist.model.WhiteList;
import com.kuangchi.sdd.baseConsole.list.whitelist.service.WhiteListService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
@Service("whiteListServiceImpl")
public class WhiteListServiceImpl implements WhiteListService {
	
	@Resource(name = "whiteListDaoImpl")
	private WhiteListDao whiteListDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	@Override
	public List<String> getStaffNameByParam(String yhDm) {
		return whiteListDao.getStaffNameByParam(yhDm);
	}
	
	@Override
	public Grid<WhiteList> searchWhiteListPage(DepartmentPage departmentPage) {
			Grid<WhiteList> grid = new Grid<WhiteList>();
	        List<WhiteList> resultList = whiteListDao.searchWhiteListPage(departmentPage);
	        grid.setRows(resultList);
	        if (null != resultList) {
	            grid.setTotal(whiteListDao.searchWhiteListCount(departmentPage));
	        } else {
	            grid.setTotal(0);
	        }
	        return grid;
	}
	
	public List<WhiteList> searchWhiteList(DepartmentPage departmentPage){
		return whiteListDao.searchWhiteList(departmentPage);
	}
	

}
