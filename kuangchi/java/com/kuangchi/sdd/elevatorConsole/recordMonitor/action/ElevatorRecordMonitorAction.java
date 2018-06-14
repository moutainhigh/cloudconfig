package com.kuangchi.sdd.elevatorConsole.recordMonitor.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.elevatorConsole.recordMonitor.service.IElevatorRecordMonitorService;
import com.kuangchi.sdd.interfaceConsole.JedisCache.service.impl.JedisCache;
import com.kuangchi.sdd.util.cacheUtils.CacheUtils;
import com.kuangchi.sdd.util.datastructure.BoundedQueueUsedForDisplay;

/**
 * 实时监控（梯控刷卡记录）
 * @author yuman.gao
 */
@Controller("elevatorRecordMonitorAction")
public class ElevatorRecordMonitorAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "elevatorRecordMonitorServiceImpl")
	private IElevatorRecordMonitorService elevatorRecordMonitorService;
	
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 查询所有梯控刷卡记录[分页]
	 * @author yuman.gao
	 */
	public void getAllRecord(){
		HttpServletRequest request = getHttpServletRequest();
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("page", (page - 1) * rows);
		param.put("rows", rows);
		List<Map<String, Object>> recordList =  elevatorRecordMonitorService.getAllRecord(param);
		Integer recordCount = elevatorRecordMonitorService.getAllRecordCount(param);
		
		Grid<Map<String, Object>> grid = new Grid<Map<String, Object>>();
		grid.setRows(recordList);
		grid.setTotal(recordCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	@Value("${redisConnectIp}")
	private String redisConnectIp;
	@Value("${redisConnectPort}")
	private String redisConnectPort;
	
	/**
	 * 从缓存中读取最新记录
	 * @author minting.he
	 */
	public void readCacheRecord(){
		List<Object> list = new ArrayList<Object>();
		try{
			CacheUtils<BoundedQueueUsedForDisplay> cacheQueue = new CacheUtils<BoundedQueueUsedForDisplay>(redisConnectIp, Integer.valueOf(redisConnectPort));
				BoundedQueueUsedForDisplay q = cacheQueue.getObject("eleSysQueue");
				if(q==null){
					q = new BoundedQueueUsedForDisplay(50);
					cacheQueue.saveObject("eleSysQueue", q);
				}
				list = q.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			printHttpServletResponse(new Gson().toJson(list));
		}
	}
	
}
