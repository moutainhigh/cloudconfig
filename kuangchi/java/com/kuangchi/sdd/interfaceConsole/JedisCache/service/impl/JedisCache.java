package com.kuangchi.sdd.interfaceConsole.JedisCache.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.baseConsole.device.dao.DeviceDao;
import com.kuangchi.sdd.baseConsole.event.dao.EventDao;
import com.kuangchi.sdd.util.cacheUtils.RedisCacheAdaptee;
import com.kuangchi.sdd.util.datastructure.BoundedQueueUsedForDisplay;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service("jedisCache")
public class JedisCache<T> {
	public static JedisPool pool = null;

	public JedisCache() {
		super();
	}

	public JedisCache(String ip, int prot) {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(500);
			config.setMaxIdle(5);
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, ip, prot, 100000);
		}
	}
 
	public void saveObject(String key,T t){
		RedisCacheAdaptee<T> adaptee=new RedisCacheAdaptee<T>();
		adaptee.saveObject(key, t,pool);
	}
		
	public T getObject(String key){
		RedisCacheAdaptee<T> adaptee=new RedisCacheAdaptee<T>();
		return adaptee.getObject(key, pool);
	}
		
		
	@Value("${redisConnectIp}")
	private String redisConnectIp;
	
	@Value("${redisConnectPort}")
	private String redisConnectPort;
	
	@Resource(name="eventDao")
	private EventDao eventDao;
	@Resource(name="deviceDao")
	private DeviceDao deviceDao;
	
	private JedisCache<List<Map>> jedisCache;
	
	//门禁监控队列
	private BoundedQueueUsedForDisplay doorSysQueue;
	//门禁地图显示队列
	private BoundedQueueUsedForDisplay doorSysLatest;
	//梯控监控队列
	private BoundedQueueUsedForDisplay eleSysQueue;
	//梯控地图显示队列
	private BoundedQueueUsedForDisplay eleSysLatest;
	
	public BoundedQueueUsedForDisplay getDoorSysQueue() {
		return doorSysQueue;
	}
	public void setDoorSysQueue(BoundedQueueUsedForDisplay doorSysQueue) {
		this.doorSysQueue = doorSysQueue;
	}
	public BoundedQueueUsedForDisplay getDoorSysLatest() {
		return doorSysLatest;
	}
	public void setDoorSysLatest(BoundedQueueUsedForDisplay doorSysLatest) {
		this.doorSysLatest = doorSysLatest;
	}
	public BoundedQueueUsedForDisplay getEleSysQueue() {
		return eleSysQueue;
	}
	public void setElevaSysQueue(BoundedQueueUsedForDisplay eleSysQueue) {
		this.eleSysQueue = eleSysQueue;
	}
	public BoundedQueueUsedForDisplay getEleSysLatest() {
		return eleSysLatest;
	}
	public void setElevaSysLatest(BoundedQueueUsedForDisplay eleSysLatest) {
		this.eleSysLatest = eleSysLatest;
	}

	/**
	 * 构建redis连接池 
	 * @author minting.he
	 * 
	 */
/*	public void createCache(){
		//当jedis为空时，创建
		if(jedisCache==null){
			jedisCache = new JedisCache<List<Map>>(redisConnectIp, Integer.valueOf(redisConnectPort));
			
			
			 * 门禁 
			 
			List<Map> eventList = new ArrayList<Map>();				//读头事件
			jedisCache.saveObject("eventList", eventList);
			
			List<Map> warningEventList = eventDao.getNowWarning();	//告警事件，获取当前未处理的
			jedisCache.saveObject("warningEventList", warningEventList);
			
			List<Map> warningLog = new ArrayList<Map>();			//告警事件日志
			jedisCache.saveObject("warningLog", warningLog);
			
			List<Map> deviceState = deviceDao.getNowDeviceState();	//设备状态，获取当前有效设备的状态
			jedisCache.saveObject("deviceState", deviceState);
			
			List<Map> handleList = new ArrayList<Map>();			//处理告警事件
			jedisCache.saveObject("handleList", handleList);	
			
			
			 * 梯控
			 
			List<Map> eleRecordList = new ArrayList<Map>();			//刷卡和触发事件
			jedisCache.saveObject("eleRecordList", eleRecordList);
			
		}
		
		//门禁
		if(doorSysQueue==null){
			doorSysQueue = new BoundedQueueUsedForDisplay(50);		//队列存储数10条
		}
		if(doorSysLatest==null){
			doorSysLatest = new BoundedQueueUsedForDisplay(5);		
		}
		
		//梯控
		if(elevaSysQueue==null){
			elevaSysQueue = new BoundedQueueUsedForDisplay(50);		
		}
		if(elevaSysLatest==null){
			elevaSysLatest = new BoundedQueueUsedForDisplay(5);		
		}
	}
	*/
		
}
