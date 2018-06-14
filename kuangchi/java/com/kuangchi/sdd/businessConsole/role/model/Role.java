package com.kuangchi.sdd.businessConsole.role.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class Role extends BaseModelSupport implements  Serializable{
	private String UUID;
	private String jsDm;// 角色代码
	private String jsMc;// 角色名称
	private String bz;// 备注
	private String zfBj;// 作废标记
	private String nzBj;// 内置标记
	private Timestamp lrSj;// 录入时间
	private String lrryDm;// 录入人员代码
	
	private String mrjsBj;//默认角色标记
	
	

	public String getNzBj() {
		return nzBj;
	}

	public void setNzBj(String nzBj) {
		this.nzBj = nzBj;
	}

	public String getMrjsBj() {
		return mrjsBj;
	}

	public void setMrjsBj(String mrjsBj) {
		this.mrjsBj = mrjsBj;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getJsDm() {
		return jsDm;
	}

	public void setJsDm(String jsDm) {
		this.jsDm = jsDm;
	}

	public String getJsMc() {
		return jsMc;
	}

	public void setJsMc(String jsMc) {
		this.jsMc = jsMc;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
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
