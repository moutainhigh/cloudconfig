package com.kuangchi.sdd.baseConsole.times.holidaytimes.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesInterf;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.service.HolidayTimesService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-5-24上午10:26:39
 * @功能描述:节假日时段管理Action
 * @参数描述:
 */
@Controller("holidayTimesAction")
public class HolidayTimesAction extends BaseActionSupport {
	private static final Logger LOG = Logger.getLogger(HolidayTimesAction.class);
	
	@Resource(name="holidayTimesServiceImpl")
	private HolidayTimesService holidayTimesService;
	private HolidayTimesModel holidayTimesModel;
	
	@Override
	public Object getModel() {
		return holidayTimesModel;
	}
	
	/**页面显示*/
	public String showView(){
		return "success";
	}
	
	/**根据条件查询*/
	public void getHolidayTimesByParam(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		String exist_nums = request.getParameter("exist_nums");
		String page =request.getParameter("page");
		String size =request.getParameter("rows");
		
		HolidayTimesModel model=GsonUtil.toBean(beanObject,HolidayTimesModel.class);
		Grid<HolidayTimesModel> GridList=holidayTimesService.getHolidayTimesByParamService(model, exist_nums, Integer.valueOf(page), Integer.valueOf(size));
		printHttpServletResponse(GsonUtil.toJson(GridList));
	}
	
	/**新增*/
	public void addHolidayTimes(){
		
		JsonResult result = new JsonResult();
		
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		String holidayDate=getHttpServletRequest().getParameter("holiday_date");
		String dayOfWeek=getHttpServletRequest().getParameter("day_of_week");
		String decript=getHttpServletRequest().getParameter("u_description");
		String hts_num=getHttpServletRequest().getParameter("hts_num");
		
		// 后台判断，不能为空
		if (EmptyUtil.atLeastOneIsEmpty(holidayDate,dayOfWeek,hts_num)||dayOfWeek=="-1") {
			result.setMsg("必输参数不能为空");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		
		HolidayTimesModel model=new HolidayTimesModel();
		model.setHoliday_time_num(hts_num);
		model.setHoliday_date(holidayDate);
		model.setDay_of_week(dayOfWeek);
		model.setCreate_user(login_User);
		model.setDescription(decript);
		
		List<HolidayTimesModel> sizeHt=holidayTimesService.getByholidayNumService("'"+hts_num+"'");
		if(sizeHt.size()>0){
			result.setMsg("该节假日时段编号已存在");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		} else {
			boolean addresult=false;
			try {
				addresult = holidayTimesService.insertHolidayTimesService(model, login_User);
				if(addresult){
					result.setMsg("新增节假日时段成功");
					result.setSuccess(true);
				}else{
					result.setMsg("新增节假日时段失败,已经超过128个");
					result.setSuccess(false);
				}
			} catch (Exception e) {
				result.setMsg("新增节假日时段失败");
				result.setSuccess(false);
			} finally {
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		}
	}

	/**修改*/
	public void modifyHolidayTimes(){
		
		JsonResult result = new JsonResult();
		
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		String hT_id=getHttpServletRequest().getParameter("holidayTimes_id");
		String holidayDate=getHttpServletRequest().getParameter("holiday_date");
		String dayOfWeek=getHttpServletRequest().getParameter("day_of_week");
		String descri=getHttpServletRequest().getParameter("u_description");
		String hts_num=getHttpServletRequest().getParameter("hts_num");
		
		// 后台判断，不能为空
		if (EmptyUtil.atLeastOneIsEmpty(holidayDate,dayOfWeek,hT_id,hts_num)||dayOfWeek=="-1") {
			result.setMsg("必输参数不能为空");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		HolidayTimesModel model=new HolidayTimesModel();
		model.setHoliday_time_num(hts_num);
		model.setHolidayTimes_id(Integer.valueOf(hT_id));
		model.setHoliday_date(holidayDate);
		model.setDay_of_week(dayOfWeek);
		model.setCreate_user(login_User);
		model.setDescription(descri);
		
		List<HolidayTimesModel> sizeHt=holidayTimesService.getByholidayNumService("'"+hts_num+"'");
		if(sizeHt.size()==0){
			boolean modifyResult=false;
			try {
				modifyResult = holidayTimesService.updateHolidayTimesService(model, login_User);
				
				if(modifyResult){
					result.setMsg("修改节假日时段成功");
					result.setSuccess(true);
				}else{
					result.setMsg("修改节假日时段失败");
					result.setSuccess(false);
				}
			} catch (Exception e) {
				result.setMsg("修改节假日时段失败");
				result.setSuccess(false);
			}finally{
				printHttpServletResponse(GsonUtil.toJson(result));
			}
			
		}else if((sizeHt.get(0).getHolidayTimes_id().equals(model.getHolidayTimes_id()))&&(sizeHt.get(0).getHoliday_time_num().equals(model.getHoliday_time_num()))){
			boolean modifyResult=false;
			try {
				modifyResult = holidayTimesService.updateHolidayTimesService(model, login_User);
				
				if(modifyResult){
					result.setMsg("修改节假日时段成功");
					result.setSuccess(true);
				}else{
					result.setMsg("修改节假日时段失败");
					result.setSuccess(false);
				}
			} catch (Exception e) {
				result.setMsg("修改节假日时段失败");
				result.setSuccess(false);
			} finally {
				printHttpServletResponse(GsonUtil.toJson(result));
			}
			
		}else{
			result.setMsg("该节假日时段编号已存在");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**删除*/
	public void delHolidayTimes(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		String holidayTimes_ids=request.getParameter("holidayTimes_ids");
		boolean delResult=false;
		try {
			delResult = holidayTimesService.delHolidayTimesService(holidayTimes_ids, login_User);
			if(delResult){
				result.setMsg("删除成功");
				result.setSuccess(true);
			}else{
				result.setMsg("删除失败");
				result.setSuccess(false);
			}
		} catch (Exception e) {
			result.setMsg("删除失败");
			result.setSuccess(false);
		}finally{
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**导出（查询条件下的）*/
	public void exportHolidayTimes(){
		//导出的方法
		List<HolidayTimesInterf> list=holidayTimesService.getHolidayTimesInterService();
	}
	
	
}

	
