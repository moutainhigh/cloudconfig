package com.kuangchi.sdd.interfaceConsole.cardSender.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.cardSender.dao.DeptCardSenderSyncDao;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.DepartmentSyncModel;

/**
 * 同步部门服务（供发卡软件用）
 * 
 * @author xuewen.deng
 * 
 */
@Repository("deptCardSenderSyncDaoImpl")
public class DeptCardSenderSyncDaoImpl extends BaseDaoImpl<DepartmentSyncModel>
		implements DeptCardSenderSyncDao {

	@Override
	public String getNameSpace() {
		return "common.DeptCardSenderSync";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public void addDepartmentSync(DepartmentSyncModel departmentSync) {
		getSqlMapClientTemplate().insert("addDeptCardSenderSync",
				departmentSync);
	}

	@Override
	public void modifyDepartmentSync(DepartmentSyncModel departmentSync) {
		getSqlMapClientTemplate().update("modifyDepatCardSenderSync",
				departmentSync);
	}

	@Override
	public void delDepartmentSync(String ids) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("id", ids);
		getSqlMapClientTemplate().delete("delDeptSync", mapParam);
	}

	@Override
	public Integer isExistParentDM(String parentDM) {

		return (Integer) getSqlMapClientTemplate().queryForObject(
				"isExistParentDM", parentDM);

	}

	@Override
	public List<DepartmentSyncModel> getCountByBm_Dm(String bm_no) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("bm_no", bm_no);
		return this.getSqlMapClientTemplate().queryForList("getCountByBm_Dm",
				mapParam);
	}

	@Override
	public List<DepartmentSyncModel> getAllBm() {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("flag", 1);
		return this.getSqlMapClientTemplate()
				.queryForList("getAllBm", mapParam);
	}

	@Override
	public List<DepartmentSyncModel> getCountByID_Bm_Dm(
			String remote_department_id, String bm_no) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("bm_no", bm_no);
		return this.getSqlMapClientTemplate().queryForList(
				"getDeptCountByBm_Dm", mapParam);
	}

	@Override
	public String getDeptNum(String deptID) {

		return (String) getSqlMapClientTemplate().queryForObject(
				"getDNumByBmDm_CardSender", deptID);
	}

	@Override
	public Integer isExistDeptNo(String deptNo) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"isExistDeptNo", deptNo);
	}

	@Override
	public Integer getEmpCountByBmDm(String bsId) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getEmpCountByBmDm", bsId);
	}

}
