package com.kuangchi.sdd.baseConsole.event.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventModel;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventWarningModel;
import com.kuangchi.sdd.baseConsole.event.model.SearchEventModel;
import com.kuangchi.sdd.baseConsole.event.service.EventService;
import com.kuangchi.sdd.baseConsole.event.util.ExcelUtilSpecialCountBig;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.cacheUtils.CacheUtils;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.datastructure.BoundedQueueUsedForDisplay;
import com.kuangchi.sdd.util.file.DownloadFile;
import com.kuangchi.sdd.util.file.PropertyUtils;

@Controller("eventAction")
public class EventAction extends BaseActionSupport {
	@Autowired
	EventService eventService;
	@Resource(name = "startQuertz")
	Scheduler scheduler;

	// 备份文件绝对路径
	private String absolutePath;

	@Value("${backupPathWindows}")
	private String absolutePathWindows;

	@Value("${backupPathLinux}")
	private String absolutePathLinux;

	// 跳转到刷卡事件页面
	public String toEventPage() {
		return "success";
	}

	// 跳转到告警事件页面
	public String toWarningPage() {
		return "success";
	}

	@Override
	public Object getModel() {
		return null;
	}

	// huixian.pan 刷卡事件多条件查询
	public void searchEvent() {
		HttpServletRequest request = getHttpServletRequest();
		Grid<DeviceEventModel> grid = null;
		String data = request.getParameter("data");
		// HashMap<String, Object> map=new HashMap<String,Object>();
		SearchEventModel searchModel = GsonUtil.toBean(data,
				SearchEventModel.class);
		// DeviceEventModel
		// eventModel=GsonUtil.toBean(data,DeviceEventModel.class);

		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		grid = eventService.searchEventInfo(searchModel, skip, rows);

		printHttpServletResponse(GsonUtil.toJson(grid));

	}

	// huixian.pan 删除刷卡事件
	public void deleteEvent() {
		HttpServletRequest request = getHttpServletRequest();
		String eventIds = request.getParameter("data_ids");
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		eventService.deleteEventInfo(eventIds, login_User);
		JsonResult result = new JsonResult();
		result.setMsg("删除成功");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	// huixian.pan 告警事件多条件查询
	public void searchWarningEvent() {
		HttpServletRequest request = getHttpServletRequest();
		Grid<DeviceEventWarningModel> grid = null;
		String data = request.getParameter("data");
		// HashMap<String, Object> map=new HashMap<String,Object>();
		SearchEventModel searchModel = GsonUtil.toBean(data,
				SearchEventModel.class);
		// DeviceEventModel
		// eventModel=GsonUtil.toBean(data,DeviceEventModel.class);
		String deviceName = searchModel.getDeviceName();
		String doorName = searchModel.getDoorName();
		String cardNum = searchModel.getCardNum();
		String startDate = searchModel.getStartDate();
		String endDate = searchModel.getEndDate();

		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		grid = eventService.searchEventWarningInfo(deviceName, doorName,
				cardNum, startDate, endDate, skip, rows);
		printHttpServletResponse(GsonUtil.toJson(grid));

	}

	// huixian.pan 删除告警事件
	public void deleteWarningEvent() {
		HttpServletRequest request = getHttpServletRequest();
		String eventIds = request.getParameter("data_ids");
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		eventService.deleteEventWarningInfo(eventIds, login_User);

		JsonResult result = new JsonResult();
		result.setMsg("删除成功");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	// huixia.pan 刷卡事件导出数据
	public void reportData() {
		// 文件名获取
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		SearchEventModel searchModel = GsonUtil.toBean(data,
				SearchEventModel.class);

		String deviceName = searchModel.getDeviceName();
		String doorName = searchModel.getDoorName();
		String cardNum = searchModel.getCardNum();
		String startDate = searchModel.getStartDate();
		String endDate = searchModel.getEndDate();

		List<DeviceEventModel> events = eventService.exportEventInfo(
				deviceName, doorName, cardNum, startDate, endDate);
		/*
		 * for(int k=0;k<events.size();k++){
		 * if("0".equals(events.get(k).getEventType())){
		 * events.get(k).setEventType("门禁记录"); }else
		 * if("1".equals(events.get(k).getEventType())){
		 * events.get(k).setEventType("巡更记录"); }else{
		 * events.get(k).setEventType("刷卡记录"); } }
		 */
		String jsonList = GsonUtil.toJson(events);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置 列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> cloTitleList = new ArrayList<String>();

		cloTitleList.add("设备IP地址");
		cloTitleList.add("设备MAC地址");
		cloTitleList.add("设备名称");
		cloTitleList.add("门名称");
		// cloTitleList.add("事件类型");
		cloTitleList.add("卡号");
		cloTitleList.add("事件名称");
		cloTitleList.add("事件时间");

		colList.add("local_ip_address");
		colList.add("deviceMac");
		colList.add("deviceName");
		colList.add("doorName");
		// colList.add("eventType");
		colList.add("cardNum");
		colList.add("eventName");
		colList.add("eventDate");

		String[] colTitles = new String[colList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = cloTitleList.get(i);
		}

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "刷卡事件信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCountBig.exportExcel("刷卡事件列表",
					colTitles, cols, list);
			workbook.write(out);
			/* ExcelUtil.export(list,out); */
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 告警事件导出数据
	public void WarningReportData() {
		// 文件名获取
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		SearchEventModel searchModel = GsonUtil.toBean(data,
				SearchEventModel.class);

		String deviceName = searchModel.getDeviceName();
		String doorName = searchModel.getDoorName();
		String cardNum = searchModel.getCardNum();
		String startDate = searchModel.getStartDate();
		String endDate = searchModel.getEndDate();

		List<DeviceEventWarningModel> warningEvents = eventService
				.exportEventWarningInfo(deviceName, doorName, cardNum,
						startDate, endDate);
		/*
		 * for(int k=0;k<warningEvents.size();k++){
		 * if("0".equals(warningEvents.get(k).getEventType())){
		 * warningEvents.get(k).setEventType("门禁记录"); }else
		 * if("1".equals(warningEvents.get(k).getEventType())){
		 * warningEvents.get(k).setEventType("巡更记录"); }else{
		 * warningEvents.get(k).setEventType("刷卡记录"); } }
		 */
		String jsonList = GsonUtil.toJson(warningEvents);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置 列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> cloTitleList = new ArrayList<String>();

		cloTitleList.add("设备IP地址");
		cloTitleList.add("设备MAC地址");
		cloTitleList.add("设备名称");
		cloTitleList.add("门名称");
		// cloTitleList.add("事件类型");
		cloTitleList.add("事件名称");
		cloTitleList.add("事件时间");

		colList.add("local_ip_address");
		colList.add("deviceMac");
		colList.add("deviceName");
		colList.add("doorName");
		// colList.add("eventType");
		colList.add("eventName");
		colList.add("eventDate");

		String[] colTitles = new String[colList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = cloTitleList.get(i);
		}

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "告警事件信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCountBig.exportExcel("告警事件列表",
					colTitles, cols, list);
			workbook.write(out);
			/* ExcelUtil.export(list,out); */
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示刷卡事件
	 * 
	 * @author minting.he
	 */
	public void eventInfo() {
		HttpServletRequest request = getHttpServletRequest();
		Grid<DeviceEventModel> grid = eventService.searchEventInfo(null, 1, 10);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	/**
	 * 显示告警事件
	 * 
	 * @author minting.he
	 */
	public void eventWarningInfo() {
		Grid<DeviceEventWarningModel> grid = eventService
				.searchEventWarningInfo2(null, null, null, null, null, 0, 10000);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	/**
	 * 处理告警事件
	 * 
	 * @author minting.he
	 */
	public void dealEventWarning() {
		HttpServletRequest request = getHttpServletRequest();
		String eventId = request.getParameter("eventId");
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		if (EmptyUtil.atLeastOneIsEmpty(eventId)) {
			result.setMsg("数据错误");
		} else {
			String[] id = eventId.split(",");
			for (int i = 0; i < id.length; i++) {
				boolean r = eventService.dealEventWarning(id[i]);
				if (r) {
					result.setSuccess(true);
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/*
	 * 修改事件自动备份间隔
	 * 
	 * 
	 * 
	 * **
	 */
	public void changeEventBackupInterval() {
		HttpServletRequest request = getHttpServletRequest();

		String interval = request.getParameter("interval");
		LOG.info(interval);
		String cronExpression = "0 0 0 1/" + interval + " * ?";
		String propertyFile = request
				.getSession()
				.getServletContext()
				.getRealPath(
						"/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		JsonResult result = new JsonResult();
		boolean flag = com.kuangchi.sdd.util.file.PropertyUtils.setProperties(
				propertyFile, "eventBackupQuartzCron", cronExpression, null);
		if (flag) {
			try {
				TriggerKey triggerKey = new TriggerKey(
						"eventBackupQuartzTaskTimer");
				CronTriggerImpl trigger = (CronTriggerImpl) scheduler
						.getTrigger(triggerKey);
				trigger.setCronExpression(cronExpression);
				scheduler.rescheduleJob(triggerKey, trigger);
				result.setSuccess(true);
				result.setMsg("修改成功");
			} catch (Exception e) {
				result.setSuccess(false);
				result.setMsg("修改失败");
				e.printStackTrace();
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	public void getInitInterval() {
		HttpServletRequest request = getHttpServletRequest();
		String propertyFile = request
				.getSession()
				.getServletContext()
				.getRealPath(
						"/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		Properties property = PropertyUtils.readProperties(propertyFile);
		String ebqc = property.getProperty("eventBackupQuartzCron");
		String ewbqc = property.getProperty("eventWarningBackupQuartzCron");
		int index = ebqc.lastIndexOf("/");
		String inter = ebqc.substring(index + 1, index + 3);
		int index2 = ewbqc.lastIndexOf("/");
		String inter2 = ewbqc.substring(index2 + 1, index2 + 3);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("interval", inter);
		map.put("interval2", inter2);
		printHttpServletResponse(GsonUtil.toJson(map));
	}

	/*
	 * 修改告警事件自动备份间隔
	 * 
	 * 
	 * 
	 * **
	 */
	public void changeEventWarningBackupInterval() {
		HttpServletRequest request = getHttpServletRequest();
		String interval = request.getParameter("interval2");
		LOG.info(interval);
		String cronExpression = "0 0 0 1/" + interval + " * ?";
		String propertyFile = request
				.getSession()
				.getServletContext()
				.getRealPath(
						"/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		JsonResult result = new JsonResult();
		boolean flag = com.kuangchi.sdd.util.file.PropertyUtils.setProperties(
				propertyFile, "eventWarningBackupQuartzCron", cronExpression,
				null);

		if (flag) {
			try {
				TriggerKey triggerKey = new TriggerKey(
						"eventWarningBackupQuartzTaskTimer");
				CronTriggerImpl trigger = (CronTriggerImpl) scheduler
						.getTrigger(triggerKey);
				trigger.setCronExpression(cronExpression);
				scheduler.rescheduleJob(triggerKey, trigger);
				result.setSuccess(true);
				result.setMsg("修改成功");
			} catch (Exception e) {
				result.setSuccess(false);
				result.setMsg("修改失败");
				e.printStackTrace();
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/*
	 * 添加事件
	 * 
	 * 
	 * *
	 */

	public void addEventInfo() {
		HttpServletRequest request = getHttpServletRequest();
		String eventType = request.getParameter("eventType");
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		String mac = request.getParameter("mac");
		String doorNum = request.getParameter("doorNum");
		String cardNum = request.getParameter("cardNum");
		String eventDm = request.getParameter("eventDm");
		String eventDescription = request.getParameter("eventDescription");
		String eventDate = request.getParameter("eventDate");
		JsonResult result = new JsonResult();
		try {
			if (!"0".equals(eventType) && !"1".equals(eventType)) {
				throw new com.kuangchi.sdd.base.exception.MyException(
						"事件类型eventType不能为空，0表示刷卡事件 ，1表示告警事件");
			}

			if (null == mac || "".equals(mac.trim())) {
				throw new com.kuangchi.sdd.base.exception.MyException(
						"设备mac不能为空");
			}
			if (null == eventDm || "".equals(eventDm.trim())) {
				throw new com.kuangchi.sdd.base.exception.MyException(
						"事件代码eventNum不能为空");
			}

			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				simpleDateFormat.setLenient(false);
				simpleDateFormat.parseObject(eventDate);

			} catch (Exception e) {
				throw new com.kuangchi.sdd.base.exception.MyException(
						"eventDate日期格式不正确");
			}

			// 0 表示上报刷卡事件 1表示上报告警事件
			if ("0".equals(eventType)) {
				eventService.addEventInfo(eventType, mac, doorNum, cardNum,
						eventDm, eventDescription, eventDate, login_User);
			} else {
				eventService.addEventWarningInfo(eventType, mac, doorNum,
						cardNum, eventDm, eventDescription, eventDate,
						login_User);
			}
			result.setSuccess(true);
			result.setMsg("修改成功");
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setSuccess(false);
			result.setMsg(myException.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("修改失败");

		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	@Value("${redisConnectIp}")
	private String redisConnectIp;
	@Value("${redisConnectPort}")
	private String redisConnectPort;

	/**
	 * 显示最新5条刷卡事件
	 * 
	 * @author minting.he
	 */
	public void latestEventInfo() {
		// 从缓存中获取最新记录
		List<Object> list = new ArrayList<Object>();
		CacheUtils<BoundedQueueUsedForDisplay> cacheQueue = new CacheUtils<BoundedQueueUsedForDisplay>(
				redisConnectIp, Integer.valueOf(redisConnectPort));
		BoundedQueueUsedForDisplay l = cacheQueue.getObject("doorSysLatest");
		if (l == null) {
			l = new BoundedQueueUsedForDisplay(5);
			cacheQueue.saveObject("doorSysLatest", l);
		}
		list = l.list();
		for (Object obj : list) {
			Map<String, Object> map = (Map<String, Object>) obj;
			if (map.get("staff_name") == null
					|| "".equals(map.get("staff_name").toString())) {
				map.put("staff_name", "-");
			}
			if (map.get("staff_no") == null
					|| "".equals(map.get("staff_no").toString())) {
				map.put("staff_no", "-");
			}
			if (map.get("event_name") == null
					|| "".equals(map.get("event_name").toString())) {
				map.put("event_name", "-");
			}
			if (map.get("card_num") == null
					|| "".equals(map.get("card_num").toString())) {
				map.put("card_num", "-");
				map.put("staff_img", "");
			} else {
				String staff_img = eventService.getStaffImg(map.get("card_num")
						.toString());
				map.put("staff_img", staff_img);
			}
		}
		printHttpServletResponse(GsonUtil.toJson(list));

		/*
		 * List<DeviceEventModel> list = new ArrayList<DeviceEventModel>(); list
		 * = eventService.latestEventInfo(); for(DeviceEventModel e : list){
		 * if(e.getStaffName()==null || "".equals(e.getStaffName())){
		 * e.setStaffName("-"); } if(e.getStaffNo()==null ||
		 * "".equals(e.getStaffNo())){ e.setStaffNo("-"); } }
		 * printHttpServletResponse(GsonUtil.toJson(list));
		 */

	}

	/**
	 * 按时间区间备份刷卡事件
	 * 
	 * @author minting.he
	 */
	public void backUpEvent() {
		JsonResult result = new JsonResult();
		try {
			String osName = System.getProperties().getProperty("os.name");
			absolutePath = absolutePathLinux;
			if (osName.contains("Windows")) {
				absolutePath = absolutePathWindows;
			}
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			String startYear = request.getParameter("startYear");
			String endYear = request.getParameter("endYear");
			String startMonth = request.getParameter("startMonth");
			String endMonth = request.getParameter("endMonth");
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			String login_user = loginUser.getYhMc();
			if (EmptyUtil.atLeastOneIsEmpty(startYear, startMonth, endYear,
					endMonth)) {
				result.setSuccess(false);
				result.setMsg("备份失败，数据不合法");
			} else if (EmptyUtil.atLeastOneIsEmpty(login_user)) {
				result.setSuccess(false);
				result.setMsg("备份失败，请先登录");
			} else {
				SimpleDateFormat dateFormat1 = new SimpleDateFormat(
						"yyyy-MM-dd");
				SimpleDateFormat dateFormat2 = new SimpleDateFormat(
						"yyyy年MM月dd日");
				Calendar cal = Calendar.getInstance(); // 构造时间区间
				Date nowDate = cal.getTime();
				String nowTime = dateFormat2.format(nowDate);
				cal.set(Integer.valueOf(startYear),
						Integer.valueOf(startMonth) - 1, 01, 0, 0, 0);
				Date startDate = cal.getTime();
				String startStr = dateFormat1.format(startDate);
				Integer endY = Integer.valueOf(endYear);
				Integer endM = Integer.valueOf(endMonth) + 1;
				if (endM >= 13) {
					endY = endY + 1;
					endM = 1;
				}
				cal.set(endY, endM - 1, 01, 0, 0, 0);
				Date endDate = cal.getTime();
				String endStr = dateFormat1.format(endDate);
				Map map = new HashMap();
				map.put("startDate", startStr);
				map.put("endDate", endStr);
				List<DeviceEventModel> events = eventService
						.getEventInterval(map);// 获取时间区间内的刷卡事件
				if (events == null || events.size() == 0) {
					result.setSuccess(false);
					result.setMsg("该时间区间内无刷卡事件");
				} else {

					String jsonList = GsonUtil.toJson(events); // 导出刷卡
					List list = GsonUtil.getListFromJson(jsonList,
							ArrayList.class);
					// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					colTitleList.add("设备IP地址");
					colTitleList.add("MAC地址");
					colTitleList.add("设备名称");
					colTitleList.add("门号");
					colTitleList.add("门名称");
					colTitleList.add("员工名称");
					colTitleList.add("卡号");
					// colTitleList.add("事件类型");
					colTitleList.add("事件名称");
					colTitleList.add("事件时间");
					//colTitleList.add("事件描述");
					colList.add("local_ip_address");
					colList.add("deviceMac");
					colList.add("deviceName");
					colList.add("doorNum");
					colList.add("doorName");
					colList.add("staffName");
					colList.add("cardNum");
					// colList.add("eventType");
					colList.add("eventName");
					colList.add("eventDate");
					//colList.add("eventDesc");
					String[] colTitles = new String[colList.size()];
					String[] cols = new String[colList.size()];
					for (int i = 0; i < colList.size(); i++) {
						cols[i] = colList.get(i);
						colTitles[i] = colTitleList.get(i);
					}
					String fileName = nowTime + "刷卡事件备份表";
					File file = new File(absolutePath + fileName + ".xls");
					// 备份成功，文件全路径包括名称放进session
					request.getSession().setAttribute("downloadSwipeBackUp",
							absolutePath + fileName + ".xls");
					request.getSession().setAttribute("swipeBackUpName",
							fileName + ".xls");
					FileOutputStream fos = new FileOutputStream(file);
					Workbook workbook = ExcelUtilSpecialCountBig.exportExcel(
							"刷卡事件备份表", colTitles, cols, list);
					workbook.write(fos);
					fos.flush();
					fos.close();

					boolean r = eventService.delEventInterval(map, login_user);// 删除备份的刷卡事件
					if (r) {
						result.setSuccess(true);
						result.setMsg("备份成功，时间区间内的刷卡事件已被删除");
					} else {
						result.setSuccess(false);
						result.setMsg("备份失败");
					}

				}
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("备份失败");
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * 按时间区间备份告警事件
	 * 
	 * @author minting.he
	 */
	public void backUpWarningEvent() {
		JsonResult result = new JsonResult();
		try {
			String osName = System.getProperties().getProperty("os.name");
			absolutePath = absolutePathLinux;
			if (osName.contains("Windows")) {
				absolutePath = absolutePathWindows;
			}
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			String startYear = request.getParameter("startYear");
			String endYear = request.getParameter("endYear");
			String startMonth = request.getParameter("startMonth");
			String endMonth = request.getParameter("endMonth");
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			String login_user = loginUser.getYhMc();
			if (EmptyUtil.atLeastOneIsEmpty(startYear, startMonth, endYear,
					endMonth)) {
				result.setSuccess(false);
				result.setMsg("备份失败，数据不合法");
			} else if (EmptyUtil.atLeastOneIsEmpty(login_user)) {
				result.setSuccess(false);
				result.setMsg("备份失败，请先登录");
			} else {
				SimpleDateFormat dateFormat1 = new SimpleDateFormat(
						"yyyy-MM-dd");
				SimpleDateFormat dateFormat2 = new SimpleDateFormat(
						"yyyy年MM月dd日");
				Calendar cal = Calendar.getInstance(); // 构造时间区间
				Date nowDate = cal.getTime();
				String nowTime = dateFormat2.format(nowDate);
				cal.set(Integer.valueOf(startYear),
						Integer.valueOf(startMonth) - 1, 01, 0, 0, 0);
				Date startDate = cal.getTime();
				String startStr = dateFormat1.format(startDate);
				Integer endY = Integer.valueOf(endYear);
				Integer endM = Integer.valueOf(endMonth) + 1;
				if (endM >= 13) {
					endY = endY + 1;
					endM = 1;
				}
				cal.set(endY, endM - 1, 01, 0, 0, 0);
				Date endDate = cal.getTime();
				String endStr = dateFormat1.format(endDate);
				Map map = new HashMap();
				map.put("startDate", startStr);
				map.put("endDate", endDate);
				List<DeviceEventWarningModel> events = eventService
						.getWarningEventInterval(map);// 获取时间区间内的告警事件
				if (events == null || events.size() == 0) {
					result.setSuccess(false);
					result.setMsg("该时间区间内无告警事件");
				} else {

					for (int i = 0; i < events.size(); i++) {
						String state = events.get(i).getDealState();
						if ("0".equals(state)) {
							events.get(i).setDealState("未处理");
						} else {
							events.get(i).setDealState("已处理");
						}
					}
					String jsonList = GsonUtil.toJson(events); // 导出告警
					List list = GsonUtil.getListFromJson(jsonList,
							ArrayList.class);
					// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					colTitleList.add("设备IP地址");
					colTitleList.add("MAC地址");
					colTitleList.add("设备名称");
					colTitleList.add("门号");
					colTitleList.add("门名称");
					colTitleList.add("处理状态");
					//colTitleList.add("员工名称");
					//colTitleList.add("卡号");
					// colTitleList.add("事件类型");
					colTitleList.add("事件名称");
					colTitleList.add("事件时间");
					//colTitleList.add("事件描述");
					colList.add("local_ip_address");
					colList.add("deviceMac");
					colList.add("deviceName");
					colList.add("doorNum");
					colList.add("doorName");
					colList.add("dealState");
					//colList.add("staffName");
					//colList.add("cardNum");
					// colList.add("eventType");
					colList.add("eventName");
					colList.add("eventDate");
					//colList.add("eventDesc");
					String[] colTitles = new String[colList.size()];
					String[] cols = new String[colList.size()];
					for (int i = 0; i < colList.size(); i++) {
						cols[i] = colList.get(i);
						colTitles[i] = colTitleList.get(i);
					}
					String fileName = nowTime + "告警事件备份表";
					File file = new File(absolutePath + fileName + ".xls");
					// 备份成功，文件全路径包括名称放进session
					request.getSession().setAttribute("downloadWarmBackUp",
							absolutePath + fileName + ".xls");
					request.getSession().setAttribute("warmBackUpName",
							fileName + ".xls");
					FileOutputStream fos = new FileOutputStream(file);
					Workbook workbook = ExcelUtilSpecialCountBig.exportExcel(
							"告警事件备份表", colTitles, cols, list);
					workbook.write(fos);
					fos.flush();
					fos.close();

					boolean r = eventService.delWarningEventInterval(map,
							login_user);// 删除备份的告警事件
					if (r) {
						result.setSuccess(true);
						result.setMsg("备份成功，时间区间内的告警事件已被删除");
					} else {
						result.setSuccess(false);
						result.setMsg("备份失败");
					}

				}
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("备份失败");
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * 下载备份文件工具方法 xuewen.deng
	 * 
	 */
	public void downloadWarmBackup() {

		try {
			HttpServletRequest request = getHttpServletRequest();
			HttpServletResponse response = getHttpServletResponse();
			OutputStream out = null;

			// 通过文件路径获得File对象(文件夹路径+文件名称)
			File file = new File((String) request.getSession().getAttribute(
					"downloadWarmBackUp"));// 从session中获取备份文件的全路径
			String fileName = (String) request.getSession().getAttribute(
					"warmBackUpName");// 从session中获取备份文件的名称
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 1.设置文件ContentType类型
			response.setContentType("application/x-msdownload");
			String iso_filename = DownloadFile.toUtf8String(fileName);
			// 2.设置文件头：fileName参数是设置下载文件名
			response.setHeader("Content-Disposition", "attachment;filename="
					+ iso_filename);
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 下载备份文件工具方法 xuewen.deng
	 * 
	 */
	public void downloadSwipeBackup() {

		try {
			HttpServletRequest request = getHttpServletRequest();
			HttpServletResponse response = getHttpServletResponse();
			OutputStream out = null;

			// 通过文件路径获得File对象(文件夹路径+文件名称)
			File file = new File((String) request.getSession().getAttribute(
					"downloadSwipeBackUp"));// 从session中获取备份文件的全路径
			String fileName = (String) request.getSession().getAttribute(
					"swipeBackUpName");// 从session中获取备份文件的名称
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 1.设置文件ContentType类型
			response.setContentType("application/x-msdownload");
			String iso_filename = DownloadFile.toUtf8String(fileName);
			// 2.设置文件头：fileName参数是设置下载文件名
			response.setHeader("Content-Disposition", "attachment;filename="
					+ iso_filename);
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * 查询一周内各刷卡事件类型数量
	 * @author huixian.pan
	 * @param 
	 * @return
	 */
	public void getEventByDate(){
		List<Map> list=eventService.getEventByDate();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 查询某天的告警次数 
	 * @author huixian.pan
	 * @param 
	 * @return
	 */
	public void getWarningEventByDate(){
		Map map=eventService.getWarningEventByDate();
		printHttpServletResponse(GsonUtil.toJson(map));
	}
	
	/**
	 * 查询当前设备数，卡数，人员数
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public void getCourrentDeviceCardEmpCount(){
		Map map=eventService.getCourrentDeviceCardEmpCount();
		printHttpServletResponse(GsonUtil.toJson(map));
	}
	
	
}
