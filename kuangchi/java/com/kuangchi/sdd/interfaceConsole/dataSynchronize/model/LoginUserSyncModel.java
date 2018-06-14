package com.kuangchi.sdd.interfaceConsole.dataSynchronize.model;

import java.sql.Timestamp;

/**
 * 添加用户
 * @author chudan.guo
 *@功能描述: 添加用户接口，供C/S端调用，同步数据
 */
public class LoginUserSyncModel {
	private String uuid;//主键
	private String yh_dm;//用户代码
	private String gly_bj;//超级管理员标记 1代表是 0 代表否
	private String yh_mc;//用户名称
	private String yh_mm;//用户密码
	private String age;//年龄
	private String bz;//备注
	private String sfzhm;//身份证号码
	private String xb;//性别
	private String gddh;//固定电话
	private String sjhm;//手机号码
	private String dzyj;//电子邮件
	private String zf_bj;//作废标记 1代表作废 0代表否
	private String macdz;//MAC地址
	private String ipdz;//IP地址
	private String imsi;//手机IMSI
	private String sjldyh_dm;//上级领导用户代码
	private String bm_dm;//主属部门代码
	private String ygxz;//用工性质 1 主业 2 外包
	private Integer xh;//序号
	private Timestamp lr_sj;//录入时间
	private String lrry_dm;//录入人员代码
	private String remote_user_id;//远端C/S的用户ID
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
	public String getGly_bj() {
		return gly_bj;
	}
	public void setGly_bj(String gly_bj) {
		this.gly_bj = gly_bj;
	}
	public String getYh_mc() {
		return yh_mc;
	}
	public void setYh_mc(String yh_mc) {
		this.yh_mc = yh_mc;
	}
	public String getYh_mm() {
		return yh_mm;
	}
	public void setYh_mm(String yh_mm) {
		this.yh_mm = yh_mm;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getSfzhm() {
		return sfzhm;
	}
	public void setSfzhm(String sfzhm) {
		this.sfzhm = sfzhm;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	public String getGddh() {
		return gddh;
	}
	public void setGddh(String gddh) {
		this.gddh = gddh;
	}
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public String getDzyj() {
		return dzyj;
	}
	public void setDzyj(String dzyj) {
		this.dzyj = dzyj;
	}
	public String getZf_bj() {
		return zf_bj;
	}
	public void setZf_bj(String zf_bj) {
		this.zf_bj = zf_bj;
	}
	public String getMacdz() {
		return macdz;
	}
	public void setMacdz(String macdz) {
		this.macdz = macdz;
	}
	public String getIpdz() {
		return ipdz;
	}
	public void setIpdz(String ipdz) {
		this.ipdz = ipdz;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getSjldyh_dm() {
		return sjldyh_dm;
	}
	public void setSjldyh_dm(String sjldyh_dm) {
		this.sjldyh_dm = sjldyh_dm;
	}
	public String getBm_dm() {
		return bm_dm;
	}
	public void setBm_dm(String bm_dm) {
		this.bm_dm = bm_dm;
	}
	public String getYgxz() {
		return ygxz;
	}
	public void setYgxz(String ygxz) {
		this.ygxz = ygxz;
	}
	public Integer getXh() {
		return xh;
	}
	public void setXh(Integer xh) {
		this.xh = xh;
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
	public String getRemote_user_id() {
		return remote_user_id;
	}
	public void setRemote_user_id(String remote_user_id) {
		this.remote_user_id = remote_user_id;
	}
	
	
}
