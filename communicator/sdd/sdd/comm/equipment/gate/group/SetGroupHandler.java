package com.kuangchi.sdd.comm.equipment.gate.group;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.GateLimitData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.comm.equipment.common.server.GateLimitInterface;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
/**
 * 设置时段表
 * 
 * */
public class SetGroupHandler extends
		BaseHandler<ByteBuf, GateLimitInterface, ReceiveData> {
	public static final Logger LOG = Logger.getLogger(SetGroupHandler.class);
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
		// 设置有效数据
		ByteBuf dataBuf = receiveBuffer.readBytes(1);// 取8字节有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;

		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		LOG.info(receiver.getCrcFromSum());
//		GateLimitInterface serverInfo = getDataForServer(data);
		String returnValue = null;
		if (receiver.getCrcFromSum() == checkSum) {
			LOG.info("数据校验成功！");
			returnValue = GsonUtil.toJson(data.getTimeBlock());
		}
		LOG.info("客户端接收的十六进制信息:"
				+ Util.lpad(Integer.toHexString(header), 2)
				+ "|"
				+ Util.lpad(Integer.toHexString(sign), 2)
				+ "|"
				// + Integer.toHexString(macAddress)
				+ Util.lpad(Integer.toHexString(mac), 6) + "|"
				+ Util.lpad(Integer.toHexString(order), 2) + "|"
				+ Util.lpad(Integer.toHexString(length), 3) + "|"
				+ Util.lpad(Integer.toHexString(orderStatus), 2) + "|"
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
		TimeBlock timeBlock = new TimeBlock();
        /**
         * 读取块号 1字节
         */
        int block = dataBuf.readUnsignedByte();
        timeBlock.setBlock(block);
        data.setTimeBlock(timeBlock);
		return data;
	}

	/**
	 * 配置返回给服务器的数据
	 * 
	 * @param data
	 * @return
	 */
	protected GateLimitInterface getDataForServer(ReceiveData data) {
		GateLimitInterface returnData = new GateLimitInterface();
		GateLimitData gateData = data.getGateLimitData();
		/**
		 * 读取卡编号 3字节
		 */
		int cardId = gateData.getCardId();
		returnData.setCardId(Integer.toHexString(cardId));
		/**
		 * 读取卡有效期（开始）2字节
		 */
		int[] start = gateData.getStart();
		String startStr = Util.getNullStr();

		for (int i = 0; i < start.length; i++) {
			startStr += Integer.toHexString(start[i]);
			System.out.println(start[i]);
		}
		returnData.setStart(startStr);
		/**
		 * 读取卡有效期（结束）2字节
		 */
		int[] end = gateData.getEnd();
		String endStr = Util.getNullStr();

		for (int i = 0; i < end.length; i++) {
			endStr += Integer.toHexString(end[i]);
		}
		returnData.setEnd(endStr);
		/**
		 * 读取时间组 4字节
		 */
		long group = gateData.getGroup();
		returnData.setGroup(Long.toHexString(group));
		LOG.info("group=" + group);
		/**
		 * 读取权限标记 2字节
		 */
		int limitSign = gateData.getLimitSign();
		returnData.setLimitSign(Integer.toHexString(limitSign));
		/**
		 * 读取删除标记 2字节
		 */
		int deleteSign = gateData.getDeleteSign();
		returnData.setLimitSign(Integer.toHexString(deleteSign));
		/**
		 * 读取保留字段 1字节
		 */
		int retain = gateData.getRetain();
		returnData.setRetain(Integer.toHexString(retain));
		LOG.info("retain=" + retain);
		return returnData;
	}
}
