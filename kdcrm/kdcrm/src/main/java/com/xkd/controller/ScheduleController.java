package com.xkd.controller;

import com.xkd.model.*;
import com.xkd.service.ScheduleService;
import com.xkd.service.UserService;
import com.xkd.utils.DateUtils;
import com.xkd.utils.OperateCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/schedule")
public class ScheduleController  extends BaseController{

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private UserService userService;

	Logger log = Logger.getLogger(ScheduleController.class);
	/**
	 * 获取Schedule列表信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getScheduleList")
	public ResponseDbCenter getScheduleList(HttpServletRequest req,HttpServletResponse rsp){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		//String companyName = req.getParameter("companyName");
		try {
			String lessonName = req.getParameter("lessonName");
			String lessonType = req.getParameter("lessonType");
			String teacherId = req.getParameter("teacherId");//行程人
			String createUser = req.getParameter("createUserId");//创建人
			String province = req.getParameter("province");
			String state = req.getParameter("states");
			String priority = req.getParameter("priority");
			String city = req.getParameter("city");


			//指定开始时间，结束时间的行程
			String startDate = req.getParameter("startDate");//2017-07-07
			String endDate = req.getParameter("endDate");//2017-07-07
			//分页功能
			String pageNo1 = req.getParameter("currentPage");
			String pageSize1 = req.getParameter("pageSize");

			String userId = (String) req.getSession().getAttribute(req.getParameter("token"));

			int pageNo = 0;
			int pageSize = 60;
			if(StringUtils.isNotBlank(pageNo1)){
				pageNo = Integer.valueOf(pageNo1);
				if(StringUtils.isNotBlank(pageSize1)){
					pageSize = Integer.valueOf(pageSize1);
				}
				pageNo= (pageNo -1)*pageSize;
			}
			if(StringUtils.isNotBlank(lessonName)){
				lessonName="%"+lessonName+"%";
			}
			if(StringUtils.isNotBlank(startDate)){
				startDate= startDate+" 00:00";
			}
			if(StringUtils.isNotBlank(endDate)){
				endDate= endDate+" 23:59";//

			}
			List<String> teacherIds = null;
			if(StringUtils.isNotBlank(teacherId)){
				teacherIds = new ArrayList<>();
				for (String tid : teacherId.split(",")) {
					teacherIds.add(tid);
				}
				if(teacherIds.size()==0){
					teacherIds.add(teacherId);
				}
			}
			List<String> prioritys = null;
			if(StringUtils.isNotBlank(priority)){
				prioritys = new ArrayList<>();
				for (String pid : priority.split(",")) {
					prioritys.add(pid);
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("type", 1);
			map.put("state", state);
			map.put("lessonName", lessonName);
			map.put("lessonType", lessonType);
			map.put("teacherId", teacherIds);
			map.put("createUserId", createUser);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("province", province);
			map.put("city", city);
			map.put("pageNo", pageNo);
			map.put("pageSize", pageSize);
			map.put("userId", userId);
			map.put("priority", prioritys);
			Map<String, Object> user = userService.selectUserById(userId);//.get("pcCompanyId");
			map.put("pcCompanyId",user.get("roleId").equals("1") ? null:user.get("pcCompanyId"));
			//当前月1号-31号之间的行程
 			List<HashMap<String, Object>> list = scheduleService.selectScheduleList(map);

			Integer totalNum = scheduleService.getTotalSchedule(map);
			responseDbCenter.setResModel(list);
			responseDbCenter.setTotalRows(totalNum==null?"0":totalNum.intValue()+"");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 获取Schedule列表信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/scheduleWriteExcel")
	public ResponseDbCenter scheduleWriteExcel(HttpServletRequest req,HttpServletResponse rsp){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		//String companyName = req.getParameter("companyName");
		//try {
		String lessonName = req.getParameter("lessonName");
		String lessonType = req.getParameter("lessonType");
		String teacherId = req.getParameter("teacherId");//行程人
		String createUser = req.getParameter("createUserId");//创建人
		String province = req.getParameter("province");
		String state = req.getParameter("states");
		String token = req.getParameter("token");
		String priority = req.getParameter("priority");
		String city = req.getParameter("city");



		//指定开始时间，结束时间的行程
		String startDate = req.getParameter("startDate");//2017-07-07
		String endDate = req.getParameter("endDate");//2017-07-07

		if(StringUtils.isNotBlank(lessonName)){
			lessonName="%"+lessonName+"%";
		}
		if(StringUtils.isNotBlank(startDate)){
			startDate= startDate+" 00:00";
		}
		if(StringUtils.isNotBlank(endDate)){
			endDate= endDate+" 23:59";
		}
		//当前月1号-31号之间的行程
		List<String> teacherIds = null;
		if(StringUtils.isNotBlank(teacherId)){
			teacherIds = new ArrayList<>();
			for (String tid : teacherId.split(",")) {
				teacherIds.add(tid);
			}
			if(teacherIds.size()==0){
				teacherIds.add(teacherId);
			}
		}
		List<String> prioritys = null;
		if(StringUtils.isNotBlank(priority)){
			prioritys = new ArrayList<>();
			for (String pid : priority.split(",")) {
				prioritys.add(pid);
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("type", 1);
		map.put("state", state);
		map.put("lessonName", lessonName);
		map.put("lessonType", lessonType);
		map.put("teacherId", teacherIds);
		map.put("createUserId", createUser);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("province", province);
		map.put("city", city);
		map.put("priority", prioritys);
		String userId = req.getSession().getAttribute(token).toString();
		Map<String, Object> user = userService.selectUserById(userId);//.get("pcCompanyId");
		map.put("pcCompanyId",user.get("roleId").equals("1") ? null:user.get("pcCompanyId"));
		List<HashMap<String, Object>> list = scheduleService.selectScheduleList(map);
		responseDbCenter.setResModel(scheduleService.scheduleWriteExcel(list,req.getSession().getAttribute(token).toString()));
		/*} catch (Exception e) {
			System.out.println(e);

		}*/
		return responseDbCenter;
	}

	/**
	 * 插入Schedule记录
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insertSchedule")
	public ResponseDbCenter insertSchedule(HttpServletRequest req,HttpServletResponse rsp){
		ResponseDbCenter responseDbCenter = null;
		try {
			Schedule schedule = new Schedule();
			responseDbCenter = loadingAndCheckData(req,"insert",schedule);
			if(responseDbCenter == null){
				return ResponseConstants.MISSING_PARAMTER;
			}else if(responseDbCenter.getRepCode().equals("S0000")==false){
				return responseDbCenter;
			}
			schedule.setLoggerId(UUID.randomUUID().toString());
			scheduleService.insertScheduleMapper(schedule);
			schedule.setStateName("新增");
			scheduleService.saveScheduleLogger(schedule);



		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}
	/**
	 * 更新Schedule
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSchedule")
	public ResponseDbCenter updateSchedule(HttpServletRequest req,HttpServletResponse rsp){
		ResponseDbCenter responseDbCenter = null;
		try {
			//Test
			Schedule schedule = new Schedule();
			responseDbCenter = loadingAndCheckData(req,"update",schedule);
			if(responseDbCenter == null){
				return ResponseConstants.MISSING_PARAMTER;
			}else if(responseDbCenter.getRepCode().equals("S0000")==false){
				return responseDbCenter;
			}
			schedule.setLoggerId(UUID.randomUUID().toString());
			scheduleService.updateSchedule(schedule);
			schedule.setStateName("修改");
			scheduleService.saveScheduleLogger(schedule);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 更新Schedule前根据id查询
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectScheduleById")
	public ResponseDbCenter selectScheduleById(HttpServletRequest req,HttpServletResponse rsp){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			String userId = (String) req.getSession().getAttribute(req.getParameter("token"));
			String id = req.getParameter("id");
			if(StringUtils.isBlank(id)){
				return ResponseConstants.MISSING_PARAMTER;
			}else {
				Schedule schedule = scheduleService.selectScheduleById(id,userId);
				responseDbCenter.setResModel(schedule);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}
	/**
	 * 更新小程序Schedule前根据id查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getXcxScheduleById")
	public ResponseDbCenter getXcxScheduleById(HttpServletRequest req,String token){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {

			String userId = (String) req.getSession().getAttribute(token);
			String id = req.getParameter("id");
			if(StringUtils.isBlank(id)){
				return ResponseConstants.MISSING_PARAMTER;
			}else {
				Schedule schedule = scheduleService.selectScheduleById(id,userId);
				if(null != schedule && schedule.getRemove() == false){
					List<Operate> operateList= OperateCacheUtil.getUserOperates(token);
					if (operateList!=null&&operateList.size()>0) {
						for (int i = 0; i < operateList.size(); i++) {
							if (operateList.get(i).getUrl().equals("schedule/deleteScheduleById")) {
								schedule.setRemove(true);
								break;
							}
						}
					}
				}
				responseDbCenter.setResModel(schedule);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}


	/**
	 * 查询2017-07月整月的日历版行程
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getScheduleListOneMonth")
	public ResponseDbCenter getScheduleListOneMonth(HttpServletRequest req,String startTime,String endTime,String flag){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		try {
			String teacherId = req.getParameter("teacherId");

			String userId = (String) req.getSession().getAttribute(req.getParameter("token"));
			String createdBy = "";

			if(StringUtils.isBlank(endTime) || StringUtils.isBlank(startTime)){
				return ResponseConstants.MISSING_PARAMTER;
			}
			List<String> teacherIds = null;
			if(StringUtils.isNotBlank(teacherId)){
				teacherIds = new ArrayList<>();
				for (String tid : teacherId.split(",")) {
					teacherIds.add(tid);
				}
				if(teacherIds.size()==0){
					teacherIds.add(teacherId);
				}
			}else if(StringUtils.isNotBlank(flag) && flag.equals("my")){
				teacherIds = new ArrayList<>();
				teacherIds.add(userId);
			}else if(StringUtils.isNotBlank(flag) && flag.equals("createdBy")){
				createdBy = userId;
			}

			List<Schedule> list = scheduleService.selectScheduleByOneMonth(createdBy,  teacherIds, startTime, endTime,userId);
			responseDbCenter.setResModel(list);
			responseDbCenter.setTotalRows(list.size()+"");
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 查询2017-07月整月的日历版小程序行程
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getScheduleXcx")
	public ResponseDbCenter getScheduleXcx(HttpServletRequest req,String startTime,String endTime,String token,String flag){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		//http://localhost:8080/dbcenter/schedule/getScheduleXcx?token=555&startTime=2017-10-01&endTime=2017-10-31
		try {
			startTime = startTime.length() <12 ?startTime+" 00:00":startTime;
			endTime = endTime.length() <12 ?endTime+" 23:59":endTime;
			String teacherId = req.getParameter("teacherId");

			String userId = (String) req.getSession().getAttribute(token);


			if(StringUtils.isBlank(endTime) || StringUtils.isBlank(startTime)){
				return ResponseConstants.MISSING_PARAMTER;
			}
			Map<String, Object> map = new HashMap<>();
			List<String> teacherIds = null;
			if(StringUtils.isNotBlank(teacherId)){
				teacherIds =new ArrayList<>();
				for (String tid : teacherId.split(",")) {
					teacherIds.add(tid);
				}
				if(teacherIds.size()==0){
					teacherIds.add(teacherId);
				}
			}else if(StringUtils.isNotBlank(flag) && flag.equals("my")){
				teacherIds =new ArrayList<>();
				teacherIds.add(userId);
			}else if(StringUtils.isNotBlank(flag) && flag.equals("createdBy")){
				map.put("createdBy", userId);
			}

			map.put("teacherId", teacherIds);
			map.put("beginDate", startTime);
			map.put("endDate", endTime);
			map.put("userId", userId);
			Map<String, Object> user = userService.selectUserById(userId);//.get("pcCompanyId");
			String pcCompanyId = user.get("roleId").equals("1") ? null:user.get("pcCompanyId").toString();
			map.put("pcCompanyId",pcCompanyId);
			List<Schedule> list = scheduleService.getScheduleXcx(map,false);
			Map<String, String> obj = new HashMap<>();
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			for (Schedule schedule : list) {
				if(StringUtils.isBlank(obj.get(schedule.getStartDate())) ||(schedule.getMy() == true)){
					if(schedule.getDayNum() == 1){
						obj.put(schedule.getStartDate(), schedule.getMy()+"");
						System.out.println(schedule.getStartDate()+"      "+schedule.getMy());
					}else{
						for (int i = 0; i < schedule.getDayNum(); i++) {
							String startDate = simpleDateFormat.format(DateUtils.getAddDaysTime(simpleDateFormat.parse(schedule.getStartDate()), (i)));
							if(StringUtils.isBlank(obj.get(startDate)) ||(schedule.getMy() == true)){
									obj.put(startDate, schedule.getMy()+"");
								System.out.println(startDate+"      "+schedule.getMy());
							}
						}
					}
				}else if(StringUtils.isNotBlank(obj.get(schedule.getStartDate())) && schedule.getDayNum() > 1){
					for (int i = 0; i < schedule.getDayNum(); i++) {
						String startDate = simpleDateFormat.format(DateUtils.getAddDaysTime(simpleDateFormat.parse(schedule.getStartDate()), (i)));
						if(StringUtils.isBlank(obj.get(startDate)) ||(schedule.getMy() == true)){
							obj.put(startDate, schedule.getMy()+"");
							System.out.println(startDate+"      "+schedule.getMy());
						}
					}
				}

			}
			responseDbCenter.setResModel(obj);
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}
	/**
	 * 根据具体日期查询行程
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getScheduleXcxByDate")
	public ResponseDbCenter getScheduleXcxByDate(HttpServletRequest req,String date,String token,String flag){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		try {

			String userId = (String) req.getSession().getAttribute(token);

			String teacherId = req.getParameter("teacherId");
			if(StringUtils.isBlank(date)){
				return ResponseConstants.MISSING_PARAMTER;
			}
			List<String> teacherIds =new ArrayList<>();
			Map<String, Object> map = new HashMap<>();
			if(StringUtils.isNotBlank(teacherId)){

				for (String tid : teacherId.split(",")) {
					teacherIds.add(tid);
				}
				if(teacherIds.size()==0){
					teacherIds.add(teacherId);
				}
			}else if(StringUtils.isNotBlank(flag) &&flag.equals("my")){
				teacherIds.add(userId);
			}else if(StringUtils.isNotBlank(flag) &&flag.equals("createdBy")){
				map.put("createdBy",userId);
			}

			map.put("beginDate", date+" 00:00");
			map.put("endDate", date+" 23:59");
			map.put("teacherId", teacherIds);
			Map<String, Object> user = userService.selectUserById(userId);//.get("pcCompanyId");
			String pcCompanyId = user.get("roleId").equals("1") ? null:user.get("pcCompanyId").toString();
			map.put("pcCompanyId",pcCompanyId);
			List<Schedule> list = scheduleService.getScheduleXcx(map,true);
			responseDbCenter.setResModel(list);
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 获取队长以及同队人员信息
	 */
	@ResponseBody
	@RequestMapping("/getAllAdvisers")
	public ResponseDbCenter getAllAdvisers(HttpServletRequest req,String adviserName){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		List<Map<String, Object>> advisers = null;
		try {
			advisers = scheduleService.getTeacherAll(adviserName);
			responseDbCenter.setResModel(advisers);
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}
	/**
	 * 获取用户行程动态
	 */
	@ResponseBody
	@RequestMapping("/getXcxScheduleDynamic")
	public ResponseDbCenter getXcxScheduleDynamic(HttpServletRequest req,String token,String ttype){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		String userId = (String) req.getSession().getAttribute(token);
		try {
			//分页功能
			String pageNo1 = req.getParameter("currentPage");
			String pageSize1 = req.getParameter("pageSize");


			int pageNo = 0;
			int pageSize = 60;
			if(StringUtils.isNotBlank(pageNo1)){
				pageNo = Integer.valueOf(pageNo1);
				if(StringUtils.isNotBlank(pageSize1)){
					pageSize = Integer.valueOf(pageSize1);
				}
				pageNo= (pageNo -1)*pageSize;
			}
			responseDbCenter.setResModel(scheduleService.getXcxScheduleDynamic(userId,ttype,pageNo,pageSize));
			responseDbCenter.setTotalRows(scheduleService.getXcxScheduleDynamicTotal(userId,ttype)+"");
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 获取用户实时动态
	 */
	@ResponseBody
	@RequestMapping("/getUserRealTimeDynamic")
	public ResponseDbCenter getUserRealTimeDynamic(HttpServletRequest req,String token){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		String userId = (String) req.getSession().getAttribute(token);
		try {
			//分页功能
			String pageNo1 = req.getParameter("currentPage");
			String pageSize1 = req.getParameter("pageSize");


			int pageNo = 0;
			int pageSize = 60;
			if(StringUtils.isNotBlank(pageNo1)){
				pageNo = Integer.valueOf(pageNo1);
				if(StringUtils.isNotBlank(pageSize1)){
					pageSize = Integer.valueOf(pageSize1);
				}
				pageNo= (pageNo -1)*pageSize;
			}
			responseDbCenter.setResModel(scheduleService.getXcxScheduleDynamic(userId,"showStatus",pageNo,pageSize));
			responseDbCenter.setTotalRows(scheduleService.getXcxScheduleDynamicTotal(userId,"showStatus")+"");
			responseDbCenter.setResExtra(scheduleService.getXcxScheduleDynamicTotal(userId,"readStatus"));
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 新增or修改实操方法oPType: insert,update
	 * @param req
	 * @param oPType
	 * @return
	 * @throws ParseException
	 */
	private ResponseDbCenter loadingAndCheckData(HttpServletRequest req ,String oPType,Schedule schedule) throws ParseException{



		String id = req.getParameter("id");//行程id
		//Pattern pattern = Pattern.compile("[0-9]*");

		String userid = req.getSession().getAttribute(req.getParameter("token")).toString();
		String lessonName = req.getParameter("lessonName");
		String lessonType = req.getParameter("lessonType");
		String colleagues = req.getParameter("colleagues");//逗号隔开   1,2,3,4
		String province = req.getParameter("province");
		String city = req.getParameter("city");
		String address = req.getParameter("address");
		String startDate = req.getParameter("startDate");//2017-01-01
		String endDate = req.getParameter("endDate");//2017-01-01
		//String dayNum = req.getParameter("dayNum");//number
		String scheduleDetail = req.getParameter("scheduleDetail");//详情说明
		String priority = req.getParameter("priority");//优先级
		String remind = req.getParameter("remind");//提醒时间
		String fullDay = req.getParameter("fullDay");

		String checkUser = req.getParameter("checkUser");//是否校验冲突


		//行程详情
		if(StringUtils.isNotBlank(lessonName)
				&& StringUtils.isNotBlank(province)&& StringUtils.isNotBlank(city)
				&& StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)
				&& StringUtils.isNotBlank(endDate)
				){


			schedule.setRemind(remind);
			schedule.setPriority(priority);
			schedule.setLessonName(lessonName);
			schedule.setProvince(province);
			schedule.setCity(city);
			schedule.setAddress(address);
			schedule.setLessonType(lessonType);
			schedule.setCompanyId(req.getParameter("companyId"));
			schedule.setScheduleDetail(scheduleDetail);
			schedule.setFullDay(StringUtils.isNotBlank(fullDay) ? Boolean.valueOf(fullDay):false);
			schedule.setTeacherName(req.getParameter("teacherName"));
			startDate = startDate.length()>11 ? startDate :startDate+" 00:00";
			endDate = endDate.length()>11 ? endDate :endDate+" 23:59";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			int dayNumInt = DateUtils.daysBetween(sdf.parse(startDate), sdf.parse(endDate));
			schedule.setDayNum(dayNumInt+1);

			schedule.setStartDate(startDate);
			schedule.setEndDate(endDate);
			if(oPType.equals("insert")){
				schedule.setCreatedBy(userid);
				schedule.setUpdatedBy(userid);
				schedule.setId(UUID.randomUUID().toString());
				//UserId 创建人
			}else if(oPType.equals("update")){
				if(StringUtils.isBlank(id)){
					return ResponseConstants.MISSING_PARAMTER;
				}
				schedule.setId(id);
				schedule.setUpdatedBy(userid);
			}
			schedule.setUpdateDate(DateUtils.currtime());
			List<String> users = new ArrayList<>();
			if(StringUtils.isNotBlank(colleagues)){
				String uids[] = colleagues.split(",");
				for (String string : uids) {
					if(StringUtils.isNotBlank(string) ){
						users.add(string);
					}
				}
				schedule.setListColleague(users);
			}
			if(StringUtils.isNotBlank(checkUser)&&(checkUser.equals("true"))){
				//判断是否是已经结束的行程
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Calendar Time1 = Calendar.getInstance();
					Calendar Time2 = Calendar.getInstance();
					Time1.setTime(df.parse(endDate));
					Time2.setTime(df.parse(DateUtils.currtime()));
					if(Time2.compareTo(Time1) < 0){
						String status = scheduleService.checkUserCongTu(schedule);
						if(status!= null){
							ResponseDbCenter responseDbCenter = new ResponseDbCenter();
							responseDbCenter.setRepCode("CRM1057");
							responseDbCenter.setResModel(status);
							responseDbCenter.setRepNote("行程冲突");
							return responseDbCenter;
						}
					}

				} catch (Exception e) {
					return ResponseConstants.ILLEGAL_PARAM;
				}
			}
		}else{
			return ResponseConstants.MISSING_PARAMTER;
		}



		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}

	/**
	 * 查询行程日志
	 */
	@ResponseBody
	@RequestMapping("/getScheduleLogger")
	public ResponseDbCenter getScheduleLogger(String scheduleId,String currentPage,String pageSize){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		//分页功能

		try {
			int  pageSizeInt = 0;
			int  currentPageInt = 60;
			if(StringUtils.isNotBlank(currentPage) && StringUtils.isNotBlank(pageSize)){
				pageSizeInt = Integer.parseInt(pageSize);
				currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
			}
			responseDbCenter.setResModel(scheduleService.getScheduleLogger(scheduleId,pageSizeInt,currentPageInt));
			responseDbCenter.setTotalRows(scheduleService.getScheduleLoggerTotal(scheduleId)+"");
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 小程序日志已阅
	 */
	@ResponseBody
	@RequestMapping("/getXcxReadSchedule")
	public ResponseDbCenter getXcxReadSchedule(HttpServletRequest req,String token,String id){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			scheduleService.setUserActionRead(req.getSession().getAttribute(token)+"",id);
			responseDbCenter.setResModel("SUCCESS");
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}
	/**
	 * 小程序查询行程日志
	 */
	@ResponseBody
	@RequestMapping("/getXcxScheduleLoggerById")
	public ResponseDbCenter getXcxScheduleLoggerById(String id){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		try {

			responseDbCenter.setResModel(scheduleService.getXcxScheduleLoggerById(id));
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}
	/**
	 * 模糊查询企业家
	 */
	@ResponseBody
	@RequestMapping("/getCompanyUserLike")
	public ResponseDbCenter getCompanyUserLike(HttpServletRequest req,String uname,String token){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			String createdBy = req.getSession().getAttribute(token).toString();
			Map<String, Object> user = userService.selectUserById(createdBy);//.get("pcCompanyId");
			String pcCompanyId = user.get("roleId").equals("1") ? null:user.get("pcCompanyId").toString();
			responseDbCenter.setResModel(scheduleService.getCompanyUserLike(uname,pcCompanyId));
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 *获取我的最近行程人
	 */
	@ResponseBody
	@RequestMapping("/getMyHistoryUser")
	public ResponseDbCenter getMyHistoryUser(HttpServletRequest req,String token){
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			responseDbCenter.setResModel(scheduleService.getMyHistoryUser(req.getSession().getAttribute(token).toString()));
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 删除行程
	 */
	@ResponseBody
	@RequestMapping("/deleteScheduleById")
	public ResponseDbCenter deleteScheduleById(HttpServletRequest req,String scheduleId,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			Schedule schedule = scheduleService.getScheduleById(scheduleId);
			if (schedule == null) {
				return ResponseConstants.FUNC_MODULE_DELETEERROR;
			}else if(schedule.getStatus().equals("2")){
				return ResponseConstants.NO_DELETE;
			}
			String userid = req.getSession().getAttribute(token).toString();

			scheduleService.deleteScheduleById(schedule, userid);

			responseDbCenter.setResModel("SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}


	public Map<String,Object>  getTongJi(String ttype,String userIds,String endDate,String stateDate,String type,String createdBy) {
		Map<String,Object> map = new HashMap<>();
		map.put("type",type);
		if(StringUtils.isNotBlank(userIds)){
			List<String> userList = new ArrayList<>();
			if(userIds.contains(",")){
				for (String id:userIds.split(",")) {
					userList.add(id);
				}
			}else{
				userList.add(userIds);
			}
			map.put("userList",userList);
		}
		map.put("ttype",ttype);
		if(StringUtils.isBlank(endDate) || StringUtils.isBlank(stateDate)){
			//SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			//endDate = sf.format(new Date());
			//map.put("stateDate",sf.format(DateUtils.getAddDaysTime(new Date(),-30)));
			//map.put("endDate",endDate+" 23:59");


			map.put("stateDate",null);
			map.put("endDate",null);
			map.put("titleDate",null);
		}else{
			map.put("stateDate",stateDate);
			map.put("endDate",endDate+" 23:59");
			map.put("titleDate",stateDate+" 至 "+endDate);
		}
		Map<String, Object> user = userService.selectUserById(createdBy);//.get("pcCompanyId");
		map.put("pcCompanyId",user.get("roleId").equals("1") ? null:user.get("pcCompanyId"));
		return map;
	}

	/**
	 * 统计行程类型
	 */
	@ResponseBody
	@RequestMapping("/getScheduleTypeList")
	public ResponseDbCenter getScheduleTypeList(HttpServletRequest req,String ttype,String userIds,String endDate,String startDate,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			String userId = req.getSession().getAttribute(token).toString();
			responseDbCenter.setResModel(scheduleService.getScheduleTypeList(getTongJi(ttype,userIds,endDate,startDate,"lessonType",userId)));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}
	/**
	 * 统计行程地区
	 */
	@ResponseBody
	@RequestMapping("/getScheduleProvinceList")
	public ResponseDbCenter getScheduleProvinceList(HttpServletRequest req,String ttype,String userIds,String endDate,String stateDate,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			String userId = req.getSession().getAttribute(token).toString();
			responseDbCenter.setResModel(scheduleService.getScheduleTypeList(getTongJi(ttype,userIds,endDate,stateDate,"province",userId)));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 统计客户类型
	 */
	@ResponseBody
	@RequestMapping("/getScheduleUserType")
	public ResponseDbCenter getScheduleUserType(HttpServletRequest req,String ttype,String userIds,String endDate,String stateDate,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			String userId = req.getSession().getAttribute(token).toString();
			responseDbCenter.setResModel(scheduleService.getScheduleComp(getTongJi(ttype,userIds,endDate,stateDate,"userType",userId)));

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 统计客户等级
	 */
	@ResponseBody
	@RequestMapping("/getScheduleUserLevel")
	public ResponseDbCenter getScheduleUserLevel(HttpServletRequest req,String ttype,String userIds,String endDate,String stateDate,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			String userId = req.getSession().getAttribute(token).toString();
			responseDbCenter.setResModel(scheduleService.getScheduleComp(getTongJi(ttype,userIds,endDate,stateDate,"userLevel",userId)));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 统计行程类型导出
	 */
	@ResponseBody
	@RequestMapping("/writeExcelScheduleTypeList")
	public ResponseDbCenter writeExcelScheduleTypeList(HttpServletRequest req,String ttype,String userIds,String endDate,String stateDate,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {

			String value = StringUtils.isNotBlank(ttype)&&ttype.equals("ci")?"次":"天";
			String userId = req.getSession().getAttribute(token).toString();
			Map<String,Object> map = getTongJi(ttype,userIds,endDate,stateDate,"lessonType",userId);
			List<Map<String,Object>> list = scheduleService.getWriteExcelScheduleTypeList(map);
			List<Map<String, Object>> listTitle = scheduleService.getScheduleTitle(map);
			Map<String,Integer> title = new HashMap<>();
			title.put("人员姓名",0);
			title.put("行程",1);
			int rows = 2;
			for (Map<String, Object> scTitle:listTitle) {
				title.put(scTitle.get("title").toString(),rows++);
			}
			List<HashMap<String,Object>> tongJi = scheduleService.getWriteExcelScheduleTongJi(map);
			responseDbCenter.setResModel(scheduleService.writeExcelScheduleTypeList(tongJi,list,title,"按行程类型（"+value+"）统计导出模板", value,map));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 统计行程地域导出
	 */
	@ResponseBody
	@RequestMapping("/writeExcelScheduleProvinceList")
	public ResponseDbCenter writeExcelScheduleProvinceList(HttpServletRequest req,String ttype,String userIds,String endDate,String stateDate,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {

			String value = StringUtils.isNotBlank(ttype)&&ttype.equals("ci")?"次":"天";
			String userId = req.getSession().getAttribute(token).toString();
			Map<String,Object> map = getTongJi(ttype,userIds,endDate,stateDate,"province",userId);
			List<Map<String,Object>> list = scheduleService.getWriteExcelScheduleTypeList(map);
			List<Map<String, Object>> listTitle = scheduleService.getScheduleTitle(map);
			Map<String,Integer> title = new HashMap<>();
			title.put("人员姓名",0);
			title.put("行程",1);
			int rows = 2;
			for (Map<String, Object> scTitle:listTitle) {
				title.put(scTitle.get("title").toString(),rows++);
			}
			List<HashMap<String,Object>> tongJi = scheduleService.getWriteExcelScheduleTongJi(map);
			responseDbCenter.setResModel(scheduleService.writeExcelScheduleTypeList(tongJi,list,title,"按行程地域（"+value+"）统计导出模板", value,map));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 统计客户类型导出
	 */
	@ResponseBody
	@RequestMapping("/writeExcelScheduleUserType")
	public ResponseDbCenter writeExcelScheduleUserType(HttpServletRequest req,String ttype,String userIds,String endDate,String stateDate,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {

			String value = StringUtils.isNotBlank(ttype)&&ttype.equals("ci")?"次":"天";
			String userId = req.getSession().getAttribute(token).toString();
			Map<String,Object> map = getTongJi(ttype,userIds,endDate,stateDate,"userType",userId);
			List<Map<String,Object>> list = scheduleService.getWriteExcelScheduleComp(map);
			List<Map<String, Object>> listTitle = scheduleService.getScheduleCompTitle(map);
			Map<String,Integer> title = new HashMap<>();
			title.put("人员姓名",0);
			title.put("行程",1);
			int rows = 2;
			for (Map<String, Object> scTitle:listTitle) {
				title.put(scTitle.get("title").toString(),rows++);
			}
			List<HashMap<String,Object>> tongJi = scheduleService.getWriteExcelCompTongJi(map);
			responseDbCenter.setResModel(scheduleService.writeExcelScheduleTypeList(tongJi,list,title,"按客户类型（"+value+"）统计导出模板", value,map));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 统计客户级别导出
	 */
	@ResponseBody
	@RequestMapping("/writeExcelScheduleUserLevel")
	public ResponseDbCenter writeExcelScheduleUserLevel(HttpServletRequest req,String ttype,String userIds,String endDate,String stateDate,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {

			String value = StringUtils.isNotBlank(ttype)&&ttype.equals("ci")?"次":"天";
			String userId = req.getSession().getAttribute(token).toString();
			Map<String,Object> map = getTongJi(ttype,userIds,endDate,stateDate,"userLevel",userId);
			List<Map<String,Object>> list = scheduleService.getWriteExcelScheduleComp(map);
			List<Map<String, Object>> listTitle = scheduleService.getScheduleCompTitle(map);
			Map<String,Integer> title = new HashMap<>();
			title.put("人员姓名",0);
			title.put("行程",1);
			int rows = 2;
			for (Map<String, Object> scTitle:listTitle) {
				title.put(scTitle.get("title").toString(),rows++);
			}
			List<HashMap<String,Object>> tongJi = scheduleService.getWriteExcelCompTongJi(map);
			responseDbCenter.setResModel(scheduleService.writeExcelScheduleTypeList(tongJi,list,title,"按客户等级（"+value+"）统计导出模板", value,map));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 保存系统发版提示
	 */
	@ResponseBody
	@RequestMapping("/savePushBanben")
	public ResponseDbCenter savePushBanben(HttpServletRequest req,String content,String title) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			System.out.println(content+" "+title);
			if(StringUtils.isBlank(content) && StringUtils.isBlank(title)){
				return ResponseConstants.MISSING_PARAMTER;
			}
			UserAction userAction = new UserAction();
			userAction.setActionType("3");
			userAction.setCreateDate(DateUtils.currtime());
			userAction.setReadStatus("0");
			userAction.setActionId(UUID.randomUUID().toString());
			List<Map<String, String>> userList = scheduleService.getPCUserIds();
			userAction.setActionTitle(title);
			userAction.setActionContent(content);
			for (Map<String, String> user:userList) {
				String userId = user.get("id");
				userAction.setActionUserId(userId);
				userAction.setCreatedBy(userId);
				scheduleService.saveUserAction(userAction);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 改变消息为不在提示
	 */
	@ResponseBody
	@RequestMapping("/changeNoPrompt")
	public ResponseDbCenter changeNoPrompt(HttpServletRequest req,String token) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			scheduleService.setUserActionNoPrompt(req.getSession().getAttribute(token)+"");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}

	/**
	 * 根据消息Id查询消息
	 */
	@ResponseBody
	@RequestMapping("/getUserActionById")
	public ResponseDbCenter getUserActionById(HttpServletRequest req,String id) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			responseDbCenter.setResModel(scheduleService.getUserActionById(id));

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseDbCenter;
	}
}