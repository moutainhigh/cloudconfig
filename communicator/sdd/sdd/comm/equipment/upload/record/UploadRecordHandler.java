package com.kuangchi.sdd.comm.equipment.upload.record;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.dao.ICardRecord;
import com.kuangchi.sdd.comm.equipment.common.CardRecordData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.commonUtil.StringUtil;
import com.kuangchi.sdd.util.network.HttpRequest;
/**
 *上传刷卡事件记录，门禁事件记录等
 * */
public class UploadRecordHandler extends
		BaseHandler<ByteBuf, GateTimeInterface, ReceiveData> {
	
	public static final Logger LOG = Logger.getLogger(UploadRecordHandler.class);
	@Resource(name = "cardRecordImpl")
	private ICardRecord icardrecord;
	ReceiveHeader receiveHeader;

	public ReceiveHeader getReceiveHeader() {
		return receiveHeader;
	}

	public void setReceiveHeader(ReceiveHeader receiveHeader) {
		this.receiveHeader = receiveHeader;
	}

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
		// 设置有效数据
		ByteBuf dataBuf = receiveBuffer.readBytes(17);// 取17字节有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;

		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		receiveHeader = receiver;
		LOG.info(receiver.getCrcFromSum());

		String returnValue = GsonUtil.toJson(data.getCardRecord());
		if (receiver.getCrcFromSum() == checkSum) {
			LOG.info("数据校验成功！");
			returnValue = GsonUtil.toJson(data.getCardRecord());
		}
		LOG.info("客户端接收的十六进制信息:"
				+ Util.lpad(Integer.toHexString(header), 2)
				+ "|"
				+ Util.lpad(Integer.toHexString(sign), 2)
				+ "|"
				// + Integer.toHexString(macAddress)
				+ Util.lpad(Integer.toHexString(mac), 6) + "|"
				+ Util.lpad(Integer.toHexString(order), 2) + "|"
				+ Util.lpad(Integer.toHexString(length), 3) + "|"
				+ Util.lpad(Integer.toHexString(orderStatus), 2) + "|"
				+ checkSum + "|");

		int gateId = data.getCardRecord().getGateId();// 门号
		long cardId = data.getCardRecord().getCardId();// 卡号

		String year = "20" + (data.getCardRecord().getTime().getYear() >> 4)
				+ "" + (0xf & data.getCardRecord().getTime().getYear());

		String month = (data.getCardRecord().getTime().getMonth() >> 4) + ""
				+ (0xf & data.getCardRecord().getTime().getMonth());
		String day = (data.getCardRecord().getTime().getDay() >> 4) + ""
				+ (0xf & data.getCardRecord().getTime().getDay());
		String hour = (data.getCardRecord().getTime().getHour() >> 4) + ""
				+ (0xf & data.getCardRecord().getTime().getHour());
		String minute = (data.getCardRecord().getTime().getMinute() >> 4) + ""
				+ (0xf & data.getCardRecord().getTime().getMinute());
		String second = (data.getCardRecord().getTime().getSecond() >> 4) + ""
				+ (0xf & data.getCardRecord().getTime().getSecond());

		String recordTime = year + "-" + month + "-" + day + " " + hour + ":"
				+ minute + ":" + second;
		LOG.info(recordTime);
		long recordId = data.getCardRecord().getRecordId();// 记录id

		String cardIdStr = Long.toHexString((cardId << 8)
				| data.getCardRecord().getHighBit());

		Long cardIdLong = Util.getLongHex(cardIdStr);
		int recordType = data.getCardRecord().getRecordType();
		int eventType = data.getCardRecord().getEventType();
		String eventTypeStr = Integer.toHexString(eventType).toUpperCase();
		String gateIdStr = "1";
		if ((gateId & 1) > 0) {
			gateIdStr = "1";
		} else if ((gateId & 2) > 0) {
			gateIdStr = "2";
		} else if ((gateId & 4) > 0) {
			gateIdStr = "3";
		} else if ((gateId & 8) > 0) {
			gateIdStr = "4";
		}
		addCardRecordToCheck(gateIdStr, cardIdLong, mac, recordTime, recordId,
				recordType, eventTypeStr);

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
		CardRecordData gateData = new CardRecordData();

		/**
		 * 0x00：门禁记录； 0x01：巡更记录； 0x02：刷卡记录。
		 */
		int recordType = dataBuf.readUnsignedByte();
		gateData.setRecordType(recordType);
		LOG.info("recordType=" + recordType);
		/**
		 * 读取记录号
		 */
		long recordId = dataBuf.readUnsignedInt();
		gateData.setRecordId(recordId);
		LOG.info("recordId=" + recordId);
		/**
		 * 读取卡号
		 */
		int cardId = dataBuf.readUnsignedMedium();
		gateData.setCardId(cardId);
		LOG.info("cardId=" + cardId);
		/**
		 * 读取事件类型
		 */
		int eventType = dataBuf.readUnsignedByte();
		gateData.setEventType(eventType);
		LOG.info("eventType=" + eventType);
		/**
		 * 读取时间
		 */
		TimeData time = new TimeData();
		time.setYear(dataBuf.readUnsignedByte());
		time.setMonth(dataBuf.readUnsignedByte());
		time.setDay(dataBuf.readUnsignedByte());
		time.setHour(dataBuf.readUnsignedByte());
		time.setMinute(dataBuf.readUnsignedByte());
		time.setSecond(dataBuf.readUnsignedByte());
		gateData.setTime(time);
		/**
		 * 读取门号
		 */
		int gateId = dataBuf.readUnsignedByte();
		gateData.setGateId(gateId);
		LOG.info("gateId=" + gateId);
		/**
		 * 读取卡号最高位
		 */
		int highBit = dataBuf.readUnsignedByte();
		gateData.setHighBit(highBit);

		data.setCardRecord(gateData);
		return data;
	}

	static Map<String, String> urlMap = new HashMap<String, String>();

	// 刷卡记录信息上报
	public void addCardRecordToCheck(String gateId, long cardId, int mac,
			String recordTime, long recordId, int recordType, String eventType) {

		if (urlMap.isEmpty()) {
			urlMap = PropertiesToMap
					.propertyToMap("photoncard_interface.properties");
		}
		String cardRecordReportUrl = urlMap.get("url");
		String str = HttpRequest.sendPost(cardRecordReportUrl
				+ "interface/checkHandle/addCheckRecord.do?",
				"door_num=" + gateId + "&card_num=" + cardId + "&device_mac="
						+ StringUtil.to_MJDevice_Mac(Integer.toHexString(mac).toUpperCase(Locale.ENGLISH))
						+ "&checktime=" + recordTime + "&record_id=" + recordId
						+ "&record_type=" + recordType + "&event_type="
						+ eventType);
		LOG.info(GsonUtil.toJson(str));

	}

	@Override
	protected GateTimeInterface getDataForServer(ReceiveData data) {
		// TODO Auto-generated method stub
		return null;
	}

	// addCardRecordToCheck调用门禁系统刷卡记录信息接口方法的测试。测试成功的返回结果是：{"isSuccess":true,"msg":"增加打卡信息成功"}
	public static void main(String[] args) {
		// String
		// url="http://192.168.210.89:8089/photoncard/checkHandle/addCheckRecord.do";
		// String
		// param="door_num=1&card_num=10280&device_mac=123&checktime=2016-05-30 18:38:40&record_id=12";
		// String str = HttpRequest.sendPost(url,param);
		// System.out.println(str);
		/*
		 * UploadRecordHandler u=new UploadRecordHandler();
		 * u.addCardRecordToCheck(1, 10282, 123, "2016-05-20 09:46:30", 12);
		 */
		//System.out.println(Integer.toHexString(8716298).toUpperCase(Locale.ENGLISH));
//		Long val = Long.parseLong("FFFFFFFF", 16);
//		LOG.info(val.intValue());

	}

}
