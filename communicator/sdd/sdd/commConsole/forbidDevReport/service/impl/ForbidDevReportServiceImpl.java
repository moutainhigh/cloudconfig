package com.kuangchi.sdd.commConsole.forbidDevReport.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeForbid;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupForbid;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.forbidDevReport.service.ForbidDevReportService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Service("forbidDevReportServiceImpl")
public class ForbidDevReportServiceImpl implements ForbidDevReportService{
	public static final Logger LOG = Logger.getLogger(ForbidDevReportServiceImpl.class);
	@Override
	public String setForbidDevReport(String mac,String device_type,String isStatu,String datas) {
		Map<String, String> datasMap=GsonUtil.toBean(datas, HashMap.class);
		Integer begin_time_hour = Integer.parseInt(datasMap.get("begin_time_hour"));
		Integer begin_time_minute = Integer.parseInt(datasMap.get("begin_time_minute"));
		Integer end_time_hour =  Integer.parseInt(datasMap.get("end_time_hour"));
		Integer end_time_minute = Integer.parseInt(datasMap.get("end_time_minute"));
		TimeForbid timeForbid = new TimeForbid();
		timeForbid.setStartHour(Util.getIntHex(""+begin_time_hour));
		timeForbid.setStartMinute(Util.getIntHex(""+begin_time_minute));
		timeForbid.setEndHour(Util.getIntHex(""+end_time_hour));
		timeForbid.setEndMinute(Util.getIntHex(""+end_time_minute));
		
		Integer begin_time_hour1 = Integer.parseInt(datasMap.get("begin_time_hour1"));
		Integer begin_time_minute1 = Integer.parseInt(datasMap.get("begin_time_minute1"));
		Integer end_time_hour1 =  Integer.parseInt(datasMap.get("end_time_hour1"));
		Integer end_time_minute1 = Integer.parseInt(datasMap.get("end_time_minute1"));
		TimeForbid timeForbid1 = new TimeForbid();
		timeForbid1.setStartHour(Util.getIntHex(""+begin_time_hour1));
		timeForbid1.setStartMinute(Util.getIntHex(""+begin_time_minute1));
		timeForbid1.setEndHour(Util.getIntHex(""+end_time_hour1));
		timeForbid1.setEndMinute(Util.getIntHex(""+end_time_minute1));
		    
		
		Integer begin_time_hour2 = Integer.parseInt(datasMap.get("begin_time_hour2"));
		Integer begin_time_minute2 = Integer.parseInt(datasMap.get("begin_time_minute2"));
		Integer end_time_hour2 =  Integer.parseInt(datasMap.get("end_time_hour2"));
		Integer end_time_minute2 = Integer.parseInt(datasMap.get("end_time_minute2"));
		TimeForbid timeForbid2 = new TimeForbid();
		timeForbid2.setStartHour(Util.getIntHex(""+begin_time_hour2));
		timeForbid2.setStartMinute(Util.getIntHex(""+begin_time_minute2));
		timeForbid2.setEndHour(Util.getIntHex(""+end_time_hour2));
		timeForbid2.setEndMinute(Util.getIntHex(""+end_time_minute2));
		
		Integer begin_time_hour3 = Integer.parseInt(datasMap.get("begin_time_hour3"));
		Integer begin_time_minute3 = Integer.parseInt(datasMap.get("begin_time_minute3"));
		Integer end_time_hour3 =  Integer.parseInt(datasMap.get("end_time_hour3"));
		Integer end_time_minute3 = Integer.parseInt(datasMap.get("end_time_minute3"));
		TimeForbid timeForbid3 = new TimeForbid();
		timeForbid3.setStartHour(Util.getIntHex(""+begin_time_hour3));
		timeForbid3.setStartMinute(Util.getIntHex(""+begin_time_minute3));
		timeForbid3.setEndHour(Util.getIntHex(""+end_time_hour3));
		timeForbid3.setEndMinute(Util.getIntHex(""+end_time_minute3));
		
		
		TimeForbid[] timeForbids=new TimeForbid[4];
		timeForbids[0] = timeForbid;
		timeForbids[1] = timeForbid1;
		timeForbids[2] = timeForbid2;
		timeForbids[3] = timeForbid3;
		
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(device_type));///设备组
		
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x43);
		pkg.setLength(0x0011);
		pkg.setOrderStatus(0x00);
		TimeGroupForbid data = new TimeGroupForbid();
		//ox01  
		data.setEnable(Util.getIntHex(isStatu));
		
		
         data.setTimeForbids(timeForbids);
		
		SendData sendData = new SendData();
		sendData.setTimeGroupForbid(data);

		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		//connector.setHeader(pkg);
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager(mac);
    	String result = null;
		try {
			result = com.kuangchi.sdd.comm.container.Service.service("set_state_forbid_time", pkg,deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//String result = connector.run();
		return result;
	}

	@Override
	public String getForbidDevReport(String mac, String device_type) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(device_type));
		
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x44);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		

		
		SendData sendData = new SendData();
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		//connector.setHeader(pkg);
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager(mac);
		String result=null;
		try {
			result = com.kuangchi.sdd.comm.container.Service.service("get_state_forbid_time", pkg,deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//String result = connector.run();
		LOG.info("容器获取的值:" + result);
		return result;
	}


}
