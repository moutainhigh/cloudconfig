package com.kuangchi.sdd.baseConsole.times.timesgroup.action;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;
import com.kuangchi.sdd.baseConsole.times.timesgroup.service.TimesGroupService;
import com.kuangchi.sdd.baseConsole.times.timesobject.service.TimesObjectService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-4-6 下午16:32:05
 * @功能描述: 时段组Action
 */

@Controller("timesGroupAction")
public class TimesGroupAction extends BaseActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "timesGroupServiceImp")
	transient private TimesGroupService timesGroupService;
	
	@Resource(name = "timesObjectServiceImpl")
	private TimesObjectService timesObjectService;
	
	@Resource(name = "deviceService")
	DeviceService deviceService;
	
	public Object getModel() {return null;}
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14下午17:32:05
	 * @功能描述: 新增时段组
	 */
	public void addTimesGroup(){
		JsonResult result = new JsonResult();
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String beanObject = getHttpServletRequest().getParameter("data");
		TimesGroup timesGroup = GsonUtil.toBean(beanObject, TimesGroup.class);
		//查询组名是否重复
		List<TimesGroup> checkGroupName = timesGroupService.getTimesGroupsByName(timesGroup.getGroup_name());
		if(checkGroupName==null||checkGroupName.size()==0){
			timesGroup.setCreate_user(loginUser.getYhMc());
			Integer maxNum = timesGroupService.getMaxNum();
			timesGroup.setGroup_num(maxNum==null?"0":""+(maxNum+1));
			
			List<TimesGroup> timesGroupList = new ArrayList<TimesGroup>();
			//循环插入8条相同记录
			for(int i=0;i<8;i++) timesGroupList.add(timesGroup);
			
			if(timesGroupService.addTimesGroup(timesGroupList)){
				result.setSuccess(true);
				result.setMsg("新增成功！");
			}else{
				result.setSuccess(false);
				result.setMsg("新增失败！");
			}
		}else{
			result.setSuccess(false);
			result.setMsg("该时段组名称已经存在！");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午14:32:05
	 * @功能描述: 查询所有时段
	 */
	public void getTimes(){
		Times times = new Times();
		List<Times> timesList = timesGroupService.getTimesByParamPageSortByBeginTime(times);
		for (Times time : timesList) {
			if ("0000".equals(time.getBegin_time())
                && "0000".equals(time.getEnd_time())) {
            time.setBegin_time("None");
        } else {
            time.setBegin_time(time.getBegin_time().substring(0, 2) + ":"
                    + time.getBegin_time().substring(2, 4) + " - "
                    + time.getEnd_time().substring(0, 2) + ":"
                    + time.getEnd_time().substring(2, 4));
        }
}
		printHttpServletResponse(GsonUtil.toJson(timesList));
	}
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14 下午15:32:05
	 * @功能描述: 查询时段组(分页)
	 */
	public void getTimesGroupByParam(){
		String group_id = getHttpServletRequest().getParameter("group_id");
		String group_name = getHttpServletRequest().getParameter("group_name");
		String group_num =  getHttpServletRequest().getParameter("group_num");
		String times_priority = getHttpServletRequest().getParameter("times_priority");
		Integer timesGroupCount = timesGroupService.getTimesGroupCount(group_id, group_name, group_num, times_priority, "");
		Integer page = Integer.valueOf(getHttpServletRequest().getParameter("page"));
		Integer size = Integer.valueOf(getHttpServletRequest().getParameter("rows"));
		List<TimesGroup> timesGroupList = timesGroupService.getTimesGroupByParam(group_id, group_name, group_num, times_priority, "",page, size);
		Grid<TimesGroup> grid = new Grid<TimesGroup>();
		grid.setTotal(timesGroupCount);
		grid.setRows(timesGroupList);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-1  下午15:32:05
	 * @功能描述: 根据设备编号查询时段组
	 */
	public void getTimesGroupByDevice(){
		String object_num = getHttpServletRequest().getParameter("object_num");
		reAddTimesObject(object_num, "1");
		List<TimesGroup> timesGroupList = timesGroupService.getTimesGroupByDevice(object_num);
		printHttpServletResponse(GsonUtil.toJson(timesGroupList));
	}
	
	/**
	 * 查询前重新下发对象时段组，防止与服务器数据不一致
	 * @author yuman.gao
	 */
	public void reAddTimesObject(String device_num, String object_type){
		try {
			HttpServletRequest request = getHttpServletRequest();
			User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
			
			//List<HolidayTimesModel> allHolidayTime = timesObjectService.getHolidayTimeByDevice(device_num);
			List<TimesGroup> timesGroupList = timesGroupService.getTimesGroupByDevice(device_num);
			StringBuilder group_nums = new StringBuilder();
			if(timesGroupList != null && timesGroupList.size()>0){
				for (TimesGroup timesGroup : timesGroupList) {
					group_nums.append(timesGroup.getGroup_num());
					group_nums.append(",");
				}
				group_nums.setLength(group_nums.length()-1);
			} 
			
			DeviceInfo device = deviceService.getDeviceInfoByNum(device_num);
			String device_mac = device.getDevice_mac();
			String device_type = device.getDevice_type();
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("group_nums", group_nums.toString());
			map.put("object_type", object_type);
			map.put("device_num", device_num);
			map.put("device_mac", device_mac);
			map.put("device_type", device_type);
			map.put("loginUserName", loginUser.getYhMc());
			
			timesObjectService.addTimesObject(map);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14 下午15:32:05
	 * @功能描述: 查询时段组(分页)
	 */
	public void getTimesGroupByParamE(){
		String group_id = getHttpServletRequest().getParameter("group_id");
		String group_name = getHttpServletRequest().getParameter("group_name");
		String group_num =  getHttpServletRequest().getParameter("group_num");
		String times_priority = getHttpServletRequest().getParameter("times_priority");
		String exist_nums = getHttpServletRequest().getParameter("exist_nums");
		Integer timesGroupCount = timesGroupService.getTimesGroupCount(group_id, group_name, group_num, times_priority, exist_nums);
		Integer page = Integer.valueOf(getHttpServletRequest().getParameter("page")==null?"1":getHttpServletRequest().getParameter("page"));
		Integer size = Integer.valueOf(getHttpServletRequest().getParameter("rows")==null?"10":getHttpServletRequest().getParameter("rows"));
		List<TimesGroup> timesGroupList = timesGroupService.getTimesGroupByParam(group_id, group_name, group_num, times_priority, exist_nums,page, size);
		Grid<TimesGroup> grid = new Grid<TimesGroup>();
		grid.setTotal(timesGroupCount);
		grid.setRows(timesGroupList);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14 下午15:32:05
	 * @功能描述: 查询时段组(不分页)
	 */
	public void getTimesGroupsByParam(){
		String group_id = getHttpServletRequest().getParameter("group_id");
		String group_name = getHttpServletRequest().getParameter("group_name");
		String group_num =  getHttpServletRequest().getParameter("group_num");
		String times_priority = getHttpServletRequest().getParameter("times_priority");
		List<TimesGroup> timesGroupList = timesGroupService.getTimesGroupsByParam(group_id, group_name, group_num, times_priority);
		printHttpServletResponse(GsonUtil.toJson(timesGroupList));
	}
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 设置时段组
	 */
	public void modifyTimesGroup(){
		
		JsonResult result = new JsonResult();
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		List<TimesGroup> timesGroupList = new ArrayList<TimesGroup>();
		//查询时段组名，防止重复
		String groupName = getHttpServletRequest().getParameter("groupName");
		String groupNum = getHttpServletRequest().getParameter("group_num");
		List<TimesGroup> checkGroupName = timesGroupService.getTimesGroupsByName(groupName);
		
		if(checkGroupName==null||checkGroupName.size()==0||checkGroupName.get(0).getGroup_num().equals(groupNum)){
			//时段组名没重复或者没更改，可以修改时段组名
			String description = getHttpServletRequest().getParameter("description");
			//String timesPriority = getHttpServletRequest().getParameter("timesPriority");
			
			for(int i=0;i<8;i++){
				String beanObject = getHttpServletRequest().getParameter("data"+i);
				TimesGroup timesGroup = GsonUtil.toBean(beanObject, TimesGroup.class);
				Class<? extends TimesGroup> tg = timesGroup.getClass();
				Class<?> tgf = tg.getSuperclass();
				try {
					Field fgroupName = tg.getDeclaredField("group_name");
					fgroupName.setAccessible(true);
					fgroupName.set(timesGroup, groupName);
					
					/*Field ftimesPriority = tg.getDeclaredField("times_priority");
					ftimesPriority.setAccessible(true);
					ftimesPriority.set(timesGroup, timesPriority);*/
					
					Field fcreateUser = tgf.getDeclaredField("create_user");
					fcreateUser.setAccessible(true);
					fcreateUser.set(timesGroup, loginUser.getYhMc());
					
					Field fdescription = tgf.getDeclaredField("description");
					fdescription.setAccessible(true);
					fdescription.set(timesGroup, description);
					
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				timesGroupList.add(timesGroup);
			}
			if(timesGroupService.modifyTimesGroup(timesGroupList)){
				result.setSuccess(true);
				result.setMsg("修改成功！");
			}else{
				result.setSuccess(false);
				result.setMsg("修改失败！");
			}
		}else{
			result.setSuccess(false);
			result.setMsg("时段组名已经存在！");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 删除时段组(伪删除)
	 */
	public void deleteTimesGroup(){
		JsonResult result = new JsonResult();
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String group_nums = getHttpServletRequest().getParameter("group_nums");
		String[] groupNums = group_nums.split(",");
		boolean canDelete = true;
		StringBuffer usedTimesGroupNameBuf = new StringBuffer("<b>以下被'对象时段组'绑定，不能删除！</b><br><br>");
		for (int i = 0; i < groupNums.length; i++) {
			String group_num = groupNums[i];	
			int timesObjecId = timesGroupService.getTimesObjectIdByGroupNum(group_num.substring(1, group_num.length()-1));
			
			if (timesObjecId>0) {
				Integer timesGroupCount = timesGroupService.getTimesGroupCount("", "", "", "", "");
				List<TimesGroup> timesGrpup = timesGroupService.getTimesGroupByParam("", "",group_num.substring(1, group_num.length()-1), "","",1 ,timesGroupCount);
				usedTimesGroupNameBuf.append(timesGrpup.get(0).getGroup_name()+"&nbsp");
				canDelete = false;
			}
		}
		if(!canDelete){
			result.setMsg(usedTimesGroupNameBuf.toString());
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		if(timesGroupService.deleteTimesGroup(group_nums,loginUser.getYhMc())){
			result.setMsg("删除成功！");
			result.setSuccess(true);
		}else{
			result.setMsg("删除失败！");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-78下午15:32:05
	 * @功能描述: 查看功能,查看时段组下时段
	 */
	public void getTimesGroupView(){
		String group_num = getHttpServletRequest().getParameter("group_num");
		List<TimesGroup> timesGroupList = timesGroupService.getTimesGroupsByParam("", "", group_num, "");
		Times times = null;
		List<Times> timesList = null;
		
		for (TimesGroup timesGroup : timesGroupList) {
			times = new Times();
			Class<? extends TimesGroup> tg = timesGroup.getClass();
			Field[] fields = tg.getDeclaredFields();
			for(int i=3;i<=10;i++){
				fields[i].setAccessible(true);
				try {
					if(fields[i].get(timesGroup)==null){
						fields[i].set(timesGroup, "");
					}else{
						times.setTimes_num((String)fields[i].get(timesGroup));
						timesList = timesGroupService.getTimesByParamPageSortByBeginTime(times);
						for (Times time : timesList) {
							Class<? extends Times> t = time.getClass();
							String tfBegin = "";
							String tfEnd = "";
							try {
								Field tfb = t.getDeclaredField("begin_time");
								Field tfe = t.getDeclaredField("end_time");
								tfb.setAccessible(true);
								tfe.setAccessible(true);
								tfBegin = (String)tfb.get(time);
								tfEnd = (String)tfe.get(time);
								tfb.set(time, tfBegin.substring(0, 2)+":"+tfBegin.substring(2, 4)+" - "+tfEnd.substring(0, 2)+":"+tfEnd.substring(2, 4));
								fields[i].set(timesGroup, (String)tfb.get(time));
							} catch (NoSuchFieldException e) {
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(timesGroupList));
	}
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-5-30  下午15:32:05
	 * @功能描述: 根据组num查询时段组(不分页)
	 */
	public void getTimesGroupsByNum(){
		String getNums = getHttpServletRequest().getParameter("group_nums");
		List<TimesGroup> groupList = timesGroupService.getTimesGroupsByNum(getNums);
		printHttpServletResponse(GsonUtil.toJson(groupList));
	}
}	
