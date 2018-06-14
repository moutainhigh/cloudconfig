package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.EmployeeSyncDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.EmployeeSyncModel;

@Repository("employeeSyncDaoImpl")
public class EmployeeSyncDaoImpl extends BaseDaoImpl<EmployeeSyncModel> implements EmployeeSyncDao {

	@Override
	public String getNameSpace() {
		return "common.EmployeeSync";
	}
	@Override
	public String getTableName() {
		return null;
	}
	@Override
	public String getDeptNum(String deptID) {
		String deptNum=(String) getSqlMapClientTemplate().queryForObject("getDeptNum", deptID);
		return  deptNum;
	}
	@Override
	public void addEmployeeSync(EmployeeSyncModel employeeSync) {
		getSqlMapClientTemplate().insert("addEmployeeSync", employeeSync);
	}
	@Override
	public void modifyEmployeeSync(EmployeeSyncModel employeeSync) {
		getSqlMapClientTemplate().update("modifyEmployeeSync", employeeSync);
	}
	@Override
	public void delEmployeeSync(String remoteStaffIds) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("id", remoteStaffIds);
		getSqlMapClientTemplate().delete("delEmployeeSync",mapParam);
	}
	
	@Override
	public List<EmployeeSyncModel> getCountByStaffNum(String StaffNo) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_no", StaffNo);
		return this.getSqlMapClientTemplate().queryForList("getCountByStaff_Num", mapParam);
	}
	@Override
	public List<EmployeeSyncModel> getCountByID_StaffNum(String remote_staff_id,String StaffNo) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("remote_staff_id", remote_staff_id);
		mapParam.put("staff_no", StaffNo);
		return this.getSqlMapClientTemplate().queryForList("getCountByID_StaffNum", mapParam);
	}
	@Override
	public EmployeeSyncModel defaultMC_DM(String deptNum) {
		return (EmployeeSyncModel) getSqlMapClientTemplate().queryForObject("defaultMC_DM", deptNum);
	}
	@Override
	public void delEmpFromDeptNum(String deptNum) {
		getSqlMapClientTemplate().delete("delEmpFromDeptNum",deptNum);
		
	}
	@Override
	public String getStaffNumByID(String remote_staff_id) {
		String staffNum=(String) getSqlMapClientTemplate().queryForObject("getStaffNumByID", remote_staff_id);
		return  staffNum;
		
	}
	@Override
	public EmployeeSyncModel getDeptNumByEmpID(String staff_id) {
		return (EmployeeSyncModel) getSqlMapClientTemplate().queryForObject("getDeptNumByEmpID", staff_id);
	}
	

}
