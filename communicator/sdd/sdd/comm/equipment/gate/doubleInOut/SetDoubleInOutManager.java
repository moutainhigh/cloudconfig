package com.kuangchi.sdd.comm.equipment.gate.doubleInOut;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 设置双向进出
 * */
public class SetDoubleInOutManager  extends Manager {

	public SetDoubleInOutManager(DeviceInfo2 deviceInfo) {
		super(deviceInfo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ByteBuf setDataBuf(SendHeader info) {
		ByteBuf sendBuf = Unpooled.buffer();
		GateParamData gateParamData = info.getData().getGateParamData();
		sendBuf.writeByte(gateParamData.getInout());//设置双向进出
		return sendBuf;
	}

	@Override
	public ByteBuf sendSetMachineParameter(Object data, SendHeader senderPkg) {
		
		return super.packageSenderBuf(senderPkg);
	}

}
