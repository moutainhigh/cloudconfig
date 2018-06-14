package com.kuangchi.sdd.comm.equipment.gate.deviceGroup;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeBlock;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.UserTimeBlock;
/**
 * 设置设备时段组
 * */
public class SetDeviceGroupManager extends Manager{
	
	public SetDeviceGroupManager(DeviceInfo2 deviceInfo) {
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
		DeviceTimeBlock block = info.getData().getDeviceTimeBlock();
		sendBuf.writeByte(block.getBlock());
		List<DeviceTimeData> times = block.getDeviceTimes();
	
		for(DeviceTimeData time:times){
			sendBuf.writeByte(time.getHour());
			sendBuf.writeByte(time.getMinute());
			sendBuf.writeByte(time.getRetain());
			sendBuf.writeByte(time.getActionType());
			//sendBuf.writeByte(0x00);
		}
		return sendBuf;
	}
}
