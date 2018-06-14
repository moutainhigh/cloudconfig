package com.kuangchi.sdd.interfaceConsole.cardSender.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.cardSender.dao.EmpCardSenSyncDao;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.EmployeeSyncModel;

@Repository("empCardSenSyncDaoImpl")
public class EmpCardSenSyncDaoImpl extends BaseDaoImpl<EmployeeSyncModel>
		implements EmpCardSenSyncDao {

	@Override
	public String getNameSpace() {
		return "common.EmpCardSenSync";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public void addEmployeeSync(EmployeeSyncModel employeeSync) {
		getSqlMapClientTemplate().insert("addEmpCardSenSync", employeeSync);
	}

	@Override
	public void modifyEmployeeSync(EmployeeSyncModel employeeSync) {
		getSqlMapClientTemplate().update("modifyEmpCardSenSync", employeeSync);
	}

	@Override
	public void delEmployeeSync(String bsId) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("bsId", bsId);
		getSqlMapClientTemplate().delete("delEmpCardSenSync", mapParam);
	}

	@Override
	public List<EmployeeSyncModel> getCountByStaffNum(String StaffNo) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_no", StaffNo);
		return this.getSqlMapClientTemplate().queryForList(
				"getCountByStaff_Num", mapParam);
	}

	@Override
	public List<EmployeeSyncModel> getCountByID_StaffNum(
			String remote_staff_id, String StaffNo) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("remote_staff_id", remote_staff_id);
		mapParam.put("staff_no", StaffNo);
		return this.getSqlMapClientTemplate().queryForList(
				"getCountByID_StaffNum", mapParam);
	}

	@Override
	public EmployeeSyncModel defaultMC_DM(String deptNum) {
		return (EmployeeSyncModel) getSqlMapClientTemplate().queryForObject(
				"defaultDM_MC", deptNum);
	}

	@Override
	public void delEmpFromDeptNum(String deptNum) {
		getSqlMapClientTemplate().delete("delEmpFromDeptNum", deptNum);

	}

	@Override
	public String getStaffNumByID(String bsId) {

		return (String) getSqlMapClientTemplate().queryForObject(
				"getStaffNumByBsId", bsId);

	}

	@Override
	public EmployeeSyncModel getDeptNumByEmpID(String staff_id) {
		return (EmployeeSyncModel) getSqlMapClientTemplate().queryForObject(
				"getDeptNumByStaffNum", staff_id);
	}

	@Override
	public Integer getStaffCountByStaff_no(String staff_no) {
		Integer staffCount = (Integer) getSqlMapClientTemplate()
				.queryForObject("getStaffCountByStaff_no", staff_no);
		return staffCount;

	}

	@Override
	public Integer getDeptCount(String bm_dm) {
		Integer deptCount = (Integer) getSqlMapClientTemplate().queryForObject(
				"getDeptCount", bm_dm);
		return deptCount;
	}

	@Override
	public List<EmployeeSyncModel> getAllEmp() {
		int flag = 1;
		return this.getSqlMapClientTemplate().queryForList("getAllEmp", flag);
	}

	@Override
	public String getDNumByBmDm(String bm_dm) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getDNumByBmDm", bm_dm);

	}

	@Override
	public Integer isStaffBoundCard(String staffNum) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"isStaffBoundCard", staffNum);
	}

}
