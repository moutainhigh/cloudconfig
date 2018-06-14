package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.impl;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.DeptGwDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DeptGw;

@Repository("deptGwDaoImpl")
public class DeptGwDaoImpl extends BaseDaoImpl<DeptGw> implements DeptGwDao {

	@Override
	public String getNameSpace() {
		return "common.DeptGw";
	}
	@Override
	public String getTableName() {
		return null;
	}
	
	@Override
	public void addDeptGw(DeptGw deptGw) {
		getSqlMapClientTemplate().insert("addDeptGw", deptGw);
		
	}
	@Override
	public void delDeptGw(String deptNum) {
		getSqlMapClientTemplate().delete("delDeptGw",deptNum);
		
	}

}
