package com.kuangchi.sdd.commConsole.deviceGroup.service;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.common.DeviceTimeBlock;



/**
 * 获取门禁控制器的接口服务
 * @author 
 *
 */
public interface DeviceGroupService {
	/**
	 * 
	 * Description:设置设备时段组
	 * date:2016年5月27日
	 * @return
	 */
	public String setDeviceGroup(String mac,List<DeviceTimeBlock> deviceGroup,String sign);
}
