package com.kuangchi.sdd.comm.equipment.gate.workparam;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 获取门工作参数
 * */
public class GetWorkParamConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler readTimeHandler = new GetWorkParamHandler();
		Manager equipment = new GetWorkParamManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(readTimeHandler, equipment);
		return result;
	}
}
