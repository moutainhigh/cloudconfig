package com.xkd.model;

import java.util.Date;

public class Payment {

	
	
	private String id;
	private String companyId;
	private String paymentMoney;
	private String paymentDate;
	private String moneySituation;
	private String remark;
	private String dealPerson;
	private Date updateDate;
	private Date createDate;
	private String createdBy;
	private String updatedBy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
 
	public String getPaymentMoney() {
		return paymentMoney;
	}
	public void setPaymentMoney(String paymentMoney) {
		this.paymentMoney = paymentMoney;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getMoneySituation() {
		return moneySituation;
	}
	public void setMoneySituation(String moneySituation) {
		this.moneySituation = moneySituation;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDealPerson() {
		return dealPerson;
	}
	public void setDealPerson(String dealPerson) {
		this.dealPerson = dealPerson;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	@Override
	public String toString() {
		return "Payment{" +
				"id='" + id + '\'' +
				", companyId='" + companyId + '\'' +
				", paymentMoney='" + paymentMoney + '\'' +
				", paymentDate='" + paymentDate + '\'' +
				", moneySituation='" + moneySituation + '\'' +
				", remark='" + remark + '\'' +
				", dealPerson='" + dealPerson + '\'' +
				", updateDate=" + updateDate +
				", createDate=" + createDate +
				", createdBy='" + createdBy + '\'' +
				", updatedBy='" + updatedBy + '\'' +
				'}';
	}
}
