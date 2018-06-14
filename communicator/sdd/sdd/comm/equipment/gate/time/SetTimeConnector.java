package com.kuangchi.sdd.comm.equipment.gate.time;
import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
/**
 * 设置设备时间
 * */
public class SetTimeConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler<ByteBuf,GateTimeInterface,ReceiveData> readTimeHandler = new SetTimeHandler();
		Manager equipment = new SetTimeManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(readTimeHandler, equipment);
		return result;
	}
}
