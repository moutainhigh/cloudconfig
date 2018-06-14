package com.kuangchi.sdd.comm.equipment.gate.group;
import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 设置时段表
 * 
 * */
public class SetGroupConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new SetGroupHandler();
		Manager manager = new SetGroupManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
