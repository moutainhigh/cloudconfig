package com.kuangchi.sdd.commConsole.fire.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.fire.service.IFrieService;
@Transactional
@Service("fireServiceImpl")
public class FireServiceImpl implements IFrieService{
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	@Override
	public String setFire(String mac,String useFireFighting,String sign) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		//pkg.setSign(0x04);
		
		pkg.setSign(Util.getIntHex(sign));
		//pkg.setMac(0x0000016);
		pkg.setMac(Util.getIntHex(mac));//转换为16进制
		pkg.setOrder(0x2c);
		pkg.setLength(0x0001);
		pkg.setOrderStatus(0x00);
		
		//配置有效数据
		GateParamData data = new GateParamData();
		data.setUseFireFighting(Util.getIntHex(useFireFighting)); // 是否启用消防
		SendData sendData = new SendData();
		sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
				
		String result=null;
		
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service("set_fire", pkg,deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;		
	}

}
