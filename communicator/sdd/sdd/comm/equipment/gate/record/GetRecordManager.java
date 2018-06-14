package com.kuangchi.sdd.comm.equipment.gate.record;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateLimitData;
import com.kuangchi.sdd.comm.equipment.common.GateRecordData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 读取门禁记录条数 
 * 
 * */
public class GetRecordManager extends Manager{
   
	public GetRecordManager(DeviceInfo2 deviceInfo) {
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
		return null;
	}
}
