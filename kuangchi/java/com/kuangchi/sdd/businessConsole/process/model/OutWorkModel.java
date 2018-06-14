package com.kuangchi.sdd.businessConsole.process.model;

public class OutWorkModel {
	private Integer id;
	private String staffNum; // 员工编号
	private String reason; // 外出原因
	private String fromTime; // 开始时间
	private String toTime; // 结束时间
	private Integer totalTime; // 外出时间
	private String processInstanceId; // 流程实例ID

	private String destination;// 目的地
	private String contactName;// 接洽人姓名
	private String contactPhone;// 接洽人电话
	private Double transportCosts;// 预计交通费
	private Double eatLiveCosts;// 预计餐费（住宿费用）
	private String transport;// 乘坐的交通工具；1.飞机；2.火车；3.轮船；4.汽车；5.公交车；6.出租车；7.个人车；8.研究院车辆

	public Integer getId() {
		return id;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Double getTransportCosts() {
		return transportCosts;
	}

	public void setTransportCosts(Double transportCosts) {
		this.transportCosts = transportCosts;
	}

	public Double getEatLiveCosts() {
		return eatLiveCosts;
	}

	public void setEatLiveCosts(Double eatLiveCosts) {
		this.eatLiveCosts = eatLiveCosts;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public Integer getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

}
