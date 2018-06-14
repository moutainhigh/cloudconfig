package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.EmployeeSyncModel;

public interface EmployeeSyncService {

	/**
	 * 添加员工
	 */
	public int addEmployeeSync(EmployeeSyncModel employeeSync);
	/**
	 * 修改员工
	 */
	public int modifyEmployeeSync(EmployeeSyncModel employeeSync);
	/**
	 * 删除员工
	 */
	public void delEmployeeSync(String remoteStaffIds);
	/**
	 * 根据部门编号删除员工
	 * @param deptNum
	 */
	public void delEmpFromDeptNum(String deptNum);
	/**
	 * 根据部门id，查部门代码
	 * @param parentID
	 */
	public String  getDeptNum(String deptID);
	/**
	 * 根据员工id，查员工编号和部门代码
	 * @param staff_id
	 */
	public EmployeeSyncModel  getDeptNumByEmpID(String staff_id);

	/**
	 * 根据部门代码查找默认岗位名称和代码
	 * @return
	 */
	public EmployeeSyncModel defaultMC_DM(String deptNum);
	/**
	 * 根据员工id查找staff_num
	 */
	public String getStaffNumByID(String remote_staff_id);
}
