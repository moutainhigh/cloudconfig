package com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;

/**
 * @创建人　: 陈桂波
 * @创建时间: 2016-10-9 下午4:12:34
 * @功能描述: 时段管理模块-Dao类
 */
public interface DeviceTimesDao {
	
    /** 新增时段信息*/
	boolean addDeviceTimes(DeviceTimes times);
	
	/** 复制时新增时段信息*/
	boolean copeAddDeviceTimes(DeviceTimes times);
	
	/** 修改时段信息*/
	boolean modifyTimes(DeviceTimes times);
	
    /** 根据条件查询时段信息 (分页) */
    List<DeviceTimes> getTimesByParamPage(DeviceTimes times,int page, int size);
    
    /** 根据条件查询时段信息总数 */
    int getTimesByParamCount(DeviceTimes times);
   
    /** 根据条件查询时段信息（接口用）*/
    List<DeviceTimes> getTimesByParamInterface(DeviceTimes times);
    
    /**根据编号查询设备*/
    List<DeviceInfo> getDeviceByNums(String deviceNums);
    
    /**根据编号查询设备mac和块号*/
    List<DeviceTimes> getDeviceMacByNums(String deviceNums);
    
    /**根据设备编号查询时段表数据除了时段编号为0*/
    List<DeviceTimes> getDeviceTimesByDeviceNum(String deviceNums);
    
    /** 复制或之前先删除时段信息*/
	boolean deleteDeviceTimesByDeviceNum(String deviceNums);
	
	public void delRepTime(DeviceTimes times);
	
	public void addTimeAfterDel(DeviceTimes times);
	
	/**
	 * 删除时段
	 * @author minting.he
	 * @param ids
	 * @return
	 */
	public boolean delTimesById(String ids);
	
	/**
	 * 获取设备的全部时段
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Map<String, Object>> getTimesByDevice(String device_num);
    
	/**
	 * 更新设备时段
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean updateTimeNum(Map<String, Object> map);
	
	/**
	 * 删除时段前判断是否在使用
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public Integer ifUsedTimes(Map<String, Object> map);
	
}