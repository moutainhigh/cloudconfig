package com.kuangchi.sdd.consum.bean;

public class DepositDown extends SendHeader{
   long cardNum;//卡号
   int alm;//报警
   int balance;//余额
   String password;//消费密码
   int limit;//限额
   String num;//编号 工号
   String name ;//姓名
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
public int getBalance() {
	return balance;
}
public void setBalance(int balance) {
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
