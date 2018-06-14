package com.kuangchi.sdd.baseConsole.times.times.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.kuangchi.sdd.baseConsole.times.times.service.TimesService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.sun.mail.iap.ConnectionException;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-4-5 下午5:53:42
 * @功能描述: 时段管理模块Action
 */
@Controller("timesAction")
public class TimesAction extends BaseActionSupport {
	
	private static final long serialVersionUID = -6309002797333809114L;
	private static final Logger LOG = Logger.getLogger(TimesAction.class);

	@Resource(name = "timesServiceImpl")
	private TimesService timesService;
	private Times times;
	
	@Override
	public Object getModel() {
		return times;
	}
	
	public String showOpera(){
		return "success";
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-6 上午9:35:21
	 * @功能描述: 根据条件查询(页面分页显示)
	 * @参数描述:
	 */
	public void getTimesByParamPage() {

		String page = getHttpServletRequest().getParameter("page");
		String size = getHttpServletRequest().getParameter("rows");
		String begin_time = getHttpServletRequest().getParameter("begin_time");
		String end_time = getHttpServletRequest().getParameter("end_time");
		String beanObject = getHttpServletRequest().getParameter("data"); 
		
		Times times = GsonUtil.toBean(beanObject, Times.class);
		times.setBegin_time(begin_time);
		times.setEnd_time(end_time);
		List<Times> timesList = timesService.getTimesByParamPage(times, Integer.valueOf(page), Integer.valueOf(size));
		
		for(int i=0;i<timesList.size();i++){
			String begin_time_str = timesList.get(i).getBegin_time();
			String end_time_str = timesList.get(i).getEnd_time();
			timesList.get(i).setBegin_time(begin_time_str.substring(0,2)+":"+begin_time_str.substring(2, 4));
			timesList.get(i).setEnd_time(end_time_str.substring(0,2)+":"+end_time_str.substring(2, 4));
		}
		int allCount = timesService.getTimesByParamCount(times);
		Grid<Times> grid = new Grid<Times>();
		grid.setTotal(allCount);
		grid.setRows(timesList);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-6 下午3:46:45
	 * @功能描述: 增加时段组
	 * @参数描述:
	 */
	public void addTimes(){
		
		JsonResult result = new JsonResult();
		
		try {
			User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			String login_User = loginUser.getYhMc();
			String begin_time = getHttpServletRequest().getParameter("begin_time");
			String end_time = getHttpServletRequest().getParameter("end_time");
			
			Times times = new Times();
			times.setBegin_time(begin_time);
			times.setEnd_time(end_time);
			boolean addResult = timesService.addTimes(times,login_User);
			
			if(addResult){
				result.setMsg("增加时间段成功");
				result.setSuccess(true);
			}else{
				result.setMsg("编号已达最大值，不可添加");
				result.setSuccess(false);
			}
			
		} catch (Exception e) {
			result.setMsg("增加失败");
			result.setSuccess(false);
			
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-6 下午5:12:14
	 * @功能描述: 修改时间段
	 * @参数描述:
	 */
	public void modifyTimes(){
		
		JsonResult result = new JsonResult();
		try {
			User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			String login_User = loginUser.getYhMc();
			String begin_time = getHttpServletRequest().getParameter("begin_time");
			String end_time = getHttpServletRequest().getParameter("end_time");
			Integer times_id = Integer.valueOf(getHttpServletRequest().getParameter("times_id"));
			
			Times times = new Times();
			times.setBegin_time(begin_time);
			times.setEnd_time(end_time);
			times.setTimes_id(times_id);
			boolean modifyResult = timesService.modifyTimes(times,login_User);
			
			if(modifyResult){
				result.setMsg("修改时间段成功");
				result.setSuccess(true);
			} else {
				result.setMsg("修改时间段失败");
				result.setSuccess(false);
			}
		}  catch (Exception e) {
			result.setMsg("修改失败");
			result.setSuccess(false);
			
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-7 下午2:10:35
	 * @功能描述: 删除时间段
	 * @参数描述:
	 */
	public void delectTimes(){
		
		JsonResult result = new JsonResult();
		
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		String times_ids = getHttpServletRequest().getParameter("times_ids");
		String[] times_nums = getHttpServletRequest().getParameter("times_nums").split(",");
		String usedTimesName = "";
		boolean canDelete = true;
		
		for (int i = 0; i < times_nums.length; i++) {
			String times_num = times_nums[i];
			List<String> groupName = timesService.getTimesGroupByTimesNum(times_num);
			
			if (groupName.size() != 0) {
				//result.setMsg("时段"+times_num+" 与时段组“"+groupName.get(0)+"”关联" + "，不可删除");
				usedTimesName = usedTimesName + "时段"+ times_num +"&nbsp";
				canDelete = false;
			}
		}
		if(canDelete){
			timesService.deleteTimesById(times_ids,login_User);
			result.setMsg("删除成功");
			result.setSuccess(true);
		} else {
			result.setMsg(usedTimesName);
			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 时段下发
	 * @author yuman.gao
	 */
	public void issuedTime(){
		JsonResult result = new JsonResult();
		
		try {
			String deviceNums = getHttpServletRequest().getParameter("deviceNums");
			timesService.issuedTime(deviceNums);
			result.setMsg("下发成功");
			result.setSuccess(true);
			
		} catch (ConnectionException e) {
			result.setMsg("连接异常");
			result.setSuccess(false);
			
		} catch (Exception e) {
			result.setMsg("下发失败");
			result.setSuccess(false);
			
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
}
