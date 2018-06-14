package com.kuangchi.sdd.comm.equipment.gate.status;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 目前没有用,项目中实际使用的设备状态是自动上报协议 ，该条协议是主动获取协议
 * */
public class GetStatusConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler readTimeHandler = new GetStatusHandler();
		Manager equipment = new GetStatusManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(readTimeHandler, equipment);
		return result;
	}
}
