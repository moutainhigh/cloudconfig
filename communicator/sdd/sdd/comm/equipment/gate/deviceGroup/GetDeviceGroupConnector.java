package com.kuangchi.sdd.comm.equipment.gate.deviceGroup;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 读取设备时段组
 * */
public class GetDeviceGroupConnector   extends Connector {
	@Override
	public String run() throws Exception {
		BaseHandler handler = new GetDeviceGroupHandler();
		Manager manager = new GetDeviceGroupManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
