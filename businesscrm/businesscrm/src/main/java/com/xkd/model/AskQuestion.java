package com.xkd.model;

public class AskQuestion {

	private Integer id;

	private Integer meetingId;

	private String companyId;
	
	private String userId;
	
	private Integer consultPersonId;

	private Integer director;
	
	private String station;
	
	private String industryClassify;
	
	private String questionKeywords;
	
	private String questionDetail;
	
	private String answerDetail;
	
	private String answerScore;
	
	private String askTime;
	
	private String answerTime;

	//咨询对象ID（顾问/总监）名字
	private String adviserName;
	
	private String userName;
	
	public Integer getDirector() {
		return director;
	}

	public void setDirector(Integer director) {
		this.director = director;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Integer meetingId) {
		this.meetingId = meetingId;
	}
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getConsultPersonId() {
		return consultPersonId;
	}

	public void setConsultPersonId(Integer consultPersonId) {
		this.consultPersonId = consultPersonId;
	}

	public String getIndustryClassify() {
		return industryClassify;
	}

	public void setIndustryClassify(String industryClassify) {
		this.industryClassify = industryClassify;
	}

	public String getQuestionKeywords() {
		return questionKeywords;
	}

	public void setQuestionKeywords(String questionKeywords) {
		this.questionKeywords = questionKeywords;
	}

	public String getQuestionDetail() {
		return questionDetail;
	}

	public void setQuestionDetail(String questionDetail) {
		this.questionDetail = questionDetail;
	}

	public String getAnswerDetail() {
		return answerDetail;
	}

	public void setAnswerDetail(String answerDetail) {
		this.answerDetail = answerDetail;
	}

	public String getAnswerScore() {
		return answerScore;
	}

	public void setAnswerScore(String answerScore) {
		this.answerScore = answerScore;
	}

	public String getAskTime() {
		return askTime;
	}

	public void setAskTime(String askTime) {
		this.askTime = askTime;
	}

	public String getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}

	public String getAdviserName() {
		return adviserName;
	}

	public void setAdviserName(String adviserName) {
		this.adviserName = adviserName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
