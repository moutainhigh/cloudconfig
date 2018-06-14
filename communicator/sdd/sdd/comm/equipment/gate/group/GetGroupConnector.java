package com.kuangchi.sdd.comm.equipment.gate.group;
import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 读取时段表
 * 
 * */
public class GetGroupConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new GetGroupHandler();
		Manager manager = new GetGroupManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
