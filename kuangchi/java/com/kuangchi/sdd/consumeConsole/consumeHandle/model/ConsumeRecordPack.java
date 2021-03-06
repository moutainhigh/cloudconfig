package com.kuangchi.sdd.consumeConsole.consumeHandle.model;



public class ConsumeRecordPack{
	
	int header;
    int machine;
    int crc;
    int tail;
	int recordNum;
    int recordType;
    long cardNum;
    Double  balance;//余额
    Double  consumeSum;//消费金额
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
    int cardFlowNum;
    byte[] retain=new byte[14];
    
 
	public int getHeader() {
		return header;
	}
	public void setHeader(int header) {
		this.header = header;
	}
	public int getMachine() {
		return machine;
	}
	public void setMachine(int machine) {
		this.machine = machine;
	}
	public int getCrc() {
		return crc;
	}
	public void setCrc(int crc) {
		this.crc = crc;
	}
	public int getTail() {
		return tail;
	}
	public void setTail(int tail) {
		this.tail = tail;
	}
	public int getRecordNum() {
		return recordNum;
	}
	public void setRecordNum(int recordNum) {
		this.recordNum = recordNum;
	}
	public int getRecordType() {
		return recordType;
	}
	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}
	public long getCardNum() {
		return cardNum;
	}
	public void setCardNum(long cardNum) {
		this.cardNum = cardNum;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getConsumeSum() {
		return consumeSum;
	}
	public void setConsumeSum(Double consumeSum) {
		this.consumeSum = consumeSum;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public int getCardFlowNum() {
		return cardFlowNum;
	}
	public void setCardFlowNum(int cardFlowNum) {
		this.cardFlowNum = cardFlowNum;
	}
	public byte[] getRetain() {
		return retain;
	}
	public void setRetain(byte[] retain) {
		this.retain = retain;
	}

    
	
	   
}


