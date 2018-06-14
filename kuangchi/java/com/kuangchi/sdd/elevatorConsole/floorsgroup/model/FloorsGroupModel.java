package com.kuangchi.sdd.elevatorConsole.floorsgroup.model;

public class FloorsGroupModel {
	private Integer id;
	private String floor_num;//楼层编号
	private String floor_name;//楼层名称
	private String floor_group_num;//楼层组编号
	private String floor_group_name;//楼层组名称
	private String old_floor_group_name;//老在楼层组名称
	private String delete_flag;//删除标志 0 未删除 1 删除
	
	
	
	
	
	
	
	public String getOld_floor_group_name() {
		return old_floor_group_name;
	}
	public void setOld_floor_group_name(String old_floor_group_name) {
		this.old_floor_group_name = old_floor_group_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFloor_group_num() {
		return floor_group_num;
	}
	public void setFloor_group_num(String floor_group_num) {
		this.floor_group_num = floor_group_num;
	}
	public String getFloor_group_name() {
		return floor_group_name;
	}
	public void setFloor_group_name(String floor_group_name) {
		this.floor_group_name = floor_group_name;
	}
	public String getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}
	public String getFloor_num() {
		return floor_num;
	}
	public void setFloor_num(String floor_num) {
		this.floor_num = floor_num;
	}
	public String getFloor_name() {
		return floor_name;
	}
	public void setFloor_name(String floor_name) {
		this.floor_name = floor_name;
	}
	
	
	
	
	
}
