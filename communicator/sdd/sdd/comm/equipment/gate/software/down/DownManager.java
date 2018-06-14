package com.kuangchi.sdd.comm.equipment.gate.software.down;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 开始下发软件命令
 * 
 * */
public class DownManager  extends Manager{

	public DownManager(DeviceInfo2 deviceInfo) {
		 super(deviceInfo);
	}
	
	@Override
	public ByteBuf setDataBuf(SendHeader senderPkg) {
		
		return null;
	}

	@Override
	public ByteBuf sendSetMachineParameter(Object data, SendHeader senderPkg) {
		return super.packageSenderBuf(senderPkg);
	}

}
