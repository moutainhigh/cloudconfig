package com.kuangchi.sdd.baseConsole.list.whitelist.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.holiday.model.Holiday;
import com.kuangchi.sdd.baseConsole.list.whitelist.dao.WhiteListDao;
import com.kuangchi.sdd.baseConsole.list.whitelist.model.WhiteList;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
@Repository("whiteListDaoImpl")
public class WhiteListDaoImpl extends BaseDaoImpl<Holiday> implements WhiteListDao{

	@Override
	public List<String> getStaffNameByParam(String staff_num) {
		return getSqlMapClientTemplate().queryForList("getStaffNameByParam", staff_num);
	}


	@Override
	public List<WhiteList> searchWhiteListPage(DepartmentPage departmentPage) {
		return getSqlMapClientTemplate().queryForList("searchWhiteListPage",departmentPage);
	}

	@Override
	public Integer searchWhiteListCount(DepartmentPage departmentPage) {
        return (Integer)this.getSqlMapClientTemplate().queryForObject("searchWhiteListCount",departmentPage);
	}
	
	public List<WhiteList> searchWhiteList(DepartmentPage departmentPage){
		return getSqlMapClientTemplate().queryForList("searchWhiteList", departmentPage);
	}



	
	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

}
