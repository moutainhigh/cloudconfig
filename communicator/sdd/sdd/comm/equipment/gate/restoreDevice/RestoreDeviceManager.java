package com.kuangchi.sdd.comm.equipment.gate.restoreDevice;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.RemoteOpenDoor;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 设备复位
 * 
 * */
public class RestoreDeviceManager  extends Manager{
	public RestoreDeviceManager(DeviceInfo2 deviceInfo) {
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
