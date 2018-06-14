package com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.kuangchi.sdd.baseConsole.device.dao.DeviceDao;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.dao.DeviceTimesDao;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.service.DeviceTimesService;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.dao.TimesGroupDao;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.model.TimesGroup;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.service.TimesGroupService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.sun.mail.iap.ConnectionException;


/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-4-6 15:15:26
 * @功能描述: 时段组-业务实现类
 */
@Transactional
@Service("deviceTimesGroupService")
public class TimesGroupServiceImp implements TimesGroupService {

	
	@Resource(name = "deviceTimesGroupDao")
	private TimesGroupDao timesGroupDao;
	
	@Resource(name = "deviceDao")
	DeviceDao deviceDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "DevicetimesServiceImpl")
	private DeviceTimesService devicetimesService;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	@Override
	public List<TimesGroup> getTimesGroupByParam(Map<String, Object> map) {
		return timesGroupDao.getTimesGroupByParam(map);
	}
	
	@Override
	public Integer getTimesGroupCount(Map<String, Object> map) {
		return timesGroupDao.getTimesGroupCount(map);
	}
	
	@Override
	public boolean modifyTimesGroup(List<TimesGroup> timesGroupList,String loginUser) {
		boolean result = false;
		for (TimesGroup timesGroup : timesGroupList) {
			result = timesGroupDao.modifyTimesGroup(timesGroup);
		}
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "时段组管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "修改时段组信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}
	
	@Override
	public List<TimesGroup> getDeviceTimesGroupByNum(Map<String, Object> map) {
		return timesGroupDao.getDeviceTimesGroupByNum(map);
	}
	
	@Override
	public List<TimesGroup> getTimesGroupDetailByNum(Map<String, Object> map) {
		//return timesGroupDao.getDeviceTimesGroupByNum(map);
		List<TimesGroup> timesGroups = timesGroupDao.getDeviceTimesGroupByNum(map);
		String device_num = (String) map.get("device_num");
		for(int i=0; i<timesGroups.size(); i++){
			TimesGroup timesGroup = timesGroups.get(i);
			timesGroup.setTimes_one_num(getTimes(timesGroup.getTimes_one_num(),device_num));
			timesGroup.setTimes_two_num(getTimes(timesGroup.getTimes_two_num(),device_num));
			timesGroup.setTimes_three_num(getTimes(timesGroup.getTimes_three_num(),device_num));
			timesGroup.setTimes_four_num(getTimes(timesGroup.getTimes_four_num(),device_num));
			timesGroup.setTimes_five_num(getTimes(timesGroup.getTimes_five_num(),device_num));
			timesGroup.setTimes_six_num(getTimes(timesGroup.getTimes_six_num(),device_num));
			timesGroup.setTimes_seven_num(getTimes(timesGroup.getTimes_seven_num(),device_num));
			timesGroup.setTimes_eight_num(getTimes(timesGroup.getTimes_eight_num(),device_num));
		}
		return timesGroups;
	}
	
	@Override
	public List<DeviceTimes> getAllTimesSortByBeginTime(Map<String, Object> map) {
		return timesGroupDao.getAllTimesSortByBeginTime(map);
	}

	@Override
	public boolean issuedTimesGroup(String device_num, String device_mac, String device_type) throws ConnectionException,Exception {
	
		// 封装数据
		List<Object> outputData = new ArrayList<Object>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", device_num);
		List<TimesGroup> timesGroupList = timesGroupDao.getDeviceTimesGroupByNum(map);
		
		int[] timesGroups = new int[1024];
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
		for (int i = 0; i < timesGroups.length; i++) {
			outputData.add(timesGroups[i]);
		}
		
		
		// 调用接口
		/*Map<String, String> urlMap = new HashMap<String, String>();
		if (urlMap.isEmpty()) {
			urlMap = PropertiesToMap.propertyToMap("comm_interface.properties"); 
		}*/
		String timeUrl =mjCommService.getMjCommUrl(device_num);
		String data = GsonUtil.toJson(outputData);
		String message = HttpRequest.sendPost(timeUrl+ "usergroup/setUserGroup.do?", 
				"data="+data+"&mac="+device_mac+"&deviceType="+device_type);
		
		if(message==null || "".equals(message)){
			throw new ConnectionException();
		} else if("1".equals(GsonUtil.toBean(message, HashMap.class).get("result_code"))){
			throw new Exception();
		} else if("0".equals(GsonUtil.toBean(message, HashMap.class).get("result_code"))){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<TimesGroup> getUserGroup(String device_num, String device_mac, String group_num)
			throws ConnectionException, Exception {
		
		//读取下发时段详情
		List<DeviceTimes> timeList = devicetimesService.getDeviceMacByNums(device_num);
		
		// 读取下发时段组中时段编号
		/*Map<String, String> urlMap = new HashMap<String, String>();
		if (urlMap.isEmpty()) {
			urlMap = PropertiesToMap.propertyToMap("comm_interface.properties"); 
		}*/
		String timeUrl = mjCommService.getMjCommUrl(device_num);
		String block = (Integer.parseInt(group_num)<8?"0":"1");
		String deviceType=deviceDao.getMacByDeviceNum(device_num).get("deviceType").toString();
		String message = HttpRequest.sendPost(timeUrl+ "usergroup/getUserGroup.do?", 
				"mac="+device_mac+"&block="+block+"&deviceType="+deviceType).replace("\\", "");
		
		if(message==null || message.equals("null")){
			throw new ConnectionException();
		}
	
		Map<String,Object> map = GsonUtil.toBean(message, HashMap.class);
		ArrayList times = (ArrayList) map.get("times");
		
		
		Integer groupNum = Integer.parseInt(group_num);
		int groupData = (groupNum <8 ? groupNum : groupNum-8) *64;  //该组数据开始下标
		
		List<TimesGroup> timesGroups = new ArrayList<TimesGroup>();
		for(int i=0; i<8; i++){
			TimesGroup timesGroup = new TimesGroup();
			int index = groupData+i*8; 
			
			String times_one_num = ((Double)times.get(index)==0)?null:Integer.valueOf((((Double)times.get(index)).intValue())).toString();
			timesGroup.setTimes_one_num(getTimesOnDevice(times_one_num,timeList));
			
			String times_two_num = ((Double)times.get(index+1)==0)?null:Integer.valueOf((((Double)times.get(index+1)).intValue())).toString();
			timesGroup.setTimes_two_num(getTimesOnDevice(times_two_num,timeList));
			
			String times_three_num = ((Double)times.get(index+2)==0)?null:Integer.valueOf((((Double)times.get(index+2)).intValue())).toString();
			timesGroup.setTimes_three_num(getTimesOnDevice(times_three_num,timeList));
			
			String times_four_num = ((Double)times.get(index+3)==0)?null:Integer.valueOf((((Double)times.get(index+3)).intValue())).toString();
			timesGroup.setTimes_four_num(getTimesOnDevice(times_four_num,timeList));
			
			String times_five_num = ((Double)times.get(index+4)==0)?null:Integer.valueOf((((Double)times.get(index+4)).intValue())).toString();
			timesGroup.setTimes_five_num(getTimesOnDevice(times_five_num,timeList));
			
			String times_six_num = ((Double)times.get(index+5)==0)?null:Integer.valueOf((((Double)times.get(index+5)).intValue())).toString();
			timesGroup.setTimes_six_num(getTimesOnDevice(times_six_num,timeList));
			
			String times_seven_num = ((Double)times.get(index+6)==0)?null:Integer.valueOf((((Double)times.get(index+6)).intValue())).toString();
			timesGroup.setTimes_seven_num(getTimesOnDevice(times_seven_num,timeList));
			
			String times_eight_num = ((Double)times.get(index+7)==0)?null:Integer.valueOf((((Double)times.get(index+7)).intValue())).toString();
			timesGroup.setTimes_eight_num(getTimesOnDevice(times_eight_num,timeList));
			
			timesGroups.add(timesGroup);
		}
		
		return timesGroups;
	}
	
	@Override
	public boolean isConnected(String device_mac) throws ConnectionException{
		/*Map<String, String> urlMap = new HashMap<String, String>();
		if (urlMap.isEmpty()) {
			urlMap = PropertiesToMap.propertyToMap("comm_interface.properties"); 
		}*/
		String deviceNum=mjCommService.getTkDevNumByMac(device_mac);
		String timeUrl = mjCommService.getMjCommUrl(deviceNum);
		String deviceType=deviceDao.getMacByDeviceNum(deviceNum).get("deviceType").toString();
		String message = HttpRequest.sendPost(timeUrl+ "usergroup/getUserGroup.do?", 
				"mac="+device_mac+"&block="+0+"&deviceType="+deviceType).replace("\\", "");
		
		if(message==null || message.equals("null")){
			throw new ConnectionException();
		}
		return true;
	}
	
	/**
	 * 根据时段编号查询时段
	 * @author yuman.gao
	 */
	public String getTimes(String times_num,String device_num){
		String timesStr = "";
		if(times_num != null && !"".equals(times_num)){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("device_num", device_num);
			map.put("times_num", times_num);
			List<DeviceTimes> timesList = timesGroupDao.getAllTimesSortByBeginTime(map);
			if(timesList != null && timesList.size() >0){
				DeviceTimes times = timesList.get(0);
				String beginTime = times.getBegin_time();
				String endTime = times.getEnd_time();
				timesStr = beginTime.substring(0, 2)+":"+beginTime.substring(2, 4)+" - "+endTime.substring(0, 2)+":"+endTime.substring(2, 4);
			}
		} 
		return timesStr;
	}

	/**
	 * 查询已下发时段信息（读取已下发时段组时使用）
	 * @author yuman.gao
	 */
	public String getTimesOnDevice(String times_num, List<DeviceTimes> timesList){
		String timesStr = "";
		
		for (DeviceTimes deviceTimes : timesList) {
			if(deviceTimes.getTimes_num().equals(times_num)){
				timesStr = deviceTimes.getBegin_time() + " - " + deviceTimes.getEnd_time();
				break;
			}
		}
		return timesStr;
	}
	
	@Override
	public Integer getTimesGroupByName(Map<String, Object> map) {
		return timesGroupDao.getTimesGroupByName(map);
	}

	@Override
	public Map<String, Object> getDeviceInfoByNum(String device_num){
		return timesGroupDao.getDeviceInfoByNum(device_num);
	}
	
	
	@Override
	public boolean copyTimesGroup(String source_num, String target_num, String loginUser) {
		Map<String, Object> sourceDevice = new HashMap<String, Object>();
		sourceDevice.put("device_num", source_num);
		List<TimesGroup> sourceTimesGroupList = timesGroupDao.getDeviceTimesGroupByNum(sourceDevice);
		
		Map<String, Object> targetDevice = new HashMap<String, Object>();
		targetDevice.put("device_num", target_num);
		List<TimesGroup> targetTimesGroupList = timesGroupDao.getDeviceTimesGroupByNum(targetDevice);
		
		boolean result = false;
		for(int i=0; i<sourceTimesGroupList.size(); i++){
			TimesGroup sourceGroup = sourceTimesGroupList.get(i);
			TimesGroup targetGroup = targetTimesGroupList.get(i);
			targetGroup.setTimes_one_num(sourceGroup.getTimes_one_num());
			targetGroup.setTimes_two_num(sourceGroup.getTimes_two_num());
			targetGroup.setTimes_three_num(sourceGroup.getTimes_three_num());
			targetGroup.setTimes_four_num(sourceGroup.getTimes_four_num());
			targetGroup.setTimes_five_num(sourceGroup.getTimes_five_num());
			targetGroup.setTimes_six_num(sourceGroup.getTimes_six_num());
			targetGroup.setTimes_seven_num(sourceGroup.getTimes_seven_num());
			targetGroup.setTimes_eight_num(sourceGroup.getTimes_eight_num());
			
			result = timesGroupDao.modifyTimesGroup(targetGroup);
		}
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "时段组管理");
		log.put("V_OP_FUNCTION", "复制");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "复制时段组信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public boolean copyIssuedTimesGroup(String source_num, String target_num,
			String target_mac, String target_type, String loginUser) throws ConnectionException, Exception {
		//复制下发时段前必须先复制下发时段
		copyTimes(source_num,target_num,loginUser);
		devicetimesService.issuedTime(target_num);
		
		copyTimesGroup(source_num, target_num,loginUser);
		issuedTimesGroup(target_num,target_mac,target_type);
		return true;
	}
	
	@Override
	public boolean copyTimes(String source_num, String target_num,
			String loginUser){
		List<DeviceTimes> timesList = devicetimesService.getDeviceTimesByDeviceNum(source_num);
		devicetimesService.deleteDeviceTimesByDeviceNum(target_num);
		for (DeviceTimes deviceTimes : timesList) {
			deviceTimes.setDevice_num(target_num);
			devicetimesService.copeAddDeviceTimes(deviceTimes, loginUser);
		}
		return false;
	}

	@Override
	public List<TimesGroup> getTimesGroupByDevice1(String device_num) {
		return timesGroupDao.getTimesGroupByDevice1(device_num);
	}

	@Override
	public List<TimesGroup> getTimesGroupsByParam1(String device_num,
			String group_name) {
		Map map = new HashMap();
		map.put("device_num", device_num);
		map.put("group_name", group_name);
		return timesGroupDao.getDeviceTimesGroupByParam1(map);
	}

	@Override
	public List<DeviceTimes> getTimesByParamPageSortByBeginTime1(DeviceTimes times) {
		return timesGroupDao.getTimesByParamPageSortByBeginTime1(times);
	}
	
	
}
