package com.kuangchi.sdd.consumeConsole.consumeRecord.dao;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.consumeHandle.model.Account;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.DeptConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.GoodConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.MealConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.PersonConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.TerminalConsumeDetail;
import com.kuangchi.sdd.consumeConsole.consumeRecord.model.ConsumeRecord;

public interface IConsumeRecordDao {
	
	public List<ConsumeRecord> selectAllConsumeRecords(ConsumeRecord consume_record,String page, String size);//模糊查询消费所有信息
	
	public Integer getAllConsumeRecordCount(ConsumeRecord consume_record);//查询总的行数
	
	public List<ConsumeRecord> exportAllConsumeRecords(ConsumeRecord consume_record);//导出消费信息
	
	/**
	 * 电子地图显示最新3条记录
	 * @author minting.he
	 * @return
	 */
	public List<ConsumeRecord> getNewRecords();
	
	
	
	/**
	 * 密码验证
	 * @author yuman.gao
	 */
	public Integer getUser(String userName, String password);

	/**
	 * 根据记录流水号查询记录
	 * @author yuman.gao
	 */
	public ConsumeRecord getRecordByNum(String record_flow_num);
	
	/**
	 * 根据账户编号查询账户
	 * @author yuman.gao
	 */
	public Account getAccountByNum(String account_num);
	
	/**
	 * 更新退款状态
	 * @author yuman.gao
	 */
	public boolean updateRefundState(String record_flow_num);
	
	/**
	 * 更新餐次退款总额
	 * @author yuman.gao
	 */
	public boolean updateMealRefundTotal(MealConsumeDetail mealConsumeDetail);
	/**
	 * 更新商品退款总额
	 * @author yuman.gao
	 */
	public boolean updateGoodRefundTotal(GoodConsumeDetail goodConsumeDetail);
	/**
	 * 更新部门退款总额
	 * @author yuman.gao
	 */
	public boolean updateDeptRefundTotal(DeptConsumeDetail deptConsumeDetail);
	/**
	 * 更新人员退款总额
	 * @author yuman.gao
	 */
	public boolean updatePersonRefundTotal(PersonConsumeDetail personConsumeDetail);
	/**
	 * 更新终端退款总额
	 * @author yuman.gao
	 */
	public boolean updateTerminalRefundTotal(TerminalConsumeDetail terminalConsumeDetail);
	
	
	/**
	 * 更新撤销状态
	 * @author yuman.gao
	 */
	public boolean updateCancelState(Map<String, Object> map);
	
	/**
	 * 更新餐次撤销总额
	 * @author yuman.gao
	 */
	public boolean updateMealCancelTotal(MealConsumeDetail mealConsumeDetail);
	/**
	 * 更新商品撤销总额
	 * @author yuman.gao
	 */
	public boolean updateGoodCancelTotal(GoodConsumeDetail goodConsumeDetail);
	/**
	 * 更新部门撤销总额
	 * @author yuman.gao
	 */
	public boolean updateDeptCancelTotal(DeptConsumeDetail deptConsumeDetail);
	/**
	 * 更新人员撤销总额
	 * @author yuman.gao
	 */
	public boolean updatePersonCancelTotal(PersonConsumeDetail personConsumeDetail);
	/**
	 * 更新终端撤销总额
	 * @author yuman.gao
	 */
	public boolean updateTerminalCancelTotal(TerminalConsumeDetail terminalConsumeDetail);
	
	/**
	 * 查看是否有销户的账户
	 * @author minting.he
	 * @param account_num
	 * @return
	 */
	public Integer ifCancelAccount(String account_num);
	
}
