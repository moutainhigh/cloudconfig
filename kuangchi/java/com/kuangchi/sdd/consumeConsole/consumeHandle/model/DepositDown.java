package com.kuangchi.sdd.consumeConsole.consumeHandle.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-24 下午5:23:20
 * @功能描述: 消费余额下发包
 */
public class DepositDown {
    long cardNum;//卡号
    int alm;//报警
    Double balance;//余额
    String password;//消费密码
    int limit;//限额
    String num;//编号 工号
    String name ;//姓名
    int header;
    int machine;
    int crc;
    int tail;
   
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
	public long getCardNum() {
		return cardNum;
	}
	public void setCardNum(long cardNum) {
		this.cardNum = cardNum;
	}
	public int getAlm() {
		return alm;
	}
	public void setAlm(int alm) {
		this.alm = alm;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

   
   
	
	
}
