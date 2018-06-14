package com.kuangchi.sdd.comm.equipment.gate.holiday;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupData;

/**
 * 获取节假日表
 * 
 * */
public class SetHolidayManager extends Manager {

	public SetHolidayManager(DeviceInfo2 deviceInfo) {
		super(deviceInfo);
		// TODO Auto-generated constructor stub
	}
	public ByteBuf sendSetMachineParameter(Object data, SendHeader senderPkg) {
		
		return packageSenderBuf(senderPkg);
	}
    //配置有效数据
	@Override
	public ByteBuf setDataBuf(SendHeader info) {
		ByteBuf sendBuf = Unpooled.buffer();
		if(info == null)
			return null;
		TimeBlock block = info.getData().getTimeBlock();
		List<TimeGroupData> groups = block.getGroups();
		TimeData time;
		for(TimeGroupData g:groups){
			time = g.getStart();
			sendBuf.writeByte(time.getYear());
			sendBuf.writeByte(time.getMonth());
			sendBuf.writeByte(time.getDay());
			sendBuf.writeByte(time.getDayOfWeek());
		}
		return sendBuf;
	}
}
