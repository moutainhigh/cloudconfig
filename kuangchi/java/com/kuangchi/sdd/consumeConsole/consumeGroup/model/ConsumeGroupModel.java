package com.kuangchi.sdd.consumeConsole.consumeGroup.model;

public class ConsumeGroupModel {
	
	private Integer id;
	private String group_num; //组代码
	private String group_name; //组名称
	private String consume_type_num; // 消费类型代码
	private String consume_type_name; //消费类型名称
	private String remark; //备注
	private String create_time; //创建时间
	private String delete_flag; //删除标志  0 未删除   1 已删除
	private String isDefault; //是否默认   0 是    1 否
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getConsume_type_num() {
		return consume_type_num;
	}
	public void setConsume_type_num(String consume_type_num) {
		this.consume_type_num = consume_type_num;
	}
	public String getConsume_type_name() {
		return consume_type_name;
	}
	public void setConsume_type_name(String consume_type_name) {
		this.consume_type_name = consume_type_name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	
}
