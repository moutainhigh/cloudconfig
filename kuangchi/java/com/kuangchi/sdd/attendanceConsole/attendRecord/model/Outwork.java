package com.kuangchi.sdd.attendanceConsole.attendRecord.model;

public class Outwork {

	private String workno;
	private String name;
	private String begindate;
	private String enddate;
	private String beginhour;//开始时间时分
	private String endhour;//结束时分
	private String kindname;
	private String flagname;
	private String id;//uuid
	private String is_synchronized;//是否是同步数据
	public String getWorkno() {
		return workno;
	}
	public void setWorkno(String workno) {
		this.workno = workno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getBeginhour() {
		return beginhour;
	}
	public void setBeginhour(String beginhour) {
		this.beginhour = beginhour;
	}
	public String getEndhour() {
		return endhour;
	}
	public void setEndhour(String endhour) {
		this.endhour = endhour;
	}
	public String getKindname() {
		return kindname;
	}
	public void setKindname(String kindname) {
		this.kindname = kindname;
	}
	public String getFlagname() {
		return flagname;
	}
	public void setFlagname(String flagname) {
		this.flagname = flagname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIs_synchronized() {
		return is_synchronized;
	}
	public void setIs_synchronized(String is_synchronized) {
		this.is_synchronized = is_synchronized;
	}
	
	
}
