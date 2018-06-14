package com.kuangchi.sdd.comm.equipment.gate.holiday;
import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 读取节假日表
 * 
 * */
public class GetHolidayConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new GetHolidayHandler();
		Manager manager = new GetHolidayManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
