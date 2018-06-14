package com.kuangchi.sdd.consumeConsole.balanceAccount.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.balanceAccount.model.AccountDetail;
import com.kuangchi.sdd.consumeConsole.balanceAccount.model.BalanceAccountModel;
import com.kuangchi.sdd.consumeConsole.consumeRecord.model.ConsumeRecord;

public interface IBalanceAccountDao {

	List<BalanceAccountModel> getBalanceAccountInfoList(String page, String rows);//查询所有账户异常记录

	Integer getBalanceAccountCount();//查询所有账户异常记录总数

	List<AccountDetail> getAccountDetailList(String previous_time,String current_time,String account_num, String page, String rows);//查询时间段内所有流水明细记录

	Integer getAccountDetailCount(String previous_time, String current_time,String account_num);//查询时间段内所有流水明细记录总数
	
	/**
	 * 获取全部账户
	 * @author minting.he
	 * @return
	 */
	public List<Map> getAllAccount();
	
	/**
	 * 查询账户在清算时间内的流水账收支
	 * @author minting.he
	 * @param account_num
	 * @param now_time
	 * @return
	 */
	public List<Map> selectBoundByNum(String account_num, String now_time);
	
	/**
	 * 插入流水异常
	 * @author minting.he
	 * @param account 
	 */
	public void insertAccountExcep(BalanceAccountModel account);
	
	/**
	 * 清算后更新账户信息
	 * @param account_num
	 * @param now_time
	 * @param now_balance
	 */
	public void updateAccountBalance(String account_num, String now_time, BigDecimal now_balance);
	
	/**
	 * 异常差额
	 * @author minting.he
	 * @param previous_time
	 * @param current_time
	 * @param account_num
	 */
	List<AccountDetail> getDetailList(String previous_time,String current_time,String account_num);
	
	/**
	 * 平账插入账户交易
	 * @author minting.he
	 * @param account
	 */
	public boolean insertAccountDetail(AccountDetail account);
	
	/**
	 * 根据账户号查询卡号
	 * @author minting.he
	 * @param staff_num
	 * @return
	 */
	public List<String> selCardByStaff(String staff_num);
	
	/**
	 * 平账插入消费记录
	 * @author minting.he
	 * @param record
	 */
	public boolean insertConsumeRecord(ConsumeRecord record);
	
	/**
	 * 平账后删除清算异常
	 * @author minting.he
	 * @param every_date
	 * @param account_num
	 */
	public boolean updateAccountExcep(String id);
	
	/**
	 * 查询账户余额
	 * @author minting.he
	 * @param account_num
	 * @return
	 */
	public BigDecimal getBalanceByAccount(Integer account_num);
}
