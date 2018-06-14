package com.kuangchi.sdd.consum.handler.serverHandler.ping;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.consum.bean.CardNumPack;
import com.kuangchi.sdd.consum.bean.ConsumeRecordPack;
import com.kuangchi.sdd.consum.bean.DataUp;
import com.kuangchi.sdd.consum.bean.ParameterSetResponse;
import com.kuangchi.sdd.consum.bean.ParameterUpResponse;
import com.kuangchi.sdd.consum.bean.PersonCardPackUp;
import com.kuangchi.sdd.consum.bean.PingUp;
import com.kuangchi.sdd.consum.bean.SinglePersonCardUp;

public class BeanUtil {
	public static final Logger LOG = Logger.getLogger(BeanUtil.class);
	public static PingUp generatePingUp(ByteBuf in) {
		PingUp pingUp = new PingUp();
		// 读取头 1字节
		int headerV = in.readUnsignedByte();
		pingUp.setHeader(headerV);

		// 读取机器号 2字节
		int machine = in.readUnsignedShort();
		pingUp.setMachine(machine);

		// 读取校验和 1字节
		int crc = in.readUnsignedByte();
		pingUp.setCrc(crc);

		// 读取tail 1字节
		int tail = in.readUnsignedByte();
		pingUp.setTail(tail);

		return pingUp;
	}

	public static CardNumPack generateCardNumPack(ByteBuf in) {
		CardNumPack cardNumPack = new CardNumPack();
		// 读取头 1字节
		int headerV = in.readUnsignedByte();
		cardNumPack.setHeader(headerV);

		// 读取机器号 2字节
		int machine = in.readUnsignedShort();
		cardNumPack.setMachine(machine);

		// 读取卡号 4字节
		long cardNum = in.readUnsignedInt();
		cardNumPack.setCardNum(cardNum);
		// 读取校验和 1字节
		int crc = in.readUnsignedByte();
		cardNumPack.setCrc(crc);

		// 读取tail 1字节
		int tail = in.readUnsignedByte();
		cardNumPack.setTail(tail);

		return cardNumPack;
	}

	public static ConsumeRecordPack generateConsumeRecordPack(ByteBuf in) {
		ConsumeRecordPack consumeRecordPack = new ConsumeRecordPack();

		// 读取头 1字节
		int headerV = in.readUnsignedByte();
		consumeRecordPack.setHeader(headerV);

		// 读取机器号 2字节
		int machine = in.readUnsignedShort();
		consumeRecordPack.setMachine(machine);

		// 读取记录号 2字节
		int recordNum = in.readUnsignedShort();
		consumeRecordPack.setRecordNum(recordNum);

		// 读取记录类型 1字节

		int recordType = in.readUnsignedByte();
		consumeRecordPack.setRecordType(recordType);

		// 读取卡号 4字节
		long cardNum = in.readUnsignedInt();
		consumeRecordPack.setCardNum(cardNum);

		// 读取余额 3字节
		int balance = in.readUnsignedMedium();
		consumeRecordPack.setBalance(balance);

		// 读取金额 3字节
		int consumeSum = in.readUnsignedMedium();
		consumeRecordPack.setConsumeSum(consumeSum);

		// 读取年 1字节
		int year = in.readUnsignedByte();
		consumeRecordPack.setYear(year + 2000);

		// 读取月 1字节
		int month = in.readUnsignedByte();
		consumeRecordPack.setMonth(month);

		// 读取日 1字节
		int day = in.readUnsignedByte();
		consumeRecordPack.setDay(day);

		// 读取时 1字节
		int hour = in.readUnsignedByte();
		consumeRecordPack.setHour(hour);

		// 读取分 1字节
		int minute = in.readUnsignedByte();
		consumeRecordPack.setMinute(minute);

		// 读取秒 1字节
		int second = in.readUnsignedByte();
		consumeRecordPack.setSecond(second);

		// 卡流水
		int cardFlowNum = in.readUnsignedShort();
		consumeRecordPack.setCardFlowNum(cardFlowNum);

		// 读取保留字节
		/*
		 * byte[] retain=new byte[14]; in.readBytes(retain);
		 * consumeRecordPack.setRetain(retain);
		 */

		// 读取校验和 1字节
		int crc = in.readUnsignedByte();
		consumeRecordPack.setCrc(crc);

		// 读取tail 1字节
		int tail = in.readUnsignedByte();
		consumeRecordPack.setTail(tail);

		return consumeRecordPack;
	}

	public static ParameterUpResponse generateParameterUpResponse(ByteBuf in) {
		ParameterUpResponse parameterUpResponse = new ParameterUpResponse();
		// 读取头 1字节
		int headerV = in.readUnsignedByte();
		parameterUpResponse.setHeader(headerV);

		// 读取机器号 2字节
		int machine = in.readUnsignedShort();
		parameterUpResponse.setMachine(machine);

		DataUp dataUp = new DataUp();

		// 定额金额
		int money = in.readUnsignedMedium();
		dataUp.setMoney(money);
		// 机器限额
		int limit = in.readUnsignedMedium();
		dataUp.setLimit(limit);

		// 定额操作方式：是否按确认键完成消费
		int confirm = in.readUnsignedByte();
		dataUp.setConfirm(confirm);
		// 定额操作方式：同一张卡连续多次刷卡，是否提示确认
		int multiUse = in.readUnsignedByte();
		dataUp.setMultiUse(multiUse);
		// 消费机操作密码
		int password = in.readUnsignedMedium();
		dataUp.setPassword(password);
		// 餐次1起止时间

		for (int i = 0; i < 4; i++) {
			dataUp.getMeal1()[i] = in.readUnsignedByte();
		}
		// 餐次2起止时间
		for (int i = 0; i < 4; i++) {
			dataUp.getMeal2()[i] = in.readUnsignedByte();
		}
		// 餐次3起止时间
		for (int i = 0; i < 4; i++) {
			dataUp.getMeal3()[i] = in.readUnsignedByte();
		}
		// 餐次4起止时间
		for (int i = 0; i < 4; i++) {
			dataUp.getMeal4()[i] = in.readUnsignedByte();
		}
		// 终端支持的卡分组
		int groupsuport = in.readUnsignedByte();
		dataUp.setGroupsuport(groupsuport);
		// 脱机、联机方式（ID机型）
		int onOffLineWay = in.readUnsignedByte();
		dataUp.setOnOffLineWay(onOffLineWay);
		// 菜号金额
		for (int i = 0; i < 10; i++) {
			dataUp.getGoodNumMoney()[i] = in.readUnsignedMedium();
		}
		// 时段定额功能
		int timeLimit = in.readUnsignedByte();
		dataUp.setTimeLimit(timeLimit);
		// 4个餐次时段的定额值
		for (int i = 0; i < 4; i++) {
			dataUp.getTimeLimitMoney()[i] = in.readUnsignedMedium();
		}
		// 8位弃用字节
		for (int i = 0; i < 4; i++) {
			in.readByte();
		}
		// 读取餐次模式
		dataUp.setMeal1Mode(in.readByte());
		dataUp.setMeal2Mode(in.readByte());
		dataUp.setMeal3Mode(in.readByte());
		dataUp.setMeal4Mode(in.readByte());
		// 预留，全部填00
		for (int i = 0; i < 40; i++) {
			dataUp.getRetain()[i] = in.readUnsignedByte();
		}
		parameterUpResponse.setDataUp(dataUp);

		// 读取校验和 2字节
		int crcL = in.readUnsignedByte();// 高位
		int crcH = in.readUnsignedByte();// 低位
		parameterUpResponse.setCrc(crcH << 8 | crcL);
		LOG.info("===============================>>"
				+ parameterUpResponse.getDataUp().getPassword());
		return parameterUpResponse;

	}

	public static ParameterSetResponse generateParameterSetResponse(ByteBuf in) {
		ParameterSetResponse parameterSetResponse = new ParameterSetResponse();
		// 读取头 1字节
		int headerV = in.readUnsignedByte();
		parameterSetResponse.setHeader(headerV);

		// 读取机器号 2字节
		int machine = in.readUnsignedShort();
		parameterSetResponse.setMachine(machine);
		return parameterSetResponse;
	}

	public static PersonCardPackUp generatePersonCardUp(ByteBuf in) {
		PersonCardPackUp personCardPackUp = new PersonCardPackUp();
		// 读取头 1字节
		int headerV = in.readUnsignedByte();
		personCardPackUp.setHeader(headerV);

		// 读取机器号 2字节
		int machine = in.readUnsignedShort();
		personCardPackUp.setMachine(machine);

		// 读取块号
		int block = in.readUnsignedByte();
		personCardPackUp.setBlock(block);

		// 读取包
		int pack = in.readUnsignedByte();
		personCardPackUp.setPack(pack);

		// 读取校验和 1字节
		int crc = in.readUnsignedByte();
		personCardPackUp.setCrc(crc);

		// 读取tail 1字节
		int tail = in.readUnsignedByte();
		personCardPackUp.setTail(tail);

		return personCardPackUp;
	}

	public static SinglePersonCardUp generateSinglePersonCardUp(ByteBuf in) {
		SinglePersonCardUp singlePersonCardUp = new SinglePersonCardUp();
		// 读取头1字节
		int headerV = in.readUnsignedByte();
		singlePersonCardUp.setHeader(headerV);
		// 读取机器号 2字节
		int machine = in.readUnsignedShort();
		singlePersonCardUp.setMachine(machine);

		// 读取卡号
		long cardNum = in.readUnsignedInt();
		singlePersonCardUp.setCardNum(cardNum);

		// 读取工号
		long personNum = in.readUnsignedInt();
		singlePersonCardUp.setPersonNum(personNum);

		// 读取挂失流水号
		int lostFlowNum = in.readUnsignedByte();
		singlePersonCardUp.setLostFlowNum(lostFlowNum);

		// 名单标志
		int flag = in.readUnsignedByte();
		singlePersonCardUp.setFlag(flag);
		// 卡片余额 3字节
		int deposit = in.readUnsignedMedium();
		singlePersonCardUp.setDeposit(deposit);
		// 预留字节 1 字节
		int retain = in.readUnsignedByte();
		singlePersonCardUp.setRetain(retain);
		// 编号(员工工号) 10 字节
		byte[] numBytes = new byte[10];
		in.readBytes(numBytes);
		singlePersonCardUp.setNum(new String(numBytes));

		// 姓名
		byte[] nameBytes = new byte[8];
		in.readBytes(nameBytes);
		singlePersonCardUp.setName(new String(nameBytes));

		// 读取校验和 2字节
		int crcL = in.readUnsignedByte();// 高位
		int crcH = in.readUnsignedByte();// 低位
		singlePersonCardUp.setCrc(crcH << 8 | crcL);

		return singlePersonCardUp;
	}

}
