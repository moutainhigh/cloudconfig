package com.kuangchi.sdd.ZigBeeConsole.authorityManager.service.impl;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.ZigBeeConsole.authorityManager.dao.AuthorityManagerDao;
import com.kuangchi.sdd.ZigBeeConsole.authorityManager.model.AuthorityManagerModel;
import com.kuangchi.sdd.ZigBeeConsole.authorityManager.service.AuthorityManagerService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;


/**
 * 光子锁权限管理 - service实现类
 * @author chudan.guo
 */
@Transactional
@Service("authorityManagerServiceImpl")
public class AuthorityManagerServiceImpl implements AuthorityManagerService{
	
	@Resource(name = "authorityManagerDaoImpl")
	private AuthorityManagerDao authorityManagerDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "employeeService")
	EmployeeService employeeService;
	
	
	
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;
	
	@Override
	public List<Map> getAllStaffToZigBee(Map<String, Object> map) {
		return authorityManagerDao.getAllStaffToZigBee(map);
	}

	@Override
	public Integer getAllStaffToZigBeeCount(Map<String, Object> map) {
		return authorityManagerDao.getAllStaffToZigBeeCount(map);
	}

	@Override
	public List<Map> getZigBeeDevice(Map<String, Object> map) {
		List<Map> devList = authorityManagerDao.getZigBeeDevice(map);
		
		// 重新整理数据，若设备不在线，则不显示电池电量、信号强度、时间等设备信息
		for (Map dev : devList) {
			if("1".equals(dev.get("device_state"))){
				dev.remove("time_stamp");
				dev.remove("device_signal");
				dev.remove("electricity");
			}
		}
		
		return devList;
	}

	@Override
	public Integer getZigBeeDeviceCount(Map<String, Object> map) {
		return authorityManagerDao.getZigBeeDeviceCount(map);
	}

	
	
	
	@Override
	public boolean addPeopleAuthority(List<AuthorityManagerModel> authList){
		// 批量增加待下载权限
		authorityManagerDao.addPeopleAuthority(authList);
	
		// 封装权限任务
		List<AuthorityManagerModel> taskList = new ArrayList<AuthorityManagerModel>();
		for (AuthorityManagerModel auth : authList) {
			// 根据光ID和设备判断是否存在旧有效权限 
			Map<String, Object> authParam = new HashMap<String, Object>();
			authParam.put("device_id", auth.getDevice_id());
			authParam.put("card_nums", auth.getLight_id());
			List<AuthorityManagerModel> authInfos = authorityManagerDao.getAuthorityByCards(authParam);
			if(authInfos != null && authInfos.size() != 0){
				AuthorityManagerModel oldAuth =  authInfos.get(0);
				String oldBeginDate = oldAuth.getBegin_valid_date();
				String newBeginDate = auth.getBegin_valid_date();
				// 如果旧权限开始时间靠后，则新权限插入任务表
				if(oldBeginDate.compareTo(newBeginDate) > 0){
					taskList.add(auth);
				}
			} else {
				taskList.add(auth);
			}
		}
		if(taskList != null && taskList.size() != 0){
			authorityManagerDao.insertZigbeeTask(taskList);
		}
		
		return true;
	}
	
	
	@Override
	public boolean delePeopleAuthority(List<AuthorityManagerModel> auths){
		Map<String, Object> stateParam = new HashMap<String, Object>();
		String authority_nums = "";
		for (AuthorityManagerModel auth : auths) {
			authority_nums += "'"+auth.getAuthority_num()+"'"+",";
		}
		stateParam.put("authority_num", authority_nums.substring(0, authority_nums.length()-1));
		stateParam.put("authority_flag", 3);  // 更新权限状态为待删除
		authorityManagerDao.updateSomeAuthorityState(stateParam);
		authorityManagerDao.insertDeleteTask(auths);
		
		return true;
	} 
		
	
	
	@Override
	public boolean issuedAddTask(AuthorityManagerModel model){
		boolean result = false;
		
		Map<String, Object> taskParam = new HashMap<String, Object>();
		taskParam.put("device_id",  model.getDevice_id());
		taskParam.put("light_id", model.getLight_id());
		taskParam.put("begin_valid_date", model.getBegin_valid_date());
		taskParam.put("end_valid_date", model.getEnd_valid_date());
		taskParam.put("authority_num", model.getAuthority_num());
		taskParam.put("task_type", 0);
		taskParam.put("create_user", model.getCreate_user());
		
		/*Cache cache = Ehcache.getCache("GWInfoCache");
		if(cache == null || cache.get(model.getDevice_id()) == null){
			Map map = authorityManagerDao.getZigbeeIpMap(model.getDevice_id());
			cache.put(new Element(model.getDevice_id(),map));
		} 
		Map map = (Map)cache.get(model.getDevice_id()).getObjectValue();*/
		
		Map map = authorityManagerDao.getZigbeeIpMap(model.getDevice_id());
		
		if(map != null) {
			String serverUrl = map.get("remote_ip").toString(); //通讯服务器IP
			String serverPort = map.get("remote_port").toString();//通讯服务器端口
			String gateway = map.get("gateway").toString(); //网关
			String gateway_port= map.get("gateway_port").toString(); //网关端口
			
				
			String data = dataBuildToAdd(model);
			String url ="http://"+serverUrl+":"+serverPort+"/comm/zigBee/setAuthority.do?";
			String param = "data="+data+"&remoteIp="+gateway+"&port="+gateway_port;
			String message = HttpRequest.sendPost(url,param); 
			
			if(!(null==message||"null".equals(message)||"".equals(message))){
				String status=GsonUtil.toBean(message, HashMap.class).get("status").toString().split("\\.")[0];
				
				/* 下载成功后，查询是否有原有效权限，若有，更改原权限状态为 "回收待下载"；
				       将权限更改为有效，删除旧权限；删除任务表，新增历史表数据 */
				if("0".equals(status)){ 
					
					Map<String, Object> authParam = new HashMap<String, Object>();
					authParam.put("device_id", model.getDevice_id());
					authParam.put("card_nums", model.getLight_id());
					List<AuthorityManagerModel> authInfos = authorityManagerDao.getAuthorityByCards(authParam);
					if(authInfos != null && authInfos.size() != 0){
						AuthorityManagerModel oldAuth =  authInfos.get(0);
						Map<String, Object> stateParam = new HashMap<String, Object>();
						stateParam.put("authority_num", oldAuth.getAuthority_num());
						stateParam.put("authority_flag", 1);
						authorityManagerDao.updateAuthorityState(stateParam);
					}
					
					Integer time = authorityManagerDao.getTaskTryTimes(taskParam);
					taskParam.put("try_times", time+1);
					taskParam.put("task_state", 0);
					taskParam.put("authority_flag", 0);// 状态更新为有效
					authorityManagerDao.removeOldAuthority(taskParam);
					authorityManagerDao.updateAuthorityState(taskParam);
					authorityManagerDao.removeTask(taskParam);
					authorityManagerDao.insertHisTask(taskParam);
					
					result = true;
					
					
				// 如果失败，则查询尝试次数；如果执行次数已经超过3次，则删除任务表记录，新增任务历史记录； 若不是，则更新执行次数
				} else if("1".equals(status)){  
					taskParam.put("task_state", 1);
					
					Integer time = authorityManagerDao.getTaskTryTimes(taskParam);
					taskParam.put("try_times", time+1);
					if((Integer)taskParam.get("try_times") == 3){
						authorityManagerDao.removeTask(taskParam);
						authorityManagerDao.insertHisTask(taskParam);
						taskParam.put("authority_flag", 7);// 状态更新为下载失败
						authorityManagerDao.updateAuthorityState(taskParam);
					} else {
						authorityManagerDao.updateTask(taskParam);
					}
					
				// 如果已满，不作操作
				} else if("2".equals(status)){
					
				}
				
			} else {
				taskParam.put("task_state", 1);
				
				Integer time = authorityManagerDao.getTaskTryTimes(taskParam);
				taskParam.put("try_times", time+1);
				if((Integer)taskParam.get("try_times") == 3){
					taskParam.put("authority_flag", 7);
					authorityManagerDao.removeTask(taskParam);
					authorityManagerDao.insertHisTask(taskParam);
					authorityManagerDao.updateAuthorityState(taskParam);
				} else {
					authorityManagerDao.updateTask(taskParam);
				}
			}
		}
		
		
		return result;
	}
	
	
	
	
	@Override
	public List<AuthorityManagerModel> getOutTimeAuthority(String device_id){
		Date date=new Date();//创建系统现在时间
	    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String currentTime=format.format(date);
	    
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_id", device_id);
		map.put("currentTime", currentTime);
		List<AuthorityManagerModel> authorityList = authorityManagerDao.getExpireAuthority(map);//查找出过期失效的授权
		
		for(int i=0; i < authorityList.size(); i++){
			if(i<authorityList.size()-1){
				authorityList.get(i).setTransportFlag(0x01);
			}else{
				authorityList.get(i).setTransportFlag(0x00);
			}
		}
		return authorityList;
	}
	
	
	
	@Override
	public boolean issuedDelTask(AuthorityManagerModel model){
		boolean result = false;
		
		Map<String, Object> taskParam = new HashMap<String, Object>();
		taskParam.put("device_id",  model.getDevice_id());
		taskParam.put("light_id", model.getLight_id());
		taskParam.put("begin_valid_date", model.getBegin_valid_date());
		taskParam.put("end_valid_date", model.getEnd_valid_date());
		taskParam.put("authority_num", model.getAuthority_num());
		taskParam.put("task_type", 1);
		taskParam.put("create_user", model.getCreate_user());
		
		/*Cache cache = Ehcache.getCache("GWInfoCache");
		if(cache == null || cache.get(model.getDevice_id()) == null){
			Map map = authorityManagerDao.getZigbeeIpMap(model.getDevice_id());
			cache.put(new Element(model.getDevice_id(),map));
		} 
		Map map = (Map)cache.get(model.getDevice_id()).getObjectValue();*/
		
		Map map = authorityManagerDao.getZigbeeIpMap(model.getDevice_id());
		// 如果该设备查询不到相应网关信息，则无法添加删除任务
		if(map != null){
		
			String serverUrl=map.get("remote_ip").toString(); //通讯服务器IP
			String serverPort=map.get("remote_port").toString();//通讯服务器端口
			String gateway=map.get("gateway").toString(); //网关
			String gateway_port=map.get("gateway_port").toString(); //网关端口
			
			String data = dataBuildToDele(model);
			String url = "http://"+serverUrl+":"+serverPort+"/comm/zigBee/deleteAuthority.do?";
			String param="data="+data+"&remoteIp="+gateway+"&port="+gateway_port;
			String message = HttpRequest.sendPost(url,param);
			
			if(!(null==message||"null".equals(message)||"".equals(message))){
				String status=GsonUtil.toBean(message, HashMap.class).get("status").toString().split("\\.")[0];
				// 如果成功，删除任务表，新增任务历史表，并将权限改为无效
				if("0".equals(status)){   
					Integer time = authorityManagerDao.getTaskTryTimes(taskParam);
					taskParam.put("try_times", time+1);
					taskParam.put("task_state", 0);
					taskParam.put("authority_flag", 6);//更新状态为无效
					
					authorityManagerDao.removeTask(taskParam);
					authorityManagerDao.insertHisTask(taskParam);
					authorityManagerDao.updateAuthorityState(taskParam);
					
					result = true;
					
				// 如果失败，则查询尝试次数；如果执行次数已经超过3次，则删除任务表记录，新增任务历史记录； 若不是，则更新执行次数
				} else if ("1".equals(status)){ 
					taskParam.put("task_state", 1);
					
					Integer time = authorityManagerDao.getTaskTryTimes(taskParam);
					taskParam.put("try_times", time+1);
					if((Integer)taskParam.get("try_times") == 3){
						authorityManagerDao.removeTask(taskParam);
						authorityManagerDao.insertHisTask(taskParam);
						taskParam.put("authority_flag", 4); //更新状态为删除未成功
						authorityManagerDao.updateAuthorityState(taskParam);
					} else {
						authorityManagerDao.updateTask(taskParam);
					}
					
				} else if ("3".equals(status)){ //已空
					Integer time = authorityManagerDao.getTaskTryTimes(taskParam);
					taskParam.put("try_times", time+1);
					taskParam.put("task_state", 0);
					taskParam.put("authority_flag", 6);
					
					authorityManagerDao.removeTask(taskParam);
					authorityManagerDao.insertHisTask(taskParam);
					authorityManagerDao.updateAuthorityState(taskParam);
					
					result = true;
				}
				
			} else {
				taskParam.put("task_state", 1);
				
				Integer time = authorityManagerDao.getTaskTryTimes(taskParam);
				taskParam.put("try_times", time+1);
				if((Integer)taskParam.get("try_times") == 3){
					authorityManagerDao.removeTask(taskParam);
					authorityManagerDao.insertHisTask(taskParam);
					taskParam.put("authority_flag", 4);
				} else {
					authorityManagerDao.updateTask(taskParam);
				}
				authorityManagerDao.updateAuthorityState(taskParam);
			}
		}
		
		return result;
	}
	
	
	
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-19
	 * @功能描述: 封装下发数据接口
	 */
	public String dataBuildToAdd(AuthorityManagerModel model){
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
		try {
			date1 = simpleDateFormat.parse(model.getBegin_valid_date());
			date2 = simpleDateFormat.parse(model.getEnd_valid_date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long startTime=date1.getTime()/1000;
		Long endTime=date2.getTime()/1000;
		map.put("lockId",model.getDevice_id());
		map.put("photonId",model.getLight_id());
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		map.put("transportFlag",model.getTransportFlag());
	    return GsonUtil.toJson(map);
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-19
	 * @功能描述: 封装删除数据接口
	 */
	public String dataBuildToDele(AuthorityManagerModel model){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lockId",model.getDevice_id());
		map.put("photonId",model.getLight_id());
		map.put("transportFlag",model.getTransportFlag());
		return GsonUtil.toJson(map);
	}
	
	
	@Override
	public Integer selectAuthorityCount(String device_id, String light_id) {
		return authorityManagerDao.selectAuthorityCount(device_id, light_id);
	}

	@Override
	public List<Map> getPeopleAuthority(Map<String, Object> map) {
		return authorityManagerDao.getPeopleAuthority(map);
	}

	@Override
	public Integer getPeopleAuthorityCount(Map<String, Object> map) {
		return authorityManagerDao.getPeopleAuthorityCount(map);
	}
	
	@Override
	public List<Map> getZBTasks(int taskCount) {
		return authorityManagerDao.getZBTasks(taskCount);
	}
	
	@Override
	public List<Map> getDelTasks() {
		return authorityManagerDao.getDelTasks();
	}
	
	@Override
	public boolean insertDeleteTask(List<AuthorityManagerModel> auths){
		// 防止插入重复的数据，重新封装删除任务
		List<AuthorityManagerModel> delTask = new ArrayList<AuthorityManagerModel>();
		for (AuthorityManagerModel auth : auths) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("authority_num", auth.getAuthority_num());
			map.put("task_type", 1);
			if(authorityManagerDao.getTaskTryTimes(map) == null){
				delTask.add(auth);
			}
		}
		if(delTask != null && delTask.size() != 0){
			authorityManagerDao.insertDeleteTask(delTask);
		} 
		return true;
	}
	
	@Override
	public boolean insertZigbeeTask(List<AuthorityManagerModel> auths){
		// 防止插入重复的数据，重新封装下发任务
		List<AuthorityManagerModel> addTask = new ArrayList<AuthorityManagerModel>();
		for (AuthorityManagerModel auth : auths) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("authority_num", auth.getAuthority_num());
			map.put("task_type", 0);
			if(authorityManagerDao.getTaskTryTimes(map) == null){
				addTask.add(auth);
			}
		}
		if(addTask != null && addTask.size() != 0){
			authorityManagerDao.insertZigbeeTask(addTask);
		} 
		return true;
		
	}
	

	@Override
	public List<AuthorityManagerModel> getAuthorityByCards(Map<String, Object> map) {
		return authorityManagerDao.getAuthorityByCards(map);
	}

	@Override
	public List<AuthorityManagerModel> getWaitAuthorityByCards(Map<String, Object> map) {
		return authorityManagerDao.getWaitAuthorityByCards(map);
	}

	@Override
	public void updateAuthorityState(Map<String, Object> map) {
		authorityManagerDao.updateAuthorityState(map);
	}
	
	@Override
	public void updateSomeAuthorityState(Map<String, Object> map) {
		authorityManagerDao.updateSomeAuthorityState(map);
	}
	
	@Override
	public boolean autoIssuedAuthority(Map<String, Object> map) {
		boolean result=false;
		/*if("0".equals(map.get("auto_flag").toString())){*/
			authorityManagerDao.deleAutoIssuedAuthority();//真删除已有
			String[] device_id=map.get("device_id").toString().split(",");
			for(int i=0;i<device_id.length;i++){
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("begin_valid_date", map.get("begin_valid_date"));
				map2.put("end_valid_date", map.get("end_valid_date"));
				map2.put("auto_flag", map.get("auto_flag"));
				map2.put("device_id", device_id[i]);
				result = authorityManagerDao.autoIssuedAuthority(map2);
			}
		/*}else if("1".equals(map.get("auto_flag").toString())){
			String[] device_id=map.get("device_id").toString().split(",");
			for(int i=0;i<device_id.length;i++){
				result = authorityManagerDao.updateAutoIssuedAuthority(device_id[i]);
			}
		}*/
		return result;
	} 

	@Override
	public void deleAutoIssuedAuthority() {
		authorityManagerDao.deleAutoIssuedAuthority();
		
	}

	@Override
	public List<Map> getAutoIssuedAuthority() {
		return authorityManagerDao.getAutoIssuedAuthority();
	}
	
	
	
	@Override
	public void autoIssuedAuth(Map<String, Object> map) {
		
		String flag = (String) map.get("flag");
		String light_id = (String) map.get("light_id");
		String create_user = (String) map.get("create_user");
		
		if("add".equals(flag)){
			// 删除旧权限，以防多次同步
			Map<String, Object> authParam = new HashMap<String, Object>();
			authParam.put("light_id", light_id);
			authorityManagerDao.removeTask(authParam);
			authorityManagerDao.removeAllAuthByCard(authParam);
			
			// 下发新权限
			List<AuthorityManagerModel> taskList = new ArrayList<AuthorityManagerModel>();
			List<Map> autoInfos = authorityManagerDao.getAutoTask();
			for (Map autoInfo : autoInfos) {
				AuthorityManagerModel model = new AuthorityManagerModel();
				model.setAuthority_num(UUID.randomUUID().toString());
				model.setLight_id(light_id); 
				model.setDevice_id(autoInfo.get("device_id").toString());
				model.setBegin_valid_date(autoInfo.get("begin_valid_date").toString());
				model.setEnd_valid_date(autoInfo.get("end_valid_date").toString());
				model.setCreate_user(create_user);
				taskList.add(model);
			}
			addPeopleAuthority(taskList);
			
		} else if ("update".equals(flag)){
			// 若卡号改变，则权限重新下发
			String oldCardNum = (String) map.get("old_light_id");
			if(light_id != null && !light_id.equals(oldCardNum)){
				Map<String, Object> authParam = new HashMap<String, Object>();
				authParam.put("card_nums", oldCardNum);
				List<AuthorityManagerModel> authInfos = authorityManagerDao.getAuthorityByCards(authParam);
				
				// 封装新权限
				List<AuthorityManagerModel> taskList = new ArrayList<AuthorityManagerModel>();
				for(AuthorityManagerModel authInfo : authInfos){
					authInfo.setAuthority_num(UUID.randomUUID().toString());
					authInfo.setLight_id(light_id);
					authInfo.setCreate_user(create_user);
					taskList.add(authInfo);
				}
				if(taskList != null && taskList.size() != 0){
					// 删除旧权限
					delePeopleAuthority(authInfos);
					// 增加新权限
					addPeopleAuthority(taskList);
				}
			}
		} 
	}

	
	// 通过编码规则获得工号
	public String getStaffNo(){
		String midStaffNo = peopleAuthorityInfoService.autoProdStaffNo();//自动生成的员工号中间部分
		//Map<String,String> map=new HashMap<String,String>();
		if(midStaffNo==null||"".equals(midStaffNo)){
			return "";
		}
		String staffRule = employeeService.selectStaffNoRule();//查询当前员工工号编码规则
		String[] staffRules=staffRule.split("\\|",-1);
		staffRules[1]=midStaffNo;//用生成的员工号中间部分替换规则的中间部分
		String finalStaffNo = staffRules[0]+"|"+staffRules[1]+"|"+staffRules[2];//重新拼接成最后显示到页面的员工号
		String finalStaffNo1=staffRules[0]+staffRules[1]+staffRules[2];//重新拼接成最后显示到页面的员工号
		boolean flag=employeeService.selectEmpByNum(finalStaffNo1);
		if(flag){
			employeeService.updateEmpNoTemp(finalStaffNo,finalStaffNo1);
			midStaffNo = peopleAuthorityInfoService.autoProdStaffNo();
			if(midStaffNo==null||"".equals(midStaffNo)){
				return "";
			}
			staffRules[1] = midStaffNo;
			finalStaffNo1=staffRules[0]+staffRules[1]+staffRules[2];
		}
		return finalStaffNo1;
	}

	@Override
	public Map getZigbeeIpMap(String device_id) {
		return authorityManagerDao.getZigbeeIpMap(device_id);
	}

	@Override
	public List<Map> getCompany() {
		return authorityManagerDao.getCompany();
	}

	@Override
	public boolean setTimerObject(String company_num) {
		authorityManagerDao.setTimerObject(company_num);
		authorityManagerDao.setNotTimerObject(company_num);
		return true;
	}

	@Override
	public List<Map> getTimerCompany() {
		return authorityManagerDao.getTimerCompany();
	}

}
