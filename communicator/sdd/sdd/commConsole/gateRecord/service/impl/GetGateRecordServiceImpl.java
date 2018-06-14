package com.kuangchi.sdd.commConsole.gateRecord.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.gateRecord.service.IGetGateRecord;

@Service("getGateRecordServiceImpl")
public class GetGateRecordServiceImpl implements IGetGateRecord {
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	@Override
	public List<HashMap> getGateRecord(String mac,String deviceType) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Integer.parseInt(deviceType));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x1C);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		pkg.setCrc(pkg.getCrcFromSum());
		List<HashMap> getGateRecordList=new ArrayList<HashMap>();
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			String result = com.kuangchi.sdd.comm.container.Service.service("get_record", pkg,deviceInfo2);
			List<String> allStrings=new ArrayList<String>();
			StringTokenizer st=new StringTokenizer(result,"|");
			while(st.hasMoreTokens()){
				allStrings.add(st.nextToken());
			}
			Gson gson=new Gson();
			java.lang.reflect.Type type = new TypeToken<List<HashMap>>() {}.getType(); 
			getGateRecordList=gson.fromJson(allStrings.toString(), type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getGateRecordList;
	}

}
