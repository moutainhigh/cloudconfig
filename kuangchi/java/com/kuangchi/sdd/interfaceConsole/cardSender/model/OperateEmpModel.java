package com.kuangchi.sdd.interfaceConsole.cardSender.model;

public class OperateEmpModel {
	private String bsId;// bs端人员代码
	private String id;// 发卡软件端人员id
	private String bsBmDm;// bs端部门代码
	private String name;// 姓名
	private String sex;// 性别
	private String workState;// 职位状态：0 在职 1 离职
	private String workNo;// 员工工号
	private String mobile;// 移动电话
	private String phone;// 固定电话
	private String idNumber;// 身份证号
	private String address;// 联系地址
	private String description;// 备注
	private String createDate;// 创建时间
	private String errorCode;// 错误编码
	private String email;// 电子邮箱
	private String room_num;// 房间号

	public String getRoom_num() {
		return room_num;
	}

	public void setRoom_num(String room_num) {
		this.room_num = room_num;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getBsId() {
		return bsId;
	}

	public void setBsId(String bsId) {
		this.bsId = bsId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBsBmDm() {
		return bsBmDm;
	}

	public void setBsBmDm(String bsBmDm) {
		this.bsBmDm = bsBmDm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWorkState() {
		return workState;
	}

	public void setWorkState(String workState) {
		this.workState = workState;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}
