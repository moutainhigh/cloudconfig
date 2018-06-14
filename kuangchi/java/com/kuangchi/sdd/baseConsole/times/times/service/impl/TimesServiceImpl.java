package com.kuangchi.sdd.baseConsole.times.times.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.baseConsole.times.times.dao.TimesDao;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.kuangchi.sdd.baseConsole.times.times.service.TimesService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.sun.mail.iap.ConnectionException;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-4-5 下午5:16:41
 * @功能描述: 时段管理模块-业务实现类
 */
@Service("timesServiceImpl")
public class TimesServiceImpl implements TimesService {

	public static final Logger LOG = Logger.getLogger(TimesServiceImpl.class);

	@Resource(name = "timesDaoImpl")
	private TimesDao timesDaoImpl;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;

	@Override
	public boolean addTimes(Times times, String loginUser){
		
		boolean result = timesDaoImpl.addTimes(times);
				
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

	@Override
	public boolean deleteTimesById(String times_ids, String loginUser) {
		
		boolean result = timesDaoImpl.deleteTimesById(times_ids);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "时间段信息管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "删除时间段信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public boolean modifyTimes(Times times, String loginUser) {
		
		boolean result = timesDaoImpl.modifyTimes(times);
		
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
	public List<Times> getTimesByParamPage(Times times, int page, int size) {
		return timesDaoImpl.getTimesByParamPage(times, page, size);
	}

	@Override
	public int getTimesByParamCount(Times times) {
		return timesDaoImpl.getTimesByParamCount(times);
	}

	@Override
	public List<String> getTimesGroupByTimesNum(String times_num) {
		return timesDaoImpl.getTimesGroupByTimesNum(times_num);
	}
	
	@Override
	public void issuedTime(String deviceNums) throws ConnectionException,Exception {
		//封装数据
		String data = dataBuild();
		List<DeviceInfo> devices = timesDaoImpl.getDeviceByNums(deviceNums);
		//调用接口
		for (DeviceInfo device : devices) {
			String url =mjCommService.getMjCommUrl(device.getDevice_num())+ "group/setGroup.do?"; 
			interConnection(url,data,device.getDevice_type(),device.getDevice_mac(),device.getLocal_ip_address());
		}
	}
	
	/**
	 * 封装调用接口所需数据
	 * @author yuman.gao
	 */
	public String dataBuild(){
		
		//分别查询块号为0 和 1 的timeList，并存入allTimeList
		Times time = new Times();
		time.setEare_num(0);
		List<Times> allTimes1 = timesDaoImpl.getTimesByParamInterface(time);
		time.setEare_num(1);
		List<Times> allTimes2 = timesDaoImpl.getTimesByParamInterface(time);
		
		List<List> allTime = new ArrayList<List>();
		allTime.add(allTimes1);
		allTime.add(allTimes2);
	
		//遍历allTimeList，将查询结果存入最终List
		List<Object> list = new ArrayList<Object>();
		for(List<Times> tempAllTime : allTime){
			if(tempAllTime != null && tempAllTime.size()!=0){
				List<Object> childList = new ArrayList<Object>();
				for(Times tempTime : tempAllTime){
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
		LOG.info(str);
		Times t = GsonUtil.toBean(str, Times.class);
		//当连接失败或执行失败时手动回滚
		if(str==null || "".equals(str)){
			throw new ConnectionException();
		} else if(t.getResult_code().equals("1")){
			throw new Exception();
		}
	}
	
}
