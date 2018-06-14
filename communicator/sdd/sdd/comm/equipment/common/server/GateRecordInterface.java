package com.kuangchi.sdd.comm.equipment.common.server;

public class GateRecordInterface {
	private String gatePoint;// 门禁记录头指针
	private String gateRecord;// 门禁记录数
	private String patrolPoint;// 巡更记录头指针
	private String patrolRecord;// 巡更记录数
	private String userPoint;// 用户记录头指针
	private String userRecord;// 用户记录数
	private String gateLimitNum;// 门禁权限记录数
	private String patrolLimitNum;// 巡更权限记录数
    private String recordType;//记录类型
    
	public String getGatePoint() {
		return gatePoint;
	}
	public void setGatePoint(String gatePoint) {
		this.gatePoint = gatePoint;
	}
	public String getGateRecord() {
		return gateRecord;
	}
	public void setGateRecord(String gateRecord) {
		this.gateRecord = gateRecord;
	}
	public String getPatrolPoint() {
		return patrolPoint;
	}
	public void setPatrolPoint(String patrolPoint) {
		this.patrolPoint = patrolPoint;
	}
	public String getPatrolRecord() {
		return patrolRecord;
	}
	public void setPatrolRecord(String patrolRecord) {
		this.patrolRecord = patrolRecord;
	}
	public String getUserPoint() {
		return userPoint;
	}
	public void setUserPoint(String userPoint) {
		this.userPoint = userPoint;
	}
	public String getUserRecord() {
		return userRecord;
	}
	public void setUserRecord(String userRecord) {
		this.userRecord = userRecord;
	}
	public String getGateLimitNum() {
		return gateLimitNum;
	}
	public void setGateLimitNum(String gateLimitNum) {
		this.gateLimitNum = gateLimitNum;
	}
	public String getPatrolLimitNum() {
		return patrolLimitNum;
	}
	public void setPatrolLimitNum(String patrolLimitNum) {
		this.patrolLimitNum = patrolLimitNum;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
}
