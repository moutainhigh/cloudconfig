package com.kuangchi.sdd.ZigBeeConsole.quartz.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.ZigBeeConsole.authorityManager.model.AuthorityManagerModel;
import com.kuangchi.sdd.ZigBeeConsole.authorityManager.service.AuthorityManagerService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;



@Service("zBAuthorityManagerQuartz")
public class ZBAuthorityManagerQuartz {

	
	@Resource(name="authorityManagerServiceImpl")
	private AuthorityManagerService authorityManagerService;

	@Resource(name="cronServiceImpl")
	private ICronService cronService;
	
	public final static Semaphore semaphore=new Semaphore(1);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-27
	 * @功能描述: 自动执行授权任务
	 */
	public synchronized void autoFindUnIssuedTask(){
		boolean getResource=false;
		try {
			getResource=semaphore.tryAcquire(5,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		if(getResource){
			try {
				boolean r = cronService.compareIP();	
				if(r){
					
					// 查询任务表，按设备和创建时间升序排列
					int taskCount = 50;  //每次下发任务条数
					List<Map> taskList = authorityManagerService.getZBTasks(taskCount);
					
					if(taskList!=null && taskList.size()>0){
						
						// 【根据设备和任务类型封装批量任务】
						List<Map> authMapList = new ArrayList<Map>();
						
						List<AuthorityManagerModel> authList = null;
						for(int i=0; i<taskList.size(); i++){
							
							boolean lastFlag = false;
							// 如果为批量集合首条，则新建List
							if(i==0 || !taskList.get(i).get("task_type").equals(taskList.get(i-1).get("task_type"))){
								authList = new ArrayList<AuthorityManagerModel>();
							}
							// 如果为批量集合最后一条，则将该记录 TransportFlag 改为0；并将该集合加入批量Map中
							if(i == taskList.size()-1 ||  !taskList.get(i).get("task_type").equals(taskList.get(i+1).get("task_type"))
									|| !taskList.get(i).get("device_id").equals(taskList.get(i+1).get("device_id"))){
								lastFlag = true;
							}
							
							AuthorityManagerModel model = new AuthorityManagerModel();
							model.setAuthority_num((String)taskList.get(i).get("authority_num"));
							model.setBegin_valid_date((String)taskList.get(i).get("begin_valid_date"));
							model.setEnd_valid_date((String)taskList.get(i).get("end_valid_date"));
							model.setDevice_id((String)taskList.get(i).get("device_id"));
							model.setLight_id((String)taskList.get(i).get("light_id"));
							if(lastFlag){
								model.setTransportFlag(0x00);
							} else {
								model.setTransportFlag(0x01);
							}
							authList.add(model);
							
							if(lastFlag){
								Map<String, Object> authMap = new HashMap<String, Object>();
								authMap.put("authList", authList);
								authMap.put("taskType", taskList.get(i).get("task_type"));
								authMap.put("device_id", taskList.get(i).get("device_id"));
								authMapList.add(authMap);
							}
						}
						
						// 【执行批量任务】
						for (Map authMap : authMapList) {
							
							String device_id = (String)authMap.get("device_id"); 
							
							// 判断任务类型，若是下发任务则判断该设备是否有剩余空间，有则下发
							String taskType = (String)authMap.get("taskType");
							if("0".equals(taskType)){
								int count = authorityManagerService.selectAuthorityCount(device_id,null);  // 此处不区分light_id，仅判断设备是否有空位
								if(count < 312){
									if(ifDevOnLine(device_id)){
										List<AuthorityManagerModel> auths = (List) authMap.get("authList");
										for (AuthorityManagerModel auth : auths) {
											authorityManagerService.issuedAddTask(auth);
										}
									}
								}
							} else {
								if(ifDevOnLine(device_id)){
									List<AuthorityManagerModel> auths = (List) authMap.get("authList");
									for (AuthorityManagerModel model : auths) {
										boolean delResult = authorityManagerService.issuedDelTask(model);
										if(delResult){
											Map<String, Object> waitParam = new HashMap<String, Object>();
											waitParam.put("device_id", model.getDevice_id());
											waitParam.put("card_nums", model.getLight_id());
											List<AuthorityManagerModel> waitAuths = authorityManagerService.getWaitAuthorityByCards(waitParam);
											authorityManagerService.insertZigbeeTask(waitAuths);
										}
									}
								}
							}
							
							// 执行任务前判断设备是否在线，若不在线则不下发
							
							/*Cache cache = Ehcache.getCache("GWInfoCache");  //  将网关信息存入缓存
							if(cache == null || cache.get(device_id) == null){
								Map map = authorityManagerService.getZigbeeIpMap(device_id);
								cache.put(new Element(device_id,map));
							} 
							Map map = (Map)cache.get(device_id).getObjectValue();*/
							/*
							Map map = authorityManagerService.getZigbeeIpMap(device_id);
							if(map != null){
								String serverUrl=map.get("remote_ip").toString(); //通讯服务器IP
								String serverPort=map.get("remote_port").toString();//通讯服务器端口
								String gateway=map.get("gateway").toString(); //网关
								String gateway_port=map.get("gateway_port").toString(); //网关端口
								
								String getStateUrl = "http://"+serverUrl+":"+serverPort+"/comm/zigBee/getLockState.do?";
								String getStateParam = "lock_id=" + device_id +"&remoteIp=" + gateway+"&port="+gateway_port;
								String stateMsg = HttpRequest.sendPost(getStateUrl, getStateParam);
							
								if(!(null==stateMsg||"null".equals(stateMsg)||"".equals(stateMsg))){
									String taskType = (String)authMap.get("taskType");
									if("0".equals(taskType)){
										List<AuthorityManagerModel> auths = (List) authMap.get("authList");
										for (AuthorityManagerModel auth : auths) {
											//String device_id = auth.getDevice_id();
											String light_id = auth.getLight_id();
											int count = authorityManagerService.selectAuthorityCount(device_id,light_id);
											if(count < 312){
												authorityManagerService.issuedAddTask(auth);
											}
										}
									} else {
										List<AuthorityManagerModel> auths = (List) authMap.get("authList");
										for (AuthorityManagerModel model : auths) {
											boolean delResult = authorityManagerService.issuedDelTask(model);
											if(delResult){
												Map<String, Object> waitParam = new HashMap<String, Object>();
												waitParam.put("device_id", model.getDevice_id());
												waitParam.put("card_nums", model.getLight_id());
												List<AuthorityManagerModel> waitAuths = authorityManagerService.getWaitAuthorityByCards(waitParam);
												authorityManagerService.insertZigbeeTask(waitAuths);
											}
										}
									}
								}
							}*/
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
	
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-27
	 * @功能描述: 定时将过期权限添加到任务表，待删除
	 */
	public synchronized void autoFindExpireAuthority(){
		boolean getResource=false;
		//在2秒之内获取许可，如果获取到了就执行更新状语句，否则不执行
		try {
			getResource=semaphore.tryAcquire(1,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		if(getResource){
			try {
				//集群访问时，只有与数据库中相同的IP地址可以执行页面定时器的业务操作
				boolean r = cronService.compareIP();	
				if(r){
					List<AuthorityManagerModel> authorityList = authorityManagerService.getOutTimeAuthority(null);//查找出过期失效的授权
					if(authorityList != null && authorityList.size()!=0){
						
						// 批量更新权限状态
						Map<String, Object> stateParam = new HashMap<String, Object>();
						String authority_nums = "";
						for (AuthorityManagerModel auth : authorityList) {
							authority_nums += "'"+auth.getAuthority_num()+"'"+",";
						}
						stateParam.put("authority_num",authority_nums.substring(0, authority_nums.length()-1));
						stateParam.put("authority_flag", 5);
						authorityManagerService.updateSomeAuthorityState(stateParam);
						
						// 新增删除权限任务
						authorityManagerService.insertDeleteTask(authorityList);
						
						for(int i=0; i < authorityList.size(); i++){
							// 根据设备和光ID查询是否有待下载权限（按开始时间升序）
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("device_id", authorityList.get(i).getDevice_id());
							map.put("card_nums", authorityList.get(i).getLight_id());
							List<AuthorityManagerModel> waitAuths = authorityManagerService.getWaitAuthorityByCards(map);
							
							authorityManagerService.insertZigbeeTask(waitAuths);
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
	
	
	
	public boolean ifDevOnLine(String device_id){
		boolean onLineState = false;
		Map map = authorityManagerService.getZigbeeIpMap(device_id);
		if(map != null){
			String serverUrl=map.get("remote_ip").toString(); //通讯服务器IP
			String serverPort=map.get("remote_port").toString();//通讯服务器端口
			String gateway=map.get("gateway").toString(); //网关
			String gateway_port=map.get("gateway_port").toString(); //网关端口
			
			String getStateUrl = "http://"+serverUrl+":"+serverPort+"/comm/zigBee/getLockState.do?";
			String getStateParam = "lock_id=" + device_id +"&remoteIp=" + gateway+"&port="+gateway_port;
			String stateMsg = HttpRequest.sendPost(getStateUrl, getStateParam);
			if(!(null==stateMsg||"null".equals(stateMsg)||"".equals(stateMsg))){
				onLineState = true;
			}
		} 
		return onLineState;
	}

}
