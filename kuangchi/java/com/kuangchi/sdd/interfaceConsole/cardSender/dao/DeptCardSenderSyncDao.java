package com.kuangchi.sdd.interfaceConsole.cardSender.dao;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.cardSender.model.DepartmentSyncModel;

/**
 * 同步部门服务（供发卡软件用）
 * 
 * @author xuewen.deng
 * 
 */
public interface DeptCardSenderSyncDao {
	/**
	 * 添加部门
	 * 
	 * @param departmentSync
	 */
	public void addDepartmentSync(DepartmentSyncModel departmentSync);

	/**
	 * 修改部门
	 * 
	 * @param departmentSync
	 */
	public void modifyDepartmentSync(DepartmentSyncModel departmentSync);

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
	public Integer isExistParentDM(String parentDM);

	/**
	 * 根据部门id查部门编号
	 * 
	 * @param deptID
	 * @return
	 */
	public String getDeptNum(String deptID);

	/**
	 * Description:根据部门编号查询数据
	 */
	List<DepartmentSyncModel> getCountByBm_Dm(String bm_dm);

	/**
	 * Description:获取所有部门
	 */
	List<DepartmentSyncModel> getAllBm();

	/**
	 * 根据远端部门id和部门编号查找数据
	 */
	List<DepartmentSyncModel> getCountByID_Bm_Dm(String remote_department_id,
			String bm_no);

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
