package com.wjh.common.model;

import com.alibaba.fastjson.JSON;

public class ResponseModel<T> {

    private String repCode = "S0000";

    private String repNote = "SUCCESS";

    private String pertain = "gateway";

    //因为前段需要统计页数，加上这个属性
    private Integer totalRows = 0;

    private T resModel;

    private Object resExtra;

    public ResponseModel() {

    }

    public ResponseModel(String repCode, String repNote, String pertain) {
        this.repCode = pertain + "_" + repCode;
        this.repNote = repNote;
        this.pertain = pertain;
    }


    public String getRepCode() {
        return repCode;
    }

    public String getRepNote() {
        return repNote;
    }

    public String getPertain() {
        return pertain;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public Object getResModel() {
        return resModel;
    }

    public Object getResExtra() {
        return resExtra;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public void setResModel(T resModel) {
        this.resModel = resModel;
    }

    public void setResExtra(Object resExtra) {
        this.resExtra = resExtra;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
