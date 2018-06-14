package com.kuangchi.sdd.elevator.elevatorServer;

import org.springframework.web.context.WebApplicationContext;

public class Server {
	public static WebApplicationContext webApplicationContext;

	private int port;

	public Server() {
		super();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Server(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		/*
		 * // 监测设备在线状态 ScheduledExecutorService executorServiceForTKDeviceState
		 * = Executors .newScheduledThreadPool(1);
		 * executorServiceForTKDeviceState.scheduleAtFixedRate(new
		 * DeviceTimer(), 20, 10, TimeUnit.SECONDS);
		 */
	}

}