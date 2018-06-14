package com.kuangchi.sdd.businessConsole.user.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class User extends BaseModelSupport implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String UUID;
	private String yhDm;// 用户代码
	private String glyBj;// 超级管理员标记 1代表是 0 代表否
	private String yhMc;// 用户名称
	private String yhMm;// 用户密码
	private String bz;// 备注
	private String sfzhm;// 身份证号码
	private String xb;// 性别
	private String gddh;// 固定电话
	private String sjhm;//手机号码
	private String dzyj;// 电子邮件
	private String zfBj;// 作废标记 1代表作废 0代表否
	private String macdz;// MAC地址
	private String ipdz;// IP地址
	private String imsi;// 手机IMSI
	private String sjldyhDm;// 上级领导用户代码
	private String bmDm;// 主属部门代码
	private String ygxz;// 用工性质 1 主业 2 外包
	private String xh;// 序号
	private Timestamp lrSj;// 录入时间
	private String lrryDm;// 录入人员代码
	
	private String yhNc; // 用户昵称，用于页面展示可修改；用户名作为登录账户不可修改   	by yuman.gao   2016-12-20
	
	/**
	 * 扩展一个字段用于展示用
	 * 含义：用户的默认角色 名称
	 *by gengji.yang
	 * @return
	 */
	private String defaultRoleName;
	

	
	public String getYhNc() {
		return yhNc;
	}

	public void setYhNc(String yhNc) {
		this.yhNc = yhNc;
	}

	public String getDefaultRoleName() {
		return defaultRoleName;
	}

	public void setDefaultRoleName(String defaultRoleName) {
		this.defaultRoleName = defaultRoleName;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getYhDm() {
		return yhDm;
	}

	public void setYhDm(String yhDm) {
		this.yhDm = yhDm;
	}

	public String getGlyBj() {
		return glyBj;
	}

	public void setGlyBj(String glyBj) {
		this.glyBj = glyBj;
	}

	public String getYhMc() {
		return yhMc;
	}

	public void setYhMc(String yhMc) {
		this.yhMc = yhMc;
	}

	public String getYhMm() {
		return yhMm;
	}

	public void setYhMm(String yhMm) {
		this.yhMm = yhMm;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getSfzhm() {
		return sfzhm;
	}

	public void setSfzhm(String sfzhm) {
		this.sfzhm = sfzhm;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getGddh() {
		return gddh;
	}

	public void setGddh(String gddh) {
		this.gddh = gddh;
	}

	public String getDzyj() {
		return dzyj;
	}

	public void setDzyj(String dzyj) {
		this.dzyj = dzyj;
	}

	public String getZfBj() {
		return zfBj;
	}

	public void setZfBj(String zfBj) {
		this.zfBj = zfBj;
	}

	public String getMacdz() {
		return macdz;
	}

	public void setMacdz(String macdz) {
		this.macdz = macdz;
	}

	public String getIpdz() {
		return ipdz;
	}

	public void setIpdz(String ipdz) {
		this.ipdz = ipdz;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getSjldyhDm() {
		return sjldyhDm;
	}

	public void setSjldyhDm(String sjldyhDm) {
		this.sjldyhDm = sjldyhDm;
	}

	public String getBmDm() {
		return bmDm;
	}

	public void setBmDm(String bmDm) {
		this.bmDm = bmDm;
	}

	public String getYgxz() {
		return ygxz;
	}

	public void setYgxz(String ygxz) {
		this.ygxz = ygxz;
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

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	
	
}