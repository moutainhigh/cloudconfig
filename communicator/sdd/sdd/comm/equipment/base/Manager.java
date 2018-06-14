package com.kuangchi.sdd.comm.equipment.base;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;

public abstract class Manager {
	public static final Logger LOG = Logger.getLogger(Manager.class);
	public static int broadcastPort=18001;// 控制器接收广播的端口     local_order_port
	public static int localPort=18009;// 本地发送广播的端口   
	public  String broadcastIP="192.168.214.16";// 广播地址  local_ip_address
	public static  String localHostIP="192.168.10.81";// 本地IP地址   remote_id_address
	public  String equipmentIP="192.168.214.88";// 设备IP地址  local_ip_address
	public static int equipmentOrderPort=18003;// 设备命令端口  
	public  int equipmentRecordPort=18002;// 记录上报端口  local_event_port
	public static  int equipmentStatusPort=18013;// 设备状态端口 remote_state_port
	public static  int equipmentEventPort=18012;// 设备事件端口  remote_event_port

	
     
	
	
    
	public Manager() {
		super();
	}

	public Manager(DeviceInfo2 deviceInfo) {
		super();
		this.broadcastIP = deviceInfo.getBroadcastIP();
		this.equipmentIP = deviceInfo.getEquipmentIP();
		this.equipmentRecordPort = deviceInfo.getEquipmentRecordPort();
	}

	/**
	 * 发送传给设备的数据
	 * 
	 * @param data
	 * @param senderPkg
	 * @return
	 */
	protected ByteBuf packageSenderBuf(SendHeader info) {
		ByteBuf sendBuf = Unpooled.buffer();
		sendBuf.writeByte(info.getHeader());// 报头
		sendBuf.writeByte(info.getSign());// 设备标志
		sendBuf.writeMedium(info.getMac());
		sendBuf.writeByte(info.getOrder());// 命令字
		sendBuf.writeChar(info.getLength());// 有效数据长度 固定2字节
		ByteBuf dataBuf = setDataBuf(info);
		if (dataBuf != null) {
			sendBuf.writeBytes(dataBuf);
		}
		sendBuf.writeByte(info.getOrderStatus());// 命令状态
		sendBuf.writeChar(info.getCrc());// 校验和
		LOG.info(sendBuf.capacity());
		return sendBuf;
	}

	public abstract ByteBuf setDataBuf(SendHeader info);

	public abstract ByteBuf sendSetMachineParameter(Object data,
			SendHeader senderPkg);

	public int getBroadcastPort() {
		return broadcastPort;
	}

	public void setBroadcastPort(int broadcastPort) {
		this.broadcastPort = broadcastPort;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getBroadcastIP() {
		return broadcastIP;
	}

	public void setBroadcastIP(String broadcastIP) {
		this.broadcastIP = broadcastIP;
	}

	public String getLocalHostIP() {
		return localHostIP;
	}

	public void setLocalHostIP(String localHostIP) {
		this.localHostIP = localHostIP;
	}

	// 设备相关的端口和IP
	public String getEquipmentIP() {
		return equipmentIP;
	}

	public void setEquipmentIP(String equipmentIP) {
		this.equipmentIP = equipmentIP;
	}

	public int getEquipmentOrderPort() {
		return equipmentOrderPort;
	}

	public void setEquipmentOrderPort(int equipmentOrderPort) {
		this.equipmentOrderPort = equipmentOrderPort;
	}

	public int getEquipmentStatusPort() {
		return equipmentStatusPort;
	}

	public void setEquipmentStatusPort(int equipmentStatusPort) {
		this.equipmentStatusPort = equipmentStatusPort;
	}

	public int getEquipmentEventPort() {
		return equipmentEventPort;
	}

	public void setEquipmentEventPort(int equipmentEventPort) {
		this.equipmentEventPort = equipmentEventPort;
	}

	public int getEquipmentRecordPort() {
		return equipmentRecordPort;
	}

	public void setEquipmentRecordPort(int equipmentRecordPort) {
		this.equipmentRecordPort = equipmentRecordPort;
	}

}
