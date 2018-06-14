package com.kuangchi.sdd.interfaceConsole.cardSender.service;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.cardSender.model.DepartmentSyncModel;

public interface DeptCardSenderSyncService {

	/**
	 * 添加部门
	 * 
	 * @param departmentSync
	 */
	public int addDepartmentSync(DepartmentSyncModel departmentSync);

	/**
	 * 修改部门
	 * 
	 * @param departmentSync
	 */
	public int modifyDepartmentSync(DepartmentSyncModel departmentSync);

	/**
	 * 删除部门
	 * 
	 * @param depIds
	 */
	public void delDepartmentSync(String ids);

	/**
	 * 根据远程父ID，查上级部门代码
	 * 
	 * @param parentID
	 */
	public Integer isExistParentDM(String parentID);

	/**
	 * 根据部门id查部门编号
	 * 
	 * @param deptID
	 * @return
	 */
	public String getDeptNum(String deptID);

	/**
	 * Description:获取所有部门
	 */
	List<DepartmentSyncModel> getAllBm();

	/**
	 * 判断是否存在部门编号
	 * 
	 * @param deptNo
	 * @return
	 */
	public Integer isExistDeptNo(String deptNo);

	/**
	 * 根据部门代码获取员工个数
	 * 
	 * @author xuewen.deng
	 */
	public Integer getEmpCountByBmDm(String bsId);

}
