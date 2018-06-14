package com.xkd.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Schedule implements Serializable {


	private String id;
	
	private String province;
	
	private String city;

	private Integer dayNum;

	private String teacherId;

	private String companyId;
	
	private String address;
	
	private String lessonType;
	
	private String lessonName;
	
	private String priority;
	
	private String remind;
	
	private Boolean fullDay;

	private String status;

	
	private String startDate;
	
	private String endDate;
	
	private String scheduleDetail;

	private String createdBy;

	private String createDate;

	private String updatedBy;
	
	private String updateDate;
	
	//temp
	private String colleagues;
	
	private String teacherName;
	
	private String stateName;
	
	private Boolean remove;
	
	private String uname;
	
	private String startTime;
	
	private String endTime;
	
	private Boolean my;

	private Boolean isNews;

	private String loggerId;

	private String action;

	private String pcCompanyId;
		
	/**
	 * 用于存放队员Id,名称colleague [{{id,1}{name,XX}}]
	 */
	private List<HashMap<String,Object>> listcolleagueIdAndName;
	/**
	 * 同行人员
	 */
	private List<String> listColleague;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Integer getDayNum() {
		return dayNum;
	}
	public void setDayNum(Integer dayNum) {
		this.dayNum = dayNum;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLessonType() {
		return lessonType;
	}
	public void setLessonType(String lessonType) {
		this.lessonType = lessonType;
	}
	public String getLessonName() {
		return lessonName;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createDate;
	}
	public void setCreatedDate(String createDate) {
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
	public List<HashMap<String, Object>> getListcolleagueIdAndName() {
		return listcolleagueIdAndName;
	}
	public void setListcolleagueIdAndName(List<HashMap<String, Object>> listcolleagueIdAndName) {
		this.listcolleagueIdAndName = listcolleagueIdAndName;
	}
	public List<String> getListColleague() {
		return listColleague;
	}
	public void setListColleague(List<String> listColleague) {
		this.listColleague = listColleague;
	}
	public String getColleagues() {
		return colleagues;
	}
	public void setColleagues(String colleagues) {
		this.colleagues = colleagues;
	}
	public String getScheduleDetail() {
		return scheduleDetail;
	}
	public void setScheduleDetail(String scheduleDetail) {
		this.scheduleDetail = scheduleDetail;
	}
	
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Boolean getRemove() {
		return remove;
	}
	public void setRemove(Boolean remove) {
		this.remove = remove;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getRemind() {
		return remind;
	}
	public void setRemind(String remind) {
		this.remind = remind;
	}
	public Boolean getFullDay() {
		return fullDay;
	}
	public void setFullDay(Boolean fullDay) {
		this.fullDay = fullDay;
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
	public Boolean getMy() {
		return my;
	}
	public void setMy(Boolean my) {
		this.my = my;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "Schedule{" +
				"id='" + id + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", dayNum=" + dayNum +
				", teacherId='" + teacherId + '\'' +
				", address='" + address + '\'' +
				", lessonType='" + lessonType + '\'' +
				", lessonName='" + lessonName + '\'' +
				", priority='" + priority + '\'' +
				", remind='" + remind + '\'' +
				", fullDay=" + fullDay +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", scheduleDetail='" + scheduleDetail + '\'' +
				", createdBy='" + createdBy + '\'' +
				", createDate='" + createDate + '\'' +
				", updatedBy='" + updatedBy + '\'' +
				", updateDate='" + updateDate + '\'' +
				", colleagues='" + colleagues + '\'' +
				", teacherName='" + teacherName + '\'' +
				", stateName='" + stateName + '\'' +
				", remove=" + remove +
				", uname='" + uname + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", my=" + my +
				", loggerId='" + loggerId + '\'' +
				", listcolleagueIdAndName=" + listcolleagueIdAndName +
				", listColleague=" + listColleague +
				'}';
	}

	public Boolean getNews() {
		return isNews;
	}

	public void setNews(Boolean news) {
		isNews = news;
	}

	public String getLoggerId() {
		return loggerId;
	}

	public void setLoggerId(String loggerId) {
		this.loggerId = loggerId;
	}


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPcCompanyId() {
		return pcCompanyId;
	}

	public void setPcCompanyId(String pcCompanyId) {
		this.pcCompanyId = pcCompanyId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
