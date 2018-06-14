package com.kuangchi.sdd.comm.equipment.gate.holiday;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.GateLimitData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupData;
import com.kuangchi.sdd.comm.equipment.common.server.GateLimitInterface;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
/**
 * 读取节假日表
 * 
 * */

public class GetHolidayHandler extends
		BaseHandler<ByteBuf,GateLimitInterface, ReceiveData> {	
	public static final Logger LOG = Logger.getLogger(GetHolidayHandler.class);
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
		ByteBuf dataBuf = receiveBuffer.readBytes(512);// 取8字节有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;
		
		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		LOG.info(receiver.getCrcFromSum());
//		GateLimitInterface serverInfo = getDataForServer(data);
		TimeBlock block = data.getTimeBlock();
		String returnValue = null;
		if(receiver.getCrcFromSum()==checkSum){
			LOG.info("数据校验成功！");
			returnValue = GsonUtil.toJson(block);
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
		TimeBlock timeBlock = new TimeBlock();
        /**
         * 读取时段组
         */
        List<TimeGroupData> groups = new ArrayList<TimeGroupData>();
        TimeGroupData group;
        TimeData time;
        for(int i=0;i<128;i++){
        	group = new TimeGroupData();
        	time = new TimeData();
        	time.setYear(dataBuf.readUnsignedByte());
        	time.setMonth(dataBuf.readUnsignedByte());
        	time.setDay(dataBuf.readUnsignedByte());
        	time.setDayOfWeek(dataBuf.readUnsignedByte());
        	group.setStart(time);
        	groups.add(group);
        	LOG.info(i+":年:"+time.getYear());
        	LOG.info(i+":月:"+time.getMonth());
        	LOG.info(i+":日:"+time.getDay());
        	LOG.info(i+":星期:"+time.getDayOfWeek());
        }
        timeBlock.setGroups(groups);
        data.setTimeBlock(timeBlock);
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
        for(int i=0;i<start.length;i++){
        	startStr += Integer.toHexString(start[i]);
        }
        returnData.setStart(startStr);
        /**
         * 读取卡有效期（结束）2字节
         */
        int[] end = gateData.getEnd();
        String endStr = Util.getNullStr();
        for(int i=0;i<end.length;i++){
        	endStr += Integer.toHexString(end[i]);
        }
        returnData.setEnd(endStr);
//        /**
//         * 读取时间组 4字节
//         */
//        long group = dataBuf.readUnsignedInt();
//        gateData.setGroup(group);
//        System.out.println("group="+group);
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
//        /**
//         * 读取保留字段 1字节
//         */
//        int retain = dataBuf.readUnsignedByte();
//        gateData.setRetain(retain);
//        System.out.println("retain="+retain);
		return returnData;
	}
}
