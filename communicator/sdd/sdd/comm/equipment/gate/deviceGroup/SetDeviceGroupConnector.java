package com.kuangchi.sdd.comm.equipment.gate.deviceGroup;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.gate.userGroup.SetUserGroupHandler;
import com.kuangchi.sdd.comm.equipment.gate.userGroup.SetUserGroupManager;
/**
 * 设置设备时段组
 * */
public class SetDeviceGroupConnector  extends Connector {
	@Override
	public String run() throws Exception {
		BaseHandler handler = new SetDeviceGroupHandler();
		Manager manager = new SetDeviceGroupManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
