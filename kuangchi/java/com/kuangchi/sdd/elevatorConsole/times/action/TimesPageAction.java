package com.kuangchi.sdd.elevatorConsole.times.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.elevatorConsole.times.model.TimesPageModel;
import com.kuangchi.sdd.elevatorConsole.times.service.TimesPageService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("timesPageAction")
public class TimesPageAction extends BaseActionSupport{

	@Override
	public Object getModel() {
		return null;
	}
	
	@Resource(name="timesPageServiceImpl")
	private TimesPageService timesPageService;
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-28 下午12:30:59
	 * @功能描述:设置时段信息 
	 * @参数描述:
	 */
	public void motifyTimesPage(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		List<TimesPageModel> list = new ArrayList<TimesPageModel>();
		String time_group_num = request.getParameter("time_group_num");
		if(EmptyUtil.isEmpty(time_group_num)){
			result.setMsg("设置失败,数据不合法");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		
		Integer count = timesPageService.getTimesPageCountByTimesGroupNum(time_group_num);//判断数据库是否存在时段组编号对应的时段信息
		
		if(count>0){
			for(int i=0;i<8;i++){
				String beanObject = request.getParameter("data"+i);
				TimesPageModel timesPageModel = GsonUtil.toBean(beanObject, TimesPageModel.class);
				timesPageModel.setDelete_flag(0);
				timesPageModel.setTime_group_num(time_group_num);
				list.add(timesPageModel);
			}
			if(timesPageService.motifyTimesPage(list,create_user)){
				result.setMsg("设置时段组成功");
				result.setSuccess(true);
			} else{
				result.setMsg("设置时段组失败");
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
			
		}else{
			for(int i=0;i<8;i++){
				String beanObject = request.getParameter("data"+i);
				TimesPageModel timesPageModel = GsonUtil.toBean(beanObject, TimesPageModel.class);
				timesPageModel.setDelete_flag(0);
				timesPageModel.setTime_group_num(time_group_num);
			}
			if(timesPageService.insertTimesPage(list,create_user)){
				result.setMsg("设置时段组成功");
				result.setSuccess(true);
			}else{
				result.setMsg("设置时段组失败");
				result.setSuccess(true);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
}
