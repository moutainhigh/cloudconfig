package com.kuangchi.sdd.consumeConsole.accountRelevant.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.accountRelevant.dao.AccountRelevantDao;
import com.kuangchi.sdd.consumeConsole.accountRelevant.service.AccountRelevantService;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
@Service("accountRelevantService")
public class AccountRelevantServiceImpl implements AccountRelevantService {
	
	@Autowired
	private AccountRelevantDao accountRelevantDao;
	
	@Resource(name="cDeviceService")
	IDeviceService cDeviceService;

	@Override
	public Grid getAccountTypeByMap(Map map) {
		Grid grid=new Grid();
		grid.setRows(accountRelevantDao.getAccountTypeByMap(map));
		grid.setTotal(accountRelevantDao.countAccountTypeByMap(map));
		return grid;
	}

	@Override  
	public void addAccountType(Map map) {
		accountRelevantDao.addAccountType(map);
	}

	@Override
	public void deleteAccountType(String nums) {
		accountRelevantDao.deleteAccountType(nums);
	}

	@Override
	public boolean ifExistUsingAccountType(String typeNum) {
		return accountRelevantDao.ifExistUsingAccountType(typeNum)||accountRelevantDao.ifDeviceUseAccountType(typeNum);
	}

	@Override
	public Map getAccountTypeMap(String typeNum) {
		return accountRelevantDao.getAccountTypeMap(typeNum);
	}

	@Override
	public void updateAccountType(Map map) {
		accountRelevantDao.updateAccountType(map);
	}

	@Override
	public List getAllAccoutType() {
		return accountRelevantDao.getAllAccountType();
	}

	@Override
	public List<Map> initType() {
		return accountRelevantDao.initType();
	}

	@Override
	public List<Map> getRegulationNames() {
		List<Map> list=accountRelevantDao.getRegulationNames();
		for(Map m:list){
			if(m.get("COLUMN_NAME").equals("staff_num")){
				m.put("CHNName","员工名称" );
			}else if(m.get("COLUMN_NAME").equals("staff_sex")){
				m.put("CHNName","员工性别" );
			}else if(m.get("COLUMN_NAME").equals("staff_position")){
				m.put("CHNName","员工岗位" );
			}else if(m.get("COLUMN_NAME").equals("staff_dept")){
				m.put("CHNName","员工部门" );
			}else if(m.get("COLUMN_NAME").equals("staff_hiredate")){
				m.put("CHNName","入职时间" );
			}
		}
		return list;
	}

	@Override
	public List<Map> getPositions() {
		return accountRelevantDao.getPositions();
	}

	@Override
	public List<Map> getStaffNames() {
		return accountRelevantDao.getStaffNames();
	}

	@Override
	public List<Map> getStaffDepts() {
		return accountRelevantDao.getStaffDepts();
	}

	@Override
	public void addRegulation(Map map) {
		accountRelevantDao.addRegulation(map);
	}

	@Override
	public Grid<Map> getAllRegulations(Map map) {
		Grid<Map> grid=new Grid<Map>();
		grid.setRows(accountRelevantDao.getAllRegulations(map));
		grid.setTotal(accountRelevantDao.countAllRegulations(map));
		return grid;
	}

	@Override
	public void delRegulation(String id) {
		accountRelevantDao.delRegulation(id);
	}

	@Override
	public boolean ifExistRegulation(String accountTypeNum) {
		return accountRelevantDao.ifExistRegulation(accountTypeNum);
	}

	@Override
	public void updateRegulation(Map map) {
		accountRelevantDao.updateRegulation(map);
	}

	@Override
	public List<Map> initRegulation() {
		return accountRelevantDao.initRegulation();
	}

	
	/**
	 * 通过账户类型获取人群
	 * 同时确保开户
	 * by gengji.yang
	 */
	public List<Map> getParticularStaffs(String accountTypeNum,String regulationId,String createUser){
		String staffCondition=accountRelevantDao.getRegulationByID(regulationId);
		Map map=new HashMap();
		map.put("staffCondition",staffCondition);
		List<Map> list=accountRelevantDao.getStaffsByCondition(map);
		for(Map m:list){
			makePossessAccount((String)m.get("staffNum"),accountTypeNum,createUser);
		}
		return list;
	}
	
/*	public List<Map> getParticularStaffs(String accountTypeNum,String createUser){
		String staffCondition=accountRelevantDao.getRegulationByAccountTypeNum(accountTypeNum);
		Map map=new HashMap();
		map.put("staffCondition",staffCondition);
		List<Map> list=accountRelevantDao.getStaffsByCondition(map);
		for(Map m:list){
			makePossessAccount((String)m.get("staffNum"),accountTypeNum,createUser);
		}
		return list;
	}*/
	
	/**
	 * 没有账户则开户
	 * 确保每个员工都有账户
	 * by gengji.yang
	 */
	public void makePossessAccount(String staffNum,String accountType,String createUser){
		Map map=new HashMap();
		map.put("staffNum", staffNum);
		map.put("accountTypeNum", accountType);
		if(!accountRelevantDao.ifExistAccount(map)){
			map.put("accountPassword","123456");
			map.put("createUser",createUser);
			accountRelevantDao.openDefaultAccountByType(map);
		}
	}
	
	/**
	 * 充值
	 * 生产账户流水
	 * by gengji.yang
	 */
	public void recordAccountFlow(String staffNum,String money,String accountTypeNum){
		Map map=accountRelevantDao.getStaffSimpleInfo(staffNum);
		map.put("money", money);
		map.put("accountTypeNum", accountTypeNum);
		Map m=accountRelevantDao.getAccountSimpleInfo(map);
		map.putAll(m);
		String nowStr=getNowTimeStr();
		String detailFlowNo=makeFlowNum("CZ",nowStr,m.get("accountNum").toString());
		map.put("nowStr", nowStr);
		map.put("detailFlowNo", detailFlowNo);
		accountRelevantDao.recordAccountFlow(map);
	}
	
	/**
	 * 方法重载
	 * 补助
	 * 生产账户流水
	 * by gengji.yang
	 */
	public void recordAccountFlow(String staffNum,String money,String accountTypeNum,String type){
		Map map=accountRelevantDao.getStaffSimpleInfo(staffNum);
		map.put("money", money);
		map.put("type", type);
		map.put("accountTypeNum", accountTypeNum);
		Map m=accountRelevantDao.getAccountSimpleInfo(map);
		map.putAll(m);
		String nowStr=getNowTimeStr();
		String detailFlowNo=makeFlowNum("BZ",nowStr,m.get("accountNum").toString());
		map.put("nowStr", nowStr);
		map.put("detailFlowNo", detailFlowNo);
		accountRelevantDao.recordSubsidizeAccountFlow(map);
	}

	@Override
	public Grid getAllAccountFlow(Map map) {
		List<Map> list=accountRelevantDao.getAllAcountFlow(map);
		Integer amount=accountRelevantDao.countAllAcountFlow(map);
		Grid grid=new Grid();
		grid.setRows(list);
		grid.setTotal(amount);
		return grid;
	}

	/**
	 * 原子操作：按员工
	 * 一个为员工充值的的完整步奏
	 * 确保开户
	 * 扣资金池并记录流水
	 * 充值到账户并记录流水
	 * 若有补扣，则记录补扣流水
	 * by gengji.yang
	 */
	@Override
	public boolean rechargeByStaffNum(String staffNum, String accountTypeNum,
			String money,String createUser) {
		try{
				Map map=new HashMap();
				BigDecimal bd=new BigDecimal(money);
				map.put("staffNum", staffNum);
				map.put("accountTypeNum", accountTypeNum);
				map.put("money", bd);
				makePossessAccount(staffNum,accountTypeNum,createUser);
				chargeByStaffNum(staffNum,accountTypeNum,money,createUser);
				deduct(staffNum,accountTypeNum,money);
				accountRelevantDao.reChargeEachStaff(map);
				recordAccountFlow(staffNum,money,accountTypeNum);
				tellDevice(staffNum,accountTypeNum,"0");
				return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 补扣
	 * 充值前，若账户余额<0,则进行补扣，并记录
	 * by gengji.yang
	 */
	public void deduct(String staffNum,String accountTypeNum,String money){
		Map map=new HashMap();
		map.put("staffNum", staffNum); 
		map.put("accountTypeNum", accountTypeNum);
		Map m=accountRelevantDao.getAccountSimpleInfo(map);
		BigDecimal balance=(BigDecimal)m.get("balance");
		if(balance.compareTo(BigDecimal.ZERO)==-1){
			BigDecimal inbound=new BigDecimal(money);
			map.put("tempBalance",balance.add(inbound)); 
			map.put("outbound", balance.abs()); 
			Map staffMap=accountRelevantDao.getStaffSimpleInfo(staffNum);
			map.put("type","3");
			map.putAll(m);
			map.putAll(staffMap);
			String nowStr=getNowTimeStr();
			String detailFlowNo=makeFlowNum("BK",nowStr,m.get("accountNum").toString());
			map.put("nowStr", nowStr);
			map.put("detailFlowNo", detailFlowNo);
			accountRelevantDao.recordDeductAccountFlow(map);
		}
	}
	

	/**
	 * 按部门：通过调用按员工实现
	 * by gengji.yang
	 */
	@Override
	public boolean rechargeByDeptNum(String deptNum, String accountTypeNum,
			String money,String createUser) {
		try{
			List<Map> list=accountRelevantDao.getStaffNumListBydeptNum(deptNum);
			for(Map m:list){
				rechargeByStaffNum((String)m.get("staffNum"),accountTypeNum,money,createUser);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	 /**
	  * 按账户类型：通过调用按员工实现
	  * by gengji.yang
	  */
	@Override
	public boolean rechargeByAccountType(String accountTypeNum, String money,String regulationId,String createUser) {
		List<Map> list=getParticularStaffs(accountTypeNum,regulationId,createUser);
		try{
			for(Map m:list){
				rechargeByStaffNum((String)m.get("staffNum"),accountTypeNum,money,createUser);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 根据账户类型充值前，判断各自资金池的钱是否够用
	 * 返回一个记录Map
	 * 主要返回的是记录Map中的message+i键与exception+i键
	 * 采用这样的map是因为可以动态的增加记录键，
	 * 同时，用LinkedHashMap来保证键的顺序
	 * by gengji.yang
	 */
	public Map isEnoughByAccountType(String accountTypeNum,String money,String regulationId){
		String staffCondition=accountRelevantDao.getRegulationByID(regulationId);
		Map map=new HashMap();
		map.put("staffCondition",staffCondition);
		List<Map> staffList=accountRelevantDao.getStaffsByCondition(map);
		List<Map> moneyPoolList=accountRelevantDao.getMoneyPoolNumListByAccountType(map);
		return makeRecordMap(staffList,moneyPoolList,money);
	}
	
	/**
	 * 生产记录Map
	 * 可共用的一个组件
	 * by gengji.yang
	 */
	public Map makeRecordMap(List<Map> staffList,List<Map> moneyPoolList,String money){
		Map<String,Object> recordMap=new LinkedHashMap<String,Object>();
		if(staffList.size()>0){
			for(int i=0;i<moneyPoolList.size();i++){
				recordMap.put("poolNum"+i,moneyPoolList.get(i).get("moneyPoolNum"));
				recordMap.put("needPoolMoney"+i,0.00);
				recordMap.put("message"+i,"");
				recordMap.put("exception"+i,"");
					for(Map m:staffList){
						if(m.get("moneyPoolNum")!=null){
							if(m.get("moneyPoolNum").equals(moneyPoolList.get(i).get("moneyPoolNum"))){
								Double a=((Double)recordMap.get("needPoolMoney"+i))+Double.parseDouble(money);
								recordMap.put("needPoolMoney"+i,a);
							}
						}else{
							recordMap.put("exception"+i,m.get("staffName")+"所在部门："+m.get("deptName")+" 没有资金池，请先绑定资金池");
							return recordMap;
						}
					}
			}
			for(int i=0;i<recordMap.keySet().size()/4;i++){
				Map infoMap=accountRelevantDao.getMoneyPoolInfo((String)recordMap.get("poolNum"+i));
				if(infoMap!=null){
					if("0".equals(infoMap.get("poolState"))){
						BigDecimal a=(BigDecimal)infoMap.get("poolSum");
						BigDecimal balance=a.subtract(new BigDecimal((Double)recordMap.get("needPoolMoney"+i)));
						if(balance.compareTo(BigDecimal.ZERO)==-1){
							recordMap.put("message"+i,"资金池： "+infoMap.get("poolName")+" 余额不足,差"+balance.abs().toString().substring(0, balance.abs().toString().length()-2)+"元，请先充值资金池");
						}
					}else{
						recordMap.put("message"+i,"资金池： "+infoMap.get("poolName")+" 被冻结，请先解冻资金池");
					}
				}
			}
			return recordMap;
		}else{
			recordMap.put("poolNum0","");
			recordMap.put("needPoolMoney0",0);
			recordMap.put("message0","");
			recordMap.put("exception0", "当前操作范围内不存在员工及其账户");
			return recordMap;
		}
	}
	
	/**
	 * 按人员充值，判断对应资金池是否够钱
	 * 实现过程类似同根据“账户类型”
	 * by gengji.yang
	 */
	public Map isEnoughByStaffNum(String staffNum,String accountTypeNum,String money){
		List<Map> staffList=new ArrayList<Map>();
		staffList.add(accountRelevantDao.getStaffSimpleInfo(staffNum));
		List<Map> moneyPoolList=accountRelevantDao.getMoneyPoolNumListByStaffNum(staffNum);
		return makeRecordMap(staffList, moneyPoolList, money);
	}
	
	/**
	 * 按部门充值，判断对应资金池是否够钱
	 * 实现过程类似同根据“账户类型”
	 * by gengji.yang
	 */
	public Map isEnoughByDeptNum(String deptNum,String accountTypeNum,String money){
		List<Map> staffList=accountRelevantDao.getStaffNumListBydeptNum(deptNum);
		List<Map> moneyPoolList=accountRelevantDao.getMoneyPoolNumListByDeptNum(deptNum);
		return makeRecordMap(staffList, moneyPoolList, money);
	}
	
	/**
	 * 生产资金池流水
	 * by gengji.yang
	 */
	public void recordFundFlow(String staffNum,String accountTypeNum,String money,String createUser){
		Map map=accountRelevantDao.getStaffSimpleInfo(staffNum);//{moneyPoolNum=22, deptName=123, staffName=ok, deptNum=d88c8b49-db75-4ca7-940c-44792fa0829d, staffNum=000000000001}
		Map moneyPoolMap=accountRelevantDao.getMoneyPoolInfo((String) map.get("moneyPoolNum"));
		map.put("money", money);
		map.put("leftTotal", moneyPoolMap.get("poolSum"));
		map.put("poolName", moneyPoolMap.get("poolName"));
		map.put("createUser", createUser);
		map.put("accountTypeNum", accountTypeNum);
		Map m=accountRelevantDao.getAccountSimpleInfo(map);//{balance=612.00, accountTypeName=主账户, accountTypeNum=1, accountNum=40095}
		map.putAll(m);
		accountRelevantDao.recordFundFlow(map);
	}
	
	/**
	 * 生产资金池流水for close account
	 * by gengji.yang
	 */
	public void recordFundFlowForCloseAcc(String staffNum,String accountTypeNum,String money,String createUser){
		Map map=accountRelevantDao.getStaffSimpleInfo(staffNum);//{moneyPoolNum=22, deptName=123, staffName=ok, deptNum=d88c8b49-db75-4ca7-940c-44792fa0829d, staffNum=000000000001}
		Map moneyPoolMap=accountRelevantDao.getMoneyPoolInfo((String) map.get("moneyPoolNum"));
		map.put("money", money);
		map.put("leftTotal", moneyPoolMap.get("poolSum"));
		map.put("poolName", moneyPoolMap.get("poolName"));
		map.put("createUser", createUser);
		map.put("accountTypeNum", accountTypeNum);
		Map m=accountRelevantDao.getAccountSimpleInfo(map);//{balance=612.00, accountTypeName=主账户, accountTypeNum=1, accountNum=40095}
		map.putAll(m);
		accountRelevantDao.recordFundFlowForCloseAcc(map);
	}
	
	/**
	 * 资金从资金池流向员工
	 * =从资金池扣钱
	 * by gengji.yang
	 */
	public void chargeByStaffNum(String staffNum,String accountTypeNum,String money,String createUser){
		Map m=accountRelevantDao.getStaffSimpleInfo(staffNum);//{moneyPoolNum=22, deptName=123, staffName=ok, deptNum=d88c8b49-db75-4ca7-940c-44792fa0829d, staffNum=000000000001}
		String moneyPoolNum=(String) m.get("moneyPoolNum");//{poolName=快乐园, poolState=0, inbound=232.6000, poolSum=182.6000}
		Map moneyPoolMap=accountRelevantDao.getMoneyPoolInfo(moneyPoolNum);
		BigDecimal leftTotalOld=(BigDecimal) moneyPoolMap.get("poolSum");
		BigDecimal leftTotal=leftTotalOld.subtract(new BigDecimal(money));
		BigDecimal inbound=(BigDecimal) moneyPoolMap.get("inbound");
		BigDecimal percent = leftTotal.divide(inbound, 4, RoundingMode.HALF_UP);
		Map map=new HashMap();
		BigDecimal bd=new BigDecimal(money);
		map.put("moneyPoolNum",moneyPoolNum);
		map.put("percent", percent);
		map.put("money", bd);
		accountRelevantDao.chargeFundEachStaff(map);
		recordFundFlow(staffNum,accountTypeNum,money,createUser);
	}

	/**
	 * 资金流回资金池
	 * =资金池+钱
	 * by gengji.yang
	 */
	public void moneyToPool(String staffNum,String accountTypeNum,String money,String createUser){
		Map m=accountRelevantDao.getStaffSimpleInfo(staffNum);//{moneyPoolNum=22, deptName=123, staffName=ok, deptNum=d88c8b49-db75-4ca7-940c-44792fa0829d, staffNum=000000000001}
		String moneyPoolNum=(String) m.get("moneyPoolNum");//{poolName=快乐园, poolState=0, inbound=232.6000, poolSum=182.6000}
		Map moneyPoolMap=accountRelevantDao.getMoneyPoolInfo(moneyPoolNum);
		BigDecimal leftTotalOld=(BigDecimal) moneyPoolMap.get("poolSum");
		BigDecimal leftTotal=leftTotalOld.add(new BigDecimal(money));
		BigDecimal inboundOld=(BigDecimal) moneyPoolMap.get("inbound");
		BigDecimal inbound=inboundOld.add(new BigDecimal(money));
		BigDecimal percent = leftTotal.divide(inbound, 4, RoundingMode.HALF_UP);
		Map map=new HashMap();
		BigDecimal bd=new BigDecimal(money);
		map.put("moneyPoolNum",moneyPoolNum);
		map.put("percent", percent);
		map.put("money", bd);
		accountRelevantDao.backToPool(map);
		recordFundFlowForCloseAcc(staffNum,accountTypeNum,money,createUser);
	}
	@Override
	public boolean rechargeByStaffNum(String staffNum, String accountTypeNum,
			String money, String createUser, String type) {
		try{
				Map map=new HashMap();
				BigDecimal bd=new BigDecimal(money);
				map.put("staffNum", staffNum);
				map.put("accountTypeNum", accountTypeNum);
				map.put("money", bd);
				makePossessAccount(staffNum,accountTypeNum,createUser);
				chargeByStaffNum(staffNum,accountTypeNum,money,createUser);
				deduct(staffNum,accountTypeNum,money);
				accountRelevantDao.reChargeEachStaff(map);
				recordAccountFlow(staffNum,money,accountTypeNum,type);
				tellDevice(staffNum,accountTypeNum,"2");
				return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean rechargeByDeptNum(String deptNum, String accountTypeNum,
			String money, String createUser, String type) {
		try{
			List<Map> list=accountRelevantDao.getStaffNumListBydeptNum(deptNum);
			for(Map m:list){
				rechargeByStaffNum((String)m.get("staffNum"),accountTypeNum,money,createUser,type);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean rechargeByAccountType(String accountTypeNum, String money,String regulationId,
			String createUser, String type) {
		List<Map> list=getParticularStaffs(accountTypeNum,regulationId,createUser);
		try{
			for(Map m:list){
				rechargeByStaffNum((String)m.get("staffNum"),accountTypeNum,money,createUser,type);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 流水号生成器
	 * by gengji.yang
	 */
	public String makeFlowNum(String operateType,String timeStr,String accountNum){
		String zeroStr="";
		if(accountNum.length()<8){
			for(int i=0;i<8-accountNum.length();i++){
				zeroStr+="0";
			}
		}
		String flowNum=operateType+getTimeStrForFlow(timeStr)+zeroStr+accountNum;
		return flowNum;
	}
	
	/**
	 * 生成时间串
	 * by gengji.yang
	 */
	public String getTimeStrForFlow(String nowStr){
		String result="";
		String[] blankArr=nowStr.split(" ");
		String[] gangArr=blankArr[0].split("-");
		String[] maoArr=blankArr[1].split(":");
		for(int i=0;i<gangArr.length;i++){
			result+=gangArr[i];
		}
		for(int i=0;i<maoArr.length;i++){
			result+=maoArr[i];
		}
		return result;
	}
	
	/**
	 * 生产时间
	 * by gengji.yang
	 */
	public String getNowTimeStr(){
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		Date now=new Date();
		String nowStr=sdf.format(now);
		return nowStr;
	}
	
	/**
	 * 生产 统计 时间
	 * 返回格式：2016-09-09 00:00:00
	 * by gengji.yang
	 */
	public String getStatisticTimeStr(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date now=new Date();
		String timeStr=sdf.format(now)+" 00:00:00";
		return timeStr;
	}

	@Override
	public Map viewRegulation(String id) {
		return accountRelevantDao.viewRegulation(id);
	}

	@Override
	public Grid searchAccount(Map map) {
		Grid grid=new Grid();
		List<Map> list=accountRelevantDao.searchAccount(map);
		Integer total=accountRelevantDao.countSearchAccount(map);
		grid.setRows(list);
		grid.setTotal(total);
		return grid;
	}

	@Override
	public Map isEnoughToCloseAccount(String staffNum, String accountTypeNum,
			String accountNum) {
		String accountBalance=accountRelevantDao.getAccountBalance(accountNum).get("accountBalance").toString();
		return isEnoughByStaffNum(staffNum, accountTypeNum, accountBalance);
	}

	@Override
	public boolean toCloseAccount(String staffNum, String accountTypeNum,
			String accountNum, String createUser) {
		try{
			String accountBalance=accountRelevantDao.getAccountBalance(accountNum).get("accountBalance").toString();
			moneyToPool(staffNum,accountTypeNum,accountBalance,createUser);//员工账户流回资金池
			//chargeByStaffNum(staffNum,accountTypeNum,accountBalance,createUser);
			accountRelevantDao.closeAccount(accountNum);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public Grid searchClosedAccount(Map map) {
		Grid grid=new Grid();
		List<Map> list=accountRelevantDao.searchCloseAccount(map);
		Integer total=accountRelevantDao.countCloseSearchAccount(map);
		grid.setRows(list);
		grid.setTotal(total);
		return grid;
	}
	
	/**
	 * 负责搞定员工每日部门每日充值，补助统计表
	 * by gengji.yang
	 */
	public void handleAllRechargeDaily(){
		handleStaffRechargeDaily();// 员工每日充值统计
		handleDeptRechargeDaily();//部门每日充值统计
		handleStaffBZDaily();//员工每日补助统计
		handleDeptBZDaily();//部门每日补助统计
	}
	
	/**
	 * 负责搞定员工，部门充值统计报表
	 * by gengji.yang
	 */
	public  void handleRecharge(){
		handleStaffRechargeDaily();// 员工每日充值统计
		handleDeptRechargeDaily();//部门每日充值统计
	}
	
	/**
	 * 负责搞定员工，部门补助统计报表
	 * by gengji.yang
	 */
	public void handleBZ(){
		handleStaffBZDaily();//员工每日补助统计
		handleDeptBZDaily();//部门每日补助统计
	}
	
	/**
	 * 负责搞定员工每日充值统计表
	 * by gengji.yang
	 */
	public void handleStaffRechargeDaily(){
		String everyDate=getStatisticTimeStr();
		List<Map> staffList=accountRelevantDao.getStaffsByCondition(null);
		for(Map m:staffList){
			makeSureStaffDailyRecord((String)m.get("staffNum"),everyDate);//确保员工每日有一条统计记录
			updateStaffDailyRecord((String)m.get("staffNum"),everyDate);//更新该记录
		}
	}
	
	/**
	 * 负责搞定员工每日补助统计表
	 * by gengji.yang
	 */
	public void handleStaffBZDaily(){
		String everyDate=getStatisticTimeStr();
		List<Map> staffList=accountRelevantDao.getStaffsByCondition(null);
		for(Map m:staffList){
			makeSureStaffBZDailyRecord((String)m.get("staffNum"),everyDate);
			updateStaffBZDailyRecord((String)m.get("staffNum"),everyDate);
		}
	}
	
	/**
	 * 负责搞定部门每日充值统计表
	 * by gengji.yang
	 */
	public void handleDeptRechargeDaily(){
		String everyDate=getStatisticTimeStr();
		List<Map> deptNumList=accountRelevantDao.getDeptNums();
		for(Map m:deptNumList){
			makeSureDeptDailyRecord((String)m.get("deptNum"),everyDate);
			updateDeptDailyRecord((String)m.get("deptNum"),everyDate);
		}
	}
	
	/**
	 * 负责搞定部门每日充值统计表
	 * by gengji.yang
	 */
	public void handleDeptBZDaily(){
		String everyDate=getStatisticTimeStr();
		List<Map> deptNumList=accountRelevantDao.getDeptNums();
		for(Map m:deptNumList){
			makeSureDeptBZDailyRecord((String)m.get("deptNum"),everyDate);
			updateDeptBZDailyRecord((String)m.get("deptNum"),everyDate);
		}
	}
	
	/**
	 * 确保员工每日充值统计表都有当天记录
	 * by gengji.yang
	 */
	public void makeSureStaffDailyRecord(String staffNum,String everyDate){
		Map map=new HashMap();
		map=accountRelevantDao.getStaffSimpleInfo(staffNum);
		map.put("everyDate", everyDate);
		boolean flag=accountRelevantDao.ifExistStaffDaily(map);
		if(!flag){
			accountRelevantDao.addOneStaffDaily(map);
		}
	}
	
	/**
	 * 确保员工每日补助统计表都有当天记录
	 * by gengji.yang
	 */
	public void makeSureStaffBZDailyRecord(String staffNum,String everyDate){
		Map map=new HashMap();
		map=accountRelevantDao.getStaffSimpleInfo(staffNum);
		map.put("everyDate", everyDate);
		boolean flag=accountRelevantDao.ifExistStaffBZDaily(map);
		if(!flag){
			accountRelevantDao.addOneStaffBZDaily(map);
		}
	}
	
	/**
	 * 确保部门每日充值统计表都有当天的记录
	 * by gengji.yang
	 */
	public void makeSureDeptDailyRecord(String deptNum,String everyDate){
		Map map=new HashMap();
		map=accountRelevantDao.getDeptSimpleInfo(deptNum);
		map.put("everyDate", everyDate);
		boolean flag=accountRelevantDao.ifExistDeptDaily(map);
		if(!flag){
			accountRelevantDao.addOneDeptDaily(map);
		}
	}
	
	/**
	 * 确保部门每日补助统计表都有当天的记录
	 * by gengji.yang
	 */
	public void makeSureDeptBZDailyRecord(String deptNum,String everyDate){
		Map map=new HashMap();
		map=accountRelevantDao.getDeptSimpleInfo(deptNum);
		map.put("everyDate", everyDate);
		boolean flag=accountRelevantDao.ifExistDeptBZDaily(map);
		if(!flag){
			accountRelevantDao.addOneDeptBZDaily(map);
		}
	}
	
	/**
	 * 负责更新员工每日充值统计表
	 * by gengji.yang
	 */
	public void updateStaffDailyRecord(String staffNum,String everyDate){
		Map map=new HashMap();
		map=accountRelevantDao.getStaffSimpleInfo(staffNum);
		map.put("everyDate", everyDate);
		List<Map> dailyList=accountRelevantDao.getStaffRechargeDailyInfo(map);
		String totalAmount=dailyList.size()+"";
		BigDecimal temp=BigDecimal.ZERO;
		for(int i=0;i<dailyList.size();i++){
			temp=temp.add((BigDecimal)dailyList.get(i).get("inbound"));
		}
		String totalMoney=temp+"";
		map.put("totalAmount", totalAmount);
		map.put("totalMoney", totalMoney);
		accountRelevantDao.updateStaffDailyRecord(map);
	}
	
	/**
	 * 负责更新员工每日补助统计表
	 * by gengji.yang
	 */
	public void updateStaffBZDailyRecord(String staffNum,String everyDate){
		Map map=new HashMap();
		map=accountRelevantDao.getStaffSimpleInfo(staffNum);
		map.put("everyDate", everyDate);
		List<Map> dailyList=accountRelevantDao.getStaffBZDailyInfo(map);
		String totalAmount=dailyList.size()+"";
		BigDecimal temp=BigDecimal.ZERO;
		for(int i=0;i<dailyList.size();i++){
			temp=temp.add((BigDecimal)dailyList.get(i).get("inbound"));
		}
		String totalMoney=temp+"";
		map.put("totalAmount", totalAmount);
		map.put("totalMoney", totalMoney);
		accountRelevantDao.updateStaffBZDailyRecord(map);
	}
	
	/**
	 * 负责更新部门每日统计数据
	 * by gengji.yang
	 */
	public void updateDeptDailyRecord(String deptNum,String everyDate){
		Map map=new HashMap();
		map.put("deptNum", deptNum);
		map.put("everyDate", everyDate);
		List<Map> deptList=accountRelevantDao.getListByDeptDailyOneByOne(map);
		Integer tempAmount=0;
		BigDecimal tempMoney=BigDecimal.ZERO;
		for(Map m:deptList){
			tempMoney=tempMoney.add((BigDecimal)m.get("totalMoney"));
			tempAmount+=(Integer)m.get("totalAmount");
		}
		map.put("totalMoney", tempMoney);
		map.put("totalAmount", tempAmount);
		accountRelevantDao.updateDeptDailyRecord(map);
	}
	
	/**
	 * 负责更新部门每日统计数据
	 * by gengji.yang
	 */
	public void updateDeptBZDailyRecord(String deptNum,String everyDate){
		Map map=new HashMap();
		map.put("deptNum", deptNum);
		map.put("everyDate", everyDate);
		List<Map> deptList=accountRelevantDao.getListByDeptBZDailyOneByOne(map);
		Integer tempAmount=0;
		BigDecimal tempMoney=BigDecimal.ZERO;
		for(Map m:deptList){
			tempMoney=tempMoney.add((BigDecimal)m.get("totalMoney"));
			tempAmount+=(Integer)m.get("totalAmount");
		}
		map.put("totalMoney", tempMoney);
		map.put("totalAmount", tempAmount);
		accountRelevantDao.updateDeptBZDailyRecord(map);
	}

	@Override
	public void frozenAccount(String accountNum) {
		accountRelevantDao.frozenAccount(accountNum);
	}

	@Override
	public void reFrozenAccount(String accountNum) {
		accountRelevantDao.reFrozenAccount(accountNum);
	}
	
	/**
	 * 调用minting.he的接口
	 * 下发名单
	 * by gengji.yang
	 */
	public void tellDevice(String staffNum,String accountTypeNum,String operateType){
		List<Map> staffConsumeCards=accountRelevantDao.getStaffConsumeCards(staffNum);
		List<Map> staffAccountTypeDeviceList=accountRelevantDao.getStaffAccountTypeDevices(accountTypeNum);
		for(Map m:staffConsumeCards){
			String cardNum=(String)m.get("cardNum");
			for(Map n:staffAccountTypeDeviceList){
				String deviceNum=(String)n.get("deviceNum");
				cDeviceService.insertNameTask(deviceNum, cardNum, "0",operateType);
			}
		}
	}

	@Override
	public Grid getDeptList(Map map) {
		Grid grid=new Grid();
		grid.setRows(accountRelevantDao.getDeptList(map));
		grid.setTotal(accountRelevantDao.countDeptList(map));
		return grid;
	}

	@Override
	public boolean checkAccTypeName(String accountTypeName) {
		return accountRelevantDao.checkAccTypeName(accountTypeName);
	}

}
