package com.kuangchi.sdd.commConsole.holiday.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupData;
import com.kuangchi.sdd.comm.equipment.gate.group.GetGroupConnector;
import com.kuangchi.sdd.comm.equipment.gate.holiday.SetHolidayConnector;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.group.model.TimeGroupBlock;
import com.kuangchi.sdd.commConsole.holiday.action.HolidayAction;
import com.kuangchi.sdd.commConsole.holiday.model.HolidayBean;
import com.kuangchi.sdd.commConsole.holiday.model.ResultJson;
import com.kuangchi.sdd.commConsole.holiday.model.TimeJson;
import com.kuangchi.sdd.commConsole.holiday.model.holidayData;
import com.kuangchi.sdd.commConsole.holiday.model.holidayTime;


import com.kuangchi.sdd.commConsole.holiday.service.IHolidayService;
import com.kuangchi.sdd.commConsole.search.model.EquipmentBean;
@Transactional
@Service("holidayServiceImpl")
public class HolidayServiceImpl implements IHolidayService{
	public static final Logger LOG = Logger.getLogger(HolidayServiceImpl.class);
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	
	
    @Override
    public String getHoliday(String mac,String deviceType) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Integer.parseInt(deviceType));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x27);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		pkg.setCrc(pkg.getCrcFromSum());
	
	
		GetDevInfoService devInfoService=new GetDevInfoService();
		DeviceInfo2 deviceInfo2=devInfoService.getManager(mac);
		String result = "";
		try {
			result = com.kuangchi.sdd.comm.container.Service.service("get_holiday", pkg,deviceInfo2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	
    }
    
    //设置节假日时段组
    @Override
    public String setHoliday(String Mac,String device_type,List<holidayData> holiday) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(device_type));//pkg.setSign(0x04);
		pkg.setMac(Util.getIntHex(Mac));//将十进制转化为十六进制
		LOG.info("MAC:"+pkg.getMac());
		pkg.setOrder(0x28);
		pkg.setLength(0x0200);
		pkg.setOrderStatus(0x00);
		TimeBlock block = new TimeBlock();
		List<TimeGroupData> groups = new ArrayList<TimeGroupData>(128);
		LOG.info("参数集合的长度="+groups.size());
		TimeGroupData group;
		TimeData time;
		LOG.info("集合的长度："+holiday.size());
		for(int i=0;i<128;i++){
			if(i<holiday.size()){
				group= new TimeGroupData();
				time = new TimeData();
				String year=(holiday.get(i).getYear().substring(2,4));
				LOG.info("年"+year);
				String month=holiday.get(i).getMonth();
				LOG.info("月"+month);
				String day=holiday.get(i).getDay();
				LOG.info("日"+day);
				String dayOfWeek=holiday.get(i).getDayOfWeek();
				LOG.info("星期"+dayOfWeek);
				time.setYear(Util.getIntHex(year));
				time.setMonth(Util.getIntHex(month));
				time.setDay(Util.getIntHex(day));
				time.setDayOfWeek(Util.getIntHex(dayOfWeek));
				group.setStart(time);
			    groups.add(group);
			  }else{
				  group= new TimeGroupData();
					time = new TimeData();
					LOG.info("补足"+Util.getIntHex("00"));
					time.setYear(Util.getIntHex("00"));
					time.setMonth(Util.getIntHex("00"));
					time.setDay(Util.getIntHex("00"));
					time.setDayOfWeek(Util.getIntHex("00"));
					group.setStart(time);
				    groups.add(group);
			  }   
		}
		block.setGroups(groups);
		SendData sendData = new SendData();
		sendData.setTimeBlock(block);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		LOG.info("输出正确holiday");
		String result = null;
		try {
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(Mac);
			result = com.kuangchi.sdd.comm.container.Service.service(
					"set_holiday", pkg,deviceInfo2);
			LOG.info("容器获取的值:" + result);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		if(result==null||"null".equals(result)){
			return null;
		}else{
			return result;
		}
		
    }


}
