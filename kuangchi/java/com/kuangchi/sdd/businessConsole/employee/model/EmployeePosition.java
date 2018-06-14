package com.kuangchi.sdd.businessConsole.employee.model;

import org.apache.poi.hssf.record.pivottable.StreamIDRecord;

import java.sql.Timestamp;

/**
 * Created by jianhui.wu on 2016/2/18.
 */
public class EmployeePosition {
    private String UUID;
    private String yhDm;
    private String gwDm;
    private Timestamp lrSj;//录入时间
    private String lrryDm;//录入人员代码

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getYhDm() {
        return yhDm;
    }

    public void setYhDm(String yhDm) {
        this.yhDm = yhDm;
    }

    public String getGwDm() {
        return gwDm;
    }

    public void setGwDm(String gwDm) {
        this.gwDm = gwDm;
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
