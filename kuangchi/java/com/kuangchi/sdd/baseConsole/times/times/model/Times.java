package com.kuangchi.sdd.baseConsole.times.times.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-4-5 下午5:12:10
 * @功能描述: 时段信息Bean
 */
public class Times {
	
	private Integer times_id;
	private String times_num;
	private String begin_time;
	private String end_time;
	private Integer eare_num;
	private String result_code;
	
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public Integer getTimes_id() {
		return times_id;
	}
	public void setTimes_id(Integer times_id) {
		this.times_id = times_id;
	}
	public String getTimes_num() {
		return times_num;
	}
	public void setTimes_num(String times_num) {
		this.times_num = times_num;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public Integer getEare_num() {
		return eare_num;
	}
	public void setEare_num(Integer eare_num) {
		this.eare_num = eare_num;
	}
	
	
}
