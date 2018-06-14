package com.kuangchi.sdd.elevatorConsole.device.model;


public class CommDevice {
     String address;
     String mac;
     String serialNo;
     String ip;
     String port;
     String subnet="255.255.255.0";
     String gateway;
     int commType;   //通信方式  0  tcp  1 串口
     String password="000000";
     String comm="";   //串口号
     
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getSubnet() {
		return subnet;
	}
	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public int getCommType() {
		return commType;
	}
	public void setCommType(int commType) {
		this.commType = commType;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getComm() {
		return comm;
	}
	public void setComm(String comm) {
		this.comm = comm;
	}

     
	
	
}
