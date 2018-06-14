package com.kuangchi.sdd.util.commonUtil;

import java.util.Observable;

public class SimpleObservable extends Observable {

    private int data = 0;

    private String name;

    public SimpleObservable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getData() {
        return data;
    }

    public void setData(int i) {
        if (this.data != i) {

            this.data = i; 

            setChanged();
            notifyObservers();
        }
    }
}
