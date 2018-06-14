package com.kuangchi.sdd.attendanceConsole.attendAnalysis.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.attendanceConsole.attendAnalysis.model.AttendanceRateModel;
import com.kuangchi.sdd.attendanceConsole.attendAnalysis.model.ResultModel;
import com.kuangchi.sdd.attendanceConsole.attendAnalysis.service.IAttendAnalysisService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;

@Controller("attendanceAnalysisAction")
public class AttendAnalysisAction extends BaseActionSupport {
	
	@Resource(name = "attendAnalysisService")
	private IAttendAnalysisService attendAnalysisService;
	
	private SimpleDateFormat format=new SimpleDateFormat("yyy-MM-dd");
	
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-13 下午7:45:14
	 * @功能描述: 员工本月考勤分析
	 * @参数描述:
	 */
	public void monthAttendanceAnalysis(){
		
		ResultModel result = new ResultModel();
		
		Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		if(session_staff == null){
			result.setSuccess(false);
			result.setMsg("用户信息失效，请重新登录");
			printHttpServletResponse(new Gson().toJson(result));
			return;
		}
		
		//获取当前月第一天
		Calendar calendar = Calendar.getInstance();    
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		String firstDate = format.format(calendar.getTime());
		
		//获取当前月最后一天
		Calendar c = Calendar.getInstance();    
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));  
		String lastDate = format.format(c.getTime());
		
		String staff_num = session_staff.getStaff_num();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_num", staff_num);
		map.put("firstDate", firstDate);
		map.put("lastDate", lastDate);
		AttendanceRateModel attendRate = attendAnalysisService.getAttendanceRate(map);
		
		if(attendRate.getTotal_rate() != null && attendRate.getTotal_rate().equals("0")){
			result.setSuccess(false);
			result.setNoData(true);
			result.setMsg("该员工没有考勤数据");
			printHttpServletResponse(new Gson().toJson(result));
			return;
		}
		
		result.setSuccess(true);
		result.setNoData(false);
		result.setMsg("获取本月考勤分析数据成功");
		result.setAttendanceRate(attendRate);
		printHttpServletResponse(new Gson().toJson(result));
	
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-16 上午9:34:06
	 * @功能描述: 员工年度考勤分析
	 * @参数描述:
	 */
	public void yearAttendanceAnalysis(){
		
		ResultModel result = new ResultModel();
		
		Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		if(session_staff == null){
			result.setSuccess(false);
			result.setMsg("用户信息失效，请重新登录");
			printHttpServletResponse(new Gson().toJson(result));
			return;
		}
		
		//获取当前年第一天
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        String firstDate = format.format(calendar.getTime());
		
        //获取当前年最后一天
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        c.roll(Calendar.DAY_OF_YEAR, -1);
        String lastDate = format.format(c.getTime());
        
        String staff_num = session_staff.getStaff_num();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_num", staff_num);
		map.put("firstDate", firstDate);
		map.put("lastDate", lastDate);
		AttendanceRateModel attendRate = attendAnalysisService.getAttendanceRate(map);
		
		if(attendRate.getTotal_rate() != null && attendRate.getTotal_rate().equals("0")){
			result.setSuccess(false);
			result.setNoData(true);
			result.setMsg("该员工没有考勤数据");
			printHttpServletResponse(new Gson().toJson(result));
			return;
		}
		
		result.setSuccess(true);
		result.setNoData(false);
		result.setMsg("获取年度考勤分析数据成功");
		result.setAttendanceRate(attendRate);
		printHttpServletResponse(new Gson().toJson(result));
	}
}
