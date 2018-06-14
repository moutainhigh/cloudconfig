package com.kuangchi.sdd.consumeConsole.fundPool.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-16 上午11:04:37
 * @功能描述: 企业资金池 Model
 */

public class FundPoolModel {
	
	private Integer id;
	private String organiztion_num; //机构代码   （即商户编号）
	private String organiztion_name; //机构名称 
	private Double recharge_sum; //充值金额
	private String recharge_reason; //充值原因
	private Double inbound; //流入资金
	private Double outbound; //流出资金
	private Double total; //资金总额
	private Double left_total; //剩余金额
	private Double percent; //资金百分比
	private String pool_state; //资金池状态
	private String register_time; //注册时间
	
	
	public String getRecharge_reason() {
		return recharge_reason;
	}
	public void setRecharge_reason(String recharge_reason) {
		this.recharge_reason = recharge_reason;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrganiztion_name() {
		return organiztion_name;
	}
	public void setOrganiztion_name(String organiztion_name) {
		this.organiztion_name = organiztion_name;
	}
	public String getOrganiztion_num() {
		return organiztion_num;
	}
	public void setOrganiztion_num(String organiztion_num) {
		this.organiztion_num = organiztion_num;
	}
	public Double getRecharge_sum() {
		return recharge_sum;
	}
	public void setRecharge_sum(Double recharge_sum) {
		this.recharge_sum = recharge_sum;
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
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getLeft_total() {
		return left_total;
	}
	public void setLeft_total(Double left_total) {
		this.left_total = left_total;
	}
	public Double getPercent() {
		return percent;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
	}
	public String getPool_state() {
		return pool_state;
	}
	public void setPool_state(String pool_state) {
		this.pool_state = pool_state;
	}
	public String getRegister_time() {
		return register_time;
	}
	public void setRegister_time(String register_time) {
		this.register_time = register_time;
	}
	
	
}
