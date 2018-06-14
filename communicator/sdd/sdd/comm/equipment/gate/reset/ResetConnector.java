package com.kuangchi.sdd.comm.equipment.gate.reset;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 初始化设备
 * 
 * */
public class ResetConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new ResetHandler();
		Manager manager = new ResetManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
