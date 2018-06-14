package com.kuangchi.sdd.baseConsole.device.model;

public class DeviceInfo {
	
	private String device_id;
	private String device_num;
	private String device_group_num; //设备组编号  0表示未分组设备 
	private String device_name;
	private String device_type;
	private String hardware_version;
	private String progarm_version;
	private String subnet_mask;
	private String gateway_param;
	private String device_mac;
	private String local_ip_address;
	private String remote_id_address;
	private String local_state_port;
	private String remote_state_port;
	private String local_order_port;
	private String remote_order_port;
	private String local_event_port;
	private String remote_event_port;
	private String device_time;
	private String validity_flag;
	private Double x_position; // x坐标
	private Double y_position; // y坐标
	private String distribution_pic_id; // 设备分布背景图片
	private String online_state; //设备状态  0在线  1不在线
	private String create_user;
	private String create_time;
	private String description;
	private String cardsign;
	private String comm_ip;
	
	
	
	
	public String getCardsign() {
		return cardsign;
	}
	public void setCardsign(String cardsign) {
		this.cardsign = cardsign;
	}
	public String getOnline_state() {
		return online_state;
	}
	public void setOnline_state(String online_state) {
		this.online_state = online_state;
	}

	public Double getX_position() {
		return x_position;
	}
	public void setX_position(Double x_position) {
		this.x_position = x_position;
	}
	public Double getY_position() {
		return y_position;
	}
	public void setY_position(Double y_position) {
		this.y_position = y_position;
	}
	public String getDistribution_pic_id() {
		return distribution_pic_id;
	}
	public void setDistribution_pic_id(String distribution_pic_id) {
		this.distribution_pic_id = distribution_pic_id;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getHardware_version() {
		return hardware_version;
	}
	public void setHardware_version(String hardware_version) {
		this.hardware_version = hardware_version;
	}
	public String getProgarm_version() {
		return progarm_version;
	}
	public void setProgarm_version(String progarm_version) {
		this.progarm_version = progarm_version;
	}
	public String getSubnet_mask() {
		return subnet_mask;
	}
	public void setSubnet_mask(String subnet_mask) {
		this.subnet_mask = subnet_mask;
	}
	public String getGateway_param() {
		return gateway_param;
	}
	public void setGateway_param(String gateway_param) {
		this.gateway_param = gateway_param;
	}
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	public String getLocal_ip_address() {
		return local_ip_address;
	}
	public void setLocal_ip_address(String local_ip_address) {
		this.local_ip_address = local_ip_address;
	}
	public String getRemote_id_address() {
		return remote_id_address;
	}
	public void setRemote_id_address(String remote_id_address) {
		this.remote_id_address = remote_id_address;
	}
	public String getLocal_state_port() {
		return local_state_port;
	}
	public void setLocal_state_port(String local_state_port) {
		this.local_state_port = local_state_port;
	}
	public String getRemote_state_port() {
		return remote_state_port;
	}
	public void setRemote_state_port(String remote_state_port) {
		this.remote_state_port = remote_state_port;
	}
	public String getLocal_order_port() {
		return local_order_port;
	}
	public void setLocal_order_port(String local_order_port) {
		this.local_order_port = local_order_port;
	}
	public String getRemote_order_port() {
		return remote_order_port;
	}
	public void setRemote_order_port(String remote_order_port) {
		this.remote_order_port = remote_order_port;
	}
	public String getLocal_event_port() {
		return local_event_port;
	}
	public void setLocal_event_port(String local_event_port) {
		this.local_event_port = local_event_port;
	}
	public String getRemote_event_port() {
		return remote_event_port;
	}
	public void setRemote_event_port(String remote_event_port) {
		this.remote_event_port = remote_event_port;
	}
	public String getDevice_time() {
		return device_time;
	}
	public void setDevice_time(String device_time) {
		this.device_time = device_time;
	}
	public String getValidity_flag() {
		return validity_flag;
	}
	public void setValidity_flag(String validity_flag) {
		this.validity_flag = validity_flag;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDevice_group_num() {
		return device_group_num;
	}
	public void setDevice_group_num(String device_group_num) {
		this.device_group_num = device_group_num;
	}
	public String getComm_ip() {
		return comm_ip;
	}
	public void setComm_ip(String comm_ip) {
		this.comm_ip = comm_ip;
	}
	
}
