package com.kuangchi.sdd.comm.equipment.gate.userGroup;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.gate.group.GetGroupHandler;
import com.kuangchi.sdd.comm.equipment.gate.group.GetGroupManager;
import com.kuangchi.sdd.comm.equipment.gate.group.SetGroupHandler;
import com.kuangchi.sdd.comm.equipment.gate.group.SetGroupManager;
/**
 * 设置用户时段组
 * */
public class SetUserGroupConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new SetUserGroupHandler();
		Manager manager = new SetUserGroupManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
