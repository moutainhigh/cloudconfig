package com.kuangchi.sdd.attendanceConsole.statistic.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("statisticAction")
public class StatisticAction extends BaseActionSupport {

	@Resource(name="statisticService")
	StatisticService statisticService;
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	//获取请假时间总值
	 public void getLeaveTimeTotal(){
		 HttpServletRequest request = getHttpServletRequest();
		 SimpleDateFormat formatter = new SimpleDateFormat( "yyyy/MM/dd HH:mm");
		 String startTime=request.getParameter("startTime");  // 2016/05/11 10:15
		 String endTime=request.getParameter("endTime");
		 String staffNum=request.getParameter("staffNum");
		 try {
			Date startTime1=formatter.parse(startTime);
			Date endTime1=formatter.parse(endTime);
			
			Double leaveTimeTotalHour=statisticService.getLeaveTimeTotal(startTime1, endTime1, staffNum);
			if(leaveTimeTotalHour==null){
				printHttpServletResponse(GsonUtil.toJson(null));
				return;
			}
			else{
			Integer days= (int) (leaveTimeTotalHour/8);//天数
			Double hours= leaveTimeTotalHour-days*8;  //小时数
			DecimalFormat df=new DecimalFormat("#.##"); //格式化小时，保留两位小数，最后位是舍入
			Double formatHours=Double.valueOf(df.format(hours));
			if(8==formatHours){
				days+=1;
				formatHours=(double) 0;
			}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("days", days);
				map.put("hours", formatHours);
				printHttpServletResponse(GsonUtil.toJson(map));
				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 }
	 
	 
	//获取请假时间总值  ,跨天的
		 public void getLeaveTimeTotal_new(){
			 HttpServletRequest request = getHttpServletRequest();
			 SimpleDateFormat formatter = new SimpleDateFormat( "yyyy/MM/dd HH:mm");
			 String startTime=request.getParameter("startTime");  // 2016/05/11 10:15
			 String endTime=request.getParameter("endTime");
			 String staffNum=request.getParameter("staffNum");
			 try {
				Date startTime1=formatter.parse(startTime);
				Date endTime1=formatter.parse(endTime);	
				Map<String, Object> leaveTimeList=statisticService.getLeaveTimeTotal_new(startTime1, endTime1, staffNum);
				printHttpServletResponse(GsonUtil.toJson(leaveTimeList));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		 }
}
