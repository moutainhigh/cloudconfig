package com.kuangchi.sdd.baseConsole.log.quartz;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.baseConsole.log.service.LogService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;


public class OperateLogBackupQuartz {

	LogService logService;
	
	ICronService cronService;
	    
	private String absolutePath;

	
	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}


   public LogService getLogService() {
		return logService;
	}


	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	
	//备份
	public void backup(){
		//集群访问时，只有与数据库中相同的IP地址可以执行页面定时器的业务操作
		boolean r = cronService.compareIP();	
		if(r){
			logService.backupLog(absolutePath);
		}
	}


}
