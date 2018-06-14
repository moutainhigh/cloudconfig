package com.kuangchi.sdd.util.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.elevator.model.Device;
import com.kuangchi.sdd.util.excel.ExcelExportServer;

public class ElevatorDeviceUtil {
	public static final Logger LOG = Logger.getLogger(ElevatorDeviceUtil.class);	
	public static List<String> getAllIp() {
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> interfaceList = NetworkInterface
					.getNetworkInterfaces();

			while (interfaceList.hasMoreElements()) {
				NetworkInterface iface = interfaceList.nextElement();
				LOG.info("Interface " + iface.getName() + ":");
				Enumeration<InetAddress> addrList = iface.getInetAddresses();
				if (!addrList.hasMoreElements()) {
					LOG.info("\t(No address for this address)");
				}
				while (addrList.hasMoreElements()) {
					InetAddress address = addrList.nextElement();
					if (address instanceof Inet4Address) {
						ipList.add(address.getHostAddress());
					}
					LOG.info("本机ip:" + address.getHostAddress());
				}
			}
		} catch (SocketException e) {
			LOG.info("Error getting network interfaces:"
					+ e.getMessage());
			e.printStackTrace();
		}
		return ipList;
	}

	public static boolean compareDevParam(Device srcDevice, Device destDev) {
		if (srcDevice != null && destDev != null) {
			String srcDeviceStr = srcDevice.getComm()
					+ ","
					+ srcDevice.getCommType()
					+ ","
					+ srcDevice.getGateway()
					+ ","
					+ srcDevice.getIp()
					+ ","
					+ srcDevice.getMac()
					+ ","
					+ (srcDevice.getPassword() == null ? "000000" : srcDevice
							.getPassword())
					+ ","
					+ (srcDevice.getSubnet() == null ? "255.255.255.0"
							: srcDevice.getSubnet());

			String destDevStr = destDev.getComm()
					+ ","
					+ destDev.getCommType()
					+ ","
					+ destDev.getGateway()
					+ ","
					+ destDev.getIp()
					+ ","
					+ destDev.getMac()
					+ ","
					+ (destDev.getPassword() == null ? "000000" : destDev
							.getPassword())
					+ ","
					+ (destDev.getSubnet() == null ? "255.255.255.0" : destDev
							.getSubnet());
			if (srcDeviceStr.equals(destDevStr)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static Device setDeviceDefault(Device device) {
		Device dev = new Device();
		if (device != null) {
			dev.setAddress(device.getAddress());
			dev.setComm(device.getComm());
			dev.setCommType(device.getCommType());
			dev.setGateway(device.getGateway());
			dev.setIp(device.getIp());
			dev.setMac(device.getMac());
			dev.setPassword(device.getPassword() == null ? "000000" : device
					.getPassword());
			dev.setPort(device.getPort());
			dev.setSerialNo(device.getSerialNo());
			dev.setSubnet(device.getSubnet() == null ? "255.255.255.0" : device
					.getSubnet());
		}
		return dev;
	}
}
