package com.kuangchi.sdd.comm.equipment.gate.first;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 设置首卡开门
 * */
public class SetFirstManager extends Manager{
   
	public SetFirstManager(DeviceInfo2 deviceInfo) {
		super(deviceInfo);
		// TODO Auto-generated constructor stub
	}
	public ByteBuf sendSetMachineParameter(Object data,
			SendHeader senderPkg) {
		return super.packageSenderBuf(senderPkg);
	}
    //配置有效数据
	@Override
	public ByteBuf setDataBuf(SendHeader info) {
		ByteBuf sendBuf = Unpooled.buffer();
		GateParamData gateParamData = info.getData().getGateParamData();
		sendBuf.writeByte(gateParamData.getGateId());//设置门号
		sendBuf.writeByte(gateParamData.getFirst()); //设置上位触发
		return sendBuf;
	}
}
