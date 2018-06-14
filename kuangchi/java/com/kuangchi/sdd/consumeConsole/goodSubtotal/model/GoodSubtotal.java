package com.kuangchi.sdd.consumeConsole.goodSubtotal.model;

import java.math.BigDecimal;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class GoodSubtotal extends BaseModelSupport {

	private String vendor_num;	//商户编号
	private String vendor_name;	//商户名称
	private String sub_date;	//清算时间
	private String trading_volume;	//交易笔数
	private String discount_balance;	//折扣率
	private BigDecimal relief_balance;	//减免金额
	private BigDecimal fee_balance;	//实际金额
	private BigDecimal real_balance;	//实付金额
	private String create_time;	//创建时间
	
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
	public String getSub_date() {
		return sub_date;
	}
	public void setSub_date(String sub_date) {
		this.sub_date = sub_date;
	}
	public String getTrading_volume() {
		return trading_volume;
	}
	public void setTrading_volume(String trading_volume) {
		this.trading_volume = trading_volume;
	}
	public String getDiscount_balance() {
		return discount_balance;
	}
	public void setDiscount_balance(String discount_balance) {
		this.discount_balance = discount_balance;
	}
	public BigDecimal getRelief_balance() {
		return relief_balance;
	}
	public void setRelief_balance(BigDecimal relief_balance) {
		this.relief_balance = relief_balance;
	}
	public BigDecimal getFee_balance() {
		return fee_balance;
	}
	public void setFee_balance(BigDecimal fee_balance) {
		this.fee_balance = fee_balance;
	}
	public BigDecimal getReal_balance() {
		return real_balance;
	}
	public void setReal_balance(BigDecimal real_balance) {
		this.real_balance = real_balance;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	
	
}
