package com.kuangchi.sdd.comm.equipment.gate.init;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.List;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.EquipmentData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
/**
 * 设置通讯参数
 * 
 * */
public class InitEquipmentManager extends Manager {
	public static final Logger LOG = Logger.getLogger(InitEquipmentManager.class);
	
	 

	public InitEquipmentManager(DeviceInfo2 deviceInfo) {
		super(deviceInfo);
		// TODO Auto-generated constructor stub
	}

	public ByteBuf sendSetMachineParameter(Object data, SendHeader senderPkg) {
		return super.packageSenderBuf(senderPkg);
	}

	// 配置有效数据
	@Override
	public ByteBuf setDataBuf(SendHeader info) {
		ByteBuf sendBuf = Unpooled.buffer();
		EquipmentData equipment = info.getData().getEquipmentData();
		sendBuf.writeByte(equipment.getCardSign());
		LOG.info(equipment.getCardSign());
		// 子网掩码
		List<Integer> mask = equipment.getMask();
		for (Integer m : mask) {
			LOG.info(m);
			System.out.println();
			sendBuf.writeByte(m);
		}
		// 网关
		List<Integer> gateWay = equipment.getGateway();
		for (Integer g : gateWay) {
			LOG.info(g);
			sendBuf.writeByte(g);
		}
		/**
		 * 0xLLLLLLLL：M4本地IP地址
		 */
		List<Integer> mechineIP = equipment.getMechineIP();
		for (Integer mp : mechineIP) {
			LOG.info(mp);
			sendBuf.writeByte(mp);
		}
		/**
		 * 0xRRRRRRRR：M4远程IP地址
		 */
		List<Integer> remoteIP = equipment.getRemoteIP();
		for (Integer rp : remoteIP) {
			LOG.info(rp);
			sendBuf.writeByte(rp);
		}
		/**
		 * BBBB：M4状态本地端口。心跳
		 */
		short[] mechineStatusPort = equipment.getMechineStatusPort();
		for (int i = 0; i < mechineStatusPort.length; i++) {
			sendBuf.writeByte(mechineStatusPort[i]);
			LOG.info(mechineStatusPort[i]);
			
		}
		/**
		 * bbbb：M4状态远程端口。
		 */
		short[] remoteStatusPort = equipment.getRemoteStatusPort();
		for (int i = 0; i < remoteStatusPort.length; i++) {
			sendBuf.writeByte(remoteStatusPort[i]);
			LOG.info(remoteStatusPort[i]);
		}
		/**
		 * IIII：M4指令本地端口。服务器下发指令
		 */
		short[] mechineOrderPort = equipment.getMechineOrderPort();
		for (int i = 0; i < mechineOrderPort.length; i++) {
			sendBuf.writeByte(mechineOrderPort[i]);
			LOG.info(mechineOrderPort[i]);
		}
		/**
		 * iiii：M4指令远程端口。
		 */
		short[] remoteOrderPort = equipment.getRemoteOrderPort();
		for (int i = 0; i < remoteOrderPort.length; i++) {
			sendBuf.writeByte(remoteOrderPort[i]);
			LOG.info(remoteOrderPort[i]);
		}
		/**
		 * EEEE：M4事件本地端口。获取设备事件，如开门
		 */
		short[] mechineEventPort = equipment.getMechineEventPort();
		for (int i = 0; i < mechineEventPort.length; i++) {
			sendBuf.writeByte(mechineEventPort[i]);
			LOG.info(mechineEventPort[i]);
		}
		/**
		 * eeee：M4事件远程端口。
		 */
		short[] remoteEventPort = equipment.getRemoteEventPort();
		for (int i = 0; i < remoteEventPort.length; i++) {
			sendBuf.writeByte(remoteEventPort[i]);
			LOG.info(remoteEventPort[i]);
		}
		return sendBuf;
	}
}
