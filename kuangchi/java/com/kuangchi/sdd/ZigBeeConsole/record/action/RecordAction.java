package com.kuangchi.sdd.ZigBeeConsole.record.action;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.ZigBeeConsole.record.model.RecordModel;
import com.kuangchi.sdd.ZigBeeConsole.record.service.IRecordService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * 光子锁记录 - action
 * @author yuman.gao
 */
@Controller("ZigBeeRecordAction")
public class RecordAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "ZigBeeRecordServiceImpl")
	private IRecordService recordService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-18 上午9:50:27
	 * @功能描述: 根据参数查询光子锁记录[分页]
	 * @参数描述:
	 */
	public void getRecordByParamPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		String data=request.getParameter("data");
		RecordModel record=GsonUtil.toBean(data, RecordModel.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		map.put("light_id", record.getLight_id());
		map.put("staff_name", record.getStaff_name());
		map.put("room_num", record.getRoom_num());
		List<Map> recordList =  recordService.getRecordByParamPage(map);
		Integer recordCount = recordService.getRecordByParamCount(map);
		
		Grid<Map> grid = new Grid<Map>();
		grid.setRows(recordList);
		grid.setTotal(recordCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-18 上午9:50:27
	 * @功能描述: 根据参数查询光子锁报警记录[分页]
	 * @参数描述:
	 */
	public void getWarningRecordByParamPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		String data=request.getParameter("data");
		RecordModel record=GsonUtil.toBean(data, RecordModel.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		map.put("room_num", record.getRoom_num());
		List<Map> recordList =  recordService.getWarningRecordByParamPage(map);
		Integer recordCount = recordService.getWarningRecordByParamCount(map);
		
		Grid<Map> grid = new Grid<Map>();
		grid.setRows(recordList);
		grid.setTotal(recordCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
}
