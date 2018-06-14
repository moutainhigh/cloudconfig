package com.kuangchi.sdd.comm.equipment.gate.actualtime;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 设置实时上传参数
 * */
public class SetActualTimeConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler readTimeHandler = new SetActualTimeHandler();
		Manager equipment = new SetActualTimeManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(readTimeHandler, equipment);
		return result;
	}
}
