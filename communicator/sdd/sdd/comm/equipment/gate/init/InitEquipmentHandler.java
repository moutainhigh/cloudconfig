package com.kuangchi.sdd.comm.equipment.gate.init;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.EquipmentDataForServer;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveEquipmentData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
/**
 * 设置通讯参数
 * 
 * */
public class InitEquipmentHandler extends
		BaseHandler<ByteBuf,EquipmentDataForServer, ReceiveData> {
	public static final Logger LOG = Logger.getLogger(SetFirstHandler.class);
	/**
	 * 处理接收信息的报头
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
		
		//获取有效数据
		ByteBuf dataBuf = receiveBuffer.readBytes(29);// 取34字节数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;
		
		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		String returnValue = null;
		LOG.info(receiver.getCrcFromSum());
		if(receiver.getCrcFromSum()==checkSum){
			LOG.info("数据校验成功！");
			returnValue = GsonUtil.toJson(data.getEquipmentData());
		}
		
//		System.out.println(checkCRC(receiver));
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
	protected ReceiveData executeData(ByteBuf dataBuf)  {
		ReceiveData commnonData = new ReceiveData();
		ReceiveEquipmentData data = new ReceiveEquipmentData();
		
		/**
		 * 设置卡号标志位
		 * 长度1字节 
		 * 0x08-4字节卡号 
		 * 0x04-3字节卡号
		 */
		short cardSign = dataBuf.readUnsignedByte();
		data.setCardSign(cardSign);
		/**
		 * 0xMMMMMMMM：子网掩码。例如：0xFFFFFF00 代表：255.255.255.0
		 */
		short maskA = dataBuf.readUnsignedByte();
		short maskB = dataBuf.readUnsignedByte();
		short maskC = dataBuf.readUnsignedByte();
		short maskD = dataBuf.readUnsignedByte();
		data.setMaskA(maskA);
		data.setMaskB(maskB);
		data.setMaskC(maskC);
		data.setMaskD(maskD);
		/**
		 * 0xGGGGGGGG：网关参数。例如：0xC0A8FE01 代表：192. 168. 254. 1
		 */
		short getewayA = dataBuf.readUnsignedByte();
		short getewayB = dataBuf.readUnsignedByte();
		short getewayC = dataBuf.readUnsignedByte();
		short getewayD = dataBuf.readUnsignedByte();
		data.setGatewayA(getewayA);
		data.setGatewayB(getewayB);
		data.setGatewayC(getewayC);
		data.setGatewayD(getewayD);
		/**
		 * 0xLLLLLLLL：M4本地IP地址
		 */
		short mechineIpA = dataBuf.readUnsignedByte();
		short mechineIpB = dataBuf.readUnsignedByte();
		short mechineIpC = dataBuf.readUnsignedByte();
		short mechineIpD = dataBuf.readUnsignedByte();
		data.setMechineIpA(mechineIpA);
		data.setMechineIpB(mechineIpB);
		data.setMechineIpC(mechineIpC);
		data.setMechineIpD(mechineIpD);
		/**
		 * 0xRRRRRRRR：M4远程IP地址
		 */
		short remoteIpA = dataBuf.readUnsignedByte();
		short remoteIpB = dataBuf.readUnsignedByte();
		short remoteIpC = dataBuf.readUnsignedByte();
		short remoteIpD = dataBuf.readUnsignedByte();
		data.setRemoteIpA(remoteIpA);
		data.setRemoteIpB(remoteIpB);
		data.setRemoteIpC(remoteIpC);
		data.setRemoteIpD(remoteIpD);
		/**
		 * BBBB：M4状态本地端口。心跳
		 */
		// int mechineStatusPort=dataBuf.readUnsignedShort()>>16;
		short mechineStatusPortA = dataBuf.readUnsignedByte();
		short mechineStatusPortB = dataBuf.readUnsignedByte();
		data.setMechineStatusPortA(mechineStatusPortA);
		data.setMechineStatusPortB(mechineStatusPortB);
		/**
		 * bbbb：M4状态远程端口。
		 */
		// int remoteStatusPort=dataBuf.readUnsignedShort();
		short remoteStatusPortA = dataBuf.readUnsignedByte();
		short remoteStatusPortB = dataBuf.readUnsignedByte();
		data.setRemoteStatusPortA(remoteStatusPortA);
		data.setRemoteStatusPortB(remoteStatusPortB);
		/**
		 * IIII：M4指令本地端口。服务器下发指令
		 */
		// int mechineOrderPort=dataBuf.readUnsignedShort();
		short mechineOrderPortA = dataBuf.readUnsignedByte();
		short mechineOrderPortB = dataBuf.readUnsignedByte();
		data.setMechineOrderPortA(mechineOrderPortA);
		data.setMechineOrderPortB(mechineOrderPortB);
		/**
		 * iiii：M4指令远程端口。
		 */
		// int remoteOrderPort=dataBuf.readUnsignedShort();
		short remoteOrderPortA = dataBuf.readUnsignedByte();
		short remoteOrderPortB = dataBuf.readUnsignedByte();
		data.setRemoteOrderPortA(remoteOrderPortA);
		data.setRemoteOrderPortB(remoteOrderPortB);
		/**
		 * EEEE：M4事件本地端口。获取设备事件，如开门
		 */
		// int mechineEventPort=dataBuf.readUnsignedShort();
		short mechineEventPortA = dataBuf.readUnsignedByte();
		short mechineEventPortB = dataBuf.readUnsignedByte();
		data.setMechineEventPortA(mechineEventPortA);
		data.setMechineEventPortB(mechineEventPortB);
		/**
		 * eeee：M4事件远程端口。
		 */
		// int remoteEventPort=dataBuf.readUnsignedShort();
		short remoteEventPortA = dataBuf.readUnsignedByte();
		short remoteEventPortB = dataBuf.readUnsignedByte();
		data.setRemoteEventPortA(remoteEventPortA);
		data.setRemoteEventPortB(remoteEventPortB);
		LOG.info("getCRC()="+data.getCrc());
		
		commnonData.setEquipmentData(data);
		return commnonData;
	}
	/**
	 * 配置返回给服务器的数据
	 * @param data
	 * @return
	 */
	protected EquipmentDataForServer getDataForServer(ReceiveData receiveData) {
		ReceiveEquipmentData data = receiveData.getEquipmentData();
		EquipmentDataForServer returnData = new EquipmentDataForServer();
        
		short mechineVersionA = data.getMechineVersionA();
		short mechineVersionB = data.getMechineVersionB();
		
		String[] mechineVersionStr = { Util.lpad(Integer.toHexString(mechineVersionA),2),
				Util.lpad(Integer.toHexString(mechineVersionB),2)};
		StringBuffer bufferForMechineVersion = new StringBuffer();
		for (String g : mechineVersionStr) {
			bufferForMechineVersion.append(g);
		}
		String mechineVersion = bufferForMechineVersion.toString();
		returnData.setMechineVersion(mechineVersion);
		
		short programVersionA = data.getProgramVersionA();
		short programVersionB = data.getProgramVersionB();
		short programVersionC = data.getProgramVersionC();
		String[] programVersionStr = { Util.lpad(Integer.toHexString(programVersionA),2),
				Util.lpad(Integer.toHexString(programVersionB),2),Util.lpad(Integer.toHexString(programVersionC),2)};
		StringBuffer bufferForProgramVersion = new StringBuffer();
		for (String g : programVersionStr) {
			bufferForProgramVersion.append(g);
		}
		String programVersion = bufferForProgramVersion.toString();
		returnData.setProgramVersion(programVersion);

		/**
		 * 子网掩码 255.255.255.0
		 */
		short maskA = data.getMaskA();
		short maskB = data.getMaskB();
		short maskC = data.getMaskC();
		short maskD = data.getMaskD();

		String[] mask = { Integer.toString(maskA), Integer.toString(maskB),
				Integer.toString(maskC), Integer.toString(maskD) };
		StringBuffer bufferForMask = new StringBuffer();
		for (String g : mask) {
			bufferForMask.append(g + ".");
		}
		String maskIp = bufferForMask.substring(0,
				bufferForMask.lastIndexOf("."));
		returnData.setMask(maskIp);
		/**
		 * 0xGGGGGGGG：网关参数。例如：0xC0A8FE01 代表：192. 168. 254. 1
		 */
		short getewayA = data.getGatewayA();
		short getewayB = data.getGatewayB();
		short getewayC = data.getGatewayC();
		short getewayD = data.getGatewayD();
		String[] geteway = { Integer.toString(getewayA),
				Integer.toString(getewayB), Integer.toString(getewayC),
				Integer.toString(getewayD) };
		StringBuffer bufferForGeteway = new StringBuffer();
		for (String g : geteway) {
			bufferForGeteway.append(g + ".");
		}
		String getewayIp = bufferForGeteway.substring(0,
				bufferForGeteway.lastIndexOf("."));
		returnData.setGateway(getewayIp);
		/**
		 * 0xLLLLLLLL：M4本地IP地址
		 */
		short mechineIpA = data.getMechineIpA();
		short mechineIpB = data.getMechineIpB();
		short mechineIpC = data.getMechineIpC();
		short mechineIpD = data.getMechineIpD();
		String[] mechineIP = { Integer.toString(mechineIpA),
				Integer.toString(mechineIpB), Integer.toString(mechineIpC),
				Integer.toString(mechineIpD) };
		StringBuffer bufferForMechineIP = new StringBuffer();
		for (String ip : mechineIP) {
			bufferForMechineIP.append(ip + ".");
			// bufferForMechineIP.
		}
		String mechineIPStr = bufferForMechineIP.substring(0,
				bufferForMechineIP.lastIndexOf("."));
		returnData.setMechineIP(mechineIPStr);
		/**
		 * 0xRRRRRRRR：M4远程IP地址
		 */
		short remoteIpA = data.getRemoteIpA();
		short remoteIpB = data.getRemoteIpB();
		short remoteIpC = data.getRemoteIpC();
		short remoteIpD = data.getRemoteIpD();
		String[] remoteIP = { Integer.toString(remoteIpA),
				Integer.toString(remoteIpB), Integer.toString(remoteIpC),
				Integer.toString(remoteIpD) };
		StringBuffer bufferForRemoteIP = new StringBuffer();
		for (String ip : remoteIP) {
			bufferForRemoteIP.append(ip + ".");
		}
		String remoteIPStr = bufferForRemoteIP.substring(0,
				bufferForRemoteIP.lastIndexOf("."));
		returnData.setRemoteIP(remoteIPStr);
		/**
		 * BBBB：M4状态本地端口。心跳
		 */
		// int mechineStatusPort=dataBuf.readUnsignedShort()>>16;
		short mechineStatusPortA = data.getMechineStatusPortA();
		short mechineStatusPortB = data.getMechineStatusPortB();
		String[] mechineStatusPortForCrc = {
				Integer.toHexString(mechineStatusPortB),
				Integer.toHexString(mechineStatusPortA) };
		StringBuffer mechineStatusPortBuffer = new StringBuffer();
		for (String ip : mechineStatusPortForCrc) {
			mechineStatusPortBuffer.append(ip);
		}
		String mechineStatusPortHex = mechineStatusPortBuffer.toString();
		int mechineStatusPort = Integer.valueOf(mechineStatusPortHex, 16);
		returnData.setMechineStatusPort(String.valueOf(mechineStatusPort));
		/**
		 * bbbb：M4状态远程端口。
		 */
		// int remoteStatusPort=dataBuf.readUnsignedShort();
		short remoteStatusPortA = data.getRemoteStatusPortA();
		short remoteStatusPortB = data.getRemoteStatusPortB();
		String[] remoteStatusPortForCrc = {
				Integer.toHexString(remoteStatusPortB),
				Integer.toHexString(remoteStatusPortA) };
		StringBuffer remoteStatusPortBuffer = new StringBuffer();
		for (String ip : remoteStatusPortForCrc) {
			remoteStatusPortBuffer.append(ip);
		}
		String remoteStatusPortHex = remoteStatusPortBuffer.toString();
		int remoteStatusPort = Integer.valueOf(remoteStatusPortHex, 16);
		returnData.setRemoteStatusPort(String.valueOf(remoteStatusPort));
		/**
		 * IIII：M4指令本地端口。服务器下发指令
		 */
		// int mechineOrderPort=dataBuf.readUnsignedShort();
		short mechineOrderPortA = data.getMechineOrderPortA();
		short mechineOrderPortB = data.getMechineOrderPortB();
		String[] mechineOrderPortForCrc = {
				Integer.toHexString(mechineOrderPortB),
				Integer.toHexString(mechineOrderPortA) };
		StringBuffer mechineOrderPortBuffer = new StringBuffer();
		for (String ip : mechineOrderPortForCrc) {
			mechineOrderPortBuffer.append(ip);
		}
		String mechineOrderPortHex = mechineOrderPortBuffer.toString();
		int mechineOrderPort = Integer.valueOf(mechineOrderPortHex, 16);
		returnData.setMechineOrderPort(String.valueOf(mechineOrderPort));
		/**
		 * iiii：M4指令远程端口。
		 */
		// int remoteOrderPort=dataBuf.readUnsignedShort();
		short remoteOrderPortA = data.getRemoteOrderPortA();
		short remoteOrderPortB = data.getRemoteOrderPortB();
		String[] remoteOrderPortForCrc = {
				Integer.toHexString(remoteOrderPortB),
				Integer.toHexString(remoteOrderPortA) };
		StringBuffer remoteOrderPortBuffer = new StringBuffer();
		for (String ip : remoteOrderPortForCrc) {
			remoteOrderPortBuffer.append(ip);
		}
		String remoteOrderPortHex = remoteOrderPortBuffer.toString();
		int remoteOrderPort = Integer.valueOf(remoteOrderPortHex, 16);
		returnData.setRemoteOrderPort(String.valueOf(remoteOrderPort));

		/**
		 * EEEE：M4事件本地端口。获取设备事件，如开门
		 */
		// int mechineEventPort=dataBuf.readUnsignedShort();
		short mechineEventPortA = data.getMechineEventPortA();
		short mechineEventPortB = data.getMechineEventPortB();
		String[] mechineEventPortForCrc = {
				Integer.toHexString(mechineEventPortB),
				Integer.toHexString(mechineEventPortA) };
		StringBuffer mechineEventPortBuffer = new StringBuffer();
		for (String ip : mechineEventPortForCrc) {
			mechineEventPortBuffer.append(ip);
		}
		String mechineEventPortHex = remoteOrderPortBuffer.toString();
		int mechineEventPort = Integer.valueOf(mechineEventPortHex, 16);
		returnData.setMechineEventPort(String.valueOf(mechineEventPort));

		/**
		 * eeee：M4事件远程端口。
		 */
		// int remoteEventPort=dataBuf.readUnsignedShort();
		short remoteEventPortA = data.getRemoteEventPortA();
		short remoteEventPortB = data.getRemoteEventPortB();
		String[] remoteEventPortForCrc = {
				Integer.toHexString(remoteEventPortB),
				Integer.toHexString(remoteEventPortA) };
		StringBuffer remoteEventPortBuffer = new StringBuffer();
		for (String ip : remoteEventPortForCrc) {
			remoteEventPortBuffer.append(ip);
		}
		String remoteEventPortHex = remoteOrderPortBuffer.toString();
		int remoteEventPort = Integer.valueOf(remoteEventPortHex, 16);
		returnData.setRemoteEventPort(String.valueOf(remoteEventPort));

		return returnData;
	}
}
