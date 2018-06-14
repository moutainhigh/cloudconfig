package com.kuangchi.sdd.baseConsole.list.whitelist.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.list.whitelist.model.WhiteList;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;

public interface WhiteListDao {
	/**根据员工代码查询员工信息*/ 
	List<String> getStaffNameByParam(String staff_num);
	
	/**  根据条件搜索员工信息(分页)（不包含黑名单人员编号）*/
    public  List<WhiteList> searchWhiteListPage(DepartmentPage departmentPage);
    
    /** 根据条件搜索员工信息总数（不包含黑名单人员编号） */
    public Integer searchWhiteListCount(DepartmentPage departmentPage);
    
    /** 根据条件搜索员工信息*/
    public List<WhiteList> searchWhiteList(DepartmentPage departmentPage);
    
    
    
    
}
