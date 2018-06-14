package com.xkd.model;

import java.util.Date;

public class BankProject {
    private String id;

    private String projectcode;

    private String projectname;

    private String projecttypeid;

    private String coursename;

    private String startdate;

    private String enddate;

    private String trainobject;

    private String trainplace;

    private String username;

    private String feel;

    private String programid;

    private String projectmanager;

    private String dutyPerson;

    private String projecuser;

    private String createdby;

    private Date createdate;

    private String updatedby;

    private Date updatedate;

    private Byte status;

    public String getDutyPerson() {
		return dutyPerson;
	}

	public void setDutyPerson(String dutyPerson) {
		this.dutyPerson = dutyPerson;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProjectcode() {
        return projectcode;
    }

    public void setProjectcode(String projectcode) {
        this.projectcode = projectcode == null ? null : projectcode.trim();
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname == null ? null : projectname.trim();
    }

    public String getProjecttypeid() {
        return projecttypeid;
    }

    public void setProjecttypeid(String projecttypeid) {
        this.projecttypeid = projecttypeid == null ? null : projecttypeid.trim();
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename == null ? null : coursename.trim();
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate == null ? null : startdate.trim();
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate == null ? null : enddate.trim();
    }

    public String getTrainobject() {
        return trainobject;
    }

    public void setTrainobject(String trainobject) {
        this.trainobject = trainobject == null ? null : trainobject.trim();
    }

    public String getTrainplace() {
        return trainplace;
    }

    public void setTrainplace(String trainplace) {
        this.trainplace = trainplace == null ? null : trainplace.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getFeel() {
        return feel;
    }

    public void setFeel(String feel) {
        this.feel = feel == null ? null : feel.trim();
    }

    public String getProgramid() {
        return programid;
    }

    public void setProgramid(String programid) {
        this.programid = programid == null ? null : programid.trim();
    }

    public String getProjectmanager() {
        return projectmanager;
    }

    public void setProjectmanager(String projectmanager) {
        this.projectmanager = projectmanager == null ? null : projectmanager.trim();
    }

    public String getProjecuser() {
        return projecuser;
    }

    public void setProjecuser(String projecuser) {
        this.projecuser = projecuser == null ? null : projecuser.trim();
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby == null ? null : createdby.trim();
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby == null ? null : updatedby.trim();
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}