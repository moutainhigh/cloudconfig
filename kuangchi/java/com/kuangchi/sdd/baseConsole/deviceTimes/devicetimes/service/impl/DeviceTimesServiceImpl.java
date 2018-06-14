package com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.dao.DeviceTimesDao;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.TimeBlock;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.TimeGroupData;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.service.DeviceTimesService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.sun.mail.iap.ConnectionException;


/**
 * @创建人　: 陈桂波
 * @创建时间: 2016-10-10 下午5:16:41
 * @功能描述: 时段管理模块-业务实现类
 */
@Transactional
@Service("DevicetimesServiceImpl")
public class DeviceTimesServiceImpl implements DeviceTimesService {


	@Resource(name = "DevicetimesDaoImpl")
	private DeviceTimesDao devicetimesDaoImpl;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	
	//添加时段
	@Override
	public boolean addDeviceTimes(DeviceTimes times, String loginUser){
		
		boolean result = devicetimesDaoImpl.addDeviceTimes(times);
		Map<String, String> log = new HashMap<String, String>();	
		log.put("V_OP_NAME", "时间段信息管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "新增时间段信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	//添加时段
		@Override
		public boolean copeAddDeviceTimes(DeviceTimes times, String loginUser){
			
			boolean result = devicetimesDaoImpl.copeAddDeviceTimes(times);
			Map<String, String> log = new HashMap<String, String>();	
			log.put("V_OP_NAME", "复制时段");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", loginUser);
			log.put("V_OP_MSG", "新增时间段信息");
			if(result){
				log.put("V_OP_TYPE", "业务");
			}else{
				log.put("V_OP_TYPE", "异常");
			}
			logDao.addLog(log);
			return result;
		}
	
	@Override
	public boolean modifyTimes(DeviceTimes times, String loginUser) {
		
		boolean result = devicetimesDaoImpl.modifyTimes(times);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "时间段信息管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "修改时间段信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public List<DeviceTimes> getTimesByParamPage(DeviceTimes times, int page, int size) {
		return devicetimesDaoImpl.getTimesByParamPage(times, page, size);
	}

	@Override
	public int getTimesByParamCount(DeviceTimes times) {
		return devicetimesDaoImpl.getTimesByParamCount(times);
	}
	//查询设备信息
	public List<DeviceInfo> getDeviceByNums(String device_num){
		return devicetimesDaoImpl.getDeviceByNums(device_num);
	}
	
	@Override
	public void issuedTime(String device_num) throws ConnectionException,Exception {
		try{
			//封装数据
			String url = mjCommService.getMjCommUrl(device_num)+ "group/setGroup.do?"; 
			String data = dataBuild(device_num);
			List<DeviceInfo> devices = devicetimesDaoImpl.getDeviceByNums(device_num);
			//调用接口
			if(devices!=null){
				for (DeviceInfo device : devices) {
					interConnection(url,data,device.getDevice_type(),device.getDevice_mac(),device.getLocal_ip_address());
				}
			}
			List<Map<String, Object>> list = devicetimesDaoImpl.getTimesByDevice(device_num);
			for(int i=0; i<list.size(); i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", list.get(i).get("Id"));
				map.put("times_num", i+1);
				devicetimesDaoImpl.updateTimeNum(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 封装调用接口所需数据
	 * @author yuman.gao
	 */
	public String dataBuild(String deviceNum){
		
		//分别查询块号为0 和 1 的timeList，并存入allTimeList
		DeviceTimes time = new DeviceTimes();
		time.setEare_num(0);
		time.setDevice_num(deviceNum);
		List<DeviceTimes> allTimes1 = devicetimesDaoImpl.getTimesByParamInterface(time);
		time.setEare_num(1);
		List<DeviceTimes> allTimes2 = devicetimesDaoImpl.getTimesByParamInterface(time);
		
		List<List> allTime = new ArrayList<List>();
		allTime.add(allTimes1);
		allTime.add(allTimes2);
	
		//遍历allTimeList，将查询结果存入最终List
		List<Object> list = new ArrayList<Object>();
		for(List<DeviceTimes> tempAllTime : allTime){
			if(tempAllTime != null && tempAllTime.size()!=0){
				List<Object> childList = new ArrayList<Object>();
				for(DeviceTimes tempTime : tempAllTime){
					Map<String, Object> map = new HashMap<String, Object>();
					Map<String, Object> start = new HashMap<String, Object>();
					Map<String, Object> end = new HashMap<String, Object>();
					start.put("hour", tempTime.getBegin_time().substring(0, 2));
					start.put("minute", tempTime.getBegin_time().substring(2, 4));
					end.put("hour", tempTime.getEnd_time().substring(0, 2));
					end.put("minute", tempTime.getEnd_time().substring(2, 4));
					map.put("start", start);
					map.put("end", end);
					
					childList.add(map);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("block", tempAllTime.get(0).getEare_num());
				map.put("groups", childList);
				list.add(map);
			}
		}
		
		//若list长度不足2,则自动补充
		for(int i=list.size(); i<2; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			List<Object> groupList = new ArrayList<Object>();
			map.put("block", i);
			map.put("groups", groupList);
			list.add(map);
		}
		
		return GsonUtil.toJson(list);
	}

	/**
	 * 进行接口连接
	 * @author yuman.gao
	 */
	public void interConnection(String url, String data, String device_type, String device_mac, String local_ip_address) throws ConnectionException,Exception{
		String str = HttpRequest.sendPost(url, "data="+ data +"&mac="+ device_mac +"&device_type="+ device_type+"&local_ip_address="+ local_ip_address);
		DeviceTimes t = GsonUtil.toBean(str, DeviceTimes.class);
		//当连接失败或执行失败时手动回滚
		if("".equals(str)){
			throw new ConnectionException();
		} else if(t.getResult_code().equals("1")){
			throw new Exception();
		}
	}
	
	
	
	//查询mac地址和块号并获取时段
	@Override
	public List<DeviceTimes> getDeviceMacByNums(String deviceNums) {
		String result1 =null;
		String result2 =null;
		String url =mjCommService.getMjCommUrl(deviceNums)+ 
				"group/getGroup.do?"; 
		
		List<DeviceTimes> list=devicetimesDaoImpl.getDeviceMacByNums(deviceNums);
		if(list.size()!=0){
			List<DeviceTimes> devicetime=new ArrayList<DeviceTimes>();
			for (DeviceTimes deviceTime : list) {
				if(deviceTime.getEare_num()==0){
					result1 = HttpRequest.sendPost(url,"mac="+ deviceTime.getDevice_mac()+"&device_type="+ deviceTime.getDevice_type()
							+"&block="+"00");
					TimeBlock times=GsonUtil.toBean(result1, TimeBlock.class);
					if(times==null || times.getResult_value()==null){
						return null;
					}
					TimeBlock timesList=GsonUtil.toBean(times.getResult_value(), TimeBlock.class);
					List<TimeGroupData> lists=timesList.getGroups();
					if(lists.size()!=0){
						for (int i = 0; i < lists.size(); i++) {
							int startHour=lists.get(i).getStart().getHour();
							int startMinute=lists.get(i).getStart().getMinute();
							int endHour=lists.get(i).getEnd().getHour();
							int endMinute=lists.get(i).getEnd().getMinute();
							if(i==0 && lists.get(i).getStart().getHour()==0 && lists.get(i).getStart().getMinute()==0 &&
								lists.get(i).getEnd().getHour()==0 && lists.get(i).getEnd().getMinute()==0){
								continue;
							}
							if(startHour==255&&startMinute==255&&endHour==255&&endMinute==255){
								break;
							}
							DeviceTimes td=new DeviceTimes();
							if(startHour!=255 ||startMinute!=255){
								String startHourNew= Integer.toHexString(startHour);
								String startMinuteNew=Integer.toHexString(startMinute);
								if(Integer.valueOf(startHourNew)<10){
									startHourNew="0"+startHourNew;
								}
								if(Integer.valueOf(startMinuteNew)<10){
									startMinuteNew="0"+startMinuteNew;
								}
								td.setBegin_time(startHourNew+
										":"+startMinuteNew);
							}
							if(endHour!=255 ||endMinute!=255){
								String endHourNew= Integer.toHexString(endHour);
								String endMinuteNew=Integer.toHexString(endMinute);
								if(Integer.valueOf(endHourNew)<10){
									endHourNew="0"+endHourNew;
								}
								if(Integer.valueOf(endMinuteNew)<10){
									endMinuteNew="0"+endMinuteNew;
								}
								td.setEnd_time(endHourNew+
										":"+endMinuteNew);
							}
							td.setTimes_num(String.valueOf(i));
							devicetime.add(td);
						}
					}
							
				}else if(deviceTime.getEare_num()==1){
					result2 = HttpRequest.sendPost(url,"&mac="+ deviceTime.getDevice_mac()
							+"&block="+"01");
					TimeBlock times2=GsonUtil.toBean(result2, TimeBlock.class);
					TimeBlock timesList=GsonUtil.toBean(times2.getResult_value(), TimeBlock.class);
					
					List<TimeGroupData> lists=timesList.getGroups();
					if(lists.size()!=0){
						for (int i = 0; i < lists.size(); i++) {
							int startHour=lists.get(i).getStart().getHour();
							int startMinute=lists.get(i).getStart().getMinute();
							int endHour=lists.get(i).getEnd().getHour();
							int endMinute=lists.get(i).getEnd().getMinute();
							if(startHour==255&&startMinute==255&&endHour==255&&endMinute==255){
								break;
							}
							DeviceTimes td=new DeviceTimes();
							if(startHour!=255 ||startMinute!=255){
								String startHourNew= Integer.toHexString(startHour);
								String startMinuteNew=Integer.toHexString(startMinute);
								if(Integer.valueOf(startHourNew)<10){
									startHourNew="0"+startHourNew;
								}
								if(Integer.valueOf(startMinuteNew)<10){
									startMinuteNew="0"+startMinuteNew;
								}
								td.setBegin_time(startHourNew+
										":"+startMinuteNew);
							}
							if(endHour!=255 ||endMinute!=255){
								String endHourNew= Integer.toHexString(endHour);
								String endMinuteNew=Integer.toHexString(endMinute);
								if(Integer.valueOf(endHourNew)<10){
									endHourNew="0"+endHourNew;
								}
								if(Integer.valueOf(endMinuteNew)<10){
									endMinuteNew="0"+endMinuteNew;
								}
								td.setEnd_time(endHourNew+
										":"+endMinuteNew);
							}
							td.setTimes_num(String.valueOf(128+i));
							devicetime.add(td);
						}
					}
						
				}
			}
			//将读取到的设备时段存进数据库 by gengji.yang
			updateDeviceTimeRecord(devicetime,deviceNums);
			return beautifyModel(devicetime);
		}
		return null;
	}
	
	/**
	 * 更新数据库中的设备时段记录
	 * by gengji.yang
	 */
	private void updateDeviceTimeRecord(List<DeviceTimes> devicetime,String deviceNums){
		for(DeviceTimes dt:devicetime){
			dt.setDevice_num(deviceNums);
			devicetimesDaoImpl.delRepTime(dt);
			Integer timesNum=Integer.parseInt(dt.getTimes_num());
			String beginTime=dt.getBegin_time().replace(":","");
			String endTime=dt.getEnd_time().replace(":","");
			dt.setBegin_time(beginTime);
			dt.setEnd_time(endTime);
			if(timesNum >= 0 && timesNum <= 127){
				dt.setEare_num(0);
			}else{
				dt.setEare_num(1);
			}
			devicetimesDaoImpl.addTimeAfterDel(dt);
		}
	}
	
	/**
	 * 修改时间的显示格式
	 * 由“0001”到"00:01"
	 * by gengji.yang
	 */
	public List<DeviceTimes> beautifyModel(List<DeviceTimes> timesList){
		for(int i=0;i<timesList.size();i++){
			String begin_time_str = timesList.get(i).getBegin_time();
			String end_time_str = timesList.get(i).getEnd_time();
			timesList.get(i).setBegin_time(begin_time_str.substring(0,2)+":"+begin_time_str.substring(2, 4));
			timesList.get(i).setEnd_time(end_time_str.substring(0,2)+":"+end_time_str.substring(2, 4));
		}
		return timesList;
	}

	//保存时段
	@Override
	public List<DeviceTimes> getDeviceTimesByDeviceNum(String deviceNums) {
		List<DeviceTimes> list=devicetimesDaoImpl.getDeviceTimesByDeviceNum(deviceNums);
		return list;
	}
	//保存并下发时段
		@Override
		public boolean getDeviceTimesByDeviceNums(String device_num,String device)throws ConnectionException,Exception {
			//查出原有设备的时段
			List<DeviceTimes> list=devicetimesDaoImpl.getDeviceTimesByDeviceNum(device_num);
				if(list.size()!=0){
						devicetimesDaoImpl.deleteDeviceTimesByDeviceNum(device);
						for (int j = 0; j < list.size(); j++) {
							DeviceTimes times=new DeviceTimes();
							times.setTimes_num(list.get(j).getTimes_num());
							times.setBegin_time(list.get(j).getBegin_time());
							times.setEnd_time(list.get(j).getEnd_time());
							times.setDevice_num(device);
							times.setEare_num(list.get(j).getEare_num());
						 devicetimesDaoImpl.copeAddDeviceTimes(times);
						 issuedTime(device);
						}
						 return true;
				}else{
						devicetimesDaoImpl.deleteDeviceTimesByDeviceNum(device);
						issuedTime(device);
						return true;
				}
				
		}
	
	
	
	@Override
	public boolean deleteDeviceTimesByDeviceNum(String deviceNums) {
		boolean result = devicetimesDaoImpl.deleteDeviceTimesByDeviceNum(deviceNums);
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "复制时段");
		log.put("V_OP_FUNCTION", "删除");
		//log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "复制时段之前删除");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}
	
	//判断服务器是否连接或设备是否在线
	@Override
	public String isNormalState(String deviceNums) {
		String result="normal";
		String result1 ="";
		String result2 ="";
		String url = mjCommService.getMjCommUrl(deviceNums)+"group/getGroup.do?"; 
		List<DeviceTimes> list=devicetimesDaoImpl.getDeviceMacByNums(deviceNums);
		if(list.size()!=0){
			for (DeviceTimes deviceTime : list) {
				if(deviceTime.getEare_num()==0){
					result1 = HttpRequest.sendPost(url,"mac="+ deviceTime.getDevice_mac()+"&block="+"00"+"&device_type="+ deviceTime.getDevice_type());
					if(result1==null){
						return null;
					}else if("".equals(result1)){
						return result1;
					}
					TimeBlock times=GsonUtil.toBean(result1, TimeBlock.class);
					if(times==null || times.getResult_value()==null|| "null".equals(times.getResult_value())){
						return null;
					}	
				}else if(deviceTime.getEare_num()==1){
					result2 = HttpRequest.sendPost(url,"&mac="+ deviceTime.getDevice_mac()+"&block="+"01"+"&device_type="+ deviceTime.getDevice_type());
					if(result2==null){
						return null;
					}else if("".equals(result2)){
						return result2;
					}
					TimeBlock times2=GsonUtil.toBean(result2, TimeBlock.class);
					if(times2==null || times2.getResult_value()==null|| "null".equals(times2.getResult_value())){
						return null;
					}	
				}
			}
		}
		return result;
	}
	
	@Override
	public boolean delTimesById(String ids, String device_num, String login_user){
		Map<String, String> log = new HashMap<String, String>();
		try{
			boolean result = devicetimesDaoImpl.delTimesById(ids);
			if(result){
				/*List<Map<String, Object>> list = devicetimesDaoImpl.getTimesByDevice(device_num);
				for(int i=0; i<list.size(); i++){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", list.get(i).get("Id"));
					map.put("times_num", i+1);
					devicetimesDaoImpl.updateTimeNum(map);
				}*/
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
			log.put("V_OP_NAME", "设备时段管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除设备时段");
			logDao.addLog(log);
		}
	}


	@Override
	public List<DeviceTimes> getDeviceMacByNums2(String deviceNums){
		String result1 =null;
		String result2 =null;
		String url =mjCommService.getMjCommUrl(deviceNums)+ 
				"group/getGroup.do?"; 
		
		List<DeviceTimes> list=devicetimesDaoImpl.getDeviceMacByNums(deviceNums);
		if(list.size()!=0){
			List<DeviceTimes> devicetime=new ArrayList<DeviceTimes>();
			for (DeviceTimes deviceTime : list) {
				if(deviceTime.getEare_num()==0){
					result1 = HttpRequest.sendPost(url,"mac="+ deviceTime.getDevice_mac()
							+"&block="+"00"+"&device_type="+ deviceTime.getDevice_type());
					TimeBlock times=GsonUtil.toBean(result1, TimeBlock.class);
					if(times==null || times.getResult_value()==null){
						return null;
					}
					TimeBlock timesList=GsonUtil.toBean(times.getResult_value(), TimeBlock.class);
					List<TimeGroupData> lists=timesList.getGroups();
					if(lists.size()!=0){
						for (int i = 0; i < lists.size(); i++) {
							int startHour=lists.get(i).getStart().getHour();
							int startMinute=lists.get(i).getStart().getMinute();
							int endHour=lists.get(i).getEnd().getHour();
							int endMinute=lists.get(i).getEnd().getMinute();
							if(i==0 && lists.get(i).getStart().getHour()==0 && lists.get(i).getStart().getMinute()==0 &&
								lists.get(i).getEnd().getHour()==0 && lists.get(i).getEnd().getMinute()==0){
								continue;
							}
							if(startHour==255&&startMinute==255&&endHour==255&&endMinute==255){
								break;
							}
							DeviceTimes td=new DeviceTimes();
							if(startHour!=255 ||startMinute!=255){
								String startHourNew= Integer.toHexString(startHour);
								String startMinuteNew=Integer.toHexString(startMinute);
								if(Integer.valueOf(startHourNew)<10){
									startHourNew="0"+startHourNew;
								}
								if(Integer.valueOf(startMinuteNew)<10){
									startMinuteNew="0"+startMinuteNew;
								}
								td.setBegin_time(startHourNew+
										":"+startMinuteNew);
							}
							if(endHour!=255 ||endMinute!=255){
								String endHourNew= Integer.toHexString(endHour);
								String endMinuteNew=Integer.toHexString(endMinute);
								if(Integer.valueOf(endHourNew)<10){
									endHourNew="0"+endHourNew;
								}
								if(Integer.valueOf(endMinuteNew)<10){
									endMinuteNew="0"+endMinuteNew;
								}
								td.setEnd_time(endHourNew+
										":"+endMinuteNew);
							}
							td.setTimes_num(String.valueOf(i));
							devicetime.add(td);
						}
					}
							
				}else if(deviceTime.getEare_num()==1){
					result2 = HttpRequest.sendPost(url,"&mac="+ deviceTime.getDevice_mac()
							+"&block="+"01"+"&device_type="+ deviceTime.getDevice_type());
					TimeBlock times2=GsonUtil.toBean(result2, TimeBlock.class);
					TimeBlock timesList=GsonUtil.toBean(times2.getResult_value(), TimeBlock.class);
					
					List<TimeGroupData> lists=timesList.getGroups();
					if(lists.size()!=0){
						for (int i = 0; i < lists.size(); i++) {
							int startHour=lists.get(i).getStart().getHour();
							int startMinute=lists.get(i).getStart().getMinute();
							int endHour=lists.get(i).getEnd().getHour();
							int endMinute=lists.get(i).getEnd().getMinute();
							if(startHour==255&&startMinute==255&&endHour==255&&endMinute==255){
								break;
							}
							DeviceTimes td=new DeviceTimes();
							if(startHour!=255 ||startMinute!=255){
								String startHourNew= Integer.toHexString(startHour);
								String startMinuteNew=Integer.toHexString(startMinute);
								if(Integer.valueOf(startHourNew)<10){
									startHourNew="0"+startHourNew;
								}
								if(Integer.valueOf(startMinuteNew)<10){
									startMinuteNew="0"+startMinuteNew;
								}
								td.setBegin_time(startHourNew+
										":"+startMinuteNew);
							}
							if(endHour!=255 ||endMinute!=255){
								String endHourNew= Integer.toHexString(endHour);
								String endMinuteNew=Integer.toHexString(endMinute);
								if(Integer.valueOf(endHourNew)<10){
									endHourNew="0"+endHourNew;
								}
								if(Integer.valueOf(endMinuteNew)<10){
									endMinuteNew="0"+endMinuteNew;
								}
								td.setEnd_time(endHourNew+
										":"+endMinuteNew);
							}
							td.setTimes_num(String.valueOf(128+i));
							devicetime.add(td);
						}
					}
						
				}
			}
			return devicetime;
		}
		return null;
	}
	
	@Override
	public Integer ifUsedTimes(Map<String, Object> map){
		try{
			return devicetimesDaoImpl.ifUsedTimes(map);
		}catch(Exception e){
			e.printStackTrace();
			return 1;
		}
	}
	
}
