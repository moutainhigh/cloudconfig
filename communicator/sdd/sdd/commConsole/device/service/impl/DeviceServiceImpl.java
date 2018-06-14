package com.kuangchi.sdd.commConsole.device.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.comm.container.Service;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.ClearData;
import com.kuangchi.sdd.comm.equipment.common.RemoteOpenDoor;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.device.service.IDeviceService;

@Transactional
@org.springframework.stereotype.Service("deviceServiceImpl")
public class DeviceServiceImpl implements IDeviceService {
	
	public static final Logger LOG = Logger.getLogger(DeviceServiceImpl.class);

	@Resource(name = "getDevInfoService")
	GetDevInfoService getDevInfoService;
	
	@Override
	public boolean clearData(String sign, String mac, String dataType){
		try {
			SendHeader header = new SendHeader();
			header.setHeader(0x55);
			header.setSign(Integer.valueOf(sign));
			header.setMac(Util.getIntHex(mac));
			header.setOrder(0x2B);
			header.setLength(0x0001);
			header.setOrderStatus(0x00);
			
			//0x01: 清除门禁记录      0x02: 清除巡更记录     0x04:清除刷卡记录       0x08:清除门禁权限       0x10: 清除巡更权限
			ClearData data = new ClearData();
			data.setDataType(Integer.valueOf(dataType));
			
			SendData sendData = new SendData();
			sendData.setClearData(data);
			header.setData(sendData);
			header.setCrc(header.getCrcFromSum());
			//connector.setHeader(header);
	        DeviceInfo2 deviceInfo2 = getDevInfoService.getManager(mac);
	        String result = Service.service("clear_data", header, deviceInfo2);
	        if(!"".equals(result) && !"null".equals(result))
	        	return true;
	        else
	        	return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean remoteOpenDoor(String sign, String mac, String door_num) {
		try {
			//Connector connector = new SetUserGroupConnector();
			SendHeader header = new SendHeader();
			header.setHeader(0x55);
			header.setSign(Integer.valueOf(sign));
			header.setMac(Util.getIntHex(mac));
			header.setOrder(0x2D);
			header.setLength(0x0001);
			header.setOrderStatus(0x00);
			
			RemoteOpenDoor data = new RemoteOpenDoor();
			int door=1<<(Integer.parseInt(door_num)-1);
			data.setDoor(door);
			
			SendData sendData = new SendData();
			sendData.setDoorData(data);
			header.setData(sendData);
			header.setCrc(header.getCrcFromSum());
			//connector.setHeader(header);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager(mac);
			String result = Service.service("set_remote_open_door", header, deviceInfo2);
			LOG.info("容器获取的值:" + result);
			if(!"".equals(result) && !"null".equals(result))
	        	return true;
	        else
	        	return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
