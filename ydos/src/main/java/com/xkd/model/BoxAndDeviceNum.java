package com.xkd.model;

/**
 * Create by @author: wanghuijiu; @date: 18-3-3;
 */
public class BoxAndDeviceNum {

    private Integer BoxNum;
    private Integer deviceNum;

    public Integer getBoxNum() {
        if (BoxNum == null){
            return 0;
        }
        return BoxNum;
    }

    public void setBoxNum(Integer boxNum) {
        BoxNum = boxNum;
    }

    public Integer getDeviceNum() {
        if (deviceNum==null){
            return 0;
        }
        return deviceNum;
    }

    public void setDeviceNum(Integer deviceNum) {
        this.deviceNum = deviceNum;
    }
}
