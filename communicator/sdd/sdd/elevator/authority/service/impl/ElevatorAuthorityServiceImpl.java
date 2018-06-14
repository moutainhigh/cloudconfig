package com.kuangchi.sdd.elevator.authority.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.elevator.authority.service.ElevatorAuthorityService;
import com.kuangchi.sdd.elevator.dllInterfaces.TKInterfaceFunctions;
import com.kuangchi.sdd.elevator.model.Auth;
import com.kuangchi.sdd.elevator.model.Authority;
import com.kuangchi.sdd.elevator.model.DelAuth;
import com.kuangchi.sdd.elevator.model.Device;
import com.kuangchi.sdd.elevator.model.Result;
@Service("elevatorAuthorityService")
public class ElevatorAuthorityServiceImpl implements ElevatorAuthorityService {

	@Override
	public Result setAuth(Map m) {
			Authority authority=new Authority();
			int commType=0;
			String comm="";
			String address=m.get("address").toString();
			String ip=m.get("device_ip").toString();
			//String port1=m.get("device_port").toString();
			Integer port1=(int) Math.pow((Double) m.get("device_port"), 1);
			String port=port1.toString();
			Device device=new Device();
			device.setCommType(commType);
			device.setComm(comm);
			device.setAddress(address);
			device.setIp(ip);
			device.setPort(port);
			Auth auth=new Auth();
			Integer cardType=Integer.parseInt(m.get("card_type").toString());
			auth.setCardType(cardType);
			auth.setCardNum((String)m.get("card_num"));
			auth.setPassword("000000");
			auth.setStartDate(getDate(m.get("start_time").toString()));
			auth.setEndDate(getDate(m.get("end_time").toString()));
			auth.setFloorList(getFloorList(m.get("floor_list").toString()));
			authority.getAuthList().add(auth);
			Result result=TKInterfaceFunctions.KKTK_SendAddID(device, authority);
			return result;
	}
	
	private static String getDate(String dateStr){
		String[] strArr=dateStr.split("-");
		StringBuilder builder=new StringBuilder();
		builder.append(strArr[0].substring(2, 4));
		builder.append(strArr[1]);
		builder.append(strArr[2]);
		return builder.toString();
	}
	
	private static List<Integer> getFloorList(String listStr){
		String[] arr=listStr.split("\\|");
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<arr.length;i++){
			if(!"".equals(arr[i])){
				list.add(Integer.parseInt(arr[i]));
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		List<String> list=new ArrayList<String>();
		list.add("1");
		list.add("2");
		System.out.println(list.toString());
	}

	@Override
	public Result delAuth(Map m) {
			int commType=0;
			String comm="";
			String address=(String)m.get("address");
			String ip=(String)m.get("device_ip");
			Integer port1=(int) Math.pow((Double) m.get("device_port"), 1);
			String port=port1.toString();
			//String port=(String)m.get("device_port");
			Device device=new Device();
			device.setCommType(commType);
			device.setComm(comm);
			device.setAddress(address);
			device.setIp(ip);
			device.setPort(port);
			DelAuth delAuth=new DelAuth();
			delAuth.setType(1);
			delAuth.getCardNumList().add((String)m.get("card_num"));
			Result result=TKInterfaceFunctions.KKTK_SendDelID(device, delAuth);
			return result;
	}

}
