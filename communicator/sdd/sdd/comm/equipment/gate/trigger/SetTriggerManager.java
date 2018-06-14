package com.kuangchi.sdd.comm.equipment.gate.trigger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 上位触发开门  ，该类弃用，目前由com\kuangchi\sdd\comm\equipment\gate\remoteOpenDoor包下的类实现远程开门
 * */
public class SetTriggerManager extends Manager{
   
	public SetTriggerManager(DeviceInfo2 deviceInfo) {
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
		sendBuf.writeByte(gateParamData.getClientTrigger());
		return sendBuf;
	}
}
