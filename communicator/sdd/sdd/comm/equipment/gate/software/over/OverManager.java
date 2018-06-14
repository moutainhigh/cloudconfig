package com.kuangchi.sdd.comm.equipment.gate.software.over;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 结束下发软件命令
 * 
 * */
public class OverManager extends Manager {
	public OverManager(DeviceInfo2 deviceInfo) {
		 super(deviceInfo);
	}

	@Override
	public ByteBuf setDataBuf(SendHeader info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteBuf sendSetMachineParameter(Object data, SendHeader senderPkg) {
		return super.packageSenderBuf(senderPkg);
	}
	
}
