package com.kuangchi.sdd.commConsole.time.service;

import com.kuangchi.sdd.commConsole.time.model.Time;

public interface ITimeService {
	/**
	 * 
	 * Description:获取设备时间 date:2016年4月29日
	 * 
	 * @param mac
	 * @return
	 */
	public Time getDeviceTime(String mac, String sign);

	/**
	 * 
	 * Description:设置设备时间 date:2016年4月29日
	 * 
	 * @param mac
	 * @param date
	 * @return
	 */
	public int setDeviceTime(String mac, String date);

	/**
	 * 
	 * Description:设置设备时间 date:2016年7月5日
	 * 
	 * @param mac
	 *            设备mac地址
	 * @param sign
	 *            设备类型
	 * @return
	 */
	public int setDeviceTime2(String mac, String device_type);

	/**
	 * 
	 * Description:设置设备时间 date:2016年7月5日
	 * 
	 * @param mac
	 *            设备mac地址
	 * @param sign
	 *            设备类型
	 * @return
	 */
	public int restartDevice(String mac, String device_type);

}
