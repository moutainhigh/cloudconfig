package com.xkd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Company implements Serializable {

	String id ;
	String companyName  ;
	String englishName  ;
	String label  ;
	String logo  ;
	String userLevel  ;
	String contactUserId ;
	String contactName ;
	String contactPhone  ;
	String country  ;
	String province   ;
	String 	city ;
	String county  ;
	String address ;
	Integer status  ;
	String createdBy ;
	String  createDate ;
	String updatedBy ;
	String  updateDate ;
	String departmentId  ;
	String pcCompanyId ;
	String companyDesc ;
	String website ;
	String phone  ;
	String email  ;
	String wechat  ;
	String content;
	String createdByName;

	Integer totalDevice=0;

	List<Map<String,Object>> contactorList=new ArrayList<>();
	List<Map<String,Object>>  contractList=new ArrayList<>();
	List<Map<String,Object>> repaireList=new ArrayList<>();
	List<Map<String,Object>>  inspectionList=new ArrayList<>();
    List<Map<String,Object>> responsibleUserList=new ArrayList<>();

	Integer inspectionPlanCount;
	Integer repaireCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getContactUserId() {
		return contactUserId;
	}

	public void setContactUserId(String contactUserId) {
		this.contactUserId = contactUserId;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getPcCompanyId() {
		return pcCompanyId;
	}

	public void setPcCompanyId(String pcCompanyId) {
		this.pcCompanyId = pcCompanyId;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public List<Map<String, Object>> getContactorList() {
		return contactorList;
	}

	public void setContactorList(List<Map<String, Object>> contactorList) {
		this.contactorList = contactorList;
	}

	public List<Map<String, Object>> getContractList() {
		return contractList;
	}

	public void setContractList(List<Map<String, Object>> contractList) {
		this.contractList = contractList;
	}

	public List<Map<String, Object>> getRepaireList() {
		return repaireList;
	}

	public void setRepaireList(List<Map<String, Object>> repaireList) {
		this.repaireList = repaireList;
	}

	public List<Map<String, Object>> getInspectionList() {
		return inspectionList;
	}

	public void setInspectionList(List<Map<String, Object>> inspectionList) {
		this.inspectionList = inspectionList;
	}

	public Integer getTotalDevice() {
		return totalDevice;
	}

	public void setTotalDevice(Integer totalDevice) {
		this.totalDevice = totalDevice;
	}

	public List<Map<String, Object>> getResponsibleUserList() {
		return responsibleUserList;
	}

	public void setResponsibleUserList(List<Map<String, Object>> responsibleUserList) {
		this.responsibleUserList = responsibleUserList;
	}

	public Integer getRepaireCount() {
		return repaireCount;
	}

	public void setRepaireCount(Integer repaireCount) {
		this.repaireCount = repaireCount;
	}

	public Integer getInspectionPlanCount() {
		return inspectionPlanCount;
	}

	public void setInspectionPlanCount(Integer inspectionPlanCount) {
		this.inspectionPlanCount = inspectionPlanCount;
	}
}
