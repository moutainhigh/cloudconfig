package com.kuangchi.sdd.comm.equipment.gate.first;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 设置首卡开门
 * */
public class SetFirstConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler readTimeHandler = new SetFirstHandler();
		Manager equipment = new SetFirstManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(readTimeHandler, equipment);
		return result;
	}
}
