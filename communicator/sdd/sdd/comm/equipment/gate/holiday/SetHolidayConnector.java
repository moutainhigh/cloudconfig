package com.kuangchi.sdd.comm.equipment.gate.holiday;
import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
/**
 * 获取节假日表
 * 
 * */
public class SetHolidayConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler readTimeHandler = new SetHolidayHandler();
		Manager equipment = new SetHolidayManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(readTimeHandler, equipment);
		return result;
	}
}
