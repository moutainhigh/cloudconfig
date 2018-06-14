package com.kuangchi.sdd.base.aop;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

public class LogAspect {
	
	private static final Logger LOG = Logger.getLogger(LogAspect.class);


	public void logBefore(JoinPoint point){
		Date date=new Date();
		if(!point.getSignature().toString().contains("compareIP")){
			LOG.info(Long.valueOf(date.getTime()).toString()+"|"+point.getSignature()+"|"+point.getTarget().hashCode()+"|0"+"|S");				
		}
	}
	public void logAfter(JoinPoint point){
		Date date=new Date();
		if(!point.getSignature().toString().contains("compareIP")){
				LOG.info(Long.valueOf(date.getTime()).toString()+"|"+point.getSignature()+"|"+point.getTarget().hashCode()+"|1"+"|S");
		}
	}
}
