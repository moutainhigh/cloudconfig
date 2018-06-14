package com.kuangchi.sdd.comm.equipment.gate.search;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 搜索设备
 * 
 * */
public class SearchEquipmentManager extends Manager{
	
	
	
	public SearchEquipmentManager(DeviceInfo2 deviceInfo) {
		super(deviceInfo);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 搜索门禁控制器 传入MAC地址，传入0xFFFFFF代表搜索所有设备
	 */
	@Override
	public ByteBuf sendSetMachineParameter(Object data, SendHeader senderPkg) {
		// TODO Auto-generated method stub
		return super.packageSenderBuf(senderPkg);
	}
	@Override
	public ByteBuf setDataBuf(SendHeader info) {
		// TODO Auto-generated method stub
		return null;
	}
}
