package com.kuangchi.sdd.consumeConsole.balanceAccount.quardz;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.consumeConsole.accountRelevant.service.AccountRelevantService;
import com.kuangchi.sdd.consumeConsole.balanceAccount.dao.IBalanceAccountDao;
import com.kuangchi.sdd.consumeConsole.balanceAccount.service.IBalanceAccountService;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.service.IGoodSubtotalService;

/**
 * 清算账户、商户、员工、部门 定时器
 * @author minting.he
 *
 */
public class CheckAccountBalanceQuarts {

	private IBalanceAccountService balanceAccountService;
	private ICronService cronService;
	private IGoodSubtotalService goodSubtotalService;
	private AccountRelevantService accountRelevantService;
	
	public IBalanceAccountService getBalanceAccountService() {
		return balanceAccountService;
	}
	public void setBalanceAccountService(
			IBalanceAccountService balanceAccountService) {
		this.balanceAccountService = balanceAccountService;
	}
	public ICronService getCronService() {
		return cronService;
	}
	public void setCronService(ICronService cronService) {
		this.cronService = cronService;
	}
	public IGoodSubtotalService getGoodSubtotalService() {
		return goodSubtotalService;
	}
	public void setGoodSubtotalService(IGoodSubtotalService goodSubtotalService) {
		this.goodSubtotalService = goodSubtotalService;
	}
	public AccountRelevantService getAccountRelevantService() {
		return accountRelevantService;
	}
	public void setAccountRelevantService(
			AccountRelevantService accountRelevantService) {
		this.accountRelevantService = accountRelevantService;
	}



	/**
	 * 清算流水
	 * @author minting.he
	 */
	public void checkAccountBalanceExcep() {
		try{
			//集群访问时，只有与数据库中相同的IP地址可以执行定时器的业务操作
			boolean r = cronService.compareIP();	
			if(r){
				//清算账户
	 			List<Map> accountList = balanceAccountService.getAllAccount();
				if(accountList!=null && accountList.size()!=0){
					Date d1 = new Date();				//当前时间
					/*Calendar calendar = Calendar.getInstance();
					calendar.setTime(d1);
					calendar.add(Calendar.DATE, -1); 	//定时器的前一天，手动清算的当天
					d1 = calendar.getTime();*/
					for(Map<String, Object> infoMap : accountList){	//检查每个账户
						Integer account_num = (Integer) infoMap.get("account_num");
						BigDecimal account_balance = new BigDecimal(infoMap.get("account_balance").toString());
						balanceAccountService.accounts(account_num, account_balance, d1);
					}
				}
				//清算商户
				List<Map> vendorList = goodSubtotalService.getVendorSubInfo();	//所有商户清算信息
				if(vendorList!=null && vendorList.size()!=0){	
					Date d2 = new Date(); 
					/*Calendar calendar = Calendar.getInstance();
					calendar.setTime(d2);
					calendar.add(Calendar.DATE, -1); 
					d2 = calendar.getTime();*/
					for(Map<String, Object> infoMap : vendorList){	
						String vendor_num = infoMap.get("vendor_num").toString();
						String previous_time = infoMap.get("previous_time").toString();
						goodSubtotalService.vendorSub(vendor_num, previous_time, d2);
					}
				}
				//清算员工、部门的充值和补助
				accountRelevantService.handleAllRechargeDaily();
			}
		}catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
}
