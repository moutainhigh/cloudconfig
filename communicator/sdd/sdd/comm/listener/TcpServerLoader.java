package com.kuangchi.sdd.comm.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.kuangchi.sdd.comm.container.Server;

public class TcpServerLoader implements ServletContextListener {
	private Server tcpServer;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	/*	if (null == tcpServer) {
			tcpServer = new Server();
			tcpServer.start(tcpServer);
		}*/
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
