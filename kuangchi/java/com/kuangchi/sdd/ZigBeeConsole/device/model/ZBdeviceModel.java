package com.kuangchi.sdd.ZigBeeConsole.device.model;


/**
 * @创建人　: 陈桂波
 * @创建时间: 2016-10-19 上午9:34:04
 * @功能描述: 光子锁记录-model
 */
public class ZBdeviceModel {

	//网关信息表
	private int gateway_id; 
	private String remote_ip; //通讯服务器ip
	private String remote_port;//通讯服务器端口
	private String gateway;//网关
	private String gateway_port; //网关端口
	private String state;//网关在线状态
	private String flag; //标志位
	
	
	//设备信息表
	private String device_id;//设备ID
	private String room_num;//房间号
	private String gateway_pan_id;//ZigBee//网关 pan_id
	private String electricity;//电池电量（如格式：5.1，表示电池电压5.1伏）
	private String device_signal;//信号强度
	private String time_stamp;//时间戳
	private String device_state;//设备状态
	private String create_date;//创建时间
	private String description;//描述
	private String zigbee_id;//ZigBee网关芯片ID
	private String device_flag;//标识（默认0 1有效 2无效）
	private int community_num; //小区号
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
	public int getCommunity_num() {
		return community_num;
	}
	public void setCommunity_num(int community_num) {
		this.community_num = community_num;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDevice_signal() {
		return device_signal;
	}
	public void setDevice_signal(String device_signal) {
		this.device_signal = device_signal;
	}
	public int getGateway_id() {
		return gateway_id;
	}
	public void setGateway_id(int gateway_id) {
		this.gateway_id = gateway_id;
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
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getRoom_num() {
		return room_num;
	}
	public void setRoom_num(String room_num) {
		this.room_num = room_num;
	}
	
	public String getGateway_pan_id() {
		return gateway_pan_id;
	}
	public void setGateway_pan_id(String gateway_pan_id) {
		this.gateway_pan_id = gateway_pan_id;
	}
	public String getElectricity() {
		return electricity;
	}
	public void setElectricity(String electricity) {
		this.electricity = electricity;
	}
	
	public String getTime_stamp() {
		return time_stamp;
	}
	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}
	public String getDevice_state() {
		return device_state;
	}
	public void setDevice_state(String device_state) {
		this.device_state = device_state;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getZigbee_id() {
		return zigbee_id;
	}
	public void setZigbee_id(String zigbee_id) {
		this.zigbee_id = zigbee_id;
	}
	public String getDevice_flag() {
		return device_flag;
	}
	public void setDevice_flag(String device_flag) {
		this.device_flag = device_flag;
	}
	
	
	
}
