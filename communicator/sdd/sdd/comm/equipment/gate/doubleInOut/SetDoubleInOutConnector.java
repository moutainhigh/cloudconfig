package com.kuangchi.sdd.comm.equipment.gate.doubleInOut;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.gate.actualtime.GetActualTimeHandler;
import com.kuangchi.sdd.comm.equipment.gate.actualtime.GetActualTimeManager;
/**
 * 设置双向进出
 * */
public class SetDoubleInOutConnector extends Connector{

	@Override
	public String run() throws Exception {
		BaseHandler doubleInOutHandler = new SetDoubleInOutHandler();
		Manager equipment = new SetDoubleInOutManager(this.getDeviceInfo());
		String result = super.connect(doubleInOutHandler, equipment);
		return result;
	}

}
