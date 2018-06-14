package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.impl;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.StaffGwDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.StaffGw;

@Repository("staffGwDaoImpl")
public class StaffGwDaoImpl extends BaseDaoImpl<StaffGw> implements StaffGwDao {

	@Override
	public String getNameSpace() {
		return "common.StaffGw";
	}
	@Override
	public String getTableName() {
		return null;
	}
	@Override
	public void addStaffGw(StaffGw staffGw) {
		getSqlMapClientTemplate().insert("addStaffGw", staffGw);
		
	}
	@Override
	public void delStaffGw(String staff_num) {
		 getSqlMapClientTemplate().delete("delStaffGw",staff_num);
		
	}

}
