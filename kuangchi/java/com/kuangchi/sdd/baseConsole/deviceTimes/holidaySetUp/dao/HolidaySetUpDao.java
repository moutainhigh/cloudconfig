package com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.model.HolidaySetUp;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;

/**
 * @创建人　: chudan.guo
 * @创建时间: 2016-10-9 
 * @功能描述: 设置节假日时段信息-Dao
 */
public interface HolidaySetUpDao {
	
	/**
	 * @功能描述: 获取设备设置的节假日信息
	 */
	public List<HolidaySetUp> getHolidayTimesOfDevice(Map map);
	
	/**
	 * @功能描述: 获取设备设置的节假日信息总条数
	 */
	public Integer getHolidayTimesOfDeviceCount(Map map);
	/**
	  * 查看节假日最大时段编号
	  */
	 public boolean getMaxHolidayTimesNum(HolidaySetUp holidaySetUp);
	/**
	 * @功能描述: 查看节假日日期是否已被用
	 */
	public Integer getHolidayDateOfDevice(HolidaySetUp holidaySetUp);
	
	/**
	  * 根据假日时段编号查询所有记录
	  */
	 List<HolidaySetUp> getByHolidayNum(String device_num);
	 /**
	 * 根据条件查询记录总数
	 */
	 Integer countHolidayTimes(HolidaySetUp holidaySetUp, String exist_nums);
	 
	 /**
	 * 增加
	 */
	 public boolean addHolidayTimes(HolidaySetUp holidaySetUp);
	 /**
	 * 修改
	 */
	 boolean updateHolidayTimes(HolidaySetUp holidaySetUp);
	 /**
	 * 删除
	 */
	 boolean delHolidayTimes(String device_num);
	 /**
	  * 根据设备编号查找名称、Mac地址、类型
	  */
	 public Map getDeviceMacAndType(String device_num);
	 
	 /**
	  * 删除
	  * @author minting.he
	  * @param ids
	  * @return
	  */
	 public boolean delHolidayById(String ids);
}
