package com.kuangchi.sdd.comm.equipment.gate.doubleInOut;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 读取双向进出
 * */
public class GetDoubleInOutConnector extends Connector{

	@Override
	public String run() throws Exception {
		BaseHandler doubleInOutHandler = new GetDoubleInOutHandler();
		Manager equipment = new GetDoubleInOutManager(this.getDeviceInfo());
		String result = super.connect(doubleInOutHandler, equipment);
		return result;
	}

}
