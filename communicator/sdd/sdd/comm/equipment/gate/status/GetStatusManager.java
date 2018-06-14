package com.kuangchi.sdd.comm.equipment.gate.status;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
/**
 * 目前没有用,项目中实际使用的设备状态是自动上报协议 ，该条协议是主动获取协议
 * */
public class GetStatusManager extends Manager{
   
	public GetStatusManager(DeviceInfo2 deviceInfo) {
		super(deviceInfo);
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
