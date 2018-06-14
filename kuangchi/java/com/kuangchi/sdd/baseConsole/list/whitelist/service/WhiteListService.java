package com.kuangchi.sdd.baseConsole.list.whitelist.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.list.whitelist.model.WhiteList;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;

public interface WhiteListService {
	/**根据员工代码查询员工信息*/ 
	List<String> getStaffNameByParam(String yhDm);
	
	/**根据条件搜索员工信息(分页)*/
	public  Grid<WhiteList> searchWhiteListPage(DepartmentPage departmentPage);
	
	/**根据条件搜索员工信息*/
	public List<WhiteList> searchWhiteList(DepartmentPage departmentPage);
	
	
}
