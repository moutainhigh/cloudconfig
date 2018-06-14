package com.kuangchi.sdd.consumeConsole.financeStatistics.dao;


import java.util.List;

import com.kuangchi.sdd.consumeConsole.financeStatistics.model.AccountStatistics;
import com.kuangchi.sdd.consumeConsole.financeStatistics.model.FinanceStatistics;

public interface IFinancestatisticsDao {
	

	//根据充值类型查询充值、消费、补助、补扣、撤销消费信息 
	public List<FinanceStatistics> selectRecharge(FinanceStatistics finance);
	public List<FinanceStatistics> selectCarddeposit(FinanceStatistics finance);//查询所有的卡片押金
	public FinanceStatistics selectAccountInfoMoney(FinanceStatistics finance);//根据账户信息表所有余额信息
	public List<FinanceStatistics> selectAccountInfoBegin(FinanceStatistics finance);//查询账户信息表的期初余额
	public List<FinanceStatistics> selectAccountInfoEnd(FinanceStatistics finance);//查询账户信息表的期末余额
	public Boolean addAccountStatic(AccountStatistics account);//添加财务总表统计
	public List<AccountStatistics> selectAccountStatic(AccountStatistics account,String page,String rows);//查询财务总表信息
	public Integer selectAccountStaticCount(AccountStatistics account);
	
	public  List<FinanceStatistics> selectCardrefundDeposit(FinanceStatistics finance);//查询所有的退卡卡片押金
	
	public  List<FinanceStatistics> selectCardStateNum(FinanceStatistics finance);//查询卡状态次数 不包括退卡次数
	
	public  List<FinanceStatistics> selectRefundCardState(FinanceStatistics finance);//查询退卡次数
	
}
