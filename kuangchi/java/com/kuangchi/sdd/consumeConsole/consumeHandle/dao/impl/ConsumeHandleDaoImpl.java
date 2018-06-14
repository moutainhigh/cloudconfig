package com.kuangchi.sdd.consumeConsole.consumeHandle.dao.impl;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.consumeHandle.dao.IConsumeHandleDao;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.Account;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.DeptConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.GoodConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.MealConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.PersonConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.RecordPackDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.TerminalConsumeDetail;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;

@Repository("consumeHandleDaoImpl")
public class ConsumeHandleDaoImpl extends BaseDaoImpl<Object> implements IConsumeHandleDao {

	@Override
	public String getNameSpace() {
		return "consume.ConsumeHandle";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean updateBalance(String account_num, Double balance) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_num", account_num);
		map.put("account_balance", balance);
		return this.update("updateBalance", map);
	}
	
	@Override
	public boolean addConsumeRecord(Map<String,Object> map) {
		return this.insert("addConsumeRecord", map);
	}

	@Override
	public Map<String, Object> getRecordDetial(Map<String, Object> map) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("getRecordDetial", map);
	}
	

	@Override
	public GoodConsumeDetail getGoodDetailByNum(String goodNum, String everyDate, Double price) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_num", goodNum);
		map.put("every_date", everyDate);
		map.put("price", price);
		return (GoodConsumeDetail) this.getSqlMapClientTemplate().queryForObject("getGoodDetailByNum", map);
	}

	@Override
	public GoodConsumeDetail getGoodTypeDetailByNum(String typeNum,
			String everyDate, Double typePrice) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_type_num", typeNum);
		map.put("every_date", everyDate);
		map.put("price", typePrice);
		return (GoodConsumeDetail) this.getSqlMapClientTemplate().queryForObject("getGoodTypeDetailByNum", map);
	}
	
	@Override
	public DeptConsumeDetail getDeptDetailByNum(String deptNum, String everyDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptNum);
		map.put("every_date", everyDate);
		return (DeptConsumeDetail) this.getSqlMapClientTemplate().queryForObject("getDeptDetailByNum", map);
	}

	@Override
	public PersonConsumeDetail getPersonDetailByNum(String staffNum, String everyDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_num", staffNum);
		map.put("every_date", everyDate);
		return (PersonConsumeDetail) this.getSqlMapClientTemplate().queryForObject("getPersonDetailByNum", map);
	}

	@Override
	public TerminalConsumeDetail getTerminalDetailByNum(String deviceNum, String everyDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", deviceNum);
		map.put("every_date", everyDate);
		return (TerminalConsumeDetail) this.getSqlMapClientTemplate().queryForObject("getTerminalDetailByNum", map);
	}

	@Override
	public MealConsumeDetail getMealDetailByNum(String mealNum, String everyDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("meal_num", mealNum);
		map.put("every_date", everyDate);
		return (MealConsumeDetail) this.getSqlMapClientTemplate().queryForObject("getMealDetailByNum", map);
	}

	@Override
	public boolean addGoodDetail(GoodConsumeDetail goodConsumeDetail) {
		return this.insert("addGoodDetail", goodConsumeDetail);
	}

	@Override
	public boolean updateGoodDetail(GoodConsumeDetail goodConsumeDetail) {
		return this.update("updateGoodDetail", goodConsumeDetail);
	}

	@Override
	public boolean addDeptDetail(DeptConsumeDetail deptConsumeDetail) {
		return this.insert("addDeptDetail", deptConsumeDetail);
	}

	@Override
	public boolean updateDeptDetail(DeptConsumeDetail deptConsumeDetail) {
		return this.insert("updateDeptDetail", deptConsumeDetail);
	}

	@Override
	public boolean addPersonDetail(PersonConsumeDetail personConsumeDetail) {
		return this.insert("addPersonDetail", personConsumeDetail);
	}

	@Override
	public boolean updatePersonDetail(PersonConsumeDetail personConsumeDetail) {
		return this.update("updatePersonDetail", personConsumeDetail);
	}

	@Override
	public boolean addTerminalDetail(TerminalConsumeDetail terminalConsumeDetail) {
		return this.insert("addTerminalDetail", terminalConsumeDetail);
	}

	@Override
	public boolean updateTerminalDetail(TerminalConsumeDetail terminalConsumeDetail) {
		return this.update("updateTerminalDetail", terminalConsumeDetail);
	}

	@Override
	public boolean addMealDetail(MealConsumeDetail mealConsumeDetail) {
		return this.insert("addMealDetail", mealConsumeDetail);
	}

	@Override
	public boolean updateMealDetail(MealConsumeDetail mealConsumeDetail) {
		return this.update("updateMealDetail", mealConsumeDetail);
	}
	@Override
	public boolean addAccountDetail(Map<String,Object> map) {
		return this.insert("addAccountDetail", map);
	}

	@Override
	public Account getAccountByCard(Map<String, Object> map) {
		return (Account) this.getSqlMapClientTemplate().queryForObject("getAccountByCard", map);
	}

	@Override
	public Integer getStaffLimit(String staffNum, String mealNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("mealNum", mealNum);
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getStaffLimit", map);
	}
	
	@Override
	public Integer getDeviceLimit(String deviceNum, String mealNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deviceNum", deviceNum);
		map.put("mealNum", mealNum);
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getDeviceLimit", map);
	}

	@Override
	public Integer getStaffConsumeCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getStaffConsumeCount", map);
	}

	@Override
	public Integer getDeviceConsumeCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getDeviceConsumeCount", map);
	}
	
	@Override
	public MealModel getMealByTime(String consumeTime) {
		return (MealModel) this.getSqlMapClientTemplate().queryForObject("getMealByTime", consumeTime);
	}

	@Override
	public boolean updatePrice(String goodNum, Double price) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("goodNum", goodNum);
		map.put("price", price);
		return this.update("updatePrice", map);
	}

	@Override
	public boolean updateTypePrice(String typeNum, Double price) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("typeNum", typeNum);
		map.put("price", price);
		return this.update("updateTypePrice", map);
	}

	@Override
	public boolean addPriceHistory(String goodNum, String isType, Double oldPrice, Double newPrice) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("goodNum", goodNum);
		map.put("isType", isType);
		map.put("oldPrice", oldPrice);
		map.put("newPrice", newPrice);
		return this.insert("addPriceHistory", map);
	}

	@Override
	public Map<String, Object> getGoodByNum(Map<String, Object> map) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("getBoundGoodByNum", map);
	}

	@Override
	public Map<String, Object> getTypeByNum(Map<String, Object> map) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("getBoundTypeByNum", map);
	}

	@Override
	public Integer getIsSupport(String cardNum) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getIsSupport", cardNum);
	}

	/*@Override
	public Integer getBoundGoodOrType(String deviceNum, String consumeDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", deviceNum);
		map.put("consume_date", consumeDate);
		Integer goodCount = (Integer) this.getSqlMapClientTemplate().queryForObject("getBoundGood", map);
		Integer typeCount = (Integer) this.getSqlMapClientTemplate().queryForObject("getBoundType", map);
		if(goodCount > 0 || typeCount > 0){
			return 1;
		} else {
			return 0;
		}
	}*/

	@Override
	public Map<String, Object> getLastRecord(String deviceNum) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("getLastRecord", deviceNum);
	}

	@Override
	public boolean updateCancelRecord(String record_flow_num) {
		return this.update("updateCancelRecord", record_flow_num);
	}

	@Override
	public Integer getDevice(String deviceNum) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getDevice", deviceNum);
	}

	@Override
	public Integer getCard(String cardNum) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getCard", cardNum);
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
	public boolean updatePersonCancelTotal(PersonConsumeDetail personConsumeDetail) {
		return this.update("updatePersonCancelTotal", personConsumeDetail);
	}

	@Override
	public boolean updateTerminalCancelTotal(TerminalConsumeDetail terminalConsumeDetail) {
		return this.update("updateTerminalCancelTotal", terminalConsumeDetail);
	}

	@Override
	public Map<String, Object> getGroupByTime(Map map) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("getGroupByTime", map);
	}
	
	@Override
	public Map<String, Object> getAccountByAccountNum(String accountNum) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("getAccountByAccountNum", accountNum);
	}
	
	@Override
	public boolean addAccountDetail2(RecordPackDetail recordPackDetail){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("account_num", recordPackDetail.getAccount_num());
		map.put("detail_flow_no", recordPackDetail.getRecord_flow_num());
		map.put("time", recordPackDetail.getConsume_time());
		
		map.put("staff_no", recordPackDetail.getStaff_no());
		map.put("staff_num", recordPackDetail.getStaff_num());
		map.put("staff_name", recordPackDetail.getStaff_name());
		map.put("dept_no", recordPackDetail.getDept_no());
		map.put("dept_num", recordPackDetail.getDept_num());
		map.put("dept_name", recordPackDetail.getDept_name());
		
		map.put("inbound", recordPackDetail.getInbound());
		map.put("outbound", recordPackDetail.getOutbound());
		map.put("balance", recordPackDetail.getAccount_balance());
		map.put("dept_name", recordPackDetail.getDept_name());
		if ("XF".equals(recordPackDetail.getRecord_type())){
			map.put("type", 1);
		} else if ("CX".equals(recordPackDetail.getRecord_type())){
			map.put("type", 4);
		}
		return this.insert("addAccountDetail", map);
	}
	
	@Override
	public boolean addConsumeRecord2(RecordPackDetail recordPackDetail){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("card_flow_no", recordPackDetail.getCard_flow_no());
		map.put("card_num", recordPackDetail.getCard_num());
		map.put("record_flow_num", recordPackDetail.getRecord_flow_num()); 
		map.put("record_flow_no", recordPackDetail.getRecord_flow_no());
		map.put("account_num", recordPackDetail.getAccount_num());
		
		map.put("consume_time", recordPackDetail.getConsume_time());
		map.put("meal_num", recordPackDetail.getMeal_num());
		map.put("meal_name", recordPackDetail.getMeal_name());
		map.put("meal_start_time", recordPackDetail.getMeal_start_time());
		map.put("meal_end_time", recordPackDetail.getMeal_end_time());
		
		map.put("staff_no", recordPackDetail.getStaff_no());
		map.put("staff_num", recordPackDetail.getStaff_num());
		map.put("staff_name", recordPackDetail.getStaff_name());
		map.put("dept_no", recordPackDetail.getDept_no());
		map.put("dept_num", recordPackDetail.getDept_num());
		map.put("dept_name", recordPackDetail.getDept_name());
		map.put("group_num", recordPackDetail.getGroup_num());
		map.put("group_name", recordPackDetail.getGroup_name());
		
		map.put("device_num", recordPackDetail.getDevice_num());
		map.put("device_name", recordPackDetail.getDevice_name());
		map.put("vendor_num", recordPackDetail.getVendor_num());
		map.put("vendor_name", recordPackDetail.getVendor_name());
		map.put("vendor_dealer_name", recordPackDetail.getVendor_dealer_name());
		
		map.put("good_type_num", recordPackDetail.getType_num());
		map.put("good_type_name", recordPackDetail.getType_name());
		map.put("good_type_price", recordPackDetail.getType_price()); 
		if("0".equals(recordPackDetail.getIs_type())){
			map.put("good_num", recordPackDetail.getGood_num());
			map.put("good_name", recordPackDetail.getGood_name());
			map.put("good_price", recordPackDetail.getPrice());
		} 
		
		map.put("balance", recordPackDetail.getAccount_balance());
		map.put("inbound", recordPackDetail.getInbound());
		map.put("outbound", recordPackDetail.getOutbound());
		if ("XF".equals(recordPackDetail.getRecord_type())){
			map.put("type", 1);
			map.put("refund", 0);
			map.put("isCancel", 0);
		} else if ("CX".equals(recordPackDetail.getRecord_type())){
			map.put("type", 2);
		}
		map.put("create_time", recordPackDetail.getCreate_time());
		return this.insert("addConsumeRecord", map);
	}
	
	@Override
	public boolean countMealCounsume2(RecordPackDetail recordPackDetail){
		MealConsumeDetail existMealDetail = getMealDetailByNum(
				recordPackDetail.getMeal_num(), recordPackDetail.getConsume_date());
		
		// 如果为消费记录则进行餐次统计；如果为撤销消费记录则更改撤销总额
		if ("XF".equals(recordPackDetail.getRecord_type())){
			Integer consumeAmount =  recordPackDetail.getConsume_amount();
			Double actualConsumeSum = recordPackDetail.getActual_consume_sum();
			Double consumeSum = recordPackDetail.getConsume_sum();
			
			if(existMealDetail != null){
				MealConsumeDetail mealDetail = existMealDetail;
				mealDetail.setAmount(existMealDetail.getAmount() + consumeAmount);
				mealDetail.setMoney(existMealDetail.getMoney() + consumeSum);
				mealDetail.setAfter_discount_money(existMealDetail.getAfter_discount_money() + actualConsumeSum);
				return this.update("updateMealDetail", mealDetail);
			} else {
				MealConsumeDetail mealDetail = new MealConsumeDetail();
				mealDetail.setMeal_num(recordPackDetail.getMeal_num());
				mealDetail.setMeal_name(recordPackDetail.getMeal_name());
				mealDetail.setEvery_date(recordPackDetail.getConsume_date());
				
				mealDetail.setAmount(consumeAmount);
				mealDetail.setMoney(consumeSum);
				mealDetail.setAfter_discount_money(actualConsumeSum);
				return this.insert("addMealDetail", mealDetail);
			}
			
		}else if("CX".equals(recordPackDetail.getRecord_type())){
			existMealDetail.setCancel_total(
					existMealDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			return this.update("updateMealCancelTotal", existMealDetail);
		}else {
			return false;
		}
	}
	
	@Override
	public boolean countGoodConsume2(RecordPackDetail recordPackDetail){
		// 查询已存在记录
		GoodConsumeDetail existGoodDetail = null;
		
		String consumeDate = recordPackDetail.getConsume_date();
		Double newPrice = null;
		if("0".equals(recordPackDetail.getIs_type())){
			String goodNum = recordPackDetail.getGood_num();
			newPrice = ("XF".equals(recordPackDetail.getRecord_type())? 
					Math.abs(recordPackDetail.getConsume_sum()):recordPackDetail.getPrice()); // 若不是消费操作，价格不变，取原始价格进行判断
			existGoodDetail = getGoodDetailByNum(goodNum, consumeDate, newPrice);
		
		} else if("1".equals(recordPackDetail.getIs_type())){
			String typeNum = recordPackDetail.getType_num();
			newPrice = ("XF".equals(recordPackDetail.getRecord_type())? 
					Math.abs(recordPackDetail.getConsume_sum()):recordPackDetail.getType_price());
			existGoodDetail = getGoodTypeDetailByNum(typeNum, consumeDate, newPrice);
		}
		
		// 如果 为消费记录则进行餐次统计；如果为撤销消费记录则更改撤销总额
		if ("XF".equals(recordPackDetail.getRecord_type())){
			String meal_num = recordPackDetail.getMeal_num();
			Double consumeSum = recordPackDetail.getConsume_sum();
			Double actualConsumeSum = recordPackDetail.getActual_consume_sum();
			Integer consumeAmount =  recordPackDetail.getConsume_amount();
			Integer goodAmount = recordPackDetail.getGood_amount();
			
			if(existGoodDetail != null){
				GoodConsumeDetail goodDetail = existGoodDetail;
				if("1".equals(meal_num)){
					goodDetail.setMeal1_amount(existGoodDetail.getMeal1_amount() +consumeAmount);
					goodDetail.setMeal1_money(existGoodDetail.getMeal1_money() + consumeSum);
				}
				if("2".equals(meal_num)){
					goodDetail.setMeal2_amount(existGoodDetail.getMeal2_amount() +consumeAmount);
					goodDetail.setMeal2_money(existGoodDetail.getMeal2_money() + consumeSum);
				}
				if("3".equals(meal_num)){
					goodDetail.setMeal3_amount(existGoodDetail.getMeal3_amount() +consumeAmount);
					goodDetail.setMeal3_money(existGoodDetail.getMeal3_money() + consumeSum);
				}
				if("4".equals(meal_num)){
					goodDetail.setMeal4_amount(existGoodDetail.getMeal4_amount() +consumeAmount);
					goodDetail.setMeal4_money(existGoodDetail.getMeal4_money() + consumeSum);
				}
				if("5".equals(meal_num)){
					goodDetail.setMeal5_amount(existGoodDetail.getMeal5_amount() +consumeAmount);
					goodDetail.setMeal5_money(existGoodDetail.getMeal5_money() + consumeSum);
				}
				goodDetail.setAmount(existGoodDetail.getAmount() + goodAmount);
				goodDetail.setTotal(existGoodDetail.getTotal() + consumeSum); 
				goodDetail.setAfter_discount_money(existGoodDetail.getAfter_discount_money() + actualConsumeSum); 
				return this.update("updateGoodDetail", goodDetail);
				
			} else {
				GoodConsumeDetail goodDetail = new GoodConsumeDetail();
				goodDetail.setDevice_num(recordPackDetail.getDevice_num());
				goodDetail.setDevice_name(recordPackDetail.getDevice_name());
				goodDetail.setGood_num(recordPackDetail.getGood_num());
				goodDetail.setGood_name(recordPackDetail.getGood_name());
				goodDetail.setGood_type_num(recordPackDetail.getType_num());
				goodDetail.setGood_type_name(recordPackDetail.getType_name());
				goodDetail.setPrice(newPrice);
				goodDetail.setEvery_date(recordPackDetail.getConsume_date());
				goodDetail.setVendor_num(recordPackDetail.getVendor_num());
				goodDetail.setVendor_name(recordPackDetail.getVendor_num());
				goodDetail.setVendor_dealer_name(recordPackDetail.getVendor_dealer_name());
				
				if("1".equals(meal_num)){
					goodDetail.setMeal1_amount(consumeAmount);
					goodDetail.setMeal1_money(consumeSum);
				}
				if("2".equals(meal_num)){
					goodDetail.setMeal2_amount(consumeAmount);
					goodDetail.setMeal2_money(consumeSum);
				}
				if("3".equals(meal_num)){
					goodDetail.setMeal3_amount(consumeAmount);
					goodDetail.setMeal3_money(consumeSum);
				}
				if("4".equals(meal_num)){
					goodDetail.setMeal4_amount(consumeAmount);
					goodDetail.setMeal4_money(consumeSum);
				}
				if("5".equals(meal_num)){
					goodDetail.setMeal5_amount(consumeAmount);
					goodDetail.setMeal5_money(consumeSum);
				}
				goodDetail.setAmount(goodAmount);
				goodDetail.setTotal(consumeSum); 
				goodDetail.setAfter_discount_money(actualConsumeSum); 
				return this.insert("addGoodDetail", goodDetail);
			}
			
		} else if("CX".equals(recordPackDetail.getRecord_type())){
			existGoodDetail.setCancel_total(
					existGoodDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			return this.update("updateGoodCancelTotal", existGoodDetail);
		}else {
			return false;
		}
	}
	
	@Override
	public boolean countPersonCousume2(RecordPackDetail recordPackDetail){
		PersonConsumeDetail existPersonDetail = getPersonDetailByNum(
				recordPackDetail.getStaff_num(), recordPackDetail.getConsume_date());
		
		// 如果 为消费记录则进行餐次统计；如果为撤销消费记录则更改撤销总额
		if ("XF".equals(recordPackDetail.getRecord_type())){
			String meal_num = recordPackDetail.getMeal_num();
			Double consumeSum = recordPackDetail.getConsume_sum();
			Double actualConsumeSum = recordPackDetail.getActual_consume_sum();
			Integer consumeAmount =  recordPackDetail.getConsume_amount();
			
			if(existPersonDetail != null){
				PersonConsumeDetail personDetail = existPersonDetail;
				
				if("1".equals(meal_num)){
					personDetail.setMeal1_amount(existPersonDetail.getMeal1_amount() + consumeAmount);
					personDetail.setMeal1_money(existPersonDetail.getMeal1_money() + consumeSum);
				}
				if("2".equals(meal_num)){
					personDetail.setMeal2_amount(existPersonDetail.getMeal2_amount() + consumeAmount);
					personDetail.setMeal2_money(existPersonDetail.getMeal2_money() + consumeSum);
				}
				if("3".equals(meal_num)){
					personDetail.setMeal3_amount(existPersonDetail.getMeal3_amount() + consumeAmount);
					personDetail.setMeal3_money(existPersonDetail.getMeal3_money() + consumeSum);
				}
				if("4".equals(meal_num)){
					personDetail.setMeal4_amount(existPersonDetail.getMeal4_amount() + consumeAmount);
					personDetail.setMeal4_money(existPersonDetail.getMeal4_money() + consumeSum);
				}
				if("5".equals(meal_num)){
					personDetail.setMeal5_amount(existPersonDetail.getMeal5_amount() + consumeAmount);
					personDetail.setMeal5_money(existPersonDetail.getMeal5_money() + consumeSum);
				}
				personDetail.setAfter_discount_money(existPersonDetail.getAfter_discount_money() + actualConsumeSum);
				personDetail.setMoney(existPersonDetail.getMoney() + consumeSum);
				return this.update("updatePersonDetail", personDetail);
				
			} else {
				PersonConsumeDetail personDetail = new PersonConsumeDetail();
				personDetail.setDept_num(recordPackDetail.getDept_num());
				personDetail.setDept_name(recordPackDetail.getDept_name());
				personDetail.setStaff_no(recordPackDetail.getStaff_no());
				personDetail.setDept_no(recordPackDetail.getDept_no());
				personDetail.setStaff_num(recordPackDetail.getStaff_num());
				personDetail.setStaff_name(recordPackDetail.getStaff_name());
				personDetail.setEvery_date(recordPackDetail.getConsume_date());
				
				if("1".equals(meal_num)){
					personDetail.setMeal1_amount(consumeAmount);
					personDetail.setMeal1_money(consumeSum);
				}
				if("2".equals(meal_num)){
					personDetail.setMeal2_amount(consumeAmount);
					personDetail.setMeal2_money(consumeSum);
				}
				if("3".equals(meal_num)){
					personDetail.setMeal3_amount(consumeAmount);
					personDetail.setMeal3_money(consumeSum);
				}
				if("4".equals(meal_num)){
					personDetail.setMeal4_amount(consumeAmount);
					personDetail.setMeal4_money(consumeSum);
				}
				if("5".equals(meal_num)){
					personDetail.setMeal5_amount(consumeAmount);
					personDetail.setMeal5_money(consumeSum);
				}
				personDetail.setAfter_discount_money(actualConsumeSum);
				personDetail.setMoney(consumeSum);
				return this.insert("addPersonDetail", personDetail);
			}
			
		} else if("CX".equals(recordPackDetail.getRecord_type())){
			existPersonDetail.setCancel_total(
					existPersonDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			return this.update("updatePersonCancelTotal", existPersonDetail);
		}else {
			return false;
		}
	}
	
	@Override
	public boolean countDeptCousume2(RecordPackDetail recordPackDetail){
		DeptConsumeDetail existDeptDetail = getDeptDetailByNum(
				recordPackDetail.getDept_num(), recordPackDetail.getConsume_date());
		
		// 如果 为消费记录则进行餐次统计；如果为撤销消费记录则更改撤销总额
		if ("XF".equals(recordPackDetail.getRecord_type())){
			String meal_num = recordPackDetail.getMeal_num();
			Double consumeSum = recordPackDetail.getConsume_sum();
			Double actualConsumeSum = recordPackDetail.getActual_consume_sum();
			Integer consumeAmount =  recordPackDetail.getConsume_amount();
			
			if(existDeptDetail != null){
				DeptConsumeDetail deptDetail = existDeptDetail;
				deptDetail.setAfter_discount_money(existDeptDetail.getAfter_discount_money() + actualConsumeSum);
				deptDetail.setMoney(existDeptDetail.getMoney() + consumeSum);
				
				if("1".equals(meal_num)){
					deptDetail.setMeal1_amount(existDeptDetail.getMeal1_amount() + consumeAmount);
					deptDetail.setMeal1_money(existDeptDetail.getMeal1_money() + consumeSum);
				}
				if("2".equals(meal_num)){
					deptDetail.setMeal2_amount(existDeptDetail.getMeal2_amount() + consumeAmount);
					deptDetail.setMeal2_money(existDeptDetail.getMeal2_money() + consumeSum);
				}
				if("3".equals(meal_num)){
					deptDetail.setMeal3_amount(existDeptDetail.getMeal3_amount() + consumeAmount);
					deptDetail.setMeal3_money(existDeptDetail.getMeal3_money() + consumeSum);
				}
				if("4".equals(meal_num)){
					deptDetail.setMeal4_amount(existDeptDetail.getMeal4_amount() + consumeAmount);
					deptDetail.setMeal4_money(existDeptDetail.getMeal4_money() + consumeSum);
				}
				if("5".equals(meal_num)){
					deptDetail.setMeal5_amount(existDeptDetail.getMeal5_amount() + consumeAmount);
					deptDetail.setMeal5_money(existDeptDetail.getMeal5_money() + consumeSum);
				}
				return this.insert("updateDeptDetail", deptDetail);
				
			} else {
				DeptConsumeDetail deptDetail = new DeptConsumeDetail();
				deptDetail.setDept_num(recordPackDetail.getDept_num());
				deptDetail.setDept_name(recordPackDetail.getDept_name());
				deptDetail.setDept_no(recordPackDetail.getDept_no());
				deptDetail.setEvery_date(recordPackDetail.getConsume_date());
				deptDetail.setAfter_discount_money(actualConsumeSum);
				deptDetail.setMoney(consumeSum);
				
				if("1".equals(meal_num)){
					deptDetail.setMeal1_amount(consumeAmount);
					deptDetail.setMeal1_money(consumeSum);
				}
				if("2".equals(meal_num)){
					deptDetail.setMeal2_amount(consumeAmount);
					deptDetail.setMeal2_money(consumeSum);
				}
				if("3".equals(meal_num)){
					deptDetail.setMeal3_amount(consumeAmount);
					deptDetail.setMeal3_money(consumeSum);
				}
				if("4".equals(meal_num)){
					deptDetail.setMeal4_amount(consumeAmount);
					deptDetail.setMeal4_money(consumeSum);
				}
				if("5".equals(meal_num)){
					deptDetail.setMeal5_amount(consumeAmount);
					deptDetail.setMeal5_money(consumeSum);
				}
				return this.insert("addDeptDetail", deptDetail);
			}
			
		} else if("CX".equals(recordPackDetail.getRecord_type())){
			existDeptDetail.setCancel_total(
					existDeptDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			return this.update("updateDeptCancelTotal", existDeptDetail);
		} else{
			return false;
		}
	}
	
	@Override
	public boolean countTerminalCousume2(RecordPackDetail recordPackDetail){
		TerminalConsumeDetail existTerminalDetail = getTerminalDetailByNum(
				recordPackDetail.getDevice_num(), recordPackDetail.getConsume_date());
		
		// 如果 为消费记录则进行餐次统计；如果为撤销消费记录则更改撤销总额
		if ("XF".equals(recordPackDetail.getRecord_type())){
			String meal_num = recordPackDetail.getMeal_num();
			Double consumeSum = recordPackDetail.getConsume_sum();
			Double actualConsumeSum = recordPackDetail.getActual_consume_sum();
			Integer consumeAmount =  recordPackDetail.getConsume_amount();
			
			if(existTerminalDetail != null){
				TerminalConsumeDetail terminalDetail = existTerminalDetail;
				terminalDetail.setAfter_discount_money(existTerminalDetail.getAfter_discount_money() + actualConsumeSum);
				terminalDetail.setMoney(existTerminalDetail.getMoney() + consumeSum);
				
				if("1".equals(meal_num)){
					terminalDetail.setMeal1_amount(existTerminalDetail.getMeal1_amount() + consumeAmount);
					terminalDetail.setMeal1_money(existTerminalDetail.getMeal1_money() + consumeSum);
				}
				if("2".equals(meal_num)){
					terminalDetail.setMeal2_amount(existTerminalDetail.getMeal2_amount() + consumeAmount);
					terminalDetail.setMeal2_money(existTerminalDetail.getMeal2_money() + consumeSum);
				}
				if("3".equals(meal_num)){
					terminalDetail.setMeal3_amount(existTerminalDetail.getMeal3_amount() + consumeAmount);
					terminalDetail.setMeal3_money(existTerminalDetail.getMeal3_money() + consumeSum);
				}
				if("4".equals(meal_num)){
					terminalDetail.setMeal4_amount(existTerminalDetail.getMeal4_amount() + consumeAmount);
					terminalDetail.setMeal4_money(existTerminalDetail.getMeal4_money() + consumeSum);
				}
				if("5".equals(meal_num)){
					terminalDetail.setMeal5_amount(existTerminalDetail.getMeal5_amount() + consumeAmount);
					terminalDetail.setMeal5_money(existTerminalDetail.getMeal5_money() + consumeSum);
				}
				return this.update("updateTerminalDetail", terminalDetail);
				
			} else {
				TerminalConsumeDetail terminalDetail = new TerminalConsumeDetail();
				terminalDetail.setDevice_num(recordPackDetail.getDevice_num());
				terminalDetail.setDevice_name(recordPackDetail.getDevice_name());
				terminalDetail.setGroup_name(recordPackDetail.getGroup_name());
				terminalDetail.setVendor_num(recordPackDetail.getVendor_num());
				terminalDetail.setVendor_name(recordPackDetail.getVendor_name());
				terminalDetail.setVendor_dealer_name(recordPackDetail.getVendor_dealer_name());
				terminalDetail.setEvery_date(recordPackDetail.getConsume_date());
				terminalDetail.setAfter_discount_money(actualConsumeSum);
				terminalDetail.setMoney(consumeSum);
				
				if("1".equals(meal_num)){
					terminalDetail.setMeal1_amount(consumeAmount);
					terminalDetail.setMeal1_money(consumeSum);
				}
				if("2".equals(meal_num)){
					terminalDetail.setMeal2_amount(consumeAmount);
					terminalDetail.setMeal2_money(consumeSum);
				}
				if("3".equals(meal_num)){
					terminalDetail.setMeal3_amount(consumeAmount);
					terminalDetail.setMeal3_money(consumeSum);
				}
				if("4".equals(meal_num)){
					terminalDetail.setMeal4_amount(consumeAmount);
					terminalDetail.setMeal4_money(consumeSum);
				}
				if("5".equals(meal_num)){
					terminalDetail.setMeal5_amount(consumeAmount);
					terminalDetail.setMeal5_money(consumeSum);
				}
				return this.insert("addTerminalDetail", terminalDetail);
			}
			
		} else if("CX".equals(recordPackDetail.getRecord_type())){
			existTerminalDetail.setCancel_total(
					existTerminalDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			return this.update("updateTerminalCancelTotal", existTerminalDetail);
		}else {
			return false;
		}
	}
	
	@Override
	public RecordPackDetail deductAmount2(RecordPackDetail recordPackDetail){
		Double consumeSum = recordPackDetail.getConsume_sum();
		Double balance = recordPackDetail.getAccount_balance();	
		
		// 根据折扣率计算实际消费总额
		Double discountRate = recordPackDetail.getDiscount_rate();
		if(discountRate != null && discountRate !=0 ){
			consumeSum = new BigDecimal(consumeSum * discountRate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		
		// 若余额足够，则扣取余额
		String account_num = recordPackDetail.getAccount_num();
		Double deductBalance = balance-consumeSum;
		updateBalance(account_num, deductBalance); 
		
		// 设置变量 recordPackDetail 的余额和实际消费总额
		recordPackDetail.setActual_consume_sum(consumeSum);
		recordPackDetail.setAccount_balance(deductBalance);
		recordPackDetail.setOutbound(consumeSum);
		recordPackDetail.setInbound(0.0);
		return recordPackDetail;
	}
	
	@Override
	public RecordPackDetail updatePrice2(RecordPackDetail recordPackDetail){
		/* 根据isType判断设备关联类型
		       若关联商品，则判断商品价格是否变动；若关联商品类型，则判断类型价格是否变动
		       若价格变动，则更新价格并将旧价格存入历史记录；否则不做任何操作*/
		
		String isType = recordPackDetail.getIs_type();
		Double consumeSum = recordPackDetail.getConsume_sum();
		Double price = recordPackDetail.getPrice();
		Double typePrice = recordPackDetail.getType_price();
		
		if("0".equals(isType) && !consumeSum.equals(price)){
			String goodNum = recordPackDetail.getGood_num();
			updatePrice(goodNum, consumeSum);
			addPriceHistory(goodNum, isType, price, consumeSum);
			recordPackDetail.setPrice(consumeSum);
		} else if ("1".equals(isType) && !consumeSum.equals(typePrice)){
			String typeNum = recordPackDetail.getType_num();
			updateTypePrice(typeNum, consumeSum);
			addPriceHistory(typeNum, isType, typePrice, consumeSum);
			recordPackDetail.setType_price(consumeSum); 
		}
		return recordPackDetail;
	}
	
	@Override
	public RecordPackDetail addBalance2(RecordPackDetail recordPackDetail){
		Double outbound = recordPackDetail.getOutbound(); 
		Double balance = recordPackDetail.getAccount_balance();	
		
		String account_num = recordPackDetail.getAccount_num();
		Double newBalance = balance + outbound;
		updateBalance(account_num, newBalance); 
		
		// 设置变量 recordPackDetail 的余额和实际消费总额
		/*recordPackDetail.setActual_consume_sum(outbound*-1);*/
		recordPackDetail.setAccount_balance(newBalance);
		recordPackDetail.setOutbound(0.0);
		recordPackDetail.setInbound(outbound);
		return recordPackDetail;
	}
	
	@Override
	public boolean updateCancelRecord2(String deviceNum){
		String record_flow_num = (String) getLastRecord(deviceNum).get("record_flow_num");
		return this.update("updateCancelRecord", record_flow_num);
	}
	
	@Override
	public List<String> getSameTypeDevice(String device_num){
		return this.getSqlMapClientTemplate().queryForList("getSameTypeDevice", device_num);
	}
	

}
