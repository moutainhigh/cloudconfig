package com.kuangchi.sdd.baseConsole.device.model;



/**
 * 门参数对象
 * @author yu.yao
 *
 */
/**
 * 门号 是否起用超级开门密码 超级开门密码 是否起用胁迫密码 胁迫密码 重锁密码 解锁密码 报警密码 功能模式 验证开门模式 工作模式 开门延时 开门超时
 * 多卡开门数量 多卡开门卡号 1 1 4 1 2 2 2 2 1 1 1 2 1 1 24 字节数 0x00 0x00 0x00 0x44 0x00
 * 0x00 0x00 0x00 0x00 0x00 0x00 0050 5 0x08 0x00 缺省值
 * 
 * 
 */
public class ActualTimeBean  {

	private int gateId; // 门号
	private int useSuperPassword;// 是否起用超级开门密码
	private long superPassword; // 超级开门密码 4字节
	private int useForcePassword;// 是否启用胁迫密码
	private int forcePassword; // 胁迫密码 2字节
	private int relockPassword;// 重锁 2字节
	private int unlockPassword;// 解锁 2字节
	private int policePassword;// 报警密码 2字节
	private int openPattern;// 功能模式
	private int checkOpenPattern;// 验证开门模式
	private int workPattern;// 工作模式 1字节
	private int openDelay; // 开门延时 2字节
	private int openOvertime;// 开门超时 1字节
	private int multiOpenNumber;// 多卡开门数量 1字节
	private int[] multiOpenCard; // 多卡开门卡号 24字节 多卡开门仅支持3字节卡号
	private int useFireFighting;//是否启用消防 0x00:取消消防;	0x01:设置消防.
	private int clientTrigger;//上位触发 0xNN：	bit0~3控制0号门到3号门.
	private int actualTime;//实时上传数据
	private int first;//首卡开门
	private int inout;//双向开门

	

	public int getGateId() {
		return gateId;
	}

	public void setGateId(int gateId) {
		this.gateId = gateId;
	}

	public int getUseSuperPassword() {
		return useSuperPassword;
	}

	public void setUseSuperPassword(int useSuperPassword) {
		this.useSuperPassword = useSuperPassword;
	}

	public long getSuperPassword() {
		return superPassword;
	}

	public void setSuperPassword(long superPassword) {
		this.superPassword = superPassword;
	}

	public int getUseForcePassword() {
		return useForcePassword;
	}

	public void setUseForcePassword(int useForcePassword) {
		this.useForcePassword = useForcePassword;
	}

	public int getForcePassword() {
		return forcePassword;
	}

	public void setForcePassword(int forcePassword) {
		this.forcePassword = forcePassword;
	}

	public int getRelockPassword() {
		return relockPassword;
	}

	public void setRelockPassword(int relockPassword) {
		this.relockPassword = relockPassword;
	}

	public int getUnlockPassword() {
		return unlockPassword;
	}

	public void setUnlockPassword(int unlockPassword) {
		this.unlockPassword = unlockPassword;
	}

	public int getPolicePassword() {
		return policePassword;
	}

	public void setPolicePassword(int policePassword) {
		this.policePassword = policePassword;
	}

	public int getOpenPattern() {
		return openPattern;
	}

	public void setOpenPattern(int openPattern) {
		this.openPattern = openPattern;
	}

	public int getCheckOpenPattern() {
		return checkOpenPattern;
	}

	public void setCheckOpenPattern(int checkOpenPattern) {
		this.checkOpenPattern = checkOpenPattern;
	}

	public int getWorkPattern() {
		return workPattern;
	}

	public void setWorkPattern(int workPattern) {
		this.workPattern = workPattern;
	}

	public int getOpenDelay() {
		return openDelay;
	}

	public void setOpenDelay(int openDelay) {
		this.openDelay = openDelay;
	}

	public int getOpenOvertime() {
		return openOvertime;
	}

	public void setOpenOvertime(int openOvertime) {
		this.openOvertime = openOvertime;
	}

	public int getMultiOpenNumber() {
		return multiOpenNumber;
	}

	public void setMultiOpenNumber(int multiOpenNumber) {
		this.multiOpenNumber = multiOpenNumber;
	}

	public int[] getMultiOpenCard() {
		return multiOpenCard;
	}

	public void setMultiOpenCard(int[] multiOpenCard) {
		this.multiOpenCard = multiOpenCard;
	}

	public int getUseFireFighting() {
		return useFireFighting;
	}

	public void setUseFireFighting(int useFireFighting) {
		this.useFireFighting = useFireFighting;
	}

	public int getClientTrigger() {
		return clientTrigger;
	}

	public void setClientTrigger(int clientTrigger) {
		this.clientTrigger = clientTrigger;
	}

	public int getActualTime() {
		return actualTime;
	}

	public void setActualTime(int actualTime) {
		this.actualTime = actualTime;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getInout() {
		return inout;
	}

	public void setInout(int inout) {
		this.inout = inout;
	}
}
