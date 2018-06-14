package com.kuangchi.sdd.comm.equipment.gate.time;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 读取设备时间
 * */
public class ReadTimeConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler readTimeHandler = new ReadTimeHandler();
		Manager equipment = new ReadTimeManager(this.getDeviceInfo());//设备接口类
		String result = super.connectGetTime(readTimeHandler, equipment);
		return result;
	}
}
