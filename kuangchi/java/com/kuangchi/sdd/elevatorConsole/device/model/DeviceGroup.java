package com.kuangchi.sdd.elevatorConsole.device.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class DeviceGroup extends BaseModelSupport {

	private Integer group_id;  //设备组ID
    private String  group_num; //设备组编号
    private String  group_name; //设备组名称
    private String  parent_group_num; //上级设备组编号
    private String  validity_flag; //有效标志
    private String  create_user; //创建人员代码
    private String  create_time; //创建时间
    private String  description; //描述
    private String flag;
    
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public String getGroup_num() {
		return group_num;
	}
	public void setGroup_num(String group_num) {
		this.group_num = group_num;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getParent_group_num() {
		return parent_group_num;
	}
	public void setParent_group_num(String parent_group_num) {
		this.parent_group_num = parent_group_num;
	}
	public String getValidity_flag() {
		return validity_flag;
	}
	public void setValidity_flag(String validity_flag) {
		this.validity_flag = validity_flag;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
    
}
