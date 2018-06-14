package com.kuangchi.sdd.comm.equipment.gate.timeGroupForbid;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
/**
 * 读取时段禁止设备上报
 * */
public class GetTimeGroupForbidConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler<ByteBuf,GateTimeInterface,ReceiveData> getTimeGroupHandler = new GetTimeGroupForbidHandler();
		Manager equipment = new GetTimeGroupForbidManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(getTimeGroupHandler, equipment);
		return result;
	}
}   
