package com.kuangchi.sdd.commConsole.first.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.first.service.IFirstService;
@Transactional
@Service("firstServiceImpl")
public class FirstServiceImpl implements IFirstService{
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	@Override
	public String setFirst(String mac,String sign,String gateId,String first) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(sign));
		//pkg.setMac(0x0000017);
		pkg.setMac(Util.getIntHex(mac));//转换为16进制
		pkg.setOrder(0x36);
		pkg.setLength(0x0002);
		pkg.setOrderStatus(0x00);
		
		//配置有效数据
		GateParamData data = new GateParamData();
		data.setGateId(Util.getIntHex(gateId)-1);//门号
		data.setFirst(Util.getIntHex(first)); // 是否首卡开门参数 0x00:取消首卡开门;	0x01:设置首卡开门.
        SendData sendData = new SendData();
        sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());

		String result=null;
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service("set_first", pkg,deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;				
	}

}
