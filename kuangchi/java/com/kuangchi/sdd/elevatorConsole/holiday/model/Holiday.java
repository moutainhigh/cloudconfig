package com.kuangchi.sdd.elevatorConsole.holiday.model;

/**
 * 梯控节假日Bean
 * 
 * @author yuman.gao
 */
public class Holiday {
	private Integer id;// 节假日ID
	private String device_num; // 设备编号

	public String getSend_state() {
		return send_state;
	}

	public void setSend_state(String send_state) {
		this.send_state = send_state;
	}

	private String holiday_num;// 节假日编号
	private String holiday_date;// 节假日日期
	private String send_state;// 下发标志 0 已下发 1未下发
	private String description;// 描述
	private String valid_date;// 有效状态

	public String getValid_date() {
		return valid_date;
	}

	public void setValid_date(String valid_date) {
		this.valid_date = valid_date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDevice_num() {
		return device_num;
	}

	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}

	public String getHoliday_num() {
		return holiday_num;
	}

	public void setHoliday_num(String holiday_num) {
		this.holiday_num = holiday_num;
	}

	public String getHoliday_date() {
		return holiday_date;
	}

	public void setHoliday_date(String holiday_date) {
		this.holiday_date = holiday_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
