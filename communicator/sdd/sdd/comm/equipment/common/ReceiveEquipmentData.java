package com.kuangchi.sdd.comm.equipment.common;

/**
 * 装载搜索设备接口返回的有效数据,映射控制器接口
 * 32|5|14|9|1|ff|ff|ff|0|c0|a8|fe|4|c0|a8|fe|1|c0|a8|fe|
 * 4|54|46|5d|46|51|46|5b|46|52|46|5c|46|8|
 * 
 * @author yu.yao
 * 
 */
public class ReceiveEquipmentData{
	// 0xVVVV： 表示设备硬件版本号。如3205
	private short mechineVersionA;
	private short mechineVersionB;
	// 0xyymmdd： 表示设备程序版本号。如140901
	private short programVersionA;
	private short programVersionB;
	private short programVersionC;
	// 0xMMMMMMMM：子网掩码。例如：0xFFFFFF00 代表：255.255.255.0
	private short maskA;
	private short maskB;
	private short maskC;
	private short maskD;
	// private int mask;
	// 0xGGGGGGGG：网关参数。例如：0xC0A8FE01 代表：192. 168. 254. 1
	private short gatewayA;
	private short gatewayB;
	private short gatewayC;
	private short gatewayD;
	// private int geteway;
	// 0xLLLLLLLL：M4本地IP地址
	private short mechineIpA;
	private short mechineIpB;
	private short mechineIpC;
	private short mechineIpD;
	// private int mechineIP;
	// 0xRRRRRRRR：M4远程IP地址
	private short remoteIpA;
	private short remoteIpB;
	private short remoteIpC;
	private short remoteIpD;
	// private int remoteIP;
	// BBBB：M4状态本地端口。心跳
	private short mechineStatusPortA;
	private short mechineStatusPortB;
	// bbbb：M4状态远程端口。
	private short remoteStatusPortA;
	private short remoteStatusPortB;
	// IIII：M4指令本地端口。服务器下发指令
	private short mechineOrderPortA;
	private short mechineOrderPortB;
	// iiii：M4指令远程端口。
	private short remoteOrderPortA;
	private short remoteOrderPortB;
	// EEEE：M4事件本地端口。获取设备事件，如开门
	private short mechineEventPortA;
	private short mechineEventPortB;
	// eeee：M4事件远程端口。
	private short remoteEventPortA;
	private short remoteEventPortB;
	// 1字节 0x08-4字节卡号 0x04-3字节卡号
	private short cardSign;
	//MAC地址
	private int mac;
	

	public int getMac() {
		return mac;
	}

	public void setMac(int mac) {
		this.mac = mac;
	}

	public short getCardSign() {
		return cardSign;
	}

	public void setCardSign(short cardSign) {
		this.cardSign = cardSign;
	}

	public int getCrc() {

		return mechineVersionA + mechineVersionB +

		programVersionA + programVersionB + programVersionC +

		maskA + maskB + maskC + maskD +

		gatewayA + gatewayB + gatewayC + gatewayD +

		mechineIpA + mechineIpB + mechineIpC + mechineIpD +

		remoteIpA + remoteIpB + remoteIpC + remoteIpD +

		mechineStatusPortA + mechineStatusPortB +

		remoteStatusPortA + remoteStatusPortB +

		mechineOrderPortA + mechineOrderPortB +

		remoteOrderPortA + remoteOrderPortB +

		mechineEventPortA + mechineEventPortB +

		remoteEventPortA + remoteEventPortB + cardSign;
	}

	public short getMechineVersionA() {
		return mechineVersionA;
	}

	public void setMechineVersionA(short mechineVersionA) {
		this.mechineVersionA = mechineVersionA;
	}

	public short getMechineVersionB() {
		return mechineVersionB;
	}

	public void setMechineVersionB(short mechineVersionB) {
		this.mechineVersionB = mechineVersionB;
	}

	public short getProgramVersionA() {
		return programVersionA;
	}

	public void setProgramVersionA(short programVersionA) {
		this.programVersionA = programVersionA;
	}

	public short getProgramVersionB() {
		return programVersionB;
	}

	public void setProgramVersionB(short programVersionB) {
		this.programVersionB = programVersionB;
	}

	public short getProgramVersionC() {
		return programVersionC;
	}

	public void setProgramVersionC(short programVersionC) {
		this.programVersionC = programVersionC;
	}

	public short getMaskA() {
		return maskA;
	}

	public void setMaskA(short maskA) {
		this.maskA = maskA;
	}

	public short getMaskB() {
		return maskB;
	}

	public void setMaskB(short maskB) {
		this.maskB = maskB;
	}

	public short getMaskC() {
		return maskC;
	}

	public void setMaskC(short maskC) {
		this.maskC = maskC;
	}

	public short getMaskD() {
		return maskD;
	}

	public void setMaskD(short maskD) {
		this.maskD = maskD;
	}

	public short getGatewayA() {
		return gatewayA;
	}

	public void setGatewayA(short gatewayA) {
		this.gatewayA = gatewayA;
	}

	public short getGatewayB() {
		return gatewayB;
	}

	public void setGatewayB(short gatewayB) {
		this.gatewayB = gatewayB;
	}

	public short getGatewayC() {
		return gatewayC;
	}

	public void setGatewayC(short gatewayC) {
		this.gatewayC = gatewayC;
	}

	public short getGatewayD() {
		return gatewayD;
	}

	public void setGatewayD(short gatewayD) {
		this.gatewayD = gatewayD;
	}

	public short getMechineIpA() {
		return mechineIpA;
	}

	public void setMechineIpA(short mechineIpA) {
		this.mechineIpA = mechineIpA;
	}

	public short getMechineIpB() {
		return mechineIpB;
	}

	public void setMechineIpB(short mechineIpB) {
		this.mechineIpB = mechineIpB;
	}

	public short getMechineIpC() {
		return mechineIpC;
	}

	public void setMechineIpC(short mechineIpC) {
		this.mechineIpC = mechineIpC;
	}

	public short getMechineIpD() {
		return mechineIpD;
	}

	public void setMechineIpD(short mechineIpD) {
		this.mechineIpD = mechineIpD;
	}

	public short getRemoteIpA() {
		return remoteIpA;
	}

	public void setRemoteIpA(short remoteIpA) {
		this.remoteIpA = remoteIpA;
	}

	public short getRemoteIpB() {
		return remoteIpB;
	}

	public void setRemoteIpB(short remoteIpB) {
		this.remoteIpB = remoteIpB;
	}

	public short getRemoteIpC() {
		return remoteIpC;
	}

	public void setRemoteIpC(short remoteIpC) {
		this.remoteIpC = remoteIpC;
	}

	public short getRemoteIpD() {
		return remoteIpD;
	}

	public void setRemoteIpD(short remoteIpD) {
		this.remoteIpD = remoteIpD;
	}

	public short getMechineStatusPortA() {
		return mechineStatusPortA;
	}

	public void setMechineStatusPortA(short mechineStatusPortA) {
		this.mechineStatusPortA = mechineStatusPortA;
	}

	public short getMechineStatusPortB() {
		return mechineStatusPortB;
	}

	public void setMechineStatusPortB(short mechineStatusPortB) {
		this.mechineStatusPortB = mechineStatusPortB;
	}

	public short getRemoteStatusPortA() {
		return remoteStatusPortA;
	}

	public void setRemoteStatusPortA(short remoteStatusPortA) {
		this.remoteStatusPortA = remoteStatusPortA;
	}

	public short getRemoteStatusPortB() {
		return remoteStatusPortB;
	}

	public void setRemoteStatusPortB(short remoteStatusPortB) {
		this.remoteStatusPortB = remoteStatusPortB;
	}

	public short getMechineOrderPortA() {
		return mechineOrderPortA;
	}

	public void setMechineOrderPortA(short mechineOrderPortA) {
		this.mechineOrderPortA = mechineOrderPortA;
	}

	public short getMechineOrderPortB() {
		return mechineOrderPortB;
	}

	public void setMechineOrderPortB(short mechineOrderPortB) {
		this.mechineOrderPortB = mechineOrderPortB;
	}

	public short getRemoteOrderPortA() {
		return remoteOrderPortA;
	}

	public void setRemoteOrderPortA(short remoteOrderPortA) {
		this.remoteOrderPortA = remoteOrderPortA;
	}

	public short getRemoteOrderPortB() {
		return remoteOrderPortB;
	}

	public void setRemoteOrderPortB(short remoteOrderPortB) {
		this.remoteOrderPortB = remoteOrderPortB;
	}

	public short getMechineEventPortA() {
		return mechineEventPortA;
	}

	public void setMechineEventPortA(short mechineEventPortA) {
		this.mechineEventPortA = mechineEventPortA;
	}

	public short getMechineEventPortB() {
		return mechineEventPortB;
	}

	public void setMechineEventPortB(short mechineEventPortB) {
		this.mechineEventPortB = mechineEventPortB;
	}

	public short getRemoteEventPortA() {
		return remoteEventPortA;
	}

	public void setRemoteEventPortA(short remoteEventPortA) {
		this.remoteEventPortA = remoteEventPortA;
	}

	public short getRemoteEventPortB() {
		return remoteEventPortB;
	}

	public void setRemoteEventPortB(short remoteEventPortB) {
		this.remoteEventPortB = remoteEventPortB;
	}
}
