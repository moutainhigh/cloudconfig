package com.kuangchi.sdd.baseConsole.device.model;
/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-6-8 上午9:32:05
 * @功能描述: 设备分布背景图维护Model
 */
public class DescriptionPicModel {
	private int id;					//id
	private String description;		//图片描述
	private String pic_path;		//图片路径
	private int delete_flag;		//删除状态
	private String create_time;		//创建时间
	private String device_group_num;//设备组编号
	private String group_name;		//设备组名
	private String flag;
	
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getDevice_group_num() {
		return device_group_num;
	}
	public void setDevice_group_num(String device_group_num) {
		this.device_group_num = device_group_num;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	public int getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(int delete_flag) {
		this.delete_flag = delete_flag;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
