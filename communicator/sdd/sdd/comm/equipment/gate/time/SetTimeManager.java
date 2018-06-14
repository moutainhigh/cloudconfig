package com.kuangchi.sdd.comm.equipment.gate.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeData;

/**
 * 设置设备时间
 * */
public class SetTimeManager extends Manager {

	public SetTimeManager(DeviceInfo2 deviceInfo) {
		super(deviceInfo);
		// TODO Auto-generated constructor stub
	}
	public ByteBuf sendSetMachineParameter(Object data, SendHeader senderPkg) {
		
		return packageSenderBuf(senderPkg);
	}
    //配置有效数据
	@Override
	public ByteBuf setDataBuf(SendHeader info) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ByteBuf destination = Unpooled.buffer();
		TimeData data = info.getData().getTimeData();
		destination.writeChar(data.getYear());
		destination.writeByte(data.getMonth());
		destination.writeByte(data.getDay());
		destination.writeByte(data.getHour());
		destination.writeByte(data.getMinute());
		destination.writeByte(data.getSecond());
		destination.writeByte(data.getDayOfWeek());
		return destination;
	}
}
