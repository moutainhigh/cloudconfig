package com.kuangchi.sdd.elevator.dllInterfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.kuangchi.sdd.elevator.model.Auth;
import com.kuangchi.sdd.elevator.model.Authority;
import com.kuangchi.sdd.elevator.model.BlackListBean;
import com.kuangchi.sdd.elevator.model.CommParam;
import com.kuangchi.sdd.elevator.model.DelAuth;
import com.kuangchi.sdd.elevator.model.Device;
import com.kuangchi.sdd.elevator.model.FloorConfigBean;
import com.kuangchi.sdd.elevator.model.FloorOpenTimeZone;
import com.kuangchi.sdd.elevator.model.HardWareParam;
import com.kuangchi.sdd.elevator.model.OpenTimeZone;
import com.kuangchi.sdd.elevator.model.Result;
import com.kuangchi.sdd.elevator.model.TimeZone;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.sun.java_cup.internal.runtime.Scanner;
import com.sun.jna.Native;
import com.sun.org.apache.regexp.internal.RE;

public class Main {
    public static void main(String[] args) {
    	//函数功能: 获取通信线程计数;
//        System.out.println(TKInterfaceFunctions.KKTK_GetCommThreadCount()); 
        
        
	    //函数功能: 停止所有通信线程;
        // TKInterfaceFunctions.KKTK_StopAllComm();
         
        //获取Dll内部版本
//	    System.out.println(TKInterfaceFunctions.KKTK_Get_DLLVersion());
	    
//	    System.out.println(device.getAddress()+" "+device.getMac()+"  "+device.getSerialNo()+"  "+device.getIp()+" "+device.getPort()+" "+device.getSubnet()+" "+device.getGateway());
//	    1 12.02.21.00.08.A2  023300002210  192.168.3.172 6000 0.0.0.0 192.168.3.251
	    //测试485
    	//for(int i=1;i<=254;i++){
    	    Device d=new Device();
    	    d.setAddress("1");
    	    //d.setMac("12.02.21.00.08.A2");
    	    //d.setSerialNo("023300002210");
    	    d.setIp("10.4.214.233");
    	    d.setPort("6000");
    	    //d.setSubnet("0.0.0.0");
    	    //d.setGateway("10.4.214.254");
    	    //d.setComm("COM1");
    	    d.setCommType(0);
    	    System.out.println(TKInterfaceFunctions.KKTK_RecvClock(d));
    	//}
	    
	    //搜索设备
	    List<Device> deviceList=  TKInterfaceFunctions.KKTK_SearchControls("10.4.214.233");
	    for (int i = 0; i < deviceList.size(); i++) {
//	    	Device device=deviceList.get(i);
//	    	System.out.println(device.getAddress()+" "+device.getMac()+"  "+device.getSerialNo()+"  "+device.getIp()+" "+device.getPort()+" "+device.getSubnet()+" "+device.getGateway());
	    	//1 12.02.21.00.08.A2  023300002210  192.168.3.172 6000 0.0.0.0 192.168.3.251

		//	 修改控制器通信参数 如IP
//			 device.setIp("192.168.10.110");
//			 device.setGateway("192.168.10.254");
//			 device.setSubnet("255.255.255.0");
//		   	 TKInterfaceFunctions.KKTK_UpdateControl(device,"1");
		   	 //接受时钟
//			System.out.println(TKInterfaceFunctions.KKTK_RecvClock(device));
			 //发送时钟
			// System.out.println(TKInterfaceFunctions.KKTK_SendClock(device));
			
			 //修改控制器密码
			// device.setPassword("000000");
           //  System.out.println(TKInterfaceFunctions.KKTK_UpdatePassword(device,"000000"));
             //初始化控制器
			// System.out.println(TKInterfaceFunctions.KKTK_SystemInit(device));
//			 
//			 //发送电梯开放时区
//			 OpenTimeZone openTimeZone=new OpenTimeZone();
//			 openTimeZone.setWeekDay(1);
//			 TimeZone timeZone0= new TimeZone();
//			 timeZone0.setStartHour("14");
//			 timeZone0.setStartMinute("05");
//			 timeZone0.setEndHour("15");
//			 timeZone0.setEndMinute("00");
//			 openTimeZone.getTimeZoneList().add(timeZone0);
//			 System.out.println(TKInterfaceFunctions.KKTK_SendElevatorOpenTimezone(device, openTimeZone));
//			 
//			 
//			 //发送硬件参数
//			 HardWareParam hardWareParam=new HardWareParam();
//			 hardWareParam.setFloorRelayTime(5);
//			 hardWareParam.setDirectRelayTime(6);
//			 hardWareParam.setOpositeRelayTime(48);
//			 hardWareParam.setTotalFloor(12);
//			 hardWareParam.setUp(0);
//			 hardWareParam.setDown(1);
//			 hardWareParam.setOpen(0);
//			 hardWareParam.setClose(1);
//			 System.out.println(TKInterfaceFunctions.KKTK_SendHardwareParam(device, hardWareParam)); 
//			 
//		//发送楼层开放时区	 
//			 FloorOpenTimeZone floorOpenTimeZone=new FloorOpenTimeZone();
//			 floorOpenTimeZone.setFloor(2);
//			 TimeZone timeZone1=new TimeZone();
//			 timeZone1.setStartHour("14");
//			 timeZone1.setStartMinute("02");
//			 timeZone1.setEndHour("15");
//			 timeZone1.setEndMinute("00");
//			 floorOpenTimeZone.getTimeZoneList().add(timeZone1);
//		 System.out.println(TKInterfaceFunctions.KKTK_SendFloorOpenTimezone(device, floorOpenTimeZone)); 
//
//		 
//		 //发送黑名单   ----------未对通
//			 BlackListBean blackListBean=new BlackListBean();
//			 blackListBean.setType(1);
//			 blackListBean.setMode(2);
//			 blackListBean.getList().add("12345678");
//			 blackListBean.getList().add("12345785");
//			 System.out.println(TKInterfaceFunctions.KKTK_SendBlacklist(device, blackListBean));
//			 
//			 
//			 //获取硬件参数
//			 HardWareParam hardWareParam1=TKInterfaceFunctions.KKTK_RecHardWareParam(device);
//			 
//			 
//			 
//			 //接收设备版本
//			 System.out.println(TKInterfaceFunctions.KKTK_RecvVer(device)); 
//			 
//			 //接收设备工作状态
//			 System.out.println(TKInterfaceFunctions.KKTK_RecvWorkStatus(device));
//			 //接收记录数
//			 System.out.println(TKInterfaceFunctions.KKTK_RecvRecordCount(device));
//			
//			 //发送通信参数  ----弃用
////			 CommParam commParam=new CommParam();
////			 commParam.setAddress("1");
////			 commParam.setIp("192.168.3.174");
////			 commParam.setGateway(device.getGateway());
////			 commParam.setPort(device.getPort());
////			 commParam.setSerialNo(device.getSerialNo());
////			 commParam.setSubnet(device.getSubnet());
////             System.out.println(TKInterfaceFunctions.KKTK_SendControlCommParam(device, commParam));
//             
//             //接收通信参数
//            System.out.println(TKInterfaceFunctions.KKTK_RecvControlCommParam(device));
//            
//            
//			 //发送配置表
//			  FloorConfigBean configBean=new FloorConfigBean();
//			  configBean.setType(0);
//			  for (int j = 0; j < 48; j++) {
//				  configBean.getFloorList().add(j+1);
//			  }
//              //System.out.println(TKInterfaceFunctions.KKTK_SendCongfigTable(device, configBean));
//              
//              
//              //接收配置表
//            FloorConfigBean floorConfigBean=TKInterfaceFunctions.KKTK_RecvConfigTable(device, 0);
//
//            //发送节假日
//            Calendar calendar=Calendar.getInstance();
//            try {
//				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01"));
//				System.out.println(calendar.get(Calendar.DAY_OF_YEAR));
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            List<String> holidayDateList=new ArrayList<String>();
//            holidayDateList.add("2016-01-25");
//            holidayDateList.add("2016-01-2");
//            holidayDateList.add("2016-02-29");
//            holidayDateList.add("2016-12-31");
//           // System.out.println(TKInterfaceFunctions.KKTK_SendHolidayTable(device, holidayDateList));
//            
//            
//            
//        //发送授权    
//            Authority authority=new Authority();
//            Auth auth=new Auth();
//            auth.setCardNum("123456");
//            auth.setCardType(1);
//            auth.setStartDate("151010");
//            auth.setEndDate("151030");
//            auth.setPassword("000000");
//            auth.getFloorList().add(1);
//            auth.getFloorList().add(10);
//            auth.getFloorList().add(17);
//            auth.getFloorList().add(18);
//            auth.getFloorList().add(25);
//            auth.getFloorList().add(33);
//            auth.getFloorList().add(34);
//            auth.getFloorList().add(42);
//            auth.getFloorList().add(43);     
//            Auth auth2=new Auth();
//            auth2.setCardNum("123457");
//            auth2.setCardType(1);
//            auth2.setStartDate("151010");
//            auth2.setEndDate("151030");
//            auth2.setPassword("000000");
//            auth2.getFloorList().add(1);
//            authority.getAuthList().add(auth);
//            authority.getAuthList().add(auth2);
//            System.out.println(TKInterfaceFunctions.KKTK_SendAddID(device, authority));
//
//
//            //删除授权
//            DelAuth delAuth=new DelAuth();
//            delAuth.setType(1);
//            delAuth.getCardNumList().add("123456");
//            delAuth.getCardNumList().add("123456");
//            System.out.println(TKInterfaceFunctions.KKTK_SendDelID(device, delAuth));
//            
//            //接收记录
//            System.out.println(TKInterfaceFunctions.KKTK_RecvEvent(device));
		}
//	    
//	    Calendar calendar=Calendar.getInstance();
//	    calendar.setTime(new Date());
//	    System.out.println(calendar.get(Calendar.DAY_OF_YEAR));
	    
 	}
}
