package com.kuangchi.sdd.interfaceConsole.dataSynchronize.model;

import java.sql.Timestamp;

/**
 * 添加部门
 * @author chudan.guo
 *@功能描述: 添加部门接口，供C/S端调用，同步数据
 */


public class DepartmentSyncModel {
	private String uuid;//主键
	private String bm_no;//部门编号
	private String bm_dm;//部门代码
	private String sjbm_dm;//上级部门代码
	private String bmznfw_dm;//部门职能范围代码
	private String zf_bj;//作废标记
	private String bm_mc;//部门名称
	private String bm_mc_j;//部门名称简
	private String bmfzr_dm;//部门负责人用户代码
	private String bz;//备注
	private Timestamp lr_sj;//录入时间
	private String lrry_dm;//录入人员代码
	private String remote_department_id;//远端C/S的部门ID
	private String remote_parentId;//远端父级部门ID
	
	
	public String getBm_no() {
		return bm_no;
	}
	public void setBm_no(String bm_no) {
		this.bm_no = bm_no;
	}
	public String getRemote_parentId() {
		return remote_parentId;
	}
	public void setRemote_parentId(String remote_parentId) {
		this.remote_parentId = remote_parentId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getBm_dm() {
		return bm_dm;
	}
	public void setBm_dm(String bm_dm) {
		this.bm_dm = bm_dm;
	}
	public String getSjbm_dm() {
		return sjbm_dm;
	}
	public void setSjbm_dm(String sjbm_dm) {
		this.sjbm_dm = sjbm_dm;
	}
	public String getBmznfw_dm() {
		return bmznfw_dm;
	}
	public void setBmznfw_dm(String bmznfw_dm) {
		this.bmznfw_dm = bmznfw_dm;
	}
	public String getZf_bj() {
		return zf_bj;
	}
	public void setZf_bj(String zf_bj) {
		this.zf_bj = zf_bj;
	}
	public String getBm_mc() {
		return bm_mc;
	}
	public void setBm_mc(String bm_mc) {
		this.bm_mc = bm_mc;
	}
	public String getBm_mc_j() {
		return bm_mc_j;
	}
	public void setBm_mc_j(String bm_mc_j) {
		this.bm_mc_j = bm_mc_j;
	}
	public String getBmfzr_dm() {
		return bmfzr_dm;
	}
	public void setBmfzr_dm(String bmfzr_dm) {
		this.bmfzr_dm = bmfzr_dm;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
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
	public String getRemote_department_id() {
		return remote_department_id;
	}
	public void setRemote_department_id(String remote_department_id) {
		this.remote_department_id = remote_department_id;
	}
	
	
}
