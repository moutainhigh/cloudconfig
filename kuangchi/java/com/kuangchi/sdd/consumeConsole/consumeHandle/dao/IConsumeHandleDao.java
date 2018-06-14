package com.kuangchi.sdd.consumeConsole.consumeHandle.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.consumeHandle.model.Account;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.DeptConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.GoodConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.MealConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.PersonConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.RecordPackDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.TerminalConsumeDetail;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;

/**
 * 消费记录上报及处理-dao
 * @author yuman.gao
 */
public interface IConsumeHandleDao {
	
	// 根据编号和日期查询商品日消费记录
	public GoodConsumeDetail getGoodDetailByNum(String goodNum, String everyDate, Double price);
	
	// 根据编号和日期查询商品类型日消费记录
	public GoodConsumeDetail getGoodTypeDetailByNum(String typeNum, String everyDate, Double typePrice);
	
	// 根据编号和日期查询部门日消费记录
	public DeptConsumeDetail getDeptDetailByNum(String deptNum, String everyDate);
	
	// 根据编号和日期查询人员日消费记录
	public PersonConsumeDetail getPersonDetailByNum(String staffNum, String everyDate);
	
	// 根据编号和日期查询终端日消费记录
	public TerminalConsumeDetail getTerminalDetailByNum(String deviceNum, String everyDate);
	
	// 根据编号和日期查询餐次日消费记录
	public MealConsumeDetail getMealDetailByNum(String mealNum, String everyDate);
	
	
	
	// 新增商品日消费流水
	public boolean addGoodDetail(GoodConsumeDetail goodConsumeDetail);
	
	// 更新商品日消费流水
	public boolean updateGoodDetail(GoodConsumeDetail goodConsumeDetail);
	
	// 新增部门日消费流水
	public boolean addDeptDetail(DeptConsumeDetail deptConsumeDetail);
	
	// 更新部门日消费流水
	public boolean updateDeptDetail(DeptConsumeDetail deptConsumeDetail);
	
	// 新增人员日消费流水
	public boolean addPersonDetail(PersonConsumeDetail personConsumeDetail);
	
	// 更新人员日消费流水
	public boolean updatePersonDetail(PersonConsumeDetail personConsumeDetail);
	
	// 新增终端日消费流水
	public boolean addTerminalDetail(TerminalConsumeDetail terminalConsumeDetail);
	
	// 更新终端日消费流水
	public boolean updateTerminalDetail(TerminalConsumeDetail terminalConsumeDetail);
	
	// 新增餐次日消费流水
	public boolean addMealDetail(MealConsumeDetail mealConsumeDetail);
	
	// 更新餐次日消费流水
	public boolean updateMealDetail(MealConsumeDetail mealConsumeDetail);
	
	// 更新餐次撤销总额
	public boolean updateMealCancelTotal(MealConsumeDetail mealConsumeDetail);
	// 更新商品撤销总额
	public boolean updateGoodCancelTotal(GoodConsumeDetail goodConsumeDetail);
	// 更新部门撤销总额
	public boolean updateDeptCancelTotal(DeptConsumeDetail deptConsumeDetail);
	// 更新人员撤销总额
	public boolean updatePersonCancelTotal(PersonConsumeDetail personConsumeDetail);
	// 更新终端撤销总额
	public boolean updateTerminalCancelTotal(TerminalConsumeDetail terminalConsumeDetail);
	
	
	
	
	// 根据消费时间查询餐次
	public MealModel getMealByTime(String consumeTime);
	
	// 根据有效期和编号查询绑定商品信息
	public Map<String,Object> getGoodByNum(Map<String,Object> map);
	
	// 根据有效期和编号查询绑定商品类型信息
	public Map<String,Object> getTypeByNum(Map<String,Object> map);
	
	// 根据时间查询消费组
	public Map getGroupByTime(Map map);
	
	// 根据账户编号查询账户
	public Map getAccountByAccountNum(String accountNum); 
	
	// 更新商品单价
	public boolean updatePrice(String goodNum, Double price);
	
	// 更新商品类型单价
	public boolean updateTypePrice(String typeNum, Double price);
	
	// 新增价格变动历史记录
	public boolean addPriceHistory(String goodNum, String isType, Double oldPrice, Double newPrice);
	
	// 更新账户余额
	public boolean updateBalance(String amountNum, Double balance);
	
	// 新增刷卡流水
	public boolean addConsumeRecord(Map<String,Object> map);
	
	// 新增交易明细记录
	public boolean addAccountDetail(Map<String,Object> map);
	
	// 查询记录包详情
	public Map<String, Object> getRecordDetial(Map<String,Object> map);
	
	// 查询指定设备最后一条记录（撤销消费时使用）
	public Map<String, Object> getLastRecord(String deviceNum);
	
	// 将撤销的记录标记为已撤销
	public boolean updateCancelRecord(String record_flow_num);
	
	
	
	
	// 查询设备是否存在
	public Integer getDevice(String deviceNum);
	
	// 查询卡号是否存在
	public Integer getCard(String cardNum);
	
	// 查询卡号是否支持消费
	public Integer getIsSupport(String cardNum);
	
	// 查询设备是否绑定商品或商品类型
	/*public Integer getBoundGoodOrType(String deviceNum, String consumeDate);*/
	
	// 根据卡号查询账户详情
	public Account getAccountByCard(Map<String,Object> map);
	
	// 查询员工消费限次
	public Integer getStaffLimit(String staffNum, String mealNum);
	
	// 查询设备消费限次
	public Integer getDeviceLimit(String deviceNum, String mealNum);
	
	// 查询员工当天指定餐次消费次数
	public Integer getStaffConsumeCount(Map<String,Object> map);
	
	// 查询设备当天指定餐次消费次数
	public Integer getDeviceConsumeCount(Map<String,Object> map);
	
	/**
	 * 新增交易明细记录2
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean addAccountDetail2(RecordPackDetail recordPackDetail);
	
	/**
	 * 新增刷卡消费流水
	 * @author minting.he
	 * @param recordPackDetail
	 * @return
	 */
	public boolean addConsumeRecord2(RecordPackDetail recordPackDetail);
	
	/**
	 * 餐次消费日统计
	 * @author minting.he
	 * @param recordPackDetail
	 * @return
	 */
	public boolean countMealCounsume2(RecordPackDetail recordPackDetail);
	
	/**
	 * 商品消费日统计
	 * @author minting.he
	 * @param recordPackDetail
	 * @return
	 */
	public boolean countGoodConsume2(RecordPackDetail recordPackDetail);
	
	/**
	 * 人员消费日统计
	 * @author minting.he
	 * @param recordPackDetail
	 * @return
	 */
	public boolean countPersonCousume2(RecordPackDetail recordPackDetail);
	
	/**
	 * 部门消费日统计
	 * @author minting.he
	 * @param recordPackDetail
	 * @return
	 */
	public boolean countDeptCousume2(RecordPackDetail recordPackDetail);
	
	/**
	 * 终端消费日统计
	 * @author minting.he
	 * @param recordPackDetail
	 * @return
	 */
	public boolean countTerminalCousume2(RecordPackDetail recordPackDetail);
	
	/**
	 * 扣取账户余额
	 * @param recordPackDetail
	 * @return
	 */
	public RecordPackDetail deductAmount2(RecordPackDetail recordPackDetail);
	
	/**
	 * 处理商品单价
	 * @author minting.he
	 * @param recordPackDetail
	 * @return
	 */
	public RecordPackDetail updatePrice2(RecordPackDetail recordPackDetail);
	
	/**
	 * 增加账户余额
	 * @author minting.he
	 * @param recordPackDetail
	 * @return
	 */
	public RecordPackDetail addBalance2(RecordPackDetail recordPackDetail);
	
	/**
	 * 将撤销的记录标记为已撤销
	 * @author minting.he
	 * @param deviceNum
	 * @return
	 */
	public boolean updateCancelRecord2(String deviceNum);
	
	/**
	 * 查询绑定同一账户类型的设备
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<String> getSameTypeDevice(String device_num);
	
}
