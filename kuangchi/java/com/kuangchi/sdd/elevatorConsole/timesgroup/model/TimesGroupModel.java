package com.kuangchi.sdd.elevatorConsole.timesgroup.model;

/**
 * @创建人　: 邓积辉
 * @创建时间: 2016-9-27 上午10:32:46
 * @功能描述:时段组bean
 */
public class TimesGroupModel {
	private Integer id;
	private String time_group_num;//时段组编号
	private String time_group_name;//时段组名称
	private String create_time;//创建时间
	private Integer delete_flag;//删除标志
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTime_group_num() {
		return time_group_num;
	}
	public void setTime_group_num(String time_group_num) {
		this.time_group_num = time_group_num;
	}
	public String getTime_group_name() {
		return time_group_name;
	}
	public void setTime_group_name(String time_group_name) {
		this.time_group_name = time_group_name;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public Integer getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(Integer delete_flag) {
		this.delete_flag = delete_flag;
	}
	
}
