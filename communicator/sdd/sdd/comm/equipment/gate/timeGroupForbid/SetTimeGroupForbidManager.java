package com.kuangchi.sdd.comm.equipment.gate.timeGroupForbid;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.RemoteOpenDoor;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeForbid;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupForbid;
/**
 * 设置时段禁止设备上报
 * */
public class SetTimeGroupForbidManager extends Manager {
	public SetTimeGroupForbidManager(DeviceInfo2 deviceInfo) {
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
		TimeGroupForbid timeGroupForBid=info.getData().getTimeGroupForbid();
		sendBuf.writeByte(timeGroupForBid.getEnable());
		for (int i = 0; i < info.getData().getTimeGroupForbid().getTimeForbids().length; i++) {
			TimeForbid timeForbid=info.getData().getTimeGroupForbid().getTimeForbids()[i];
			sendBuf.writeByte(timeForbid.getStartHour());
			sendBuf.writeByte(timeForbid.getStartMinute());
			sendBuf.writeByte(timeForbid.getEndHour());
			sendBuf.writeByte(timeForbid.getEndMinute());
		}
		
		return sendBuf;
	}
}
