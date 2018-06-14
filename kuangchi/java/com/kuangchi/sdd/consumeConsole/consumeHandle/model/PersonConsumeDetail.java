package com.kuangchi.sdd.consumeConsole.consumeHandle.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-21 下午5:14:56
 * @功能描述: 人员日消费流水-model
 */
public class PersonConsumeDetail {
	
	private Integer id;
	private String every_date; //日期字符 如 2012-10-11 每天一条
	private String dept_num; //部门编号
	private String dept_name; //部门名称
	private String dept_no; //部门编号（显示）
	private String staff_num; //员工编号
	private String staff_name; // 员工名称
	private String staff_no; //员工工号（显示）
	private Integer meal1_amount = 0; //餐次一次数
	private Double meal1_money = 0.0; //餐次一金额
	private Integer meal2_amount = 0; //餐次二次数
	private Double meal2_money = 0.0; //餐次二金额
	private Integer meal3_amount = 0; //餐次三次数
	private Double meal3_money = 0.0; //餐次三金额
	private Integer meal4_amount = 0; //餐次四次数
	private Double meal4_money = 0.0; //餐次四金额
	private Integer meal5_amount = 0; //餐次五次数
	private Double meal5_money = 0.0; //餐次五金额
	private Double cancel_total; // 撤销总额
	private Double refund_total; //退款总额
	private Double money; //合计交易金额（折前金额）
	private Double after_discount_money; //实际交易金额（折后金额）
	private String create_time; //创建时间
	
	
	public Double getCancel_total() {
		return cancel_total;
	}
	public void setCancel_total(Double cancel_total) {
		this.cancel_total = cancel_total;
	}
	public Double getRefund_total() {
		return refund_total;
	}
	public void setRefund_total(Double refund_total) {
		this.refund_total = refund_total;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Double getAfter_discount_money() {
		return after_discount_money;
	}
	public void setAfter_discount_money(Double after_discount_money) {
		this.after_discount_money = after_discount_money;
	}
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEvery_date() {
		return every_date;
	}
	public void setEvery_date(String every_date) {
		this.every_date = every_date;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public Integer getMeal1_amount() {
		return meal1_amount;
	}
	public void setMeal1_amount(Integer meal1_amount) {
		this.meal1_amount = meal1_amount;
	}
	public Double getMeal1_money() {
		return meal1_money;
	}
	public void setMeal1_money(Double meal1_money) {
		this.meal1_money = meal1_money;
	}
	public Integer getMeal2_amount() {
		return meal2_amount;
	}
	public void setMeal2_amount(Integer meal2_amount) {
		this.meal2_amount = meal2_amount;
	}
	public Double getMeal2_money() {
		return meal2_money;
	}
	public void setMeal2_money(Double meal2_money) {
		this.meal2_money = meal2_money;
	}
	public Integer getMeal3_amount() {
		return meal3_amount;
	}
	public void setMeal3_amount(Integer meal3_amount) {
		this.meal3_amount = meal3_amount;
	}
	public Double getMeal3_money() {
		return meal3_money;
	}
	public void setMeal3_money(Double meal3_money) {
		this.meal3_money = meal3_money;
	}
	public Integer getMeal4_amount() {
		return meal4_amount;
	}
	public void setMeal4_amount(Integer meal4_amount) {
		this.meal4_amount = meal4_amount;
	}
	public Double getMeal4_money() {
		return meal4_money;
	}
	public void setMeal4_money(Double meal4_money) {
		this.meal4_money = meal4_money;
	}
	public Integer getMeal5_amount() {
		return meal5_amount;
	}
	public void setMeal5_amount(Integer meal5_amount) {
		this.meal5_amount = meal5_amount;
	}
	public Double getMeal5_money() {
		return meal5_money;
	}
	public void setMeal5_money(Double meal5_money) {
		this.meal5_money = meal5_money;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	
	
}
