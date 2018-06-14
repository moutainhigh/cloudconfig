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
import com.kuangchi.sdd.comm.equipment.upload.record.UploadStatus3Handler;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadStatus4Handler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;

/**
 * 获取门禁权限 
 * 
 * */
public class GetLimitHandler extends
		BaseHandler<ByteBuf,GateLimitInterface, ReceiveData> {	
	public static final Logger LOG = Logger.getLogger(GetLimitHandler.class);
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
		ByteBuf dataBuf = receiveBuffer.readBytes(23);// 取8字节有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;
		
		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		LOG.info(",,,,,,,,"+receiver.getCrcFromSum());
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
		 * 读取卡编号 3字节
		 */
		int cardId = dataBuf.readUnsignedMedium();
		gateData.setCardId(cardId);
		LOG.info("cardId=" + cardId);
		/**
		 * 读取卡有效期（开始）2字节
		 */
		int[] start = new int[5];
		for (int i = 0; i < 5; i++) {
			start[i] = dataBuf.readUnsignedByte();
			LOG.info("start---->" + i +"---"+ start[i]);
		}
		gateData.setStart(start);
		
		/**
		 * 读取卡有效期（结束）2字节
		 */
		int[] end = new int[5];
		for (int i = 0; i < 5; i++) {
			end[i] = dataBuf.readUnsignedByte();
			LOG.info("end---->" + i +"--"+ end[i]);
		}
		gateData.setEnd(end);
		
		int password=dataBuf.readUnsignedShort();
		gateData.setPassword(password);
		
		/**
		 * 读取时间组 4字节
		 */
		long group = dataBuf.readUnsignedInt();
		gateData.setGroup(group);
		LOG.info("group=" + group);
		/**
		 * 读取权限标记 2字节
		 */
		int limitSign = dataBuf.readUnsignedShort();
		gateData.setLimitSign(limitSign);
		LOG.info("limitSignooo=" + limitSign);
		/**
		 * 读取删除标记 2字节
		 */
		int deleteSign = dataBuf.readUnsignedByte();
		gateData.setDeleteSign(deleteSign);
		LOG.info("deleteSign=" + deleteSign);
		/**
		 * 读取保留字段 1字节
		 */
		int retain = dataBuf.readUnsignedByte();
		gateData.setRetain(retain);
		LOG.info("retain=" + retain);

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
         * 读取卡编号 3字节
         */
        int cardId = gateData.getCardId();
        returnData.setCardId(Integer.toHexString(cardId));
        /**
         * 读取卡有效期（开始）2字节
         */
        int[] start = gateData.getStart();
        String startStr = Util.getNullStr();
        startStr+=Integer.toHexString(start[0]);
        for(int i=1;i<start.length;i++){
        	startStr +="-"+ Integer.toHexString(start[i]);
        }
        returnData.setStart(startStr);
        /**
         * 读取卡有效期（结束）2字节
         */
        int[] end = gateData.getEnd();
        String endStr = Util.getNullStr();
        endStr +=Integer.toHexString(end[0]);
        for(int i=1;i<end.length;i++){
        	endStr +="-"+  Integer.toHexString(end[i]);
        }
        returnData.setEnd(endStr);

        
        int password=gateData.getPassword();
        returnData.setPassword(Integer.toHexString(password));
        
        long group = gateData.getGroup();
        returnData.setGroup(Long.toHexString(group));
        LOG.info("group="+group);
        /**
         * 读取权限标记 2字节
         */
        int limitSign = gateData.getLimitSign();
        LOG.info("//////////.."+limitSign);
        returnData.setLimitSign(Integer.toHexString(limitSign));
        /**
         * 读取删除标记 2字节
         */
        int deleteSign = gateData.getDeleteSign();
        returnData.setDeleteSign(Integer.toHexString(deleteSign));


        int retain = gateData.getRetain();
        returnData.setRetain(Integer.toHexString(retain));
        LOG.info("retain="+retain);
		return returnData;
	}
}
