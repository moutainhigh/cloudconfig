package com.kuangchi.sdd.elevatorConsole.device.quartz.service.impl;

import java.util.List;

import com.google.gson.Gson;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.elevatorConsole.device.model.CommDevice;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Result;
import com.kuangchi.sdd.elevatorConsole.device.service.ITKDeviceService;
import com.kuangchi.sdd.elevatorConsole.tkComm.service.TkCommService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;

public class TKDeviceStateQuartz {
	ITKDeviceService deviceService;
	private ICronService cronService;

	public ICronService getCronService() {
		return cronService;
	}

	public void setCronService(ICronService cronService) {
		this.cronService = cronService;
	}

	public ITKDeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(ITKDeviceService deviceService) {
		this.deviceService = deviceService;
	}

	private TkCommService tkCommService;

	public TkCommService getTkCommService() {
		return tkCommService;
	}

	public void setTkCommService(TkCommService tkCommService) {
		this.tkCommService = tkCommService;
	}

	/**
	 * @创建人　: xuewen.deng
	 * @创建时间: 2016-11-7
	 * @功能描述: 每隔20秒更新一次梯控设备状态
	 * @参数描述:
	 */
	public void ModifyDeviceState() {
		boolean r = cronService.compareIP();
		if (r) {
			List<Device> allDevice = deviceService.getAllTKDeviceInfo();
			/*
			 * List<LinkedTreeMap> allDevice = gson.fromJson(allDeviceStr,
			 * ArrayList.class);
			 */
			/*
			 * Map<String, String> map = PropertiesToMap
			 * .propertyToMap("comm_interface.properties");
			 */

			if (allDevice != null) {
				for (Device dev : allDevice) {
					String clockUrl = tkCommService.getTkCommUrl(dev
							.getDevice_num()) + "TKDevice/tk_RecvClock.do?";

					CommDevice device = new CommDevice();
					device.setMac(dev.getMac());
					device.setIp(dev.getDevice_ip());
					device.setPort(dev.getDevice_port());
					device.setAddress(dev.getAddress());
					device.setGateway(dev.getNetwork_gateway());
					device.setSerialNo(dev.getDevice_sequence());
					device.setSubnet(dev.getNetwork_mask());
					// 接收设备时钟
					Result result = new Result();

					String clockRespStr = HttpRequest.sendPost(clockUrl,
							"device=" + GsonUtil.toJson(device));

					// String resultStr =
					// TKInterfaceFunctions.KKTK_RecvClock(dev);
					Gson gson = new Gson();
					result = gson.fromJson(clockRespStr, Result.class);

					// 修改设备状态
					if (result != null && result.getResultCode() == 0) {
						// 将接收到时钟的设备置为在线状态
						dev.setOnline_state("1");
						deviceService.updateTKDevcieState(dev);
					} else {
						dev.setOnline_state("0");
						deviceService.updateTKDevcieState(dev);
					}

				}

			}
		}

	}
}
