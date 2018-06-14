package com.kuangchi.sdd.comm.equipment.gate.timeGroupForbid;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
import com.kuangchi.sdd.comm.equipment.gate.time.SetTimeHandler;
import com.kuangchi.sdd.comm.equipment.gate.time.SetTimeManager;
/**
 * 设置时段禁止设备上报
 * */
public class SetTimeGroupForbidConnector extends Connector {
	@Override
	public String run() throws Exception {
		BaseHandler<ByteBuf,GateTimeInterface,ReceiveData> setTimeGroupHandler = new SetTimeGroupForbidHandler();
		Manager equipment = new SetTimeGroupForbidManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(setTimeGroupHandler, equipment);
		return result;
	}
}
