package com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.dao.DeviceDao;
import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.dao.HolidaySetUpDao;
import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.model.HolidaySetUp;
import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.service.HolidaySetUpService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.baseConsole.times.timesobject.model.TimesObject;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.sun.mail.iap.ConnectionException;


@Service("holidaySetUpServiceImpl")
public class HolidaySetUpServiceImpl implements HolidaySetUpService {

	@Resource(name = "holidaySetUpDaoImpl")
	private HolidaySetUpDao holidaySetUpDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	@Resource(name = "deviceDao")
	DeviceDao deviceDao;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	@Override
	public Grid<HolidaySetUp> getHolidayTimesOfDevice(Map map) {
		Grid<HolidaySetUp> grid = new Grid<HolidaySetUp>();
		List<HolidaySetUp> list =holidaySetUpDao.getHolidayTimesOfDevice(map);
		grid.setRows(list);
		if(null!=list){
		Integer count =	holidaySetUpDao.getHolidayTimesOfDeviceCount(map);
		grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}
	
	@Override
	public boolean getMaxHolidayTimesNum(HolidaySetUp holidaySetUp) {
		return holidaySetUpDao.getMaxHolidayTimesNum(holidaySetUp);
	}
	@Override
	public Integer getHolidayDateOfDevice(HolidaySetUp holidaySetUp) {
		return holidaySetUpDao.getHolidayDateOfDevice(holidaySetUp);
	}
	public boolean addHolidayTimes(HolidaySetUp holidaySetUp,String logUser){
		boolean result=holidaySetUpDao.addHolidayTimes(holidaySetUp);
		
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
	@Override
	public boolean updateHolidayTimes(HolidaySetUp holidaySetUp,String logUser)  {
		boolean result=holidaySetUpDao.updateHolidayTimes(holidaySetUp);
		
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
	public boolean delHolidayTimes(String device_num){
		boolean result=holidaySetUpDao.delHolidayTimes(device_num);
		return result;
	}
	
	/**
	 * 下发节假日时段
	 */
	@Override
	public void addTimesObject(Map<String, Object> map) throws  Exception {
		
		String device_num  = (String) map.get("device_num");
		String device_mac = (String) map.get("device_mac");
		String device_type = (String) map.get("device_type");
		//封装数据并调用接口
		String data = dataBuild(device_num);
		interConnection(data, device_mac, device_type);
		
	}
	/**
	 * @创建人　: 高育漫  
	 * @创建时间: 2016-6-1 下午2:46:16
	 * @功能描述: 封装接口所需的数据
	 * @其他: 改 chudan.guo 2016-10-10
	 */
	public String dataBuild(String device_num){
		
		List<Object> outputData = new ArrayList<Object>();
		List<HolidaySetUp> holidaySetUp = holidaySetUpDao.getByHolidayNum(device_num);
		if(holidaySetUp!=null && holidaySetUp.size()!=0){
			for(HolidaySetUp tempholidaySetUp : holidaySetUp){
				Map<String, Object> map = new HashMap<String, Object>();
				String [] holiday_date = tempholidaySetUp.getHoliday_date().split("-");
				map.put("year",holiday_date[0]);
				map.put("month", holiday_date[1]);
				map.put("day", holiday_date[2]);
				map.put("dayOfWeek", tempholidaySetUp.getDay_of_week());
				outputData.add(map);
			}
		}
		return GsonUtil.toJson(outputData);
	}


	/**
	 * @throws Exception 
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-1 下午2:46:31
	 * @功能描述: 连接服务器接口
	 * @其他: 改 chudan.guo 2016-10-10
	 */
	public void interConnection(String data, String device_mac, String device_type) throws ConnectionException,Exception{
		/*Map<String, String> urlMap = new HashMap<String, String>();
		if (urlMap.isEmpty()) {
			urlMap = PropertiesToMap.propertyToMap("comm_interface.properties"); 
		}*/
		String deviceNum=mjCommService.getTkDevNumByMac(device_mac);
		String timeUrl = mjCommService.getMjCommUrl(deviceNum);
		String message = "";
		message = HttpRequest.sendPost(timeUrl+ "holiday/setHoliday.do?", "data="+data+"&mac="+device_mac+"&device_type="+device_type);
		if(message==null || "".equals(message)){
			throw new ConnectionException();
		} else if("1".equals(GsonUtil.toBean(message, TimesObject.class).getResult_code())){
			throw new Exception();
		}
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-11 
	 * @功能描述: 查看设备已下发节假日时段
	 */
	public String watchHolidayTimes(String device_mac){
		/*Map<String, String> urlMap = new HashMap<String, String>();
		if (urlMap.isEmpty()) {
			urlMap = PropertiesToMap.propertyToMap("comm_interface.properties"); 
		}*/
		String message = "";
		try{
			String deviceNum=mjCommService.getTkDevNumByMac(device_mac);
			String deviceType=deviceDao.getMacByDeviceNum(deviceNum).get("deviceType").toString();
			String timeUrl = mjCommService.getMjCommUrl(deviceNum);
			message = HttpRequest.sendPost(timeUrl+ "holiday/getHoliday.do?", "mac="+device_mac+"&deviceType="+deviceType);
			if(message==null){
				return "";
			}else {
				return message;
			}
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	
	@Override
	public boolean copyHolidayTimes(String device_num, String deviceNum) {
		List<HolidaySetUp> holidaySetUp = holidaySetUpDao.getByHolidayNum(device_num);
		if(holidaySetUp!=null && holidaySetUp.size()!=0){
			delHolidayTimes(deviceNum);
			for(HolidaySetUp tempholidaySetUp : holidaySetUp){
				tempholidaySetUp.setDevice_num(deviceNum);
				holidaySetUpDao.addHolidayTimes(tempholidaySetUp);
			}
			return true;
		}else{
			delHolidayTimes(deviceNum);
			return true;
		}
	}

	@Override
	public boolean copyAndIssuedHolidayTimes(String device_num,String deviceNum) throws  Exception {
		    boolean msg=copyHolidayTimes(device_num, deviceNum);
	    	if(msg){
		    	Map deviceMap=holidaySetUpDao.getDeviceMacAndType(deviceNum);
		    	String device_mac =(String) deviceMap.get("device_mac");
		    	String device_type=(String) deviceMap.get("device_type");
		    	String data = dataBuild(device_num);
				interConnection(data, device_mac, device_type);
	    	}else{
	    		return false;
	    	}
	    return true;
	}

	@Override
	public Map getDeviceMacAndType(String device_num) {
		return holidaySetUpDao.getDeviceMacAndType(device_num);
	}
	
	@Override
	public boolean delHolidayById(String ids, String login_user){
		Map<String, String> log = new HashMap<String, String>();
		try{
			boolean result = holidaySetUpDao.delHolidayById(ids);
			if(result){
				log.put("V_OP_TYPE", "业务");
			}else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e){
			log.put("V_OP_TYPE", "异常");
			e.printStackTrace();
			return false;
		}finally{
			log.put("V_OP_NAME", "节假日时段管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除节假日时段");
			logDao.addLog(log);
		}
	}
	
}
