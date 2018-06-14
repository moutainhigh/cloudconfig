package com.kuangchi.sdd.comm.equipment.gate.remoteOpenDoor;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeBlock;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeData;
import com.kuangchi.sdd.comm.equipment.common.RemoteOpenDoor;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 远程开门
 * 
 * */
public class SetRemoteOpenDoorManager  extends Manager {
	public SetRemoteOpenDoorManager(DeviceInfo2 deviceInfo) {
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
		RemoteOpenDoor doorData=info.getData().getDoorData();
		sendBuf.writeByte(doorData.getDoor());
		return sendBuf;
	}
}
