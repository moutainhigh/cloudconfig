package com.kuangchi.sdd.baseConsole.device.model;

public class EquipmentBean {

	//设备类型
	private String devicetype;

	//门编号
	private Integer doornum;
	
	//通讯服务器id
	private String commId;
	
	
	
	public String getCommId() {
		return commId;
	}
	public void setCommId(String commId) {
		this.commId = commId;
	}
	public Integer getDoornum() {
		return doornum;
	}
	public void setDoornum(Integer doornum) {
		this.doornum = doornum;
	}
	public String getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	//创建人员代码
	private String createuser;
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getDevicenum() {
		return devicenum;
	}
	public void setDevicenum(String devicenum) {
		this.devicenum = devicenum;
	}
	//设备编号
	private String devicenum;
	//设备硬件版本号
	private String mechineVersion;
    //设备程序版本号
	private String programVersion;
	//子网掩码
	private String mask;
	//网关参数
	private String gateway;
	//本地IP地址
	private String mechineIP;
	//远程IP地址
	private String remoteIP;
	//状态本地端口
	private String mechineStatusPort;
	//状态远程端口
	private String remoteStatusPort;
	//指令本地端口
	private String mechineOrderPort;
	//指令远程端口
	private String remoteOrderPort;
	//事件本地端口
	private String mechineEventPort;
	//事件远程端口
	private String remoteEventPort;
	//0x08-4字节卡号 0x04-3字节卡号
	private String cardSign;
	private String deviceName;
	//MAC地址
	private String mac;
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
	public String getCardSign() {
		return cardSign;
	}
	public void setCardSign(String cardSign) {
		this.cardSign = cardSign;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	
	
}
