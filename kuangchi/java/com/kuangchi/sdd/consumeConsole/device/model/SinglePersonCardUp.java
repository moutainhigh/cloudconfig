package com.kuangchi.sdd.consumeConsole.device.model;

public class SinglePersonCardUp extends ReceiveHeader {
	long cardNum;// 卡号 4 字节
	long personNum;// 人员ID 4 字节(自增长键)
	int lostFlowNum;// 挂失流水号 IC卡挂失流水号，ID只读模式下无作用，填写0x00 1 字节
	int flag; // 名单标志 0xFF表示名单有效，0x00表示名单已删除 1 字节
	int deposit;// 卡片余额 3字节
	int retain; // 预留字节 1 字节
	String num;// 编号(员工工号) 10 字节
	String name;// 姓名 8 字节

	public long getCardNum() {
		return cardNum;
	}

	public void setCardNum(long cardNum) {
		this.cardNum = cardNum;
	}

	public long getPersonNum() {
		return personNum;
	}

	public void setPersonNum(long personNum) {
		this.personNum = personNum;
	}

	public int getLostFlowNum() {
		return lostFlowNum;
	}

	public void setLostFlowNum(int lostFlowNum) {
		this.lostFlowNum = lostFlowNum;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	public int getRetain() {
		return retain;
	}

	public void setRetain(int retain) {
		this.retain = retain;
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
