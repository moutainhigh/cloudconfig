package com.kuangchi.sdd.comm.equipment.common;

import com.kuangchi.sdd.comm.equipment.base.Data;

/**
 * 装载传输给服务器的内容，按双方约定的接口设置属性
 * @author yu.yao
 *
 */
public class EquipmentDataForServer extends Data{
	private String mechineVersion;//0xVVVV：	表示设备硬件版本号。如3205
    private String programVersion;//0xyymmdd：	表示设备程序版本号。如1409
    private String mask;//0xMMMMMMMM：子网掩码。例如：0xFFFFFF00 代表：255.255.255.0
    private String gateway;	//0xGGGGGGGG：网关参数。例如：0xC0A8FE01 代表：192. 168. 254. 1
    private String mechineIP; //0xLLLLLLLL：M4本地IP地址
    private String remoteIP;  //0xRRRRRRRR：M4远程IP地址
    private String mechineStatusPort;//BBBB：M4状态本地端口。心跳
    private String remoteStatusPort; //bbbb：M4状态远程端口。
    private String mechineOrderPort; //IIII：M4指令本地端口。服务器下发指令
    private String remoteOrderPort;  //iiii：M4指令远程端口。
    private String mechineEventPort; //EEEE：M4事件本地端口。获取设备事件，如开门
    private String remoteEventPort;  //eeee：M4事件远程端口。
    private String cardSign;         //卡的位数标记
    private String mac;//MAC地址
    
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getCardSign() {
		return cardSign;
	}
	public void setCardSign(String cardSign) {
		this.cardSign = cardSign;
	}
	public String getMechineVersion() {
		return mechineVersion;
	}
	public void setMechineVersion(String mechineVersion) {
		this.mechineVersion = mechineVersion;
	}
	public String getProgramVersion() {
		return programVersion;
	}
	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String mask) {
		this.mask = mask;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getMechineIP() {
		return mechineIP;
	}
	public void setMechineIP(String mechineIP) {
		this.mechineIP = mechineIP;
	}
	public String getRemoteIP() {
		return remoteIP;
	}
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}
	public String getMechineStatusPort() {
		return mechineStatusPort;
	}
	public void setMechineStatusPort(String mechineStatusPort) {
		this.mechineStatusPort = mechineStatusPort;
	}
	public String getRemoteStatusPort() {
		return remoteStatusPort;
	}
	public void setRemoteStatusPort(String remoteStatusPort) {
		this.remoteStatusPort = remoteStatusPort;
	}
	public String getMechineOrderPort() {
		return mechineOrderPort;
	}
	public void setMechineOrderPort(String mechineOrderPort) {
		this.mechineOrderPort = mechineOrderPort;
	}
	public String getRemoteOrderPort() {
		return remoteOrderPort;
	}
	public void setRemoteOrderPort(String remoteOrderPort) {
		this.remoteOrderPort = remoteOrderPort;
	}
	public String getMechineEventPort() {
		return mechineEventPort;
	}
	public void setMechineEventPort(String mechineEventPort) {
		this.mechineEventPort = mechineEventPort;
	}
	public String getRemoteEventPort() {
		return remoteEventPort;
	}
	public void setRemoteEventPort(String remoteEventPort) {
		this.remoteEventPort = remoteEventPort;
	}
}
