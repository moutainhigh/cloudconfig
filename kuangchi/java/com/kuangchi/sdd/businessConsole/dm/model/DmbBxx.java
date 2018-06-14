package com.kuangchi.sdd.businessConsole.dm.model;

import java.sql.Timestamp;

import com.kuangchi.sdd.base.model.BaseModelSupport;

/**
 * 代码表信息
 * 
 * @author work-admin
 *
 */
public class DmbBxx extends BaseModelSupport {

	private String UUID;
	private String dmbBdmz;// 代码表表代码值
	private String dmbBmc;// 代码表表名称

	private String dcDcBj;// 单层多层标记 0 单层 1 多层

	private String dwDm;// 单位代码 录入人员的所属单位代码

	private String zfBj;// 作废标记

	private Timestamp lrSj;// 录入时间

	private String lrryDm;// 录入人员代码
	
	
	

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getDmbBdmz() {
		return dmbBdmz;
	}

	public void setDmbBdmz(String dmbBdmz) {
		this.dmbBdmz = dmbBdmz;
	}

	public String getDmbBmc() {
		return dmbBmc;
	}

	public void setDmbBmc(String dmbBmc) {
		this.dmbBmc = dmbBmc;
	}

	public String getDcDcBj() {
		return dcDcBj;
	}

	public void setDcDcBj(String dcDcBj) {
		this.dcDcBj = dcDcBj;
	}

	public String getDwDm() {
		return dwDm;
	}

	public void setDwDm(String dwDm) {
		this.dwDm = dwDm;
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
