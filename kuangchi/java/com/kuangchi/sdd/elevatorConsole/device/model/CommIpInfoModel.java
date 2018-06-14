package com.kuangchi.sdd.elevatorConsole.device.model;

/*
 * @创建人　: 邓积辉
 * @创建时间: 2016-12-5 上午10:26:54
 * @功能描述:	通讯服务器ipModel
 */
public class CommIpInfoModel {
	private Integer id;
	private String ip_address;
	private String comm_port;
	private String description;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public String getComm_port() {
		return comm_port;
	}
	public void setComm_port(String comm_port) {
		this.comm_port = comm_port;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
