package com.wjh.companydemomodel.model;


import java.io.Serializable;

public class ContentVo {
    private String representative;
    private int score;
    private String manageScope;

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public String getManageScope() {
        return manageScope;
    }

    public void setManageScope(String manageScope) {
        this.manageScope = manageScope;
    }
}
