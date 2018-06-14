package com.kuangchi.sdd.base.log;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;



/**
 * 启动关闭监控
 * @author work-admin
 *
 */
public class AppLog implements ServletContextListener{
	
	private static final Logger LOG = Logger.getLogger(AppLog.class);
	private static WebApplicationContext webApplicationContext;


	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		ServletContext servletContext = sce.getServletContext();
		
		webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		
	
		LOG.info("服务器启动" + System.currentTimeMillis());
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOG.info("服务器关闭" + System.currentTimeMillis());

		
	}

}
