package com.kuangchi.sdd.doorAccessConsole.authority.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.doorAccessConsole.authority.service.AthorityTaskService;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Scope("prototype")
@Controller("authorityTaskAction")
public class AuthorityTaskAction extends BaseActionSupport{

	private static final long serialVersionUID = -4551270484793566334L;
	
	@Resource(name = "athorityTaskServiceImpl")
	private AthorityTaskService athorityTaskService;
	
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-10-10 
	 * @功能描述: 查询所有下发权限任务（分页）
	 * @参数描述: 
	 */
	public  void  getAuthorityTask(){
		HttpServletRequest request= getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=athorityTaskService.getAuthorityTask(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-10-10 
	 * @功能描述: 获取时段组信息
	 * @参数描述: 
	 */
	public  void  getTimeGroup(){
		List<Map> list=athorityTaskService.getTimeGroup();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-10-11 
	 * @功能描述: 下载未下载权限
	 * @参数描述: 
	 */
	public  void  downloadFailureAuthority(){
		JsonResult  result=new JsonResult();
		
		Date date=new Date();//创建当前系统时间
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today=format.format(date);
        
        User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
        String create_user=loginUser.getYhMc();
		try {
			List<Map>  mapList=athorityTaskService.getFailureAuthorityTask();
			//boolean flag1=athorityTaskService.deleteRepeatAuthorityTask(mapList, today, create_user);
			if(mapList.size()>0){
				boolean flag2=athorityTaskService.addFailureAuthorityTask(mapList, today, create_user);
				if(flag2){
					result.setSuccess(true);
					result.setMsg("下载成功");
				}else{
					result.setSuccess(false);
					result.setMsg("下载失败");
				}
			}else{
					result.setSuccess(false);
					result.setMsg("下载完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("下载失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-01-06 
	 * @功能描述: 下载已删除权限
	 * @参数描述: 
	 */
	public  void  downloadDeletedAuthority(){
		JsonResult  result=new JsonResult();
		
		HttpServletRequest request=getHttpServletRequest();
		String id=request.getParameter("id");
		
		Date date=new Date();//创建当前系统时间
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String today=format.format(date);
		
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
		String create_user=loginUser.getYhMc();
		try {
			List<Map>  mapList=athorityTaskService.getDeletedAuthById(id);
			for(Map m:mapList){
				m.put("action_flag", "0");
				m.put("create_time", today);
			}
			//boolean flag1=athorityTaskService.deleteRepeatAuthorityTask(mapList, today, create_user);
			if(mapList.size()>0){
				addWorker(mapList,"00");
				boolean flag2=athorityTaskService.addFailureAuthorityTask(mapList, today, create_user);
				if(flag2){
					result.setSuccess(true);
					result.setMsg("下载成功");
				}else{
					result.setSuccess(false);
					result.setMsg("下载失败");
				}
			}else{
				result.setSuccess(false);
				result.setMsg("下载完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("下载失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	
	/**
	 * 新增权限时
	 * 先插入权限记录，task_state=00
	 */
	public void addWorker(List<Map>authList,String state){
		for(Map map:authList){
			map.put("state",state);
			map.put("cardNum", map.get("card_num"));
			map.put("doorNum", map.get("door_num"));
			map.put("deviceNum", map.get("device_num"));
			map.put("startTime", map.get("valid_start_time"));
			map.put("endTime", map.get("valid_end_time"));
			map.put("groupNum", map.get("time_group_num"));
			peopleAuthorityInfoService.delAuthRecord(map);
			peopleAuthorityInfoService.addAuthRecord(map);
		}
	}
	
	
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//查询权限任务表kc_doorsys_authority_task  guofei.lian 2016-10-13
	public void getAuthorityTaskList(){
		HttpServletRequest request= getHttpServletRequest();
		Gson gson=new Gson();
		Map map=gson.fromJson(request.getParameter("data"), HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		
		Grid grid=athorityTaskService.getAuthorityTaskList(map,page,rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

}
