package com.kuangchi.sdd.elevatorConsole.authorityByDevice.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;

public interface AuthorityByDeviceService {

	/**
	 * 获取所有的梯控设备
	 * by gengji.yang
	 */
	public Grid getAllTkDevices(Map map);
	
	/**
	 * 获取员工及卡
	 * by gengji.yang
	 */
	public Grid getStaffCardsBydeptNums(Map map);
	
	/**
	 * 初始化楼层组下拉框
	 * by gengji.yang
	 */
	public List<Map> getFloorGroups();
	
	/**
	 * 设备授权
	 */
	public boolean addDeviceAuth(Map map);
	
	/**
	 * 查询已有权限
	 * by gengji.yang
	 */
	public Grid getAuths(Map map);
	
	/**
	 * 删除权限
	 * by gengji.yang
	 */
	public boolean deleteAuths(Map map);
	
	/**
	 * 防止一张卡有两条权限记录
	 * by gengji.yang
	 */
	public boolean preventRepAuth(Map map);
	
	/**
	 * 获取设备信息
	 * by huixian.pan
	 */
	public Grid getTkDeviceInfo(Map map);
	
	/**
	 * 通过设备编号获取授权信息
	 * by huixian.pan
	 */
	public List<Map> getTkAuthByDeviceNum(Map map);
	
	/**
	 * 获取楼层信息
	 * by huixian.pan
	 */
	public List<String> getFloorNum2(String groupNum);
	
	/**
	 * 通过设备编号获取所梯控设备IP
	 * by huixian.pan
	 */
	public Map getTkDeviceIPByDeviceNum(String device_num);	
	
	/**
	 * 查询已有权限 (用于删除时查看)
	 * by huixian.pan
	 */
	public Grid getAuthsNoRepeat(Map map);
}
