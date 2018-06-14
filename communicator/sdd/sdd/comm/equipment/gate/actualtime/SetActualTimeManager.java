package com.kuangchi.sdd.comm.equipment.gate.actualtime;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 * 设置控制器时间的参数配置类
 * @author yu.yao
 *
 */
public class SetActualTimeManager extends Manager{
   
	public SetActualTimeManager(DeviceInfo2 deviceInfo) {
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
		sendBuf.writeChar(info.getData().getGateParamData().getActualTime());//有效数据 2字节
		return sendBuf;
	}
}
