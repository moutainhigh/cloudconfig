package com.kuangchi.sdd.elevatorConsole.elevatorReport.model;

public class ElevatorDeviceInfo {
		private String device_num; //设备编号
		private String device_name;//设备名称
		private String device_ip;  //设备IP
		private int device_port;   //设备端口
		private String device_sequence;//设备序列号
		private String password; //密码  （哪里来？）
		
		private String group_name;  //设备组名称  表 kc_device_group
		private String network_mask;//子网掩码
		private String network_gateway;//网关
		private String mac;			   //mac地址
		private String online_state;//设备在线状态 0 不在线 1 在线
		private String create_time; //创建时间
		private String description; //描述
		
		public String getGroup_name() {
			return group_name;
		}
		public void setGroup_name(String group_name) {
			this.group_name = group_name;
		}
		public String getNetwork_mask() {
			return network_mask;
		}
		public void setNetwork_mask(String network_mask) {
			this.network_mask = network_mask;
		}
		public String getNetwork_gateway() {
			return network_gateway;
		}
		public void setNetwork_gateway(String network_gateway) {
			this.network_gateway = network_gateway;
		}
		public String getMac() {
			return mac;
		}
		public void setMac(String mac) {
			this.mac = mac;
		}
		public String getOnline_state() {
			return online_state;
		}
		public void setOnline_state(String online_state) {
			this.online_state = online_state;
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
		public String getDevice_ip() {
			return device_ip;
		}
		public void setDevice_ip(String device_ip) {
			this.device_ip = device_ip;
		}
		public int getDevice_port() {
			return device_port;
		}
		public void setDevice_port(int device_port) {
			this.device_port = device_port;
		}
		public String getDevice_sequence() {
			return device_sequence;
		}
		public void setDevice_sequence(String device_sequence) {
			this.device_sequence = device_sequence;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
}
