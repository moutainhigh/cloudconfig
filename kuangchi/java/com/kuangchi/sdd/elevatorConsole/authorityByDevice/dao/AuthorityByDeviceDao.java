package com.kuangchi.sdd.elevatorConsole.authorityByDevice.dao;

import java.util.List;
import java.util.Map;

public interface AuthorityByDeviceDao {

	public List<Map> getAllTKDevices(Map map);
	public Integer countAllTKDevices(Map map);
	public List<Map> getStaffCardsBydeptNums(Map map);
	public Integer countStaffCardsBydeptNums(Map map);
	public List<Map> getFloorGroups();
	public boolean addDeviceAuth(Map map);
	public List<Map> getAuths(Map map);
	public Integer countAuths(Map map);
	public boolean preventRepAuth(Map map);
	
	/**
	 * 查询已有权限 (用于删除时查看)
	 * by huixian.pan
	 */
	public List<Map> getAuthsNoRepeat(Map map);
	
	/**
	 * 查询已有权限（用于删除时查看） 
	 * by huixian.pan
	 */
	public Integer countAuthsNoRepeat(Map map);
	
	/**
	 * 添加删除任务到任务表
	 * by huixian.pan
	 */
	public boolean deleteAuths(Map map);
	
	/**
	 * 获取设备信息
	 * by huixian.pan
	 */
	public List<Map> getTkDeviceInfo(Map map);
	
	/**
	 * 获取设备信息条数
	 * by huixian.pan
	 */
	public Integer getTkDeviceInfoCount(Map map);
	
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
}
