package com.kuangchi.sdd.commConsole.gateWorkParam.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

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
import com.kuangchi.sdd.commConsole.gateWorkParam.service.IGateWorkParam;

@Service("gateWorkParamServiceImpl")
public class GateWorkParamServiceImpl implements IGateWorkParam {
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	@Override
	public String getGateWorkParam(String sign,String mac, String gateId) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(sign));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x14);
		pkg.setLength(0x0001);
		pkg.setOrderStatus(0x00);
		// 配置有效数据
		GateParamData data = new GateParamData();
		data.setGateId(Util.getIntHex(String.valueOf((Integer.valueOf(gateId)-1))));
		SendData sendData = new SendData();
		sendData.setGateParamData(data);
		pkg.setData(sendData);
		// 校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		//GateParamData gateWorkParamData=new GateParamData();
		String result=null;
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			 result = com.kuangchi.sdd.comm.container.Service.service("get_workparam", pkg,deviceInfo2);
			/*List<String> allStrings=new ArrayList<String>();
			StringTokenizer st=new StringTokenizer(result,"|");
			while(st.hasMoreTokens()){
				allStrings.add(st.nextToken());
			}
			Gson gson=new Gson();
			java.lang.reflect.Type type = new TypeToken<List<GateParamData>>() {}.getType(); 
			gateWorkParamDataList=gson.fromJson(allStrings.toString(), type);*/
			//gateWorkParamData=GsonUtil.toBean(result, GateParamData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	
	}

	@Override
	public int setGateWorkParam(String mac,String sign,
			GateParamData gateParamData) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(sign));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x15);
		pkg.setLength(0x002e);
		pkg.setOrderStatus(0x00);
		String result="";
		// 配置有效数据
		GateParamData data = new GateParamData();
		data.setGateId(gateParamData.getGateId()); // 门号
		data.setUseSuperPassword(gateParamData.getUseSuperPassword());// 是否起用超级开门密码
		data.setSuperPassword(gateParamData.getSuperPassword()); // 超级开门密码 4字节
		data.setUseForcePassword(gateParamData.getUseForcePassword());// 是否启用胁迫密码
		data.setForcePassword(gateParamData.getForcePassword()); // 胁迫密码 2字节
		data.setRelockPassword(gateParamData.getRelockPassword());// 重锁 2字节
		data.setUnlockPassword(gateParamData.getUnlockPassword());// 解锁 2字节
		data.setPolicePassword(gateParamData.getPolicePassword());// 报警密码 2字节
		data.setOpenPattern(gateParamData.getOpenPattern());// 功能模式
		data.setCheckOpenPattern(gateParamData.getCheckOpenPattern());// 验证开门模式
		data.setWorkPattern(gateParamData.getWorkPattern());// 工作模式 1字节
		data.setOpenDelay(gateParamData.getOpenDelay()); // 开门延时 2字节
		data.setOpenOvertime(gateParamData.getOpenOvertime());// 开门超时 1字节
		data.setMultiOpenNumber(gateParamData.getMultiOpenNumber());// 多卡开门数量 1字节
		long[] multiOpenCard1=new long[6];
		if(null!=gateParamData.getMultiOpenCard()){
			for(int i=0;i<gateParamData.getMultiOpenCard().length;i++){
				multiOpenCard1[i]=gateParamData.getMultiOpenCard()[i];
			}
		}
		
		data.setMultiOpenCard(multiOpenCard1); // 多卡开门卡号 24字节 多卡开门仅支持4字节卡号
		// 校验值要求在配置有效数据后配置
		SendData sendData = new SendData();
		sendData.setGateParamData(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			result=com.kuangchi.sdd.comm.container.Service.service("set_workparam", pkg,deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result.equals("null")||"".equals(result)){
			return 1;
		}else{
			return 0;
		}
	}

}
