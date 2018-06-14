package com.kuangchi.sdd.comm.equipment.gate.remoteOpenDoor;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.gate.record.GetRecordHandler;
import com.kuangchi.sdd.comm.equipment.gate.record.GetRecordManager;
/**
 * 远程开门
 * 
 * */
public class SetRemoteOpenDoorConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new SetRemoteOpenDoorHandler();
		Manager manager = new SetRemoteOpenDoorManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
