package com.kuangchi.sdd.interfaceConsole.cardSender.service;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.cardSender.model.EmployeeSyncModel;

/**
 * 同步员工服务（供发卡软件用）
 * 
 * @author xuewen.deng
 * 
 */
public interface EmpCardSenSyncService {

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
	 * 
	 * @param deptNum
	 */
	public void delEmpFromDeptNum(String deptNum);

	/**
	 * 根据员工id，查员工编号和部门代码
	 * 
	 * @param staff_id
	 */
	public EmployeeSyncModel getDeptNumByEmpID(String staff_id);

	/**
	 * 根据部门代码查找默认岗位名称和代码
	 * 
	 * @return
	 */
	public EmployeeSyncModel defaultMC_DM(String deptNum);

	/**
	 * 根据员工id查找staff_num
	 */
	public String getStaffNumByID(String bsId);

	/**
	 * 根据staff_no查找staff
	 * 
	 * @author xuewen.deng
	 */
	public Integer getStaffCountByStaff_no(String staff_no);

	/**
	 * 根据部门代码查部门个数
	 * 
	 * @author xuewen.deng
	 * @param
	 */
	public Integer getDeptCount(String bm_dm);

	/**
	 * 获取所有员工
	 * 
	 * @author xuewen.deng
	 * */
	public List<EmployeeSyncModel> getAllEmp();

	/**
	 * 根据部门代码查部门编号
	 * 
	 * @author xuewen.deng
	 * @param
	 */
	public String getDNumByBmDm(String bm_dm);

	/**
	 * 根据staff_num判断人是否还有没退的卡
	 */
	public boolean isStaffBoundCard(String staffNum);
}
