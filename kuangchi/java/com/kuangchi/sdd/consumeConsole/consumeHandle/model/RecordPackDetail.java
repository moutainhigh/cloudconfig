package com.kuangchi.sdd.consumeConsole.consumeHandle.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-23 下午3:46:16
 * @功能描述: 记录包-model
 */
public class RecordPackDetail {
	
	private String record_flow_num; //记录流水号 （接口生成）
	private String record_flow_no; //记录流水号 （消费机上报）
    private String record_type;
    private String card_num; //卡号
    private String card_flow_no; //卡流水号
    private Double inbound; //收入
    private Double outbound; //支出
    
    private String device_num; //设备编号
    private String device_name; //设备名称
    private String device_type_num; //设备类型编号
    private String is_type; //设备绑定类别      0 商品     1 商品类别
    private String good_or_type_num; //设备绑定商品编号
    
    private String vendor_num; //商户编号
    private String vendor_name; //商户名称
    private String vendor_dealer_name; //商家名称
    private String good_num; //设备绑定商品编号
    private String good_name; //商品名称
    private Double price; //商品单价
    private String type_num; //商品类型编号 
    private String type_name; //类型名称
    private Double type_price; //类型单价
    private String discount_num; //商品折扣代码
    private Double discount_rate; //商品折扣率
    
    private String account_num; //消费账号
    private Double account_balance; //账号余额
    private String account_type_num; //账号类型
    private String group_num; //消费组编号
    private String group_name; //消费组名称
    
    private String consume_date; //消费日期
    private String consume_time; //消费时间
    private Double consume_sum; //应消费总额
    private Double actual_consume_sum; //实际消费总额 （扣取折扣的情况下）
    private Double cancel_sum; //撤销总额（撤销消费记录时使用）
    private Double refund_sum; //退款总额（退款时使用）
    private String refund; //是否已退款（撤销消费时进行判断使用）
    private Integer consume_amount; //消费次数
    private Integer good_amount; //商品数量
    
    private String staff_id;
    private String staff_num; //人员代码
    private String staff_name; //人员名称
    private String staff_no; //人员工号（显示）
    private String dept_num; //部门代码
    private String dept_name; //部门名称
    private String dept_no; //部门编号（显示）
    
    private String meal_num; //餐次编号
    private String meal_name; //餐次名称
    private String meal_start_time; //餐次开始时间
    private String meal_end_time; //餐次结束时间
    private String create_time;//创建时间
    
    
	public String getRefund() {
		return refund;
	}
	public void setRefund(String refund) {
		this.refund = refund;
	}
	public Double getCancel_sum() {
		return cancel_sum;
	}
	public void setCancel_sum(Double cancel_sum) {
		this.cancel_sum = cancel_sum;
	}
	public Double getRefund_sum() {
		return refund_sum;
	}
	public void setRefund_sum(Double refund_sum) {
		this.refund_sum = refund_sum;
	}
	public String getGood_or_type_num() {
		return good_or_type_num;
	}
	public void setGood_or_type_num(String good_or_type_num) {
		this.good_or_type_num = good_or_type_num;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getRecord_flow_num() {
		return record_flow_num;
	}
	public void setRecord_flow_num(String record_flow_num) {
		this.record_flow_num = record_flow_num;
	}
	public Integer getGood_amount() {
		return good_amount;
	}
	public void setGood_amount(Integer good_amount) {
		this.good_amount = good_amount;
	}
	public Double getInbound() {
		return inbound;
	}
	public void setInbound(Double inbound) {
		this.inbound = inbound;
	}
	public Double getOutbound() {
		return outbound;
	}
	public void setOutbound(Double outbound) {
		this.outbound = outbound;
	}
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public Integer getConsume_amount() {
		return consume_amount;
	}
	public void setConsume_amount(Integer consume_amount) {
		this.consume_amount = consume_amount;
	}
	public Double getActual_consume_sum() {
		return actual_consume_sum;
	}
	public void setActual_consume_sum(Double actual_consume_sum) {
		this.actual_consume_sum = actual_consume_sum;
	}
	public Double getType_price() {
		return type_price;
	}
	public void setType_price(Double type_price) {
		this.type_price = type_price;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}
	public String getIs_type() {
		return is_type;
	}
	public void setIs_type(String is_type) {
		this.is_type = is_type;
	}
	public String getType_num() {
		return type_num;
	}
	public void setType_num(String type_num) {
		this.type_num = type_num;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getConsume_date() {
		return consume_date;
	}
	public void setConsume_date(String consume_date) {
		this.consume_date = consume_date;
	}
	public String getVendor_dealer_name() {
		return vendor_dealer_name;
	}
	public void setVendor_dealer_name(String vendor_dealer_name) {
		this.vendor_dealer_name = vendor_dealer_name;
	}
	public String getDiscount_num() {
		return discount_num;
	}
	public void setDiscount_num(String discount_num) {
		this.discount_num = discount_num;
	}
	public Double getDiscount_rate() {
		return discount_rate;
	}
	public void setDiscount_rate(Double discount_rate) {
		this.discount_rate = discount_rate;
	}
	public String getRecord_flow_no() {
		return record_flow_no;
	}
	public void setRecord_flow_no(String record_flow_no) {
		this.record_flow_no = record_flow_no;
	}
	public String getRecord_type() {
		return record_type;
	}
	public void setRecord_type(String record_type) {
		this.record_type = record_type;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getCard_flow_no() {
		return card_flow_no;
	}
	public void setCard_flow_no(String card_flow_no) {
		this.card_flow_no = card_flow_no;
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
	public String getDevice_type_num() {
		return device_type_num;
	}
	public void setDevice_type_num(String device_type_num) {
		this.device_type_num = device_type_num;
	}
	public String getAccount_num() {
		return account_num;
	}
	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}
	public Double getAccount_balance() {
		return account_balance;
	}
	public void setAccount_balance(Double account_balance) {
		this.account_balance = account_balance;
	}
	public String getAccount_type_num() {
		return account_type_num;
	}
	public void setAccount_type_num(String account_type_num) {
		this.account_type_num = account_type_num;
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
	public Double getConsume_sum() {
		return consume_sum;
	}
	public void setConsume_sum(Double consume_sum) {
		this.consume_sum = consume_sum;
	}
	public String getConsume_time() {
		return consume_time;
	}
	public void setConsume_time(String consume_time) {
		this.consume_time = consume_time;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
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
	public String getMeal_start_time() {
		return meal_start_time;
	}
	public void setMeal_start_time(String meal_start_time) {
		this.meal_start_time = meal_start_time;
	}
	public String getMeal_end_time() {
		return meal_end_time;
	}
	public void setMeal_end_time(String meal_end_time) {
		this.meal_end_time = meal_end_time;
	}
	
}


