package com.kuangchi.sdd.comm.equipment.gate.software.over;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;

/**
 * 结束下发软件命令
 * 
 * */
public class OverConnector  extends Connector {
	@Override
	public String run() throws Exception {
		BaseHandler overHandler = new OverHandler();
		Manager equipment = new OverManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(overHandler, equipment);
		return result;
	}
}
