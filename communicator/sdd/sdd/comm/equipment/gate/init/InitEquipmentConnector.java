package com.kuangchi.sdd.comm.equipment.gate.init;
import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 设置通讯参数
 * 
 * */
public class InitEquipmentConnector extends Connector{

	public String run() throws Exception {
		BaseHandler handler = new InitEquipmentHandler();
		Manager equipment = new InitEquipmentManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, equipment);
		return result;
	}
}
