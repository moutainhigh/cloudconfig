package com.xkd.model;

import java.util.Date;

public class Meeting {

	private String id;
	
	private String meetingName;
	
	private String invitationId;
	
	private String introduce;
	
	private String place;
	
	private String startTime;

	private String endTime;
	
	private String meetingType;

	private String meetingStatus;
	
	private String dates;
	
	private String leader;

	private String contacter;
	
	private String mrequire;
	
	private String theme;

	private String content;
	
	private String travelArrangement;
	
	private String teacherId;

	private Integer exerciseId;
	
	private String teacherName;
	
	private Integer status;
	//为了方便统计
	private Integer total;
	//为了方便统计
	private Integer attended;
	//封装对象返回前端
	private Object object;
	
	private Date updateTime;
	
	private String createTime;

	private String meetingContent;
	
	private String createdBy;

	private String updatedBy;

	private String createdByName;

	private String updatedByName;

	private Date updateDate; 
	
	private Date createDate; 
	
	private String province;
	
	private String city;
	
	private String county;

	private String attributeContent;

	private String ticketRights;

	private String meetingImage;

	private String meetingDetail;

	private String departmentId;

	private String departmentName;

	private String sendMessageFlag;

	private String mobile;

	private String pcCompanyId;
	private Integer getTicketNumber;
	private Integer ticketNumber;
	private  Integer mflag;
	private  Integer flag;
	private  String spreadSettingId;

	public String getSpreadSettingId() {
		return spreadSettingId;
	}

	public void setSpreadSettingId(String spreadSettingId) {
		this.spreadSettingId = spreadSettingId;
	}

	public Integer getGetTicketNumber() {
		return getTicketNumber;
	}

	public void setGetTicketNumber(Integer getTicketNumber) {
		this.getTicketNumber = getTicketNumber;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getUpdatedByName() {
		return updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public Integer getMflag() {
		return mflag;
	}

	public void setMflag(Integer mflag) {
		this.mflag = mflag;
	}

	public Integer getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(Integer ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getSendMessageFlag() {
		return sendMessageFlag;
	}

	public void setSendMessageFlag(String sendMessageFlag) {
		this.sendMessageFlag = sendMessageFlag;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getMeetingDetail() {
		return meetingDetail;
	}

	public void setMeetingDetail(String meetingDetail) {
		this.meetingDetail = meetingDetail;
	}

	public String getAttributeContent() {
		return attributeContent;
	}

	public void setAttributeContent(String attributeContent) {
		this.attributeContent = attributeContent;
	}

	public String getTicketRights() {
		return ticketRights;
	}

	public void setTicketRights(String ticketRights) {
		this.ticketRights = ticketRights;
	}

	public String getMeetingImage() {
		return meetingImage;
	}

	public void setMeetingImage(String meetingImage) {
		this.meetingImage = meetingImage;
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

	public Integer getExerciseId() {
		return exerciseId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setExerciseId(Integer exerciseId) {
		this.exerciseId = exerciseId;
	}

	public String getId() {
		return id;
	}

	public String getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	public Object getObject() {
		return object;
	}

	public String getMeetingContent() {
		return meetingContent;
	}

	public void setMeetingContent(String meetingContent) {
		this.meetingContent = meetingContent;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getMeetingStatus() {
		return meetingStatus;
	}

	public void setMeetingStatus(String meetingStatus) {
		this.meetingStatus = meetingStatus;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMeetingName() {
		return meetingName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

	public String getContent() {
		return content;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getPcCompanyId() {
		return pcCompanyId;
	}

	public void setPcCompanyId(String pcCompanyId) {
		this.pcCompanyId = pcCompanyId;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getAttended() {
		return attended;
	}

	public void setAttended(Integer attended) {
		this.attended = attended;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getContacter() {
		return contacter;
	}

	public void setContacter(String contacter) {
		this.contacter = contacter;
	}

	public String getMrequire() {
		return mrequire;
	}

	public void setMrequire(String mrequire) {
		this.mrequire = mrequire;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTravelArrangement() {
		return travelArrangement;
	}

	public void setTravelArrangement(String travel_arrangement) {
		this.travelArrangement = travel_arrangement;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}
	
	
}
