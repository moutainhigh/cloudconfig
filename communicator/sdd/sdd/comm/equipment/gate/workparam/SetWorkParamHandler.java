package com.kuangchi.sdd.comm.equipment.gate.workparam;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
import com.kuangchi.sdd.comm.equipment.common.server.GateWorkParamInterface;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;

/**
 *设置门工作参数
 * */
public class SetWorkParamHandler extends
		BaseHandler<ByteBuf,GateWorkParamInterface, ReceiveData> {	
	public static final Logger LOG = Logger.getLogger(SetFirstHandler.class);
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
		ByteBuf dataBuf = receiveBuffer.readBytes(46);// 取8字节有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;
		
		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		GateWorkParamInterface serverInfo = getDataForServer(data);
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
		GateParamData gateData = new GateParamData();
		/**
		 * 读取门号 1字节
		 */
		int gateId = dataBuf.readUnsignedByte();
		gateData.setGateId(gateId);
		LOG.info("gateId="+gateId);
		/**
		 * 读取是否起用超级开门密码  1字节
		 */
        int useSuperPassword = dataBuf.readUnsignedByte();
        gateData.setUseSuperPassword(useSuperPassword);
    	LOG.info("useSuperPassword="+useSuperPassword);
        /**
         * 读取超级开门密码 4字节
         */
        int superPassword = dataBuf.readInt();
        gateData.setSuperPassword(superPassword);
        LOG.info("superPassword="+superPassword);
        /**
         * 读取是否启用胁迫密码 1字节
         */
        int useForcePassword = dataBuf.readUnsignedByte();
        gateData.setUseForcePassword(useForcePassword);
        LOG.info("useForcePassword="+useForcePassword);
        /**
         * 读取胁迫密码 2字节
         */
        int forcePassword = dataBuf.readUnsignedShort();
        gateData.setForcePassword(forcePassword);
        LOG.info("forcePassword="+forcePassword);
        /**
         * 读取重锁密码 2字节
         */
        int relockPassword = dataBuf.readUnsignedShort();
        gateData.setRelockPassword(relockPassword);
        LOG.info("relockPassword="+relockPassword);
        /**
         * 读取解锁密码 2字节
         */
        int unlockPassword = dataBuf.readUnsignedShort();
        gateData.setUnlockPassword(unlockPassword);
        LOG.info("unlockPassword="+unlockPassword);
        /**
         * 读取报警密码 2字节
         */
        int policePassword = dataBuf.readUnsignedShort();
        gateData.setPolicePassword(policePassword);
        LOG.info("policePassword="+policePassword);
        /**
         * 读取功能模式 1字节
         */
        int openPattern = dataBuf.readUnsignedByte();
        gateData.setOpenPattern(openPattern);
        LOG.info("openPattern="+openPattern);
        /**
         * 读取验证开门模式 1字节
         */
        int checkOpenPattern = dataBuf.readUnsignedByte();
        gateData.setCheckOpenPattern(checkOpenPattern);
        LOG.info("checkOpenPattern="+checkOpenPattern);
        /**
         * 读取工作模式 1字节
         */
        int workPattern = dataBuf.readUnsignedByte();
        gateData.setWorkPattern(workPattern);
        LOG.info("workPattern="+workPattern);
        /**
         * 读取开门延时 2字节
         */
        int openDelay = dataBuf.readUnsignedShort();
        
        openDelay=((openDelay&0x00ff)<<8)|((openDelay&0xff00)>>8);      
        gateData.setOpenDelay(openDelay);
        LOG.info("openDelay="+openDelay);
        /**
         * 读取开门超时 1字节
         */
        int openOvertime = dataBuf.readUnsignedByte();
        gateData.setOpenOvertime(openOvertime);
        LOG.info("openOvertime="+openOvertime);
        /**
         * 读取多卡开门数量 1字节
         */
        int multiOpenNumber = dataBuf.readUnsignedByte();
        gateData.setMultiOpenNumber(multiOpenNumber);
        LOG.info("multiOpenNumber="+multiOpenNumber);
        /**
         * 读取多卡开门卡号 24字节 卡号为4字节
         */
        long[] multiOpenCard = new long[multiOpenNumber]; 
        for(int i=0;i<multiOpenNumber;i++)
        {
        	int m0,m1,m2,m3;
        	m0=dataBuf.readUnsignedByte();
        	m1=dataBuf.readUnsignedByte();
        	m2=dataBuf.readUnsignedByte();
        	m3=dataBuf.readUnsignedByte();
        	multiOpenCard[i]=((m3<<24)&0xff000000)|((m2<<16)&0x00ff0000)|((m1<<8)&0x0000ff00)|((m0)&0x000000ff);
        	LOG.info("multiOpenCard="+multiOpenCard[i]);
        }
        gateData.setMultiOpenCard(multiOpenCard);
        data.setGateData(gateData);
		return data;
	}
	/**
	 * 配置返回给服务器的数据
	 * @param data
	 * @return
	 */
	protected GateWorkParamInterface getDataForServer(ReceiveData data) {
		GateWorkParamInterface returnData = new GateWorkParamInterface();
		GateParamData gateData = data.getGateData();
		/**
		 * 读取门号
		 */
		int gateId = gateData.getGateId();
		returnData.setGateId(Integer.toHexString(gateId));
		/**
		 * 读取是否起用超级开门密码
		 */
        int useSuperPassword = gateData.getUseSuperPassword();
        returnData.setUseSuperPassword(Integer.toHexString(useSuperPassword));
        /**
         * 读取超级开门密码 4字节
         */
        long superPassword = gateData.getSuperPassword();
        returnData.setSuperPassword(Long.toHexString(superPassword));
        /**
         * 读取是否启用胁迫密码 1字节
         */
        int useForcePassword = gateData.getUseForcePassword();
        returnData.setUseForcePassword(Integer.toHexString(useForcePassword));
        /**
         * 读取胁迫密码 2字节
         */
        int forcePassword = gateData.getForcePassword();
        returnData.setForcePassword(Integer.toHexString(forcePassword));
        /**
         * 读取重锁密码 2字节
         */
        int relockPassword = gateData.getRelockPassword();
        returnData.setRelockPassword(Integer.toHexString(relockPassword));
        /**
         * 读取解锁密码 2字节
         */
        int unlockPassword = gateData.getUnlockPassword();
        returnData.setUnlockPassword(Integer.toHexString(unlockPassword));
        /**
         * 读取报警密码 2字节
         */
        int policePassword = gateData.getPolicePassword();
        returnData.setPolicePassword(Integer.toHexString(policePassword));
        /**
         * 读取功能模式 1字节
         */
        int openPattern = gateData.getOpenPattern();
        returnData.setOpenPattern(Integer.toHexString(openPattern));
        /**
         * 读取验证开门模式 1字节
         */
        int checkOpenPattern = gateData.getCheckOpenPattern();
        returnData.setCheckOpenPattern(Integer.toHexString(checkOpenPattern));
        /**
         * 读取工作模式 1字节
         */
        int workPattern = gateData.getWorkPattern();
        returnData.setWorkPattern(Integer.toHexString(workPattern));
        /**
         * 读取开门延时 2字节
         */
        int openDelay = gateData.getOpenDelay();
        returnData.setOpenDelay(Integer.toHexString(openDelay));
        /**
         * 读取开门超时 1字节
         */
        int openOvertime = gateData.getOpenOvertime();
        returnData.setOpenOvertime(Integer.toHexString(openOvertime));
        /**
         * 读取多卡开门数量 1字节
         */
        int multiOpenNumber = gateData.getMultiOpenNumber();
        returnData.setMultiOpenNumber(Integer.toHexString(multiOpenNumber));
        /**
         * 读取多卡开门卡号 24字节 卡号为3字节
         */
        long[] multiOpenCard = gateData.getMultiOpenCard();
        String multiOpenCardStr = Util.getNullStr();
        for(long card:multiOpenCard){
        	multiOpenCardStr += Long.toHexString(card);
        }
        returnData.setMultiOpenCard(multiOpenCardStr);	
		return returnData;
	}
}
