package com.kuangchi.sdd.elevatorConsole.elevatorReport.quardz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.elevatorConsole.device.dao.ITKDeviceDao;
import com.kuangchi.sdd.elevatorConsole.device.model.CommDevice;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.impl.ElevatorRecordReportDaoImpl;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorRecordInfo;
import com.kuangchi.sdd.interfaceConsole.JedisCache.service.impl.JedisCache;
import com.kuangchi.sdd.util.cacheUtils.CacheUtils;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

/**
 * 梯控写缓存记录定时器
 * @author minting.he
 *
 */
@Transactional
public class WriteElevatorRecordQuardz {
	
	private ICronService cronService;
	private ElevatorRecordReportDaoImpl elevatorRecordReportDao;
	
	public ICronService getCronService() {
		return cronService;
	}
	public void setCronService(ICronService cronService) {
		this.cronService = cronService;
	}
	public ElevatorRecordReportDaoImpl getElevatorRecordReportDao() {
		return elevatorRecordReportDao;
	}
	public void setElevatorRecordReportDao(
			ElevatorRecordReportDaoImpl elevatorRecordReportDao) {
		this.elevatorRecordReportDao = elevatorRecordReportDao;
	}

	@Value("${redisConnectIp}")
	private String redisConnectIp;
	@Value("${redisConnectPort}")
	private String redisConnectPort;
	
	
	/**
	 * 将缓存中的记录写进数据库
	 * @author minting.he
	 */
	public void writeRecord(){
		//集群访问时，只有与数据库中相同的IP地址可以执行定时器的业务操作
		boolean r = cronService.compareIP();	
		if(r){
			CacheUtils<List<Map<String, Object>>> cacheList = new CacheUtils<List<Map<String,Object>>>(redisConnectIp, Integer.valueOf(redisConnectPort));
			List<Map<String, Object>> eleRecordMap = new ArrayList<Map<String, Object>>();
			try{
				List<Map<String, Object>> eleRecordList = cacheList.getObject("eleRecordList");
				eleRecordMap = eleRecordList;
				if(eleRecordList!=null){
					for(int i=0; i<eleRecordList.size(); i++){
						Map<String, Object> map = eleRecordList.get(i);
						boolean result = elevatorRecordReportDao.insertEventRecord(map);
						if(result){
							eleRecordList.remove(i);
							--i;
						}else {
							throw new RuntimeException();
						}
					}
				}else {
					eleRecordList = new ArrayList<Map<String, Object>>();
				}
				cacheList.saveObject("eleRecordList", eleRecordList);
			}catch(Exception e){		//抛出RunningException回滚事务
				e.printStackTrace();
				cacheList.saveObject("eleRecordList", eleRecordMap);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
	}
	
}
