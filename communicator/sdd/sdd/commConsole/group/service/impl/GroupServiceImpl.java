package com.kuangchi.sdd.commConsole.group.service.impl;

import java.text.SimpleDateFormat;
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
import com.kuangchi.sdd.comm.equipment.gate.group.SetGroupConnector;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.group.model.TimeGroupBlock;
import com.kuangchi.sdd.commConsole.group.service.IGroupService;
import com.kuangchi.sdd.commConsole.holiday.service.IHolidayService;
import com.kuangchi.sdd.commConsole.search.model.EquipmentBean;
@Transactional
@Service("groupServiceImpl")
public class GroupServiceImpl implements IGroupService{
	public static final Logger LOG = Logger.getLogger(GroupServiceImpl.class);
	
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	//设置时段组上报
	@Override
	public String setGroup(String mac, String device_type,List<TimeBlock> groupTime) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(device_type));//pkg.setSign(0x04);
		pkg.setMac(Util.getIntHex(mac));
		LOG.info("这是转为16进制:"+pkg.getMac());
		pkg.setOrder(0x22);
		pkg.setLength(0x0201);
		pkg.setOrderStatus(0x00);
		String result1 = null;
		String result2 = null;
		for (int i = 0; i < groupTime.size(); i++) {
			LOG.info("块号"+Integer.valueOf(groupTime.get(i).getBlock()));
			if(String.valueOf(groupTime.get(i).getBlock()).equals("0")){
				TimeBlock data = new TimeBlock();
				data.setBlock(0x00);
				List<TimeGroupData> groups = new ArrayList<TimeGroupData>();
				TimeGroupData group;
				TimeData start;
				TimeData end;
				List<TimeGroupData> groupTimeNew1=groupTime.get(i).getGroups();//一个新的集合
				for (int j = 0; j < 128; j++) {
					if(j<groupTimeNew1.size()){
						int start_hour=groupTimeNew1.get(j).getStart().getHour();//开始小时
						LOG.info("开始小时"+start_hour);
						int start_minute=groupTimeNew1.get(j).getStart().getMinute();//开始分钟
						LOG.info("开始分钟"+start_minute);
						int end_hour=groupTimeNew1.get(j).getEnd().getHour();//结束小时
						LOG.info("结束小时"+end_hour);
						int end_minute=groupTimeNew1.get(j).getEnd().getMinute();//结束分钟
						LOG.info("结束分钟"+end_minute);
						LOG.info("集合00块长度"+groupTimeNew1.size());
						group = new TimeGroupData();
						start = new TimeData();
						start.setHour(Util.getIntHex(String.valueOf(start_hour)));
						start.setMinute(Util.getIntHex(String.valueOf(start_minute)));
						end = new TimeData();
						end.setHour(Util.getIntHex(String.valueOf(end_hour)));
						end.setMinute(Util.getIntHex(String.valueOf(end_minute)));
						group.setStart(start);
						group.setEnd(end);
						groups.add(group);
					}else{
						LOG.info("补足块0:"+Util.getIntHex("FF"));
						group = new TimeGroupData();
						start = new TimeData();
						start.setHour(Util.getIntHex("FF"));
						start.setMinute(Util.getIntHex("FF"));
						end = new TimeData();
						end.setHour(Util.getIntHex("FF"));
						end.setMinute(Util.getIntHex("FF"));
						group.setStart(start);
						group.setEnd(end);
						groups.add(group);
					}
				}
				data.setGroups(groups);
				SendData sendData = new SendData();
				sendData.setTimeBlock(data);
				pkg.setData(sendData);
				pkg.setCrc(pkg.getCrcFromSum());
				LOG.info("输出成功1");
				try {
					DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);

					result1 = com.kuangchi.sdd.comm.container.Service.service(
							"set_group", pkg,deviceInfo2);
					LOG.info("容器获取的值:" + result1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(String.valueOf(groupTime.get(i).getBlock()).equals("1")){
				TimeBlock data = new TimeBlock();
				data.setBlock(0x01);
				List<TimeGroupData> groups = new ArrayList<TimeGroupData>();
				TimeGroupData group;
				TimeData start;
				TimeData end;
				List<TimeGroupData> groupTimeNew2=groupTime.get(i).getGroups();//一个新的集合
				for (int j = 0; j < 128; j++) {
					if(j<groupTimeNew2.size()){
						int start_hour=groupTimeNew2.get(j).getStart().getHour();//开始小时
						LOG.info("开始小时1"+start_hour);
						int start_minute=groupTimeNew2.get(j).getStart().getMinute();//开始分钟
						LOG.info("开始分钟1"+start_minute);
						int end_hour=groupTimeNew2.get(j).getEnd().getHour();//结束小时
						LOG.info("结束小时1"+end_hour);
						int end_minute=groupTimeNew2.get(j).getEnd().getMinute();//结束分钟
						LOG.info("结束分钟1"+end_minute);
						LOG.info("集合01块长度:"+groupTimeNew2.size());
						group = new TimeGroupData();
						start = new TimeData();
						start.setHour(Util.getIntHex(String.valueOf(start_hour)));
						start.setMinute(Util.getIntHex(String.valueOf(start_minute)));
						end = new TimeData();
						end.setHour(Util.getIntHex(String.valueOf(end_hour)));
						end.setMinute(Util.getIntHex(String.valueOf(end_minute)));
						group.setStart(start);
						group.setEnd(end);
						groups.add(group);
					}else{
						LOG.info("补足块1:"+Util.getIntHex("FF"));
						group = new TimeGroupData();
						start = new TimeData();
						start.setHour(Util.getIntHex("FF"));
						start.setMinute(Util.getIntHex("FF"));
						end = new TimeData();
						end.setHour(Util.getIntHex("FF"));
						end.setMinute(Util.getIntHex("FF"));
						group.setStart(start);
						group.setEnd(end);
						groups.add(group);
					}
				}
				data.setGroups(groups);
				SendData sendData = new SendData();
				sendData.setTimeBlock(data);
				pkg.setData(sendData);
				pkg.setCrc(pkg.getCrcFromSum());
				LOG.info("输出成功2");
				try {
					DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
					result2 = com.kuangchi.sdd.comm.container.Service.service(
							"set_group", pkg,deviceInfo2);
					LOG.info("容器获取的值:" + result2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(result1==null || result2==null || "null".equals(result1) || "null".equals(result2)){
			return null;
		}else{
			LOG.info("result"+result1+result2);
			return result1+result2;	
		}
	}
	
	//获取时段组上报
	public String getGroup(String mac,String block, String device_type) {
		Connector connector = new GetGroupConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(device_type));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x21);
		pkg.setLength(0x0001);
		pkg.setOrderStatus(0x00);
		String result =null;
		TimeBlock timeBlock = new TimeBlock();
		if(block.equals("00")){
			timeBlock.setBlock(0x00);
		}else{
			timeBlock.setBlock(0x01);
		}
			try {
				SendData sendData = new SendData();
				sendData.setTimeBlock(timeBlock);
				pkg.setData(sendData);
				pkg.setCrc(pkg.getCrcFromSum());
				connector.setHeader(pkg);
				DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
				result = com.kuangchi.sdd.comm.container.Service.service("get_group", pkg,deviceInfo2);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
	}	
}
