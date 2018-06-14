package com.kuangchi.sdd.comm.equipment.gate.workparam;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
/**
 *设置门工作参数
 * */
public class SetWorkParamManager extends Manager{
   
	public SetWorkParamManager(DeviceInfo2 deviceInfo) {
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
		GateParamData gateParamData = info.getData().getGateParamData();
		//发送门号
		sendBuf.writeByte(gateParamData.getGateId());
		//发送是否起用超级开门密码
		sendBuf.writeByte(gateParamData.getUseSuperPassword());
		//发送超级开门密码
		long superPassword = gateParamData.getSuperPassword();
		long s1 =  (superPassword & 0xFF000000) >> 24;
		long s2 = (superPassword & 0x00FF0000) >> 16;
		long s3 = (superPassword & 0x000000FF) >> 8;
		long s4 = (superPassword & 0x000000FF);
		sendBuf.writeByte((int)s1);
		sendBuf.writeByte((int)s2);
		sendBuf.writeByte((int)s3);
		sendBuf.writeByte((int)s4);
		//发送是否起用胁迫密码
		sendBuf.writeByte(gateParamData.getUseForcePassword());
		//发送胁迫密码
		int forcePassword = gateParamData.getForcePassword();
		int f1, f2;
		f1 = (forcePassword & 0xFF00) >> 8;
		f2 = (forcePassword & 0x00FF);
		sendBuf.writeByte(f1);
		sendBuf.writeByte(f2);
		//发送重锁密码
		int relockPassword = gateParamData.getRelockPassword();
		int r1, r2;
		r1 = (relockPassword & 0xFF00) >> 8;
		r2 = (relockPassword & 0x00FF);
		sendBuf.writeByte(r1);
		sendBuf.writeByte(r2);
		//发送解锁密码
		int unlockPassword = gateParamData.getUnlockPassword();
		int u1, u2;
		u1 = (unlockPassword & 0xFF00) >> 8;
		u2 = (unlockPassword & 0x00FF);
		sendBuf.writeByte(u1);
		sendBuf.writeByte(u2);
		//发送报警密码
		int policePassword = gateParamData.getPolicePassword();
		int p1, p2;
		p1 = (policePassword & 0xFF00) >> 8;
		p2 = (policePassword & 0x00FF);
		sendBuf.writeByte(p1);
		sendBuf.writeByte(p2);
		//发送功能模式
		sendBuf.writeByte(gateParamData.getOpenPattern());
		//发送验证开门模式
		sendBuf.writeByte(gateParamData.getCheckOpenPattern());
		//发送工作模式
		sendBuf.writeByte(gateParamData.getWorkPattern());
		//发送开门延时
		int openDelay = gateParamData.getOpenDelay();
		int o1, o2;
		o1 = (openDelay & 0xFF00) >> 8;
		o2 = (openDelay & 0x00FF);
		sendBuf.writeByte(o2);
		sendBuf.writeByte(o1);
		//发送开门超时
		sendBuf.writeByte(gateParamData.getOpenOvertime());
		//发送多卡开门数量
		sendBuf.writeByte(gateParamData.getMultiOpenNumber());
		//发送多卡开门卡号
		long[] multiOpenCard = gateParamData.getMultiOpenCard();
		if(multiOpenCard != null){
			long m0,m1, m2, m3;
			for (int i = 0; i < multiOpenCard.length; i++) {
				m0 = ((multiOpenCard[i] ) >>24)& 0x000000ff;
				m1 = ((multiOpenCard[i] ) >> 16)& 0x000000ff;
				m2 = ((multiOpenCard[i] ) >> 8)& 0x000000ff;
				m3 = (multiOpenCard[i] & 0x000000ff);
				sendBuf.writeByte(Long.valueOf(m3).intValue());
				sendBuf.writeByte(Long.valueOf(m2).intValue());
				sendBuf.writeByte(Long.valueOf(m1).intValue());
				sendBuf.writeByte(Long.valueOf(m0).intValue());//低位在前
			}
		}
		return sendBuf;
	}
}
