package com.kuangchi.sdd.elevatorConsole.elevatorReport.model;

public class Record {
	CheckEvent  checkEvent;   //checkEvent 和 triggerEvent 两个属性中最多只有一个非空
    TriggerEvent triggerEvent;
    
	public CheckEvent getCheckEvent() {
		return checkEvent;
	}
	public void setCheckEvent(CheckEvent checkEvent) {
		this.checkEvent = checkEvent;
	}
	public TriggerEvent getTriggerEvent() {
		return triggerEvent;
	}
	public void setTriggerEvent(TriggerEvent triggerEvent) {
		this.triggerEvent = triggerEvent;
	}
	
}
