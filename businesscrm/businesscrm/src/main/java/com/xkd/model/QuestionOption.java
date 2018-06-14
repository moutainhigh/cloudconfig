package com.xkd.model;

import java.math.BigDecimal;

/*
 * gaodd
 * 问卷中心、试卷的题目
 */
public class QuestionOption {

	private String id;
	
	private String questionId;
	
	private String opt;
	
	private int grade;
	
	private Boolean answer;
	
	private int level;
	
	private String userAnswer;//temp
	
	private BigDecimal chart;//temp 百分比
	
	private int orderNumber;//排序题时表示排的第几个

	private Boolean showInput;//是否增加填空框

	private String isCheck;//是否校验（0不校验，1必输，2提示）

	private int lengthSize;//题型长度

	private String checkType;//验证类型

	private String toQuestion;//跳转的题目
	
	//temp
	
	private String textContent;

	private String toQuestionName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}
	
	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Boolean getAnswer() {
		return answer;
	}

	

	public void setAnswer(Boolean answer) {
		this.answer = answer;
	}

	public BigDecimal getChart() {
		return chart;
	}

	public void setChart(BigDecimal chart) {
		this.chart = chart;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	@Override
	public String toString() {
		return "DC_WJ_QuestionOption [id=" + id + ", questionId=" + questionId + ", opt=" + opt + ", grade=" + grade + ", level=" + level + ", toQuestion=" + toQuestion + ", answer="
				+ answer + ", userAnswer=" + userAnswer + ", chart=" + chart +", showInput=" + showInput +", isCheck=" + isCheck +", lengthSize=" + lengthSize +", checkType=" + checkType + "]";
	}



	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Boolean getShowInput() {
		return showInput;
	}

	public void setShowInput(Boolean showInput) {
		this.showInput = showInput;
	}

	public void setIsInput(Boolean isInput) {
		isInput = isInput;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public int getLengthSize() {
		return lengthSize;
	}

	public void setLengthSize(int lengthSize) {
		this.lengthSize = lengthSize;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getToQuestion() {
		return toQuestion;
	}

	public void setToQuestion(String toQuestion) {
		this.toQuestion = toQuestion;
	}

	public String getToQuestionName() {
		return toQuestionName;
	}

	public void setToQuestionName(String toQuestionName) {
		this.toQuestionName = toQuestionName;
	}
}
