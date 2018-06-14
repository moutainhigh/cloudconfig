package com.kuangchi.sdd.consumeConsole.balanceAccount.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.balanceAccount.dao.IBalanceAccountDao;
import com.kuangchi.sdd.consumeConsole.balanceAccount.model.AccountDetail;
import com.kuangchi.sdd.consumeConsole.balanceAccount.model.BalanceAccountModel;
import com.kuangchi.sdd.consumeConsole.consumeRecord.model.ConsumeRecord;

@Repository("balanceAccountDaoImpl")
public class BalanceAccountDaoImpl extends BaseDaoImpl<BalanceAccountModel> implements IBalanceAccountDao {

	@Override
	public String getNameSpace() {
		return "common.BalanceAccount";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<BalanceAccountModel> getBalanceAccountInfoList(String Page,String Rows) {
		int page=Integer.valueOf(Page);
		int rows=Integer.valueOf(Rows);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("page", (page-1)*rows);
		map.put("rows", rows);
		return this.getSqlMapClientTemplate().queryForList("selectAllBalanceAccountInfos", map);
	}

	@Override
	public Integer getBalanceAccountCount() {
		return queryCount("getBalanceAccountCount",null);
	}

	@Override
	public List<AccountDetail> getAccountDetailList(String previous_time,String current_time,String account_num, String Page, String Rows) {
		int page=Integer.valueOf(Page);
		int rows=Integer.valueOf(Rows);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("page", (page-1)*rows);
		map.put("rows", rows);
		map.put("previous_time", previous_time);
		map.put("current_time", current_time);
		map.put("account_num", account_num);
		return this.getSqlMapClientTemplate().queryForList("getAccountDetailList", map);
	}

	@Override
	public Integer getAccountDetailCount(String previous_time,String current_time,String account_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("previous_time", previous_time);
		map.put("current_time", current_time);
		map.put("account_num", account_num);
		return queryCount("getAccountDetailCount",map);
	}

	@Override
	public List<Map> getAllAccount() {
		return this.getSqlMapClientTemplate().queryForList("getAllAccount");
	}

	@Override
	public List<Map> selectBoundByNum(String account_num, String now_time) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("account_num", account_num);
		map.put("now_time", now_time);
		return this.getSqlMapClientTemplate().queryForList("selectBoundByNum", map);
	}

	@Override
	public void insertAccountExcep(BalanceAccountModel account) {
		this.insert("insertAccountExcep", account);
	}

	@Override
	public void updateAccountBalance(String account_num, String now_time,
			BigDecimal now_balance) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("account_num", account_num);
		map.put("now_time", now_time);
		map.put("now_balance", now_balance);
		this.update("updateAccountBalance", map);
	}

	@Override
	public boolean insertAccountDetail(AccountDetail account) {
		return this.insert("insertAccountDetail", account);
	}

	@Override
	public List<String> selCardByStaff(String staff_num) {
		return this.getSqlMapClientTemplate().queryForList("selCardByStaff", staff_num);
	}

	@Override
	public boolean insertConsumeRecord(ConsumeRecord record) {
		return this.insert("insertConsumeRecord", record);
	}

	@Override
	public boolean updateAccountExcep(String id) {
		return this.update("updateAccountExcep", id);
	}

	@Override
	public List<AccountDetail> getDetailList(String previous_time,
			String current_time, String account_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("previous_time", previous_time);
		map.put("current_time", current_time);
		map.put("account_num", account_num);
		return this.getSqlMapClientTemplate().queryForList("getDetailList", map);
	}
	
	@Override
	public BigDecimal getBalanceByAccount(Integer account_num){
		return (BigDecimal) this.getSqlMapClientTemplate().queryForObject("getBalanceByAccount", account_num);
	}
}
