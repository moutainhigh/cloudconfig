package com.kuangchi.sdd.consumeConsole.accountRelevant.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;

public interface AccountRelevantService {
	/**
	 * 查询账户类型
	 * by gengji.yang
	 */
	public Grid getAccountTypeByMap(Map map);
	
	/**
	 * 添加账户类型
	 * by gengji.yang
	 */  
	public void addAccountType(Map map);
	
	/**
	 * 删除账户类型
	 * by gengji.yang
	 */
	public  void deleteAccountType(String nums);
	
	/**
	 * 判断是否已经使用了账户类型
	 * by gengji.yang
	 */
	public boolean ifExistUsingAccountType(String typeNum);
	
	/**
	 * 获取账户类型Map
	 * by gengji.yang
	 */
	public Map getAccountTypeMap(String typeNum);
	
	/**
	 * 更新账户类型
	 * by gengji.yang
	 */
	public void updateAccountType(Map map);
	
	/**
	 * 获取所有的账户类型
	 * by minting.he
	 * @return
	 */
	public List getAllAccoutType();
	
	/**
	 * 初始化账户类型下拉列表
	 * by gengji.yang
	 */
	public List<Map> initType();
	
	/**
	 * 获规则定义字段
	 * by gengji.yang
	 */
	public List<Map> getRegulationNames();
	
	/**
	 * 初始化岗位下拉框
	 * by gengji.yang
	 */
	public List<Map> getPositions();
	
	/**
	 * 初始化员工姓名下拉框
	 * by gengji.yang
	 */
	public List<Map> getStaffNames();
	
	/**
	 * 初始化部门下拉框
	 * by gengji.yang
	 */
	public List<Map> getStaffDepts();
	
	/**
	 * 添加规则
	 * by gengji.yang
	 */
	public void addRegulation(Map map);
	
	/**
	 * 查询规则
	 * by gengji.yang
	 */
	public Grid<Map> getAllRegulations(Map map);
	
	/**
	 * 删除规则
	 * by gengji.yang 
	 */
	public void delRegulation(String id);
	
	/**
	 * 判断账户类型是否已存在规则
	 * by gengji.yang
	 */
	public boolean ifExistRegulation(String accountTypeNum);
	
	/**
	 * 更新账户类型规则
	 * by gengji.yang
	 */
	public void updateRegulation(Map map);
	
	/**
	 * 初始化 规则下拉列表
	 * by gengji.yang
	 */
	public List<Map> initRegulation();
	
	/**
	 * 账户类型可能绑定了规则，
	 * 		若有规则，则先找到人群，判断是否都有账户，
	 * 		若没有规则，则直接判断所有的人是都都有账户
	 * 			若员工没有账户，则自动为员工开户
	 * by gengji.yang
	 */
	public boolean rechargeByAccountType(String accountTypeNum,String money,String regulationId,String createUser);
	
	/**
	 * 查询账号流水
	 * by gengji.yang
	 */
	public Grid getAllAccountFlow(Map map);
	
	/**
	 * 根据员工编号充值
	 * by gengji.yang
	 */
	public boolean rechargeByStaffNum(String staffNum,String accountTypeNum,String money,String createUser);
	
	/**
	 * 根据部门编号充值
	 * by gengji.yang
	 */
	public boolean rechargeByDeptNum(String deptNum,String accountTypeNum,String money,String createUser); 
	
	/**
	 * 根据账户类型充值前，判断各自资金池的钱是否够用
	 * 返回一个记录Map
	 * 主要返回的是记录Map中的message（i）键
	 * by gengji.yang
	 */
	public Map isEnoughByAccountType(String accountTypeNum,String money,String regulationId);
	
	/**
	 * 按人员充值，判断对应资金池是否够钱
	 * 实现过程类似同根据“账户类型”
	 * by gengji.yang
	 */
	public Map isEnoughByStaffNum(String staffNum,String accountTypeNum,String money);
	
	/**
	 * 按部门充值，判断对应资金池是否够钱
	 * 实现过程类似同根据“账户类型”
	 * by gengji.yang
	 */
	public Map isEnoughByDeptNum(String deptNum,String accountTypeNum,String money);
	
	/**
	 * 方法重载
	 * 根据员工编号补助
	 * by gengji.yang
	 */
	public boolean rechargeByStaffNum(String staffNum,String accountTypeNum,String money,String createUser,String type);
	
	/**
	 * 方法重载
	 * 根据部门编号补助
	 * by gengji.yang
	 */
	public boolean rechargeByDeptNum(String deptNum,String accountTypeNum,String money,String createUser,String type);
	
	/**
	 * 方法重载
	 * 根据账户类型补助
	 * by gengji.yang
	 */
	public boolean rechargeByAccountType(String accountTypeNum,String money,String regulationId,String createUser,String type);
	
	/**
	 * 流水号生成器
	 * 返回：时间-positionOne-positionTwo-positionThree-positionFour-positionFive
	 * 根据需要传入实际的String ，对好位置
	 * by gengji.yang
	 */
	public String makeFlowNum(String operateType,String timeStr,String accountNum);
	
	/**
	 * 查看某个规则
	 * by gengji.yang
	 */
	public Map viewRegulation(String id);
	
	/**
	 * 账户查询
	 * by gengji.yang
	 */
	public Grid searchAccount(Map map);
	
	/**
	 * 销户前，判断资金池是否够钱
	 * by gengji.yang
	 */
	public Map isEnoughToCloseAccount(String staffNum,String accountTypeNum,String accountNum);
	
	/**
	 * 销户
	 *  	退钱，删除账户
	 * by gengji.yang
	 */
	public boolean toCloseAccount(String staffNum,String accountTypeNum,String accountNum,String createUser);
	
	/**
	 * 销户查询
	 * by gengji.yang
	 */
	public Grid searchClosedAccount(Map map);
	
	/**
	 * 获取当前时间字符串
	 * 返回格式：2016-09-05 12:12:12 
	 * by gengji.yang
	 */
	public String getNowTimeStr();
	
	/**
	 * 负责搞定所有的充值统计表
	 * by gengji.yang
	 */
	public void handleAllRechargeDaily();
	
	/**
	 * 冻结账户
	 * by gengji.yang
	 */
	public void frozenAccount(String accountNum);
	
	/**
	 * 解冻账户
	 * by gengji.yang
	 */
	public void reFrozenAccount(String accountNum);
	
	/**
	 * 获取部门列表用于选择部门
	 * by gengji.yang
	 */
	public Grid  getDeptList(Map map);
	
	/**
	 * 判断是否存在账户类型名字
	 * by gengji.yang
	 */
	public boolean checkAccTypeName(String accountTypeName);
	
	/**
	 * 负责搞定员工，部门充值统计报表
	 * by gengji.yang
	 */
	public  void handleRecharge();
	
	/**
	 * 负责搞定员工，部门补助统计报表
	 * by gengji.yang
	 */
	public void handleBZ();
	
}

