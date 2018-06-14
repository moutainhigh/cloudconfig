package com.kuangchi.sdd.interfaceConsole.dataSynchronize.model;

import java.sql.Timestamp;

public class StaffGw {
	private String uuid;//
	private String yh_dm;//员工代码 kc_staff_info表的staff_num
	private String gw_dm;//岗位代码
	private Timestamp lr_sj; //录入时间
	private String lrry_dm;//录入人员代码,取自kc_staff_info表的staff_num
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
	public String getGw_dm() {
		return gw_dm;
	}
	public void setGw_dm(String gw_dm) {
		this.gw_dm = gw_dm;
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
	
	
}
