package com.kuangchi.sdd.commConsole.upLoadVersion.service.impl;

import org.apache.commons.codec.binary.Base32;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.Down;
import com.kuangchi.sdd.comm.equipment.common.Over;
import com.kuangchi.sdd.comm.equipment.common.PackageDown;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.upLoadVersion.service.IUpLoadVersionService;

@Service("upLoadVersionServiceImpl")
public class UpLoadVersionServiceImpl implements IUpLoadVersionService {
	public static final Logger LOG = Logger.getLogger(UpLoadVersionServiceImpl.class);
	@Override
	public String downSoftware(String mac, String deviceType) throws Exception {
		// Connector connector = new SetUserGroupConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(deviceType));

		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x50);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);

		SendData sendData = new SendData();
		Down down = new Down();
		sendData.setDown(down);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		// connector.setHeader(pkg);
		GetDevInfoService devInfoService = new GetDevInfoService();
		DeviceInfo2 deviceInfo2 = null;
		deviceInfo2 = devInfoService.getManager(mac);
		LOG.info("容器获取的值:" + null);

		String result = com.kuangchi.sdd.comm.container.Service.service("down",
				pkg, deviceInfo2);
		// String result = connector.run();
		LOG.info("容器获取的值:" + null);
		return result;
	}

	@Override
	public int packageDownSoftware(String mac, String deviceType,
			String packageNum, String isoString) throws Exception {

		long packageSize = 900;
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(deviceType));

		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x51);
		pkg.setLength(Long.valueOf(packageSize).intValue());
		pkg.setOrderStatus(0x00);

		SendData sendData = new SendData();
		PackageDown packageDown = new PackageDown();
		packageDown.setPackageNum(Integer.valueOf(packageNum));
		byte[] bytes = new byte[Long.valueOf(packageSize).intValue()];
		// fis.read(bytes);
		Base32 base32 = new Base32();// 用base32进行编码解码
		bytes = base32.decode(isoString.getBytes("utf-8"));
		packageDown.setBytes(bytes);
		sendData.setPackageDown(packageDown);

		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		// connector.setHeader(pkg);
		GetDevInfoService devInfoService = new GetDevInfoService();
		DeviceInfo2 deviceInfo2 = devInfoService.getManager(mac);
		String result = com.kuangchi.sdd.comm.container.Service.service(
				"package_down", pkg, deviceInfo2);
		if (null == result || "null".equals(result)) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int packageStern(String mac, String deviceType,
			String residueLength, String packageNum, String isoString)
			throws Exception {
		// 尾字节
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(deviceType));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x51);
		pkg.setLength(Integer.valueOf(residueLength) + 2);
		pkg.setOrderStatus(0x00);
		SendData sendData = new SendData();

		PackageDown packageDown = new PackageDown();
		packageDown.setPackageNum(Integer.valueOf(packageNum));
		byte[] bytes = new byte[Integer.valueOf(residueLength)];
		// fis.read(bytes);
		// bytes = isoString.getBytes("ISO-8859-1");
		Base32 base32 = new Base32();// 用base32进行编码解码
		bytes = base32.decode(isoString.getBytes("utf-8"));
		packageDown.setBytes(bytes);
		sendData.setPackageDown(packageDown);

		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		// connector.setHeader(pkg);
		GetDevInfoService devInfoService = new GetDevInfoService();
		DeviceInfo2 deviceInfo2 = devInfoService.getManager(mac);
		String result = com.kuangchi.sdd.comm.container.Service.service(
				"package_down", pkg, deviceInfo2);
		if (null == result || "null".equals(result)) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int overSoftware(String mac, String deviceType) throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(deviceType));

		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x52);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);

		SendData sendData = new SendData();
		Over over = new Over();
		sendData.setOver(over);
		pkg.setData(sendData);

		pkg.setCrc(pkg.getCrcFromSum());
		// connector.setHeader(pkg);
		GetDevInfoService devInfoService = new GetDevInfoService();
		DeviceInfo2 deviceInfo2 = devInfoService.getManager(mac);
		String result = com.kuangchi.sdd.comm.container.Service.service("over",
				pkg, deviceInfo2);
		// String result = connector.run();
		LOG.info("容器获取的值:" + result);
		if (null == result || "null".equals(result)) {
			return 1;
		} else {
			return 0;
		}

	}

}
