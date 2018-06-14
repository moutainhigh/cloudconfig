package com.kuangchi.sdd.comm.equipment.gate.clear;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.ClearData;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeBlock;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 清 空控制器数据
 * */
public class ClearDataManager extends Manager{

	public ClearDataManager(DeviceInfo2 deviceInfo) {
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
		ClearData clearData=info.getData().getClearData();
		 sendBuf.writeByte(clearData.getDataType());
		return sendBuf;
	}
}
