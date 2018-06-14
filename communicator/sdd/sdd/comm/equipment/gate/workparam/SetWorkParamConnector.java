package com.kuangchi.sdd.comm.equipment.gate.workparam;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 *设置门工作参数
 * */
public class SetWorkParamConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new SetWorkParamHandler();
		Manager manager = new SetWorkParamManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
