package com.kuangchi.sdd.comm.equipment.gate.software.packageDown;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;

/**
 * 下发软件包   开始下发命令--->下发软件包----->结束下发命令
 * 
 * */
public class PackageDownConnector  extends Connector{

	@Override
	public String run() throws Exception {
		 
		BaseHandler packageDownHandler = new PackagedownHandler();
		Manager equipment = new PackageDownManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(packageDownHandler, equipment);
		return result;
	}
   
}
