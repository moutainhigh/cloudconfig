package com.kuangchi.sdd.baseConsole.quartz.service.impl;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.devicePosition.service.DevicePositionService;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.baseConsole.quartz.model.Result;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;

@Service("deviceStateQuartz")
public class DeviceStateQuartz {

	/*@Resource(name = "devicePositionDaoImpl")
	private DevicePositionDao devicePositionDao;*/
	
	public static final Logger LOG = Logger.getLogger(DeviceStateQuartz.class);

	@Resource(name = "devicePositionService")
	private DevicePositionService devicePositionService;
	
	@Resource(name = "cronServiceImpl")
	private ICronService cronService;

	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;

	static Semaphore semaphore = new Semaphore(1);

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-14 上午11:29:14
	 * @功能描述: 定时更新设备在线状态
	 * @参数描述:
	 */
	public void updateDeviceState() {
	  boolean getResource = false; 
	  try {
		  	getResource = semaphore.tryAcquire(1, TimeUnit.SECONDS);
		  }catch(Exception e) {
			e.printStackTrace(); return; 
		  } 
	  if (getResource) {
		 try {
		// 集群访问时，只有与数据库中相同的IP地址可以执行页面定时器的业务操作
		boolean r = cronService.compareIP();
		if (r) {
			List<DeviceInfo> devices = devicePositionService.getDeviceByPic(null);
			for (DeviceInfo device : devices) {
				try {
					String url = mjCommService.getMjCommUrl(device
							.getDevice_num());
					String message = HttpRequest.sendPost(url
							+ "time/getTime.do?",
							"mac=" + device.getDevice_mac() + "&device_type="
									+ device.getDevice_type());
					Result result = GsonUtil.toBean(message, Result.class);
					if (result != null && "0".equals(result.getResult_code())) {
						devicePositionService.updateDeviceState(
								device.getDevice_num(), 0);
					} else {
//						System.out
//								.println("设备离线了哟...设备编号："
//										+ device.getDevice_num() + "url:" + url
//										+ "  result=" + result + "  message:"
//										+ message);
						devicePositionService.updateDeviceState(
								device.getDevice_num(), 1);
					}
				} catch (Exception e) {
					LOG.info("连接异常...门禁设备编号：" + device.getDevice_num());
					devicePositionService.updateDeviceState(device.getDevice_num(),
							1);
				}
			}
		}
		 } catch (Exception e) {
		 e.printStackTrace();
		 } finally {
		 semaphore.release();
		 }
	}
 }

}
