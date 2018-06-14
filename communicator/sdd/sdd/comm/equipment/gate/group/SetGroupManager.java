package com.kuangchi.sdd.comm.equipment.gate.group;

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
 * 设置时段表
 * 
 * */
public class SetGroupManager extends Manager{
   
	public SetGroupManager(DeviceInfo2 deviceInfo) {
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
		TimeBlock block = info.getData().getTimeBlock();
		sendBuf.writeByte(block.getBlock());
		List<TimeGroupData> groups = block.getGroups();
		TimeData start;
		TimeData end;
		for(TimeGroupData g:groups){
			start = g.getStart();
			end = g.getEnd();
			sendBuf.writeByte(start.getHour());
			sendBuf.writeByte(start.getMinute());
			sendBuf.writeByte(end.getHour());
			sendBuf.writeByte(end.getMinute());
		}
		return sendBuf;
	}
}
