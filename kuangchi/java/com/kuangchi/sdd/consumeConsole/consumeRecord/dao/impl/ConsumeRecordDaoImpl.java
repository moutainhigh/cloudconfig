package com.kuangchi.sdd.consumeConsole.consumeRecord.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.Account;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.DeptConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.GoodConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.MealConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.PersonConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.TerminalConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeRecord.dao.IConsumeRecordDao;
import com.kuangchi.sdd.consumeConsole.consumeRecord.model.ConsumeRecord;

@Repository("ConsumeRecordDaoImpl")
public class ConsumeRecordDaoImpl extends BaseDaoImpl<ConsumeRecord> implements IConsumeRecordDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	/**
	 * 查询所有消费信息
	 */
	@Override
	public List<ConsumeRecord> selectAllConsumeRecords(
			ConsumeRecord consume_record, String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("card_num",consume_record.getCard_num());
		mapState.put("staff_no", consume_record.getStaff_no());
		mapState.put("staff_name", consume_record.getStaff_name());
		mapState.put("type", consume_record.getType());
		mapState.put("record_flow_num", consume_record.getRecord_flow_num());
		return this.getSqlMapClientTemplate().queryForList("selectAllConsumeRecords", mapState);
	}
	/**
	 * 查询总条数
	 */
	@Override
	public Integer getAllConsumeRecordCount(ConsumeRecord consume_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("card_num",consume_record.getCard_num());
		mapState.put("staff_no", consume_record.getStaff_no());
		mapState.put("staff_name", consume_record.getStaff_name());
		mapState.put("type", consume_record.getType());
		mapState.put("record_flow_num", consume_record.getRecord_flow_num());
		return queryCount("getAllConsumeRecordCount",mapState);
	}
	
	/**
	 * 导出消费信息
	 */
	@Override
	public List<ConsumeRecord> exportAllConsumeRecords(
			ConsumeRecord consume_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("card_num",consume_record.getCard_num());
		mapState.put("staff_no", consume_record.getStaff_no());
		mapState.put("staff_name", consume_record.getStaff_name());
		return this.getSqlMapClientTemplate().queryForList("exportAllConsumeRecords", mapState);
	}

	@Override
	public List<ConsumeRecord> getNewRecords() {
		return this.getSqlMapClientTemplate().queryForList("getNewRecords");
	}

	@Override
	public Integer getUser(String userName, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("password", password);
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getUser", map);
	}

	@Override
	public ConsumeRecord getRecordByNum(String record_flow_num) {
		return (ConsumeRecord) this.getSqlMapClientTemplate().queryForObject("getRecordByNum", record_flow_num);
	}

	@Override
	public Account getAccountByNum(String account_num) {
		return (Account) this.getSqlMapClientTemplate().queryForObject("getAccountByNum", account_num);
	}

	@Override
	public boolean updateRefundState(String record_flow_num) {
		return this.update("updateRefundState", record_flow_num);
	}
	
	@Override
	public boolean updateMealRefundTotal(MealConsumeDetail mealConsumeDetail) {
		return this.update("updateMealRefundTotal", mealConsumeDetail);
	}

	@Override
	public boolean updateGoodRefundTotal(GoodConsumeDetail goodConsumeDetail) {
		return this.update("updateGoodRefundTotal", goodConsumeDetail);
	}

	@Override
	public boolean updateDeptRefundTotal(DeptConsumeDetail deptConsumeDetail) {
		return this.update("updateDeptRefundTotal", deptConsumeDetail);
	}

	@Override
	public boolean updatePersonRefundTotal(
			PersonConsumeDetail personConsumeDetail) {
		return this.update("updatePersonRefundTotal", personConsumeDetail);
	}

	@Override
	public boolean updateTerminalRefundTotal(
			TerminalConsumeDetail terminalConsumeDetail) {
		return this.update("updateTerminalRefundTotal", terminalConsumeDetail);
	}
	
	
	
	@Override
	public boolean updateCancelState(Map<String, Object> map) {
		return this.update("updateCancelState", map);
	}
	
	@Override
	public boolean updateMealCancelTotal(MealConsumeDetail mealConsumeDetail) {
		return this.update("updateMealCancelTotal", mealConsumeDetail);
	}
	
	@Override
	public boolean updateGoodCancelTotal(GoodConsumeDetail goodConsumeDetail) {
		return this.update("updateGoodCancelTotal", goodConsumeDetail);
	}
	
	@Override
	public boolean updateDeptCancelTotal(DeptConsumeDetail deptConsumeDetail) {
		return this.update("updateDeptCancelTotal", deptConsumeDetail);
	}
	
	@Override
	public boolean updatePersonCancelTotal(
			PersonConsumeDetail personConsumeDetail) {
		return this.update("updatePersonCancelTotal", personConsumeDetail);
	}
	
	@Override
	public boolean updateTerminalCancelTotal(
			TerminalConsumeDetail terminalConsumeDetail) {
		return this.update("updateTerminalCancelTotal", terminalConsumeDetail);
	}
	
	@Override
	public Integer ifCancelAccount(String account_num){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("ifCancelAccount", account_num);
	}
	
}
