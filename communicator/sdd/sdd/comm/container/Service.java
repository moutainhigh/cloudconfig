package com.kuangchi.sdd.comm.container;
import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.gate.search.SearchEquipmentConnector;
import com.kuangchi.sdd.util.excel.ExcelExportServer;
/**
 * 获取门禁控制器的接口服务
 * @author yu.yao
 *
 */
public class Service {
	public static final Logger LOG = Logger.getLogger(Service.class);	
	public static String service(String connectorType,SendHeader header,DeviceInfo2 deviceInfo) throws Exception {
		Connector connector = ConnectorFactory.getConnector(connectorType);
		connector.setHeader(header);
	    connector.setDeviceInfo(deviceInfo);
		String result = "";
		result += connector.run();
		LOG.info("执行Service");
		LOG.info("返回Service的结果："+result);
        return result;		
	}
}
