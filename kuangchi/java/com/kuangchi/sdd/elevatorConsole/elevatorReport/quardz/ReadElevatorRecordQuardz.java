package com.kuangchi.sdd.elevatorConsole.elevatorReport.quardz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;


import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.baseConsole.event.dao.EventDao;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.elevatorConsole.device.dao.ITKDeviceDao;
import com.kuangchi.sdd.elevatorConsole.device.model.CommDevice;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.impl.ElevatorRecordReportDaoImpl;
import com.kuangchi.sdd.elevatorConsole.tkComm.service.TkCommService;
import com.kuangchi.sdd.interfaceConsole.JedisCache.service.impl.JedisCache;
import com.kuangchi.sdd.util.cacheUtils.CacheUtils;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.datastructure.BoundedQueueUsedForDisplay;

/**
 * 梯控读取记录到缓存定时器
 * @author minting.he
 *
 */
public class ReadElevatorRecordQuardz {
	
	private ICronService cronService;
	private ITKDeviceDao tkDeviceDao;
	private ElevatorRecordReportDaoImpl elevatorRecordReportDao;
	private EventDao eventDao;
	
	public ICronService getCronService() {
		return cronService;
	}
	public void setCronService(ICronService cronService) {
		this.cronService = cronService;
	}
	public ITKDeviceDao getTkDeviceDao() {
		return tkDeviceDao;
	}
	public void setTkDeviceDao(ITKDeviceDao tkDeviceDao) {
		this.tkDeviceDao = tkDeviceDao;
	}
	public ElevatorRecordReportDaoImpl getElevatorRecordReportDao() {
		return elevatorRecordReportDao;
	}
	public void setElevatorRecordReportDao(
			ElevatorRecordReportDaoImpl elevatorRecordReportDao) {
		this.elevatorRecordReportDao = elevatorRecordReportDao;
	}
	public EventDao getEventDao() {
		return eventDao;
	}
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	
	@Value("${redisConnectIp}")
	private String redisConnectIp;
	@Value("${redisConnectPort}")
	private String redisConnectPort;
	
	private TkCommService tkCommService;
	
	
	public TkCommService getTkCommService() {
		return tkCommService;
	}
	public void setTkCommService(TkCommService tkCommService) {
		this.tkCommService = tkCommService;
	}
	/**
	 * 定时读取梯控的记录
	 * @author minting.he
	 */
	public void readRecord(){
		try{
			//集群访问时，只有与数据库中相同的IP地址可以执行定时器的业务操作
			boolean r = cronService.compareIP();	
			if(r){
				CacheUtils<List<Map<String, Object>>> cacheList = new CacheUtils<List<Map<String,Object>>>(redisConnectIp, Integer.valueOf(redisConnectPort));
				CacheUtils<BoundedQueueUsedForDisplay> cacheQueue = new CacheUtils<BoundedQueueUsedForDisplay>(redisConnectIp, Integer.valueOf(redisConnectPort));
				List<Device> deviceList = tkDeviceDao.getAllTKDeviceInfo();
				if(deviceList!=null){
					for(int j=0; j<deviceList.size(); j++){
						//设备
						Device device = deviceList.get(j);
						CommDevice commDevice = new CommDevice();
						commDevice.setMac(device.getMac());
						commDevice.setIp(device.getDevice_ip());
						commDevice.setPort(device.getDevice_port());
						commDevice.setGateway(device.getNetwork_gateway());
						commDevice.setSerialNo(device.getDevice_sequence());
						commDevice.setSubnet(device.getNetwork_mask());
						commDevice.setAddress(device.getAddress());
						String jsonDevice = GsonUtil.toJson(commDevice);
						
						String deviceNum=tkCommService.getTkDevNumByMac(device.getMac());
						
						String url = tkCommService.getTkCommUrl(deviceNum); 
						String message = HttpRequest.sendPost(url+ "TKDevice/tk_RecvEvent.do?", "device="+jsonDevice);
						Gson gson = new Gson();
						List<LinkedTreeMap> commRecord = gson.fromJson(message, ArrayList.class);
						if (commRecord!=null) {
							for (int i = 0; i<commRecord.size(); i++) {
								Map<String, Object> recordMap = new HashMap<String, Object>();
								
								//刷卡事件
								LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) commRecord.get(i).get("checkEvent");	
								if(map!=null){
									recordMap.put("device_num", device.getDevice_num());
									recordMap.put("device_ip", device.getDevice_ip());
									recordMap.put("mac", device.getMac());
									recordMap.put("device_name", device.getDevice_name());
									recordMap.put("check_time", "20"+map.get("recordTime").toString());
									recordMap.put("event_type", "0");
									recordMap.put("card_num", map.get("cardId").toString());
									recordMap.put("type_name", elevatorRecordReportDao.getCardTypeName(map.get("cardId").toString()));
									recordMap.put("card_type_name", map.get("cardType").toString());
									Map<String, Object> staffMap = elevatorRecordReportDao.getStaffInfoByCard(map.get("cardId").toString());
									if(staffMap!=null && staffMap.size()!=0){
										recordMap.put("staff_name", staffMap.get("staff_name").toString());
										recordMap.put("staff_num", staffMap.get("staff_num").toString());
										recordMap.put("staff_no", staffMap.get("staff_no").toString());
									}else {
										Map<String, Object> visitorMap = eventDao.getVisitorName(map.get("cardId").toString());
										if(visitorMap!=null && visitorMap.size()!=0){
											recordMap.put("staff_name", "【访客】"+visitorMap.get("m_visitor_name").toString());
											recordMap.put("staff_num", "");
											recordMap.put("staff_no", "");
										}else {
											recordMap.put("staff_name", "");
											recordMap.put("staff_num", "");
											recordMap.put("staff_no", "");
										}
									}
									recordMap.put("check_type", map.get("recordStatus").toString());
								}else {
									//触发事件
									map = (LinkedTreeMap) commRecord.get(i).get("triggerEvent");		
									if(map!=null){
										recordMap.put("device_num", device.getDevice_num());
										recordMap.put("device_ip", device.getDevice_ip());
										recordMap.put("mac", device.getMac());
										recordMap.put("device_name", device.getDevice_name());
										recordMap.put("check_time", "20"+map.get("recordTime").toString());
										recordMap.put("event_type", "1");
										if(map.get("cardId")==null){
											recordMap.put("card_num", "");
										}else {
											recordMap.put("card_num", map.get("cardId").toString());
										}
										if(map.get("cardType")==null){
											recordMap.put("card_type_name", "");
										}else {
											recordMap.put("card_type_name", map.get("cardType").toString());
										}
										List<String> list = (List) map.get("floorList");
										if(list!=null){
											Map<String, Object> floorMap = new HashMap();
											floorMap.put("floor_num", list.get(0));
											floorMap.put("device_num", device.getDevice_num());
											recordMap.put("floor_num", list.get(0));
											recordMap.put("floor_name", elevatorRecordReportDao.getFloorName(floorMap));
										}
										recordMap.put("floor_state", map.get("floorStatus").toString());
										recordMap.put("check_type", map.get("recordFlag").toString());
									}
								}
								
								//放进缓存中
								List<Map<String, Object>> eleRecordList = cacheList.getObject("eleRecordList");
								if(eleRecordList==null){
									eleRecordList = new ArrayList<Map<String, Object>>();
								}
								eleRecordList.add(recordMap);
								cacheList.saveObject("eleRecordList", eleRecordList);
								BoundedQueueUsedForDisplay q = cacheQueue.getObject("eleSysQueue");
								if(q==null){
									q = new BoundedQueueUsedForDisplay(50);
								}
								q.offer(recordMap);
								cacheQueue.saveObject("eleSysQueue", q);
								BoundedQueueUsedForDisplay l = cacheQueue.getObject("eleSysLatest");
								if(l==null){
									l = new BoundedQueueUsedForDisplay(5);
								}
								l.offer(recordMap);
								cacheQueue.saveObject("eleSysLatest", l);

								
							/*	
								ElevatorRecordInfo record = new ElevatorRecordInfo();
								LinkedTreeMap map = (LinkedTreeMap) commRecord.get(i).get("checkEvent");	//刷卡事件
								if(map!=null){
									record.setDevice_num(device.getDevice_num());
									record.setDevice_ip(device.getDevice_ip());
									record.setMac(device.getMac());
									record.setDevice_name(device.getDevice_name());
									record.setCheck_time("20"+map.get("recordTime").toString());
									record.setEvent_type("0");
									record.setCard_num(map.get("cardId").toString());
									record.setCard_type_name(map.get("cardType").toString());
									HashMap staffMap = elevatorRecordReportDao.getStaffInfoByCard(record.getCard_num());
									if(staffMap!=null && staffMap.size()!=0){
										record.setStaff_no(staffMap.get("staff_no").toString());
										record.setStaff_name(staffMap.get("staff_name").toString());
									}
									record.setCheck_type(map.get("recordStatus").toString());
									elevatorRecordReportDao.insertEventRecord(record);
								}else{	
									map = (LinkedTreeMap) commRecord.get(i).get("triggerEvent");		//触发事件
									if(map!=null){
										record.setDevice_num(device.getDevice_num());
										record.setDevice_ip(device.getDevice_ip());
										record.setMac(device.getMac());
										record.setDevice_name(device.getDevice_name());
										record.setCheck_time("20"+map.get("recordTime").toString());
										record.setEvent_type("1");
										List<String> list = (List) map.get("floorList");
										if(list!=null){
											record.setFloor_num(list.get(0));
										}
										record.setFloor_state(map.get("floorStatus").toString());
										record.setCheck_type(map.get("recordFlag").toString());
										elevatorRecordReportDao.insertEventRecord(record);
									}
								}
							*/
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
