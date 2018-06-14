package com.kuangchi.sdd.commConsole.init.service;

import com.kuangchi.sdd.comm.equipment.common.EquipmentDataForServer;

public interface IEquipmentInit {
    /**
	 * 
	 * Description:初始化控制器设备
	 * date:2016年4月27日
	 * @return
	 */
	public String initSearchEquipment(String mac,String deviceType,EquipmentDataForServer data);//修改设备信息
	public  String reset(String mac,String deviceType);//初始化设备信息
}
