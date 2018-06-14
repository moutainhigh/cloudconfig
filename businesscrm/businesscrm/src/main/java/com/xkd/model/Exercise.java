package com.xkd.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * gaodd
 * 问卷中心、试卷
 */
public class Exercise {

	private String id;
	
	private String title;

	private String codeTitle;
	
	private String departmentId;//部门id
	
	private String ttype;
	
	private String meetingName;
	
	private String meetingType;
	
	private String meetingId;
	
	private String createdBy;

	private String pcCompanyId;

	private String titleImg;
	
	private String createDate;
	
	private String updatedBy;
	
	private String updateDate;

	private String prompt;//结束语文本内容

	private Boolean showPrompt;   //是否显示结束语
	private Boolean showGrade;     //是否显示分数
	private Boolean showButtom; // 是否显示按钮
	private Boolean showImgpath; //是否显示图片
	private String imgpath;    //图片链接
	private String buttomContent; //按钮文本
	private String toLinkType;       //跳转类型
	private String toLinkUrl;           //跳转链接
	private String tabType;    //跳转类型
	private String outLinkUrl;
	private String collectContent;
	private String collect;




	private String cnt;//temp做题人数 or用户做题请求试卷时表示0未做题1已经做题
	
	private String cssType;//呈现方式
	
	private String childTitle;//副标题
	
	private Boolean flag = false;//是否选中

	private int questionSum;//题目个数

	private String uname;
	
	private String submitDate;
	
	private String grade;//分数

	private String departmentName;//部门名称

	private String mobile;

	private String companyName;
	private String profession;//职位
	private String email;

	private String userId;


	private List<Question> question;
	
	private List<Map<String, List>> data;//表集合
	
	private List<HashMap<String, Object>> answerList;//题目集合
	
	private List<Map<String, Object>> comment;//评论集合
	
	private DC_User user;//做题的用户

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCodeTitle() {
		return codeTitle;
	}

	public void setCodeTitle(String codeTitle) {
		this.codeTitle = codeTitle;
	}

	public String getTtype() {
		return ttype;
	}

	public void setTtype(String ttype) {
		this.ttype = ttype;
	}

	public String getMeetingName() {
		return meetingName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

	public String getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
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

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public Boolean getShowPrompt() {
		return showPrompt;
	}

	public void setShowPrompt(Boolean showPrompt) {
		this.showPrompt = showPrompt;
	}

	public Boolean getShowGrade() {
		return showGrade;
	}

	public void setShowGrade(Boolean showGrade) {
		this.showGrade = showGrade;
	}

	public Boolean getShowButtom() {
		return showButtom;
	}

	public void setShowButtom(Boolean showButtom) {
		this.showButtom = showButtom;
	}

	public Boolean getShowImgpath() {
		return showImgpath;
	}

	public void setShowImgpath(Boolean showImgpath) {
		this.showImgpath = showImgpath;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public String getButtomContent() {
		return buttomContent;
	}

	public void setButtomContent(String buttomContent) {
		this.buttomContent = buttomContent;
	}

	public String getToLinkType() {
		return toLinkType;
	}

	public void setToLinkType(String toLinkType) {
		this.toLinkType = toLinkType;
	}

	public String getToLinkUrl() {
		return toLinkUrl;
	}

	public void setToLinkUrl(String toLinkUrl) {
		this.toLinkUrl = toLinkUrl;
	}

	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	public String getCssType() {
		return cssType;
	}

	public void setCssType(String cssType) {
		this.cssType = cssType;
	}

	public String getChildTitle() {
		return childTitle;
	}

	public void setChildTitle(String childTitle) {
		this.childTitle = childTitle;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public int getQuestionSum() {
		return questionSum;
	}

	public void setQuestionSum(int questionSum) {
		this.questionSum = questionSum;
	}

	public String getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}

	public List<Question> getQuestion() {
		return question;
	}

	public void setQuestion(List<Question> question) {
		this.question = question;
	}

	public List<Map<String, List>> getData() {
		return data;
	}

	public void setData(List<Map<String, List>> data) {
		this.data = data;
	}

	public List<HashMap<String, Object>> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<HashMap<String, Object>> answerList) {
		this.answerList = answerList;
	}

	public List<Map<String, Object>> getComment() {
		return comment;
	}

	public void setComment(List<Map<String, Object>> comment) {
		this.comment = comment;
	}

	public DC_User getUser() {
		return user;
	}

	public void setUser(DC_User user) {
		this.user = user;
	}

	public String getOutLinkUrl() {
		return outLinkUrl;
	}

	public void setOutLinkUrl(String outLinkUrl) {
		this.outLinkUrl = outLinkUrl;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCollectContent() {
		return collectContent;
	}

	public void setCollectContent(String collectContent) {
		this.collectContent = collectContent;
	}

	public String getCollect() {
		return collect;
	}

	public void setCollect(String collect) {
		this.collect = collect;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPcCompanyId() {
		return pcCompanyId;
	}

	public void setPcCompanyId(String pcCompanyId) {
		this.pcCompanyId = pcCompanyId;
	}

	public String getTitleImg() {
		return titleImg;
	}

	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}
}
