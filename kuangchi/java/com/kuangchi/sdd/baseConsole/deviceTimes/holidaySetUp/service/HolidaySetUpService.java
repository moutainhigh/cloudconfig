package com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.model.HolidaySetUp;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorDeviceInfo;

/**
 * @创建人　: chudan.guo
 * @创建时间: 2016-10-9 
 * @功能描述: 设置节假日时段信息-Service
 */
public interface HolidaySetUpService {
	 /**
	  * 获取设备设置的节假日信息
	  */
	public Grid<HolidaySetUp> getHolidayTimesOfDevice(Map map);
	
	 /**
	  * 增加节假日时段
	  */
	 public boolean addHolidayTimes(HolidaySetUp holidaySetUp,String logUser);
	 /**
	  * 查看节假日最大时段编号
	  */
	 public boolean getMaxHolidayTimesNum(HolidaySetUp holidaySetUp);
	 /**
	  *  查看节假日日期是否已被用
	  */
		public Integer getHolidayDateOfDevice(HolidaySetUp holidaySetUp);
	/**
	 * 修改节假日时段
	 */
	public boolean updateHolidayTimes(HolidaySetUp holidaySetUp,String logUser) ;
	/**
	 * 删除节假日时段
	 */
	public  boolean delHolidayTimes(String deleIds) ;
	/** 
	 * 下发设备节假日时段
	 */
	public void addTimesObject(Map<String,Object> map) throws  Exception;
	/**
	 * @功能描述: 查看设备已下发节假日时段
	 */
	public String watchHolidayTimes(String device_mac);
	/**
	  * 根据设备编号查找名称、Mac地址、类型
	  */
	 public Map getDeviceMacAndType(String device_num);
	/**
	 * @功能描述: 复制节假日时段
	 */
	public boolean copyHolidayTimes(String device_num,String deviceNum);
	/**
	 * @功能描述: 复制并下发节假日时段
	 */
	public boolean copyAndIssuedHolidayTimes(String device_num,String deviceNum)throws  Exception;
	
	/**
	 * 删除
	 * @author minting.he
	 * @param ids
	 * @param login_user
	 * @return
	 */
	public boolean delHolidayById(String ids, String login_user);
}
