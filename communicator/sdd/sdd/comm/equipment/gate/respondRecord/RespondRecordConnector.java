package com.kuangchi.sdd.comm.equipment.gate.respondRecord;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.gate.record.GetRecordHandler;
import com.kuangchi.sdd.comm.equipment.gate.record.GetRecordManager;
/**
 * 回复设备上报记录
 * 
 * */
public class RespondRecordConnector  extends Connector{
	@Override
	public String run() throws Exception {
		BaseHandler handler = new RespondRecordHandler();
		handler.setShouldClose(true);
		Manager manager = new RespondRecordManager(this.getDeviceInfo());//设备接口类
		super.connectEvent(handler, manager);
		return null;
	//	return result;
	}
}
