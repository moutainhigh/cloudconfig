package com.kuangchi.sdd.businessConsole.station.model;

import java.sql.Timestamp;

import com.kuangchi.sdd.base.model.BaseModelSupport;

/**
 * 岗位
 * 
 * @author ccdt
 *
 */
public class Station extends BaseModelSupport {
	private String UUID;
	private String gwDm;// 岗位代码
	private String gwMc;// 岗位名称
	private String gwMcJ;// 岗位名称简
	private String bmDm;// 部门代码
	private String bmNo;// 部门代码(显示)
	private String bmMc;
	private String defaultGw;//是否是默认岗位
	private Timestamp lrSj;// 录入时间
	private String lrryDm;// 录入人员代码

	public String getBmNo() {
		return bmNo;
	}

	public void setBmNo(String bmNo) {
		this.bmNo = bmNo;
	}

	public String getDefaultGw() {
		return defaultGw;
	}

	public void setDefaultGw(String defaultGw) {
		this.defaultGw = defaultGw;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getGwDm() {
		return gwDm;
	}

	public void setGwDm(String gwDm) {
		this.gwDm = gwDm;
	}

	public String getGwMc() {
		return gwMc;
	}

	public void setGwMc(String gwMc) {
		this.gwMc = gwMc;
	}

	public String getGwMcJ() {
		return gwMcJ;
	}

	public void setGwMcJ(String gwMcJ) {
		this.gwMcJ = gwMcJ;
	}

	public String getBmDm() {
		return bmDm;
	}

	public void setBmDm(String bmDm) {
		this.bmDm = bmDm;
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

	public String getBmMc() {
		return bmMc;
	}

	public void setBmMc(String bmMc) {
		this.bmMc = bmMc;
	}
}
