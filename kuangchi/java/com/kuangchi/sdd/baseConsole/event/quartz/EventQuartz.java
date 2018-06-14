package com.kuangchi.sdd.baseConsole.event.quartz;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.baseConsole.event.service.EventService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;


public class EventQuartz {
	EventService eventService;

    ICronService cronService;
	
	private String absolutePath;

	
	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
      
	
	/**
	 * 备份
	 */
	public void backupEvent(){
		//集群访问时，只有与数据库中相同的IP地址可以执行页面定时器的业务操作
		boolean r = cronService.compareIP();	
		if(r){
			eventService.backupEvent(absolutePath);
		}
	}
	
	public void backupEventWarning(){
		//集群访问时，只有与数据库中相同的IP地址可以执行页面定时器的业务操作
		boolean r = cronService.compareIP();	
		if(r){
			eventService.backupEventWarning(absolutePath);
		}
	}



}
