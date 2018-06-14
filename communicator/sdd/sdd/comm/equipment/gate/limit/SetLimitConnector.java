package com.kuangchi.sdd.comm.equipment.gate.limit;
import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 设置门禁权限 
 * 
 * */
public class SetLimitConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new SetLimitHandler();
		Manager manager = new SetLimitManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
