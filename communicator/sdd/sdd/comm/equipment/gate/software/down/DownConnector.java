package com.kuangchi.sdd.comm.equipment.gate.software.down;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;

/**
 * 开始下发软件命令
 * 
 * */
public class DownConnector  extends Connector {
	@Override
	public String run() throws Exception {
		BaseHandler downHandler = new DownHandler();
		Manager equipment = new DownManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(downHandler, equipment);
		return result;
	}
}
