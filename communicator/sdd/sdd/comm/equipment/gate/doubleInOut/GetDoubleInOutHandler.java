package com.kuangchi.sdd.comm.equipment.gate.doubleInOut;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadStatus3Handler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.util.excel.ExcelExportServer;
/**
 * 读取双向进出
 * */
public class GetDoubleInOutHandler extends BaseHandler<ByteBuf,GateTimeInterface, ReceiveData> {
	public static final Logger LOG = Logger.getLogger(GetDoubleInOutHandler.class);	
	@Override
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
		ByteBuf dataBuf = receiveBuffer.readBytes(1);// 取8字节有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;
		
		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		
		LOG.info(receiver.getCrcFromSum());
//		GateTimeInterface serverInfo = getDataForServer(data);
		String returnValue = null;
		if(receiver.getCrcFromSum()==checkSum){
			LOG.info("数据校验成功！");
			returnValue = GsonUtil.toJson(data.getGateData());
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

	@Override
	protected ReceiveData executeData(ByteBuf dataBuf) {
		ReceiveData data = new ReceiveData();
		GateParamData gateData = new GateParamData();
		/**
		 * 读取上传实时参数
		 */
		int inout = dataBuf.readUnsignedByte();
		gateData.setInout(inout);
		LOG.info("inout="+inout);
		
        data.setGateData(gateData);
		return data;
	}

	@Override
	protected GateTimeInterface getDataForServer(ReceiveData data) {
		return null;
	}

}
