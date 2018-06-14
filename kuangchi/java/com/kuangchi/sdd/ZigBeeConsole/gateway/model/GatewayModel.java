package com.kuangchi.sdd.ZigBeeConsole.gateway.model;


/**
 * @创建人　: 陈桂波
 * @创建时间: 2016-10-19 上午9:34:04
 * @功能描述: 光子锁记录-model
 */
public class GatewayModel {

	//网关信息表
	private int gateway_id; 
	private String old_remote_ip;
	private String remote_ip; //通讯服务器ip
	private String remote_port;//通讯服务器端口
	private String gateway;//网关
	private String old_gateway;
	private String gateway_port; //网关端口
	private String zigbee_id;//网关id
	private String state;//在线状态
	private String flag; //标志位
	private String gateway_pan_id;
	private String description;
	private String old_gateway_pan_id;//原有的panid
	
	private String softwareVersion; //软件版本号
	private String firmwareVersion; // 固件版本号
	
	
	
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public String getFirmwareVersion() {
		return firmwareVersion;
	}
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	public String getOld_gateway_pan_id() {
		return old_gateway_pan_id;
	}
	public void setOld_gateway_pan_id(String old_gateway_pan_id) {
		this.old_gateway_pan_id = old_gateway_pan_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getGateway_id() {
		return gateway_id;
	}
	public void setGateway_id(int gateway_id) {
		this.gateway_id = gateway_id;
	}
	
	
	public String getOld_remote_ip() {
		return old_remote_ip;
	}
	public void setOld_remote_ip(String old_remote_ip) {
		this.old_remote_ip = old_remote_ip;
	}
	public String getOld_gateway() {
		return old_gateway;
	}
	public void setOld_gateway(String old_gateway) {
		this.old_gateway = old_gateway;
	}
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getZigbee_id() {
		return zigbee_id;
	}
	public void setZigbee_id(String zigbee_id) {
		this.zigbee_id = zigbee_id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getGateway_pan_id() {
		return gateway_pan_id;
	}
	public void setGateway_pan_id(String gateway_pan_id) {
		this.gateway_pan_id = gateway_pan_id;
	}
	
	
	
}
