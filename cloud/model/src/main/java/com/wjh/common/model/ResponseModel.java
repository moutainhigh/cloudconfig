package com.wjh.common.model;

import com.alibaba.fastjson.JSON;

public class ResponseModel {

    private String repCode = "S0000";

    private String repNote = "SUCCESS";

    private String pertain = "gateway";

    //因为前段需要统计页数，加上这个属性
    private Integer totalRows =0;

    private Object resModel;

    private Object resExtra;

    public ResponseModel() {

    }

    public ResponseModel(String repCode, String repNote, String pertain, Integer totalRows, Object resModel, Object resExtra) {
        this.repCode = repCode;
        this.repNote = repNote;
        this.pertain = pertain;
        this.totalRows = totalRows;
        this.resModel = resModel;
        this.resExtra = resExtra;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
