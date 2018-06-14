package com.kuangchi.sdd.comm.equipment.gate.holiday;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.server.Flag;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
/**
 * 获取节假日表
 * 
 * */

public class SetHolidayHandler extends
		BaseHandler<ByteBuf,GateTimeInterface, ReceiveData> {
	public static final Logger LOG = Logger.getLogger(SetHolidayHandler.class);
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
//		ByteBuf dataBuf = receiveBuffer.readBytes(8);// 取8字节有效数据
//		ReceiveData data = executeData(dataBuf);
//		receiver.setData(data);
//		dataBuf = null;
		
		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		LOG.info(receiver.getCrcFromSum());
//		GateTimeInterface serverInfo = getDataForServer(data);
		String returnValue = null;
		if(receiver.getCrcFromSum()==checkSum){
			Flag flag = new Flag();
			flag.setFlag("0");
			LOG.info("数据校验成功！");
			returnValue = GsonUtil.toJson(flag);
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
		TimeData timeData = new TimeData();
		/**
		 * 读取年
		 */
		int year = dataBuf.readChar();
		timeData.setYear(year);
		/**
		 * 读取月度
		 */
        short month = dataBuf.readUnsignedByte();
        timeData.setMonth(month);
        /**
         * 读取日
         */
        short day = dataBuf.readUnsignedByte();
        timeData.setDay(day);
        /**
         * 读取小时
         */
        short hour = dataBuf.readUnsignedByte();
        timeData.setHour(hour);
        /**
         * 读取分钟
         */
        short minute = dataBuf.readUnsignedByte();
        timeData.setMinute(minute);
        /**
         * 读取秒
         */
        short second = dataBuf.readUnsignedByte();
        timeData.setSecond(second);
        /**
         * 读取星期
         */
        short dayOfWeek = dataBuf.readUnsignedByte();
        timeData.setDayOfWeek(dayOfWeek);
        data.setTimeData(timeData);
        
		return data;
	}
	/**
	 * 处理返回给服务器的数据
	 * @param data
	 * @return
	 */
	protected GateTimeInterface getDataForServer(ReceiveData data) {
		GateTimeInterface returnData = new GateTimeInterface();
		TimeData timeData = data.getTimeData();
		String year = Integer.toHexString(timeData.getYear());
		String month = Integer.toHexString(timeData.getMonth());
		String day = Integer.toHexString(timeData.getDay());
		String hour = Integer.toHexString(timeData.getHour());
		String minute = Integer.toHexString(timeData.getMinute());
		String second = Integer.toHexString(timeData.getSecond());
		String week= Integer.toHexString(timeData.getDayOfWeek());
        returnData.setYear(year);
        returnData.setMonth(month);
        returnData.setDay(day);
        returnData.setHour(hour);
        returnData.setMinute(minute);
        returnData.setSecond(second);
        returnData.setDayOfWeek(week);
		return returnData;
	}
}
