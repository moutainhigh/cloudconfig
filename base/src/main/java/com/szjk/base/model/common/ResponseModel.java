package com.szjk.base.model.common;

import com.alibaba.fastjson.JSON;

public class ResponseModel<T> {

    private String repCode = "S0000";

    private String repNote = "SUCCESS";

    private String pertain = "gateway";

    //因为前段需要统计页数，加上这个属性
    private Integer total = 0;

    private T rows;

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

    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    public String getRepNote() {
        return repNote;
    }

    public void setRepNote(String repNote) {
        this.repNote = repNote;
    }

    public String getPertain() {
        return pertain;
    }

    public void setPertain(String pertain) {
        this.pertain = pertain;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }

    public Object getResExtra() {
        return resExtra;
    }

    public void setResExtra(Object resExtra) {
        this.resExtra = resExtra;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
