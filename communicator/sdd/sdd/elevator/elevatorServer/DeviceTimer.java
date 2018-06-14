package com.kuangchi.sdd.elevator.elevatorServer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.consum.bean.Cron;
import com.kuangchi.sdd.elevator.dllInterfaces.TKInterfaceFunctions;
import com.kuangchi.sdd.elevator.model.Device;
import com.kuangchi.sdd.elevator.model.Result;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.network.ElevatorDeviceUtil;
import com.kuangchi.sdd.util.network.HttpRequest;

public class DeviceTimer extends Thread {
	public static final Logger LOG = Logger.getLogger(DeviceTimer.class);
	
	@Override
	public synchronized void run() {

		// 比较ip是否是服务器ip
		// 获取请求的服务器ip
		Enumeration<NetworkInterface> allNetInterfaces;

		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			List<String> arr = new ArrayList<String>();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				// System.out.println(netInterface.getName()); //网关
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						arr.add("'" + ip.getHostAddress() + "'");
					}
				}
			}
			String str = arr.toString().substring(1,
					arr.toString().length() - 1);
			Cron cron = new Cron();
			cron.setSys_value(str);

			String compareServerIP = PropertiesToMap.propertyToMap(
					"photoncard_interface.properties").get("url")
					+ "interface/cDevice/compareIP.do";
			String photoReturnCount = HttpRequest.sendPost(compareServerIP,
					"cron=" + GsonUtil.toJson(cron));

			if (photoReturnCount != null
					&& Integer.valueOf(photoReturnCount) >= 1) {
				LOG.info("~~~~~~本机ip是运行comm的服务器的ip");
				// 将所有的设备状态置为离线
				/*
				 * String offLineUrl = PropertiesToMap.propertyToMap(
				 * "photoncard_interface.properties").get("url") +
				 * "interface/tkDevice/updateAllState.do"; String photoReturnstr
				 * = HttpRequest.sendPost(offLineUrl, "online_state=0");//
				 * 调用一卡通的方法更新全部设备状态（离线）
				 */

				String consumUrl = PropertiesToMap.propertyToMap(
						"photoncard_interface.properties").get("url")
						+ "interface/tkDevice/updateTKDeviceState.do";
				String getAllDevUrl = PropertiesToMap.propertyToMap(
						"photoncard_interface.properties").get("url")
						+ "interface/tkDevice/getAllTKDeviceInfo.do";
				String allDeviceStr = HttpRequest.sendPost(getAllDevUrl,
						"flag=1");// 调用一卡通的方法获取数据库的设备信息
				Gson gson = new Gson();
				List<LinkedTreeMap> allDevice = gson.fromJson(allDeviceStr,
						ArrayList.class);

				List<String> ipList = ElevatorDeviceUtil.getAllIp();
				if (allDevice != null) {
					List<Device> devices = new ArrayList();
					for (int i = 0; i < allDevice.size(); i++) {
						LinkedTreeMap map = allDevice.get(i);
						Device device = new Device();
						device.setAddress(map.get("address").toString());
						device.setComm(map.get("comm").toString());

						device.setCommType(Integer.valueOf(map.get("commType")
								.toString().split("\\.")[0]));
						device.setGateway(map.get("gateway").toString());
						device.setIp(map.get("ip").toString());
						device.setMac(map.get("mac").toString());
						device.setPassword(map.get("password").toString());
						device.setPort(map.get("port").toString());
						device.setSerialNo(map.get("serialNo").toString());
						device.setSubnet(map.get("subnet").toString());

						devices.add(ElevatorDeviceUtil.setDeviceDefault(device));
					}

					for (Device dev : devices) {
						// 接收设备时钟
						Result result = new Result();

						result = TKInterfaceFunctions.KKTK_RecvClock(dev);
						if (result != null && result.getResultCode() == 0) {
							// 将接收到时钟的设备置为在线状态
							String photoReturnstr2 = HttpRequest.sendPost(
									consumUrl, "mac=" + dev.getMac()
											+ "&state=1");// 调用一卡通的方法更新设备状态（在线）
							LOG.info("将设备" + dev.getMac() + "置为在线状态");
						} else {

							// 将接收到时钟的设备置为在线状态
							String photoReturnstr3 = HttpRequest.sendPost(
									consumUrl, "mac=" + dev.getMac()
											+ "&state=0");// 调用一卡通的方法更新设备状态（离线）
							LOG.info("将设备" + dev.getMac() + "置为离线状态");
						}

					}

				}

			} else {
				LOG.info("~~~~~~本机ip不是运行comm服务器的ip");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}
}
