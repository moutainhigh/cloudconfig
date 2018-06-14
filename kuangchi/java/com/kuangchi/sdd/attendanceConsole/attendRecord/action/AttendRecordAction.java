package com.kuangchi.sdd.attendanceConsole.attendRecord.action;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.attendanceConsole.attendRecord.model.AttendRecordModel;
import com.kuangchi.sdd.attendanceConsole.attendRecord.model.ResultModel;
import com.kuangchi.sdd.attendanceConsole.attendRecord.model.Outwork;
import com.kuangchi.sdd.attendanceConsole.attendRecord.service.IAttendRecordService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("attendanceRecordAction")
public class AttendRecordAction extends BaseActionSupport {
	
	@Resource(name = "attendRecordService")
	private IAttendRecordService attendRecordService;
	
	private SimpleDateFormat format=new SimpleDateFormat("yyy-MM-dd");
	
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-24 上午9:34:05
	 * @功能描述: 查询本周考勤记录
	 * @参数描述:
	 */
	public void getWeekAttendRecord(){
		
		ResultModel result = new ResultModel();
		
		Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		if(session_staff == null){
			result.setSuccess(false);
			result.setMsg("用户信息失效，请重新登录");
			printHttpServletResponse(new Gson().toJson(result));
			return;
		}
		
		//获取本周一和当前日期
		Calendar calendar = Calendar.getInstance();    
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		String firstDate = format.format(calendar.getTime()); 
		String lastDate = format.format(new Date());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_num", session_staff.getStaff_num());
		map.put("firstDate", firstDate);
		map.put("lastDate", lastDate);
		
		List<AttendRecordModel> attendRecordList = attendRecordService.getAttendRecord(map);
		if(attendRecordList == null ||attendRecordList.size()==0 ){
			result.setSuccess(false);
			result.setNoDate(true);
			result.setMsg("该账号没有考勤记录");
			printHttpServletResponse(new Gson().toJson(result));
		}
		result.setSuccess(true);
		result.setNoDate(false);
		result.setMsg("获取本周考勤信息成功");
		result.setAttendRecordList(attendRecordList);
		printHttpServletResponse(new Gson().toJson(result));
	}
	
	//新增农行外出记录
		public void addOutwork(){
			HttpServletRequest request = getHttpServletRequest();
			String beanObject = request.getParameter("data");
			Outwork work=GsonUtil.toBean(beanObject, Outwork.class);
			 UUID uuid = UUID.randomUUID();
			 work.setId(uuid.toString());
			 work.setBeginhour(work.getBegindate().substring(11, 16));
			 work.setEndhour(work.getEnddate().substring(11, 16));
			 work.setBegindate(work.getBegindate().substring(0, 10));
			 work.setEnddate(work.getEnddate().substring(0, 10));
			 work.setFlagname("可用");
			 Boolean out=attendRecordService.addOutworkInfo(work);
			 ResultModel result = new ResultModel();
			 if(out){
				 result.setSuccess(true);
				 result.setMsg("添加成功");
			 }else{
				 result.setSuccess(false);
				 result.setMsg("添加失败");
			 }
			 
			 printHttpServletResponse(new Gson().toJson(result));
		}
	
		//更新农行外出记录
				public void editOutwork(){
					HttpServletRequest request = getHttpServletRequest();
					String beanObject = request.getParameter("data");
					Outwork work=GsonUtil.toBean(beanObject, Outwork.class);
					 work.setBeginhour(work.getBegindate().substring(11, 16));
					 work.setEndhour(work.getEnddate().substring(11, 16));
					 work.setBegindate(work.getBegindate().substring(0, 10));
					 work.setEnddate(work.getEnddate().substring(0, 10));
					 work.setFlagname("可用");
					 Boolean out=attendRecordService.editOutworkInfo(work);
					 ResultModel result = new ResultModel();
					 if(out){
						 result.setSuccess(true);
						 result.setMsg("修改成功");
					 }else{
						 result.setSuccess(false);
						 result.setMsg("修改失败");
					 }
					 
					 printHttpServletResponse(new Gson().toJson(result));
				}
				
				//分发页面
				public String toMyupdatePage(){
					HttpServletRequest request = getHttpServletRequest();
					String id = request.getParameter("id");
					List<Outwork> work=attendRecordService.selectOutworkInfoById(id);
					for (Outwork outwork : work) {
						outwork.setBegindate(outwork.getBegindate()+" "+outwork.getBeginhour());
						outwork.setEnddate(outwork.getEnddate()+" "+outwork.getEndhour());
						request.setAttribute("outwork", outwork);
					}
					return "success";
				}
				
				
				
				
				//删除农行外出记录
				public void deleteOutwork(){
					HttpServletRequest request = getHttpServletRequest();
					String id = request.getParameter("id");
					
					 Boolean out=attendRecordService.deleteOutworkInfo(id);
					 ResultModel result = new ResultModel();
					 if(out){
						 result.setSuccess(true);
						 result.setMsg("删除成功");
					 }else{
						 result.setSuccess(false);
						 result.setMsg("删除失败");
					 }
					 
					 printHttpServletResponse(new Gson().toJson(result));
				}
					
	
}
