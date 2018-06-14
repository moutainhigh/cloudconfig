package com.kuangchi.sdd.comm.equipment.common;

import java.util.ArrayList;
import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.Data;

public class EquipmentData extends Data{
	// 0xVVVV： 表示设备硬件版本号。如3205
	private short[] mechineVersion = {};
	// 0xyymmdd： 表示设备程序版本号。如140901
	private short[] programVersion = {};
	// 0xMMMMMMMM：子网掩码。例如：0xFFFFFF00 代表：255.255.255.0
	private List<Integer> mask=new ArrayList<Integer>();
	// private int mask;
	// 0xGGGGGGGG：网关参数。例如：0xC0A8FE01 代表：192. 168. 254. 1
	private List<Integer> gateway;
	// 0xLLLLLLLL：M4本地IP地址
	private List<Integer> mechineIP;
	// 0xRRRRRRRR：M4远程IP地址
	private List<Integer> remoteIP;
	// BBBB：M4状态本地端口。心跳
	private short[] mechineStatusPort = {};
	// bbbb：M4状态远程端口。
	private short[] remoteStatusPort = {};
	// IIII：M4指令本地端口。服务器下发指令
	private short[] mechineOrderPort = {};
	// iiii：M4指令远程端口。
	private short[] remoteOrderPort = {};
	// EEEE：M4事件本地端口。获取设备事件，如开门
	private short[] mechineEventPort = {};
	// eeee：M4事件远程端口。
	private short[] remoteEventPort = {};
	// 1字节 0x08-4字节卡号 0x04-3字节卡号
	private short cardSign;

	public int getCrcFromSum() {

		int mechineVersionSum = 0;
		for (short mv : getMechineVersion()) {
			mechineVersionSum += mv;
		}

		int programVersionSum = 0;
		for (short pv : getProgramVersion()) {
			programVersionSum += pv;
		}

		int maskSum = 0;
		for (Integer m : getMask()) {
			maskSum += m;
		}

		int gatewaySum = 0;
		for (Integer m : getGateway()) {
			gatewaySum += m;
		}

		int mechineIPSum = 0;
		for (Integer mp : getMechineIP()) {
			mechineIPSum += mp;
		}

		int remoteIPSum = 0;
		for (Integer rp : getRemoteIP()) {
			remoteIPSum += rp;
		}

		int mechineStatusPortSum = 0;
		for (short mp : getMechineStatusPort()) {
			mechineStatusPortSum += mp;
		}

		int remoteStatusPortSum = 0;
		for (short rp : getRemoteStatusPort()) {
			remoteStatusPortSum += rp;
		}

		int mechineOrderPortSum = 0;
		for (short rp : getMechineOrderPort()) {
			mechineOrderPortSum += rp;
		}

		int remoteOrderPortSum = 0;
		for (short rp : getRemoteOrderPort()) {
			remoteOrderPortSum += rp;
		}

		int mechineEventPortSum = 0;
		for (short rp : getMechineEventPort()) {
			mechineEventPortSum += rp;
		}

		int remoteEventPortSum = 0;
		for (short rp : getRemoteEventPort()) {
			remoteEventPortSum += rp;
		}

		return mechineVersionSum + programVersionSum + maskSum + gatewaySum
				+ mechineIPSum + remoteIPSum + mechineStatusPortSum
				+ remoteStatusPortSum + mechineOrderPortSum
				+ remoteOrderPortSum + mechineEventPortSum + remoteEventPortSum
				+ cardSign;
	}

	public List<Integer> getMask() {
		return mask;
	}

	public void setMask(List<Integer> mask) {
		this.mask = mask;
	}

	public List<Integer> getGateway() {
		return gateway;
	}

	public void setGateway(List<Integer> gateway) {
		this.gateway = gateway;
	}

	public List<Integer> getMechineIP() {
		return mechineIP;
	}

	public void setMechineIP(List<Integer> mechineIP) {
		this.mechineIP = mechineIP;
	}

	public List<Integer> getRemoteIP() {
		return remoteIP;
	}

	public void setRemoteIP(List<Integer> remoteIP) {
		this.remoteIP = remoteIP;
	}

	public short[] getMechineStatusPort() {
		return mechineStatusPort;
	}

	public void setMechineStatusPort(short[] mechineStatusPort) {
		this.mechineStatusPort = mechineStatusPort;
	}

	public short[] getRemoteStatusPort() {
		return remoteStatusPort;
	}

	public void setRemoteStatusPort(short[] remoteStatusPort) {
		this.remoteStatusPort = remoteStatusPort;
	}

	public short[] getMechineOrderPort() {
		return mechineOrderPort;
	}

	public void setMechineOrderPort(short[] mechineOrderPort) {
		this.mechineOrderPort = mechineOrderPort;
	}

	public short[] getRemoteOrderPort() {
		return remoteOrderPort;
	}

	public void setRemoteOrderPort(short[] remoteOrderPort) {
		this.remoteOrderPort = remoteOrderPort;
	}

	public short[] getMechineEventPort() {
		return mechineEventPort;
	}

	public void setMechineEventPort(short[] mechineEventPort) {
		this.mechineEventPort = mechineEventPort;
	}

	public short[] getRemoteEventPort() {
		return remoteEventPort;
	}

	public void setRemoteEventPort(short[] remoteEventPort) {
		this.remoteEventPort = remoteEventPort;
	}

	public short getCardSign() {
		return cardSign;
	}

	public void setCardSign(short cardSign) {
		this.cardSign = cardSign;
	}

	public short[] getMechineVersion() {
		return mechineVersion;
	}

	public void setMechineVersion(short[] mechineVersion) {
		this.mechineVersion = mechineVersion;
	}

	public short[] getProgramVersion() {
		return programVersion;
	}

	public void setProgramVersion(short[] programVersion) {
		this.programVersion = programVersion;
	}

}
