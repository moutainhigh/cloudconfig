package com.kuangchi.sdd.baseConsole.holiday.model;

import java.io.Serializable;

import com.kuangchi.sdd.baseConsole.card.model.BaseModelSupport;

/**
 * @创建人　: 杨金林
 * @创建时间: 2016-3-24 下午3:21:38
 * @功能描述: 节假日信息Bean
 */
public class Holiday extends BaseModelSupport implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer holiday_id;//节假日ID
	private String holiday_num;//节假日编号
	private String holiday_type_num;//类型编号
	private String holiday_name;//节假日名称
	private String holiday_begin_date;//假期开始时间
	private String holiday_end_date;//假期结束时间
	private String holiday_scope;//作用范围（全部|男|女）
	private String create_time;//创建时间
	private String type; // 类型 	0节假日   1补班
	
	private String dept_num; //作用部门编号
	private String time_period; //时段     1上午   2下午   3全天
	
	
	
	public String getDept_num() {
		return dept_num;
	}

	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}

	public String getTime_period() {
		return time_period;
	}

	public void setTime_period(String time_period) {
		this.time_period = time_period;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Holiday() {
		
	}
	
	public String getHoliday_name() {
		return holiday_name;
	}

	public void setHoliday_name(String holiday_name) {
		this.holiday_name = holiday_name;
	}

	public Integer getHoliday_id() {
		return holiday_id;
	}
	public void setHoliday_id(Integer holiday_id) {
		this.holiday_id = holiday_id;
	}
	public String getHoliday_num() {
		return holiday_num;
	}
	public void setHoliday_num(String holiday_num) {
		this.holiday_num = holiday_num;
	}
	public String getHoliday_type_num() {
		return holiday_type_num;
	}
	public void setHoliday_type_num(String holiday_type_num) {
		this.holiday_type_num = holiday_type_num;
	}
	public String getHoliday_begin_date() {
		return holiday_begin_date;
	}
	public void setHoliday_begin_date(String holiday_begin_date) {
		this.holiday_begin_date = holiday_begin_date;
	}
	public String getHoliday_end_date() {
		return holiday_end_date;
	}
	public void setHoliday_end_date(String holiday_end_date) {
		this.holiday_end_date = holiday_end_date;
	}
	public String getHoliday_scope() {
		return holiday_scope;
	}
	public void setHoliday_scope(String holiday_scope) {
		this.holiday_scope = holiday_scope;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
