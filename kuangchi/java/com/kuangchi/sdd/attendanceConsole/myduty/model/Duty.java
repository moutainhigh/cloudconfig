package com.kuangchi.sdd.attendanceConsole.myduty.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;
/**
 * @创建人　: 邓积辉
 * @创建时间: 2016-4-26 上午11:07:13
 * @功能描述:工作班次bean
 */
public class Duty extends BaseModelSupport {
	private Integer id;//排班id
	private String duty_name;//排班名称
	private String is_elastic;//是否是弹性工作制
	private Integer valid_flag;
	private String duty_time1;//上午上班时间
	private String duty_time2;//上午下班时间
	private String duty_time3;//下午上班时间
	private String duty_time4;//下午下班时间
	private String flag_status;//是否启用
	private String elastic_default_duty_time1;//弹性工作制默认上班时间
	private String elastic_default_duty_time2;//弹性工作制默认下班时间
	private String elastic_duty_time1;//弹性工作制必须上班时间起
	private String elastic_duty_time2;//弹性工作制必须上班时间止
	private Double elastic_time_absent_time;//弹性工作制少于多少小时算旷工
	private String duty_start_check_point;//本班次开始统计打卡时间点
	private String duty_end_check_point;//本班次开始统计打卡时间点
	private String duty_time1_point;//上午上班考勤点
	private String duty_time2_point;//上午下班考勤点
	private String duty_time3_point;//下午上班考勤点
	private String duty_time4_point;//下午下班考勤点
	private String over_time_start;//加班时间开始
	private String over_time_end;//加班时间结束
	private String duty_time1_absent;//上午上班旷工时间点
	private String duty_time2_absent;//上午下班旷工时间点
	private String duty_time3_absent;//下午上班旷工时间点
	private String duty_time4_absent;//下午下班旷工时间点
	private String vocation;//公休日
	
	
	public String getDuty_start_check_point() {
		return duty_start_check_point;
	}
	public void setDuty_start_check_point(String duty_start_check_point) {
		this.duty_start_check_point = duty_start_check_point;
	}
	public String getDuty_end_check_point() {
		return duty_end_check_point;
	}
	public void setDuty_end_check_point(String duty_end_check_point) {
		this.duty_end_check_point = duty_end_check_point;
	}
	public String getDuty_time1_point() {
		return duty_time1_point;
	}
	public void setDuty_time1_point(String duty_time1_point) {
		this.duty_time1_point = duty_time1_point;
	}
	public String getDuty_time2_point() {
		return duty_time2_point;
	}
	public void setDuty_time2_point(String duty_time2_point) {
		this.duty_time2_point = duty_time2_point;
	}
	public String getDuty_time3_point() {
		return duty_time3_point;
	}
	public void setDuty_time3_point(String duty_time3_point) {
		this.duty_time3_point = duty_time3_point;
	}
	public String getDuty_time4_point() {
		return duty_time4_point;
	}
	public void setDuty_time4_point(String duty_time4_point) {
		this.duty_time4_point = duty_time4_point;
	}
	public String getOver_time_start() {
		return over_time_start;
	}
	public void setOver_time_start(String over_time_start) {
		this.over_time_start = over_time_start;
	}
	public String getOver_time_end() {
		return over_time_end;
	}
	public void setOver_time_end(String over_time_end) {
		this.over_time_end = over_time_end;
	}
	public String getDuty_time1_absent() {
		return duty_time1_absent;
	}
	public void setDuty_time1_absent(String duty_time1_absent) {
		this.duty_time1_absent = duty_time1_absent;
	}
	public String getDuty_time2_absent() {
		return duty_time2_absent;
	}
	public void setDuty_time2_absent(String duty_time2_absent) {
		this.duty_time2_absent = duty_time2_absent;
	}
	public String getDuty_time3_absent() {
		return duty_time3_absent;
	}
	public void setDuty_time3_absent(String duty_time3_absent) {
		this.duty_time3_absent = duty_time3_absent;
	}
	public String getDuty_time4_absent() {
		return duty_time4_absent;
	}
	public void setDuty_time4_absent(String duty_time4_absent) {
		this.duty_time4_absent = duty_time4_absent;
	}
	public String getVocation() {
		return vocation;
	}
	public void setVocation(String vocation) {
		this.vocation = vocation;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDuty_name() {
		return duty_name;
	}
	public void setDuty_name(String duty_name) {
		this.duty_name = duty_name;
	}
	public String getIs_elastic() {
		return is_elastic;
	}
	public void setIs_elastic(String i) {
		this.is_elastic = i;
	}

	public String getDuty_time1() {
		return duty_time1;
	}
	public void setDuty_time1(String duty_time1) {
		this.duty_time1 = duty_time1;
	}
	public String getDuty_time2() {
		return duty_time2;
	}
	public void setDuty_time2(String duty_time2) {
		this.duty_time2 = duty_time2;
	}
	public String getDuty_time3() {
		return duty_time3;
	}
	public void setDuty_time3(String duty_time3) {
		this.duty_time3 = duty_time3;
	}
	public String getDuty_time4() {
		return duty_time4;
	}
	public void setDuty_time4(String duty_time4) {
		this.duty_time4 = duty_time4;
	}
	public String getFlag_status() {
		return flag_status;
	}
	public void setFlag_status(String flag_status) {
		this.flag_status = flag_status;
	}
	
	public Integer getValid_flag() {
		return valid_flag;
	}
	public void setValid_flag(Integer valid_flag) {
		this.valid_flag = valid_flag;
	}
	public String getElastic_default_duty_time1() {
		return elastic_default_duty_time1;
	}
	public void setElastic_default_duty_time1(String elastic_default_duty_time1) {
		this.elastic_default_duty_time1 = elastic_default_duty_time1;
	}
	public String getElastic_default_duty_time2() {
		return elastic_default_duty_time2;
	}
	public void setElastic_default_duty_time2(String elastic_default_duty_time2) {
		this.elastic_default_duty_time2 = elastic_default_duty_time2;
	}
	public String getElastic_duty_time1() {
		return elastic_duty_time1;
	}
	public void setElastic_duty_time1(String elastic_duty_time1) {
		this.elastic_duty_time1 = elastic_duty_time1;
	}
	public String getElastic_duty_time2() {
		return elastic_duty_time2;
	}
	public void setElastic_duty_time2(String elastic_duty_time2) {
		this.elastic_duty_time2 = elastic_duty_time2;
	}
	public Double getElastic_time_absent_time() {
		return elastic_time_absent_time;
	}
	public void setElastic_time_absent_time(Double elastic_time_absent_time) {
		this.elastic_time_absent_time = elastic_time_absent_time;
	}
	
}
