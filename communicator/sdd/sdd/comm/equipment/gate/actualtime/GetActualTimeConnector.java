package com.kuangchi.sdd.comm.equipment.gate.actualtime;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 获取实时上传参数
 * */
public class GetActualTimeConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler readTimeHandler = new GetActualTimeHandler();
		Manager equipment = new GetActualTimeManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(readTimeHandler, equipment);
		return result;
	}
}
