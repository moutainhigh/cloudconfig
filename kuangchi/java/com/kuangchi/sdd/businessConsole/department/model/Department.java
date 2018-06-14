package com.kuangchi.sdd.businessConsole.department.model;

import java.sql.Timestamp;

import com.kuangchi.sdd.base.model.BaseModelSupport;
/**
 * 部门
 * @author ccdt
 *
 */
public class Department extends BaseModelSupport {
	private String UUID;//主键
	private String bmDm;//部门代码
	private String BM_NO_TEMP;//部门编号中间列
	private String bmNo;//部门代码
	private String sjbmDm;//上级部门代码
	private String bmznfwDm;//部门职能范围代码
	private String zfBj;//作废标记
	private String bmMc;//部门名称
	private String bmMcJ;//部门名称简
	private String bmfzrDm;//部门负责人用户代码
	private String bz;//备注
	private Timestamp lrSj;//录入时间
	private String lrryDm;//录入人员代码
	private String remoteDepartmentId;//远程C/S的部门Id
	private String remoteParentid;//远程父级部门id
	
	
	public String getRemoteDepartmentId() {
		return remoteDepartmentId;
	}

	public void setRemoteDepartmentId(String remoteDepartmentId) {
		this.remoteDepartmentId = remoteDepartmentId;
	}

	public String getRemoteParentid() {
		return remoteParentid;
	}

	public void setRemoteParentid(String remoteParentid) {
		this.remoteParentid = remoteParentid;
	}

	
	
	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getBmNo() {
		return bmNo;
	}

	public void setBmNo(String bmNo) {
		this.bmNo = bmNo;
	}

	public String getBmDm() {
		return bmDm;
	}

	public void setBmDm(String bmDm) {
		this.bmDm = bmDm;
	}
	public String getBM_NO_TEMP() {
		return BM_NO_TEMP;
	}

	public void setBM_NO_TEMP(String bM_NO_TEMP) {
		BM_NO_TEMP = bM_NO_TEMP;
	}
	public String getSjbmDm() {
		return sjbmDm;
	}

	public void setSjbmDm(String sjbmDm) {
		this.sjbmDm = sjbmDm;
	}

	public String getBmznfwDm() {
		return bmznfwDm;
	}

	public void setBmznfwDm(String bmznfwDm) {
		this.bmznfwDm = bmznfwDm;
	}

	public String getZfBj() {
		return zfBj;
	}

	public void setZfBj(String zfBj) {
		this.zfBj = zfBj;
	}

	public String getBmMc() {
		return bmMc;
	}

	public void setBmMc(String bmMc) {
		this.bmMc = bmMc;
	}

	public String getBmMcJ() {
		return bmMcJ;
	}

	public void setBmMcJ(String bmMcJ) {
		this.bmMcJ = bmMcJ;
	}

	public String getBmfzrDm() {
		return bmfzrDm;
	}

	public void setBmfzrDm(String bmfzrDm) {
		this.bmfzrDm = bmfzrDm;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
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
