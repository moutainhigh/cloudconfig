package com.xkd.model;

/**
 * @author gaodd
 *  用户表
 */

public class DC_PC_User {


	private String token;
	
	private int id;

	private Integer roleId;
	
	private String uname;
	
	private String password;

	private String mobile;

	private String sex;
	
	private String remark;
	
	private String picAtta;
	
	private String email;
	
	private String weixin;
	
	private String updateDate;

	private String userLogo;

	private String roleName;

	public DC_PC_User(String tel,String weixin) {
		if(null != tel){
			this.mobile = tel;
		}
		if(null != weixin){
			this.weixin = weixin;
		}
	}
	
	public DC_PC_User() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getToken() {
		return token;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRemark() {
		return remark;
	}

	public String getUserLogo() {
		return userLogo;
	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPicAtta() {
		return picAtta;
	}

	public void setPicAtta(String picAtta) {
		this.picAtta = picAtta;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	
	
	
}
