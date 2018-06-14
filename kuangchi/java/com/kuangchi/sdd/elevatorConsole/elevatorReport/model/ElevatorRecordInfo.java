package com.kuangchi.sdd.elevatorConsole.elevatorReport.model;

public class ElevatorRecordInfo {
	private String device_name;//设备名称
	private String device_num;//设备编号
	private String device_ip;//设备ip
	private String mac;//设备Mac地址
	private String card_num;//卡号
	private String staff_name;//员工名称
	private String staff_no;//员工工号
	private String check_time;//刷卡时间
	private String card_type_name;//卡片类型
	private String floor_num;//楼层编号
	private String floor_state;//楼层状态
	private String event_type;//事件类型
	private String check_type;//记录标记
	private String begin_time;//开始时间
	private String end_time;//结束时间
	private String type_name;//卡类型
	private String floor_name;//楼层名称
	private String BM_MC;
	
	
	public String getBM_MC() {
		return BM_MC;
	}
	public void setBM_MC(String bM_MC) {
		BM_MC = bM_MC;
	}
	public String getFloor_name() {
		return floor_name;
	}
	public void setFloor_name(String floor_name) {
		this.floor_name = floor_name;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
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
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getCheck_time() {
		return check_time;
	}
	public void setCheck_time(String check_time) {
		this.check_time = check_time;
	}
	public String getCard_type_name() {
		return card_type_name;
	}
	public void setCard_type_name(String card_type_name) {
		this.card_type_name = card_type_name;
	}
	public String getFloor_num() {
		return floor_num;
	}
	public void setFloor_num(String floor_num) {
		this.floor_num = floor_num;
	}
	public String getFloor_state() {
		return floor_state;
	}
	public void setFloor_state(String floor_state) {
		this.floor_state = floor_state;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public String getCheck_type() {
		return check_type;
	}
	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}
	
	
}
