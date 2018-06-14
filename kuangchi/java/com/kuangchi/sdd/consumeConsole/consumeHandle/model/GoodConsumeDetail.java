package com.kuangchi.sdd.consumeConsole.consumeHandle.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-21 下午5:14:24
 * @功能描述: 商品日消费流水-model
 */
public class GoodConsumeDetail {

	private Integer id;
	private String every_date; //日期字符 如 2012-10-11 每天一条
	private String meal_num;  //餐次
	private String meal_name; //餐次名称
	private String device_num; //设备编号
	private String device_name; //设备名称
	private String good_num; //商品编号
	private String good_name; //商品名称
	private String good_type_num; //商品类型
	private String good_type_name; //商品类型名称
	private Double price; //单价
	private Integer amount; //数量
	private Double total; //合计
	private String vendor_num; //商户编号
	private String vendor_name; //商户名称
	private String vendor_dealer_name; //商家名称
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
	private String record_place; //记录位置  由消费机上报
	private String create_time; //创建时间
	private Double cancel_total; // 撤销总额
	private Double refund_total; //退款总额
	private Double after_discount_money; //实际交易金额（折后金额）
	
	
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
	public Double getAfter_discount_money() {
		return after_discount_money;
	}
	public void setAfter_discount_money(Double after_discount_money) {
		this.after_discount_money = after_discount_money;
	}
	public String getGood_type_name() {
		return good_type_name;
	}
	public void setGood_type_name(String good_type_name) {
		this.good_type_name = good_type_name;
	}
	public String getVendor_num() {
		return vendor_num;
	}
	public void setVendor_num(String vendor_num) {
		this.vendor_num = vendor_num;
	}
	public String getVendor_name() {
		return vendor_name;
	}
	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}
	public String getVendor_dealer_name() {
		return vendor_dealer_name;
	}
	public void setVendor_dealer_name(String vendor_dealer_name) {
		this.vendor_dealer_name = vendor_dealer_name;
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
	public String getMeal_num() {
		return meal_num;
	}
	public void setMeal_num(String meal_num) {
		this.meal_num = meal_num;
	}
	public String getMeal_name() {
		return meal_name;
	}
	public void setMeal_name(String meal_name) {
		this.meal_name = meal_name;
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
	public String getGood_num() {
		return good_num;
	}
	public void setGood_num(String good_num) {
		this.good_num = good_num;
	}
	public String getGood_name() {
		return good_name;
	}
	public void setGood_name(String good_name) {
		this.good_name = good_name;
	}
	public String getGood_type_num() {
		return good_type_num;
	}
	public void setGood_type_num(String good_type_num) {
		this.good_type_num = good_type_num;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getRecord_place() {
		return record_place;
	}
	public void setRecord_place(String record_place) {
		this.record_place = record_place;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	
	
	
}
