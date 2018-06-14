package com.kuangchi.sdd.baseConsole.times.holidaytimes.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.dao.HolidayTimesDao;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesInterf;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.service.HolidayTimesService;
import com.kuangchi.sdd.baseConsole.times.times.dao.TimesDao;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-5-24上午9:50:35
 * @功能描述:节假日时段管理-业务类（service）实现类
 * @参数描述:
 */
@Service("holidayTimesServiceImpl")
public class HolidayTimesServiceImpl implements HolidayTimesService {
	
	@Resource(name="holidayTimesDaoImpl")
	private HolidayTimesDao holidayTimesDaoImpl;
	@Resource(name = "timesDaoImpl")
	private TimesDao timesDaoImpl;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	@Override
	public Grid<HolidayTimesModel> getHolidayTimesByParamService(
			HolidayTimesModel holidayDate, String exist_nums, int page, int size) {
		
		Grid<HolidayTimesModel> grid=new Grid<HolidayTimesModel>();
		List<HolidayTimesModel> holidayTimesList=holidayTimesDaoImpl.getHolidayTimesByParam(holidayDate, exist_nums, page, size);
		grid.setRows(holidayTimesList);
		
		if (null !=holidayTimesList){
			grid.setTotal(holidayTimesDaoImpl.countHolidayTimes(holidayDate, exist_nums));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public boolean insertHolidayTimesService(HolidayTimesModel holidayTimes,String logUser) throws Exception {
		//统计总记录数（调用接口用）
		int htTableSize=holidayTimesDaoImpl.countHolidayTimes(holidayTimes,null);
		if(htTableSize>128){
			return false;
		} else {
			boolean result=holidayTimesDaoImpl.insertHolidayTimes(holidayTimes);
			
			Map<String, String> log = new HashMap<String, String>();
			log.put("V_OP_NAME","节假日时段管理");
			log.put("V_OP_FUNCTION","新增");
			log.put("V_OP_ID", logUser);
			log.put("V_OP_MSG", "新增节假日时段");
			if(result){
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			logDao.addLog(log);
			return result;
		}
	}
	

	@Override
	public boolean updateHolidayTimesService(HolidayTimesModel holidayTimes,String logUser) throws Exception {
		boolean result=holidayTimesDaoImpl.updateHolidayTimes(holidayTimes);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME","节假日时段管理");
		log.put("V_OP_FUNCTION","修改");
		log.put("V_OP_ID", logUser);
		log.put("V_OP_MSG", "修改节假日时段");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		return result;
	}

	@Override
	public boolean delHolidayTimesService(String holidayTimes_ids,String logUser) throws Exception {
		boolean result=holidayTimesDaoImpl.delHolidayTimes(holidayTimes_ids);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME","节假日时段管理");
		log.put("V_OP_FUNCTION","删除");
		log.put("V_OP_ID", logUser);
		log.put("V_OP_MSG", "删除节假日时段");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		return result;
	}

	@Override
	public List<HolidayTimesModel> exportHolidayTimesService(HolidayTimesModel holidayDate) {
		return holidayTimesDaoImpl.exportHolidayTimes(holidayDate);
	}

	@Override
	public List<HolidayTimesInterf> getHolidayTimesInterService() {
		return holidayTimesDaoImpl.getHolidayTimesInter();
	}
	
	/**
	 * 调用接口的传参
	 */
	public String ParamHolidayTimes(){
		List<HolidayTimesInterf> holidayTimesList=holidayTimesDaoImpl.getHolidayTimesInter();
		if(holidayTimesList!=null && holidayTimesList.size()!=0){
			List<Object> list = new ArrayList<Object>();
			for(HolidayTimesInterf htInterf : holidayTimesList){
				String [] htInterfStr=htInterf.getHoliday_dateInter().split("-");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("year",htInterfStr[0]);
				map.put("month", htInterfStr[1]);
				map.put("day", htInterfStr[2]);
				map.put("dayOfWeek", htInterf.getDay_of_weekInter());
				
				list.add(map);
			}
			String data = GsonUtil.toJson(list);
			return data;
		}
		return null;
	}
	

	@Override
	public List<HolidayTimesModel> getByholidayNumService(String ht_num) {
		return holidayTimesDaoImpl.getByholidayNumDao(ht_num);
	}
	
	
	/**
	 * 查询所有设备
	 */
	public List<DeviceInfo> getAllDevice(){
		return timesDaoImpl.getAllDevice();
	}
	
	
	/**
	 * 接口调用方法
	 * @throws Exception 
	 * */
	void InterfaceMethod() throws Exception{
		List<DeviceInfo> devices = getAllDevice();
		String data=ParamHolidayTimes();
		//接口调用
		Map<String, String> urlMap = new HashMap<String, String>();
		if (urlMap.isEmpty()) {
			urlMap = PropertiesToMap.propertyToMap("comm_interface.properties"); 
		}
		for (DeviceInfo device : devices) {
			String timeUrl = mjCommService.getMjCommUrl(device.getDevice_num());	
			String str = HttpRequest.sendPost(timeUrl+ "holiday/setHoliday.do?", "data="+data+"&mac="+device.getDevice_mac());
			HolidayTimesInterf getStr=GsonUtil.toBean(str,HolidayTimesInterf.class);
			if(getStr==null || "1".equals(getStr.getResult_code())){
				throw new Exception();
			}
		}
	}
}
