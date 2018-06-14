package com.kuangchi.sdd.zigbee.handler;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.network.HttpRequest;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x01;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x02;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x03;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x04;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x05;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x06;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x07;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x08;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x09;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0A;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0B;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0C;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0D;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0E;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0F;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x04;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x06;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x0C;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x0D;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x0E;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x0F;

import io.netty.channel.ChannelHandlerContext;

/**
 * 逻辑处理并发送协议到网关
 * */
public class SendService {
	
	public static final Logger LOG = Logger.getLogger(SendService.class);
      public static void sendData0xEE0x01(Data0xAA0x01 data0xAA0x01,ChannelHandlerContext ctx){
    	    
    	  
    	  
    	  
       
    	  
    	  
      }
      
      public static void sendData0xEE0x02(Data0xAA0x02 data0xAA0x02,ChannelHandlerContext ctx){
    	

      }
      
      
      public static  void sendData0xEE0x03(Data0xAA0x03 data0xAA0x03,ChannelHandlerContext ctx){
    	 

      }
      
      
      public static  void sendData0xEE0x04(Data0xAA0x04 data0xAA0x04,ChannelHandlerContext ctx){
    	
    	  String url = PropertiesToMap.propertyToMap("photoncard_interface.properties")
    				.get("url")+ "interface/ZigBeeInterAction/recordReport.do?"; 
  	  	  long photonId = data0xAA0x04.getPhotonId();
  	  	  int openType = data0xAA0x04.getOpenType();
  	  	  String lockId = data0xAA0x04.getLockId();
  	  	  String data = HttpRequest.sendPost(url, "photonId="+ photonId+"&openType="+openType+"&lockId="+lockId);
  	  	  LOG.info("=========返回结果为"+data);
      	  
  	  	  Map<String, Object> map = GsonUtil.toBean(data, HashMap.class);
    	  //==============回复开锁记录上报===============
		  try {
			  Data0xEE0x04 data0xEE0x04=new Data0xEE0x04();
	    	  data0xEE0x04.setHeader(0xee);
	    	  data0xEE0x04.setCmd(0x04);
	    	  data0xEE0x04.setLength(9);
	    	  data0xEE0x04.setLockId((String)map.get("lockId"));
		   	  data0xEE0x04.setStatus(Integer.parseInt((String)map.get("status")));
		   	  String gateway_ip = (String)map.get("gateway_ip");
		   	  int gateway_port = Integer.parseInt((String)map.get("gateway_port"));
              new ClientEE(gateway_ip, gateway_port, data0xEE0x04).run();	   

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
      }
      
      public static  void sendData0xEE0x05(Data0xAA0x05 data0xAA0x05,ChannelHandlerContext ctx){


      }
      
      
      public static  void sendData0xEE0x06(Data0xAA0x06 data0xAA0x06,ChannelHandlerContext ctx){
    		//==============回复电量不足记录上报=============
    	  String url = PropertiesToMap.propertyToMap("photoncard_interface.properties")
  				.get("url")+ "interface/ZigBeeInterAction/updateElectricity.do?"; 
	  	  String bateryBalance = data0xAA0x06.getBateryBalance();
	  	  String roomNo = data0xAA0x06.getRoomNo();
	  	  String lockId = data0xAA0x06.getLockId();
	  	  String data = HttpRequest.sendPost(url, "bateryBalance="+ bateryBalance+"&roomNo="+roomNo+"&lockId="+lockId);
	  	  LOG.info("=========返回结果为"+data);
    	  
	  	  Map<String, Object> map = GsonUtil.toBean(data, HashMap.class);
    	  
		  try {
			  Data0xEE0x06 data0xEE0x06=new Data0xEE0x06();
		   	  data0xEE0x06.setHeader(0xee);
		   	  data0xEE0x06.setCmd(0x06);
		   	  data0xEE0x06.setLength(9);
		   	  data0xEE0x06.setLockId((String)map.get("lockId"));
		   	  data0xEE0x06.setStatus(Integer.parseInt((String)map.get("status")));
		   	  String gateway_ip = (String)map.get("gateway_ip");
		   	  int gateway_port = Integer.parseInt((String)map.get("gateway_port"));
              new ClientEE(gateway_ip, gateway_port, data0xEE0x06).run();	   

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
		  
		  
      }
      
      
      public static  void sendData0xEE0x07(Data0xAA0x07 data0xAA0x07,ChannelHandlerContext ctx){
    	  

      }
      
      
      
      public static  void sendData0xEE0x08(Data0xAA0x08 data0xAA0x08,ChannelHandlerContext ctx){
       


      }
      
      public static  void sendData0xEE0x09(Data0xAA0x09 data0xAA0x09,ChannelHandlerContext ctx){
       


      }
      
      
      public static  void sendData0xEE0x0A(Data0xAA0x0A data0xAA0x0A,ChannelHandlerContext ctx){


      
      }
      
      public static  void sendData0xEE0x0B(Data0xAA0x0B data0xAA0x0B,ChannelHandlerContext ctx){


          
      }
      
      public static  void sendData0xEE0x0C(Data0xAA0x0C data0xAA0x0C,ChannelHandlerContext ctx){
      	
    	  String url = PropertiesToMap.propertyToMap("photoncard_interface.properties")
  				.get("url")+ "interface/ZigBeeInterAction/illegalRecordReport.do?"; 
	  	  String lockId = data0xAA0x0C.getLockId();
	  	  int openType = 3;
	  	  String data = HttpRequest.sendPost(url, "openType="+openType+"&lockId="+lockId);
	  	  LOG.info("=========返回结果为"+data);
    	  
    	  
  	  	  Map<String, Object> map = GsonUtil.toBean(data, HashMap.class);
    	  //==============回复开锁记录上报===============
		  try {
			  Data0xEE0x0C data0xEE0x0C=new Data0xEE0x0C();
	    	  data0xEE0x0C.setHeader(0xee);
	    	  data0xEE0x0C.setCmd(0x0C);
	    	  data0xEE0x0C.setLength(9);
	    	  data0xEE0x0C.setLockId((String)map.get("lockId"));
		   	  data0xEE0x0C.setStatus(Integer.parseInt((String)map.get("status")));
		   	  String gateway_ip = (String)map.get("gateway_ip");
		   	  int gateway_port = Integer.parseInt((String)map.get("gateway_port"));
              new ClientEE(gateway_ip, gateway_port, data0xEE0x0C).run();	   

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
      }
    
      public static  void sendData0xEE0x0D(Data0xAA0x0D data0xAA0x0D,ChannelHandlerContext ctx){
    	  
    	  String url = PropertiesToMap.propertyToMap("photoncard_interface.properties")
    				.get("url")+ "interface/ZigBeeInterAction/illegalRecordReport.do?"; 
  	  	  String lockId = data0xAA0x0D.getLockId();
  	  	  int openType = 4;
  	  	  String data = HttpRequest.sendPost(url, "openType="+openType+"&lockId="+lockId);
  	  	  LOG.info("=========返回结果为"+data);
  	  	  
    	  Map<String, Object> map = GsonUtil.toBean(data, HashMap.class);
    	  //==============回复开锁记录上报===============
    	  try {
    		  Data0xEE0x0D data0xEE0x0D=new Data0xEE0x0D();
    		  data0xEE0x0D.setHeader(0xee);
    		  data0xEE0x0D.setCmd(0x0D);
    		  data0xEE0x0D.setLength(9);
    		  data0xEE0x0D.setLockId((String)map.get("lockId"));
    		  data0xEE0x0D.setStatus(Integer.parseInt((String)map.get("status")));
    		  String gateway_ip = (String)map.get("gateway_ip");
    		  int gateway_port = Integer.parseInt((String)map.get("gateway_port"));
    		  new ClientEE(gateway_ip, gateway_port, data0xEE0x0D).run();	   
    		  
    	  } catch (Exception e) {
    		  // TODO Auto-generated catch block
    		  e.printStackTrace();
    	  } 
    	  
      }
      
      public static  void sendData0xEE0x0E(Data0xAA0x0E data0xAA0x0E,ChannelHandlerContext ctx){
    	  
    	  String url = PropertiesToMap.propertyToMap("photoncard_interface.properties")
  				.get("url")+ "interface/ZigBeeInterAction/pasVaild.do?"; 
	  	  String lockId = data0xAA0x0E.getLockId();
	  	  String password = data0xAA0x0E.getPassword();
	  	  String data = HttpRequest.sendPost(url, "password="+password+"&lockId="+lockId);
	  	  LOG.info("=========返回结果为"+data);
    	  Map<String, Object> map = GsonUtil.toBean(data, HashMap.class);
    	  
    	 //==============回复密码上报===============
    	  try {
    		  Data0xEE0x0E data0xEE0x0E=new Data0xEE0x0E();
    		  data0xEE0x0E.setHeader(0xee);
    		  data0xEE0x0E.setCmd(0x0E);
    		  data0xEE0x0E.setLength(9);
    		  data0xEE0x0E.setLockId((String)map.get("lockId"));
    		  data0xEE0x0E.setStatus(Integer.parseInt((String)map.get("status")));
    		  String gateway_ip = (String)map.get("gateway_ip");
    		  int gateway_port = Integer.parseInt((String)map.get("gateway_port"));
    		  new ClientEE(gateway_ip, gateway_port, data0xEE0x0E).run();	   
    		  
    	  } catch (Exception e) {
    		  // TODO Auto-generated catch block
    		  e.printStackTrace();
    	  } 
    	  
      }
      
      
      // 假锁
      public static void sendData0xEE0x0F(Data0xAA0x0F data0xAA0x0F,ChannelHandlerContext ctx){
    	  
    	  String url = PropertiesToMap.propertyToMap("photoncard_interface.properties")
    				.get("url")+ "interface/ZigBeeInterAction/illegalRecordReport.do?"; 
  	  	  String lockId = data0xAA0x0F.getLockId();
  	  	  int openType = 6;
  	  	  String data = HttpRequest.sendPost(url, "openType="+openType+"&lockId="+lockId);
  	  	  LOG.info("=========返回结果为"+data);
  	  	  
    	  Map<String, Object> map = GsonUtil.toBean(data, HashMap.class);
    	  //==============回复开锁记录上报===============
    	  try {
    		  Data0xEE0x0F data0xEE0x0F=new Data0xEE0x0F();
    		  data0xEE0x0F.setHeader(0xee);
    		  data0xEE0x0F.setCmd(0x0F);
    		  data0xEE0x0F.setLength(9);
    		  data0xEE0x0F.setLockId((String)map.get("lockId"));
    		  data0xEE0x0F.setStatus(Integer.parseInt((String)map.get("status")));
    		  String gateway_ip = (String)map.get("gateway_ip");
    		  int gateway_port = Integer.parseInt((String)map.get("gateway_port"));
    		  new ClientEE(gateway_ip, gateway_port, data0xEE0x0F).run();	   
    		  
    	  } catch (Exception e) {
    		  // TODO Auto-generated catch block
    		  e.printStackTrace();
    	  } 
    	  
      }
}
