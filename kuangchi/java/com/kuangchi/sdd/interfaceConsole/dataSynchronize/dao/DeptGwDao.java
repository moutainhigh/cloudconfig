package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DeptGw;

public interface DeptGwDao {

	/**
	 * 添加部门时，同时添加部门默认岗位
	 * @param deptGw
	 */
	public void addDeptGw(DeptGw deptGw);
	
	/**
	 * 删除部门同时删除t_xt_gw表中该部门所有岗位
	 * @param deptNum
	 */
	public void delDeptGw(String deptNum);
	
	
}
