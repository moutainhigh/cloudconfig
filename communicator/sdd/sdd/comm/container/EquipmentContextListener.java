package com.kuangchi.sdd.comm.container;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.consum.bean.ResultMsg2;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.network.HttpRequest;
//启动所有监听程序
public class EquipmentContextListener implements ServletContextListener {
	private ServletContext context = null;
	public static final Logger LOG = Logger.getLogger(EquipmentContextListener.class);
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		// 读取配置文件，初始化Manager的IP和端口等信息
		Map<String, String> managerConfigMap = new HashMap<String, String>();
		managerConfigMap = PropertiesToMap.propertyToMap("manager.properties");
		Manager.broadcastPort = Integer.valueOf(managerConfigMap
				.get("broadcastPort"));
		Manager.localPort = Integer.valueOf(managerConfigMap.get("localPort"));
		/* Manager.broadcastIP = managerConfigMap.get("broadcastIP"); */
		Manager.localHostIP = managerConfigMap.get("localHostIP");
		// Manager.equipmentIP = managerConfigMap.get("equipmentIP");
		Manager.equipmentOrderPort = Integer.valueOf(managerConfigMap
				.get("equipmentOrderPort"));
		// Manager.equipmentRecordPort = Integer.valueOf(managerConfigMap
		// .get("equipmentRecordPort"));
		Manager.equipmentStatusPort = Integer.valueOf(managerConfigMap
				.get("equipmentStatusPort"));
		Manager.equipmentEventPort = Integer.valueOf(managerConfigMap
				.get("equipmentEventPort"));

		// 启动门禁控制器状态监听线程
		new Thread() {

			@Override
			public void run() {

				Server server = new Server();
				server.startStatusUpload(server);

			}
		}.start();

		// 启动门禁控制器刷卡记录监听线程
		new Thread() {

			@Override
			public void run() {

				Server server = new Server();
				server.startRecordUpload(server);

			}
		}.start();

		// 获取spring容器,webcontext 初始化完之后，通过WebApplicationContextUtils获取spring容器
		com.kuangchi.sdd.consum.handler.serverHandler.ping.Server.webApplicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext());
		// 启动消费设备监控线程
		new Thread() {
			public void run() {
				try {
					new com.kuangchi.sdd.consum.handler.serverHandler.ping.Server(
							9000).run();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
		// 启动梯控设备监控线程
		new Thread() {
			public void run() {
				try {
					new com.kuangchi.sdd.elevator.elevatorServer.Server().run();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();

		// 用户可自定义监听端口
		
	/*	String commandPortStr = PropertiesToMap.propertyToMap("photon_lock.properties")
				.get("command_port"); 
		String reportPortStr = PropertiesToMap.propertyToMap("photon_lock.properties")
				.get("report_port"); 
		
		final int commandPort = Integer.parseInt(commandPortStr);
	  	final int reportPort = Integer.parseInt(reportPortStr);*/
		
		// 启动光子锁监听线程
		new Thread() {
			@Override
			public void run() {
				try {
					new com.kuangchi.sdd.zigbee.handler.Server(3333).run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		// 启动光子锁监听线程
		new Thread() {
			@Override
			public void run() {
				try {
					new com.kuangchi.sdd.zigbee.handler.RecordServer(4444)
							.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		// 修改消费设备状态
		new Thread() {
			public void run() {
				try {
					String consumUrl = PropertiesToMap.propertyToMap(
							"photoncard_interface.properties").get("url")
							+ "interface/deviceState/initDevState.do";

					// 调用一卡通的方法将设备设为离线和空闲状态
					String str = HttpRequest.sendPost(consumUrl, "flag=1");//

					/*
					 * String consumUrl2 = PropertiesToMap.propertyToMap(
					 * "photoncard_interface.properties").get("url") +
					 * "interface/cDevice/updateTaskRunState.do";
					 * 
					 * // 调用一卡通的方法将下发任务改为未运行状态 String str2 =
					 * HttpRequest.sendPost(consumUrl2, null);//
					 */
					ResultMsg2 resultMsg = GsonUtil.toBean(str,
							ResultMsg2.class);
					// if ("0".equals(str2) &&
					// "0".equals(resultMsg.getResult())) {
					if ("0".equals(resultMsg.getResult())) {
						LOG.info("++++++++++++++通讯服务器启动初始化设备状态结果：success");
					} else {
						LOG.info("++++++++++++++通讯服务器启动初始化设备状态结果：fail");
					}
				} catch (Exception e) {
					e.printStackTrace();
					LOG.info("++++++++++++++通讯服务器启动初始化设备状态结果：fail");
				}

			};
		}.start();

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
