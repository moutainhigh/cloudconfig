package com.kuangchi.sdd.util.commonUtil;

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

public class SimpleObserver implements Observer {
	
	public static final Logger LOG = Logger.getLogger(SimpleObserver.class);

    private String name;

    public SimpleObserver(String name) {
        this.name = name;
    }

    public void update(Observable o, Object arg) {
        SimpleObservable observable = (SimpleObservable) o;
        LOG.info(name + " found that Data of " + observable.getName() + " has changed to " + observable.getData());
    }
}
