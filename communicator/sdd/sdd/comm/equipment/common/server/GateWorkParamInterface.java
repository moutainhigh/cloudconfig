package com.kuangchi.sdd.comm.equipment.common.server;

public class GateWorkParamInterface {
	private String gateId; // 门号
	private String useSuperPassword;// 是否起用超级开门密码
	private String superPassword; // 超级开门密码 4字节
	private String useForcePassword;// 是否启用胁迫密码
	private String forcePassword; // 胁迫密码 2字节
	private String relockPassword;// 重锁 2字节
	private String unlockPassword;// 解锁 2字节
	private String policePassword;// 报警密码 2字节
	private String openPattern;// 功能模式
	private String checkOpenPattern;// 验证开门模式
	private String workPattern;// 工作模式 1字节
	private String openDelay; // 开门延时 2字节
	private String openOvertime;// 开门超时 1字节
	private String multiOpenNumber;// 多卡开门数量 1字节
	private String multiOpenCard; // 多卡开门卡号 24字节 多卡开门仅支持4字节卡号
	private String useFireFighting;//是否启用消防 0x00:取消消防;	0x01:设置消防.
	private String clientTrigger;//上位触发 0xNN：	bit0~3控制0号门到3号门.
	private String actualTime;//实时上传数据
	private String first;//首卡开门
	private String inout;//双向开门
	
	public String getGateId() {
		return gateId;
	}
	public void setGateId(String gateId) {
		this.gateId = gateId;
	}
	public String getUseSuperPassword() {
		return useSuperPassword;
	}
	public void setUseSuperPassword(String useSuperPassword) {
		this.useSuperPassword = useSuperPassword;
	}
	public String getSuperPassword() {
		return superPassword;
	}
	public void setSuperPassword(String superPassword) {
		this.superPassword = superPassword;
	}
	public String getUseForcePassword() {
		return useForcePassword;
	}
	public void setUseForcePassword(String useForcePassword) {
		this.useForcePassword = useForcePassword;
	}
	public String getForcePassword() {
		return forcePassword;
	}
	public void setForcePassword(String forcePassword) {
		this.forcePassword = forcePassword;
	}
	public String getRelockPassword() {
		return relockPassword;
	}
	public void setRelockPassword(String relockPassword) {
		this.relockPassword = relockPassword;
	}
	public String getUnlockPassword() {
		return unlockPassword;
	}
	public void setUnlockPassword(String unlockPassword) {
		this.unlockPassword = unlockPassword;
	}
	public String getPolicePassword() {
		return policePassword;
	}
	public void setPolicePassword(String policePassword) {
		this.policePassword = policePassword;
	}
	public String getOpenPattern() {
		return openPattern;
	}
	public void setOpenPattern(String openPattern) {
		this.openPattern = openPattern;
	}
	public String getCheckOpenPattern() {
		return checkOpenPattern;
	}
	public void setCheckOpenPattern(String checkOpenPattern) {
		this.checkOpenPattern = checkOpenPattern;
	}
	public String getWorkPattern() {
		return workPattern;
	}
	public void setWorkPattern(String workPattern) {
		this.workPattern = workPattern;
	}
	public String getOpenDelay() {
		return openDelay;
	}
	public void setOpenDelay(String openDelay) {
		this.openDelay = openDelay;
	}
	public String getOpenOvertime() {
		return openOvertime;
	}
	public void setOpenOvertime(String openOvertime) {
		this.openOvertime = openOvertime;
	}
	public String getMultiOpenNumber() {
		return multiOpenNumber;
	}
	public void setMultiOpenNumber(String multiOpenNumber) {
		this.multiOpenNumber = multiOpenNumber;
	}
	public String getMultiOpenCard() {
		return multiOpenCard;
	}
	public void setMultiOpenCard(String multiOpenCard) {
		this.multiOpenCard = multiOpenCard;
	}
	public String getUseFireFighting() {
		return useFireFighting;
	}
	public void setUseFireFighting(String useFireFighting) {
		this.useFireFighting = useFireFighting;
	}
	public String getClientTrigger() {
		return clientTrigger;
	}
	public void setClientTrigger(String clientTrigger) {
		this.clientTrigger = clientTrigger;
	}
	public String getActualTime() {
		return actualTime;
	}
	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getInout() {
		return inout;
	}
	public void setInout(String inout) {
		this.inout = inout;
	}
}
