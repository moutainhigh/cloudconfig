package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DepartmentSyncModel;

public interface DepartmentSyncService {

	/**
	 * 添加部门
	 * @param departmentSync
	 */
	public int addDepartmentSync(DepartmentSyncModel departmentSync);
	/**
	 * 修改部门
	 * @param departmentSync
	 */
	public int modifyDepartmentSync(DepartmentSyncModel departmentSync);
	/**
	 * 删除部门
	 * @param depIds
	 */
	public void delDepartmentSync(String remoteDepartmentIds);
	/**
	 * 根据远程父ID，查上级部门代码
	 * @param parentID
	 */
	public String  getParentDM(String parentID);
	/**
	 * 根据部门id查部门编号
	 * @param deptID
	 * @return
	 */
	public String getDeptNum(String deptID);
	
}
