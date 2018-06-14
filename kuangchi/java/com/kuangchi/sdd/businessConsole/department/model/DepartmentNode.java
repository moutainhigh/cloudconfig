package com.kuangchi.sdd.businessConsole.department.model;

/**
 * Created by jianhui.wu on 2016/2/15.
 */
public class DepartmentNode {
    private String UUID;//主键
    private String mc;
    private String bmDm;//部门代码
    private String sjbmDm;//上级部门代码
    private Integer isDepartment=0;
    private String xb;//性别


    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public Integer getIsDepartment() {
        return isDepartment;
    }

    public void setIsDepartment(Integer isDepartment) {
        this.isDepartment = isDepartment;
    }

    public String getBmDm() {
        return bmDm;
    }

    public void setBmDm(String bmDm) {
        this.bmDm = bmDm;
    }

    public String getSjbmDm() {
        return sjbmDm;
    }

    public void setSjbmDm(String sjbmDm) {
        this.sjbmDm = sjbmDm;
    }

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}
    
    
}
