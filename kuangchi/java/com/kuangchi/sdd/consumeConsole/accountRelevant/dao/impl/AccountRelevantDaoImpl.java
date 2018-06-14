package com.kuangchi.sdd.consumeConsole.accountRelevant.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attend.model.LeavetimeModel;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.accountRelevant.dao.AccountRelevantDao;

@Repository("accountRelevantDao")
public class AccountRelevantDaoImpl extends BaseDaoImpl<Map> implements AccountRelevantDao {
	
	@Override
	public String getNameSpace() {
		return "consumeConsole.accountRelevant";
	}  

	@Override
	public String getTableName() {
		return null;
	}
	
	
	
	@Override
	public List<Map> getAccountTypeByMap(Map map) {
		return getSqlMapClientTemplate().queryForList("getAccountTypeByMap", map);
	}

	@Override
	public Integer countAccountTypeByMap(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countAccountTypeByMap", map);
	}

	@Override
	public void addAccountType(Map map) {
		getSqlMapClientTemplate().insert("addAccountType", map);
	}

	@Override
	public void deleteAccountType(String nums) {
		getSqlMapClientTemplate().update("deleteAccountType", nums);
	}

	@Override
	public boolean ifExistUsingAccountType(String typeNum) {
		Integer amount=(Integer) getSqlMapClientTemplate().queryForObject("ifExistUsingAccountType", typeNum);
		if(amount.intValue()>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Map getAccountTypeMap(String typeNum) {
		return (Map) getSqlMapClientTemplate().queryForObject("getAccountTypeMap", typeNum);
	}

	@Override
	public void updateAccountType(Map map) {
		getSqlMapClientTemplate().update("updateAccountType", map);
	}

	@Override
	public List<Map> getAllAccountType() {
		return getSqlMapClientTemplate().queryForList("getAllAccountType");
	}

	@Override
	public List<Map> initType() {
		return getSqlMapClientTemplate().queryForList("initType");
	}

	@Override
	public List<Map> getRegulationNames() {
		return getSqlMapClientTemplate().queryForList("getRegulationNames");
	}

	@Override
	public List<Map> getPositions() {
		return getSqlMapClientTemplate().queryForList("getPositions");
	}

	@Override
	public List<Map> getStaffNames() {
		return getSqlMapClientTemplate().queryForList("getStaffNames");
	}

	@Override
	public List<Map> getStaffDepts() {
		return getSqlMapClientTemplate().queryForList("getStaffDepts");
	}

	@Override
	public void addRegulation(Map map) {
		getSqlMapClientTemplate().insert("addRegulation", map);
	}

	@Override
	public List<Map> getAllRegulations(Map map) {
		return getSqlMapClientTemplate().queryForList("getAllRegulations", map);
	}

	@Override
	public void delRegulation(String id) {
		getSqlMapClientTemplate().delete("delRegulation", id);
	}

	@Override
	public boolean ifExistRegulation(String accountTypeNum) {
		Integer amount=(Integer) getSqlMapClientTemplate().queryForObject("ifExistRegulation", accountTypeNum);
		if(amount>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void updateRegulation(Map map) {
		getSqlMapClientTemplate().update("updateRegulation", map);
	}

	@Override
	public Integer countAllRegulations(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countAllRegulations", map);
	}

	@Override
	public List<Map> initRegulation() {
		return getSqlMapClientTemplate().queryForList("initRegulation");
	}

	@Override
	public String getRegulationByAccountTypeNum(String accountTypeNum) {
		return (String)getSqlMapClientTemplate().queryForObject("getRegulationByAccountTypeNum", accountTypeNum);
	}

	@Override
	public List<Map> getStaffsByCondition(Map map) {
		return getSqlMapClientTemplate().queryForList("getStaffsByCondition", map);
	}

	@Override
	public boolean ifExistAccount(Map map) {
		Integer flag=(Integer) getSqlMapClientTemplate().queryForObject("ifExistAccount", map);
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void openDefaultAccountByType(Map map) {
		getSqlMapClientTemplate().insert("openDefaultAccountByType", map);
	}

	@Override
	public void reChargeEachStaff(Map map) {
		getSqlMapClientTemplate().update("reChargeEachStaff", map);
	}

	@Override
	public void recordAccountFlow(Map map) {
		getSqlMapClientTemplate().insert("recordAccountFlow", map);
	}

	@Override
	public Map getStaffSimpleInfo(String staffNum) {
		return (Map) getSqlMapClientTemplate().queryForObject("getStaffSimpleInfo", staffNum);
	}

	@Override
	public Map getAccountSimpleInfo(Map map) {
		return (Map)getSqlMapClientTemplate().queryForObject("getAccountSimpleInfo", map);
	}

	@Override
	public List<Map> getAllAcountFlow(Map map) {
		return getSqlMapClientTemplate().queryForList("getAllAcountFlow", map);
	}

	@Override
	public Integer countAllAcountFlow(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countAllAcountFlow", map);
	}

	@Override
	public List<Map> getStaffNumListBydeptNum(String deptNum) {
		return getSqlMapClientTemplate().queryForList("getStaffNumListBydeptNum", deptNum);
	}

	@Override
	public List<Map> getMoneyPoolNumListByAccountType(Map map) {
		return getSqlMapClientTemplate().queryForList("getMoneyPoolNumListByAccountType", map);
	}

	@Override
	public List<Map> getMoneyPoolNumListByStaffNum(String staffNum) {
		return getSqlMapClientTemplate().queryForList("getMoneyPoolNumListByStaffNum", staffNum);
	}

	@Override
	public List<Map> getMoneyPoolNumListByDeptNum(String deptNum) {
		return getSqlMapClientTemplate().queryForList("getMoneyPoolNumListByDeptNum", deptNum);
	}

	@Override
	public Map getMoneyPoolInfo(String poolNum) {
		return (Map) getSqlMapClientTemplate().queryForObject("getMoneyPoolInfo", poolNum);
	}

	@Override
	public void chargeFundEachStaff(Map map) {
		getSqlMapClientTemplate().update("chargeFundEachStaff", map);
	}

	@Override
	public void recordFundFlow(Map map) {
		getSqlMapClientTemplate().insert("recordFundFlow", map);
	}

	@Override
	public void recordSubsidizeAccountFlow(Map map) {
		getSqlMapClientTemplate().insert("recordSubsidizeAccountFlow", map);
	}

	@Override
	public void recordDeductAccountFlow(Map map) {
		getSqlMapClientTemplate().insert("recordDeductAccountFlow", map);
	}

	@Override
	public Map viewRegulation(String id) {
		return (Map) getSqlMapClientTemplate().queryForObject("viewRegulation", id);
	}

	@Override
	public List<Map> searchAccount(Map map) {
		return getSqlMapClientTemplate().queryForList("searchAccount", map);
	}

	@Override
	public Integer countSearchAccount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countSearchAccount", map);
	}

	@Override
	public Map getAccountBalance(String accountNum) {
		return (Map) getSqlMapClientTemplate().queryForObject("getAccountBalance", accountNum);
	}

	@Override
	public void closeAccount(String accountNum) {
		getSqlMapClientTemplate().update("closeAccount", accountNum);
	}

	@Override
	public List<Map> searchCloseAccount(Map map) {
		return getSqlMapClientTemplate().queryForList("searchCloseAccount", map);
	}

	@Override
	public Integer countCloseSearchAccount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countCloseSearchAccount", map);
	}

	@Override
	public void addOneStaffDaily(Map map) {
		getSqlMapClientTemplate().insert("addOneStaffDaily", map);
	}

	@Override
	public boolean ifExistStaffDaily(Map map) {
		Integer flag=(Integer) getSqlMapClientTemplate().queryForObject("ifExistStaffDaily", map);
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<Map> getStaffRechargeDailyInfo(Map map) {
		return getSqlMapClientTemplate().queryForList("getStaffRechargeDailyInfo", map);
	}

	@Override
	public void updateStaffDailyRecord(Map map) {
		getSqlMapClientTemplate().update("updateStaffDailyRecord", map);
	}

	@Override
	public List<Map> getDeptNums() {
		return getSqlMapClientTemplate().queryForList("getDeptNums");
	}

	@Override
	public List<Map> getListByDeptDailyOneByOne(Map map) {
		return getSqlMapClientTemplate().queryForList("getListByDeptDailyOneByOne", map);
	}

	@Override
	public boolean ifExistDeptDaily(Map map) {
		Integer flag=(Integer) getSqlMapClientTemplate().queryForObject("ifExistDeptDaily", map);
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Map getDeptSimpleInfo(String deptNum) {
		return (Map)getSqlMapClientTemplate().queryForObject("getDeptSimpleInfo", deptNum);
	}

	@Override
	public void addOneDeptDaily(Map map) {
		getSqlMapClientTemplate().insert("addOneDeptDaily", map);
	}

	@Override
	public void updateDeptDailyRecord(Map map) {
		getSqlMapClientTemplate().update("updateDeptDailyRecord", map);
	}

	@Override
	public boolean ifDeviceUseAccountType(String accountTypeNum) {
		Integer flag=(Integer) getSqlMapClientTemplate().queryForObject("ifDeviceUseAccountType", accountTypeNum);
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void frozenAccount(String accountNum) {
		getSqlMapClientTemplate().update("frozenAccount", accountNum);
	}

	@Override
	public void reFrozenAccount(String accountNum) {
		getSqlMapClientTemplate().update("reFrozenAccount", accountNum);
	}

	@Override
	public void addOneStaffBZDaily(Map map) {
		getSqlMapClientTemplate().insert("addOneStaffBZDaily", map);
	}

	@Override
	public boolean ifExistStaffBZDaily(Map map) {
		Integer flag=(Integer) getSqlMapClientTemplate().queryForObject("ifExistStaffBZDaily", map);
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<Map> getStaffBZDailyInfo(Map map) {
		return getSqlMapClientTemplate().queryForList("getStaffBZDailyInfo", map);
	}

	@Override
	public void updateStaffBZDailyRecord(Map map) {
		getSqlMapClientTemplate().update("updateStaffBZDailyRecord", map);
	}

	@Override
	public boolean ifExistDeptBZDaily(Map map) {
		Integer flag=(Integer) getSqlMapClientTemplate().queryForObject("ifExistDeptBZDaily", map);
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void addOneDeptBZDaily(Map map) {
		getSqlMapClientTemplate().insert("addOneDeptBZDaily", map);
	}

	@Override
	public List<Map> getListByDeptBZDailyOneByOne(Map map) {
		return getSqlMapClientTemplate().queryForList("getListByDeptBZDailyOneByOne", map);
	}

	@Override
	public void updateDeptBZDailyRecord(Map map) {
		getSqlMapClientTemplate().update("updateDeptBZDailyRecord", map);
	}

	@Override
	public List<Map> getStaffConsumeCards(String staffNum) {
		return getSqlMapClientTemplate().queryForList("getStaffConsumeCards", staffNum);
	}

	@Override
	public List<Map> getStaffAccountTypeDevices(String accountTypeNum) {
		return getSqlMapClientTemplate().queryForList("getStaffAccountTypeDevices", accountTypeNum);
	}

	@Override
	public void recordFundFlowForCloseAcc(Map map) {
		getSqlMapClientTemplate().insert("recordFundFlowForCloseAcc", map);
	}

	@Override
	public void backToPool(Map map) {
		getSqlMapClientTemplate().update("backToPool", map);
	}

	@Override
	public List<Map> getDeptList(Map map) {
		return getSqlMapClientTemplate().queryForList("getDeptList", map);
	}

	@Override
	public Integer countDeptList(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countDeptList", map);
	}

	@Override
	public boolean checkAccTypeName(String accountTypeName) {
		Integer flag=(Integer) getSqlMapClientTemplate().queryForObject("checkAccTypeName", accountTypeName);
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public String getRegulationByID(String regulationId) {
		return (String) getSqlMapClientTemplate().queryForObject("getRegulationByID", regulationId);
	}

}
