package com.kuangchi.sdd.zigbee.handler;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.util.GsonUtil;
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
import com.kuangchi.sdd.zigbee.pool.ObjectCountDownLatchPool;
import com.kuangchi.sdd.zigbee.pool.ResultPool;
import com.kuangchi.sdd.zigbee.util.Tool;

import io.netty.buffer.ByteBuf;
/**
 * 解析网关协议
 * */
public class ReadService {
	public static final Logger LOG = Logger.getLogger(ReadService.class);
     public static Data0xAA0x01 generateData0xAA0x01(ByteBuf byteBuf){
    	 Data0xAA0x01 data0xAA0x01=new Data0xAA0x01();
    	 int header= byteBuf.readUnsignedByte();
    	 data0xAA0x01.setHeader(header);
    	 int cmd=byteBuf.readUnsignedByte();
    	 data0xAA0x01.setCmd(cmd);
    	 int length=byteBuf.readUnsignedByte();
    	 data0xAA0x01.setLength(length);
    	 if (length!=0) {
    		 //lockId 锁ID
    		 StringBuffer lockId=new StringBuffer();
    		 for (int i = 0; i < 8; i++) {
				lockId.append(Tool.byteToHexString(byteBuf.readByte()));
			}
	    	 data0xAA0x01.setLockId(lockId.toString());
	    	 //roomNo 房间号
	    	 byte[] roomNo=new byte[4];
	    	  byteBuf.readBytes(roomNo);
	    	 data0xAA0x01.setRoomNo(new String(roomNo));
	    	 
	    	 // 固件版本号
	    	 StringBuffer firmwareVersion = new StringBuffer();
	    	 for (int i = 0; i < 3; i++) {
	    		 firmwareVersion.append(Integer.parseInt(Tool.byteToHexString(byteBuf.readByte())));
	    		 firmwareVersion.append(".");
			 }
	    	 if(!"".equals(firmwareVersion)){
	    		 firmwareVersion.setLength(firmwareVersion.length()-1);
	    	 }
	    	 data0xAA0x01.setFirmwareVersion(firmwareVersion.toString());
	    	 
	    	 // 软件版本号
	    	 StringBuffer softwareVersion = new StringBuffer();
	    	 for (int i = 0; i < 3; i++) {
	    		 softwareVersion.append(Integer.parseInt(Tool.byteToHexString(byteBuf.readByte())));
	    		 softwareVersion.append(".");
			 }
	    	 if(!"".equals(softwareVersion)){
	    		 softwareVersion.setLength(softwareVersion.length()-1);
	    	 }
	    	 data0xAA0x01.setSoftwareVersion(softwareVersion.toString());
    	 }
    	 int crc=byteBuf.readUnsignedByte();
    	 data0xAA0x01.setCrc(crc);
    	   String headerCmdLockId="ee"+cmd;
       	 ObjectCountDownLatchPool.release(headerCmdLockId);
       	// System.out.println("data0xAA0x01:"+GsonUtil.toJson(data0xAA0x01));
      	 ResultPool.putResult(headerCmdLockId, GsonUtil.toJson(data0xAA0x01));
    	 return data0xAA0x01;
     }
     
     
     public static Data0xAA0x02 generateData0xAA0x02(ByteBuf byteBuf){
    	 Data0xAA0x02 data0xAA0x02=new Data0xAA0x02();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x02.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x02.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x02.setLength(length);
     	 //锁Id
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x02.setLockId(lockId.toString());
     	 //状态码
     	 int status=byteBuf.readUnsignedByte();
     	 data0xAA0x02.setStatus(status);
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x02.setCrc(crc);
     	  String headerCmdLockId="ee"+cmd+lockId;
 	     ObjectCountDownLatchPool.release(headerCmdLockId);
  	// System.out.println("data0xAA0x02:"+GsonUtil.toJson(data0xAA0x02));
 	 ResultPool.putResult(headerCmdLockId, GsonUtil.toJson(data0xAA0x02));
     	 return data0xAA0x02; 
     }
     
     public static Data0xAA0x03 generateData0xAA0x03(ByteBuf byteBuf){
    	 Data0xAA0x03 data0xAA0x03=new Data0xAA0x03();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x03.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x03.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x03.setLength(length);
     	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x03.setLockId(lockId.toString());
     	 //状态码
     	 int status=byteBuf.readUnsignedByte();
     	 data0xAA0x03.setStatus(status);
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x03.setCrc(crc);
     	 String headerCmdLockId="ee"+cmd+lockId;
    	   ObjectCountDownLatchPool.release(headerCmdLockId);
     	// System.out.println("data0xAA0x03:"+GsonUtil.toJson(data0xAA0x03));  
     	 ResultPool.putResult(headerCmdLockId, GsonUtil.toJson(data0xAA0x03));
     	 return data0xAA0x03; 
     }
     
     public static Data0xAA0x04 generateData0xAA0x04(ByteBuf byteBuf){
    	 Data0xAA0x04 data0xAA0x04=new Data0xAA0x04();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x04.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x04.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x04.setLength(length);
     	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x04.setLockId(lockId.toString());
     	 //开门方式
     	 int openType=byteBuf.readUnsignedByte();
     	 data0xAA0x04.setOpenType(openType);
     	 
     	 if (openType==0x01) {
         	 //光ID
         	
         	 long photonId=byteBuf.readUnsignedInt();
         	 data0xAA0x04.setPhotonId(photonId);
		}

     	 
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x04.setCrc(crc);
     	 LOG.info("data0xAA0x04:"+GsonUtil.toJson(data0xAA0x04));
     	 return data0xAA0x04; 
    	 
     }
     
     
     public static Data0xAA0x05 generateData0xAA0x05(ByteBuf byteBuf){
    	 Data0xAA0x05 data0xAA0x05=new Data0xAA0x05();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x05.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x05.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x05.setLength(length);
     	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x05.setLockId(lockId.toString());
     	 //pan-id
     	 StringBuffer panId=new StringBuffer();
     	 for (int i = 0; i < 2; i++) {
     		panId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x05.setPanId(panId.toString());
    	 //roomNo 房间号
    	 byte[] roomNo=new byte[4];
    	  byteBuf.readBytes(roomNo);
    	 data0xAA0x05.setRoomNo(new String(roomNo));
     	 //电池电量
     	 byte[] bateryBalance=new byte[3];
     	 byteBuf.readBytes(bateryBalance);
     	 data0xAA0x05.setBateryBalance(new String(bateryBalance));
     	 
     	 int signalLevel=byteBuf.readUnsignedByte();
     	 data0xAA0x05.setSignalLevel(signalLevel);
     	 long time=byteBuf.readUnsignedInt();
     	 data0xAA0x05.setTime(time);
     	 
     	 
     	int communityNo=byteBuf.readUnsignedShort();
     	 data0xAA0x05.setCommunityNo(communityNo);
     	 
     	 
     	 
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x05.setCrc(crc);
     	
   	  String headerCmdLockId="ee"+cmd+lockId;
  	   ObjectCountDownLatchPool.release(headerCmdLockId);
  	// System.out.println("data0xAA0x05:"+GsonUtil.toJson(data0xAA0x05));
	 ResultPool.putResult(headerCmdLockId, GsonUtil.toJson(data0xAA0x05));
     	 return data0xAA0x05; 
     }
     
     public static Data0xAA0x06 generateData0xAA0x06(ByteBuf byteBuf){
    	 Data0xAA0x06 data0xAA0x06=new Data0xAA0x06();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x06.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x06.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x06.setLength(length);
     	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x06.setLockId(lockId.toString());
    	 //电池电量
     	 byte[] bateryBalance=new byte[3];
     	 byteBuf.readBytes(bateryBalance);
     	 data0xAA0x06.setBateryBalance(new String(bateryBalance));
     	 
    	 //roomNo 房间号
    	 byte[] roomNo=new byte[4];
    	  byteBuf.readBytes(roomNo);
    	 data0xAA0x06.setRoomNo(new String(roomNo));
    	 
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x06.setCrc(crc);
    	// System.out.println("data0xAA0x06:"+GsonUtil.toJson(data0xAA0x06));

     	 return data0xAA0x06; 
     }
     
     public static Data0xAA0x07 generateData0xAA0x07(ByteBuf byteBuf){
    	 Data0xAA0x07 data0xAA0x07=new Data0xAA0x07();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x07.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x07.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x07.setLength(length);
    	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x07.setLockId(lockId.toString());
         int status=byteBuf.readUnsignedByte();
         data0xAA0x07.setStatus(status);
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x07.setCrc(crc);
     	
     	String headerCmdLockId="ee"+cmd+lockId;
  	     ObjectCountDownLatchPool.release(headerCmdLockId);
  	   // System.out.println("data0xAA0x07:"+GsonUtil.toJson(data0xAA0x07));
  	 ResultPool.putResult(headerCmdLockId, GsonUtil.toJson(data0xAA0x07));
     	 return data0xAA0x07; 
     }
     
     
     public static  Data0xAA0x08  generateData0xAA0x08 (ByteBuf byteBuf){
    	 Data0xAA0x08 data0xAA0x08=new Data0xAA0x08();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x08.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x08.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x08.setLength(length);
    	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x08.setLockId(lockId.toString());
         int status=byteBuf.readUnsignedByte();
         data0xAA0x08.setStatus(status);
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x08.setCrc(crc);
     	String headerCmdLockId="ee"+cmd+lockId;
  	   ObjectCountDownLatchPool.release(headerCmdLockId);
  	 //System.out.println("data0xAA0x08:"+GsonUtil.toJson(data0xAA0x08));
 	 ResultPool.putResult(headerCmdLockId, GsonUtil.toJson(data0xAA0x08));
     	 return data0xAA0x08; 
     }
     
     public static Data0xAA0x09 generateData0xAA0x09(ByteBuf byteBuf){
    	 Data0xAA0x09 data0xAA0x09=new Data0xAA0x09();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x09.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x09.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x09.setLength(length);
    	 //zigBeeID
     	 StringBuffer zigBeeId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			 zigBeeId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x09.setZigBeeId(zigBeeId.toString());
         int status=byteBuf.readUnsignedByte();
         data0xAA0x09.setStatus(status);
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x09.setCrc(crc);
     	String headerCmdLockId="ee"+cmd+zigBeeId;
  	   ObjectCountDownLatchPool.release(headerCmdLockId);
  	// System.out.println("data0xAA0x09:"+GsonUtil.toJson(data0xAA0x09));
 	 ResultPool.putResult(headerCmdLockId, GsonUtil.toJson(data0xAA0x09));
     	 return data0xAA0x09; 
     }
     
     
     public static Data0xAA0x0A generateData0xAA0x0A(ByteBuf byteBuf){
    	 Data0xAA0x0A data0xAA0x0A=new Data0xAA0x0A();
    	 int header= byteBuf.readUnsignedByte();
    	 data0xAA0x0A.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	data0xAA0x0A.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x0A.setLength(length);
     	 //zigBeeID
     	 StringBuffer zigBeeId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			 zigBeeId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x0A.setZigBeeId(zigBeeId.toString());
     	 //pan-id
     	 StringBuffer panId=new StringBuffer();
     	 for (int i = 0; i < 2; i++) {
     		panId.append(Tool.byteToHexString(byteBuf.readByte()));
		 }
     	 data0xAA0x0A.setPanId(panId.toString());
     	 
     	 // 固件版本号
    	 StringBuffer firmwareVersion = new StringBuffer();
    	 for (int i = 0; i < 3; i++) {
    		 firmwareVersion.append(Integer.parseInt(Tool.byteToHexString(byteBuf.readByte())));
    		 firmwareVersion.append(".");
		 }
    	 if(!"".equals(firmwareVersion)){
    		 firmwareVersion.setLength(firmwareVersion.length()-1);
    	 }
    	 data0xAA0x0A.setFirmwareVersion(firmwareVersion.toString());
    	 
    	 // 软件版本号
    	 StringBuffer softwareVersion = new StringBuffer();
    	 for (int i = 0; i < 3; i++) {
    		 softwareVersion.append(Integer.parseInt(Tool.byteToHexString(byteBuf.readByte())));
    		 softwareVersion.append(".");
		 }
    	 if(!"".equals(softwareVersion)){
    		 softwareVersion.setLength(softwareVersion.length()-1);
    	 }
    	 data0xAA0x0A.setSoftwareVersion(softwareVersion.toString());
	 
     	 int crc=byteBuf.readUnsignedByte();
     	data0xAA0x0A.setCrc(crc);
  	  String headerCmdLockId="ee"+cmd;
	   ObjectCountDownLatchPool.release(headerCmdLockId);
	 // System.out.println("data0xAA0x0A:"+GsonUtil.toJson(data0xAA0x0A));
	  ResultPool.putResult(headerCmdLockId, GsonUtil.toJson(data0xAA0x0A));
     	 return data0xAA0x0A; 
     }
     
    
     
     public static Data0xAA0x0B generateData0xAA0x0B(ByteBuf byteBuf){
    	 Data0xAA0x0B data0xAA0x0B=new Data0xAA0x0B();
    	 int header= byteBuf.readUnsignedByte();
    	 data0xAA0x0B.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	data0xAA0x0B.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	data0xAA0x0B.setLength(length);
     	
     	 //lockId 锁ID
		 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
		  data0xAA0x0B.setLockId(lockId.toString());
	   	  
	   	  int status=byteBuf.readUnsignedByte();
	   	  data0xAA0x0B.setStatus(status);
     	  int crc=byteBuf.readUnsignedByte();
     	   data0xAA0x0B.setCrc(crc);
  	     String headerCmdLockId="ee"+cmd+lockId;
	     ObjectCountDownLatchPool.release(headerCmdLockId);
	 // System.out.println("data0xAA0x0A:"+GsonUtil.toJson(data0xAA0x0B));
	  ResultPool.putResult(headerCmdLockId, GsonUtil.toJson(data0xAA0x0B));
     	 return data0xAA0x0B; 
     }
     
     public static Data0xAA0x0C generateData0xAA0x0C(ByteBuf byteBuf){
    	 Data0xAA0x0C data0xAA0x0C=new Data0xAA0x0C();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x0C.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x0C.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x0C.setLength(length);
     	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x0C.setLockId(lockId.toString());
     	 
     	 
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x0C.setCrc(crc);
     	 LOG.info("data0xAA0x0C:"+GsonUtil.toJson(data0xAA0x0C));
     	 return data0xAA0x0C; 
    	 
     }
     
     public static Data0xAA0x0D generateData0xAA0x0D(ByteBuf byteBuf){
    	 Data0xAA0x0D data0xAA0x0D=new Data0xAA0x0D();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x0D.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x0D.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x0D.setLength(length);
     	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x0D.setLockId(lockId.toString());
     	 
     	 
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x0D.setCrc(crc);
    	 LOG.info("data0xAA0x0D:"+GsonUtil.toJson(data0xAA0x0D));
     	 return data0xAA0x0D; 
    	 
     }
     
     
     public static Data0xAA0x0E generateData0xAA0x0E(ByteBuf byteBuf){
    	 Data0xAA0x0E data0xAA0x0E=new Data0xAA0x0E();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x0E.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x0E.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x0E.setLength(length);
     	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x0E.setLockId(lockId.toString());
     	 // 密码
     	 /*StringBuffer password=new StringBuffer();
		 for (int i = 0; i < 16; i++) {
			 password.append(Tool.byteToHexString(byteBuf.readByte()));
		}*/
     	 data0xAA0x0E.setPassword("0000000000000000");
     	 

     	 
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x0E.setCrc(crc);
    	 LOG.info("data0xAA0x0E:"+GsonUtil.toJson(data0xAA0x0E));
     	 return data0xAA0x0E; 
    	 
     }
     
     public static Data0xAA0x0F generateData0xAA0x0F(ByteBuf byteBuf){
    	 Data0xAA0x0F data0xAA0x0F=new Data0xAA0x0F();
    	 int header= byteBuf.readUnsignedByte();
     	 data0xAA0x0F.setHeader(header);
     	 int cmd=byteBuf.readUnsignedByte();
     	 data0xAA0x0F.setCmd(cmd);
     	 int length=byteBuf.readUnsignedByte();
     	 data0xAA0x0F.setLength(length);
     	 //锁ID
     	 StringBuffer lockId=new StringBuffer();
		 for (int i = 0; i < 8; i++) {
			lockId.append(Tool.byteToHexString(byteBuf.readByte()));
		}
     	 data0xAA0x0F.setLockId(lockId.toString());
     	 
     	 
     	 int crc=byteBuf.readUnsignedByte();
     	 data0xAA0x0F.setCrc(crc);
    	 LOG.info("data0xAA0x0F:"+GsonUtil.toJson(data0xAA0x0F));
     	 return data0xAA0x0F; 
    	 
     }
     
     
}
