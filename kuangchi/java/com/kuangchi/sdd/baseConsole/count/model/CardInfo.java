package com.kuangchi.sdd.baseConsole.count.model;

/**
 * @创建人　: chudan.guo
 * @创建时间: 2016-10-25
 * @功能描述: 卡片信息统计-model
 */
public class CardInfo {
     private String staff_name; //姓名
     private String staff_no;   //员工工号
     private String type_name;  //卡类型
     private String card_num;   //卡号
     private String state_name; //卡片状态
     private String card_validity; //卡有效期
     private String create_time;   //创建时间
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public String getCard_validity() {
		return card_validity;
	}
	public void setCard_validity(String card_validity) {
		this.card_validity = card_validity;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
     
} 
