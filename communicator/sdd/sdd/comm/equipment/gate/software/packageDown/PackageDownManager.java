package com.kuangchi.sdd.comm.equipment.gate.software.packageDown;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.PackageDown;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
/**
 * 下发软件包    软件下发的流程是 :    开始下发命令--->下发软件包----->结束下发命令
 * 
 * */
public class PackageDownManager extends Manager {

		public PackageDownManager(DeviceInfo2 deviceInfo) {
			 super(deviceInfo);
		}

		@Override
		public ByteBuf setDataBuf(SendHeader info) {
			ByteBuf byteBuf = Unpooled.buffer();
			PackageDown packageDown =info.getData().getPackageDown();
			byteBuf.writeShort(packageDown.getPackageNum());
            if (packageDown.getBytes()!=null) {
				for (int i = 0; i < packageDown.getBytes().length; i++) {
					  byteBuf.writeByte(packageDown.getBytes()[i]);
				}
			}
			return byteBuf;
		}

		@Override
		public ByteBuf sendSetMachineParameter(Object data, SendHeader senderPkg) {
			return super.packageSenderBuf(senderPkg);
		}
		
		
		
}
