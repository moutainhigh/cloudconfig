package com.kuangchi.sdd.ZigBeeConsole.device.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.ZigBeeConsole.device.dao.IZBdeviceDao;
import com.kuangchi.sdd.ZigBeeConsole.device.model.ZBdeviceModel;
import com.kuangchi.sdd.ZigBeeConsole.device.service.IZBdeviceService;
import com.kuangchi.sdd.ZigBeeConsole.device.util.ObjectSephamorePool;
import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;
import com.kuangchi.sdd.ZigBeeConsole.quartz.service.impl.ZBAuthorityManagerQuartz;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;


/**
 * 光子锁记录 - service实现类
 * @author guibo.chen
 */
@Transactional
@Service("zBdeviceServiceImpl")
public class ZBdeviceServiceImpl implements IZBdeviceService{
	
	@Resource(name = "zBdeviceDaoImpl")
	private IZBdeviceDao zbdeviceDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Override
	public Grid getRecordByParamPage(ZBdeviceModel device, String Page,
			String size) {
		Integer count=zbdeviceDao.getZBdeviceInfoCount(device);
		List<ZBdeviceModel> list=zbdeviceDao.getZBdeviceInfo(device, Page, size);
		
		List<Map> signalList = zbdeviceDao.getDeviceSignalRange();
		
		
		// 重新整理数据，若设备不在线，则不显示信号强度、电量、时间等设备信息
		for (ZBdeviceModel zBdeviceModel : list) {
			if("1".equals(zBdeviceModel.getDevice_state())){
				zBdeviceModel.setElectricity(null);
				zBdeviceModel.setTime_stamp(null);
				zBdeviceModel.setDevice_signal(null);
			} else {
				// 根据用户自定义信号强度设置信号强弱程度
				for (Map signalMap : signalList) {
					Double signal1 = Double.parseDouble(((String)signalMap.get("signal_range")).split("~")[0].replace("-", ""));
					Double signal2 = Double.parseDouble(((String)signalMap.get("signal_range")).split("~")[1].replace("-", ""));
					
					Double devSignal = Double.parseDouble(zBdeviceModel.getDevice_signal());
					if(devSignal > signal2 && signal2 < signal1){
						zBdeviceModel.setDevice_signal(signalMap.get("degree") +"(" + devSignal + ")");
						break;
					}
				}
			}
		}
		
		
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(list);
		return grid;
	}

	
	//搜索勾选设备
	@Override
	public List<ZBdeviceModel> getZBgateway(String id) {
		
		List<ZBdeviceModel> newDeviceList = new ArrayList<ZBdeviceModel>();
		
		boolean getResource=false;
		//在5秒之内获取许可，如果获取到了就执行更新状语句，否则不执行
		Semaphore semaphore = new Semaphore(1);
		try {
			getResource=semaphore.tryAcquire(5,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return null;
		}
		
		if(getResource){
			try {
				List<GatewayModel> gatewayList = null;
				if(id == null) {
					gatewayList =zbdeviceDao.getZBgateway();//查询全部网关和网关端口
				} else {
					gatewayList = zbdeviceDao.getZBgatewayById(id);
				}
				
				
				for (GatewayModel gatewayModel : gatewayList) {
					// 循环调用网关接口，直到返回结束信息
					int count = 0;
					while(true){
						String url = "http://"+gatewayModel.getRemote_ip() + ":" +
								gatewayModel.getRemote_port() + "/comm/zigBee/getLock.do?";
						String data = "remoteIp=" + gatewayModel.getGateway() + "&port=" + gatewayModel.getGateway_port();
						String lockMsg = HttpRequest.sendPost(url, data);//返回锁ID和房间号
						
						if (lockMsg != null && !"".equals(lockMsg) && !"null".equals(lockMsg)){
							Map lockMap = GsonUtil.toBean(lockMsg, HashMap.class);
							String device_id = (String)lockMap.get("lockId"); 
							if(device_id == null){  
								break;  //结束信息
							} else {
								
								//调用第二次接口，查询设备详细信息
								String urls="http://"+gatewayModel.getRemote_ip()+":"+
										gatewayModel.getRemote_port()+"/comm/zigBee/getLockState.do?";
								String datas="remoteIp="+gatewayModel.getGateway()+"&port="+gatewayModel.getGateway_port()+"&lock_id="+device_id;
								String devMsg = HttpRequest.sendPost(urls, datas);
								
								if(devMsg != null && !"".equals(devMsg) && !"null".equals(devMsg)){
									Map deviceMap = GsonUtil.toBean(devMsg, HashMap.class);
									
									// 查询该设备是否存在。若存在则更新设备，若不存在则新增设备
									List<ZBdeviceModel> existDevice = zbdeviceDao.getZBdeviceByIp(device_id);
									if(existDevice == null || existDevice.size()==0){
										ZBdeviceModel devices = new ZBdeviceModel();
										devices.setGateway_id(gatewayModel.getGateway_id());//网关id
										devices.setZigbee_id(gatewayModel.getZigbee_id());//网关芯片id
										devices.setRemote_ip(gatewayModel.getRemote_ip());//通讯服务器ID
										devices.setDevice_id((String)deviceMap.get("lockId"));//设备ID
										devices.setRoom_num((String)deviceMap.get("roomNo"));//房间号
										devices.setGateway_pan_id(String.valueOf(Integer.parseInt(deviceMap.get("panId").toString(), 16)));//panid
										devices.setElectricity((String)deviceMap.get("bateryBalance"));//电量
										devices.setDevice_signal(((Double)deviceMap.get("signalLevel")).toString());//信号强度
										//BigDecimal bd = new BigDecimal(((Double)deviceMap.get("time")).toString());  
										//String time = bd.toPlainString();
										//Long times=Long.valueOf(time)*1000;
										Long times = ((Double)deviceMap.get("time")).longValue()*1000;
										Date d = new Date(times);
										SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										devices.setTime_stamp(sf.format(d));//时间戳
										
										Integer Community_num = ((Double)deviceMap.get("communityNo")).intValue();
										if(Community_num != 65535){
											devices.setCommunity_num(Community_num);
										}
										devices.setDevice_state("0");
										
										devices.setFirmwareVersion((String)lockMap.get("firmwareVersion"));
										devices.setSoftwareVersion((String)lockMap.get("softwareVersion"));
										
										
										zbdeviceDao.addZbDeviceinfo(devices);
										
										newDeviceList.add(devices);
										
									} else {
										ZBdeviceModel devices = new ZBdeviceModel();
										devices.setDevice_id(device_id);
										devices.setGateway_id(gatewayModel.getGateway_id());
										devices.setZigbee_id(gatewayModel.getZigbee_id());//网关芯片id
										devices.setRemote_ip(gatewayModel.getRemote_ip());//通讯服务器ID
										devices.setRoom_num((String)deviceMap.get("roomNo"));
										devices.setGateway_pan_id(String.valueOf(Integer.parseInt(deviceMap.get("panId").toString(), 16)));//panid
										devices.setElectricity((String)deviceMap.get("bateryBalance"));//电量
										devices.setDevice_signal(((Double)deviceMap.get("signalLevel")).toString());//信号强度
										
//										BigDecimal bd = new BigDecimal(((Double)deviceMap.get("time")).toString()); 
//										String time = bd.toPlainString();
//										Long times=Long.valueOf(time)*1000;
										Long times = ((Double)deviceMap.get("time")).longValue()*1000;
										Date d = new Date(times);
										SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										devices.setTime_stamp(sf.format(d));//时间戳
										
										Integer Community_num = ((Double)deviceMap.get("communityNo")).intValue();
										if(Community_num != 65535){
											devices.setCommunity_num(Community_num);
										}
										
										devices.setDevice_state("0");
										
										devices.setFirmwareVersion((String)lockMap.get("firmwareVersion"));
										devices.setSoftwareVersion((String)lockMap.get("softwareVersion"));
										
										//更新设备信息和设备状态
										zbdeviceDao.updateZbDeviceState(devices);
										
										//更新成功之后判断表中的数据是不是被删除，如果是要加入集合里面，以便返回多少个设备给页面显示
										for (ZBdeviceModel zBdeviceModel2 : existDevice) {
											if("1".equals(zBdeviceModel2.getDevice_flag())){
												newDeviceList.add(devices);
											}
										}
										
									}
								}
							} 
						} else {
							count = count +1;   //防止在返回为空的情况下没及时处理结束信息，造成下次搜索失败
							if(count == 2){
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				  e.printStackTrace();
			} finally{
				semaphore.release();	 
			}
		}
		
		return newDeviceList;
	}	
	
	@Override
	public Boolean deleteZBdevice(String device_id) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "设备信息维护");
        log.put("V_OP_FUNCTION", "删除");
        //log.put("V_OP_ID", discount_info.getCreate_user());
        try{
        	Boolean obj=zbdeviceDao.deleteZBdevice(device_id);
    		if(obj==true){
    	        log.put("V_OP_TYPE", "业务");
    	        log.put("V_OP_MSG", "删除成功");
    	        logDao.addLog(log);
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "删除失败");
    			logDao.addLog(log);
    			return false;
    		}
        }catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "删除异常");
    		logDao.addLog(log);
    		return false;
        }
	}

	@Override
	public List<ZBdeviceModel> getZBdeviceByIpAndPort(ZBdeviceModel device) {
		return zbdeviceDao.getZBdeviceByIpAndPort(device);
	}

	//设置pan_id
	@Override
	public Boolean updateZbPanId(ZBdeviceModel device) {
		
		boolean getResource=false;
		//在5秒之内获取许可，如果获取到了就执行更新状语句，否则不执行
		Semaphore semaphore = new Semaphore(1);
		try {
			getResource=semaphore.tryAcquire(5,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return false;
		}
		
		if(getResource){
			try {
				Map<String, String> log = new HashMap<String, String>();
		        log.put("V_OP_NAME", "设置网关panId");
		        log.put("V_OP_FUNCTION", "修改");
		        //log.put("V_OP_ID", discount_info.getCreate_user());
		        try{
		        	Map<String, Object> map = zbdeviceDao.getGatewayByZigbeeId(device.getDevice_id());
		        	 if(map!=null){
		        		String remote_ip = (String)map.get("remote_ip");
		         		String remote_port = (String)map.get("remote_port");
		         		String gateway = (String)map.get("gateway");
		         		String gateway_port = (String)map.get("gateway_port");
		         		String device_id = (String)map.get("device_id");
		         		
		         		String pan_id=Integer.toHexString(Integer.valueOf(device.getGateway_pan_id()));
		         		String pasParam = "0000";
		         		pan_id = pasParam.substring(0, 4-pan_id.length()) + pan_id;
		        		
		         		String url="http://"+remote_ip+":"+
		         				remote_port+"/comm/zigBee/setPanId.do?";
		 				String data="remoteIp="+gateway+"&port="+gateway_port+
		 						"&zigBeeId="+device_id+"&panId="+pan_id;
		 				String str = HttpRequest.sendPost(url, data);//返回锁ID和状态位0x00或0x01
		 				if (str != null && !"".equals(str) && !"null".equals(str)){
		 					Map deviceMap = GsonUtil.toBean(str, HashMap.class);
		 					if(0.0==(Double)deviceMap.get("status")){
		 						//设置panid成功之后获取设备在线状态
		 						//String urls="http://" + remote_ip + ":"+
		 						//		remote_port + "/comm/zigBee/getLockState.do?";
								//String datas="remoteIp="+gateway + "&port=" + gateway_port+
								//		"&lock_id=" + device_id;
								//String gatewayMsg = HttpRequest.sendPost(urls, datas);
								
								// 更新该设备状态
		 						/*if(gatewayMsg != null && !"".equals(gatewayMsg) && !"null".equals(gatewayMsg)){
		 							device.setDevice_state("0");//在线
		 					    }else{
		 						device.setDevice_state("1");//不在线
		 					    }*/
		 						Boolean obj=zbdeviceDao.updateZbPanId(device);
		 	 		    		if(obj==true){
		 	 		    	        log.put("V_OP_TYPE", "业务");
		 	 		    	        log.put("V_OP_MSG", "修改成功");
		 	 		    	        logDao.addLog(log);
		 	 		    	        return true; 
		 	 		    		}else{
		 	 		    			log.put("V_OP_TYPE", "业务");
		 	 		    			log.put("V_OP_MSG", "修改失败");
		 	 		    			logDao.addLog(log);
		 	 		    		}	
		 	 				}
		 				}else{
		 					log.put("V_OP_TYPE", "业务");
		 	    			log.put("V_OP_MSG", "修改失败");
		 	    			logDao.addLog(log);
		 				}
		        	 }
		        }catch(Exception e){
		        	e.printStackTrace();
		        	log.put("V_OP_TYPE", "异常");
		        	log.put("V_OP_MSG", "修改失败");
		    		logDao.addLog(log);
		    		return false;
		        }
			} catch (Exception e) {
				  e.printStackTrace();
			} finally{
				semaphore.release();	 
			}
		}
		
		return false;
	}

	
	@Override
	public List<ZBdeviceModel> getZBgatewayByGateway(ZBdeviceModel device) {
		return zbdeviceDao.getZBgatewayByGateway(device);
	}

	
	@Override
	public boolean setSystemTime(String device_id) {
		
		boolean getResource=false;
		//在5秒之内获取许可，如果获取到了就执行更新状语句，否则不执行
		Semaphore semaphore = ObjectSephamorePool.getSemaphore(device_id);
		try {
			getResource=semaphore.tryAcquire(5,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return false;
		}
		
		boolean result = false;
		if(getResource){
			try {
//				Map map = zbdeviceDao.getGatewayByDeviceId(device_id);
				/*Cache cache = Ehcache.getCache("GWInfoCache");  //  将网关信息存入缓存
				if(cache == null || cache.get(device_id) == null){
					Map map = zbdeviceDao.getGatewayByDeviceId(device_id);
					cache.put(new Element(device_id,map));
				} 
				Map map = (Map)cache.get(device_id).getObjectValue();*/
				
				Map map = zbdeviceDao.getGatewayByDeviceId(device_id);
				if(map != null) {
					String remote_ip = (String)map.get("remote_ip");
					String remote_port = (String)map.get("remote_port");
					String gateway = (String)map.get("gateway");
					String gateway_port = (String)map.get("gateway_port");
					
					Date date = new Date();
					String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					long timeStamp = new Date().getTime()/1000;
					
					String url = "http://"+ remote_ip + ":" + remote_port + "/comm/" + "zigBee/setSystemTime.do?";
					String msg = HttpRequest.sendPost(url, "remoteIp=" + gateway + "&port=" 
							+ gateway_port+ "&lockId=" + device_id + "&time=" + timeStamp);
					
					if(msg != null && !"".equals(msg) && !"null".equals(msg)){
						Map resultMap = GsonUtil.toBean(msg, HashMap.class);
						Double status = (Double) resultMap.get("status");
						if(status == 0){
							Map<String, Object> timeParam = new HashMap<String, Object>();
							timeParam.put("device_id", device_id);
							timeParam.put("time_stamp", timeStr);
							result = zbdeviceDao.updateDeviceTime(timeParam);
						} 
					}
				}
			} catch (Exception e) {
				  e.printStackTrace();
			} finally{
				semaphore.release();	 
			}
		}
		
		return result;
	}

	
	@Override
	public boolean setPassword(String device_id, String password) {
		
		boolean getResource=false;
		//在5秒之内获取许可，如果获取到了就执行更新状语句，否则不执行
		Semaphore semaphore = ObjectSephamorePool.getSemaphore(device_id);
		try {
			getResource=semaphore.tryAcquire(5,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return false;
		}
		
		boolean result = false;
		if(getResource){
			try {
				/*Cache cache = Ehcache.getCache("GWInfoCache");  //  将网关信息存入缓存
				if(cache == null || cache.get(device_id) == null){
					Map map = zbdeviceDao.getGatewayByDeviceId(device_id);
					cache.put(new Element(device_id,map));
				} 
				Map map = (Map)cache.get(device_id).getObjectValue();*/
				
				Map map = zbdeviceDao.getGatewayByDeviceId(device_id);
				if(map != null) {
					String remote_ip = (String)map.get("remote_ip");
					String remote_port = (String)map.get("remote_port");
					String gateway = (String)map.get("gateway");
					String gateway_port = (String)map.get("gateway_port");
					
					String url = "http://"+ remote_ip + ":" + remote_port + "/comm/" + "zigBee/setPassword.do?";
					String msg = HttpRequest.sendPost(url, "remoteIp=" + gateway + "&port=" 
							+ gateway_port+ "&lockId=" + device_id + "&password=" + password);
					
					if(msg != null && !"".equals(msg) && !"null".equals(msg)){
						Map resultMap = GsonUtil.toBean(msg, HashMap.class);
						Double status = (Double) resultMap.get("status");
						if(status == 0){
							result =  true;
						} 
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				semaphore.release();	 
			}
		}
		
		return result;
		
	}

	@Override
	public Map<String, Object> getDeviceInfoByDeviceId(String device_id) {
		return zbdeviceDao.getDeviceInfoByDeviceId(device_id);
	}

	@Override
	public String getDeviceTime(String device_id) {
		
		boolean getResource=false;
		//在2秒之内获取许可，如果获取到了就执行更新状语句，否则不执行
		Semaphore semaphore = ObjectSephamorePool.getSemaphore(device_id);
		try {
			getResource=semaphore.tryAcquire(5,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return null;
		}
		
		if(getResource){
			try {
				Map map = zbdeviceDao.getGatewayByDeviceId(device_id);
				if(map != null) {
					String remote_ip = (String)map.get("remote_ip");
					String remote_port = (String)map.get("remote_port");
					String gateway = (String)map.get("gateway");
					String gateway_port = (String)map.get("gateway_port");
					String url = "http://"+ remote_ip + ":" + remote_port + "/comm/zigBee/getLockState.do?";
					String msg = HttpRequest.sendPost(url, "remoteIp=" + gateway + "&port=" 
							+ gateway_port+ "&lock_id=" + device_id);
					
					if(msg != null && !"".equals(msg) && !"null".equals(msg)){
						Map resultMap = GsonUtil.toBean(msg, HashMap.class);
						
//						String timeStr = new BigDecimal(((Double)resultMap.get("time")).toString()).toPlainString();
//						Long timeStamp = Long.valueOf(timeStr)*1000;
						Long timeStamp = ((Double)resultMap.get("time")).longValue()*1000;
						String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeStamp));
						
						return time;
					}
				}
			} catch (Exception e) {
				  e.printStackTrace();
			} finally{
				semaphore.release();	 
			}
		}
		
		return null;
	}
	
	//查询网关信息
	@Override
	public List<GatewayModel> getZBgateways() {
		return zbdeviceDao.getZBgateway();
	}

	@Override
	public List<ZBdeviceModel> getGatewayAndDevice(int gateway_id) {
		return zbdeviceDao.getGatewayAndDevice(gateway_id);
	}

	@Override
	public Boolean updateZbDeviceInfos(ZBdeviceModel device) {
		return zbdeviceDao.updateZbDeviceInfos(device);
	}

	@Override
	public Boolean updateZbNoState(ZBdeviceModel device) {
		return zbdeviceDao.updateZbNoState(device);
	}
	
	@Override
	public Boolean setDevStateByID(ZBdeviceModel device) {
		return zbdeviceDao.setDevStateByID(device);
	}


	//设置小区号
	@Override
	public Boolean setHouseNumber(ZBdeviceModel device) {
		boolean getResource=false;
		//在5秒之内获取许可，如果获取到了就执行更新状语句，否则不执行
		Semaphore semaphore = ObjectSephamorePool.getSemaphore(device.getDevice_id());
		try {
			getResource=semaphore.tryAcquire(2,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return false;
		}
		
		if(getResource){
			try {
				Map<String, String> log = new HashMap<String, String>();
		        log.put("V_OP_NAME", "设置小区号");
		        log.put("V_OP_FUNCTION", "修改");
		        //log.put("V_OP_ID", discount_info.getCreate_user());
		        try{
		        	Map<String, Object> map = zbdeviceDao.getGatewayByDeviceId(device.getDevice_id());
		        	 if(map!=null){
		        		String remote_ip = (String)map.get("remote_ip");
		         		String remote_port = (String)map.get("remote_port");
		         		String gateway = (String)map.get("gateway");
		         		String gateway_port = (String)map.get("gateway_port");
		         		
//			         		String community_num=Integer.toHexString(Integer.valueOf(device.getCommunity_num()));
//			         		String pasParam = "0000";
//			         		community_num = pasParam.substring(0, 4-community_num.length()) + community_num;
		         		Integer community_num = Integer.valueOf(device.getCommunity_num());
		         		String url="http://"+remote_ip+":"+
		         				remote_port+"/comm/zigBee/setCommunityNo.do?";
		 				String data="remoteIp="+gateway+"&port="+gateway_port+
		 						"&lockId="+device.getDevice_id()+"&communityNo="+community_num;
		 				String str = HttpRequest.sendPost(url, data);//返回锁ID和状态位0x00或0x01
		 				if (str != null && !"".equals(str) && !"null".equals(str)){
		 					Map deviceMap = GsonUtil.toBean(str, HashMap.class);
		 					if(0.0==(Double)deviceMap.get("status")){
		 	 					Boolean obj=zbdeviceDao.updateZbHouseNumber(device);
		 	 		    		if(obj==true){
		 	 		    	        log.put("V_OP_TYPE", "业务");
		 	 		    	        log.put("V_OP_MSG", "修改成功");
		 	 		    	        logDao.addLog(log);
		 	 		    	        return true; 
		 	 		    		}else{
		 	 		    			log.put("V_OP_TYPE", "业务");
		 	 		    			log.put("V_OP_MSG", "修改失败");
		 	 		    			logDao.addLog(log);
		 	 		    		}
		 	 				}
		 				}else{
		 					log.put("V_OP_TYPE", "业务");
		 	    			log.put("V_OP_MSG", "修改失败");
		 	    			logDao.addLog(log);
		 				}
		        	 }
		        }catch(Exception e){
		        	e.printStackTrace();
		        	log.put("V_OP_TYPE", "异常");
		        	log.put("V_OP_MSG", "修改失败");
		    		logDao.addLog(log);
		    		return false;
		        }
			} catch (Exception e) {
				  e.printStackTrace();
			} finally{
				semaphore.release();	 
			}
		}
		
		return false;
	}

	@Override
	public List<ZBdeviceModel> getZBdeviceByIp(String device_id) {
		return zbdeviceDao.getZBdeviceByIp(device_id);
	}
	
	//点击刷新
	public Boolean reloadDevice(String device_id){
		Map<String,Object> map=zbdeviceDao.getGatewayByDeviceIDs(device_id);//查询通讯参数
		if(map!=null){
    		String remote_ip = (String)map.get("remote_ip");
     		String remote_port = (String)map.get("remote_port");
     		String gateway = (String)map.get("gateway");
     		String gateway_port = (String)map.get("gateway_port");
     		String zigbee_id = (String)map.get("zigbee_id");
     		
     		String urls="http://" + remote_ip + ":"+
     				remote_port + "/comm/zigBee/getLockState.do?";
			String datas="remoteIp="+gateway + "&port=" + gateway_port+
					"&lock_id=" + device_id;
			String devMsg = HttpRequest.sendPost(urls, datas);	
			
			// 更新该设备状态
			if(devMsg != null && !"".equals(devMsg) && !"null".equals(devMsg)){
				Map deviceMap = GsonUtil.toBean(devMsg, HashMap.class);
				ZBdeviceModel deviceModel=new ZBdeviceModel();
				//更新设备信息表的在线状态
				deviceModel.setDevice_id(device_id);
				deviceModel.setRoom_num((String)deviceMap.get("roomNo"));
				deviceModel.setGateway_pan_id(String.valueOf(Integer.parseInt(deviceMap.get("panId").toString(), 16)));//panid
				deviceModel.setElectricity((String)deviceMap.get("bateryBalance"));//电量
				deviceModel.setDevice_signal(((Double)deviceMap.get("signalLevel")).toString());//信号强度
				
				
				deviceModel.setRemote_ip(remote_ip);
				deviceModel.setZigbee_id(zigbee_id);
				
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
				//zbdeviceDao.updateZbDeviceStateTime(deviceModel);
				zbdeviceDao.updateZbDeviceInfos(deviceModel);
				return true;
			}else{
				//更新设备信息表的不在线状态根据device_id更新
				ZBdeviceModel device=new ZBdeviceModel();
				device.setDevice_id(device_id);
				device.setDevice_state("1");
				zbdeviceDao.setDevStateByID(device);
			}
				
		}
			return false;
 }
	
	
	/**
	 * 查询设备信号强度信息
	 * @author huixian.pan
	 * @return
	 */
	public List<Map> getDeviceSignalRange(){
		return zbdeviceDao.getDeviceSignalRange();
	}
	
	/**
	 * 更新设置
	 * @author huixian.pan
	 * @param list
	 * @return
	 */
	public Boolean updDeviceSignalRange(List<Map> list){
		   boolean result=true;
		   Map<String, String> log = new HashMap<String, String>();
	       log.put("V_OP_NAME", "设置设备信号强度");
	       log.put("V_OP_FUNCTION", "修改");
		   for(Map map:list){
			   if(zbdeviceDao.updDeviceSignalRange(map)){
				   log.put("V_OP_TYPE", "业务");
		    	   log.put("V_OP_MSG", "修改成功");
		    	   logDao.addLog(log);
			   }else{
				   log.put("V_OP_TYPE", "业务");
		    	   log.put("V_OP_MSG", "修改失败");
		    	   logDao.addLog(log);
		    	   result=false;
			   }
		   }
		   return result;
	}
	
	/**
	 * 查询失败设备信息
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public Grid getFailureZBdeviceInfo(Map map){
		Grid grid=new Grid();
		grid.setTotal(zbdeviceDao.getFailureZBdeviceInfoCount(map));
		grid.setRows(zbdeviceDao.getFailureZBdeviceInfo(map));
		return grid;
	}
	
	
}