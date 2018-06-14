package com.kuangchi.sdd.consumeConsole.financeStatistics.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;

import com.kuangchi.sdd.consumeConsole.financeStatistics.dao.IFinancestatisticsDao;
import com.kuangchi.sdd.consumeConsole.financeStatistics.model.AccountStatistics;
import com.kuangchi.sdd.consumeConsole.financeStatistics.model.FinanceStatistics;
import com.kuangchi.sdd.consumeConsole.financeStatistics.service.IFinancestatisticsService;

@Transactional
@Service("financestatisticsServiceImpl")
public class FinancestatisticsServiceImpl  implements IFinancestatisticsService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "financestatisticsDaoImpl")
	private IFinancestatisticsDao financestatisticsDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;

	//处理、导出财务报表信息
	@Override
	public List<FinanceStatistics> SelectAllStatistics(FinanceStatistics finance) {
		if(finance.getBegin_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(finance.getBegin_time());
				date.setHours(date.getHours()+24);
				finance.setBeginTimes(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	
		if(finance.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(finance.getEnd_time());
				date.setHours(date.getHours()+24);
				finance.setEndTimes(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<FinanceStatistics> list=new ArrayList<FinanceStatistics>();
		FinanceStatistics Newfinance=new FinanceStatistics();
		Newfinance.setBegin_time(finance.getBegin_time());
		Newfinance.setEnd_time(finance.getEndTimes());
		try {
			//所有充值、补助、撤销消费、消费、补扣、退款总额
			List<FinanceStatistics> recharge=financestatisticsDao.selectRecharge(finance);
			if(recharge.size()!=0){
				for (FinanceStatistics financeStatistics : recharge) {
					if(financeStatistics.getType().equals("0")){
						double a=Newfinance.getInbound_a();
						Newfinance.setInbound_a(a+financeStatistics.getInbound());
						Newfinance.setInbound_a_c(Newfinance.getInbound_a_c()+1);
					}else if(financeStatistics.getType().equals("1")){
						double a=Newfinance.getOutbound_a();
						Newfinance.setOutbound_a(a+financeStatistics.getOutbound());
						Newfinance.setOutbound_a_c(Newfinance.getOutbound_a_c()+1);
						
					}else if(financeStatistics.getType().equals("2")){
						double a=Newfinance.getInbound_b();
						Newfinance.setInbound_b(a+financeStatistics.getInbound());
						Newfinance.setInbound_b_c(Newfinance.getInbound_b_c()+1);
					}else if(financeStatistics.getType().equals("3")){
						
						double a=Newfinance.getOutbound_b();
						Newfinance.setOutbound_b(a+financeStatistics.getOutbound());
						Newfinance.setOutbound_b_c(Newfinance.getOutbound_b_c()+1);
						
					}else if(financeStatistics.getType().equals("4")){
						double a=Newfinance.getInbound_c();
						Newfinance.setInbound_c(a+financeStatistics.getInbound());
						Newfinance.setInbound_c_c(Newfinance.getInbound_c_c()+1);
					}else if(financeStatistics.getType().equals("5")){
						
						double a=Newfinance.getOutbound_c();
						Newfinance.setOutbound_c(a+financeStatistics.getInbound());
						Newfinance.setOutbound_c_c(Newfinance.getOutbound_c_c()+1);
					}
					
				}
			}
				
				//查询未发卡、已发卡收卡、退卡押金
				List<FinanceStatistics> card=financestatisticsDao.selectCarddeposit(finance);
				if(card.size()!=0){
					for (FinanceStatistics financeStatistics : card) {
						if(financeStatistics.getState_dm().equals("10")){
							double a=Newfinance.getNotcard_pledge_money();
							if(financeStatistics.getCard_pledge()!=null){
								Newfinance.setNotcard_pledge_money(a+Double.valueOf(financeStatistics.getCard_pledge()));
							}
							Newfinance.setNotcard_pledge_count(Newfinance.getRefund_pledge_count()+1);
						}else{
							double a=Newfinance.getCard_pledge_money();
							if(financeStatistics.getCard_pledge()!=null){
								Newfinance.setCard_pledge_money(a+Double.valueOf(financeStatistics.getCard_pledge()));	
							}
							
							Newfinance.setCard_pledge_count(Newfinance.getCard_pledge_count()+1);
						}
					}
				}
				
				//查询未发卡、已发卡收卡、退卡押金
				List<FinanceStatistics> refundcard=financestatisticsDao.selectCardrefundDeposit(finance);
				for (FinanceStatistics financeStatistics : refundcard) {
					if(financeStatistics.getState_dm().equals("60")){
					double a=Newfinance.getRefund_pledge_money();
					if(financeStatistics.getCard_pledge()!=null){
						Newfinance.setRefund_pledge_money(a+Double.valueOf(financeStatistics.getCard_pledge()));
					}
					Newfinance.setRefund_pledge_count(Newfinance.getRefund_pledge_count()+1);
				}
				}
				
				//查询账户表所有余额
				//FinanceStatistics account=financestatisticsDao.selectAccountInfoMoney(finance);
				/*if(account!=null){
					Newfinance.setPeople_accountMoney(account.getPeople_accountMoney());
				}*/
				
				//查询账户表期初余额
				List<FinanceStatistics> begin=financestatisticsDao.selectAccountInfoBegin(finance);
				if(begin.size()!=0){
					for (FinanceStatistics financeStatistics : begin) {
						double a=Newfinance.getBegin_accountMoney();
						Newfinance.setBegin_accountMoney(a+financeStatistics.getAccount_balance());
						Newfinance.setBegin_accountCount(Newfinance.getBegin_accountCount()+1);
					}
				}
				
				
				
				//查询卡片状态类型次数
				List<FinanceStatistics> cardstate=financestatisticsDao.selectCardStateNum(finance);
				if(cardstate.size()!=0){
					for (FinanceStatistics financeStatistics : cardstate) {
						if(financeStatistics.getState_dm().equals("00")){
							Newfinance.setCount00(Newfinance.getCount00()+1);
						}else if(financeStatistics.getState_dm().equals("10")){
							Newfinance.setCount10(Newfinance.getCount10()+1);
						}else if(financeStatistics.getState_dm().equals("20")){
							Newfinance.setCount20(Newfinance.getCount20()+1);
						}else if(financeStatistics.getState_dm().equals("30")){
							Newfinance.setCount30(Newfinance.getCount30()+1);
						}else if(financeStatistics.getState_dm().equals("40")||financeStatistics.getState_dm().equals("401")){
							Newfinance.setCount40(Newfinance.getCount40()+1);
						}else if(financeStatistics.getState_dm().equals("50")){
							Newfinance.setCount50(Newfinance.getCount50()+1);
						}else if(financeStatistics.getState_dm().equals("100")){
							Newfinance.setCount100(Newfinance.getCount100()+1);
						}
					}
				}
				
				//查询退卡次数
				List<FinanceStatistics> refundcardstate=financestatisticsDao.selectRefundCardState(finance);
				if(refundcardstate.size()!=0){
					for (FinanceStatistics financeStatistics : refundcardstate) {
						Newfinance.setCount60(Newfinance.getCount60()+1);
						}
					}
				
				//查询账户表期末余额
				List<FinanceStatistics> end=financestatisticsDao.selectAccountInfoEnd(finance);
				if(end.size()!=0){
					for (FinanceStatistics financeStatistics : end) {
						Newfinance.setEnd_accountMoney(financeStatistics.getEnd_accountMoney());
						Newfinance.setEnd_accountCount(financeStatistics.getEnd_accountCount());
					}
				}
				
		} catch (Exception e) {
		}
		list.add(Newfinance);

		//添加进财务统计总表
		AccountStatistics accounts=new AccountStatistics();
		accounts.setBegin_t(finance.getBegin_time());
		accounts.setEnd_t(finance.getEnd_time());
		accounts.setStart_account(Newfinance.getBegin_accountMoney());
		accounts.setStart_account_c(Newfinance.getBegin_accountCount());
		accounts.setEnd_account(Newfinance.getEnd_accountMoney());
		accounts.setEnd_account_c(Newfinance.getEnd_accountCount());
		accounts.setRecharge_inbound(Newfinance.getInbound_a());
		accounts.setRecharge_c(Newfinance.getInbound_a_c());
		accounts.setGrants_inbound(Newfinance.getInbound_b());
		accounts.setGrants_c(Newfinance.getInbound_b_c());
		accounts.setRevoke_inbound(Newfinance.getInbound_c());
		accounts.setRevoke_c(Newfinance.getInbound_c_c());
		accounts.setCard_inbound(Newfinance.getCard_pledge_money());
		accounts.setCard_c(Newfinance.getCard_pledge_count());
		accounts.setNotcard_inbound(Newfinance.getNotcard_pledge_money());
		accounts.setNotcard_c(Newfinance.getNotcard_pledge_count());
		accounts.setCardback_inbound(Newfinance.getRefund_pledge_money());
		accounts.setCardback_c(Newfinance.getRefund_pledge_count());
		accounts.setConsume_outbound(Newfinance.getOutbound_a());
		accounts.setConsume_c(Newfinance.getOutbound_a_c());
		accounts.setBuckle_outbound(Newfinance.getOutbound_b());
		accounts.setBuckle_c(Newfinance.getOutbound_b_c());
		accounts.setRefund_outbound(Newfinance.getOutbound_c());
		accounts.setRefund_c(Newfinance.getOutbound_c_c());
		financestatisticsDao.addAccountStatic(accounts);
		
		return list;
	}

	@Override
	public Grid selectAccountStatic(AccountStatistics account, String page,
			String rows) {
		int count=financestatisticsDao.selectAccountStaticCount(account);
		List<AccountStatistics> list=financestatisticsDao.selectAccountStatic(account, page, rows);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(list);
		return grid;
	}

	//导出报表汇总
	@Override
	public List<FinanceStatistics> ExportAllStatistics(FinanceStatistics finance) {
		if(finance.getBegin_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(finance.getBegin_time());
				date.setHours(date.getHours()+24);
				finance.setBeginTimes(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	
		if(finance.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(finance.getEnd_time());
				date.setHours(date.getHours()+24);
				finance.setEndTimes(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<FinanceStatistics> list=new ArrayList<FinanceStatistics>();
		FinanceStatistics Newfinance=new FinanceStatistics();
		Newfinance.setBegin_time(finance.getBegin_time());
		Newfinance.setEnd_time(finance.getEnd_time());
		try {
			//所有充值、补助、撤销消费、消费、补扣、退款总额
			List<FinanceStatistics> recharge=financestatisticsDao.selectRecharge(finance);
			if(recharge.size()!=0){
				for (FinanceStatistics financeStatistics : recharge) {
					if(financeStatistics.getType().equals("0")){
						double a=Newfinance.getInbound_a();
						Newfinance.setInbound_a(a+financeStatistics.getInbound());
						Newfinance.setInbound_a_c(Newfinance.getInbound_a_c()+1);
					}else if(financeStatistics.getType().equals("1")){
						double a=Newfinance.getOutbound_a();
						Newfinance.setOutbound_a(a+financeStatistics.getOutbound());
						Newfinance.setOutbound_a_c(Newfinance.getOutbound_a_c()+1);
						
					}else if(financeStatistics.getType().equals("2")){
						double a=Newfinance.getInbound_b();
						Newfinance.setInbound_b(a+financeStatistics.getInbound());
						Newfinance.setInbound_b_c(Newfinance.getInbound_b_c()+1);
					}else if(financeStatistics.getType().equals("3")){
						
						double a=Newfinance.getOutbound_b();
						Newfinance.setOutbound_b(a+financeStatistics.getOutbound());
						Newfinance.setOutbound_b_c(Newfinance.getOutbound_b_c()+1);
						
					}else if(financeStatistics.getType().equals("4")){
						double a=Newfinance.getInbound_c();
						Newfinance.setInbound_c(a+financeStatistics.getInbound());
						Newfinance.setInbound_c_c(Newfinance.getInbound_c_c()+1);
					}else if(financeStatistics.getType().equals("5")){
						
						double a=Newfinance.getOutbound_c();
						Newfinance.setOutbound_c(a+financeStatistics.getInbound());
						Newfinance.setOutbound_c_c(Newfinance.getOutbound_c_c()+1);
					}
					
				}
			}
				
				//查询未发卡、已发卡收卡、退卡押金
				List<FinanceStatistics> card=financestatisticsDao.selectCarddeposit(finance);
				if(card.size()!=0){
					for (FinanceStatistics financeStatistics : card) {
						if(financeStatistics.getState_dm().equals("10")){
							double a=Newfinance.getNotcard_pledge_money();
							if(financeStatistics.getCard_pledge()!=null){
								Newfinance.setNotcard_pledge_money(a+Double.valueOf(financeStatistics.getCard_pledge()));
							}
							Newfinance.setNotcard_pledge_count(Newfinance.getRefund_pledge_count()+1);
						}else{
							double a=Newfinance.getCard_pledge_money();
							if(financeStatistics.getCard_pledge()!=null){
								Newfinance.setCard_pledge_money(a+Double.valueOf(financeStatistics.getCard_pledge()));	
							}
							
							Newfinance.setCard_pledge_count(Newfinance.getCard_pledge_count()+1);
						}
					}
				}
				
				//查询未发卡、已发卡收卡、退卡押金
				List<FinanceStatistics> refundcard=financestatisticsDao.selectCardrefundDeposit(finance);
				for (FinanceStatistics financeStatistics : refundcard) {
					if(financeStatistics.getState_dm().equals("60")){
					double a=Newfinance.getRefund_pledge_money();
					if(financeStatistics.getCard_pledge()!=null){
						Newfinance.setRefund_pledge_money(a+Double.valueOf(financeStatistics.getCard_pledge()));
					}
					Newfinance.setRefund_pledge_count(Newfinance.getRefund_pledge_count()+1);
				}
				}
				
				//查询账户表所有余额
				//FinanceStatistics account=financestatisticsDao.selectAccountInfoMoney(finance);
				/*if(account!=null){
					Newfinance.setPeople_accountMoney(account.getPeople_accountMoney());
				}*/
				
				//查询账户表期初余额
				List<FinanceStatistics> begin=financestatisticsDao.selectAccountInfoBegin(finance);
				if(begin.size()!=0){
					for (FinanceStatistics financeStatistics : begin) {
						double a=Newfinance.getBegin_accountMoney();
						Newfinance.setBegin_accountMoney(a+financeStatistics.getAccount_balance());
						Newfinance.setBegin_accountCount(Newfinance.getBegin_accountCount()+1);
					}
				}
				
				
				
				//查询卡片状态类型次数
				List<FinanceStatistics> cardstate=financestatisticsDao.selectCardStateNum(finance);
				if(cardstate.size()!=0){
					for (FinanceStatistics financeStatistics : cardstate) {
						if(financeStatistics.getState_dm().equals("00")){
							Newfinance.setCount00(Newfinance.getCount00()+1);
						}else if(financeStatistics.getState_dm().equals("10")){
							Newfinance.setCount10(Newfinance.getCount10()+1);
						}else if(financeStatistics.getState_dm().equals("20")){
							Newfinance.setCount20(Newfinance.getCount20()+1);
						}else if(financeStatistics.getState_dm().equals("30")){
							Newfinance.setCount30(Newfinance.getCount30()+1);
						}else if(financeStatistics.getState_dm().equals("40")||financeStatistics.getState_dm().equals("401")){
							Newfinance.setCount40(Newfinance.getCount40()+1);
						}else if(financeStatistics.getState_dm().equals("50")){
							Newfinance.setCount50(Newfinance.getCount50()+1);
						}else if(financeStatistics.getState_dm().equals("100")){
							Newfinance.setCount100(Newfinance.getCount100()+1);
						}
					}
				}
				
				//查询退卡次数
				List<FinanceStatistics> refundcardstate=financestatisticsDao.selectRefundCardState(finance);
				if(refundcardstate.size()!=0){
					for (FinanceStatistics financeStatistics : refundcardstate) {
						Newfinance.setCount60(Newfinance.getCount60()+1);
						}
					}
				
				//查询账户表期末余额
				List<FinanceStatistics> end=financestatisticsDao.selectAccountInfoEnd(finance);
				if(end.size()!=0){
					for (FinanceStatistics financeStatistics : end) {
						Newfinance.setEnd_accountMoney(financeStatistics.getEnd_accountMoney());
						Newfinance.setEnd_accountCount(financeStatistics.getEnd_accountCount());
					}
				}
				
		} catch (Exception e) {
		}
		list.add(Newfinance);
		return list;
	}

}
