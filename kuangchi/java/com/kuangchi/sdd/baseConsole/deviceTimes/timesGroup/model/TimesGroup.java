package com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.model;


import com.kuangchi.sdd.baseConsole.card.model.BaseModelSupport;


public class TimesGroup extends BaseModelSupport{
	//这里的属性顺序必须是这样，不然，查看功能会出问题
	private int group_id;
	private String group_num;
	private String group_name;
	private String times_one_num;
	private String times_two_num;
	private String times_three_num;
	private String times_four_num;
	private String times_five_num;
	private String times_six_num;
	private String times_seven_num;
	private String times_eight_num;
	private String times_priority;
	
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
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
	public String getTimes_one_num() {
		return times_one_num;
	}
	public void setTimes_one_num(String times_one_num) {
		this.times_one_num = times_one_num;
	}
	public String getTimes_two_num() {
		return times_two_num;
	}
	public void setTimes_two_num(String times_two_num) {
		this.times_two_num = times_two_num;
	}
	public String getTimes_three_num() {
		return times_three_num;
	}
	public void setTimes_three_num(String times_three_num) {
		this.times_three_num = times_three_num;
	}
	public String getTimes_four_num() {
		return times_four_num;
	}
	public void setTimes_four_num(String times_four_num) {
		this.times_four_num = times_four_num;
	}
	public String getTimes_five_num() {
		return times_five_num;
	}
	public void setTimes_five_num(String times_five_num) {
		this.times_five_num = times_five_num;
	}
	public String getTimes_six_num() {
		return times_six_num;
	}
	public void setTimes_six_num(String times_six_num) {
		this.times_six_num = times_six_num;
	}
	public String getTimes_seven_num() {
		return times_seven_num;
	}
	public void setTimes_seven_num(String times_seven_num) {
		this.times_seven_num = times_seven_num;
	}
	public String getTimes_eight_num() {
		return times_eight_num;
	}
	public void setTimes_eight_num(String times_eight_num) {
		this.times_eight_num = times_eight_num;
	}
	public String getTimes_priority() {
		return times_priority;
	}
	public void setTimes_priority(String times_priority) {
		this.times_priority = times_priority;
	}
	
}
