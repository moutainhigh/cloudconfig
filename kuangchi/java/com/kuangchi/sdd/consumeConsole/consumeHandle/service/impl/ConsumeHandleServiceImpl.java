package com.kuangchi.sdd.consumeConsole.consumeHandle.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.accountRelevant.service.AccountRelevantService;
import com.kuangchi.sdd.consumeConsole.consumeHandle.dao.IConsumeHandleDao;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.Account;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.ConsumeRecordPack;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.DepositDown;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.DeptConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.GoodConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.JsonResult;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.MealConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.PersonConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.RecordPackDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.TerminalConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.service.IConsumeHandleService;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;


/**
 * 消费上报 Service
 * @author yuman.gao
 */
@Transactional
@Service("consumeHandleServiceImpl")
public class ConsumeHandleServiceImpl implements IConsumeHandleService{

	@Resource(name = "consumeHandleDaoImpl")
	private IConsumeHandleDao consumeHandleDao;
	
	@Resource(name = "accountRelevantService")
	private AccountRelevantService accountRelevantService;
	
	@Resource(name = "cDeviceService")
	private IDeviceService deviceService;
	
	
	@Override
	public JsonResult recordReport(ConsumeRecordPack recordPack){
		JsonResult result = new JsonResult();
		try {
			RecordPackDetail recordPackDetail = setRecordPack(recordPack);
			int type = recordPack.getRecordType();
			if(12 == type){
				result = cancelConsume(recordPackDetail);
			} else if(20 == type){
				result = doConsume(recordPackDetail);
			} 
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("操作失败");
			result.setSuccess(false);
			result.setAlm(01);
			return result;
		}
	}

	@Override
	public DepositDown issuedBalance(String cardNum, String deviceNum) {
		DepositDown deposit = new DepositDown();
		
		/* 设置报警信号:
		 * 设备不存在，报警01
		 * 卡不存在（非法卡） ，报警02
		 * 该卡不支持消费，报警03；
		 * 无绑定账户，报警04；
		 * 账号冻结，报警05；
		 * 员工消费超次，报警06；
		 * 设备消费超次，报警07；
		 * 若无异常，报警00 */
		Integer alm ;
		Integer device = consumeHandleDao.getDevice(deviceNum);
		Integer card = consumeHandleDao.getCard(cardNum);
		if(device <= 0){
			alm = 01;
		} else if(card <= 0){
			alm = 02;
		} else {
			Integer supportCard = consumeHandleDao.getIsSupport(cardNum);
			if(supportCard <= 0){
				alm = 03;
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("card_num", cardNum);
				map.put("device_num", deviceNum);
				Account account = consumeHandleDao.getAccountByCard(map);
				if(account == null){
					alm = 04;
				} else {
					if("2".equals(account.getAccount_state())){
						alm = 05;
					} else {
						// 根据时间、员工、设备分别查询限次信息
						String consumeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						String day = consumeTime.split(" ")[0];
						String time = consumeTime.split(" ")[1];
						
						MealModel meal = consumeHandleDao.getMealByTime(time);
						String mealNum = (meal == null? "5":meal.getMeal_num());
						String staffNum = account.getStaff_num();
						String startTime = day + " 00:00:00";
						String endTime = day + " 23:59:59";
						
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("start_time", startTime);
						param.put("end_time", endTime);
						param.put("staff_num", staffNum);
						param.put("meal_num", mealNum);
						param.put("device_num", deviceNum);
						
						Integer staffConsumeCount = consumeHandleDao.getStaffConsumeCount(param); // 员工当天指定餐次消费次数
						Integer staffLimit = consumeHandleDao.getStaffLimit(staffNum,mealNum); // 员工消费限次
						
						// 比较限次信息和已消费记录：若员工超次，不进行下步判断；  否则判断设备是否超次
						if(staffLimit != null && staffLimit <= staffConsumeCount){
							alm = 06;
						} else {
							Integer deviceConsumeCount = consumeHandleDao.getDeviceConsumeCount(param); // 设备当天指定餐次消费次数
							Integer deviceLimit = consumeHandleDao.getDeviceLimit(deviceNum, mealNum); // 设备消费限次
							if(deviceLimit != null && deviceLimit <= deviceConsumeCount){
								alm = 07;
							} else {
								alm = 00;
							}
						} 
						if(account.getAccount_balance()<0){
							deposit.setBalance(Math.floor(0.0));
						}else {
							deposit.setBalance(Math.floor(account.getAccount_balance()*100));
						}
						deposit.setNum(account.getStaff_no());
						deposit.setName(account.getStaff_name());
					}
				}
			}
		}
		deposit.setNum(deposit.getNum()==null?"0":deposit.getNum());
		deposit.setName(deposit.getName()==null?"0":deposit.getName());
		deposit.setCardNum(Integer.parseInt(cardNum));
		deposit.setMachine(Integer.parseInt(deviceNum));
		deposit.setPassword("00");
		deposit.setLimit(00);
		deposit.setAlm(alm);
		return deposit;
	}


	/**
	 * 根据记录包查询该消费记录详情
	 * @author yuman.gao
	 */
	public RecordPackDetail setRecordPack(ConsumeRecordPack recordPack){
		
		// 获取基本参数
		String recordNum = Integer.toString(recordPack.getRecordNum());
		String recordType = Integer.toString((recordPack.getRecordType()));
		String cardFlowNum = Integer.toString(recordPack.getCardFlowNum());
		String cardNum = Long.toString(recordPack.getCardNum());
		String deviceNum = Integer.toString(recordPack.getMachine());
		Double consumeSum = recordPack.getConsumeSum()/100;
		
		Integer year = recordPack.getYear();
		Integer month = recordPack.getMonth();
		Integer day = recordPack.getDay();
		Integer hour = recordPack.getHour();
		Integer minute = recordPack.getMinute();
		Integer second = recordPack.getSecond();
		
		RecordPackDetail recordPackDetail = null;
		
		/* 
		 * 如果为消费操作，则根据参数封装数据； 如果为撤销消费操作，则查询该设备最新消费记录封装数据 
		 */
		if(recordPack.getRecordType() == 20){
			
			// 重置消费时间
			Calendar c = Calendar.getInstance();
			c.set(year, month-1, day, hour, minute ,second); 
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String consumeTime = format.format(c.getTime());
			
			
			// 查询记录详情
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("card_num", cardNum);
			map.put("device_num", deviceNum);
			map.put("consume_date", consumeTime.split(" ")[0]);
			/*// 根据当前时间查询餐次编号，用于筛选当前消费组
			MealModel meal = consumeHandleDao.getMealByTime(consumeTime.split(" ")[1]);
			if(meal != null){
				map.put("meal_num", meal.getMeal_num());
			} else {
				map.put("meal_num", 5);
			}
			*/
			
			Map<String, Object> detailMap = consumeHandleDao.getRecordDetial(map);
			recordPackDetail = GsonUtil.toBean(GsonUtil.toJson(detailMap), RecordPackDetail.class);
			
			if(recordPackDetail != null){
				// 设置已知参数
				recordPackDetail.setRecord_flow_no(recordNum);
				recordPackDetail.setCard_flow_no(cardFlowNum);
				recordPackDetail.setCard_num(cardNum);
				recordPackDetail.setRecord_type(recordType);
				recordPackDetail.setConsume_date(consumeTime.split(" ")[0]);
				recordPackDetail.setConsume_time(consumeTime);
				
				// 生成创建时间和流水号
				String timeStr = accountRelevantService.getNowTimeStr();
				String recordFlowNum = accountRelevantService.makeFlowNum("XF", timeStr, recordPackDetail.getAccount_num());
				recordPackDetail.setCreate_time(formatTime(timeStr));
				recordPackDetail.setRecord_flow_num(recordFlowNum);
				recordPackDetail.setRecord_type("XF");
				
				//设置本次记录消费次数、商品次数和消费金额 
				recordPackDetail.setConsume_amount(1);
				recordPackDetail.setGood_amount(1);
				recordPackDetail.setConsume_sum(consumeSum);
				
				// 根据消费时间设置餐次
				MealModel meal = consumeHandleDao.getMealByTime(consumeTime.split(" ")[1]);
				if(meal != null){
					recordPackDetail.setMeal_num(meal.getMeal_num());
					recordPackDetail.setMeal_name(meal.getMeal_name());
					recordPackDetail.setMeal_start_time(meal.getStart_date());
					recordPackDetail.setMeal_end_time(meal.getEnd_date());
				} else {
					recordPackDetail.setMeal_num("5");
					recordPackDetail.setMeal_name("餐次外");
					recordPackDetail.setMeal_start_time("00:00:00");
					recordPackDetail.setMeal_end_time("00:00:00");
				}
				
				// 设置消费组
//				String meal_num = recordPackDetail.getMeal_num();
				Map groupParam = new HashMap();
				groupParam.put("meal_num", recordPackDetail.getMeal_num());
				groupParam.put("staff_num", recordPackDetail.getStaff_num());
				Map groupResult = consumeHandleDao.getGroupByTime(groupParam);
				if(groupResult != null){
					recordPackDetail.setGroup_num((String)groupResult.get("group_num"));
					recordPackDetail.setGroup_name((String)groupResult.get("group_name"));
				} else {
					recordPackDetail.setGroup_num("0");
					recordPackDetail.setGroup_name("默认组");
				}
				
				// 设置设备绑定商品和折扣
				String isType = recordPackDetail.getIs_type();
				Map<String, Object> goodResult = null;
				if("0".equals(isType)){
					Map<String, Object> goodParam = new HashMap<String, Object>();
					goodParam.put("consume_date", recordPackDetail.getConsume_date());
					goodParam.put("consume_time", recordPackDetail.getConsume_time());
					goodParam.put("good_num", recordPackDetail.getGood_or_type_num());
					goodResult = consumeHandleDao.getGoodByNum(goodParam);
				} else if("1".equals(isType)){
					Map<String, Object> typeParam = new HashMap<String, Object>();
					typeParam.put("consume_date", recordPackDetail.getConsume_date());
					typeParam.put("consume_time", recordPackDetail.getConsume_time());
					typeParam.put("type_num", recordPackDetail.getGood_or_type_num());
					goodResult = consumeHandleDao.getTypeByNum(typeParam);
				}
				if(goodResult != null){
					recordPackDetail.setGood_num(goodResult.get("good_num") == null? null:goodResult.get("good_num").toString());
					recordPackDetail.setGood_name(goodResult.get("good_name") == null? null:goodResult.get("good_name").toString());
					recordPackDetail.setPrice(goodResult.get("good_price") == null? null:Double.parseDouble(goodResult.get("good_price").toString()));
					recordPackDetail.setType_num(goodResult.get("type_num") == null? null:goodResult.get("type_num").toString());
					recordPackDetail.setType_name(goodResult.get("type_name") == null? null:goodResult.get("type_name").toString());
					recordPackDetail.setType_price(goodResult.get("type_price") == null? null:Double.parseDouble(goodResult.get("type_price").toString()));
					recordPackDetail.setDiscount_num(goodResult.get("discount_num") == null? null: goodResult.get("discount_num").toString());
					recordPackDetail.setDiscount_rate(goodResult.get("discount_rate") == null? 0: Double.parseDouble(goodResult.get("discount_rate").toString()));
				}
			}
			
		} else if(recordPack.getRecordType()==12) {
			
			recordPackDetail = GsonUtil.toBean(GsonUtil.toJson(consumeHandleDao.getLastRecord(deviceNum)), 
					RecordPackDetail.class);
			
			// 重新生成创建时间和流水号
			String timeStr = accountRelevantService.getNowTimeStr();
			String recordFlowNum = accountRelevantService.makeFlowNum("CX", timeStr, recordPackDetail.getAccount_num());
			recordPackDetail.setCreate_time(formatTime(timeStr));
			recordPackDetail.setConsume_time(formatTime(timeStr));
			recordPackDetail.setRecord_flow_num(recordFlowNum);
			recordPackDetail.setRecord_type("CX");
			
			// 设置日期
			recordPackDetail.setConsume_date(recordPackDetail.getConsume_time().split(" ")[0]);
			
			// 设置本次记录消费次数、商品次数和消费金额 （由于是撤销，不增不减）
			/*recordPackDetail.setConsume_amount(0);
			recordPackDetail.setGood_amount(0);*/
			
			// 设置撤销金额（即原记录支出）
			recordPackDetail.setCancel_sum(recordPackDetail.getOutbound());
			
			// 重新获取账户最新余额
			Map account = consumeHandleDao.getAccountByAccountNum(recordPackDetail.getAccount_num());
			if(account != null){
				BigDecimal balance = (BigDecimal)account.get("account_balance");
				if(balance != null){
					recordPackDetail.setAccount_balance(balance.doubleValue());
				}
			}
						
			if(recordPackDetail.getGood_num()!=null){
				recordPackDetail.setIs_type("0");
			} else if(recordPackDetail.getType_num()!=null){
				recordPackDetail.setIs_type("1");
			}
		}
		return recordPackDetail;
	}

	
	/**
	 * 进行消费
	 * @author yuman.gao
	 * @throws Exception 
	 */
	public JsonResult doConsume(RecordPackDetail recordPackDetail) throws Exception{
		JsonResult result = new JsonResult();
		try{
			// 处理商品单价
			/*if(recordPackDetail.getGood_num()!=null || recordPackDetail.getType_num()!=null){
				recordPackDetail = updatePrice(recordPackDetail);    //单价有变，返回新的纪录包
			}*/
			// 扣取余额
			recordPackDetail = deductAmount(recordPackDetail);   // 余额有变，返回新的记录包
			if( recordPackDetail == null){
				result.setMsg("上报失败");
				result.setSuccess(false);
				return result;
			} 
			// 统计消费记录流水
			addAccountDetail(recordPackDetail);
			addConsumeRecord(recordPackDetail);
			countMealCounsume(recordPackDetail);
			if(recordPackDetail.getGood_num() != null || recordPackDetail.getType_num() != null){
				countGoodConsume(recordPackDetail);
			}
			countPersonCousume(recordPackDetail);
			countDeptCousume(recordPackDetail);
			countTerminalCousume(recordPackDetail);
			// 下发名单
			insertNameTask(recordPackDetail);
			
			result.setMsg("上报成功");
			result.setSuccess(true);
			result.setAlm(00);
			return result;
		}catch(Exception e){
			throw e;
		}
	}
	
	
	/**
	 * 撤销消费
	 * @author yuman.gao
	 * @throws Exception 
	 */
	public JsonResult cancelConsume(RecordPackDetail recordPackDetail) throws Exception{
		try{
			JsonResult result = new JsonResult();
			// 若该记录已退款，则不进行撤销消费，返回提示信息
			if("1".equals(recordPackDetail.getRefund())){
				result.setMsg("撤销失败，该记录已退款");
				result.setSuccess(false);
				result.setAlm(01);
				return result;
			}
			// 增加账户余额
			recordPackDetail = addBalance(recordPackDetail);   // 余额有变，返回新的记录包
			
			// 统计消费记录流水
			addAccountDetail(recordPackDetail);
			addConsumeRecord(recordPackDetail);
			countMealCounsume(recordPackDetail);
			if(recordPackDetail.getGood_num() != null || recordPackDetail.getType_num() != null){
				countGoodConsume(recordPackDetail);
			}
			countPersonCousume(recordPackDetail);
			countDeptCousume(recordPackDetail);
			countTerminalCousume(recordPackDetail);
			// 更新记录撤销状态
			updateCancelRecord(recordPackDetail.getDevice_num());
			// 名单下发
			insertNameTask(recordPackDetail);
			
			result.setMsg("撤销成功");
			result.setSuccess(true);
			result.setAlm(00);
			return result;
		}catch(Exception e){
			throw e;
		}
	}
	

	/**
	 * 扣取账户余额
	 * @author yuman.gao
	 */
	public RecordPackDetail deductAmount(RecordPackDetail recordPackDetail){
		try{
			Double consumeSum = recordPackDetail.getConsume_sum();
			Double balance = recordPackDetail.getAccount_balance();	
			
			// 根据折扣率计算实际消费总额
			Double discountRate = recordPackDetail.getDiscount_rate();
			if(discountRate != null && discountRate !=0 ){
				consumeSum = new BigDecimal(consumeSum * discountRate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			
			// 若余额足够，则扣取余额
		//	if(balance >= consumeSum){
				String account_num = recordPackDetail.getAccount_num();
				Double deductBalance = balance-consumeSum;
				consumeHandleDao.updateBalance(account_num, deductBalance); 
				
				// 设置变量 recordPackDetail 的余额和实际消费总额
				recordPackDetail.setActual_consume_sum(consumeSum);
				recordPackDetail.setAccount_balance(deductBalance);
				recordPackDetail.setOutbound(consumeSum);
				recordPackDetail.setInbound(0.0);
				return recordPackDetail;
		/*	} else {
				return null; 
			}*/
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 增加账户余额
	 * @author yuman.gao
	 */
	public RecordPackDetail addBalance(RecordPackDetail recordPackDetail){
		Double outbound = recordPackDetail.getOutbound(); 
		Double balance = recordPackDetail.getAccount_balance();	
		
		String account_num = recordPackDetail.getAccount_num();
		Double newBalance = balance + outbound;
		consumeHandleDao.updateBalance(account_num, newBalance); 
		
		// 设置变量 recordPackDetail 的余额和实际消费总额
		/*recordPackDetail.setActual_consume_sum(outbound*-1);*/
		recordPackDetail.setAccount_balance(newBalance);
		recordPackDetail.setOutbound(0.0);
		recordPackDetail.setInbound(outbound);
		
		return recordPackDetail;
	}
	
	
	/**
	 * 处理商品单价
	 * @author yuman.gao
	 */
	public RecordPackDetail updatePrice(RecordPackDetail recordPackDetail){
		/* 根据isType判断设备关联类型
		       若关联商品，则判断商品价格是否变动；若关联商品类型，则判断类型价格是否变动
		       若价格变动，则更新价格并将旧价格存入历史记录；否则不做任何操作*/
		try{
			String isType = recordPackDetail.getIs_type();
			Double consumeSum = recordPackDetail.getConsume_sum();
			Double price = recordPackDetail.getPrice();
			Double typePrice = recordPackDetail.getType_price();
			
			if("0".equals(isType) && !consumeSum.equals(price)){
				String goodNum = recordPackDetail.getGood_num();
				consumeHandleDao.updatePrice(goodNum, consumeSum);
				consumeHandleDao.addPriceHistory(goodNum, isType, price, consumeSum);
				recordPackDetail.setPrice(consumeSum);
				
			} else if ("1".equals(isType) && !consumeSum.equals(typePrice)){
				String typeNum = recordPackDetail.getType_num();
				consumeHandleDao.updateTypePrice(typeNum, consumeSum);
				consumeHandleDao.addPriceHistory(typeNum, isType, typePrice, consumeSum);
				recordPackDetail.setType_price(consumeSum); 
			}
			return recordPackDetail;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 下发人员名单
	 * @author yuman.gao
	 */
	public void insertNameTask(RecordPackDetail recordPackDetail){
		String deviceNum = recordPackDetail.getDevice_num();
		String cardNum = recordPackDetail.getCard_num();
		String triggerFlag = null;
		if("XF".equals(recordPackDetail.getRecord_type())){
			triggerFlag = "1";
		} else if("CX".equals(recordPackDetail.getRecord_type())){
			triggerFlag = "4";
		}
		
		List<String> nums = consumeHandleDao.getSameTypeDevice(deviceNum);
		for(String device_num : nums){
			deviceService.insertNameTask(device_num, cardNum, "0", triggerFlag);
		}
	}
	
	
	/**
	 * 将撤销的记录标记为已撤销
	 * @author yuman.gao
	 */
	public boolean updateCancelRecord(String deviceNum){
		String record_flow_num = (String)consumeHandleDao.getLastRecord(deviceNum).get("record_flow_num");
		return consumeHandleDao.updateCancelRecord(record_flow_num);
	}
	
	
	/**
	 * 新增刷卡消费流水
	 * @author yuman.gao
	 */
	public boolean addConsumeRecord(RecordPackDetail recordPackDetail){
		
		try{
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
			
			return consumeHandleDao.addConsumeRecord(map); 
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	
	/**
	 * 新增交易明细记录
	 * @author yuman.gao
	 */
	public boolean addAccountDetail(RecordPackDetail recordPackDetail){
		try{
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
			return consumeHandleDao.addAccountDetail(map);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 餐次消费日统计
	 * @author yuman.gao
	 */
	public void countMealCounsume(RecordPackDetail recordPackDetail){
	
		MealConsumeDetail existMealDetail = consumeHandleDao.getMealDetailByNum(
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
				consumeHandleDao.updateMealDetail(mealDetail);
			} else {
				MealConsumeDetail mealDetail = new MealConsumeDetail();
				mealDetail.setMeal_num(recordPackDetail.getMeal_num());
				mealDetail.setMeal_name(recordPackDetail.getMeal_name());
				mealDetail.setEvery_date(recordPackDetail.getConsume_date());
				
				mealDetail.setAmount(consumeAmount);
				mealDetail.setMoney(consumeSum);
				mealDetail.setAfter_discount_money(actualConsumeSum);
				consumeHandleDao.addMealDetail(mealDetail);
			}
			
		} else if ("CX".equals(recordPackDetail.getRecord_type())){
			existMealDetail.setCancel_total(
					existMealDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			consumeHandleDao.updateMealCancelTotal(existMealDetail);
		}
		
	}
	
	
	/**
	 * 商品消费日统计
	 * @author yuman.gao
	 */
	public void countGoodConsume(RecordPackDetail recordPackDetail){
		
		// 查询已存在记录
		GoodConsumeDetail existGoodDetail = null;
		
		String consumeDate = recordPackDetail.getConsume_date();
		Double newPrice = null;
		if("0".equals(recordPackDetail.getIs_type())){
			String goodNum = recordPackDetail.getGood_num();
			/*newPrice = ("XF".equals(recordPackDetail.getRecord_type())? 
					Math.abs(recordPackDetail.getConsume_sum()):recordPackDetail.getPrice()); // 若不是消费操作，价格不变，取原始价格进行判断*/	
			newPrice = recordPackDetail.getPrice();
			existGoodDetail = consumeHandleDao.getGoodDetailByNum(goodNum, consumeDate, newPrice);
		
		} else if("1".equals(recordPackDetail.getIs_type())){
			String typeNum = recordPackDetail.getType_num();
			/*newPrice = ("XF".equals(recordPackDetail.getRecord_type())? 
					Math.abs(recordPackDetail.getConsume_sum()):recordPackDetail.getType_price());*/
			newPrice = recordPackDetail.getType_price();
			existGoodDetail = consumeHandleDao.getGoodTypeDetailByNum(typeNum, consumeDate, newPrice);
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
				consumeHandleDao.updateGoodDetail(goodDetail);
				
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
				consumeHandleDao.addGoodDetail(goodDetail);
			}
			
		} else if("CX".equals(recordPackDetail.getRecord_type())){
			existGoodDetail.setCancel_total(
					existGoodDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			consumeHandleDao.updateGoodCancelTotal(existGoodDetail);
		}
		
	}
	
	
	/**
	 * 人员消费日统计
	 * @author yuman.gao
	 */
	public void countPersonCousume(RecordPackDetail recordPackDetail){
		
		PersonConsumeDetail existPersonDetail = consumeHandleDao.getPersonDetailByNum(
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
				
				consumeHandleDao.updatePersonDetail(personDetail);
				
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
				
				consumeHandleDao.addPersonDetail(personDetail);
			}
			
		} else if("CX".equals(recordPackDetail.getRecord_type())){
			existPersonDetail.setCancel_total(
					existPersonDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			consumeHandleDao.updatePersonCancelTotal(existPersonDetail);
		}
	}
	
	
	/**
	 * 部门消费日统计
	 * @author yuman.gao
	 */
	public void countDeptCousume(RecordPackDetail recordPackDetail){
		
		DeptConsumeDetail existDeptDetail = consumeHandleDao.getDeptDetailByNum(
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
				consumeHandleDao.updateDeptDetail(deptDetail);
				
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
				
				consumeHandleDao.addDeptDetail(deptDetail);
			}
			
		} else if("CX".equals(recordPackDetail.getRecord_type())){
			existDeptDetail.setCancel_total(
					existDeptDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			consumeHandleDao.updateDeptCancelTotal(existDeptDetail);
		}
		
	}
	
	
	/**
	 * 终端消费日统计
	 * @author yuman.gao
	 */
	public void countTerminalCousume(RecordPackDetail recordPackDetail){
		
		TerminalConsumeDetail existTerminalDetail = consumeHandleDao.getTerminalDetailByNum(
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
				consumeHandleDao.updateTerminalDetail(terminalDetail);
				
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
				
				consumeHandleDao.addTerminalDetail(terminalDetail);
			}
			
		} else if("CX".equals(recordPackDetail.getRecord_type())){
			existTerminalDetail.setCancel_total(
					existTerminalDetail.getCancel_total() + recordPackDetail.getCancel_sum());
			consumeHandleDao.updateTerminalCancelTotal(existTerminalDetail);
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
