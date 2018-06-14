package com.kuangchi.sdd.commConsole.doubleInOut.service.impl;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.doubleInOut.service.DoubleInOutService;
@Transactional
@Service("doubleInOutServicempl")
public class DoubleInOutServicempl implements DoubleInOutService{
	public static final Logger LOG = Logger.getLogger(DoubleInOutServicempl.class);
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	@Override
	public String setDoubleInOut(String mac,String deviceType,String inOut) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Integer.parseInt(deviceType));
		//pkg.setMac(0x000016);
		pkg.setMac(Util.getIntHex(mac));//转换为16进制
		
		pkg.setOrder(0x37);
		pkg.setLength(0x0001);
		pkg.setOrderStatus(0x00);

		//配置有效数据
		GateParamData data = new GateParamData();
		
		//data.setInout(0x00); //
		data.setInout((Integer.parseInt(inOut))); //
        SendData sendData = new SendData();
        sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());

		String result=null;
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service("set_double_in_out", pkg,deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.info("容器获取的值:" + result);
        return result;		
	}

	@Override
	public String getDoubleInOut(String mac,String deviceType) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Integer.parseInt(deviceType));
		//pkg.setMac(0x000016);
		pkg.setMac(Util.getIntHex(mac));//转换为16进制
		pkg.setOrder(0x38);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);

		//配置有效数据
		GateParamData data = new GateParamData();
        SendData sendData = new SendData();
        sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());

		String result=null;
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service("get_double_in_out", pkg,deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.info("容器获取的值:" + result);
        return result;		
	}
}
