package com.wjh.companydemomodel.model;


import java.io.Serializable;

public class ContentVo implements Serializable {
    private String contactor;
    private int score;

    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
