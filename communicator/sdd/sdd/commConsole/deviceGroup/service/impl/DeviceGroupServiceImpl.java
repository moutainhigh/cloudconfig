package com.kuangchi.sdd.commConsole.deviceGroup.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeBlock;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeData;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.actualTime.service.impl.ActualTimeServicempl;
import com.kuangchi.sdd.commConsole.deviceGroup.service.DeviceGroupService;

@Transactional
@Service("deviceGroupServiceImpl")
public class DeviceGroupServiceImpl implements DeviceGroupService {
	
	public static final Logger LOG = Logger.getLogger(DeviceGroupServiceImpl.class);
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	@Override
	public String setDeviceGroup(String mac, List<DeviceTimeBlock> deviceGroup,String sign) {

		// Connector connector = new SetUserGroupConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		//pkg.setSign(0x04);
		pkg.setSign(Util.getIntHex(sign));
		

		// pkg.setMac(0x000016);
		LOG.info("mac地址为："+Util.getIntHex(mac));
		pkg.setMac(Util.getIntHex(mac));// 转换为16进制
		pkg.setOrder(0x26);
		pkg.setLength(0x0201);
		pkg.setOrderStatus(0x00);

		String result = null;
		String result1 = null;
		for (int i = 0; i < deviceGroup.size(); i++) {
		
			if (String.valueOf(deviceGroup.get(i).getBlock()).equals("0")) {// 判断是哪个块
				// 传入设备时段组的块号
				DeviceTimeBlock data = new DeviceTimeBlock();
				data.setBlock(0x00);
				// List<DeviceTimeData> times = new ArrayList<DeviceTimeData>();
				List<DeviceTimeData> deviceTimes0_0 = deviceGroup.get(i)
						.getDeviceTimes();//第0个块的设备时段组信息
				List<DeviceTimeData> deviceTimes0_1 = new ArrayList<DeviceTimeData>();// 一个新的集合，存放第0个块的设备时段组信息

				for (int j = 0; j < 128; j++) {
					if (j < deviceTimes0_0.size()) {
						DeviceTimeData deviceTimeData = new DeviceTimeData();
						deviceTimeData.setHour(Util.getIntHex(""+deviceTimes0_0.get(j).getHour()));
						deviceTimeData.setMinute(Util.getIntHex(""+deviceTimes0_0.get(j).getMinute()));
						deviceTimeData.setActionType(deviceTimes0_0.get(j).getActionType());
						deviceTimeData.setRetain(0xff);
						deviceTimes0_1.add(deviceTimeData);
					} else {// 补足ff
						
						DeviceTimeData deviceTimeData = new DeviceTimeData();
						deviceTimeData.setHour(0xff);
						deviceTimeData.setMinute(0xff);
						deviceTimeData.setActionType(0xff);
						deviceTimeData.setRetain(0xff);
						deviceTimes0_1.add(deviceTimeData);
					}
				}
				data.setDeviceTimes(deviceTimes0_1);

				SendData sendData = new SendData();
				sendData.setDeviceTimeBlock(data);
				pkg.setData(sendData);
				pkg.setCrc(pkg.getCrcFromSum());
				// connector.setHeader(pkg);

				try {
					DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
					result = com.kuangchi.sdd.comm.container.Service.service(
							"set_device_group", pkg,deviceInfo2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// String result = connector.run();
				LOG.info("容器获取的值result0:" + result);

			} else if (String.valueOf(deviceGroup.get(i).getBlock())
					.equals("1")) {

				DeviceTimeBlock data1 = new DeviceTimeBlock();
				data1.setBlock(0x01);
				
				// List<DeviceTimeData> times = new ArrayList<DeviceTimeData>();
				List<DeviceTimeData> deviceTimes1_0 = deviceGroup.get(i)
						.getDeviceTimes();// 一个新的集合
				List<DeviceTimeData> deviceTimes1_1 = new ArrayList<DeviceTimeData>();// 一个新的集合，存放第0个块的设备时段组信息
				
				for (int k = 0; k < 128; k++) {
					if (k < deviceTimes1_0.size()) {
						DeviceTimeData deviceTimeData1 = new DeviceTimeData();
						deviceTimeData1.setHour(Util.getIntHex(""+deviceTimes1_0.get(k).getHour()));
						deviceTimeData1.setMinute(Util.getIntHex(""+deviceTimes1_0.get(k).getMinute()));
						deviceTimeData1.setActionType(deviceTimes1_0.get(k).getActionType());
						deviceTimeData1.setRetain(0xff);
						deviceTimes1_1.add(deviceTimeData1);
					} else {// 补足0
						
						DeviceTimeData deviceTimeData1 = new DeviceTimeData();
						deviceTimeData1.setHour(0xff);
						deviceTimeData1.setMinute(0xff);
						deviceTimeData1.setActionType(0xff);
						deviceTimeData1.setRetain(0xff);
						deviceTimes1_1.add(deviceTimeData1);
					}
				}

				data1.setDeviceTimes(deviceTimes1_1);

				SendData sendData1 = new SendData();
				sendData1.setDeviceTimeBlock(data1);
				pkg.setData(sendData1);
				pkg.setCrc(pkg.getCrcFromSum());
				// connector.setHeader(pkg);

				try {
					DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
					result1 = com.kuangchi.sdd.comm.container.Service.service(
							"set_device_group", pkg,deviceInfo2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// String result = connector.run();
				LOG.info("容器获取的值result1:" + result);
			}
		}

		if (result == null || result1 == null || "null".equals(result)
				|| "null".equals(result1)) {// 判断返回值是否为空
			return "null";
		} else {
			return "succ";
		}
		//return result;

	}

}
