package com.kuangchi.sdd.businessConsole.employee.model;

/**
 * Created by jianhui.wu on 2016/2/15.
 */
public class Employee {

	private String UUID;
	private String yhNo;// 显示的员工工号（可变）
	private String staff_no_temp;//员工工号中间列
	private String yhDm;// 员工编号（不可变）
	private String yhMc;// 员工名称
	private String bmDm;// 主属部门代码
	private String bmNo; // 部门代码（显示）
	private String bmMc; // 部门名称
	private String gwDm; // 岗位代码
	private String gwmc;// 岗位名称
	private String sjldyhNo;// 上级领导工号
	private String sjldyhDm;// 上级领导用户代码
	private String sjldyhMc; // 上级领导名称
	private String zjbh;// 证件类型
	private String zjmc;// 证件名称
	private String sfzhm;// 证件号码
	private String xb;// 性别
	private String xbMc; // 性别名称
	private String sjhm;// 手机号码
	private String gddh;// 固定电话
	private String dzyj;// 电子邮箱
	private String rzsj;// 入职时间
	private String nl;// 年龄
	private String ygzz;// 员工住址
	private String ygtx;// 员工头像
	private String zfBj;// 作废标记 1代表作废 0代表否
	private String staff_hire_state;// 职位状态
	private String bz;// 备注
	private String roomNum; //房间号
	
	

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getStaff_hire_state() {
		return staff_hire_state;
	}

	public void setStaff_hire_state(String staff_hire_state) {
		this.staff_hire_state = staff_hire_state;
	}

	public String getBmNo() {
		return bmNo;
	}

	public void setBmNo(String bmNo) {
		this.bmNo = bmNo;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String UUID) {
		this.UUID = UUID;
	}

	public String getYhNo() {
		return yhNo;
	}

	public void setYhNo(String yhNo) {
		this.yhNo = yhNo;
	}
	public String getStaff_no_temp() {
		return staff_no_temp;
	}

	public void setStaff_no_temp(String staff_no_temp) {
		this.staff_no_temp = staff_no_temp;
	}
	public String getYhDm() {
		return yhDm;
	}

	public void setYhDm(String yhDm) {
		this.yhDm = yhDm;
	}

	public String getYhMc() {
		return yhMc;
	}

	public void setYhMc(String yhMc) {
		this.yhMc = yhMc;
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

	public String getZfBj() {
		return zfBj;
	}

	public void setZfBj(String zfBj) {
		this.zfBj = zfBj;
	}

	public String getSjldyhNo() {
		return sjldyhNo;
	}

	public void setSjldyhNo(String sjldyhNo) {
		this.sjldyhNo = sjldyhNo;
	}

	public String getSjldyhDm() {
		return sjldyhDm;
	}

	public void setSjldyhDm(String sjldyhDm) {
		this.sjldyhDm = sjldyhDm;
	}

	public String getBmDm() {
		return bmDm;
	}

	public void setBmDm(String bmDm) {
		this.bmDm = bmDm;
	}

	public String getGwDm() {
		return gwDm;
	}

	public void setGwDm(String gwDm) {
		this.gwDm = gwDm;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getYgzz() {
		return ygzz;
	}

	public void setYgzz(String ygzz) {
		this.ygzz = ygzz;
	}

	public String getZjbh() {
		return zjbh;
	}

	public void setZjbh(String zjbh) {
		this.zjbh = zjbh;
	}

	public String getYgtx() {
		return ygtx;
	}

	public void setYgtx(String ygtx) {
		this.ygtx = ygtx;
	}

	public String getRzsj() {
		return rzsj;
	}

	public void setRzsj(String rzsj) {
		this.rzsj = rzsj;
	}

	public String getZjmc() {
		return zjmc;
	}

	public void setZjmc(String zjmc) {
		this.zjmc = zjmc;
	}

	public String getGwmc() {
		return gwmc;
	}

	public void setGwmc(String gwmc) {
		this.gwmc = gwmc;
	}

	public String getSjldyhMc() {
		return sjldyhMc;
	}

	public void setSjldyhMc(String sjldyhMc) {
		this.sjldyhMc = sjldyhMc;
	}

	public String getBmMc() {
		return bmMc;
	}

	public void setBmMc(String bmMc) {
		this.bmMc = bmMc;
	}

	public String getXbMc() {
		return xbMc;
	}

	public void setXbMc(String xbMc) {
		this.xbMc = xbMc;
	}

}
