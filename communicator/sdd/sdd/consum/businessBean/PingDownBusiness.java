package com.kuangchi.sdd.consum.businessBean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.consum.bean.CardNumPack;
import com.kuangchi.sdd.consum.bean.ConsumeRecordPack;
import com.kuangchi.sdd.consum.bean.DepositDown;
import com.kuangchi.sdd.consum.bean.PersonCard;
import com.kuangchi.sdd.consum.bean.PersonCardPackDown;
import com.kuangchi.sdd.consum.bean.PersonCardPackUp;
import com.kuangchi.sdd.consum.bean.PersonCardTask;
import com.kuangchi.sdd.consum.bean.PingUp;
import com.kuangchi.sdd.consum.bean.ResultMsg2;
import com.kuangchi.sdd.consum.bean.SinglePersonCardUp;
import com.kuangchi.sdd.consum.handler.serverHandler.ping.SendPersonDownThread;
import com.kuangchi.sdd.consum.util.Util;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.network.HttpRequest;
/**
 * 回复消费机类
 * 
 * */
public class PingDownBusiness {
	public static final Logger LOG = Logger.getLogger(PingDownBusiness.class);
	// 软件下发，回复心跳包（13字节）
	public void responsePing(PingUp pingUp, ChannelHandlerContext ctx) {

		ResultMsg2 resultMsg = new ResultMsg2();


		// 获取机号
		ByteBuf reByteBuf = Unpooled.buffer(13);

		// 首字 2字节
		reByteBuf.writeShort(0x0403);

		// 机号2字节
		reByteBuf.writeShort(pingUp.getMachine());

		// 获取时间
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// 当前年
		int year = Integer.parseInt(String.valueOf(cal.get(Calendar.YEAR))
				.substring(2, 4));
		// 当前月
		int month = (cal.get(Calendar.MONTH)) + 1;
		// 当前月的第几天：即当前日
		int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
		// 当前时：HOUR_OF_DAY-24小时制；HOUR-12小时制
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		// 当前分
		int minute = cal.get(Calendar.MINUTE);
		// 当前秒
		int second = cal.get(Calendar.SECOND);
		// 获取星期几
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

		reByteBuf.writeByte(year);
		reByteBuf.writeByte(month);
		reByteBuf.writeByte(day_of_month);
		reByteBuf.writeByte(hour);
		reByteBuf.writeByte(minute);
		reByteBuf.writeByte(second);
		reByteBuf.writeByte(day_of_week);

		byte[] bytes = new byte[reByteBuf.readableBytes()];
		reByteBuf.getBytes(0, bytes);
		// 计算校验和
		int calCrc = Util.getXor(11, bytes);
		// 校验和
		// reByteBuf.writeByte(calCrc);
		reByteBuf.writeByte(0x00);// 测试发现回复心跳包并没有校验
		reByteBuf.writeByte(0xff);

		ctx.writeAndFlush(reByteBuf);
	}

	// POS01H汉显系列余额包（软件发往终端，52个字节）
	public void responseDeposit(CardNumPack cardNumPack,
			ChannelHandlerContext ctx) {

		// ============业务逻辑查询余额====================

		String url = PropertiesToMap.propertyToMap(
				"photoncard_interface.properties").get("url")
				+ "interface/consumeHandle/issuedBalance.do?";
		String data = HttpRequest.sendPost(url,
				"device_num=" + cardNumPack.getMachine() + "&card_num="
						+ cardNumPack.getCardNum());
		HashMap<String, Object> map = GsonUtil.toBean(data, HashMap.class);
		LOG.info("==" + map.get("depositDown"));
		DepositDown depositDown = GsonUtil.toBean(map.get("depositDown")
				.toString(), DepositDown.class);


		// =============业务逻辑查询余额===================

		// 获取机号
		ByteBuf reByteBuf = Unpooled.buffer(52);

		// 首字 2字节
		reByteBuf.writeShort(0xd22d); // 固定值 命令字

		// 机号2字节
		reByteBuf.writeShort(depositDown.getMachine());

		// 卡号
		reByteBuf.writeInt(Long.valueOf(depositDown.getCardNum()).intValue());

		// 报警
		reByteBuf.writeByte(depositDown.getAlm());

		// 余额
		reByteBuf.writeMedium(depositDown.getBalance()); // 单位是分

		// 密码
		reByteBuf.writeMedium(Util.getIntHex(depositDown.getPassword())); // 3字节密码
																			// 如果密码是字符888888
																			// 则直接下发0x888888
																			// 或十进制8947848
		// 限额
		reByteBuf.writeMedium(depositDown.getLimit());

		String num = depositDown.getNum();
		byte[] numBytes16 = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
		byte[] numBytes = num.getBytes();
		for (int i = 0; i < (numBytes.length > 16 ? 16 : numBytes.length); i++) {
			numBytes16[i] = numBytes[i];
		}

		// 编号 不足16位补FF
		reByteBuf.writeBytes(numBytes16);

		String name = depositDown.getName();
		byte[] nameBytes16 = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
		byte[] nameBytes = null;
		try {
			nameBytes = name.getBytes("GBK");
			// nameBytes = name.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < (nameBytes.length > 16 ? 16 : nameBytes.length); i++) {
			nameBytes16[i] = nameBytes[i];
		}

		// 姓名 不足16位补FF
		reByteBuf.writeBytes(nameBytes16);

		byte[] bytes = new byte[reByteBuf.readableBytes()];
		reByteBuf.getBytes(0, bytes);
		// 计算校验和
		int calCrc = Util.getXor(49, bytes);
		// 校验和
		reByteBuf.writeByte(calCrc);

		reByteBuf.writeByte(0xff);

		ctx.writeAndFlush(reByteBuf);
	}

	// 应答包（软件发往终端，8个字节）
	public void responsePack(ConsumeRecordPack consumeRecordPack,
			ChannelHandlerContext ctx) {
		// ===============记录消费记录==================
		String url = PropertiesToMap.propertyToMap(
				"photoncard_interface.properties").get("url")
				+ "interface/consumeHandle/recordReport.do?";
		String str = HttpRequest.sendPost(url,
				"data=" + GsonUtil.toJson(consumeRecordPack));
		LOG.info(str);

		// ==============记录消费记录==============

		ByteBuf reByteBuf = Unpooled.buffer(8);
		// 首
		reByteBuf.writeShort(0x0302);
		// 机号2字节
		reByteBuf.writeShort(consumeRecordPack.getMachine());

		// 记录号
		reByteBuf.writeShort(consumeRecordPack.getRecordNum());

		byte[] bytes = new byte[reByteBuf.readableBytes()];
		reByteBuf.getBytes(0, bytes);
		// 计算校验和
		int calCrc = Util.getXor(6, bytes);
		// 校验和
		reByteBuf.writeByte(calCrc);

		reByteBuf.writeByte(0xff);

		ctx.writeAndFlush(reByteBuf);
	}

	// 下发名单包
	public void sendPersonCardPacksDown(PersonCardPackUp personCardPackUp,
			ChannelHandlerContext ctx) {
		// ===============查询出名单准备下发==================
		// 调用一卡通的接口获取<=8条下发名单
		String consumUrl = PropertiesToMap.propertyToMap(
				"photoncard_interface.properties").get("url")
				+ "interface/cDevice/getSomeTask.do";

		String str = HttpRequest.sendPost(consumUrl,
				"machine=" + personCardPackUp.getMachine() + "&pack="
						+ personCardPackUp.getPack());// 调用一卡通的方法获取下发名单

		Gson gson = new Gson();
		List<PersonCardTask> taskList = gson.fromJson(str,
				new TypeToken<List<PersonCardTask>>() {
				}.getType());

		// /////////////////////拼装数据
		PersonCardPackDown personCardPackDown = new PersonCardPackDown();
		if (taskList != null && taskList.size() >= 1) {
			PersonCard personCard1 = new PersonCard();
			personCard1.setCardNum(Long.valueOf(taskList.get(0).getCard_num()));
			personCard1.setPersonNum(Long
					.valueOf(taskList.get(0).getStaff_id()));
			personCard1.setLostFlowNum(00);
			personCard1.setFlag(0xff);
			personCard1.setDeposit(Double.valueOf(
					(Double.valueOf(taskList.get(0).getBalance()) * 100))
					.intValue());
			personCard1.setRetain(0x00);
			personCard1.setNum(taskList.get(0).getStaff_no());
			personCard1.setName(taskList.get(0).getStaff_name());

			personCardPackDown.getPersonCards()[0] = personCard1;
		}

		if (taskList != null && taskList.size() >= 2) {
			PersonCard personCard2 = new PersonCard();
			personCard2.setCardNum(Long.valueOf(taskList.get(1).getCard_num()));
			personCard2.setPersonNum(Long
					.valueOf(taskList.get(1).getStaff_id()));
			personCard2.setLostFlowNum(00);
			personCard2.setFlag(0xff);
			personCard2.setDeposit(Double.valueOf(
					(Double.valueOf(taskList.get(1).getBalance()) * 100))
					.intValue());
			personCard2.setRetain(0x00);
			personCard2.setNum(taskList.get(1).getStaff_no());
			personCard2.setName(taskList.get(1).getStaff_name());

			personCardPackDown.getPersonCards()[1] = personCard2;
		}
		if (taskList != null && taskList.size() >= 3) {
			PersonCard personCard3 = new PersonCard();
			personCard3.setCardNum(Long.valueOf(taskList.get(2).getCard_num()));
			personCard3.setPersonNum(Long
					.valueOf(taskList.get(2).getStaff_id()));
			personCard3.setLostFlowNum(00);
			personCard3.setFlag(0xff);
			personCard3.setDeposit(Double.valueOf(
					(Double.valueOf(taskList.get(2).getBalance()) * 100))
					.intValue());
			personCard3.setRetain(0x00);
			personCard3.setNum(taskList.get(2).getStaff_no());
			personCard3.setName(taskList.get(2).getStaff_name());

			personCardPackDown.getPersonCards()[2] = personCard3;
		}
		if (taskList != null && taskList.size() >= 4) {
			PersonCard personCard4 = new PersonCard();
			personCard4.setCardNum(Long.valueOf(taskList.get(3).getCard_num()));
			personCard4.setPersonNum(Long
					.valueOf(taskList.get(3).getStaff_id()));
			personCard4.setLostFlowNum(00);
			personCard4.setFlag(0xff);
			personCard4.setDeposit(Double.valueOf(
					(Double.valueOf(taskList.get(3).getBalance()) * 100))
					.intValue());
			personCard4.setRetain(0x00);
			personCard4.setNum(taskList.get(3).getStaff_no());
			personCard4.setName(taskList.get(3).getStaff_name());

			personCardPackDown.getPersonCards()[3] = personCard4;
		}

		if (taskList != null && taskList.size() >= 5) {
			PersonCard personCard5 = new PersonCard();
			personCard5.setCardNum(Long.valueOf(taskList.get(4).getCard_num()));
			personCard5.setPersonNum(Long
					.valueOf(taskList.get(4).getStaff_id()));
			personCard5.setLostFlowNum(00);
			personCard5.setFlag(0xff);
			personCard5.setDeposit(Double.valueOf(
					(Double.valueOf(taskList.get(4).getBalance()) * 100))
					.intValue());
			personCard5.setRetain(0x00);
			personCard5.setNum(taskList.get(4).getStaff_no());
			personCard5.setName(taskList.get(4).getStaff_name());

			personCardPackDown.getPersonCards()[4] = personCard5;
		}
		if (taskList != null && taskList.size() >= 6) {
			PersonCard personCard6 = new PersonCard();
			personCard6.setCardNum(Long.valueOf(taskList.get(5).getCard_num()));
			personCard6.setPersonNum(Long
					.valueOf(taskList.get(5).getStaff_id()));
			personCard6.setLostFlowNum(00);
			personCard6.setFlag(0xff);
			personCard6.setDeposit(Double.valueOf(
					(Double.valueOf(taskList.get(5).getBalance()) * 100))
					.intValue());
			personCard6.setRetain(0x00);
			personCard6.setNum(taskList.get(5).getStaff_no());
			personCard6.setName(taskList.get(5).getStaff_name());

			personCardPackDown.getPersonCards()[5] = personCard6;
		}
		if (taskList != null && taskList.size() >= 7) {
			PersonCard personCard7 = new PersonCard();
			personCard7.setCardNum(Long.valueOf(taskList.get(6).getCard_num()));
			personCard7.setPersonNum(Long
					.valueOf(taskList.get(6).getStaff_id()));
			personCard7.setLostFlowNum(00);
			personCard7.setFlag(0xff);
			personCard7.setDeposit(Double.valueOf(
					(Double.valueOf(taskList.get(6).getBalance()) * 100))
					.intValue());
			personCard7.setRetain(0x00);
			personCard7.setNum(taskList.get(6).getStaff_no());
			personCard7.setName(taskList.get(6).getStaff_name());

			personCardPackDown.getPersonCards()[6] = personCard7;
		}

		if (taskList != null && taskList.size() >= 8) {
			PersonCard personCard8 = new PersonCard();
			personCard8.setCardNum(Long.valueOf(taskList.get(7).getCard_num()));
			personCard8.setPersonNum(Long
					.valueOf(taskList.get(7).getStaff_id()));
			personCard8.setLostFlowNum(00);
			personCard8.setFlag(0xff);
			personCard8.setDeposit(Double.valueOf(
					(Double.valueOf(taskList.get(7).getBalance()) * 100))
					.intValue());
			personCard8.setRetain(0x00);
			personCard8.setNum(taskList.get(7).getStaff_no());
			personCard8.setName(taskList.get(7).getStaff_name());

			personCardPackDown.getPersonCards()[7] = personCard8;
		}

		if (taskList == null || taskList.size() < 8) {
			// 将设备改为空闲状态
			ResultMsg2 resultMsg2 = new ResultMsg2();
			try {
				String consumUrl2 = PropertiesToMap.propertyToMap(
						"photoncard_interface.properties").get("url")
						+ "interface/deviceState/modifyBusyState.do";

				String str2 = HttpRequest.sendPost(consumUrl2, "deviceNum="
						+ personCardPackUp.getMachine() + "&busyState=0");// 调用一卡通的方法将设备改为空闲状态
				resultMsg2 = GsonUtil.toBean(str2, ResultMsg2.class);
				LOG.info("+++++++++++++++" + resultMsg2.getMsg());
			} catch (Exception e) {
				LOG.info("+++++++++++++++" + resultMsg2.getMsg());
				e.printStackTrace();
			}
		}
		
		// ==============查询出名单准备下发==============

		ByteBuf reByteBuf = Unpooled.buffer(262);
		// 首
		reByteBuf.writeShort(0x0504);
		// 机号2字节
		reByteBuf.writeShort(personCardPackUp.getMachine());

		for (int i = 0; i < personCardPackDown.getPersonCards().length; i++) {
			PersonCard pc = personCardPackDown.getPersonCards()[i];
			if (null == pc) {
				for (int j = 0; j < 32; j++) {
					reByteBuf.writeByte(0xff);
				}
			} else {
				reByteBuf.writeInt(Long.valueOf(pc.getCardNum()).intValue());
				reByteBuf.writeInt(Long.valueOf(pc.getPersonNum()).intValue());
				reByteBuf.writeByte(pc.getLostFlowNum());
				reByteBuf.writeByte(pc.getFlag());
				reByteBuf.writeMedium(pc.getDeposit());
				reByteBuf.writeByte(pc.getRetain());
				String num = pc.getNum();
				byte[] numBytes10 = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
						(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
						(byte) 0x00, (byte) 0x00, (byte) 0x00 };
				byte[] numBytes = num.getBytes();
				for (int k = 0; k < (numBytes.length > 10 ? 10
						: numBytes.length); k++) {
					numBytes10[k] = numBytes[k];
				}
				reByteBuf.writeBytes(numBytes10);

				String name = pc.getName();
				byte[] nameBytes8 = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
						(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
						(byte) 0x00 };
				byte[] nameBytes = name.getBytes();
				for (int k = 0; k < (nameBytes.length > 8 ? 8
						: nameBytes.length); k++) {
					nameBytes8[k] = nameBytes[k];
				}
				reByteBuf.writeBytes(nameBytes8);
			}
		}

		byte[] bytes = new byte[reByteBuf.readableBytes()];
		reByteBuf.getBytes(0, bytes);
		// 计算校验和
		int calCrc = Util.getCrc16(260, bytes);
		// 校验和
		reByteBuf.writeByte(calCrc & 0x00ff);

		reByteBuf.writeByte(calCrc >> 8);

		// reByteBuf.writeByte(0x00);

		// reByteBuf.writeByte(0x00);
		ctx.writeAndFlush(reByteBuf);

	}

	// 下发名单设备返回
	public void recordSinglePersonDown(SinglePersonCardUp singlePersonCardUp,
			ChannelHandlerContext ctx) {
		// 调用一卡通接口将名单删除并且插入kc_xf_person_card_task_history表中
		ResultMsg2 resultMsg2 = new ResultMsg2();
		try {

			PersonCardTask personCardTask = SendPersonDownThread.currentPersonCardTask;

			/*
			 * 设备延时上报可能超过设定的15秒，
			 * 当超过15秒后再上报的singlePersonCardUp记录就和SendPersonDownThread
			 * .currentPersonCardTask获取的记录不是指同一条记录了
			 * 假如设备过了45秒才上报记录1，这时候当前任务记录可能已经是记录5了
			 * ，这样的话我们就要做一个判断，假如上报的记录和当前的任务记录卡号，机号，余额是一模一样的，则暂且认为是同一条，如果卡号，机号相同
			 * ，而余额不同，则记录1
			 * 对应的卡号和机号相同的那条记录，可能余额被更新过一次，这时虽然记录1下发且返回了，但是任务表中的相应记录又被更新了
			 * ，所以我们认为这条更新的记录是下发失败的，需要重新下发
			 */
			if (singlePersonCardUp.getCardNum() == Long.valueOf(personCardTask
					.getCard_num())
					&& singlePersonCardUp.getMachine() == Long
							.valueOf(personCardTask.getDevice_num())
					&& singlePersonCardUp.getDeposit() == Double.valueOf(
							personCardTask.getBalance()).intValue()) {
				/*
				 * System.out.println(personCardTask.getTask_id() +
				 * "----------------2----======" + new Date());
				 */
				personCardTask.setSuccess_flag(0);
				String consumUrl2 = PropertiesToMap.propertyToMap(
						"photoncard_interface.properties").get("url")
						+ "interface/cDevice/insertPersonCardTaskHistoryList.do";
				String str2 = HttpRequest.sendPost(consumUrl2, "data="
						+ GsonUtil.toJson(personCardTask));
				resultMsg2 = GsonUtil.toBean(str2, ResultMsg2.class);

				SendPersonDownThread.semaphore.release();
				if (SendPersonDownThread.LockFlag) {// 当标志解开时，才释放第二个锁
					SendPersonDownThread.semaphore2.release();
				}
				/*
				 * System.out.println(personCardTask.getTask_id() +
				 * "------------3--------======" + new Date());
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
