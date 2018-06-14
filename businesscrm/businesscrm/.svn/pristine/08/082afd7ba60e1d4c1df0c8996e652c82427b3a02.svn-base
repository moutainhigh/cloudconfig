package com.xkd.model;

import java.util.List;

/*
 * gaodd
 * 问卷中心、试卷的题目
 */
public class Question {

	private String id;
	
	private String exerciseId;
	
	private String name;
	
	private int ttype;
	
	private String size;//文本框的大小
	
	private String placeholder;
	
	private int answerType;//多选时，1对一个得一个分，2表示全对才得分
	
	private String remark;//题目说明
	

	
	private String tableName;
	
	private String tableColum;
	
	private String tableAndColumn;
	
	private int score;
	
	private int level;
	
	private int flag = 0;//0默认1展开

	private String isCheck;//是否校验（0不校验，1必输，2提示）
	
	private int lengthSize;//题型长度
	
	private String checkType;//验证类型

	private int maxOption;

	private int minOption;
	
	
	
	private List<QuestionOption> child;//题目下面的选项
	
	private List<QuestionOption> opt;//模块下面的题目
	
	private List<UserAnswer> answerList;//问答题，复合题答案集合
	
	private String userAnswer;//temp
	
	private int qindex;//temp题号

	private String showOrHide;
	
	private int key;//temp题号

	public String getTableAndColumn() {
		return tableAndColumn;
	}

	public void setTableAndColumn(String tableAndColumn) {
		this.tableAndColumn = tableAndColumn;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTtype() {
		return ttype;
	}

	public void setTtype(int ttype) {
		this.ttype = ttype;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<QuestionOption> getChild() {
		return child;
	}

	public void setChild(List<QuestionOption> child) {
		this.child = child;
	}

	public List<QuestionOption> getOpt() {
		return opt;
	}

	public void setOpt(List<QuestionOption> opt) {
		this.opt = opt;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public int getQindex() {
		return qindex;
	}

	public void setQindex(int qindex) {
		this.qindex = qindex;
	}

	public List<UserAnswer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<UserAnswer> answerList) {
		this.answerList = answerList;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}


	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	

	public int getAnswerType() {
		return answerType;
	}

	public void setAnswerType(int answerType) {
		this.answerType = answerType;
	}

	
	
	public String getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(String exerciseId) {
		this.exerciseId = exerciseId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableColum() {
		return tableColum;
	}

	public void setTableColum(String tableColum) {
		this.tableColum = tableColum;
	}

	public String getShowOrHide() {
		return showOrHide;
	}

	public void setShowOrHide(String showOrHide) {
		this.showOrHide = showOrHide;
	}

	public int getMaxOption() {
		return maxOption;
	}

	public void setMaxOption(int maxOption) {
		this.maxOption = maxOption;
	}

	public int getMinOption() {
		return minOption;
	}

	public void setMinOption(int minOption) {
		this.minOption = minOption;
	}

	//请不要随意操作高丁丁的toString方法
	@Override
	public String toString() {
		return "DC_WJ_Question [id=" + id +  ", name=" + name + ", ttype=" + ttype + ", lengthSize=" + lengthSize+ ", checkType=" + checkType
				+ ", size=" + size + ", placeholder=" + placeholder + ", isCheck=" + isCheck + ", remark=" + remark + ", score=" + score + ", tableName="
				+ tableName + ", tableColum=" + tableColum+ ", answerType=" + answerType + ", level="
				+ level + ", flag=" + flag +", minOption=" + minOption +", maxOption=" + maxOption + "]";
	}






	
}
