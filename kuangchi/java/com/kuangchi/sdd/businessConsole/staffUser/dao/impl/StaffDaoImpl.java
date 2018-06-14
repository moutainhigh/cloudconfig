package com.kuangchi.sdd.businessConsole.staffUser.dao.impl;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.staffUser.dao.IStaffDao;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;


@Repository("staffDaoImpl")
public class StaffDaoImpl extends BaseDaoImpl<Staff> implements IStaffDao{

	@Override
	public String getNameSpace() {
		return "common.Staff";
	}
	
	@Override
	public String getTableName() {
		return null;
	}
	
	//查询符合信息的员工
	public Staff getLoginStaff(Staff staff) throws SQLException {
		return (Staff) getSqlMapClient().queryForObject("getLoginStaff", staff);
	}

	//员工修改密码
	public void modifyPassword(Staff staff) {
		this.getSqlMapClientTemplate().update("modifyPassword", staff);
	}
	
	

	
}
