package com.kuangchi.sdd.consum.handler.serverHandler.ping;

import java.util.Date;
import java.util.Map;

import com.kuangchi.sdd.consum.container.ConnectorFactory;
/**
 * 检查设备是否还在线,对不在线的ctx进行移除
 * 
 * */
public class CheckOnLineThread extends Thread {

	@Override
	public void run() {
		for (Map.Entry<String, Date> entry : ConnectorFactory.channelHandlerContextTimeMap
				.entrySet()) {

			String machine = entry.getKey();

			Date now = new Date();

			Date heartBeatTime = ConnectorFactory.channelHandlerContextTimeMap
					.get(machine);// 上一次心跳时间

			if (heartBeatTime != null
					&& ConnectorFactory.getConnection(machine) != null) {
				Long interval = (now.getTime() - heartBeatTime.getTime()) / 1000;
				if (interval > 16) {// 如果大于10秒

					// 将设备 置为离线和空闲状态
					// ResultMsg2 resultMsg = new ResultMsg2();
					// ResultMsg2 resultMsg2 = new ResultMsg2();
					try {
						/*
						 * String consumUrl = PropertiesToMap.propertyToMap(
						 * "photoncard_interface.properties").get("url") +
						 * "interface/deviceState/modifyOnlineState.do";
						 * 
						 * String str = HttpRequest.sendPost(consumUrl,
						 * "deviceNum=" + machine + "&onlineState=0");//
						 * 调用一卡通的方法将设备设为离线状态 resultMsg = GsonUtil.toBean(str,
						 * ResultMsg2.class);
						 */
						// 如果设备离线，则移除缓存里的设备数据（防止消费终端参数还能设置成功）
						ConnectorFactory.connections.remove(machine);
						// 将设备改为空闲状态
						/*
						 * String consumUrl2 = PropertiesToMap.propertyToMap(
						 * "photoncard_interface.properties").get("url") +
						 * "interface/deviceState/modifyBusyState.do";
						 * 
						 * String str2 = HttpRequest.sendPost(consumUrl2,
						 * "deviceNum=" + machine + "&busyState=0");//
						 * 调用一卡通的方法将设备改为空闲状态 resultMsg2 = GsonUtil.toBean(str2,
						 * ResultMsg2.class);
						 */
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}
}
