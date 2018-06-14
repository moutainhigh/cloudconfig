package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.DepartmentSyncDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DepartmentSyncModel;

@Repository("departmentSyncDaoImpl")
public class DepartmentSyncDaoImpl extends BaseDaoImpl<DepartmentSyncModel> implements DepartmentSyncDao {

	@Override
	public String getNameSpace() {
		return "common.DepartmentSync";
	}
	@Override
	public String getTableName() {
		return null;
	}
	@Override
	public void addDepartmentSync(DepartmentSyncModel departmentSync) {
		getSqlMapClientTemplate().insert("addDepartmentSync", departmentSync);
	}
	@Override
	public void modifyDepartmentSync(DepartmentSyncModel departmentSync) {
		getSqlMapClientTemplate().update("modifyDepartmentSync", departmentSync);
	}

	@Override
	public void delDepartmentSync(String remoteDepartmentIds) {
		 Map<String, Object> mapParam = new HashMap<String, Object>();
		 mapParam.put("id", remoteDepartmentIds);
		 getSqlMapClientTemplate().delete("delDepartmentSync",mapParam);
	}
	@Override
	public String getParentDM(String parentID) {
		
		String parentDM=(String) getSqlMapClientTemplate().queryForObject("getParentDM", parentID);
		return  parentDM;
	}

	@Override
	public List<DepartmentSyncModel> getCountByBm_Dm(String bm_no) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("bm_no", bm_no);
		return this.getSqlMapClientTemplate().queryForList("getCountByBm_Dm", mapParam);
	}

	@Override
	public List<DepartmentSyncModel> getCountByID_Bm_Dm(String remote_department_id, String bm_no) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("remote_department_id", remote_department_id);
		mapParam.put("bm_no", bm_no);
		return this.getSqlMapClientTemplate().queryForList("getCountByID_Bm_Dm", mapParam);
	}
	@Override
	public String getDeptNum(String deptID) {
		
		return (String) getSqlMapClientTemplate().queryForObject("getDeptNumById", deptID);
	}

}
