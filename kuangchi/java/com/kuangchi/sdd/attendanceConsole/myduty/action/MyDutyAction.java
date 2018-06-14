package com.kuangchi.sdd.attendanceConsole.myduty.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.myduty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.myduty.model.DutyUser;
import com.kuangchi.sdd.attendanceConsole.myduty.service.MyDutyService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * 
 * @创建人　: 邓积辉
 * @创建时间: 2016-5-30 下午5:40:35
 * @功能描述:员工工作班次信息Action
 */
@Controller("myDutyAction")
public class MyDutyAction extends BaseActionSupport {
	private DutyUser model;

	@Resource(name="myDutyServiceImpl")
	MyDutyService myDutyService;
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-5-31 下午4:36:48
	 * @功能描述:查询个人的排班 
	 * @参数描述:
	 */
	public void getDutyUserByParamPages(){
		HttpServletRequest request = getHttpServletRequest();
		Staff sessionStaff_num = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		String beanObject = request.getParameter("data");
		DutyUser dutyUser = GsonUtil.toBean(beanObject, DutyUser.class);
		dutyUser.setStaff_num(sessionStaff_num.getStaff_num());
		dutyUser.setPage(Integer.parseInt(request.getParameter("page")));
		dutyUser.setRows(Integer.parseInt(request.getParameter("rows")));
		Grid<DutyUser> dutyUserGrid = myDutyService.getDutyUserByParamPages(dutyUser);
		printHttpServletResponse(GsonUtil.toJson(dutyUserGrid));
	}
	     
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-5-31 下午4:37:25
	 * @功能描述:根据排班类型查看班次信息 
	 * @参数描述:
	 */
	public void getDutyByDutyId(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String duty_id = request.getParameter("duty_id");
		if(EmptyUtil.isEmpty(duty_id)){
			result.setMsg("查看失败，请选择一条员工排班信息");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return ;
		}
		Duty dutyResult = myDutyService.getDutyByDutyId(duty_id);
		printHttpServletResponse(GsonUtil.toJson(dutyResult));
	}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	      
	
}
