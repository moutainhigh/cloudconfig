package com.kuangchi.sdd.comm.equipment.gate.userGroup;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 读取用户时段组
 * */
public class GetUserGroupConnector extends Connector {
	@Override
	public String run() throws Exception {
		BaseHandler handler = new GetUserGroupHandler();
		Manager manager = new GetUserGroupManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
