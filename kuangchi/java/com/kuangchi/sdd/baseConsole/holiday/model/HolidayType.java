package com.kuangchi.sdd.baseConsole.holiday.model;

import java.io.Serializable;

import com.kuangchi.sdd.baseConsole.card.model.BaseModelSupport;

/**
 * @创建人　: 杨金林
 * @创建时间: 2016-3-24 15:54:51
 * @功能描述: 节假日类型信息Bean
 */
public class HolidayType extends BaseModelSupport implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer holiday_type_id;//节假日类型Id
	private String holiday_type_num;//节假日类型编号
	private String holiday_name;//节假日类型名称
	private String holiday_remark;//节假日类型备注
	private String holiday_validity;//有效期
	private String create_time;//创建时间
	
	public Integer getHoliday_type_id() {
		return holiday_type_id;
	}
	public void setHoliday_type_id(Integer holiday_type_id) {
		this.holiday_type_id = holiday_type_id;
	}
	public String getHoliday_type_num() {
		return holiday_type_num;
	}
	public void setHoliday_type_num(String holiday_type_num) {
		this.holiday_type_num = holiday_type_num;
	}
	public String getHoliday_name() {
		return holiday_name;
	}
	public void setHoliday_name(String holiday_name) {
		this.holiday_name = holiday_name;
	}
	public String getHoliday_remark() {
		return holiday_remark;
	}
	public void setHoliday_remark(String holiday_remark) {
		this.holiday_remark = holiday_remark;
	}
	public String getHoliday_validity() {
		return holiday_validity;
	}
	public void setHoliday_validity(String holiday_validity) {
		this.holiday_validity = holiday_validity;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	@Override
	public String toString() {
		return "HolidayType [holiday_type_id=" + holiday_type_id
				+ ", holiday_type_num=" + holiday_type_num + ", holiday_name="
				+ holiday_name + ", holiday_remark=" + holiday_remark
				+ ", holiday_validity=" + holiday_validity + ", create_time="
				+ create_time + "]";
	}
	
}
