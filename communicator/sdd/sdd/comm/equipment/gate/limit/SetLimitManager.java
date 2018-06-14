package com.kuangchi.sdd.comm.equipment.gate.limit;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateLimitData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstHandler;
/**
 * 设置门禁权限 
 * 
 * */
public class SetLimitManager extends Manager{
	public static final Logger LOG = Logger.getLogger(SetLimitManager.class);
   
	public SetLimitManager(DeviceInfo2 deviceInfo) {
		super(deviceInfo);
		// TODO Auto-generated constructor stub
	}
	public ByteBuf sendSetMachineParameter(Object data,
			SendHeader senderPkg) {
		return super.packageSenderBuf(senderPkg);
	}
    //配置有效数据
	@Override
	public ByteBuf setDataBuf(SendHeader info) {
		ByteBuf sendBuf = Unpooled.buffer();
		GateLimitData gateLimitData = info.getData().getGateLimitData();
		// 发送门号
		sendBuf.writeByte(gateLimitData.getGateId());
		// 发送下载序号
		sendBuf.writeByte(gateLimitData.getSeq());
		
		// 卡编号
		int cardId = gateLimitData.getCardId();
//		int c1, c2, c3;
//		c1 = (cardId & 0xFF0000) >> 16;
//		c2 = (cardId & 0x00FF00) >> 8;
//		c3 = (cardId & 0x0000FF);
		sendBuf.writeMedium(cardId);
		// 卡有效期（起始）
		int[] start = gateLimitData.getStart();
		if (start != null) {
			for (int i = 0; i < start.length; i++) {
				sendBuf.writeByte(start[i]);
				LOG.info("传入卡有效期");
			}
		}
		// 卡有效期（结束）
		int[] end = gateLimitData.getEnd();
		if (end != null) {
			for (int i = 0; i < end.length; i++) {
				sendBuf.writeByte(end[i]);
			}
		}
		// 开门密码
		sendBuf.writeChar(gateLimitData.getPassword());
		// 时段组4字节
		long group = gateLimitData.getGroup();
		long g1, g2, g3, g4;
		g1 = ((group>>>24) & 0x000000FF) ;
		g2 = (group & 0x00FF0000) >> 16;
		g3 = (group & 0x0000FF00) >> 8;
		g4 = (group & 0x000000FF);
		sendBuf.writeByte((int)g1);
		sendBuf.writeByte((int)g2);
		sendBuf.writeByte((int)g3);
		sendBuf.writeByte((int)g4);

		// 权限标志
		sendBuf.writeChar(gateLimitData.getLimitSign());
		// 删除标志
		sendBuf.writeByte(gateLimitData.getDeleteSign());
		// 保留字段
		sendBuf.writeByte(gateLimitData.getRetain());
		return sendBuf;
	}
}
