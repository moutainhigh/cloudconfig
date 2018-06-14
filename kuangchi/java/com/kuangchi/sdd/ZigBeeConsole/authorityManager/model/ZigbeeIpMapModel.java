package com.kuangchi.sdd.ZigBeeConsole.authorityManager.model;


/**
 * @创建人　: chudan.guo
 * @创建时间: 2016-10-18
 * @功能描述: 光子锁设备IP和网关-model
 */
public class ZigbeeIpMapModel {
	private String remote_ip;//通讯服务器IP
	private String remote_port;//通讯服务器端口
	private String gateway;//网关
	private String gateway_port;//网关端口
	private String zigbee_id;//ZigBee网关芯片ID
	public String getRemote_ip() {
		return remote_ip;
	}
	public void setRemote_ip(String remote_ip) {
		this.remote_ip = remote_ip;
	}
	public String getRemote_port() {
		return remote_port;
	}
	public void setRemote_port(String remote_port) {
		this.remote_port = remote_port;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getGateway_port() {
		return gateway_port;
	}
	public void setGateway_port(String gateway_port) {
		this.gateway_port = gateway_port;
	}
	public String getZigbee_id() {
		return zigbee_id;
	}
	public void setZigbee_id(String zigbee_id) {
		this.zigbee_id = zigbee_id;
	}
	
	
}