package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DepartmentSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.EmployeeSyncModel;

public interface DepartmentSyncDao {
	/**
	 * 添加部门
	 * @param departmentSync
	 */
	public void addDepartmentSync(DepartmentSyncModel departmentSync);
	/**
	 * 修改部门
	 * @param departmentSync
	 */
	public void modifyDepartmentSync(DepartmentSyncModel departmentSync);
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
	/**
	  * Description:根据部门编号查询数据
	  */
	 List<DepartmentSyncModel> getCountByBm_Dm(String bm_dm);
	 /**
	  * 根据远端部门id和部门编号查找数据
	  */
	 List<DepartmentSyncModel> getCountByID_Bm_Dm(String remote_department_id,String bm_no);
}
