package com.xkd.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SolrLogger {
	
	
	public static Log logger ;
	/**
	 * 将信息写出到日志文件中
	 * @param meesage
	 * @param clazz
	 */
	public static void loggerInfo(String meesage){
		
		if(logger == null){
			
			logger = LogFactory.getLog("Solr");
			
		}
		
		logger.info(meesage);
		
	}
	
}
