package com.kuangchi.sdd.comm.equipment.gate.limit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateLimitData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 获取门禁权限 
 * 
 * */
public class GetLimitManager extends Manager{
   
	public GetLimitManager(DeviceInfo2 deviceInfo) {
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
		GateLimitData data = info.getData().getGateLimitData();
	//	sendBuf.writeMedium(data.getCardId());
		sendBuf.writeInt(data.getCardId());
		return sendBuf;
	}
}
