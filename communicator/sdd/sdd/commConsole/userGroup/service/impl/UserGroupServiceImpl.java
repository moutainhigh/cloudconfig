package com.kuangchi.sdd.commConsole.userGroup.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.UserTimeBlock;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.userGroup.service.IUserGroupService;
@Transactional
@Service("userGroupServiceImpl")
public class UserGroupServiceImpl implements IUserGroupService{
	public static final Logger LOG = Logger.getLogger(UserGroupServiceImpl.class);
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	public String setUserGroup(String mac,String deviceType,List<String> userGroup){
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Integer.parseInt(deviceType));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x24);
		pkg.setLength(0x0201);
		pkg.setOrderStatus(0x00);
		UserTimeBlock Onedata = new UserTimeBlock();
		UserTimeBlock Twodata = new UserTimeBlock();
		List<Integer> Onetimes = new ArrayList<Integer>();//存前512组
		List<Integer> Twotimes = new ArrayList<Integer>();//存后512组
		String result =null;
		String result1 = null;
		String result2 = null;
		for (int i = 0; i < userGroup.size(); i++) {
			if(i<userGroup.size()/2){
				Onetimes.add(Integer.parseInt(userGroup.get(i)));
				Onedata.setBlock(0x00);// 传入时间组的块号
			}else{
				Twotimes.add(Integer.parseInt(userGroup.get(i)));
				Twodata.setBlock(0x01);// 传入时间组的块号
			}
		}
			Onedata.setTimes(Onetimes);
			SendData sendData = new SendData();
			sendData.setUserTimeBlock(Onedata);
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			try {
				DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
				result1 =com.kuangchi.sdd.comm.container.Service.service("set_user_group", pkg,deviceInfo2);
				LOG.info("容器获取的值:" + result1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Twodata.setTimes(Twotimes);
			SendData sendData1 = new SendData();
			sendData1.setUserTimeBlock(Twodata);
			pkg.setData(sendData1);
			pkg.setCrc(pkg.getCrcFromSum());
			try {
				DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
				result2 =com.kuangchi.sdd.comm.container.Service.service("set_user_group", pkg,deviceInfo2);
				LOG.info("容器获取的值:" + result2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			result = result1+result2;
			if(result1==null||result2==null||result1.equals("null")||result2.equals("null")){
				return null;
			}
		return result;
	}
	
	//获取用户时段组
	public String getUserGroup(String mac,String deviceType, String block){
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Integer.parseInt(deviceType));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x23);
		pkg.setLength(0x0001);
		pkg.setOrderStatus(0x00);
		// 传入时间组的块号
		UserTimeBlock data = new UserTimeBlock();
		data.setBlock(Util.getIntHex(block));
		SendData sendData = new SendData();
		sendData.setUserTimeBlock(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager(mac);
		String result = null;
		try {
			result = com.kuangchi.sdd.comm.container.Service.service("get_user_group", pkg,deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.info("容器获取的值:" + result);
		return result;
	}
	
	
	
	
}
