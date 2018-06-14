package com.kuangchi.sdd.comm.equipment.gate.trigger;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 上位触发开门  ，该类弃用，目前由com\kuangchi\sdd\comm\equipment\gate\remoteOpenDoor包下的类实现远程开门
 * */
public class SetTriggerConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler readTimeHandler = new SetTriggerHandler();
		Manager equipment = new SetTriggerManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(readTimeHandler, equipment);
		return result;
	}
}
