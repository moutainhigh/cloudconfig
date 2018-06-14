package com.kuangchi.sdd.comm.equipment.gate.clear;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.gate.deviceGroup.SetDeviceGroupHandler;
import com.kuangchi.sdd.comm.equipment.gate.deviceGroup.SetDeviceGroupManager;
/**
 * 清 空控制器数据
 * */
public class ClearDataConnector extends Connector {

	@Override
	public String run() throws Exception {
		BaseHandler handler = new ClearDataHandler();
		Manager manager = new ClearDataManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}

}
