package com.kuangchi.sdd.visitorConsole.visitLinkage.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;

public interface VisitLinkageService {
	/**
	 * 获取设备门记录
	 * by gengji.yang
	 */
	public Grid getFkDevDors(Map map);
	
	/**
	 * 获取访客系统中的门禁权限信息
	 * by gengji.yang
	 */
	public Grid getFkDoorSysAuths(Map map);
	
	/**
	 * 获取访客系统中的梯控权限信息
	 * by gengji.yang
	 */
	public Grid getFkTkSysAuths(Map map);
	
	/**
	 * 获取访客系统中的门禁权限信息
	 * 不带分页
	 * by gengji.yang
	 */
	public Grid getFkDoorSysAuthsNoPage(Map map);
	
	/**
	 * 获取访客系统中的梯控权限信息
	 * 不带分页
	 * by gengji.yang
	 */
	public Grid getFkTkSysAuthsNoPage(Map map);
	/**
	 * 新增访客系统中的门禁权限
	 * by gengji.yang
	 */
	public void addFkDoorSysAuth(List<Map> list);
	
	/**
	 * 删除访客系统中的门禁权限
	 * by gengji.yang
	 */
	public void delFkDoorSysAuth(Map map);
	
	/**
	 * 查询访客系统中的梯控权限
	 * by gengji.yang
	 */
	public Grid getFkTkDevs(Map map);
	
	/**
	 * 新增访客系统中的梯控权限
	 * by gengji.yang
	 */
	public void addFkTkSysAuth(List<Map> list);
	
	/**
	 * 保存梯控权限
	 * by gengji.yang
	 */
	public void saveFkTkAuths(List<Map> list);
	
	/**
	 * 查询权限组信息
	 * by gengji.yang
	 */
	public Grid getAuthGroupInfoByGroupNum(Map map);
	
	/**
	 * 新增权限组信息
	 * by gengji.yang
	 */
	public void addAuthGroup(List<Map> list);
	
	/**
	 * 删除权限组信息
	 * by gengji.yang
	 */
	public void delAuthGroup(Map map);
	
	/* 判断权限是否已被分配到权限组  by huixian.pan */
	public boolean  ifAuthExitInGroup(Map map);

}
