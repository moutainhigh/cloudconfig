package com.kuangchi.sdd.interfaceConsole.dataSynchronize.model;

import java.sql.Timestamp;

public class UserRole {
	private String uuid;
	private String yh_dm;
	private String js_dm;
	private Timestamp lr_sj;
	private String lrry_dm;
	private String mrjs_bj;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getYh_dm() {
		return yh_dm;
	}
	public void setYh_dm(String yh_dm) {
		this.yh_dm = yh_dm;
	}
	public String getJs_dm() {
		return js_dm;
	}
	public void setJs_dm(String js_dm) {
		this.js_dm = js_dm;
	}
	public Timestamp getLr_sj() {
		return lr_sj;
	}
	public void setLr_sj(Timestamp lr_sj) {
		this.lr_sj = lr_sj;
	}
	public String getLrry_dm() {
		return lrry_dm;
	}
	public void setLrry_dm(String lrry_dm) {
		this.lrry_dm = lrry_dm;
	}
	public String getMrjs_bj() {
		return mrjs_bj;
	}
	public void setMrjs_bj(String mrjs_bj) {
		this.mrjs_bj = mrjs_bj;
	}
	
	
}
