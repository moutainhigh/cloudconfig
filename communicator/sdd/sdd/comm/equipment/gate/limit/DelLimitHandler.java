package com.kuangchi.sdd.comm.equipment.gate.limit;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.GateLimitData;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.server.GateLimitInterface;
import com.kuangchi.sdd.comm.equipment.common.server.GateWorkParamInterface;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
/**
 * 删除门禁权限 
 * 
 * */

public class DelLimitHandler extends
		BaseHandler<ByteBuf,GateLimitInterface, ReceiveData> {	
	public static final Logger LOG = Logger.getLogger(DelLimitHandler.class);
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
		ByteBuf dataBuf = receiveBuffer.readBytes(5);// 取8字节有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;
		
		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		LOG.info(receiver.getCrcFromSum());
		GateLimitInterface serverInfo = getDataForServer(data);
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
		GateLimitData gateData = new GateLimitData();
		/**
		 * 读取门号
		 */
		int gateId = dataBuf.readUnsignedByte();
		gateData.setGateId(gateId);
		LOG.info("gateId="+gateId);
		/**
		 * 读取下载序号
		 */
        int seq = dataBuf.readUnsignedByte();
        gateData.setSeq(seq);
        LOG.info("seq="+seq);
        /**
         * 读取卡编号 3字节
         */
        int cardId = dataBuf.readUnsignedMedium();
        gateData.setCardId(cardId);
        LOG.info("cardId="+cardId);
        data.setGateLimitData(gateData);
		return data;
	}
	/**
	 * 配置返回给服务器的数据
	 * @param data
	 * @return
	 */
	protected GateLimitInterface getDataForServer(ReceiveData data) {
		GateLimitInterface returnData = new GateLimitInterface();
		GateLimitData gateData = data.getGateLimitData();
        /**
         * 读门号 1字节
         */
        int gateId = gateData.getGateId();
        returnData.setGateId(Integer.toHexString(gateId));
        /**
         * 读取下载序号
         */
        int seq = gateData.getSeq();
        returnData.setSeq(Integer.toHexString(seq));
        /**
         * 读取卡编号
         */
        int cardId = gateData.getCardId();
        returnData.setSeq(Integer.toHexString(cardId)); 
        
		return returnData;
	}
}
