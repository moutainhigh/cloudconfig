package com.kuangchi.sdd.elevatorConsole.elevatorReport.model;

public class CheckEvent {
	String recordId;
    String recordTime;
    String dayInWeek;//星期几   1,2,3,4,5,6,7,  星期一---星期日
    String recordType;
    String cardId;
    String recordStatus;
    String recordCount;
    String cardType;
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getDayInWeek() {
		return dayInWeek;
	}
	public void setDayInWeek(String dayInWeek) {
		this.dayInWeek = dayInWeek;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public String getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	 
	
}
