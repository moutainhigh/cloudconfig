package com.kuangchi.sdd.comm.equipment.base.service;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.commonUtil.StringUtil;
import com.kuangchi.sdd.util.network.HttpRequest;

@Service("getDevInfoService")
public class GetDevInfoService {
	public DeviceInfo2 getManager(String mac) {
		String url = PropertiesToMap.propertyToMap(
				"photoncard_interface.properties").get("url")
				+ "interface/deviceInterface/getDeviceByMac.do?";
		String str = HttpRequest.sendPost(url,
				"device_mac=" + StringUtil.to_MJDevice_Mac(mac));// 远程调用
		DeviceInfo deviceInfo = GsonUtil.toBean(str, DeviceInfo.class);

		DeviceInfo2 deviceInfo2 = new DeviceInfo2();
		deviceInfo2.setBroadcastIP(deviceInfo.getLocal_ip_address());

		deviceInfo2.setEquipmentIP(deviceInfo.getLocal_ip_address());

		deviceInfo2.setEquipmentRecordPort(Integer.valueOf(deviceInfo
				.getLocal_event_port()));
		return deviceInfo2;
	}

}
