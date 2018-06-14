package com.kuangchi.sdd.doorAccessConsole.quartz;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;


public class VerifyDoorDeviceTimeQuardz {
	
	static Semaphore semaphoreA = new Semaphore(1);
	
	@Resource(name = "cronServiceImpl")
	private ICronService cronService;
	
	@Resource(name = "deviceService")
	DeviceService deviceService;
	
	MjCommService mjCommService;
	
	public MjCommService getMjCommService() {
		return mjCommService;
	}

	public void setMjCommService(MjCommService mjCommService) {
		this.mjCommService = mjCommService;
	}
	
	public void verifyDeviceTime(){
		try {
			boolean r = cronService.compareIP();
			if (r) {
				 List<DeviceInfo> list= deviceService.getOnlineDeviceInfo();//获取全部在线设备
				 if(!list.isEmpty()){
					 for(DeviceInfo deviceInfo:list){
						 deviceService.setDeviceTime2(deviceInfo.getDevice_mac(),deviceInfo.getDevice_type());
						/* if(!"0".equals(result.getResult_code())){
							 verifyTime(deviceInfo.getDevice_mac(),deviceInfo.getDevice_type());
						 }*/
					 }	 
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			semaphoreA.release();
		}
	}
	
	/*校时失败递归校时*/
	/*public void verifyTime(String mac,String type){
		 ResultMsg result = deviceService.setDeviceTime2(mac,type);
		 if(!"0".equals(result.getResult_code())){
			 verifyTime(mac,type);
		 }else{
			 return;
		 }
	}*/
}

