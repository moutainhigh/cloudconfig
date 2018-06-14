package com.kuangchi.sdd.baseConsole.times.times.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-4-5 下午5:12:34
 * @功能描述: 时段管理模块-Dao类
 */
public interface TimesDao {
	
    /** 新增时段信息*/
	boolean addTimes(Times times);
	
	/** 删除时段信息*/
	boolean deleteTimesById(String times_ids);
	
	/** 修改时段信息*/
	boolean modifyTimes(Times times);
	
    /** 根据条件查询时段信息 (分页) */
    List<Times> getTimesByParamPage(Times times,int page, int size);
    
    /** 根据条件查询时段信息总数 */
    int getTimesByParamCount(Times times);
   
    /** 根据编号查询关联时段组名称  */
    List<String> getTimesGroupByTimesNum(String times_num);
    
    /** 根据条件查询时段信息（接口用）*/
    List<Times> getTimesByParamInterface(Times times);
    
    /**查询所有设备*/
    List<DeviceInfo> getAllDevice();
    
    /**根据编号查询设备*/
    List<DeviceInfo> getDeviceByNums(String deviceNums);
	
}