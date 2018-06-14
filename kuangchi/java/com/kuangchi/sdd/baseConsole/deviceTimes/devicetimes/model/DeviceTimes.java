package com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-4-5 下午5:12:10
 * @功能描述: 时段信息Bean
 */
public class DeviceTimes {
	
	private Integer id;
	private String times_num;
	private String begin_time;
	private String end_time;
	private Integer eare_num;
	private String device_num;
	private String device_mac;
	private String result_code;
	private String device_type;
	
	
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public Integer getId() {
		return id;
	}
	
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTimes_num() {
		return times_num;
	}
	public void setTimes_num(String times_num) {
		this.times_num = times_num;
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
	
	public Integer getEare_num() {
		return eare_num;
	}
	public void setEare_num(Integer eare_num) {
		this.eare_num = eare_num;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	
	
}
