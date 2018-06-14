package com.kuangchi.sdd.comm.equipment.gate.fire;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 设置取消消防
 * */
public class SetFireFlagConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler readTimeHandler = new SetFireFlagHandler();
		Manager equipment = new SetFireFlagManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(readTimeHandler, equipment);
		return result;
	}
}
