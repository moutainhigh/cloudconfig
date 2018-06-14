package com.kuangchi.sdd.interfaceConsole.dataSynchronize.model;

import java.sql.Timestamp;

public class RoleSyncModel {
	
	private String uuid;
	private String js_Dm;// 角色代码
	private String js_Mc;// 角色名称
	private String bz;// 备注
	private String zf_Bj;// 作废标记
	private Timestamp lr_Sj;// 录入时间
	private String lrry_Dm;// 录入人员代码
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getJs_Dm() {
		return js_Dm;
	}
	public void setJs_Dm(String js_Dm) {
		this.js_Dm = js_Dm;
	}
	public String getJs_Mc() {
		return js_Mc;
	}
	public void setJs_Mc(String js_Mc) {
		this.js_Mc = js_Mc;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getZf_Bj() {
		return zf_Bj;
	}
	public void setZf_Bj(String zf_Bj) {
		this.zf_Bj = zf_Bj;
	}
	public Timestamp getLr_Sj() {
		return lr_Sj;
	}
	public void setLr_Sj(Timestamp lr_Sj) {
		this.lr_Sj = lr_Sj;
	}
	public String getLrry_Dm() {
		return lrry_Dm;
	}
	public void setLrry_Dm(String lrry_Dm) {
		this.lrry_Dm = lrry_Dm;
	}
	
}
