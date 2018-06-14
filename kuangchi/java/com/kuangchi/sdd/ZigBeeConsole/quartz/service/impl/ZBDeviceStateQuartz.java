package com.kuangchi.sdd.ZigBeeConsole.quartz.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.ZigBeeConsole.device.model.ZBdeviceModel;
import com.kuangchi.sdd.ZigBeeConsole.device.service.IZBdeviceService;
import com.kuangchi.sdd.ZigBeeConsole.device.util.ObjectSephamorePool;
import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;
import com.kuangchi.sdd.ZigBeeConsole.gateway.service.IGatewayService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;


@Service("zBDeviceStateQuartz")
public class ZBDeviceStateQuartz {
	
	public static final Logger LOG = Logger.getLogger(ZBDeviceStateQuartz.class);

	@Resource(name = "zBdeviceServiceImpl")
	private IZBdeviceService zbdeviceService;
	
	@Resource(name="gatewayServiceImpl")
	private IGatewayService gatewayService;
	

	@Resource(name="cronServiceImpl")
	private ICronService cronService;
	
	static Semaphore semaphore=new Semaphore(1);
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-19 上午13:29:14
	 * @功能描述: 定时更新光子锁设备在线状态
	 * @参数描述:
	 */
	public void updateZBDeviceState() {
		boolean getResource=false;
		try {
			getResource=semaphore.tryAcquire(3,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		if(getResource){
			try {
				boolean r = cronService.compareIP();	
				if(r){
					List<GatewayModel> gatewayList = zbdeviceService.getZBgateways();
					if(gatewayList != null && gatewayList.size()!=0){
						for (GatewayModel gatewayModel : gatewayList) {
							try {
								//调用panid接口查询网关是否在线
				 				String url="http://"+gatewayModel.getRemote_ip()+":"+
				 						gatewayModel.getRemote_port();
				 				String data="remoteIp="+gatewayModel.getGateway()+"&port="+gatewayModel.getGateway_port();
				 				String str = HttpRequest.sendPost(url + "/comm/zigBee/getPanId.do?", data);
				 				
				 				if (str != null && !"".equals(str) && !"null".equals(str)){
				 					Map gatewayMap = GsonUtil.toBean(str, HashMap.class);
				 					if(gatewayMap.get("zigBeeId") != null){
				 						
				 						// 若网关pan_id与库中不同，则重新设置pan_id（因导入网关时pan_id会缓存到库中，故需再做判断）
				 						String pan_id = gatewayModel.getGateway_pan_id();
				 						String old_pan_id = String.valueOf(Integer.parseInt((String)gatewayMap.get("panId"), 16));
				 						if(!pan_id.equals(old_pan_id)){
				 							pan_id=Integer.toHexString(Integer.valueOf(pan_id));
							         		String pasParam = "0000";
							         		pan_id = pasParam.substring(0, 4-pan_id.length()) + pan_id;
							        		
							 				String setPanIdData="remoteIp="+gatewayModel.getGateway()+"&port="+gatewayModel.getGateway_port()+
							 						"&zigBeeId="+gatewayModel.getZigbee_id()+"&panId="+pan_id;
							 				HttpRequest.sendPost(url + "/comm/zigBee/setPanId.do?", setPanIdData); //返回锁ID和状态位0x00或0x01
							 				try {
							 		            Thread.sleep(10000);   // 设置后会重启网关，因此需等待3秒钟
							 		        } catch (InterruptedException e) {
							 		            e.printStackTrace(); 
							 		        }
							 			
				 						}
				 						
				 						//更新网关信息表的在线状态
//										gatewayModel.setZigbee_id(gatewayMap.get("zigBeeId").toString());//网关芯片id
//										gatewayModel.setGateway_pan_id(String.valueOf(Integer.parseInt((String)gatewayMap.get("panId"), 16)));//panid
										gatewayModel.setState("0");
										gatewayService.getNoStateOnline(gatewayModel);
										
										//查询该网关下设备信息，查询设备是否在线
										List<ZBdeviceModel> deviceList=zbdeviceService.getGatewayAndDevice(gatewayModel.getGateway_id());
										if(deviceList != null && deviceList.size() != 0){
											for (ZBdeviceModel deviceModel : deviceList) {
												Semaphore semaphore = ObjectSephamorePool.getSemaphore(deviceModel.getDevice_id());
												try {
													getResource=semaphore.tryAcquire(2,TimeUnit.SECONDS);
												} catch (Exception e) {
													 e.printStackTrace();
													 return;
												}
												if(getResource){
													try {
														String urls="http://" + gatewayModel.getRemote_ip() + ":"+
																gatewayModel.getRemote_port() + "/comm/zigBee/getLockState.do?";
														String datas="remoteIp="+gatewayModel.getGateway() + "&port=" + gatewayModel.getGateway_port()+
																"&lock_id=" + deviceModel.getDevice_id();
														String devMsg = HttpRequest.sendPost(urls, datas);
														
														// 更新该设备状态
														if(devMsg != null && !"".equals(devMsg) && !"null".equals(devMsg)){
															Map deviceMap = GsonUtil.toBean(devMsg, HashMap.class);
															
															//更新设备信息表的在线状态
															deviceModel.setRoom_num((String)deviceMap.get("roomNo"));
															deviceModel.setGateway_pan_id(String.valueOf(Integer.parseInt(deviceMap.get("panId").toString(), 16)));//panid
															deviceModel.setElectricity((String)deviceMap.get("bateryBalance"));//电量
															deviceModel.setDevice_signal(((Double)deviceMap.get("signalLevel")).toString());//信号强度
															
															deviceModel.setRemote_ip(gatewayModel.getRemote_ip());
															//deviceModel.setZigbee_id(gatewayModel.getZigbee_id());
															deviceModel.setZigbee_id(gatewayMap.get("zigBeeId").toString());//网关芯片id
															
															BigDecimal bd = new BigDecimal(((Double)deviceMap.get("time")).toString()); 
															String time = bd.toPlainString();
															Long times=Long.valueOf(time)*1000;
															Date d = new Date(times);
															SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
															deviceModel.setTime_stamp(sf.format(d));//时间戳
															
															Integer Community_num = ((Double)deviceMap.get("communityNo")).intValue();
															if(Community_num != 65535){
																deviceModel.setCommunity_num(Community_num);
															}
															
															deviceModel.setDevice_state("0");
															zbdeviceService.updateZbDeviceInfos(deviceModel);
															
														} else {
															deviceModel.setDevice_state("1");
															zbdeviceService.setDevStateByID(deviceModel);
														}
													} catch (Exception e) {
														  e.printStackTrace();
													} finally{
														semaphore.release();	 
													}
												}
											}
										}
				 					}
									
								} else {
									//更新网关信息表的不在线状态
									gatewayModel.setState("1");
									gatewayService.getNoStateOnline(gatewayModel);
									
									//更新设备信息表的不在线状态
									ZBdeviceModel device=new ZBdeviceModel();
									device.setGateway_id(gatewayModel.getGateway_id());
									device.setDevice_state("1");
									zbdeviceService.updateZbNoState(device);
								}	
								
							} catch (RuntimeException e) {
								 throw e;
							}catch (Exception e) {
								LOG.info("光子锁 连接异常...");
								//更新网关信息表的不在线状态
								gatewayModel.setState("1");
								gatewayService.getNoStateOnline(gatewayModel);
								
								//更新设备信息表的不在线状态
								ZBdeviceModel device=new ZBdeviceModel();
								device.setGateway_id(gatewayModel.getGateway_id());
								device.setDevice_state("1");
								zbdeviceService.updateZbNoState(device);
							}
						}
					}
				}
				
			} catch (Exception e) {
				  e.printStackTrace();
			}finally{
				semaphore.release();	 
			}
		}
	} 

}
