package com.xkd.model;

import java.util.Date;

public class BankProgram {
    private String id;

    private String programcode;

    private String programname;

    private String programtypeid;

    private String writedate;

    private String username;

    private String managername;

    private String programuser;

    private String createdby;

    private Date createdate;

    private String updatedby;

    private Date updatedate;

    private Byte status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProgramcode() {
        return programcode;
    }

    public void setProgramcode(String programcode) {
        this.programcode = programcode == null ? null : programcode.trim();
    }

    public String getProgramname() {
        return programname;
    }

    public void setProgramname(String programname) {
        this.programname = programname == null ? null : programname.trim();
    }

    public String getProgramtypeid() {
        return programtypeid;
    }

    public void setProgramtypeid(String programtypeid) {
        this.programtypeid = programtypeid == null ? null : programtypeid.trim();
    }

    public String getWritedate() {
        return writedate;
    }

    public void setWritedate(String writedate) {
        this.writedate = writedate == null ? null : writedate.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getManagername() {
        return managername;
    }

    public void setManagername(String managername) {
        this.managername = managername == null ? null : managername.trim();
    }

    public String getProgramuser() {
        return programuser;
    }

    public void setProgramuser(String programuser) {
        this.programuser = programuser == null ? null : programuser.trim();
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