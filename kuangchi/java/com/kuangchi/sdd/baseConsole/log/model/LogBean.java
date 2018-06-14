package com.kuangchi.sdd.baseConsole.log.model;

/**
 * @创建人: 杨金林
 * @创建时间: 2015-12-30 19:27:46
 * @功能描述: 日志信息
 */
public class LogBean {
	private String op_name;//存储过程名
	private String op_msg;//信息
	private String op_date;//操作时间
	private String op_type;//业务类型
	private String op_function;//功能
	private String op_id;//操作员
	
	/*weixuan.lu 增加两个属性*/
	private String startDate;
	private String endDate;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
	public String getOp_name() {
		return op_name;
	}
	public void setOp_name(String op_name) {
		this.op_name = op_name;
	}
	public String getOp_msg() {
		return op_msg;
	}
	public void setOp_msg(String op_msg) {
		this.op_msg = op_msg;
	}
	public String getOp_date() {
		return op_date;
	}
	public void setOp_date(String op_date) {
		this.op_date = op_date;
	}
	public String getOp_type() {
		return op_type;
	}
	public void setOp_type(String op_type) {
		this.op_type = op_type;
	}
	public String getOp_function() {
		return op_function;
	}
	public void setOp_function(String op_function) {
		this.op_function = op_function;
	}
	public String getOp_id() {
		return op_id;
	}
	public void setOp_id(String op_id) {
		this.op_id = op_id;
	}
	public int setPage(int parseInt) {
		return parseInt;
		// TODO Auto-generated method stub
		
	}
	public int setRows(int parseInt) {
		return parseInt;
		// TODO Auto-generated method stub
		
	}
	
}
