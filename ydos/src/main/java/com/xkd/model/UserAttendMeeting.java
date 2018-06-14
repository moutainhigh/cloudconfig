package com.xkd.model;

import java.util.Date;

public class UserAttendMeeting {

	private String id;
	
	private String userId;
	
	private String companyId;
	
	private String attendMeetingTime;
	
	private String enrollTime;
	
	private String meetingId;
	
	private String trainingSituation;
	
	private String trainingResultLevel;
	
	private String trainingImpactAssessment;
	
	private String status;
	
	private String mgroup;
	
	private String manager;

	private String director;

	private String need;

	private String needDetail;
	
	private Object company;

	private Object userInfo;

	private Object address;
	
	private Integer totalCount;

	private Integer star;

	private Integer ustatus;
	
	private Integer learnStatus;
	
	private Date updateTime;

	private Date createDate;

	private  String createdBy;

	private  String updatedBy;

	private  String ticketLoginUserId;

	public String getTicketLoginUserId() {
		return ticketLoginUserId;
	}

	public void setTicketLoginUserId(String ticketLoginUserId) {
		this.ticketLoginUserId = ticketLoginUserId;
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

	public Date getCreateDate() {

		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getUstatus() {
		return ustatus;
	}
	public void setUstatus(Integer ustatus) {
		this.ustatus = ustatus;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public String getDirector() {
		return director;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public Integer getLearnStatus() {
		return learnStatus;
	}
	public void setLearnStatus(Integer learnStatus) {
		this.learnStatus = learnStatus;
	}
	public String getNeedDetail() {
		return needDetail;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getMgroup() {
		return mgroup;
	}
	public void setMgroup(String mgroup) {
		this.mgroup = mgroup;
	}
	public String getNeed() {
		return need;
	}
	public void setNeed(String need) {
		this.need = need;
	}
	public String getNeedDtail() {
		return needDetail;
	}
	public void setNeedDetail(String needDtail) {
		this.needDetail = needDtail;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getAttendMeetingTime() {
		return attendMeetingTime;
	}
	public void setAttendMeetingTime(String attendMeetingTime) {
		this.attendMeetingTime = attendMeetingTime;
	}
	public String getEnrollTime() {
		return enrollTime;
	}
	public void setEnrollTime(String enrollTime) {
		this.enrollTime = enrollTime;
	}
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getTrainingSituation() {
		return trainingSituation;
	}
	public Object getAddress() {
		return address;
	}
	public void setAddress(Object address) {
		this.address = address;
	}
	public void setTrainingSituation(String trainingSituation) {
		this.trainingSituation = trainingSituation;
	}
	public Object getCompany() {
		return company;
	}
	public void setCompany(Object company) {
		this.company = company;
	}
	public Object getUserInfo() {
		return userInfo;
	}
	public Integer getStar() {
		return star;
	}
	public void setStar(Integer star) {
		this.star = star;
	}
	public void setUserInfo(Object userInfo) {
		this.userInfo = userInfo;
	}
	public String getTrainingResultLevel() {
		return trainingResultLevel;
	}
	public void setTrainingResultLevel(String trainingResultLevel) {
		this.trainingResultLevel = trainingResultLevel;
	}
	public String getTrainingImpactAssessment() {
		return trainingImpactAssessment;
	}
	public void setTrainingImpactAssessment(String trainingImpactAssessment) {
		this.trainingImpactAssessment = trainingImpactAssessment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
