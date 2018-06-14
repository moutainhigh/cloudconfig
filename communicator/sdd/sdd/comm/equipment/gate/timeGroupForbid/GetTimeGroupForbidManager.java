package com.kuangchi.sdd.comm.equipment.gate.timeGroupForbid;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeForbid;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupForbid;
/**
 * 读取时段禁止设备上报
 * */
public class GetTimeGroupForbidManager  extends Manager {
	public GetTimeGroupForbidManager(DeviceInfo2 deviceInfo) {
		
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
