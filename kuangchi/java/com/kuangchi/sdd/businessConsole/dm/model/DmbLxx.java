package com.kuangchi.sdd.businessConsole.dm.model;

import java.sql.Timestamp;

import com.kuangchi.sdd.base.model.BaseModelSupport;

/**
 * 代码表列信息
 * 
 * @author work-admin
 *
 */
public class DmbLxx extends BaseModelSupport {

	private String UUID;

	private String DmbLDmz;// 列代码值

	private String DmbLMc;// 列名称

	private String sfkjBj;// 是否可见 1可见 0不可见

	private int cd;// 长度

	private int xh;// 序号

	private String ywbBdmz;// 代码表代码值

	private String zfBj;// 作废标记

	private Timestamp lrSj;// 录入时间
	private String lrryDm;// 录入人员代码
	
	
	

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getDmbLDmz() {
		return DmbLDmz;
	}

	public void setDmbLDmz(String dmbLDmz) {
		DmbLDmz = dmbLDmz;
	}

	public String getDmbLMc() {
		return DmbLMc;
	}

	public void setDmbLMc(String dmbLMc) {
		DmbLMc = dmbLMc;
	}

	public String getSfkjBj() {
		return sfkjBj;
	}

	public void setSfkjBj(String sfkjBj) {
		this.sfkjBj = sfkjBj;
	}

	public int getCd() {
		return cd;
	}

	public void setCd(int cd) {
		this.cd = cd;
	}

	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}

	public String getYwbBdmz() {
		return ywbBdmz;
	}

	public void setYwbBdmz(String ywbBdmz) {
		this.ywbBdmz = ywbBdmz;
	}

	public String getZfBj() {
		return zfBj;
	}

	public void setZfBj(String zfBj) {
		this.zfBj = zfBj;
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

}
