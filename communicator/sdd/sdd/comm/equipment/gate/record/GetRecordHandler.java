package com.kuangchi.sdd.comm.equipment.gate.record;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.GateRecordData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.server.GateLimitInterface;
import com.kuangchi.sdd.comm.equipment.common.server.GateRecordInterface;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;

/**
 * 读取门禁记录条数 
 * 
 * */
public class GetRecordHandler extends
		BaseHandler<ByteBuf,GateRecordInterface, ReceiveData> {	
	public static final Logger LOG = Logger.getLogger(GetRecordHandler.class);
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
		ByteBuf dataBuf = receiveBuffer.readBytes(23);// 取有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;
		
		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		LOG.info(receiver.getCrcFromSum());
		GateRecordInterface serverInfo = getDataForServer(data);
		String returnValue = null;
		if(receiver.getCrcFromSum()==checkSum){
			LOG.info("数据校验成功！");
			returnValue = GsonUtil.toJson(serverInfo);
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
		GateRecordData gateData = new GateRecordData();
		/**
		 * 读取门禁记录头指针
		 */
		long gateHeadPoint = dataBuf.readUnsignedInt();  //低位在前，高位在后
		gateHeadPoint=((gateHeadPoint&0xff000000)>>24)|((gateHeadPoint&0x00ff0000)>>8)|((gateHeadPoint&0x0000ff00)<<8)|((gateHeadPoint&0x000000ff)<<24);	
		gateData.setGatePoint(gateHeadPoint);
		/**
		 * 读取门禁记录数
		 */
        int gateRecord = dataBuf.readUnsignedShort();//低位在前，高位在后
        gateRecord=((gateRecord&0x0000ff00)>>8)|((gateRecord&0x000000ff)<<8);
        gateData.setGateRecord(gateRecord);
        /**
         * 读取巡更记录头指针
         */
        long patrolHeadPoint = dataBuf.readUnsignedInt();//低位在前，高位在后
        patrolHeadPoint=((patrolHeadPoint&0xff000000)>>24)|((patrolHeadPoint&0x00ff0000)>>8)|((patrolHeadPoint&0x0000ff00)<<8)|((patrolHeadPoint&0x000000ff)<<24);	
        gateData.setPatrolPoint(patrolHeadPoint);
        /**
         * 读取巡更记录数
         */
        int patrolRecord = dataBuf.readUnsignedShort();//低位在前，高位在后
        patrolRecord=((patrolRecord&0x0000ff00)>>8)|((patrolRecord&0x000000ff)<<8);
        gateData.setPatrolRecord(patrolRecord);
        /**
         * 用户记录头指针
         */
        long userHeadPoint = dataBuf.readUnsignedInt();//低位在前，高位在后
        userHeadPoint=((userHeadPoint&0xff000000)>>24)|((userHeadPoint&0x00ff0000)>>8)|((userHeadPoint&0x0000ff00)<<8)|((userHeadPoint&0x000000ff)<<24);	
        gateData.setUserPoint(userHeadPoint);
        /**
         * 读取用户记录数
         */
        int userRecord = dataBuf.readUnsignedShort();//低位在前，高位在后
        userRecord=((userRecord&0x0000ff00)>>8)|((userRecord&0x000000ff)<<8);
        gateData.setUserRecord(userRecord);
        /**
         * 读取门禁权限记录数   实际上是保留位
         */
        long gateLimitNum = dataBuf.readUnsignedInt();
        gateData.setGateLimitNum(gateLimitNum);
        /**
         * 读取巡更权限记录数   实际上是保留位
         */
        int patrolLimitNum = dataBuf.readUnsignedByte();
        gateData.setPatrolLimitNum(patrolLimitNum);
        data.setGateRecordData(gateData);
		return data;
	}
	/**
	 * 配置返回给服务器的数据
	 * @param data
	 * @return
	 */
	protected GateRecordInterface getDataForServer(ReceiveData data) {
		GateRecordInterface returnData = new GateRecordInterface();
		GateRecordData gateData = data.getGateRecordData();
		/**
		 * 读取门禁记录头指针
		 */
		long gatePoint = gateData.getGatePoint();
		returnData.setGatePoint(Long.toHexString(gatePoint));
		/**
		 * 读取门禁记录数
		 */
        int gateRecord =gateData.getGateRecord();
        returnData.setGateRecord(Long.toHexString(gateRecord));
        /**
         * 读取巡更记录头指针
         */
        long patrolHeadPoint = gateData.getPatrolPoint();
        returnData.setPatrolPoint(Long.toHexString(patrolHeadPoint));
        /**
         * 读取巡更记录数
         */
        int patrolRecord = gateData.getPatrolRecord();
        returnData.setPatrolRecord(Integer.toHexString(patrolRecord));
        /**
         * 用户记录头指针
         */
        long userHeadPoint = gateData.getUserPoint();
        returnData.setUserPoint(Long.toHexString(userHeadPoint));
        /**
         * 读取用户记录数
         */
        int userRecord = gateData.getUserRecord();
        returnData.setUserRecord(Integer.toHexString(userRecord));
        /**
         * 读取门禁权限记录数
         */
        long gateLimitNum = gateData.getGateLimitNum();
        returnData.setGateLimitNum(Long.toHexString(gateLimitNum));
        /**
         * 读取巡更权限记录数
         */
        int patrolLimitNum = gateData.getPatrolLimitNum();
        returnData.setPatrolLimitNum(Integer.toHexString(patrolLimitNum));
		return returnData;
	}
}
