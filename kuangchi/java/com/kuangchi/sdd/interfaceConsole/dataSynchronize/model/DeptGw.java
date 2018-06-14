package com.kuangchi.sdd.interfaceConsole.dataSynchronize.model;

import java.sql.Timestamp;

import org.apache.commons.net.ntp.TimeStamp;

public class DeptGw {
	private String uuid;
	private String gw_dm;
	private String gw_mc;
	private String gw_mc_j;
	private String bm_dm;
	private Timestamp lr_sj;
	private String lrry_dm;
	private String default_gw;//是否是默认岗位（0是，1不是）
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getGw_dm() {
		return gw_dm;
	}
	public void setGw_dm(String gw_dm) {
		this.gw_dm = gw_dm;
	}
	public String getGw_mc() {
		return gw_mc;
	}
	public void setGw_mc(String gw_mc) {
		this.gw_mc = gw_mc;
	}
	public String getGw_mc_j() {
		return gw_mc_j;
	}
	public void setGw_mc_j(String gw_mc_j) {
		this.gw_mc_j = gw_mc_j;
	}
	public String getBm_dm() {
		return bm_dm;
	}
	public void setBm_dm(String bm_dm) {
		this.bm_dm = bm_dm;
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
	public String getDefault_gw() {
		return default_gw;
	}
	public void setDefault_gw(String default_gw) {
		this.default_gw = default_gw;
	}
	
}
