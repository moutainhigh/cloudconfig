package com.kuangchi.sdd.consumeConsole.device.quardz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
import com.kuangchi.sdd.consumeConsole.xfComm.service.XfCommService;
import com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.service.DeviceStateService;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;

public class CheckDeviceStateQuartz {

	private ICronService cronService;
	@Resource(name = "cDeviceService")
	IDeviceService deviceService;
	@Resource(name = "deviceStateServiceImpl")
	private DeviceStateService deviceStateService;
	static Semaphore semaphoreXFD = new Semaphore(1);

	public ICronService getCronService() {
		return cronService;
	}

	public void setCronService(ICronService cronService) {
		this.cronService = cronService;
	}

	private XfCommService xfCommService;

	public XfCommService getXfCommService() {
		return xfCommService;
	}

	public void setXfCommService(XfCommService xfCommService) {
		this.xfCommService = xfCommService;
	}

	/**
	 * 修改消费设备状态
	 * 
	 * @author xuewen.deng
	 */
	public void checkDeviceStateT() {
		boolean getResource = false;
		try {
			getResource = semaphoreXFD.tryAcquire(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (getResource) {

			try {
				// 集群访问时，只有与数据库中相同的IP地址可以执行定时器的业务操作
				boolean r = cronService.compareIP();
				if (r) {
					List<Map> commIpList = xfCommService.getXfCommIp();// 获取所有的通讯服务器的ip
					List<String> devNumList = deviceService.getAllDevNum();// 获取所有的消费设备编号
					if (commIpList == null || commIpList.size() <= 0) {
						for (String device_num : devNumList) {
							// 将设备状态改为离线空闲
							deviceStateService.modifyOnlineState(device_num, 0);
							deviceStateService.modifyBusyState(device_num, 0);
						}
					}
					// 以下算法的作用是确保所有的通讯服务器中只要有一个说设备在线，则将设备设为在线
					Map<String, String> devAndStateMap = new HashMap<String, String>();// 存放设备编号和设备状态
					for (String device_num : devNumList) {
						Map<String, String> devCommIp = deviceService
								.getCommIPByDevNum(device_num);
						if (devCommIp == null || devCommIp.size() <= 0
								|| "".equals(devCommIp.get("ip"))) {// 判断设备有无绑定commIp，若无绑定IP，则直接全局搜索，若有绑定IP，则固定搜索
							for (Map commIpMap : commIpList) {// 遍历所有的通讯服务器
								String commUrl = "http://"
										+ commIpMap.get("ip")
										+ ":"
										+ commIpMap.get("port")
										+ "/comm/CDevice/checkOnLineT.do?device_num="
										+ device_num;// 通讯服务器URL
								long starTime = System.currentTimeMillis();// 执行开始时间点
								String clockRespStr = HttpRequest.sendPost(
										commUrl, "flag=1");// 调用通讯服务器接口
								long endTime = System.currentTimeMillis();// 执行结束时间点
								long time = endTime - starTime;// 执行时间差/毫秒
								if ("0".equals(clockRespStr)) {// 检测到在线
									devAndStateMap.put(device_num, "1");// 置为在线状态
									deviceStateService.modifyDevCommIp(
											device_num,
											commIpMap.get("id") == null ? ""
													: "" + commIpMap.get("id"));// 修改设备绑定的通讯服务器IP（commIp）
								} else {
									if ("1".equals(devAndStateMap
											.get(device_num))) {// 如果设备在线则跳过（确保所有的通讯服务器中只要有一个说设备在线，则将设备设为在线）
									} else {// 如果设备不在线则设为不在线
										devAndStateMap.put(device_num, "0");// 置为离线状态
									}

									if (time > 15000) {
										commIpList.remove(commIpMap);// 若执行时间差大于15秒，则说明此通讯服务器不存在或没启动，直接移除，防止下次还继续请求导致线程执行缓慢
										break;
									}
								}

							}
						}// if
						else {// 若设备有绑定IP（commIp）
								// 遍历所有的通讯服务器
							String commUrl = "http://"
									+ devCommIp.get("ip")
									+ ":"
									+ devCommIp.get("port")
									+ "/comm/CDevice/checkOnLineT.do?device_num="
									+ device_num;// 通讯服务器URL
							long starTime = System.currentTimeMillis();// 执行开始时间点
							String clockRespStr = HttpRequest.sendPost(commUrl,
									"flag=1");// 远程调用接口
							long endTime = System.currentTimeMillis();// 执行结束时间点
							long time = endTime - starTime;// 执行时间差/毫秒
							if ("0".equals(clockRespStr)) {// 检测到在线
								devAndStateMap.put(device_num, "1");// 置为在线状态
							} else {// 此通讯服务器上检测到设备不在线，则全局搜索

								for (Map commIpMap : commIpList) {// 遍历所有的通讯服务器
									String commUrl2 = "http://"
											+ commIpMap.get("ip")
											+ ":"
											+ commIpMap.get("port")
											+ "/comm/CDevice/checkOnLineT.do?device_num="
											+ device_num;// 通讯服务器URL
									long starTime2 = System.currentTimeMillis();// 执行开始时间点
									String clockRespStr2 = HttpRequest
											.sendPost(commUrl2, "flag=1");// 调用通讯服务器接口
									long endTime2 = System.currentTimeMillis();
									long time2 = endTime2 - starTime2;// 执行结束时间点
									if ("0".equals(clockRespStr2)) {// 检测到在线
										devAndStateMap.put(device_num, "1");// 置为在线状态
										deviceStateService
												.modifyDevCommIp(
														device_num,
														commIpMap.get("id") == null ? ""
																: ""
																		+ commIpMap
																				.get("id"));// 修改设备绑定的通讯服务器IP（commIp）
									} else {
										if ("1".equals(devAndStateMap
												.get(device_num))) {// 如果设备在之前的通讯服务器在线过则跳过（确保所有的通讯服务器中只要有一个说设备在线，则将设备设为在线）
										} else {// 如果设备不在线则设为不在线
											devAndStateMap.put(device_num, "0");// 置为离线状态
										}

										if (time2 > 15000) {// 若执行时间差大于15秒，则说明此通讯服务器不存在或没启动，直接移除，防止下次还继续请求导致线程执行缓慢
											commIpList.remove(commIpMap);
											break;
										}
									}
								}
							}
						}
					}
					// 批量修改消费设备状态
					for (Map.Entry<String, String> devAndStateEntry : devAndStateMap
							.entrySet()) {
						deviceStateService.modifyOnlineState(
								devAndStateEntry.getKey(),
								Integer.valueOf(devAndStateEntry.getValue()));
						deviceStateService.modifyBusyState(
								devAndStateEntry.getKey(),
								Integer.valueOf(devAndStateEntry.getValue()));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				semaphoreXFD.release();
			}
		}
	}

}
