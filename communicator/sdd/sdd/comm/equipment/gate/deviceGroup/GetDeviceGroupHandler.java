package com.kuangchi.sdd.comm.equipment.gate.deviceGroup;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeBlock;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.server.GateLimitInterface;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadStatus3Handler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.util.excel.ExcelExportServer;
/**
 * 读取设备时段组
 * */
public class GetDeviceGroupHandler extends BaseHandler<ByteBuf, GateLimitInterface, ReceiveData>{
	public static final Logger LOG = Logger.getLogger(GetDeviceGroupHandler.class);	
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
		ByteBuf dataBuf = receiveBuffer.readBytes(513);// 取1字节有效数据
		
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;

		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		LOG.info("-------------orderStatus="+orderStatus);
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		LOG.info("-------------="+checkSum);
		receiver.setCrc(checkSum);

//		GateLimitInterface serverInfo = getDataForServer(data);
		String returnValue = null;
		if (receiver.getCrcFromSum() == checkSum) {
			LOG.info("数据校验成功-----");
			returnValue = GsonUtil.toJson(data.getDeviceTimeBlock());
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
		DeviceTimeBlock timeBlock = new DeviceTimeBlock();
        /**
         * 读取块号 1字节
         */
        int block = dataBuf.readUnsignedByte();
        LOG.info("block="+block);
        timeBlock.setBlock(block);
        data.setDeviceTimeBlock(timeBlock);
        List<DeviceTimeData> list=new ArrayList<DeviceTimeData>();
        for(int i=0;i<128;i++){
          int hour=	dataBuf.readUnsignedByte();
          int minute=dataBuf.readUnsignedByte();
          int retain=dataBuf.readUnsignedByte();
          int actionType=dataBuf.readUnsignedByte();

          DeviceTimeData deviceTimeData=new DeviceTimeData();
          deviceTimeData.setHour(hour);
          deviceTimeData.setMinute(minute);
          deviceTimeData.setActionType(actionType);
          deviceTimeData.setRetain(retain);
          LOG.info("hour="+hour+"minute="+minute+"actionType="+actionType);
          list.add(deviceTimeData);
        }
        timeBlock.setDeviceTimes(list);
        
        
        
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
