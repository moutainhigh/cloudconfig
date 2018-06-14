package com.kuangchi.sdd.comm.equipment.gate.respondRecord;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.RespondRecord;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.UserTimeBlock;
/**
 * 回复设备上报记录
 * 
 * */
public class RespondRecordManager  extends Manager {

	public RespondRecordManager(DeviceInfo2 deviceInfo) {
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
		RespondRecord record = info.getData().getRespondRecord();
		sendBuf.writeByte(record.getRecordType());
		sendBuf.writeInt(Long.valueOf(record.getRecordId()).intValue());
		
		return sendBuf;
	}
}
