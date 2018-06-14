package com.kuangchi.sdd.comm.equipment.gate.status;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.GateStatusData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;

/**
 * 目前没有用,项目中实际使用的设备状态是自动上报协议 ，该条协议是主动获取协议
 * */
public class GetStatusHandler extends
		BaseHandler<ByteBuf,GateTimeInterface, ReceiveData> {	
	public static final Logger LOG = Logger.getLogger(GetStatusHandler.class);
	/**
	 * 处理报头
	 * 
	 * @param receiveBuffer
	 */
	protected String executeResponse(ByteBuf receiveBuffer) {
		ReceiveHeader receiver = new ReceiveHeader();
		int header = receiveBuffer.readUnsignedByte();// 报头aa
		receiver.setHeader(header);
		int sign = receiveBuffer.readUnsignedByte();// 设备标志04
		receiver.setSign(sign);
		int mac = receiveBuffer.readUnsignedMedium();// MAC地址
		receiver.setMac(mac);
		int order = receiveBuffer.readUnsignedByte();// 命令字
		receiver.setOrder(order);
		int length = receiveBuffer.readUnsignedShort();// 有效数据长度 2字节
		receiver.setLength(length);
		//设置有效数据
		ByteBuf dataBuf = receiveBuffer.readBytes(25);// 取8字节有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;
		
		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		LOG.info(receiver.getCrcFromSum());
		
		String returnValue = null;
		if(receiver.getCrcFromSum()==checkSum){
			LOG.info("数据校验成功！");
			returnValue = GsonUtil.toJson(data.getGateStatusData());
		}
		LOG.info("客户端接收的十六进制信息:"
				+ Util.lpad(Integer.toHexString(header),2)
				+ "|"
				+ Util.lpad(Integer.toHexString(sign),2)
				+ "|"
				// + Integer.toHexString(macAddress)
				+ Util.lpad(Integer.toHexString(mac),6) + "|"
				+ Util.lpad(Integer.toHexString(order),2) + "|"
				+ Util.lpad(Integer.toHexString(length),3) + "|"
				+ Util.lpad(Integer.toHexString(orderStatus),2) + "|"
				+ checkSum + "|");
		return returnValue;

	}
	/**
	 * 获取有效数据
	 * 
	 * @param dataBuf
	 * @param bufLength
	 * @return
	 */
	protected ReceiveData executeData(ByteBuf dataBuf) {
		ReceiveData data = new ReceiveData();
		GateStatusData gateData = new GateStatusData();
		/**
		 * 读取锁的状态
		 */
		int lock = dataBuf.readUnsignedByte();
		gateData.setLock(lock);
		LOG.info("lock="+lock);
		/**
		 * 读取门磁状态
		 */
        int magnetic = dataBuf.readUnsignedByte();
        gateData.setMagnetic(magnetic);
        LOG.info("magnetic="+magnetic);
        /**
         * 读取按键状态
         */
        int key = dataBuf.readUnsignedByte();
        gateData.setKey(key);
        LOG.info("key="+key);
        /**
         * 读取防撬状态
         */
        int anti = dataBuf.readUnsignedByte();
        gateData.setAnti(anti);
        LOG.info("anti="+anti);
        /**
         * 读取消防状态
         */
        int fire = dataBuf.readUnsignedShort();
        gateData.setFire(fire);
        LOG.info("fire="+fire);
        /**
         * 读取门的事件信息集合
         */
        List<Long> events = new ArrayList<Long>();
        //0号门 出入与读头事件
        int access0 = dataBuf.readUnsignedMedium();
        int reader0 = dataBuf.readUnsignedByte();
        //0号门 出入与读头事件
        int access1 = dataBuf.readUnsignedMedium();
        int reader1 = dataBuf.readUnsignedByte();
        //0号门 出入与读头事件
        int access2 = dataBuf.readUnsignedMedium();
        int reader2 = dataBuf.readUnsignedByte();
        //0号门 出入与读头事件
        int access3 = dataBuf.readUnsignedMedium();
        int reader3 = dataBuf.readUnsignedByte();
        events.add(Long.valueOf(access0));
        events.add(Long.valueOf(reader0));
        events.add(Long.valueOf(access1));
        events.add(Long.valueOf(reader1));
        events.add(Long.valueOf(access2));
        events.add(Long.valueOf(reader2));
        events.add(Long.valueOf(access3));
        events.add(Long.valueOf(reader3));
        gateData.setEvent(events);
        data.setGateStatusData(gateData);
		return data;
	}
	@Override
	protected GateTimeInterface getDataForServer(ReceiveData data) {
		// TODO Auto-generated method stub
		return null;
	}
}
