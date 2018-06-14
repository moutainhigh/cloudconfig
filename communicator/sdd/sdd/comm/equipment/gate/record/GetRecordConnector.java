package com.kuangchi.sdd.comm.equipment.gate.record;
import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
/**
 * 读取门禁记录条数 
 * 
 * */
public class GetRecordConnector extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new GetRecordHandler();
		Manager manager = new GetRecordManager(this.getDeviceInfo());//设备接口类
		String result = super.connect(handler, manager);
		return result;
	}
}
