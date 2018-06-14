package com.kuangchi.sdd.ZigBeeConsole.authorityManager.action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.ZigBeeConsole.authorityManager.model.AuthorityManagerModel;
import com.kuangchi.sdd.ZigBeeConsole.authorityManager.service.AuthorityManagerService;
import com.kuangchi.sdd.ZigBeeConsole.inter.service.IInterService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.PropertyUtils;

/**
 * 光子锁权限管理 - action
 * @author chudan.guo
 */
@Controller("authorityManagerAction")
public class AuthorityManagerAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "authorityManagerServiceImpl")
	private AuthorityManagerService authorityManagerService;
	
	@Resource(name = "ZigBeeInterServiceImpl")
	private IInterService interService;
	
	@Resource(name="startQuertz")
    Scheduler scheduler;
	
	@Override
	public Object getModel() {
		return null;
	}

	
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-18 
	 * @功能描述: 根据参数查询所有员工[分页]
	 */
	public void getAllStaffToZigBee(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		List<Map> staffList =  authorityManagerService.getAllStaffToZigBee(map);
		Integer staffCount = authorityManagerService.getAllStaffToZigBeeCount(map);
		Grid<Map> grid = new Grid<Map>();
		grid.setRows(staffList);
		grid.setTotal(staffCount);
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-18 
	 * @功能描述: 根据参数查询所有光子锁设备[分页]
	 */
	public void getZigBeeDevice(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		List<Map> deviceList =  authorityManagerService.getZigBeeDevice(map);
		for(int i=0;i<deviceList.size();i++){
			String deviceId=deviceList.get(i).get("device_id").toString();
			int count=authorityManagerService.selectAuthorityCount(deviceId,null);
			deviceList.get(i).put("spareCount", 312-count);  //设备剩余可插入条数
		}
		Integer deviceCount = authorityManagerService.getZigBeeDeviceCount(map);
		Grid<Map> grid = new Grid<Map>();
		grid.setRows(deviceList);
		grid.setTotal(deviceCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-19 
	 * @功能描述: 根据员工卡号和房间号查看用户权限
	 */
	public void getPeopleAuthority(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);
		String flag = request.getParameter("flag");
		if("onlyValid".equals(flag)){
			map.put("authority_flag", "0,1,4");   
		} else if("current".equals(flag)){
			map.put("authority_flag", "0,1,2,3,4");
		} else if("lose".equals(flag)){
			map.put("authority_flag", "5,6,7");
		}
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		List<Map> staffList =  authorityManagerService.getPeopleAuthority(map);
		Integer staffCount = authorityManagerService.getPeopleAuthorityCount(map);
		Grid<Map> grid = new Grid<Map>();
		grid.setRows(staffList);
		grid.setTotal(staffCount);
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-18 
	 * @功能描述: 下发权限
	 */
	public void issuedPeopleAuthority(){
		JsonResult result = new JsonResult();
		
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		List<Map> lightList = GsonUtil.toBean(beanObject, ArrayList.class);
	
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		
		List<AuthorityManagerModel> authList = new ArrayList<AuthorityManagerModel>();
		for (Map<String, Object> lightMap : lightList) {
			AuthorityManagerModel model = new AuthorityManagerModel();
			model.setAuthority_num(UUID.randomUUID().toString());
			model.setLight_id(lightMap.get("light_id").toString()); 
			model.setDevice_id(lightMap.get("device_id").toString());
			model.setBegin_valid_date(lightMap.get("begin_valid_date").toString());
			model.setEnd_valid_date(lightMap.get("end_valid_date").toString());
			model.setNumber_code(lightMap.get("number_code").toString());
			model.setCreate_user(create_user);
			authList.add(model);
		}
	
		boolean issuedResult = authorityManagerService.addPeopleAuthority(authList);
		if(issuedResult){
			result.setMsg("授权任务已添加成功,请到权限任务表查看");
			result.setSuccess(true);
		}
		
	    printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-19
	 * @功能描述: 删除权限
	 */
	public void delePeopleAuthority(){
		JsonResult result = new JsonResult();
		
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		List<LinkedTreeMap> list =  new Gson().fromJson(beanObject,new ArrayList<LinkedTreeMap>().getClass());
		
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		
		List<AuthorityManagerModel> authList = new ArrayList<AuthorityManagerModel>();
		for(int i=0;i<list.size();i++){
			AuthorityManagerModel model = new AuthorityManagerModel();
			model.setAuthority_num(list.get(i).get("authority_num").toString());
			model.setLight_id(list.get(i).get("light_id").toString());
			model.setDevice_id(list.get(i).get("device_id").toString());
			model.setBegin_valid_date(list.get(i).get("begin_valid_date").toString());
			model.setEnd_valid_date(list.get(i).get("end_valid_date").toString());
			model.setAuthority_flag(list.get(i).get("authority_flag").toString());
			model.setCreate_user(create_user);
			authList.add(model);
		}
		boolean delResult = authorityManagerService.delePeopleAuthority(authList);
		
		if(delResult){
			result.setMsg("删除权限任务已添加成功,请到权限任务表查看");
			result.setSuccess(true);
		} 
	    printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	//判断卡是否有权限
	public void isAuth(){
		HttpServletRequest request = getHttpServletRequest();
        JsonResult result=new  JsonResult();
        try {
    		String cardNums = request.getParameter("cardNums");
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("card_nums", cardNums);
    		List<AuthorityManagerModel> authList = authorityManagerService.getAuthorityByCards(map);
    		
    		if(authList.size()>0){
    			result.setSuccess(true);
    		}else{
    			result.setSuccess(false);
    		}
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
		}
		
		printHttpServletResponse(new Gson().toJson(result));	
   }
	
	
	/**
	 * 复制人员权限
	 * @author yuman.gao
	 */
	public void copyPeopleAuthority(){
        JsonResult result=new  JsonResult();

        User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
        
		HttpServletRequest request = getHttpServletRequest();
		String source_card = request.getParameter("source_card");
		String[] target_cards = request.getParameter("target_cards").split(",");
		
		// 查询源卡上权限
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_nums", source_card);
		List<AuthorityManagerModel> authList = authorityManagerService.getAuthorityByCards(map);
		
		// 封装目标卡权限
		List<AuthorityManagerModel> issuedList = new ArrayList<AuthorityManagerModel>();
		for (int i=0; i<authList.size(); i++) {
			for (int j=0; j<target_cards.length; j++) {
				AuthorityManagerModel model = new AuthorityManagerModel();
				model.setAuthority_num(UUID.randomUUID().toString());
				model.setLight_id(target_cards[j]); 
				model.setDevice_id(authList.get(i).getDevice_id());
				model.setBegin_valid_date(authList.get(i).getBegin_valid_date());
				model.setEnd_valid_date(authList.get(i).getEnd_valid_date());
				model.setNumber_code(authList.get(i).getNumber_code());
				model.setCreate_user(loginUser.getYhMc());
				issuedList.add(model);
			}
		}
		authorityManagerService.addPeopleAuthority(issuedList);
		
		result.setMsg("复制权限任务已添加成功,请到权限任务表查看");
		result.setSuccess(true);
		
		printHttpServletResponse(new Gson().toJson(result));
	}

	
	
	/**
	 * 手动同步员工信息
	 * @author yuman.gao
	 */
	public void handSynchronized(){
		
		JsonResult result = new JsonResult();
//		HttpServletRequest request = getHttpServletRequest();
//		String company_num = request.getParameter("company_num");
		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress().toString();
			boolean synResult = interService.synchronizedUser(server_ip);
			if(synResult){
				result.setSuccess(true);
				result.setMsg("同步成功");
			} else {
				result.setSuccess(false);
				result.setMsg("同步失败");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result)); 
		}
	}
	
	
	/**
	 * @创建人　: chudan.guo
	 * @功能描述: 自动下发权限设置
	 */
	public void autoIssuedAuthority(){
		JsonResult result = new JsonResult();
		
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);
		boolean rt=authorityManagerService.autoIssuedAuthority(map);
		if(rt){
			result.setMsg("设置成功");
			result.setSuccess(true);
		} else {
			result.setMsg("设置失败");
			result.setSuccess(false);
		}
		
	    printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	 * @创建人　: chudan.guo
	 * @功能描述: 查找已有的自动下发任务
	 */
	public void getAutoIssuedAuthority(){
		List<Map> list = authorityManagerService.getAutoIssuedAuthority();
		String deviceId="";
		String rooms="";
		String begin_valid_date="";
		String end_valid_date="";
		String auto_flag="";
		if(list.size()!=0){
			for(int i=0;i<list.size();i++){
				deviceId += list.get(i).get("device_id")+",";
				rooms += list.get(i).get("room_num")+",";
			}
			deviceId = deviceId.substring(0, deviceId.length()-1);
			rooms = rooms.substring(0, rooms.length()-1);
			begin_valid_date=list.get(0).get("begin_valid_date").toString();
			end_valid_date=list.get(0).get("end_valid_date").toString();
			auto_flag=list.get(0).get("auto_flag").toString();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("begin_valid_date",begin_valid_date );
		map.put("end_valid_date",end_valid_date);
		map.put("device_id", deviceId);
		map.put("room_num", rooms);
		map.put("auto_flag",auto_flag );
		printHttpServletResponse(GsonUtil.toJson(map));
	}
	
	

	/**
	 * 查询所有公司名称
	 * @author yuman.gao
	 */
	public void getCompany(){
		List<Map> staffList =  authorityManagerService.getCompany();
		printHttpServletResponse(new Gson().toJson(staffList));
	}
	
	/**
	 * 设置员工同步参数
	 * @author yuman.gao
	 */
	public void setSynchParam(){
		
		JsonResult result = new JsonResult();
		
		HttpServletRequest request = getHttpServletRequest();
		//String company_num = request.getParameter("company_num");
		String time = request.getParameter("time");
		
		String cronExpression = "0 0 0/" + time + " * * ?";
		String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		
		boolean flag = com.kuangchi.sdd.util.file.PropertyUtils.setProperties(propertyFile, "userSynchronCron", cronExpression,null);
		if (flag) {
			try {
				TriggerKey triggerKey = new TriggerKey("updateUserTimer");
				
				CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
				trigger.setCronExpression(cronExpression);
				scheduler.rescheduleJob(triggerKey, trigger);
				
				// 更新同步公司对象
				//authorityManagerService.setTimerObject(company_num);
				result.setSuccess(true);
				result.setMsg("设置成功");
				
			} catch (Exception e) {
				result.setSuccess(false);
				result.setMsg("设置失败");
				e.printStackTrace();
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 获取员工同步参数
	 * @author yuman.gao
	 */
	public void getSynchParam(){
		HttpServletRequest request = getHttpServletRequest();
		
		// 查询公司
		List<Map> company = authorityManagerService.getTimerCompany();
		String timerCompanyNum = "";
		if(company != null && company.size() != 0){
			timerCompanyNum = company.get(0).get("company_num").toString();
		} 
		
		//　查询频率
		String propertyFile=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
    	Properties  property=PropertyUtils.readProperties(propertyFile);
		String cron=property.getProperty("userSynchronCron");
		int index=cron.lastIndexOf("/");
		String time=cron.substring(index+1, index+3);
		
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("time", time);
		map.put("company_num", timerCompanyNum);
		printHttpServletResponse(GsonUtil.toJson(map));
	}
}
