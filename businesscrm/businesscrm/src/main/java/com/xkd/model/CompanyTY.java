package com.xkd.model;

import java.io.Serializable;
/**
 * 
 * @author: zhoujunlin
 * @2017年5月24日 
 */
public class CompanyTY implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String TermStart;//营业日期自（开始日期）
	
	private String TeamEnd;//营业日期自（结束日期）
	
	private String CheckDate;//检查日期
	
	private String KeyNo;//公司内部关联主键
	
	private String Name;//公司名称
	
	private String No;//注册号
	
	private String BelongOrg;//登记机关
	
	private String OperName;//法定代表人
	
	private String StartDate;//成立日期
	
	private String EndDate;//注销/吊销日期
	
	private String Status;//登记状态（存续、在业、注销、迁入、吊销、迁出、停业、清算）
	
	private String Province;//所在省份缩写
	
	private String UpdatedDate;//记录更新日期
	
	private String CreditCode;//统一社会信用代码
	
	private String RegistCapi;//注册资本
	
	private String EconKind;//类型
	
	private String Address;//住所
	
	private String Scope;//经营范围

	public String getTermStart() {
		return TermStart;
	}

	public void setTermStart(String termStart) {
		TermStart = termStart;
	}

	public String getTeamEnd() {
		return TeamEnd;
	}

	public void setTeamEnd(String teamEnd) {
		TeamEnd = teamEnd;
	}

	public String getCheckDate() {
		return CheckDate;
	}

	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}

	public String getKeyNo() {
		return KeyNo;
	}

	public void setKeyNo(String keyNo) {
		KeyNo = keyNo;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getNo() {
		return No;
	}

	public void setNo(String no) {
		No = no;
	}

	public String getBelongOrg() {
		return BelongOrg;
	}

	public void setBelongOrg(String belongOrg) {
		BelongOrg = belongOrg;
	}

	public String getOperName() {
		return OperName;
	}

	public void setOperName(String operName) {
		OperName = operName;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getProvince() {
		return Province;
	}

	public void setProvince(String province) {
		Province = province;
	}

	public String getUpdatedDate() {
		return UpdatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		UpdatedDate = updatedDate;
	}

	public String getCreditCode() {
		return CreditCode;
	}

	public void setCreditCode(String creditCode) {
		CreditCode = creditCode;
	}

	public String getRegistCapi() {
		return RegistCapi;
	}

	public void setRegistCapi(String registCapi) {
		RegistCapi = registCapi;
	}

	public String getEconKind() {
		return EconKind;
	}

	public void setEconKind(String econKind) {
		EconKind = econKind;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getScope() {
		return Scope;
	}

	public void setScope(String scope) {
		Scope = scope;
	}
	
	
}
