package com.kuangchi.sdd.visitorConsole.connCs.service;

import java.util.List;
import java.util.Map;

public interface ConnCsService {

	/**
	 * 发卡/补发卡
	 * 并下门禁，梯控权限
	 * by gengji.yang
	 */
	public boolean makeCard(Map map);
	
	/**
	 * 发卡/补发卡,用于光钥匙
	 * 并下门禁，梯控权限
	 * by gengji.yang
	 */
	public boolean makeCard_phone(Map map);
	
	/**
	 * 卡解挂
	 * by gengji.yang
	 */
	public void makeCardBack(Map map);
	
	/**
	 * 卡挂失
	 * 并删除门禁，梯控权限
	 * by gengji.yang
	 */
	public boolean makeCardLost(Map map);
	
	/**
	 * 获取新的卡号
	 * 若返回null,则说明，
	 * 	某类型的卡号的使用范围区间已满
	 * 返回的卡号有两种可能：
	 * 	1，已经新增的未绑定卡
	 * 	2，才新增的卡，会往kc_card_info,kc_bound_card_info补充信息
	 * cardType:0 >> 光子卡 ;1 >> IC卡
	 * by gengji.yang
	 */
	public String getNewCardNum(Map map);
	
	/**
	 * 超时后的离开登记
	 * lostFlag:卡挂失标记位，0：挂失 1：不挂失
	 * blackFlag:加入黑名单标记位 0：加入 1：不加入
	 * By gengji.yang
	 */
	public boolean overTimeLeave(Map map);
	
	/**
	 * 更新访客状态 
	 * ：访问超时
	 * by gengji.yang
	 */
	public void updateVisitorState();
	
	/**
	 * 更新手机访客状态 
	 * ：预约》》正在访问
	 * by gengji.yang
	 */
	public void updateShouJiVisiting();
	
	/**
	 * 更新手机访客状态 
	 * ：正在访问》》离开
	 * by gengji.yang
	 */
	public void updateShouJiLeaving();

	/**
	 * 删除手机离开访客的card信息 by gengji.yang
	 */
	public void deleteShouJiLeaveCard();
	
	/**
	 * 删除手机离开访客的boundcard信息 by gengji.yang
	 */
	public void deleteShouJiLeaveBoundCard();
	
	/**
	 *删除访客过期门禁权限 by gengji.yang 
	 */
	public void delGuoQiMenJinQuanXianJilu();
	
	/**
	 * 删除访客过期梯控权限记录 by gengji.yang
	 */
	public void delGuoQiTkQuanXianJilu();
	
	/**
	 * 更新访客状态 
	 * ：预约超时
	 * by gengji.yang
	 */
	public void updateVisitorStateA();
	
	/**
	 *获取未邮件通知的访客超时记录
	 *by gengji.yang 
	 */
	public List<Map> getNYetEmailRecd();
	
	/**
	 *统计所有的访客超时记录数
	 *by gengji.yang 
	 */
	public Integer countOverTimeRecd();
	
	/**
	 *发送邮件之后，更新访客记录的发送邮件标志位
	 *by gengji.yang 
	 */
	public void updateEmailState(List<Map> list);
	
	/**
	 * 获取通知人列表
	 * by gengji.yang
	 */
	public List<Map> getInformList();
	
	/**
	 * 访客正常离开时，离开登记，要删除卡权限
	 * 故封装此接口
	 * map中传cardNum
	 * by gengji.yang
	 */
	public boolean delAuthOnLeave(Map map);
	
	/**
	 * 预约的访客到访登记后，更改状态为 正在访问
	 * by gengji.yang
	 */
	public void makeVisitorVisiting(Map map);
	
	/**
	 * 回收卡，卡号可以再次被使用，删除卡权限
	 * by gengji.yang
	 */
	public void recycleCard(Map map);
	
	/**
	 * 删除卡记录
	 * by gengji.yang
	 */
	public void delCardRec(Map map);
	
}
