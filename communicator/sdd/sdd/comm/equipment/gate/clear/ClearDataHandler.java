package com.kuangchi.sdd.comm.equipment.gate.clear;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.ClearData;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeBlock;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.server.GateLimitInterface;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.util.excel.ExcelExportServer;
/**
 * 清 空控制器数据
 * */
public class ClearDataHandler extends BaseHandler<ByteBuf, GateLimitInterface, ReceiveData> {
	public static final Logger LOG = Logger.getLogger(ClearDataHandler.class);	
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
		ByteBuf dataBuf = receiveBuffer.readBytes(1);// 取1字节有效数据
		
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
			LOG.info("数据校验成功-----");
			returnValue = GsonUtil.toJson(data.getClearData());
			LOG.info("returnValue="+returnValue);
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
		ClearData clearData = new ClearData();
        /**
         * 读取块号 1字节
         */
        int dataType = dataBuf.readUnsignedByte();
        LOG.info("dataType="+dataType);
        clearData.setDataType(dataType);
        data.setClearData(clearData);
		return data;
	}

	/**
	 * 配置返回给服务器的数据
	 * 
	 * @param data
	 * @return
	 */
	protected GateLimitInterface getDataForServer(ReceiveData data) {
		
		return null;
	}
}
