package com.kuangchi.sdd.zigbee.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x01;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x02;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x03;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x05;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x07;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x08;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x09;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x0A;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x0B;
import com.kuangchi.sdd.zigbee.service.OperateDeviceService;

@Controller("zigBeeAction")
public class ZigBeeAction   extends BaseActionSupport {
	public static final Logger LOG = Logger.getLogger(ZigBeeAction.class);
	@Resource
	OperateDeviceService operateDeviceService;
	@Override
	public Object getModel() {
		return null;
	}
	
	// 下发上报锁ID与房号
	public void getLock(){
		String remoteIp = getHttpServletRequest().getParameter("remoteIp");
		int port = Integer.parseInt(getHttpServletRequest().getParameter("port"));
		
		try {
	        Data0xEE0x01 data0xEE0x01=new Data0xEE0x01();
	        data0xEE0x01.setHeader(0xEE);
	        data0xEE0x01.setCmd(0x01);
	        data0xEE0x01.setLength(0);
    		String result=operateDeviceService.getData0xAA0x01(data0xEE0x01, remoteIp, port);
    		LOG.info("获得结果："+result);
    		
    		printHttpServletResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//批量下载光子卡的开锁权限
	public void setAuthority(){
		String remoteIp=getHttpServletRequest().getParameter("remoteIp");
		int port= Integer.parseInt(getHttpServletRequest().getParameter("port"));
		String data = getHttpServletRequest().getParameter("data");
		Data0xEE0x02 data0xEE0x02 = GsonUtil.toBean(data, Data0xEE0x02.class);
		try {
//			 Data0xEE0x02 data0xEE0x02=new Data0xEE0x02();
	    	  data0xEE0x02.setHeader(0xee);
	    	  data0xEE0x02.setCmd(0x02);
	    	  data0xEE0x02.setLength(21);
	    	 /* 
	    	  data0xEE0x02.setLockId("44a96502004b1200");
	    	  data0xEE0x02.setPhotonId(0x12345678);
	    	  data0xEE0x02.setStartTime(0x50744A00);
	    	  data0xEE0x02.setEndTime(0x59DB9D00);
	    	
	    	  data0xEE0x02.setTransportFlag(0x00);*/
	    	  String result=operateDeviceService.getData0xAA0x02(data0xEE0x02, remoteIp, port);
	    	  LOG.info("获得结果："+result);

      		printHttpServletResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		
		
	//批量取消光子卡的开锁权限
	public void deleteAuthority(){
		String remoteIp=getHttpServletRequest().getParameter("remoteIp");
		int port= Integer.parseInt(getHttpServletRequest().getParameter("port"));
		String data = getHttpServletRequest().getParameter("data");
		Data0xEE0x03 data0xEE0x03 = GsonUtil.toBean(data, Data0xEE0x03.class);
		
		try {
			//  Data0xEE0x03 data0xEE0x03=new Data0xEE0x03();
	    	  data0xEE0x03.setHeader(0xee);
	    	  data0xEE0x03.setCmd(0x03);
	    	  data0xEE0x03.setLength(13);
	    	 /* data0xEE0x03.setLockId("44a96502004b1200");
	    	  data0xEE0x03.setPhotonId(0x12345678);
	    	  data0xEE0x03.setTransport(0x00);*/
	    	  String result=operateDeviceService.getData0xAA0x03(data0xEE0x03, remoteIp, port);
	    	  LOG.info("获得结果："+result);

      		printHttpServletResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
		
	//查询锁状态信息
	public void getLockState(){
		String remoteIp=getHttpServletRequest().getParameter("remoteIp");
		int port= Integer.parseInt(getHttpServletRequest().getParameter("port"));
		String lock_id = getHttpServletRequest().getParameter("lock_id");
		try {
			  Data0xEE0x05 data0xEE0x05=new Data0xEE0x05();
	   	      data0xEE0x05.setHeader(0xee);
	   	      data0xEE0x05.setCmd(0x05);
	   	      data0xEE0x05.setLength(8);
	   	  //    data0xEE0x05.setLockId("44a96502004b1200");
	   	      data0xEE0x05.setLockId(lock_id);
	   	   String result=operateDeviceService.getData0xAA0x05(data0xEE0x05, remoteIp, port);
	   	LOG.info("获得结果："+result);
	   	   printHttpServletResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	// 更新密钥
	public void setPassword(){
		String remoteIp=getHttpServletRequest().getParameter("remoteIp");
		int port= Integer.parseInt(getHttpServletRequest().getParameter("port"));
		String lockId = getHttpServletRequest().getParameter("lockId");
		String password = getHttpServletRequest().getParameter("password");
		try {
			  Data0xEE0x07 data0xEE0x07=new Data0xEE0x07();
		   	  data0xEE0x07.setHeader(0xee)   ;
		   	  data0xEE0x07.setCmd(0x07);
		   	  data0xEE0x07.setLength(16);
		   	 // data0xEE0x07.setLockId("44a96502004b1200");
		   	 // data0xEE0x07.setPassword("00000000");
		   	  data0xEE0x07.setLockId(lockId);
		   	  data0xEE0x07.setPassword(password);
		   	String result=operateDeviceService.getData0xAA0x07(data0xEE0x07, remoteIp, port);
		   	LOG.info("获得结果："+result);

    		 printHttpServletResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
		
		
	// 更新系统时间
	public void setSystemTime(){
		String remoteIp=getHttpServletRequest().getParameter("remoteIp");
		int port= Integer.parseInt(getHttpServletRequest().getParameter("port"));
		String lockId = getHttpServletRequest().getParameter("lockId");
		Long time = Long.parseLong(getHttpServletRequest().getParameter("time"));
		try {
			  Data0xEE0x08 data0xEE0x08=new Data0xEE0x08();
		   	  data0xEE0x08.setHeader(0xee);
		   	  data0xEE0x08.setCmd(0x08);
		   	  data0xEE0x08.setLength(12);
		   	//  data0xEE0x08.setLockId("44a96502004b1200");
		   	//  data0xEE0x08.setTime(0x50744A00);
		   	  data0xEE0x08.setLockId(lockId);
		   	  data0xEE0x08.setTime(time);
		   	String result=operateDeviceService.getData0xAA0x08(data0xEE0x08, remoteIp, port);
		   	LOG.info("获得结果："+result);

    		printHttpServletResponse(result);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	// 设置Zigbee锁入网Pan-id
	public void setPanID(){
		String remoteIp=getHttpServletRequest().getParameter("remoteIp");
		int port= Integer.parseInt(getHttpServletRequest().getParameter("port"));
		String zigBeeId = getHttpServletRequest().getParameter("zigBeeId");
		String panId = getHttpServletRequest().getParameter("panId");
		try {
			  Data0xEE0x09 data0xEE0x09=new Data0xEE0x09();
		   	  data0xEE0x09.setHeader(0xee);
		   	  data0xEE0x09.setCmd(0x09);
		   	  data0xEE0x09.setLength(10);
		   	 /* data0xEE0x09.setZigBeeId("f84e6607004b1200");
		   	  data0xEE0x09.setPanId("FFF1");*/
		   	  data0xEE0x09.setZigBeeId(zigBeeId);
		   	  data0xEE0x09.setPanId(panId);
		   	String result=operateDeviceService.getData0xAA0x09(data0xEE0x09, remoteIp, port);
		   	LOG.info("获得结果："+result);

    		printHttpServletResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//上报ZigBee网关ID
	public void getPanId(){
		String remoteIp=getHttpServletRequest().getParameter("remoteIp");
		int port= Integer.parseInt(getHttpServletRequest().getParameter("port"));
		try {
			Data0xEE0x0A data0xEE0x0A=new Data0xEE0x0A();
		   	data0xEE0x0A.setHeader(0xee);
		   	data0xEE0x0A.setCmd(0x0a);
		   	data0xEE0x0A.setLength(0);  
		   	String result=operateDeviceService.getData0xAA0x0A(data0xEE0x0A, remoteIp, port);
		   	LOG.info("获得结果："+result);
    		printHttpServletResponse(result);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void setCommunityNo(){
		String remoteIp=getHttpServletRequest().getParameter("remoteIp");
		int port= Integer.parseInt(getHttpServletRequest().getParameter("port"));
		String lockId = getHttpServletRequest().getParameter("lockId");
		int communityNo = Integer.parseInt(getHttpServletRequest().getParameter("communityNo"));
		try {
			  Data0xEE0x0B data0xEE0x0B=new Data0xEE0x0B();
			  data0xEE0x0B.setHeader(0xee);
			  data0xEE0x0B.setCmd(0x0b);
			  data0xEE0x0B.setLength(10);  
			  data0xEE0x0B.setLockId(lockId);
			  data0xEE0x0B.setCommunityNo(communityNo);
		   	String result=operateDeviceService.getData0xAA0x0B(data0xEE0x0B, remoteIp, port);
		   	LOG.info("获得结果："+result);

		   	printHttpServletResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
