package com.kuangchi.sdd.consumeConsole.balanceAccount.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.balanceAccount.model.AccountDetail;
import com.kuangchi.sdd.consumeConsole.balanceAccount.model.BalanceAccountModel;
import com.kuangchi.sdd.consumeConsole.consumeRecord.model.ConsumeRecord;

public interface IBalanceAccountService {

	Grid getBalanceAccountInfoList(String page, String rows);//查询所有账户异常记录

	Grid getAccountDetailList(String previous_time, String current_time,String account_num,String page, String rows);//查询时间段内的所有流水明细记录
	
	/**
	 * 获取全部账户信息
	 * @author minting.he
	 * @return
	 */
	public List<Map> getAllAccount();
	
	/**
	 * 清算账户
	 * @author minting.he
	 * @param account_num
	 * @param account_balance
	 */
	public void accounts(Integer account_num, BigDecimal account_balance, Date date);
	
	/**
	 * 查看流水异常时显示差额
	 * @author minting.he
	 * @param previous_balance
	 * @param current_balance
	 */
	public BigDecimal initBalance(BigDecimal previous_balance, BigDecimal current_balance);
	
	/**
	 * 平账插入账户交易流水
	 * @author minting.he
	 * @param account
	 * @param login_user
	 */
	public boolean insertAccountDetail(AccountDetail account, String flow_num, String login_user);
	
	/**
	 * 平账插入消费记录
	 * @author minting.he
	 * @param record
	 * @param login_user
	 */
	public boolean insertConsumeRecord(ConsumeRecord record, String time, String flow_num, String login_user);
	
	/**
	 * 平账后删除清算异常
	 * @author minting.he
	 * @param every_date
	 * @param account_num
	 */
	public boolean updateAccountExcep(String id, String login_user);
	
	/**
	 * 平账，补差额
	 * @author minting.he
	 * @param current_balance
	 * @param every_date
	 * @param account_num
	 * @param login_user
	 * @return
	 */
	public boolean supplyBalance(String id, String current_balance, String account_num, String login_user);
	
	/**
	 * 手动清算账户
	 * @author minting.he
	 * @param accounts
	 * @param login_user
	 * @return
	 */
	public boolean clearAccounts(String login_user);
	
}
