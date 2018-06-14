package com.kuangchi.sdd.zigbee.service;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.zigbee.handler.ClientEE;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x01;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x02;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x03;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x05;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x07;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x08;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x09;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x0A;
import com.kuangchi.sdd.zigbee.model.Data0xEE0x0B;
import com.kuangchi.sdd.zigbee.pool.ObjectSephamorePool;
import com.kuangchi.sdd.zigbee.pool.ResultPool;
/**
 * 设备操作服务类，所有对设备下发指定需要走这个类往下调
 * */
@Service("operateDeviceService")
public class OperateDeviceService {
	public static final Logger LOG = Logger.getLogger(OperateDeviceService.class);
	
     public static int timeout=11;
	   public String getData0xAA0x01(Data0xEE0x01 data0xEE0x01,String remoteIp,int port){
		   String headerCmdLockId="ee"+data0xEE0x01.getCmd();   //某一个设备某一个命令的标志位
   		   Semaphore semaphore=ObjectSephamorePool.getSemaphore(headerCmdLockId);	
   		   String result=null;
   		 boolean getLock=false;
		   try {
			   LOG.info("等待semaphore");
			  getLock =semaphore.tryAcquire(timeout,TimeUnit.SECONDS);   //让某一个设备的某一个命令在同一时刻只有一个线程下发,以下代码表示排队执行
			} catch (Exception e) {
				 e.printStackTrace();
			}
		   
			   if (getLock) {
				   try {
					   LOG.info("获得semaphore");
			           new ClientEE(remoteIp, port, data0xEE0x01).run();	
			           result=ResultPool.getResult(headerCmdLockId);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					   semaphore.release();	
				}
			 }
		 return result; 
	   }
	   
	   public String getData0xAA0x02(Data0xEE0x02 data0xEE0x02,String remoteIp,int port){
		   String headerCmdLockId="ee"+data0xEE0x02.getCmd()+data0xEE0x02.getLockId();  //某一个设备某一个命令的标志位
   		   Semaphore semaphore=ObjectSephamorePool.getSemaphore(headerCmdLockId);	
   		   String result=null;
   		 boolean getLock=false;
		   try {
			   LOG.info("等待semaphore");
			  getLock =semaphore.tryAcquire(timeout,TimeUnit.SECONDS);   //让某一个设备的某一个命令在同一时刻只有一个线程下发,以下代码表示排队执行
			} catch (Exception e) {
				 e.printStackTrace();
			}
		   
			   if (getLock) {
				   try {
					   LOG.info("获得semaphore");
			           new ClientEE(remoteIp, port, data0xEE0x02).run();	
			           result=ResultPool.getResult(headerCmdLockId);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					   semaphore.release();	
				}
			 }
		 return result; 
	   }
	   
	   public String getData0xAA0x03(Data0xEE0x03 data0xEE0x03,String remoteIp,int port){
		   String headerCmdLockId="ee"+data0xEE0x03.getCmd()+data0xEE0x03.getLockId();  //某一个设备某一个命令的标志位
   		   Semaphore semaphore=ObjectSephamorePool.getSemaphore(headerCmdLockId);	
   		   String result=null;
   		 boolean getLock=false;
		   try {
			   LOG.info("等待semaphore");
			  getLock =semaphore.tryAcquire(timeout,TimeUnit.SECONDS);   //让某一个设备的某一个命令在同一时刻只有一个线程下发,以下代码表示排队执行
			} catch (Exception e) {
				 e.printStackTrace();
			}
		   
			   if (getLock) {
				   try {
					   LOG.info("获得semaphore");
			           new ClientEE(remoteIp, port, data0xEE0x03).run();	
			           result=ResultPool.getResult(headerCmdLockId);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					   semaphore.release();	
				}
			 }
		 return result; 
	   }

	   public String getData0xAA0x05(Data0xEE0x05 data0xEE0x05,String remoteIp,int port){
		   String headerCmdLockId="ee"+data0xEE0x05.getCmd()+data0xEE0x05.getLockId();  //某一个设备某一个命令的标志位
   		   Semaphore semaphore=ObjectSephamorePool.getSemaphore(headerCmdLockId);	
   		   String result=null;
   		 boolean getLock=false;
		   try {
			   LOG.info("等待semaphore");
			  getLock =semaphore.tryAcquire(timeout,TimeUnit.SECONDS);   //让某一个设备的某一个命令在同一时刻只有一个线程下发,以下代码表示排队执行
			} catch (Exception e) {
				 e.printStackTrace();
			}
		   
			   if (getLock) {
				   try {
					   LOG.info("获得semaphore");
			           new ClientEE(remoteIp, port, data0xEE0x05).run();	
			           result=ResultPool.getResult(headerCmdLockId);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					   semaphore.release();	
				}
			 }
		 return result; 
	   }

	   
	   public String getData0xAA0x07(Data0xEE0x07 data0xEE0x07,String remoteIp,int port){
		   String headerCmdLockId="ee"+data0xEE0x07.getCmd()+data0xEE0x07.getLockId();  //某一个设备某一个命令的标志位
   		   Semaphore semaphore=ObjectSephamorePool.getSemaphore(headerCmdLockId);	
   		   String result=null;
   		 boolean getLock=false;
		   try {
			   LOG.info("等待semaphore");
			  getLock =semaphore.tryAcquire(timeout,TimeUnit.SECONDS);   //让某一个设备的某一个命令在同一时刻只有一个线程下发,以下代码表示排队执行
			} catch (Exception e) {
				 e.printStackTrace();
			}
		   
			   if (getLock) {
				   try {
					   LOG.info("获得semaphore");
			           new ClientEE(remoteIp, port, data0xEE0x07).run();	
			           result=ResultPool.getResult(headerCmdLockId);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					   semaphore.release();	
				}
			 }
		 return result; 
	   }

	   
	   public String getData0xAA0x08(Data0xEE0x08 data0xEE0x08,String remoteIp,int port){
		   String headerCmdLockId="ee"+data0xEE0x08.getCmd()+data0xEE0x08.getLockId();  //某一个设备某一个命令的标志位
   		   Semaphore semaphore=ObjectSephamorePool.getSemaphore(headerCmdLockId);	
   		   String result=null;
   		   boolean getLock=false;
		   try {
			   LOG.info("等待semaphore");
			   getLock =semaphore.tryAcquire(timeout,TimeUnit.SECONDS);   //让某一个设备的某一个命令在同一时刻只有一个线程下发,以下代码表示排队执行
			} catch (Exception e) {
				 e.printStackTrace();
			}
		   
			   if (getLock) {
				   try {
					   LOG.info("获得semaphore");
			           new ClientEE(remoteIp, port, data0xEE0x08).run();	
			           result=ResultPool.getResult(headerCmdLockId);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					   semaphore.release();	
				}
			 }
		 return result; 
	   }

	   
	   public String getData0xAA0x09(Data0xEE0x09 data0xEE0x09,String remoteIp,int port){
 		 String headerCmdLockId="ee"+data0xEE0x09.getCmd()+data0xEE0x09.getZigBeeId(); //某一个设备某一个命令的标志位
   		   Semaphore semaphore=ObjectSephamorePool.getSemaphore(headerCmdLockId);	
   		   String result=null;
   		 boolean getLock=false;
		   try {
			   LOG.info("等待semaphore");
			  getLock =semaphore.tryAcquire(timeout,TimeUnit.SECONDS);   //让某一个设备的某一个命令在同一时刻只有一个线程下发,以下代码表示排队执行
			} catch (Exception e) {
				 e.printStackTrace();
			}
		   
			   if (getLock) {
				   try {
					   LOG.info("获得semaphore");
			           new ClientEE(remoteIp, port, data0xEE0x09).run();	
			           result=ResultPool.getResult(headerCmdLockId);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					   semaphore.release();	
				}
			 }
		 return result; 
	   }

	   
	   public String getData0xAA0x0A(Data0xEE0x0A data0xEE0x0A,String remoteIp,int port){
		   String headerCmdLockId="ee"+data0xEE0x0A.getCmd(); //某一个设备某一个命令的标志位
   		   Semaphore semaphore=ObjectSephamorePool.getSemaphore(headerCmdLockId);	
   		   String result=null;
   		   boolean getLock=false;
		   try {
	   		 LOG.info("等待semaphore");
			  getLock =semaphore.tryAcquire(timeout,TimeUnit.SECONDS);   //让某一个设备的某一个命令在同一时刻只有一个线程下发,以下代码表示排队执行
			} catch (Exception e) {
				 e.printStackTrace();
			}
		   
			   if (getLock) {
				   try {
					   LOG.info("获得semaphore");
			           new ClientEE(remoteIp, port, data0xEE0x0A).run();	
			           result=ResultPool.getResult(headerCmdLockId);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					   semaphore.release();	
				}
			 }
		 return result; 
	   }

	   
	   public String getData0xAA0x0B(Data0xEE0x0B data0xEE0x0B,String remoteIp,int port){
	 		 String headerCmdLockId="ee"+data0xEE0x0B.getCmd()+data0xEE0x0B.getLockId(); //某一个设备某一个命令的标志位
	   		   Semaphore semaphore=ObjectSephamorePool.getSemaphore(headerCmdLockId);	
	   		   String result=null;
	   		 boolean getLock=false;
			   try {
				   LOG.info("等待semaphore");
				  getLock =semaphore.tryAcquire(timeout,TimeUnit.SECONDS);   //让某一个设备的某一个命令在同一时刻只有一个线程下发,以下代码表示排队执行
				} catch (Exception e) {
					 e.printStackTrace();
				}
			   
				   if (getLock) {
					   try {
						   LOG.info("获得semaphore");
				           new ClientEE(remoteIp, port, data0xEE0x0B).run();	
				           result=ResultPool.getResult(headerCmdLockId);
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						   semaphore.release();	
					}
				 }
			 return result; 
		   }
	   
	
}
