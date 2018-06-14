package com.kuangchi.sdd.commConsole.actualTime.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.actualTime.model.ActualTimeBean;
import com.kuangchi.sdd.commConsole.actualTime.service.IActualTimeService;
import com.kuangchi.sdd.commConsole.device.service.impl.DeviceServiceImpl;
import com.kuangchi.sdd.commConsole.fire.service.IFrieService;
import com.kuangchi.sdd.commConsole.search.model.EquipmentBean;
@Transactional
@Service("actualTimeServicempl")
public class ActualTimeServicempl implements IActualTimeService{
	
	public static final Logger LOG = Logger.getLogger(ActualTimeServicempl.class);
	
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	@Override
	public String setActualTime(String mac,String sign,int actualTime) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(sign));
		//pkg.setMac(0x0000017);
		pkg.setMac(Util.getIntHex(mac));//转换为16进制
		pkg.setOrder(0x34);
		pkg.setLength(0x0002);
		pkg.setOrderStatus(0x00);

		//配置有效数据
		GateParamData data = new GateParamData();
		/*
		 * 0xCC：实时上传参数。  
		 * -bit0：实时状态上传参数；	
		 * -bit1：巡更、门禁事件记录上传参数；	
		 * -bit2：刷卡记录上传参数。     
		 * -0：不上传； 1：上传。
		 * 
　　     * 0xTT： 上传间隔事件，以100毫秒为单位。最大间隔25.6秒
		 */
		data.setActualTime(actualTime); // 是否启用读取实时上传参数
        SendData sendData = new SendData();
        sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());

		String result=null;
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service("set_actualtime", pkg,deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;	
	}

	@Override
	public ActualTimeBean getActualTime(String mac, String device_type) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(device_type));
		//pkg.setMac(0x0000017);
		pkg.setMac(Util.getIntHex(mac));//转换为16进制
		pkg.setOrder(0x33);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());

		String result=null;
		ActualTimeBean allActualTime=new ActualTimeBean();
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service("get_actualtime", pkg,deviceInfo2);
			/*List<String> allStrings=new ArrayList<String>();
			StringTokenizer st=new StringTokenizer(result,"|");
			while(st.hasMoreTokens()){
				allStrings.add(st.nextToken());
			}*/
			//Gson gson=new Gson();
			//java.lang.reflect.Type type = new TypeToken<ActualTimeBean>() {}.getType(); 
			//allActualTime=gson.fromJson(result, type);
			allActualTime=GsonUtil.toBean(result, ActualTimeBean.class);
			LOG.info(result+"-------------"+allActualTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allActualTime;
	}

}
