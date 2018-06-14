package com.kuangchi.sdd.zigbee.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x01;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x02;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x05;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x07;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x08;
import com.kuangchi.sdd.zigbee.service.OperateDeviceService;

@Controller("zigBeeTestAction")
public class TestAction   extends BaseActionSupport {
	public static final Logger LOG = Logger.getLogger(TestAction.class);
	@Resource
	OperateDeviceService operateDeviceService;
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void doTest(){
		String remoteIp="192.168.1.55";
		int port=23;
		
		//==============下发上报锁ID与房号.
		  try {
		        Data0xEE0x01 data0xEE0x01=new Data0xEE0x01();
		        data0xEE0x01.setHeader(0xEE);
		        data0xEE0x01.setCmd(0x01);
		        data0xEE0x01.setLength(0);
        		String result=operateDeviceService.getData0xAA0x01(data0xEE0x01, remoteIp, port);
        		LOG.info("获得结果："+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		//==============批量下载光子卡的开锁权限.
		  try {
			  Data0xEE0x02 data0xEE0x02=new Data0xEE0x02();
	    	  data0xEE0x02.setHeader(0xee);
	    	  data0xEE0x02.setCmd(0x02);
	    	  data0xEE0x02.setLength(21);
	    	  
	    	  data0xEE0x02.setLockId("44a96502004b1200");
	    	  data0xEE0x02.setPhotonId(1002);
	    	
	    	  data0xEE0x02.setStartTime(0x50744A00);
	    	  data0xEE0x02.setEndTime(0x59DB9D00);
	    	  data0xEE0x02.setTransportFlag(0x00);
	    	  String result=operateDeviceService.getData0xAA0x02(data0xEE0x02, remoteIp, port);
	    	  LOG.info("获得结果："+result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		  
//		 
//		  
//		//==============批量取消光子卡的开锁权限.
//		  try {
//			  Data0xEE0x03 data0xEE0x03=new Data0xEE0x03();
//	    	  data0xEE0x03.setHeader(0xee);
//	    	  data0xEE0x03.setCmd(0x03);
//	    	  data0xEE0x03.setLength(13);
//	    	  data0xEE0x03.setLockId("44a96502004b1200");
//	    	  data0xEE0x03.setPhotonId(1002);
//	    	  data0xEE0x03.setTransport(0x00);
//	    	  String result=operateDeviceService.getData0xAA0x03(data0xEE0x03, remoteIp, port);
//      		System.out.println("获得结果："+result);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		  
//		  
//		
		//==============查询锁状态信息
		  try {
			  Data0xEE0x05 data0xEE0x05=new Data0xEE0x05();
	   	      data0xEE0x05.setHeader(0xee);
	   	      data0xEE0x05.setCmd(0x05);
	   	      data0xEE0x05.setLength(8);
	   	      data0xEE0x05.setLockId("44a96502004b1200");
	   	   String result=operateDeviceService.getData0xAA0x05(data0xEE0x05, remoteIp, port);
	   	LOG.info("获得结果："+result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		  
//		  
//		
//		  
//		//==============更新密钥.
		  try {
			  Data0xEE0x07 data0xEE0x07=new Data0xEE0x07();
		   	  data0xEE0x07.setHeader(0xee)   ;
		   	  data0xEE0x07.setCmd(0x07);
		   	  data0xEE0x07.setLength(16);
		   	  data0xEE0x07.setLockId("44a96502004b1200");
		   	  data0xEE0x07.setPassword("00000000");
		   	String result=operateDeviceService.getData0xAA0x07(data0xEE0x07, remoteIp, port);
		   	LOG.info("获得结果："+result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		  
//		  
//		  
//		//==============更新系统时间
		  try {
			  Data0xEE0x08 data0xEE0x08=new Data0xEE0x08();
		   	  data0xEE0x08.setHeader(0xee);
		   	  data0xEE0x08.setCmd(0x08);
		   	  data0xEE0x08.setLength(12);
		   	  data0xEE0x08.setLockId("44a96502004b1200");
		   	  data0xEE0x08.setTime(0x50744A00);
		   	String result=operateDeviceService.getData0xAA0x08(data0xEE0x08, remoteIp, port);
		   	LOG.info("获得结果："+result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
//		  
//		//==============设置Zigbee锁入网Pan-id.
//		  try {
//			  Data0xEE0x09 data0xEE0x09=new Data0xEE0x09();
//		   	  data0xEE0x09.setHeader(0xee);
//		   	  data0xEE0x09.setCmd(0x09);
//		   	  data0xEE0x09.setLength(10);
//		   	  data0xEE0x09.setZigBeeId("f84e6607004b1200");
//		   	  data0xEE0x09.setPanId("FFF1");
//		   	String result=operateDeviceService.getData0xAA0x09(data0xEE0x09, remoteIp, port);
//    		System.out.println("获得结果："+result);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  
//		  
//		//==============上报Zigbee网关ID.
//		  try {
//			  Data0xEE0x0A data0xEE0x0A=new Data0xEE0x0A();
//		   	  data0xEE0x0A.setHeader(0xee);
//		   	  data0xEE0x0A.setCmd(0x0a);
//		   	  data0xEE0x0A.setLength(0);  
//		   	String result=operateDeviceService.getData0xAA0x0A(data0xEE0x0A, remoteIp, port);
//    		System.out.println("获得结果："+result);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  
//		  
  
	}
	
}
