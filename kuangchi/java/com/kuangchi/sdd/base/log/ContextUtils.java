package com.kuangchi.sdd.base.log;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ContextUtils implements ApplicationContextAware {

	private  ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
		this.context = applicationContext;
	}
	
	
	public  ApplicationContext getContext(){
		return context;
	}

}
