package com.kuangchi.sdd.baseConsole.times.timesobject.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.dao.HolidayTimesDao;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
import com.kuangchi.sdd.baseConsole.times.timesgroup.dao.TimesGroupDao;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;
import com.kuangchi.sdd.baseConsole.times.timesobject.dao.TimesObjectDao;
import com.kuangchi.sdd.baseConsole.times.timesobject.model.TimesObject;
import com.kuangchi.sdd.baseConsole.times.timesobject.service.TimesObjectService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.sun.mail.iap.ConnectionException;

/**
 * @创建人　: 梁豆豆
 * @创建时间: 2016-4-5 下午6:28:17
 * @功能描述: 对象时段组模块-业务实现类
 */
@Service("timesObjectServiceImpl")
public class TimesObjectServiceImpl implements TimesObjectService {
	
	@Resource(name = "timesObjectDaoImpl")
	private TimesObjectDao timesObjectDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "holidayTimesDaoImpl")
	private HolidayTimesDao holidayTimesDao;
	
	@Resource(name = "timesGroupDaoImp")
	private TimesGroupDao timesGroupDao;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;

	@Override
	public boolean addTimesObject(Map<String, Object> map) throws Exception {
	
		boolean result = true;
		
		String loginUserName = (String) map.get("loginUserName");
		String device_num  = (String) map.get("device_num");
		String group_nums = (String) map.get("group_nums");
		String device_mac = (String) map.get("device_mac");
		String object_type = (String) map.get("object_type");
		String device_type = (String) map.get("device_type");
		
		//删除绑定该设备的对象时段组
		timesObjectDao.deleteTimesObjectByDevice(object_type,device_num); 
				
		// 增加对象时段组
		if(!"".equals(group_nums)){
			String[] groupNums = group_nums.split(",");
			List<TimesObject> timesObjects = new ArrayList<TimesObject>();
			for (String groupNum: groupNums) {
				TimesObject timesObject = new TimesObject();
				timesObject.setObject_type(object_type);
				timesObject.setObject_num(device_num);
				timesObject.setGroup_num(groupNum.replace("'", ""));
				timesObject.setCreate_user(loginUserName);
				timesObjects.add(timesObject);
			}
			result = timesObjectDao.addTimesObject(timesObjects);
		} 
		
		//封装数据并调用接口
		String data = dataBuild(object_type, group_nums);
		interConnection(data, device_mac, device_type, object_type);
		
		//写入日志
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "对象时段组管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "新增对象时段组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public List<TimesObject> getTimesObjectByParam(TimesObject timesObject) {
		return timesObjectDao.getTimesObjectByParam(timesObject);
	}

	@Override
	public List<TimesObject> getTimesObjectByParamPage(TimesObject timesObject, int page,
			int size) {
		return timesObjectDao.getTimesObjectByParamPage(timesObject, page, size);
	}

	@Override
	public int getTimesObjectByParamCount(TimesObject timesObject) {
		return timesObjectDao.getTimesObjectByParamCount(timesObject);
	}
	
	@Override
	public List<HolidayTimesModel> getHolidayTimeByDevicePage(
			String device_num, int page, int size) {
		return timesObjectDao.getHolidayTimeByDevicePage(device_num, page, size);
	}
	
	@Override
	public List<HolidayTimesModel> getHolidayTimeByDevice(String device_num) {
		return timesObjectDao.getHolidayTimeByDevice(device_num);
	}

	@Override
	public int getHolidayTimeByDeviceCount(String device_num) {
		return timesObjectDao.getHolidayTimeByDeviceCount(device_num);
	}
	
	@Override
	public HolidayTimesModel getHolidayByNum (String holiday_time_num) {
		return timesObjectDao.getHolidayByNum(holiday_time_num);
	}

	@Override
	public String getGroupNameByNum(String group_num) {
		return timesObjectDao.getGroupNameByNum(group_num);
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-1 下午2:46:16
	 * @功能描述: 封装接口所需的数据
	 * @参数描述:
	 */
	public String dataBuild(String object_type, String group_nums){
		
		List<Object> outputData = new ArrayList<Object>();
		//根据对象类型进行不同封装
		if("1".equals(object_type)){
			int[] timesGroups = new int[1024];
			List<TimesGroup> timesGroupList = timesGroupDao.getTimesGroupsByNum(group_nums);

			for(int j=0;j<timesGroupList.size()/8;j++){ 
				         for(int k=0;k<8;k++){
				        	    int inx=j*8+k;
				                Integer groupNum=Integer.parseInt(timesGroupList.get(inx).getGroup_num());
				        	    int index=groupNum*64+8*k;
				                if (timesGroupList.get(inx).getTimes_one_num()!=null){
									Integer timesOneNum=Integer.valueOf(timesGroupList.get(inx).getTimes_one_num());
									timesGroups[index]=timesOneNum;
								}
								
								if (timesGroupList.get(inx).getTimes_two_num()!=null){
									Integer timesTwoNum=Integer.valueOf(timesGroupList.get(inx).getTimes_two_num());
									timesGroups[index+1]=timesTwoNum;
								}
								
								if (timesGroupList.get(inx).getTimes_three_num()!=null){
									Integer timesThreeNum=Integer.valueOf(timesGroupList.get(inx).getTimes_three_num());
									timesGroups[index+2]=timesThreeNum;
								}
								
								if (timesGroupList.get(inx).getTimes_four_num()!=null){
									Integer timesFourNum=Integer.valueOf(timesGroupList.get(inx).getTimes_four_num());
									timesGroups[index+3]=timesFourNum;
								}
								
								if (timesGroupList.get(inx).getTimes_five_num()!=null){
									Integer timesFiveNum=Integer.valueOf(timesGroupList.get(inx).getTimes_five_num());
									timesGroups[index+4]=timesFiveNum;
								}
								
								if (timesGroupList.get(inx).getTimes_six_num()!=null){
									Integer timesSixNum=Integer.valueOf(timesGroupList.get(inx).getTimes_six_num());
									timesGroups[index+5]=timesSixNum;
								}
								
								if (timesGroupList.get(inx).getTimes_seven_num()!=null){
									Integer timesSevenNum=Integer.valueOf(timesGroupList.get(inx).getTimes_seven_num());
									timesGroups[index+6]=timesSevenNum;
								}
								
								if (timesGroupList.get(inx).getTimes_eight_num()!=null){
									Integer timesEightNum=Integer.valueOf(timesGroupList.get(inx).getTimes_eight_num());
									timesGroups[index+7]=timesEightNum;
								} 
				         }
				
									
							   
					
			}
//			for(int k=8*(timesGroupList.size());k<1024;k++){
//				outputData.add("0");
//			}
			for (int i = 0; i < timesGroups.length; i++) {
				outputData.add(timesGroups[i]);
			}
		} else {
			List<HolidayTimesModel> HolidayTimes = holidayTimesDao.getByholidayNumDao(group_nums);
			
			if(HolidayTimes!=null && HolidayTimes.size()!=0){
				for(HolidayTimesModel tempHolidayTimes : HolidayTimes){
					Map<String, Object> map = new HashMap<String, Object>();
					String [] holiday_date = tempHolidayTimes.getHoliday_date().split("-");
					map.put("year",holiday_date[0]);
					map.put("month", holiday_date[1]);
					map.put("day", holiday_date[2]);
					map.put("dayOfWeek", tempHolidayTimes.getDay_of_week());
					outputData.add(map);
				}
			}
		}
		return GsonUtil.toJson(outputData);
	}

	/**
	 * @throws Exception 
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-1 下午2:46:31
	 * @功能描述: 连接服务器接口
	 * @参数描述:
	 */
	public void interConnection(String data, String device_mac, String device_type, String object_type) throws ConnectionException,Exception{
		/*Map<String, String> urlMap = new HashMap<String, String>();
		if (urlMap.isEmpty()) {
			urlMap = PropertiesToMap.propertyToMap("comm_interface.properties"); 
		}*/
		String deviceNum=mjCommService.getTkDevNumByMac(device_mac);
		String timeUrl = mjCommService.getMjCommUrl(deviceNum);
		String message = "";
		if("1".equals(object_type)){
			message = HttpRequest.sendPost(timeUrl+ "usergroup/setUserGroup.do?", "data="+data+"&mac="+device_mac+"&deviceType="+device_type);
		} else if ("2".equals(object_type)){
			message = HttpRequest.sendPost(timeUrl+ "holiday/setHoliday.do?", "data="+data+"&mac="+device_mac+"&deviceType="+device_type);
		}
		
		if(message==null || "".equals(message)){
			throw new ConnectionException();
		} else if("1".equals(GsonUtil.toBean(message, TimesObject.class).getResult_code())){
			throw new Exception();
		}
	}

	/**查询所有对象时段中的用户时段组（分页）*/
	@Override
	public List<TimesObject> getUserTimesGroup(TimesObject timesObject,
			int page, int size) {
		return timesObjectDao.getUserTimesGroup(timesObject, page, size);
	}
    
	/**查询所有对象时段中的用户时段组总行数*/
	@Override
	public int getUserTimesGroupCount(TimesObject timesObject) {
		return timesObjectDao.getUserTimesGroupCount(timesObject);
	}
	
	
	/**查询所有对象时段中的节假日时段组（分页）*/
	@Override
	public List<TimesObject> getHolidayTimesGroup(Map<String, Object> map) {
		return timesObjectDao.getHolidayTimesGroup(map);
	}

	/**查询所有对象时段中的节假日时段组总行数*/
	@Override
	public int getHolidayTimesGroupCount(Map<String, Object> map) {
		return timesObjectDao.getHolidayTimesGroupCount(map);
	}

	@Override
	public String getGroupNumByName(String group_name) {
		return timesObjectDao.getGroupNumByName(group_name);
	}

	
}

