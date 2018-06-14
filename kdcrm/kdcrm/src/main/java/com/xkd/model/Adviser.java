package com.xkd.model;

public class Adviser {

	private Integer id;
	
	private Integer userId;
	
	private String adviserName;
	
	private String imgpath;

	private String test;
	
	private Integer ttype;

	private Integer status;

	private Integer level;

	private String ttypeName;
	
	private String mobile;

	private String email;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdviserName() {
		return adviserName;
	}

	public void setAdviserName(String adviserName) {
		this.adviserName = adviserName;
	}

	public Integer getTtype() {
		return ttype;
	}

	public String getImgpath() {
		return imgpath;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getTest() {
		return test;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTtypeName() {
		return ttypeName;
	}

	public void setTtypeName(String ttypeName) {
		this.ttypeName = ttypeName;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public void setTtype(Integer ttype) {
		this.ttype = ttype;
	}
}
