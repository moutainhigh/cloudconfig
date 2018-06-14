package com.xkd.model;

/**
 * @author gaodd
 *  用户问卷答题选项表
 */

public class UserAnswer {



	private String id;
	
	private String userExamId;
	
	private String exerciseId;
	
	private String questionId;
	
	private int ttype;
	
	private String answer;
	
	private int orderNumber;//排序题时表示排的第几个
	
	private String submitDate;
	
	private String openId;

	private String textContent;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExerciseId() {
		return exerciseId;
	}




	public void setExerciseId(String exerciseId) {
		this.exerciseId = exerciseId;
	}




	public String getQuestionId() {
		return questionId;
	}




	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}




	public int getTtype() {
		return ttype;
	}




	public void setTtype(int ttype) {
		this.ttype = ttype;
	}




	

	
	public String getUserExamId() {
		return userExamId;
	}
	public void setUserExamId(String userExamId) {
		this.userExamId = userExamId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}


	public String getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}

	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	//不要删除or改动我的toString2
	@Override
	public String toString() {
		return "'"+answer+"'";
	}
}
