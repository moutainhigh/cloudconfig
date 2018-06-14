package com.kuangchi.sdd.comm.equipment.base.dao;

public class CardRecordModel {
private int gateid;
private int cardid;
private String recordtime;
private int mac;
private long recordid;
public int getGateid() {
	return gateid;
}
public void setGateid(int gateid) {
	this.gateid = gateid;
}
public int getCardid() {
	return cardid;
}
public void setCardid(int cardid) {
	this.cardid = cardid;
}

public int getMac() {
	return mac;
}
public void setMac(int mac) {
	this.mac = mac;
}
public String getRecordtime() {
	return recordtime;
}
public void setRecordtime(String recordtime) {
	this.recordtime = recordtime;
}
public long getRecordid() {
	return recordid;
}
public void setRecordid(long recordid) {
	this.recordid = recordid;
}


}
