package com.kuangchi.sdd.comm.equipment.common;

/**
 * 存放刷卡记录信息（上传门禁出入记录接口调用）
 * 
 * @author 姚宇
 * 
 */
public class CardRecordData {
	private long recordId;// 记录编号
	private int cardId;// 卡号
	private int eventType;// 事件类型
	private TimeData time;// 刷卡时间
	private int gateId;// 门号
	private int highBit;// 卡号最高位
	private int recordType;//记录类型
    //计算校验和
	public long getCrcFromSum() {
		// 计算记录编号校验和
		long recordIdSum = 0;
		long r1, r2, r3, r4;
		r1 = (recordId & 0xFF000000) >> 24;
		r2 = (recordId & 0x00FF0000) >> 16;
		r3 = (recordId & 0x000000FF) >> 8;
		r4 = (recordId & 0x000000FF);
		recordIdSum += (r1 + r2 + r3 + r4);
		// 计算卡号校验和
		long cardIdSum = 0;
		long c1, c2, c3;
		c1 = (cardId & 0xFF0000) >> 16;
		c2 = (cardId & 0x0000FF) >> 8;
		c3 = (cardId & 0x0000FF);
		cardIdSum += (c1 + c2 + c3);
		// 时间校验和
		long timeSum = (time == null ? 0 : time.getCrcFromSum());
		//汇总校验和
		long result = recordIdSum + cardIdSum + eventType + timeSum + gateId
				+ highBit+recordType;
		return result;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public TimeData getTime() {
		return time;
	}

	public void setTime(TimeData time) {
		this.time = time;
	}

	public int getGateId() {
		return gateId;
	}

	public void setGateId(int gateId) {
		this.gateId = gateId;
	}

	public int getHighBit() {
		return highBit;
	}

	public void setHighBit(int highBit) {
		this.highBit = highBit;
	}

	public int getRecordType() {
		return recordType;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}
	
	
}
