package com.kuangchi.sdd.baseConsole.event.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.dao.DeviceDao;
import com.kuangchi.sdd.baseConsole.event.dao.EventDao;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventModel;
import com.kuangchi.sdd.baseConsole.event.model.DeviceEventWarningModel;
import com.kuangchi.sdd.baseConsole.event.model.SearchEventModel;
import com.kuangchi.sdd.baseConsole.event.service.EventService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.log.model.Log;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.file.DownloadFile;

@Service("eventService")
public class EventServiceImpl  implements EventService{

	@Resource(name="eventDao")
	EventDao eventDao;
	@Resource(name="deviceDao")
	DeviceDao deviceDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	
	@Override
	public void addEventInfo(String eventType, String mac,
			String doorNum, String cardNum, String eventDm,
			String eventDescription, String eventDate,String loginUser) {
		String deviceNum=deviceDao.getDeviceNumByMac(mac);
		Map<String, String> log = new HashMap<String, String>();
		boolean result = eventDao.addEventInfo(eventType,deviceNum,mac, doorNum, cardNum, eventDm, eventDescription, eventDate);
		log.put("V_OP_NAME", "刷卡事件管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "新增刷卡事件");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
	}

	@Override
	public void addEventWarningInfo(String eventType, String mac,
			String doorNum, String cardNum, String eventDm,
			String eventDescription, String eventDate,String loginUser) {
		String deviceNum=deviceDao.getDeviceNumByMac(mac);
		Map<String, String> log = new HashMap<String, String>();
		boolean result =  eventDao.addEventWarningInfo(eventType, deviceNum, mac,doorNum, cardNum, eventDm, eventDescription, eventDate);
		log.put("V_OP_NAME", "警告事件管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "新增事件警告");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
	}

	@Override
	public void deleteEventInfo(String eventIds,String loginUser) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result =  eventDao.deleteEventInfo(eventIds);;
		log.put("V_OP_NAME", "刷卡事件管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "删除刷卡事件");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
	}

	@Override
	public void deleteEventWarningInfo(String eventIds,String loginUser) {
		boolean result = eventDao.deleteEventWarningInfo(eventIds);
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "警告事件管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "删除警告事件");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
	}

	@Override
	public Grid<DeviceEventModel> searchEventInfo(SearchEventModel model, Integer skip, Integer rows) {
        Grid<DeviceEventModel> grid = new Grid<DeviceEventModel>();
        List<DeviceEventModel> resultList = eventDao
               .searchEventInfo(model, skip, rows);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(eventDao.searchEventInfoCount(model));
        } else {
            grid.setTotal(0);
        }
        return grid;
	}

	@Override
	public Grid<DeviceEventWarningModel> searchEventWarningInfo(
			String deviceName,
			String doorName, String cardNum, String startDate,String endDate,Integer skip, Integer rows) {
        Grid<DeviceEventWarningModel> grid = new Grid<DeviceEventWarningModel>();
        List<DeviceEventWarningModel> resultList = eventDao
               .searchEventWarningInfo(deviceName, doorName, cardNum,startDate,endDate, skip, rows);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(eventDao.searchEventWarningInfoCount(deviceName, doorName, cardNum, startDate, endDate));
        } else {
            grid.setTotal(0);
        }
        return grid;
	}

	@Override
	public Grid<DeviceEventWarningModel> searchEventWarningInfo2(
			String deviceName,
			String doorName, String cardNum, String startDate,String endDate,Integer skip, Integer rows) {
        Grid<DeviceEventWarningModel> grid = new Grid<DeviceEventWarningModel>();
        List<DeviceEventWarningModel> resultList = eventDao
               .searchEventWarningInfo2(deviceName, doorName, cardNum,startDate,endDate, skip, rows);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(eventDao.searchEventWarningInfoCount2(deviceName, doorName, cardNum, startDate, endDate));
        } else {
            grid.setTotal(0);
        }
        return grid;
	}
	
	@Override
	public boolean dealEventWarning(String eventId) {
		return eventDao.dealEventWarning(eventId);
		
	}

	@Override
	public void backupEvent(String absolutePath) {
		String fileName="刷卡事件"+DateUtil.getDateString(new Date(), "yyyyMMddHHmmss")+".xls";
		fileName = DownloadFile.toUtf8String(fileName);
		FileOutputStream fos=null;
		 List<Map<String, Object>> list=eventDao.getAllEvents();	
		 List<String> columns=eventDao.getEventColumns();
		 
		try{
			 String[] colTitles =new String[columns.size()];	
			 String[] cols =new String[columns.size()];	
			for(int i=0;i<columns.size();i++){
				colTitles[i]=columns.get(i);
				cols[i]=columns.get(i);
			}

		fos=new FileOutputStream(absolutePath+fileName);
        Workbook workbook = ExcelUtilSpecial.exportExcel("刷卡事件",colTitles, cols, list);
        workbook.write(fos);
        fos.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=fos){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
        for(Map map:list){
        	eventDao.deleteEvent(map);
        } 
		
	}

	@Override
	public void backupEventWarning(String absolutePath) {
		String fileName="告警事件"+DateUtil.getDateString(new Date(), "yyyyMMddHHmmss")+".xls";
		fileName = DownloadFile.toUtf8String(fileName);
		FileOutputStream fos=null;
		 List<Map<String, Object>> list=eventDao.getAllEventWarnings();
		 List<String> columns=eventDao.getEventWarningColumns();
		try{
			 String[] colTitles =new String[columns.size()];	
			 String[] cols =new String[columns.size()];	
			for(int i=0;i<columns.size();i++){
				colTitles[i]=columns.get(i);
				cols[i]=columns.get(i);
			}
		fos=new FileOutputStream(absolutePath+fileName);
        Workbook workbook = ExcelUtilSpecial.exportExcel("告警事件",colTitles, cols, list);
        workbook.write(fos);
        fos.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=fos){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			
        for(Map map:list){
        	eventDao.deleteEventWarning(map);
        } 
		
	}

	@Override
	public List<DeviceEventModel> exportEventInfo(String deviceName,
			String doorName, String cardNum, String startDate, String endDate) {
		 List<DeviceEventModel> resultList =eventDao.exportEventInfo(deviceName, doorName, cardNum, startDate, endDate);
		 return resultList;
	}

	@Override
	public List<DeviceEventWarningModel> exportEventWarningInfo(String deviceName,
			String doorName, String cardNum, String startDate, String endDate) {
		 List<DeviceEventWarningModel> resultList =eventDao.exportWarningEventInfo(deviceName, doorName, cardNum, startDate, endDate);
		 return resultList;
	}

	@Override
	public List<DeviceEventModel> latestEventInfo(){
		return eventDao.latestEventInfo();
	}
	
	@Override
	public List<DeviceEventModel> getEventInterval(Map map){
		try{
			return eventDao.getEventInterval(map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean delEventInterval(Map map, String login_user){
		Map<String,String> log = new HashMap<String,String>();
		try{
			boolean result = eventDao.delEventInterval(map);
			if(result){
				log.put("V_OP_TYPE", "业务");
			}else{
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "刷卡事件管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除备份刷卡事件");
			logDao.addLog(log);
		}
	}

	@Override
	public List<DeviceEventWarningModel> getWarningEventInterval(Map map){
		try{
			return eventDao.getWarningEventInterval(map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean delWarningEventInterval(Map map, String login_user){
		Map<String,String> log = new HashMap<String,String>();
		try{
			boolean result = eventDao.delWarningEventInterval(map);
			if(result){
				log.put("V_OP_TYPE", "业务");
			}else{
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "告警事件管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除备份告警事件");
			logDao.addLog(log);
		}
	}
	
	@Override
	public String getStaffImg(String card_num){
		try{
			return eventDao.getStaffImg(card_num);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询一周内各刷卡事件类型数量
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	@Override
	public List<Map> getEventByDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Map>  list = new ArrayList();// 周一到周五异常事件发生周期数量数组
		Map m = new HashMap();// 查询条件Map

		List<String> days = getPerDayInWeek();
		m.put("begin_date", days.get(0));
		m.put("end_date", days.get(6));
		list= eventDao.getEventByDate(m);
		
		return list;
	}
	
	/**
	 * 查询某天的告警次数 
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	@Override
	public Map getWarningEventByDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList arr = new ArrayList();// 周一到周五异常事件发生周期数量数组
		Map map = new HashMap();// 返回数据Map
		Map m = new HashMap();// 查询条件Map
		
		try {
			Date today = df.parse(df.format(new Date()));
			List<String> days = getPerDayInWeek();
			for (String day : days) {
				int count = 0;
				Date d = df.parse(day);
				if (d.before(today) || d.compareTo(today) == 0) {
					m.put("event_date", day);
					count = eventDao.getWarningEventByDate(m);
					arr.add(count);
				} else {
					arr.add(0);
				}
			}
			map.put("data", arr);
			map.put("date", days);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	// 获取周一到周日日期 by huixian.pan
	public List<String> getPerDayInWeek() {
		List<String> days = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Calendar cal3 = Calendar.getInstance();
		Calendar cal4 = Calendar.getInstance();
		Calendar cal5 = Calendar.getInstance();
		Calendar cal6 = Calendar.getInstance();
		Calendar cal7 = Calendar.getInstance();

		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal2.setTime(new Date());
		cal2.add(Calendar.DAY_OF_MONTH, -1);
		cal2.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		cal3.setTime(new Date());
		cal3.add(Calendar.DAY_OF_MONTH, -1);
		cal3.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		cal4.setTime(new Date());
		cal4.add(Calendar.DAY_OF_MONTH, -1);
		cal4.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		cal5.setTime(new Date());
		cal5.add(Calendar.DAY_OF_MONTH, -1);
		cal5.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		cal6.setTime(new Date());
		cal6.add(Calendar.DAY_OF_MONTH, -1);
		cal6.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		cal7.setTime(new Date());
		cal7.add(Calendar.DAY_OF_MONTH, -1);
		cal7.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal7.add(Calendar.WEEK_OF_YEAR, 1);

		String monday = sdf.format(cal.getTime());
		String tuesday = sdf.format(cal2.getTime());
		String wednesday = sdf.format(cal3.getTime());
		String thursday = sdf.format(cal4.getTime());
		String friday = sdf.format(cal5.getTime());
		String saturday = sdf.format(cal6.getTime());
		String sunday = sdf.format(cal7.getTime());

		days.add(monday);
		days.add(tuesday);
		days.add(wednesday);
		days.add(thursday);
		days.add(friday);
		days.add(saturday);
		days.add(sunday);

		return days;
	}
	
	/**
	 * 查询当前设备数，卡数，人员数
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public Map getCourrentDeviceCardEmpCount(){
		return  eventDao.getCourrentDeviceCardEmpCount();
		
	}
	
}
