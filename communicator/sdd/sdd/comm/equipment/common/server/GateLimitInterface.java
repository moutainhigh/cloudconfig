package com.kuangchi.sdd.comm.equipment.common.server;

public class GateLimitInterface {
	private String gateId; // 门号
	private String seq; // 连续下载的序号
	private String cardId; // 卡编号 3字节
	private String start; // 卡有效期开始时间 5字节
	private String end;   // 卡有效期结束时间 5字节
	private String password; // 开门密码 2字节
	private String group; // 时段组 4字节
	private String limitSign; // 权限标志 2字节
	private String deleteSign;// 删除标记 2字节
	private String retain; // 保留
	public String getGateId() {
		return gateId;
	}
	public void setGateId(String gateId) {
		this.gateId = gateId;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getLimitSign() {
		return limitSign;
	}
	public void setLimitSign(String limitSign) {
		this.limitSign = limitSign;
	}
	public String getDeleteSign() {
		return deleteSign;
	}
	public void setDeleteSign(String deleteSign) {
		this.deleteSign = deleteSign;
	}
	public String getRetain() {
		return retain;
	}
	public void setRetain(String retain) {
		this.retain = retain;
	}
}
