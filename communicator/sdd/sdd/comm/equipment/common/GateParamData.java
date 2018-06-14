package com.kuangchi.sdd.comm.equipment.common;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.Data;
import com.kuangchi.sdd.util.excel.ExcelExportServer;

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
public class GateParamData extends Data {
	public static final Logger LOG = Logger.getLogger(GateParamData.class);	
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
	private long[] multiOpenCard; // 多卡开门卡号 24字节 多卡开门仅支持4字节卡号
	private int useFireFighting;//是否启用消防 0x00:取消消防;	0x01:设置消防.
	private int clientTrigger;//上位触发 0xNN：	bit0~3控制0号门到3号门.
	private int actualTime;//实时上传数据
	private int first;//首卡开门
	private int inout;//双向开门

	/**
	 * 获取校验码
	 * 
	 * @return
	 */
	public long getCrcFromSum() {
		// 计算多卡开门校验和
		int multiOpenCardSum = 0;
		if(multiOpenCard != null){
			long m0,m1, m2, m3;
			for (int i = 0; i < multiOpenCard.length; i++) {
				m0 = (multiOpenCard[i] & 0xFF000000) >>24;
				m1 = (multiOpenCard[i] & 0x00FF0000) >> 16;
				m2 = (multiOpenCard[i] & 0x0000FF00) >> 8;
				m3 = (multiOpenCard[i] & 0x000000FF);
				multiOpenCardSum += (m0+m1 + m2 + m3);
			}
			LOG.info("multiOpenCardSum="+multiOpenCardSum);
		}
		
		// 计算超级密码校验和
		int superPasswordSum = 0;
		long s1, s2, s3, s4;
		s1 = (superPassword & 0xFF000000) >> 24;
		s2 = (superPassword & 0x00FF0000) >> 16;
		s3 = (superPassword & 0x000000FF) >> 8;
		s4 = (superPassword & 0x000000FF);
		superPasswordSum += (s1 + s2 + s3 + s4);
		LOG.info("superPasswordSum="+superPasswordSum);
		// 计算胁迫密码校验和 2字节
		int forcePasswordSum = 0;
		int f1, f2;
		f1 = (forcePassword & 0xFF00) >> 8;
		f2 = (forcePassword & 0x00FF);
		forcePasswordSum += (f1 + f2);
		// 计算重锁密码校验和 2字节
		int relockPasswordSum = 0;
		int r1, r2;
		r1 = (relockPassword & 0xFF00) >> 8;
		r2 = (relockPassword & 0x00FF);
		relockPasswordSum += (r1 + r2);
		LOG.info("forcePasswordSum="+forcePasswordSum);
		// 计算解锁密码校验和 2字节
		int unlockPasswordSum = 0;
		int u1, u2;
		u1 = (unlockPassword & 0xFF00) >> 8;
		u2 = (unlockPassword & 0x00FF);
		unlockPasswordSum += (u1 + u2);
		LOG.info("unlockPasswordSum="+unlockPasswordSum);
		// 计算报警密码校验和 2字节
		int policePasswordSum = 0;
		int p1, p2;
		p1 = (policePassword & 0xFF00) >> 8;
		p2 = (policePassword & 0x00FF);
		policePasswordSum += (p1 + p2);
		LOG.info("policePasswordSum="+policePasswordSum);
		//计算开门延时的校验和
		int openDelaySum = 0;
		int o1, o2;
		o1 = (openDelay & 0xFF00) >> 8;
		o2 = (openDelay & 0x00FF);
		openDelaySum += (o1 + o2);
		LOG.info("openDelaySum="+openDelaySum);
		//计算实时上传参数的校验和
		int actualTimeSum = 0;
		int a1 = (actualTime & 0xFF00) >> 8;
		int a2 = (actualTime & 0x00FF);
		actualTimeSum = a1+a2;
		
		int returnVal=
		getGateId() + getUseSuperPassword() + superPasswordSum
				+ getUseForcePassword() + forcePasswordSum
				+ relockPasswordSum + unlockPasswordSum
				+ policePasswordSum + getOpenPattern()
				+ getCheckOpenPattern() + getWorkPattern() + openDelaySum
				+ getOpenOvertime() + getMultiOpenNumber() + multiOpenCardSum
				+ useFireFighting+clientTrigger+actualTimeSum+first+inout;
		LOG.info("returnVal="+returnVal);
		return returnVal;
	}

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

	

	public long[] getMultiOpenCard() {
		return multiOpenCard;
	}

	public void setMultiOpenCard(long[] multiOpenCard) {
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
