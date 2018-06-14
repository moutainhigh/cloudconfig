package com.kuangchi.sdd.zigbee.model;

public class Data {
   int header;
   int cmd;
   int length;
   int crc;
public int getHeader() {
	return header;
}
public void setHeader(int header) {
	this.header = header;
}
public int getCmd() {
	return cmd;
}
public void setCmd(int cmd) {
	this.cmd = cmd;
}
public int getLength() {
	return length;
}
public void setLength(int length) {
	this.length = length;
}
public int getCrc() {
	return crc;
}
public void setCrc(int crc) {
	this.crc = crc;
}
   
}
