package com.kuangchi.sdd.businessConsole.menu.model;

import java.sql.Timestamp;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class Menu extends BaseModelSupport {
	private String UUID;
	private String cdDM;// 菜单代码
	private String cdMc;// 菜单名称
	private String cdUrl;// 菜单URL
	private String fcdDm;// 父菜代码
	private String lx;// 类型 1代表菜单 2 代表目录
	private int xspx;// 显示顺序
	private String zfBj;// 作废标记
	private String bz;// 备注
	private Timestamp lrSj;// 录入时间
	private String lrryDm;// 录入人员代码
	private String cdFlag;// 菜单所属的模块标志

	public String getCdFlag() {
		return cdFlag;
	}

	public void setCdFlag(String cdFlag) {
		this.cdFlag = cdFlag;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getCdDM() {
		return cdDM;
	}

	public void setCdDM(String cdDM) {
		this.cdDM = cdDM;
	}

	public String getCdMc() {
		return cdMc;
	}

	public void setCdMc(String cdMc) {
		this.cdMc = cdMc;
	}

	public String getCdUrl() {
		return cdUrl;
	}

	public void setCdUrl(String cdUrl) {
		this.cdUrl = cdUrl;
	}

	public String getFcdDm() {
		return fcdDm;
	}

	public void setFcdDm(String fcdDm) {
		this.fcdDm = fcdDm;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public int getXspx() {
		return xspx;
	}

	public void setXspx(int xspx) {
		this.xspx = xspx;
	}

	public String getZfBj() {
		return zfBj;
	}

	public void setZfBj(String zfBj) {
		this.zfBj = zfBj;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Timestamp getLrSj() {
		return lrSj;
	}

	public void setLrSj(Timestamp lrSj) {
		this.lrSj = lrSj;
	}

	public String getLrryDm() {
		return lrryDm;
	}

	public void setLrryDm(String lrryDm) {
		this.lrryDm = lrryDm;
	}

	private int cdcc;// 菜单层次

	public int getCdcc() {
		return cdcc;
	}

	public void setCdcc(int cdcc) {
		this.cdcc = cdcc;
	}

}
