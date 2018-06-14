package com.kuangchi.sdd.consumeConsole.consumeRecord.service.impl;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.accountRelevant.service.AccountRelevantService;
import com.kuangchi.sdd.consumeConsole.consumeHandle.dao.IConsumeHandleDao;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.Account;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.DeptConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.GoodConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.MealConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.PersonConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.TerminalConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeRecord.dao.IConsumeRecordDao;
import com.kuangchi.sdd.consumeConsole.consumeRecord.model.ConsumeRecord;
import com.kuangchi.sdd.consumeConsole.consumeRecord.service.IConsumeRecordService;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
@Transactional
@Service("ConsumeRecordServiceImpl")
public class ConsumeRecordServiceImpl  implements IConsumeRecordService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "accountRelevantService")
	private AccountRelevantService accountRelevantService;
	
	@Resource(name = "cDeviceService")
	private IDeviceService deviceService;
	
	@Resource(name = "ConsumeRecordDaoImpl")
	private IConsumeRecordDao consumeRecordDao;
	
	@Resource(name = "consumeHandleDaoImpl")
	private IConsumeHandleDao consumeHandleDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	


	/**
	 * 查询所有消费信息
	 */
	@Override
	public Grid selectAllConsumeRecords(ConsumeRecord consume_record,
			String page, String size) {
		Integer count=consumeRecordDao.getAllConsumeRecordCount(consume_record);
		List<ConsumeRecord> consume=consumeRecordDao.selectAllConsumeRecords(consume_record, page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(consume);
		return grid;
	}


	/**
	 * 导出消费信息
	 */
	@Override
	public List<ConsumeRecord> exportConsumeRecords(ConsumeRecord consume_record) {
		List<ConsumeRecord> consume=consumeRecordDao.exportAllConsumeRecords(consume_record);
		try {
			
			for (ConsumeRecord consumeRecord : consume) {
				if(consumeRecord.getType()!=null){
					if(consumeRecord.getType().equals("1")){
						consumeRecord.setType("消费");
					}else if(consumeRecord.getType().equals("2")){
						consumeRecord.setType("撤销消费");
					}else if(consumeRecord.getType().equals("3")){
						consumeRecord.setType("平账");
					}else if(consumeRecord.getType().equals("4")){
						consumeRecord.setType("退款");
					}else{
						consumeRecord.setType("-");
					}
				}
				
			}
			for (ConsumeRecord consumeRecord : consume) {
				if(consumeRecord.getRefund()!=null){
					if(consumeRecord.getRefund().equals("1")){
						consumeRecord.setRefund("已退款");
					}else{
						consumeRecord.setRefund("-");
					}
				}
		}
		for (ConsumeRecord consumeRecord : consume) {
			if(consumeRecord.getIsCancel()!=null){
			if(consumeRecord.getIsCancel().equals("1")){
				consumeRecord.setIsCancel("已撤销");
			}else{
				consumeRecord.setIsCancel("-");
			}
			}
		}
		} catch (Exception e) {
		}
		return consume;
	}


	@Override
	public List<ConsumeRecord> getNewRecords() {
		return consumeRecordDao.getNewRecords();
	}


	@Override
	public Integer getUser(String userName, String password) {
		return consumeRecordDao.getUser(userName, password);
	}


	@Override
	public ConsumeRecord getRecordByNum(String record_flow_num) {
		return consumeRecordDao.getRecordByNum(record_flow_num);
	}

	/**
	 * 退款
	 */
	@Override
	public boolean refund(ConsumeRecord consumeRecord, String type) {
		try {
			addBalance(consumeRecord);
			/*if("CX".equals(type)){
			 	addConsumeRecord(consumeRecord);
			}*/
			addConsumeRecord(consumeRecord, type);
			addAccountDetail(consumeRecord, type);
			countMealCounsume(consumeRecord, type);
			countGoodConsume(consumeRecord, type);
			countDeptCousume(consumeRecord, type);
			countPersonCousume(consumeRecord, type);
			countTerminalCousume(consumeRecord, type);
			updateRefundState(consumeRecord, type);
			insertNameTask(consumeRecord, type);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-1 下午5:00:56
	 * @功能描述: 增加余额
	 * @参数描述:
	 */
	public void addBalance(ConsumeRecord consumeRecord){
		
		String accountNum = consumeRecord.getAccount_num();
		Account account = consumeRecordDao.getAccountByNum(accountNum);
		BigDecimal outBound = consumeRecord.getOutbound();
		Double balance = account.getAccount_balance();	
		consumeHandleDao.updateBalance(accountNum, outBound.doubleValue() + balance); 
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-9 上午10:04:13
	 * @功能描述: 下发人员名单
	 */
	public void insertNameTask(ConsumeRecord consumeRecord, String type){
		String deviceNum = consumeRecord.getDevice_num();
		String cardNum = consumeRecord.getCard_num();
		String triggerFlag = "";
		if("TK".equals(type)){
			triggerFlag = "5";
		} else if("CX".equals(type)){
			triggerFlag = "4";
		}
		deviceService.insertNameTask(deviceNum, cardNum, "0", triggerFlag);
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-24 下午4:57:59
	 * @功能描述: 新增交易明细记录
	 * @参数描述:
	 */
	public void addAccountDetail(ConsumeRecord consumeRecord, String type){
		
		// 生成创建时间和流水号
		String timeStr = accountRelevantService.getNowTimeStr();
		String recordFlowNum = accountRelevantService.makeFlowNum(type, timeStr, consumeRecord.getAccount_num());
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("account_num", consumeRecord.getAccount_num());
		map.put("detail_flow_no", recordFlowNum);
		map.put("time", formatTime(timeStr));
		
		map.put("staff_no", consumeRecord.getStaff_no());
		map.put("staff_num", consumeRecord.getStaff_num());
		map.put("staff_name", consumeRecord.getStaff_name());
		map.put("dept_no", consumeRecord.getDept_no());
		map.put("dept_num", consumeRecord.getDept_num());
		map.put("dept_name", consumeRecord.getDept_name());
		
		String balance = consumeRecordDao.getAccountByNum(consumeRecord.getAccount_num()).getAccount_balance().toString();
		map.put("balance", balance);
		map.put("inbound", consumeRecord.getOutbound());
		map.put("outbound", 0.0);
		if("TK".equals(type)){
			map.put("type", 5);
		} else if("CX".equals(type)){
			map.put("type", 4);
		}
		consumeHandleDao.addAccountDetail(map);
	}
	
	
	
	/**
	 * 新增刷卡消费流水
	 * @author yuman.gao
	 */
	public void addConsumeRecord(ConsumeRecord consumeRecord, String type){
		
		// 生成创建时间和流水号
		String timeStr = accountRelevantService.getNowTimeStr();
		String recordFlowNum = accountRelevantService.makeFlowNum(type, timeStr, consumeRecord.getAccount_num());
				
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("consume_time", formatTime(timeStr));
		map.put("create_time", formatTime(timeStr));
		map.put("record_flow_num", recordFlowNum); 
		
		map.put("card_flow_no", UUID.randomUUID().toString());
		map.put("record_flow_no",  UUID.randomUUID().toString());
		map.put("card_num", consumeRecord.getCard_num());
		map.put("account_num", consumeRecord.getAccount_num());
		
		map.put("meal_num", consumeRecord.getMeal_num());
		map.put("meal_name", consumeRecord.getMeal_name());
		map.put("meal_start_time", consumeRecord.getMeal_start_time());
		map.put("meal_end_time", consumeRecord.getMeal_end_time());
		
		map.put("staff_no", consumeRecord.getStaff_no());
		map.put("staff_num", consumeRecord.getStaff_num());
		map.put("staff_name", consumeRecord.getStaff_name());
		map.put("dept_no", consumeRecord.getDept_no());
		map.put("dept_num", consumeRecord.getDept_num());
		map.put("dept_name", consumeRecord.getDept_name());
		map.put("group_num", consumeRecord.getGroup_num());
		map.put("group_name", consumeRecord.getGroup_name());
		
		map.put("device_num", consumeRecord.getDevice_num());
		map.put("device_name", consumeRecord.getDevice_name());
		map.put("vendor_num", consumeRecord.getVendor_num());
		map.put("vendor_name", consumeRecord.getVendor_name());
		map.put("vendor_dealer_name", consumeRecord.getVendor_dealer_name());
		
		map.put("good_type_num", consumeRecord.getGood_type_num());
		map.put("good_type_name", consumeRecord.getGood_type_name());
		map.put("good_type_price", consumeRecord.getGood_type_price()); 
		
		map.put("good_num", consumeRecord.getGood_num());
		map.put("good_name", consumeRecord.getGood_name());
		map.put("good_price", consumeRecord.getGood_price());
		
		String balance = consumeRecordDao.getAccountByNum(consumeRecord.getAccount_num()).getAccount_balance().toString();
		map.put("balance", balance);
		map.put("inbound", consumeRecord.getOutbound());
		map.put("outbound", 0.0);
		if("CX".equals(type)){
			map.put("type", 2);
		} else if ("TK".equals(type)){
			map.put("type", 4);
		}
		
		consumeHandleDao.addConsumeRecord(map); 
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-5 上午9:48:16
	 * @功能描述: 更新退款状态
	 * @参数描述:
	 */
	public void updateRefundState(ConsumeRecord consumeRecord, String type){
		String record_flow_num = consumeRecord.getRecord_flow_num().toString();
		if("TK".equals(type)){
			consumeRecordDao.updateRefundState(record_flow_num);
		} else if("CX".equals(type)){
			consumeHandleDao.updateCancelRecord(record_flow_num);
		}
		
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-6 上午8:54:14
	 * @功能描述: 餐次消费日统计
	 * @参数描述:
	 */
	public void countMealCounsume(ConsumeRecord consumeRecord, String type){
		// 查询已存在记录
		String mealNum = consumeRecord.getMeal_num();
		String consumeDate = consumeRecord.getConsume_time().split(" ")[0];
		MealConsumeDetail mealDetail = consumeHandleDao.getMealDetailByNum(mealNum, consumeDate);
			
		Double outBound = consumeRecord.getOutbound().doubleValue();
		
		if("TK".equals(type)){
			mealDetail.setRefund_total(mealDetail.getRefund_total() + outBound);
			consumeRecordDao.updateMealRefundTotal(mealDetail);
		} else if("CX".equals(type)){
			mealDetail.setCancel_total(mealDetail.getCancel_total() + outBound);
			consumeHandleDao.updateMealCancelTotal(mealDetail);
		}
		
		
		
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-1 下午5:00:56
	 * @功能描述: 商品消费日统计
	 * @参数描述:
	 */
	public void countGoodConsume(ConsumeRecord consumeRecord, String type){
		
		// 查询已存在记录
		String goodNum = consumeRecord.getGood_num();
		if(goodNum == null){
			return;
		}
		String consumeDate = consumeRecord.getConsume_time().split(" ")[0];
		Double goodPrice = consumeRecord.getGood_price().doubleValue();
		GoodConsumeDetail goodDetail = consumeHandleDao.getGoodDetailByNum(goodNum, consumeDate, goodPrice);
		
		Double outBound = consumeRecord.getOutbound().doubleValue();
		if("TK".equals(type)){
			goodDetail.setRefund_total(goodDetail.getRefund_total() + outBound);
			consumeRecordDao.updateGoodRefundTotal(goodDetail);
		} else if("CX".equals(type)){
			goodDetail.setCancel_total(goodDetail.getCancel_total() + outBound);
			consumeHandleDao.updateGoodCancelTotal(goodDetail);
		}
		
		
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-23 下午8:59:01
	 * @功能描述: 人员消费日统计
	 * @参数描述:
	 */
	public void countPersonCousume(ConsumeRecord consumeRecord, String type){
		
		String staffNum = consumeRecord.getStaff_num();
		String consumeDate = consumeRecord.getConsume_time().split(" ")[0];
		PersonConsumeDetail personDetail = consumeHandleDao.getPersonDetailByNum(staffNum,consumeDate);
		
		Double outBound = consumeRecord.getOutbound().doubleValue();
		if("TK".equals(type)){
			personDetail.setRefund_total(personDetail.getRefund_total() + outBound);
			consumeRecordDao.updatePersonRefundTotal(personDetail);
		} else if("CX".equals(type)){
			personDetail.setCancel_total(personDetail.getCancel_total() + outBound);
			consumeHandleDao.updatePersonCancelTotal(personDetail);
		}
		
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-2 下午5:01:37
	 * @功能描述: 部门消费日统计
	 * @参数描述:
	 */
	public void countDeptCousume(ConsumeRecord consumeRecord, String type){
		
		String DeptNum = consumeRecord.getDept_num();
		String consumeDate = consumeRecord.getConsume_time().split(" ")[0];
		DeptConsumeDetail deptDetail = consumeHandleDao.getDeptDetailByNum(DeptNum,consumeDate);
		
		Double outBound = consumeRecord.getOutbound().doubleValue();
		if("TK".equals(type)){
			deptDetail.setRefund_total(deptDetail.getRefund_total() + outBound);
			consumeRecordDao.updateDeptRefundTotal(deptDetail);
		} else if("CX".equals(type)){
			deptDetail.setCancel_total(deptDetail.getCancel_total() + outBound);
			consumeHandleDao.updateDeptCancelTotal(deptDetail);
		}
		
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-2 下午5:03:46
	 * @功能描述: 终端消费日统计
	 * @参数描述:
	 */
	public void countTerminalCousume(ConsumeRecord consumeRecord, String type){
	
		String DeviceNum = consumeRecord.getDevice_num();
		String consumeDate = consumeRecord.getConsume_time().split(" ")[0];
		TerminalConsumeDetail terminalDetail = consumeHandleDao.getTerminalDetailByNum(DeviceNum,consumeDate);
		
		Double outBound = consumeRecord.getOutbound().doubleValue();
		if("TK".equals(type)){
			terminalDetail.setRefund_total(terminalDetail.getRefund_total() + outBound);
			consumeRecordDao.updateTerminalRefundTotal(terminalDetail);
		} else if("CX".equals(type)){
			terminalDetail.setCancel_total(terminalDetail.getCancel_total() + outBound);
			consumeHandleDao.updateTerminalCancelTotal(terminalDetail);
		}
	}
	
	@Override
	public Integer ifCancelAccount(String account_num){
		try{
			return consumeRecordDao.ifCancelAccount(account_num);
		}catch(Exception e){
			e.printStackTrace();
			return 1;
		}
	}
	
	
	/**
	 * 返回精确到秒的字符串
	 * @param  yyyy-mm-dd HH:MM:ss:SSS 
	 * @return yyyy-mm-dd HH:MM:ss
	 */
	public String formatTime(String timeStr){
		return timeStr.substring(0, timeStr.lastIndexOf(":"));
	}
}
