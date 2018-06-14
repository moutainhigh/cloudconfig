package com.kuangchi.sdd.base.aop;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

public class LogAspect {
	
	private static final Logger LOG = Logger.getLogger(LogAspect.class);


	public void log(JoinPoint point){
		Object[] param = point.getArgs();
		
		Method method = null;
		
		String methodName = point.getSignature().getName();
		
		Class targetClass = point.getTarget().getClass();
		
		try {
			Class[] paramClass = new Class[param.length];
			
			for(int i=0;i<param.length;i++){
				paramClass[i] = param[i].getClass();
			}
			method = targetClass.getMethod(methodName, paramClass);
		
			if(null != method){
				boolean hasAnnotation = method.isAnnotationPresent(Log.class);
				
				if(hasAnnotation){
					Log anLog = method.getAnnotation(Log.class);
					
					LOG.info("操作类型" + anLog.operationType() + "操作名称" +anLog.operationName() );
				}
			}
		
		} catch (SecurityException e) {
			
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			
			e.printStackTrace();
		}
		
	}
}
