package com.kuangchi.sdd.comm.equipment.gate.userGroup;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupData;
import com.kuangchi.sdd.comm.equipment.common.UserTimeBlock;
/**
 * 设置用户时段组
 * */
public class SetUserGroupManager extends Manager {
	
	
	public SetUserGroupManager(DeviceInfo2 deviceInfo) {
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
		UserTimeBlock block = info.getData().getUserTimeBlock();
		sendBuf.writeByte(block.getBlock());
		List<Integer> times = block.getTimes();
	
		for(Integer time:times){
			sendBuf.writeByte(time);
		}
		return sendBuf;
	}
}
