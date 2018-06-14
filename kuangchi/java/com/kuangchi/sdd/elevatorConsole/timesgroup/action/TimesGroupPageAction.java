package com.kuangchi.sdd.elevatorConsole.timesgroup.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.elevatorConsole.timesgroup.model.TimesGroupModel;
import com.kuangchi.sdd.elevatorConsole.timesgroup.service.TimesGroupService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("timesGroupPageAction")
public class TimesGroupPageAction extends BaseActionSupport {
	

	@Override
	public Object getModel() {
		return null;
	}
	
	@Resource(name="timesGroupServiceImpl")
	private TimesGroupService  timesGroupSercice;
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-27 上午11:05:44
	 * @功能描述:全部时段组信息 
	 * @参数描述:
	 */
	public void getTimesGroupPage(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<TimesGroupModel> grid = new Grid<TimesGroupModel>();
		String beanObject = request.getParameter("data");
		Map<String, Integer> map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer page = (Integer.parseInt(request.getParameter("page"))-1)*rows;
		map.put("rows", rows);
		map.put("page", page);
		grid = timesGroupSercice.getTimesGroupPage(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
		
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-27 上午11:35:06
	 * @功能描述: 新增时段组信息 
	 * @参数描述:
	 */
	public void insertTimesGroup(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String beanObject = request.getParameter("data");
		Map<String, String> map = GsonUtil.toBean(beanObject, HashMap.class);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Date date = new Date();
		String create_time = format.format(date);
		
		map.put("create_time", create_time);
		
		if(EmptyUtil.isEmpty(beanObject)){
			result.setSuccess(false);
			result.setMsg("新增失败，数据不合法");
		}else{
			 User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			 if(null==loginUser){
				 	result.setSuccess(false);
					result.setMsg("操作失败，请先登录");
			 }else{
			 String create_user = loginUser.getYhMc();
			 boolean res = timesGroupSercice.insertTimesGroup(map, create_user);
			 if(res){
				result.setSuccess(true);
				result.setMsg("新增时段组成功");
			}else{
				result.setMsg("新增时段组失败");
				result.setSuccess(false);
				}
			 }
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	public void checkTimeGroupNumUnique(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String time_group_num = request.getParameter("time_group_num");
		if(EmptyUtil.isEmpty(time_group_num)){
			result.setSuccess(false);
			result.setMsg("新增失败，数据不合法");
		}else{
		Integer count = timesGroupSercice.checkTimeGroupNumUnique(time_group_num);
		if(count>0){
			result.setSuccess(false);
		}else{
			result.setSuccess(true);
			}
		printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
}