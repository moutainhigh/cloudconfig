package com.xkd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/22.
 */
public class DeviceGroupTreeNode {
    String id;
    String parentId;
    Integer parentGroupType;

    Integer groupType;
    String name;

    String deviceType;

    String img;
    String imgHover;
    String imgNor;

    Map<String,Object> confMap;

    String boxId;



    List<DeviceGroupTreeNode> childTreeNodeList=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getParentGroupType() {
        return parentGroupType;
    }

    public void setParentGroupType(Integer parentGroupType) {
        this.parentGroupType = parentGroupType;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public List<DeviceGroupTreeNode> getChildTreeNodeList() {
        return childTreeNodeList;
    }

    public void setChildTreeNodeList(List<DeviceGroupTreeNode> childTreeNodeList) {
        this.childTreeNodeList = childTreeNodeList;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgHover() {
        return imgHover;
    }

    public void setImgHover(String imgHover) {
        this.imgHover = imgHover;
    }

    public String getImgNor() {
        return imgNor;
    }

    public void setImgNor(String imgNor) {
        this.imgNor = imgNor;
    }

    public Map<String, Object> getConfMap() {
        return confMap;
    }

    public void setConfMap(Map<String, Object> confMap) {
        this.confMap = confMap;
    }


    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }
}
