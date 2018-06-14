package com.kuangchi.sdd.comm.equipment.gate.restoreDevice;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.gate.remoteOpenDoor.SetRemoteOpenDoorHandler;
import com.kuangchi.sdd.comm.equipment.gate.remoteOpenDoor.SetRemoteOpenDoorManager;
/**
 * 设备复位
 * 
 * */
public class RestoreDeviceConnector extends Connector {
	@Override
	public String run() throws Exception {
		BaseHandler handler = new RestoreDeviceHandler();
		Manager manager = new RestoreDeviceManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
