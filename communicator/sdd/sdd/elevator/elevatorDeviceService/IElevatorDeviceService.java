package com.kuangchi.sdd.elevator.elevatorDeviceService;

import java.util.List;

import com.kuangchi.sdd.elevator.model.Device;
import com.kuangchi.sdd.elevator.model.HardWareParam;
import com.kuangchi.sdd.elevator.model.Result;

public interface IElevatorDeviceService {
	/**
	 * 搜索梯控设备
	 * 
	 * @author xuewen.deng
	 */
	public List<Device> tk_SearchControls(String ip);

	/**
	 * 修改梯控设备
	 * 
	 * @author xuewen.deng
	 */
	public Result tk_UpdateControl(Device device, String value);

	/**
	 * 初始化梯控设备
	 * 
	 * @author xuewen.deng
	 */
	public Result tk_SystemInit(Device device);

	/**
	 * 发送动作参数（硬件参数）给梯控设备
	 * 
	 * @author xuewen.deng
	 */
	public Result tk__SendHardwareParam(Device device,
			HardWareParam hardWareParam);

}
