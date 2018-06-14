package com.kuangchi.sdd.consum.businessBean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

import com.kuangchi.sdd.consum.bean.ParameterDownResponse;
import com.kuangchi.sdd.consum.bean.SinglePersonCard;
import com.kuangchi.sdd.consum.container.ConnectorFactory;
import com.kuangchi.sdd.consum.util.Util;
/**
 * 回复消费机类
 * 
 * */
public class UpToDownBusiness {
	// 读取参数请求包（软件发往终端，6字节）
	public void sendParameterRequest(String machine) {
		ChannelHandlerContext ctx = ConnectorFactory.getConnection(machine);
		ByteBuf byteBuf = Unpooled.buffer(6);
		byteBuf.writeShort(0x0b0a);
		byteBuf.writeShort(Integer.parseInt(machine));
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.getBytes(0, bytes);
		int crc16 = Util.getCrc16(4, bytes);
		// 校验和低位
		byteBuf.writeByte(crc16 & 0x00ff);
		// 校验和高位
		byteBuf.writeByte(crc16 >> 8);
		ctx.writeAndFlush(byteBuf);
	}

	// 设置终端参数包（软件发往终端，262字节）
	public void setTerminalParameter(ParameterDownResponse parameterDownResponse) {
		ChannelHandlerContext ctx = ConnectorFactory.getConnection(String
				.valueOf(parameterDownResponse.getMachine()));

		ByteBuf byteBuf = Unpooled.buffer(262);
		byteBuf.writeShort(0x0a09);
		byteBuf.writeShort(parameterDownResponse.getMachine());

		byteBuf.writeMedium(parameterDownResponse.getDataDown().getMoney());
		byteBuf.writeMedium(parameterDownResponse.getDataDown().getLimit());
		byteBuf.writeByte(parameterDownResponse.getDataDown().getConfirm());
		byteBuf.writeByte(parameterDownResponse.getDataDown().getMultiUse());
		byteBuf.writeMedium(parameterDownResponse.getDataDown().getPassword());
		for (int i = 0; i < 4; i++) {
			byteBuf.writeByte(parameterDownResponse.getDataDown().getMeal1()[i]);
		}
		for (int i = 0; i < 4; i++) {
			byteBuf.writeByte(parameterDownResponse.getDataDown().getMeal2()[i]);
		}
		for (int i = 0; i < 4; i++) {
			byteBuf.writeByte(parameterDownResponse.getDataDown().getMeal3()[i]);
		}
		for (int i = 0; i < 4; i++) {
			byteBuf.writeByte(parameterDownResponse.getDataDown().getMeal4()[i]);
		}

		byteBuf.writeByte(parameterDownResponse.getDataDown().getGroupsuport());
		byteBuf.writeByte(parameterDownResponse.getDataDown().getOnOffLineWay());
		for (int i = 0; i < 10; i++) {
			byteBuf.writeMedium(parameterDownResponse.getDataDown()
					.getGoodNumMoney()[i]);
		}

		byteBuf.writeByte(parameterDownResponse.getDataDown().getTimeLimit());
		for (int i = 0; i < 4; i++) {
			byteBuf.writeMedium(parameterDownResponse.getDataDown()
					.getTimeLimitMoney()[i]);
		}

		for (int i = 0; i < 4; i++) {
			byteBuf.writeByte(0);
		}

		// 写入餐次消费模式
		byteBuf.writeByte(parameterDownResponse.getDataDown().getMeal1Mode());
		byteBuf.writeByte(parameterDownResponse.getDataDown().getMeal2Mode());
		byteBuf.writeByte(parameterDownResponse.getDataDown().getMeal3Mode());
		byteBuf.writeByte(parameterDownResponse.getDataDown().getMeal4Mode());
		// ============

		for (int i = 0; i < 176; i++) {
			byteBuf.writeByte(parameterDownResponse.getDataDown().getRetain()[i]);
		}

		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.getBytes(0, bytes);
		int crc16 = Util.getCrc16(260, bytes);
		// 校验和低位
		byteBuf.writeByte(crc16 & 0x00ff);
		// 校验和高位
		byteBuf.writeByte(crc16 >> 8);
		ctx.writeAndFlush(byteBuf);

	}

	// 指令包（下行，5个字节）
	public void sendPersonCardDown(String machine) {
		ChannelHandlerContext ctx = ConnectorFactory.getConnection(machine);
		ByteBuf byteBuf = Unpooled.buffer(5);
		byteBuf.writeMedium(0x0101fe);
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.getBytes(0, bytes);
		int crc = Util.getXor(3, bytes);
		// 校验和
		byteBuf.writeByte(crc);
		// 校验和高位
		byteBuf.writeByte(0xff);
		ctx.writeAndFlush(byteBuf);

	}
//发送单卡名单
	public void sendSinglePersonCardDown(SinglePersonCard singlePersonCard) {
		ChannelHandlerContext ctx = ConnectorFactory.getConnection(String
				.valueOf(singlePersonCard.getMachine()));
		ByteBuf byteBuf = Unpooled.buffer(38);
		// 命令字
		byteBuf.writeShort(0x0706);
		// 机号2字节
		byteBuf.writeShort(singlePersonCard.getMachine());

		byteBuf.writeInt(Long.valueOf(singlePersonCard.getCardNum()).intValue());
		byteBuf.writeInt(Long.valueOf(singlePersonCard.getPersonNum())
				.intValue());
		byteBuf.writeByte(singlePersonCard.getLostFlowNum());
		byteBuf.writeByte(singlePersonCard.getFlag());
		byteBuf.writeMedium(singlePersonCard.getDeposit());
		byteBuf.writeByte(singlePersonCard.getRetain());
		String num = singlePersonCard.getNum();
		byte[] numBytes10 = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00 };
		byte[] numBytes = num.getBytes();
		for (int k = 0; k < (numBytes.length > 10 ? 10 : numBytes.length); k++) {
			numBytes10[k] = numBytes[k];
		}
		byteBuf.writeBytes(numBytes10);

		String name = singlePersonCard.getName();
		byte[] nameBytes8 = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
		byte[] nameBytes = null;
		try {
			nameBytes = name.getBytes("GBK");
			// nameBytes = name.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		for (int k = 0; k < (nameBytes.length > 8 ? 8 : nameBytes.length); k++) {
			nameBytes8[k] = nameBytes[k];
		}
		byteBuf.writeBytes(nameBytes8);

		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.getBytes(0, bytes);
		// 计算校验和
		int calCrc = Util.getCrc16(36, bytes);
		// 校验和
		byteBuf.writeByte(calCrc & 0x00ff);

		byteBuf.writeByte(calCrc >> 8);

		ctx.writeAndFlush(byteBuf);
	}
}
