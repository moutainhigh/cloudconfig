package com.kuangchi.sdd.elevatorConsole.device.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class Holiday extends BaseModelSupport {
	private String device_num;
	private String holiday_num;
	private String holiday_date;
	private String description;
	private String send_state;// 下发标志 0 已下发 1未下发
	private String delete_flag;// 删除标志 0 未删除 1 删除
	private String valid_date;// 有效状态

	public String getValid_date() {
		return valid_date;
	}

	public void setValid_date(String valid_date) {
		this.valid_date = valid_date;
	}

	public String getSend_state() {
		return send_state;
	}

	public void setSend_state(String send_state) {
		this.send_state = send_state;
	}

	public String getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
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
