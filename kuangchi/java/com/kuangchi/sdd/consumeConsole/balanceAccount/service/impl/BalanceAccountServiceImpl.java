package com.kuangchi.sdd.consumeConsole.balanceAccount.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.accountRelevant.service.AccountRelevantService;
import com.kuangchi.sdd.consumeConsole.balanceAccount.dao.IBalanceAccountDao;
import com.kuangchi.sdd.consumeConsole.balanceAccount.model.AccountDetail;
import com.kuangchi.sdd.consumeConsole.balanceAccount.model.BalanceAccountModel;
import com.kuangchi.sdd.consumeConsole.balanceAccount.service.IBalanceAccountService;
import com.kuangchi.sdd.consumeConsole.consumeRecord.model.ConsumeRecord;

@Service("balanceAccountServiceImpl")
public class BalanceAccountServiceImpl implements IBalanceAccountService {
	
	@Resource(name = "balanceAccountDaoImpl")
	private IBalanceAccountDao balanceAccountDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "accountRelevantService")
	private AccountRelevantService accountRelevantService;
	
	private List<AccountDetail> detailList=null;	
	private BigDecimal balance=null;
	private Integer consume_num = 1;

	@Override
	public Grid getBalanceAccountInfoList(String page, String rows) {
		try{
			List<BalanceAccountModel> balanceAccountInfoList=balanceAccountDao.getBalanceAccountInfoList(page,rows);
			Integer count=balanceAccountDao.getBalanceAccountCount();
			Grid grid=new Grid();
			grid.setRows(balanceAccountInfoList);
			grid.setTotal(count);
			return grid;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	@Override
	public Grid getAccountDetailList(String previous_time, String current_time,String account_num,String page, String rows) {
		try{
			List<AccountDetail> accountDetailList=balanceAccountDao.getAccountDetailList(previous_time,current_time,account_num,page,rows);
			detailList = balanceAccountDao.getDetailList(previous_time, current_time, account_num);
			Integer count=balanceAccountDao.getAccountDetailCount(previous_time,current_time,account_num);
			Grid grid=new Grid();
			grid.setRows(accountDetailList);
			grid.setTotal(count);
			return grid;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	@Override
	public List<Map> getAllAccount(){
		try{
			return balanceAccountDao.getAllAccount();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void accounts(Integer account_num, BigDecimal account_balance, Date date){
		try{
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
			String now_time = dateFormat1.format(date); 
			List<Map> boundList = balanceAccountDao.selectBoundByNum(account_num.toString(), now_time);	//清算时间内的流水账收支
			if(boundList!=null && boundList.size()!=0){
				String staff_num = (String) boundList.get(0).get("staff_num");
				String previous_time = (String) boundList.get(0).get("previous_time");
				BigDecimal previous_balance = new BigDecimal(boundList.get(0).get("previous_balance").toString()); 
				BigDecimal count = previous_balance;			//以上次清算金额为基
				for(Map<String, Object> boundMap: boundList){
					BigDecimal inbound = new BigDecimal(boundMap.get("inbound").toString());
					count = count.add(inbound);
					BigDecimal outbound = new BigDecimal(boundMap.get("outbound").toString());
					count = count.subtract(outbound);
				}
				if(count.compareTo(account_balance)!=0){		//流水金额异常
					BalanceAccountModel account = new BalanceAccountModel();
					String every_date = dateFormat2.format(date);
					account.setEvery_date(every_date);
					account.setStaff_num(staff_num.toString());
					account.setPrevious_time(previous_time);
					account.setPrevious_balance(previous_balance);
					account.setCurrent_balance(account_balance);
					account.setCurrent_time(now_time);
					account.setAccount_num(account_num.toString());
					balanceAccountDao.insertAccountExcep(account);
				}
			}
			balanceAccountDao.updateAccountBalance(account_num.toString(), now_time, account_balance);	//清算后更新账户信息
		}catch(Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public BigDecimal initBalance(BigDecimal previous_balance,
			BigDecimal current_balance) {
		try{
			BigDecimal count_balance = previous_balance;
			for(AccountDetail detail:detailList){
				count_balance = count_balance.add(detail.getInbound());
				count_balance = count_balance.subtract(detail.getOutbound());
			}
			balance = current_balance.subtract(count_balance);
			return balance;
		}catch(Exception e) {
			e.printStackTrace();
			return new BigDecimal(0);
		} 
	}

	@Override
	public boolean insertAccountDetail(AccountDetail account, String flow_num,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try{
			if(balance.compareTo(new BigDecimal(0))>=0){	//差额比0大，则为收，反则为支
				account.setInbound(balance);
				account.setOutbound(new BigDecimal(0));
			} else {
				BigDecimal bal = balance.multiply(new BigDecimal(-1));
				account.setOutbound(bal);
				account.setInbound(new BigDecimal(0));
			}
			int length = detailList.size();
			account.setTime(detailList.get(length-1).getTime());
			account.setStaff_num(detailList.get(0).getStaff_num());
			account.setStaff_name(detailList.get(0).getStaff_name());
			account.setStaff_no(detailList.get(0).getStaff_no());
			account.setDept_num(detailList.get(0).getDept_num());
			account.setDept_name(detailList.get(0).getDept_name());
			account.setDept_no(detailList.get(0).getDept_no());
			account.setDetail_flow_no(flow_num);
			boolean result = balanceAccountDao.insertAccountDetail(account);
			if(result){
				log.put("V_OP_TYPE", "业务");
			} else{
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally{
			log.put("V_OP_NAME", "账户清帐管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "新增账户交易流水");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean insertConsumeRecord(ConsumeRecord record, String time, String flow_num, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try{
			if(balance.compareTo(new BigDecimal(0))>=0){	//差额比0大，则为收，反则为支
				record.setInbound(balance);
				record.setOutbound(new BigDecimal(0));
			} else {
				BigDecimal bal = balance.multiply(new BigDecimal(-1));
				record.setOutbound(bal);
				record.setInbound(new BigDecimal(0));
			}
			record.setRecord_flow_num(flow_num);
			record.setCard_num("0");	//系统卡
			int length = detailList.size();
			record.setConsume_time(detailList.get(length-1).getTime());
			record.setCreate_time(time);
			record.setStaff_num(detailList.get(0).getStaff_num());
			record.setStaff_no(detailList.get(0).getStaff_no());
			record.setStaff_name(detailList.get(0).getStaff_name());
			record.setDept_num(detailList.get(0).getDept_num());
			record.setDept_no(detailList.get(0).getDept_no());
			record.setDept_name(detailList.get(0).getDept_name());
			record.setCard_flow_no(consume_num.toString());
			record.setRecord_flow_no(consume_num.toString());
			boolean result = balanceAccountDao.insertConsumeRecord(record);
			if(result){
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally{
			log.put("V_OP_NAME", "账户清帐管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "新增消费记录");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean updateAccountExcep(String id, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try{
			boolean result = balanceAccountDao.updateAccountExcep(id);
			if(result){
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally{
			log.put("V_OP_NAME", "账户清帐管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除清算异常记录");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean supplyBalance(String id, String current_balance, 
			String account_num, String login_user) {
		try{
			AccountDetail account = new AccountDetail();
			ConsumeRecord record = new ConsumeRecord();
			String time = accountRelevantService.getNowTimeStr();
			String flow_num = accountRelevantService.makeFlowNum("PZ", time, account_num);
			account.setAccount_num(account_num);
			account.setBalance(new BigDecimal(current_balance));	
			record.setAccount_num(account_num);
			record.setBalance(new BigDecimal(current_balance));
			boolean r1 = insertAccountDetail(account, flow_num, login_user);
			boolean r2 = insertConsumeRecord(record, time, flow_num, login_user);
			boolean r3 = updateAccountExcep(id, login_user);
			boolean result = false;
			if(r1 && r2 && r3){
				result = true;
				++consume_num;
			}
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	@Override
	public boolean clearAccounts(String login_user){
		Map<String, String> log = new HashMap<String, String>();
		try{
			//清算所有账户
 			List<Map> accountList = balanceAccountDao.getAllAccount();
 			Date date = new Date(); 	//当前时间
			if(accountList!=null && accountList.size()!=0){
				for(Map infoMap : accountList){	//检查每个账户
					Integer account_num = (Integer) infoMap.get("account_num");
					BigDecimal account_balance = new BigDecimal(infoMap.get("account_balance").toString());
					accounts(account_num, account_balance, date);
				}
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "账户清帐管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "账户清账");
			logDao.addLog(log);
		}
	}
	
}
