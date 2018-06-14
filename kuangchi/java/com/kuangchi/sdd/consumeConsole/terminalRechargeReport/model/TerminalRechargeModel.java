package com.kuangchi.sdd.consumeConsole.terminalRechargeReport.model;

public class TerminalRechargeModel {
	
	private  Integer  id;         //终端充值报表ID
	private  String   device_num;   //设备编号
	private  String   device_name;  //设备名称
    private  String   vendor_healer_name; //商家名称
	private  String   every_date;    //统计日期
	private  String   begin_time;    //查询时间段开始时间
	private  String   end_time;      //查询时间段结束时间
	private  String   total_amount;        //合计数量   
	private  String   total_money;        //合计金额   
	
	
	
	public String getVendor_healer_name() {
		return vendor_healer_name;
	}
	public void setVendor_healer_name(String vendor_healer_name) {
		this.vendor_healer_name = vendor_healer_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	
	public String getEvery_date() {
		return every_date;
	}
	public void setEvery_date(String every_date) {
		this.every_date = every_date;
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
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getTotal_money() {
		return total_money;
	}
	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}
	
	

}
