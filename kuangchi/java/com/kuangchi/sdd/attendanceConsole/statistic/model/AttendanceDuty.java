package com.kuangchi.sdd.attendanceConsole.statistic.model;

public class AttendanceDuty {
		   Integer id;
		   String dutyName;// '排班名称',
		   Integer isElastic;// '是否是弹性工作制',
		   String dutyTime1;// '上午上班时间，如09:00',
		   String dutyTime2;// '上午下班时间',
		   String dutyTime3;// '下午上班时间',
		   String dutyTime4;//'下班时间',
		   String elasticDutyTime1;//弹性工作制必须上班时间起,处在弹性工作制必须上班时间开始与结束之间的时间必须上班
		   String elasticDutyTime2;//弹性工作制必须上班时间止
		   String elasticDefaultDutyTime1;//弹性工作制默认上班时间
		   String elasticDefaultDutyTime2;//弹性工作制默认下班时间
		   Double elasticTimeAbsentTime;//弹性工作制少于多少小时算旷工
		      String dutyStartCheckPoint;// '本班次开始统计打卡时间点',
			  String dutyEndCheckPoint;// '本班次结束统计打卡时间点',
			  String dutyTime1Point;//'上午上班考勤点',
			  String dutyTime2Point;// '上午下班考勤点,这个是非必须的',
			  String dutyTime3Point;//'下午上班考勤点，这个是非必须的',
			  String dutyTime4Point;// '下午下班考勤点',
			  String overTimeStart;// '加班时间开始，如果有加班的话',
			  String overTimeEnd;// '加班时间结束，如果有加班的话',
			  String dutyTime1Absent;//'上午上班旷工时间点',
			  String dutyTime2Absent;//'上午下班旷工时间点',
			  String dutyTime3Absent;// '下午上班旷工时间点',
			  String dutyTime4Absent;// '下午下班旷工时间点',
			  String vocation;
		  String dept_num;  //部门编号,这个是非持久化属性
		  String dept_name;//部门名称,这个是非持久化属性
	
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	
	public Integer getIsElastic() {
		return isElastic;
	}
	public void setIsElastic(Integer isElastic) {
		this.isElastic = isElastic;
	}
	public String getDutyTime1() {
		return dutyTime1;
	}
	public void setDutyTime1(String dutyTime1) {
		this.dutyTime1 = dutyTime1;
	}
	public String getDutyTime2() {
		return dutyTime2;
	}
	public void setDutyTime2(String dutyTime2) {
		this.dutyTime2 = dutyTime2;
	}
	public String getDutyTime3() {
		return dutyTime3;
	}
	public void setDutyTime3(String dutyTime3) {
		this.dutyTime3 = dutyTime3;
	}
	public String getDutyTime4() {
		return dutyTime4;
	}
	public void setDutyTime4(String dutyTime4) {
		this.dutyTime4 = dutyTime4;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDutyStartCheckPoint() {
		return dutyStartCheckPoint;
	}
	public void setDutyStartCheckPoint(String dutyStartCheckPoint) {
		this.dutyStartCheckPoint = dutyStartCheckPoint;
	}
	public String getDutyEndCheckPoint() {
		return dutyEndCheckPoint;
	}
	public void setDutyEndCheckPoint(String dutyEndCheckPoint) {
		this.dutyEndCheckPoint = dutyEndCheckPoint;
	}
	public String getDutyTime1Point() {
		return dutyTime1Point;
	}
	public void setDutyTime1Point(String dutyTime1Point) {
		this.dutyTime1Point = dutyTime1Point;
	}
	public String getDutyTime2Point() {
		return dutyTime2Point;
	}
	public void setDutyTime2Point(String dutyTime2Point) {
		this.dutyTime2Point = dutyTime2Point;
	}
	public String getDutyTime3Point() {
		return dutyTime3Point;
	}
	public void setDutyTime3Point(String dutyTime3Point) {
		this.dutyTime3Point = dutyTime3Point;
	}
	public String getDutyTime4Point() {
		return dutyTime4Point;
	}
	public void setDutyTime4Point(String dutyTime4Point) {
		this.dutyTime4Point = dutyTime4Point;
	}
	public String getOverTimeStart() {
		return overTimeStart;
	}
	public void setOverTimeStart(String overTimeStart) {
		this.overTimeStart = overTimeStart;
	}
	public String getOverTimeEnd() {
		return overTimeEnd;
	}
	public void setOverTimeEnd(String overTimeEnd) {
		this.overTimeEnd = overTimeEnd;
	}
	public String getDutyTime1Absent() {
		return dutyTime1Absent;
	}
	public void setDutyTime1Absent(String dutyTime1Absent) {
		this.dutyTime1Absent = dutyTime1Absent;
	}
	public String getDutyTime2Absent() {
		return dutyTime2Absent;
	}
	public void setDutyTime2Absent(String dutyTime2Absent) {
		this.dutyTime2Absent = dutyTime2Absent;
	}
	public String getDutyTime3Absent() {
		return dutyTime3Absent;
	}
	public void setDutyTime3Absent(String dutyTime3Absent) {
		this.dutyTime3Absent = dutyTime3Absent;
	}
	public String getDutyTime4Absent() {
		return dutyTime4Absent;
	}
	public void setDutyTime4Absent(String dutyTime4Absent) {
		this.dutyTime4Absent = dutyTime4Absent;
	}
	public String getVocation() {
		return vocation;
	}
	public void setVocation(String vocation) {
		this.vocation = vocation;
	}
	public String getElasticDutyTime1() {
		return elasticDutyTime1;
	}
	public void setElasticDutyTime1(String elasticDutyTime1) {
		this.elasticDutyTime1 = elasticDutyTime1;
	}
	public String getElasticDutyTime2() {
		return elasticDutyTime2;
	}
	public void setElasticDutyTime2(String elasticDutyTime2) {
		this.elasticDutyTime2 = elasticDutyTime2;
	}
	public Double getElasticTimeAbsentTime() {
		return elasticTimeAbsentTime;
	}
	public void setElasticTimeAbsentTime(Double elasticTimeAbsentTime) {
		this.elasticTimeAbsentTime = elasticTimeAbsentTime;
	}
	public String getElasticDefaultDutyTime1() {
		return elasticDefaultDutyTime1;
	}
	public void setElasticDefaultDutyTime1(String elasticDefaultDutyTime1) {
		this.elasticDefaultDutyTime1 = elasticDefaultDutyTime1;
	}
	public String getElasticDefaultDutyTime2() {
		return elasticDefaultDutyTime2;
	}
	public void setElasticDefaultDutyTime2(String elasticDefaultDutyTime2) {
		this.elasticDefaultDutyTime2 = elasticDefaultDutyTime2;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	

	
	   
	   
	   
}
