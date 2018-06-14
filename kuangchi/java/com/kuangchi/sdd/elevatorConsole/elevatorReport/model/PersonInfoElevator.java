package com.kuangchi.sdd.elevatorConsole.elevatorReport.model;

public class PersonInfoElevator {
		private String device_name;//设备名称
		private String device_ip;//设备IP
		private String mac;//设备mac地址
		private String staff_no; //人员ID(员工工号)
		private String staff_name; //人员姓名（员工名字）
		private String begin_valid_time ; //开始生效时间
		private String end_valid_time; //结束生效时间
		private String device_num; //设备编号
		private String staff_num; //员工编号
		private String card_num; //人员卡号
		//操作时间
		//操作员
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
		public String getMac() {
			return mac;
		}
		public void setMac(String mac) {
			this.mac = mac;
		}
		public String getStaff_no() {
			return staff_no;
		}
		public void setStaff_no(String staff_no) {
			this.staff_no = staff_no;
		}
		public String getStaff_name() {
			return staff_name;
		}
		public void setStaff_name(String staff_name) {
			this.staff_name = staff_name;
		}
		public String getBegin_valid_time() {
			return begin_valid_time;
		}
		public void setBegin_valid_time(String begin_valid_time) {
			this.begin_valid_time = begin_valid_time;
		}
		public String getEnd_valid_time() {
			return end_valid_time;
		}
		public void setEnd_valid_time(String end_valid_time) {
			this.end_valid_time = end_valid_time;
		}
		public String getDevice_num() {
			return device_num;
		}
		public void setDevice_num(String device_num) {
			this.device_num = device_num;
		}
		public String getStaff_num() {
			return staff_num;
		}
		public void setStaff_num(String staff_num) {
			this.staff_num = staff_num;
		}
	
}
