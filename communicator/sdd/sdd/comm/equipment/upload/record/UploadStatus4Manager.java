package com.kuangchi.sdd.comm.equipment.upload.record;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
/**
 *上传门禁状态
 * */
public class UploadStatus4Manager extends Manager{
   
	public UploadStatus4Manager(DeviceInfo2 deviceInfo) {
		super(deviceInfo);
		// TODO Auto-generated constructor stub
	}
	public ByteBuf sendSetMachineParameter(Object data,
			SendHeader senderPkg) {
		senderPkg.setHeader(0x55);
		senderPkg.setSign(0x04);
		senderPkg.setMac(0x000016);
		senderPkg.setOrder(0x38);
		senderPkg.setLength(0x0000);
		senderPkg.setOrderStatus(0x00);
		
		return super.packageSenderBuf(senderPkg);
	}
    //配置有效数据
	@Override
	public ByteBuf setDataBuf(SendHeader info) {
		return null;
	}
}
