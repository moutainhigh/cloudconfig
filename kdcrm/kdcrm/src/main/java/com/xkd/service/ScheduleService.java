package com.xkd.service;

import com.xkd.mapper.DC_UserMapper;
import com.xkd.mapper.ScheduleMapper;
import com.xkd.mapper.ScheduleUserMapper;
import com.xkd.model.DC_User;
import com.xkd.model.Schedule;
import com.xkd.model.ScheduleUser;
import com.xkd.model.UserAction;
import com.xkd.utils.DateUtils;
import com.xkd.utils.MailUtils;
import com.xkd.utils.PropertiesUtil;
import com.xkd.utils.SmsApi;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleMapper scheduleMapper;

	@Autowired
	private ScheduleUserMapper scheduleColleagueMapper;

	@Autowired
	private DC_UserMapper userMapper;

	@Autowired
	private UserService userService;
	/**
	 * 新增Schedule
	 * @param schedule
	 * @return
	 */
	public Integer insertScheduleMapper(Schedule schedule){
		schedule.setPcCompanyId(userService.selectUserById(schedule.getUpdatedBy()).get("pcCompanyId").toString());
		int num = scheduleMapper.insertSchedule(schedule);
		UserAction userAction = new UserAction();
		userAction.setActionId(schedule.getLoggerId());
		userAction.setActionType("1");
		userAction.setCreateDate(DateUtils.currtime());
		userAction.setCreatedBy(schedule.getUpdatedBy());
		userAction.setActionTitle(" 创建了 行程 添加 你 为相关人");
		userAction.setActionContent(schedule.getLessonName());
		userAction.setStartDate(schedule.getStartDate());
		userAction.setEndDate(schedule.getEndDate());
		String toMobile =
				"【蝌蚪智慧】您有一个新的行程通知；" +
						"行程类型："+schedule.getLessonType()+"；" +
						"行程名称："+schedule.getLessonName()+"；" +
						"行程时间："+schedule.getStartDate()+"至"+schedule.getEndDate()+"；" +
						"行程地点："+schedule.getProvince()+"-"+schedule.getCity()+"；" +
						"行程人员："+schedule.getTeacherName()+"；" +
						(StringUtils.isBlank(schedule.getScheduleDetail())?"备注：无":(("备注："+schedule.getScheduleDetail())+"。"));
		DC_User loginUser = userMapper.getUserById(schedule.getUpdatedBy());
		String toEamil ="，您好！</br>" +
				"您有一个新的行程通知<br>" +
				"行程类型："+schedule.getLessonType()+"<br>" +
				"行程名称："+schedule.getLessonName()+"<br>" +
				"行程时间："+schedule.getStartDate()+"至"+schedule.getEndDate()+"<br>" +
				"行程地点："+schedule.getProvince()+"-"+schedule.getCity()+"<br>" +
				"行程人员："+schedule.getTeacherName()+"<br>" +
				(StringUtils.isBlank(schedule.getScheduleDetail())?"备注：无":(("备注："+schedule.getScheduleDetail())+"。"))+"<br>"+
				"创建人："+loginUser.getUname()+"<br>" +
				"点击以下链接登录可查看详情：<br>" +
				"<a href='"+PropertiesUtil.USER_LOGIN_HREF+"'>"+PropertiesUtil.USER_LOGIN_HREF+"(CRM链接地址)</a><br>";

		if(schedule.getListColleague().size()>0){
			Map<String,Object> historyUserInfo = new HashMap<>();
			historyUserInfo.put("myUserId",schedule.getUpdatedBy());
			for (int i = 0; i < schedule.getListColleague().size(); i++) {
				String id = schedule.getListColleague().get(i);
				historyUserInfo.put("userId",id);
				historyUserInfo.put("time",i);
				scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
				if(i == schedule.getListColleague().size() -1){
					historyUserInfo.put("userId",schedule.getUpdatedBy());
					historyUserInfo.put("time",i+1);
					scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
				}
				ScheduleUser s = new ScheduleUser();
				s.setUserId(id);
				s.setScheduleId(schedule.getId());
				scheduleColleagueMapper.insertScheduleUser(s);

				userAction.setActionUserId(id);
				scheduleColleagueMapper.saveUserAction(userAction);

				DC_User user = userMapper.getUserById(id);
				if(!("0").equals(schedule.getRemind()) && null != user && StringUtils.isNotBlank(user.getMobile())){
					System.out.println(user.getMobile());
					System.out.println(toMobile);
					SmsApi.sendSms(user.getMobile(),toMobile);
				}
				if(!("0").equals(schedule.getRemind()) && null != user && StringUtils.isNotBlank(user.getEmail())){

					List<Schedule> scheduleList = scheduleMapper.getUserSchedule(id);
					String huizong = "";
					if(scheduleList.size()>0){
						huizong = "<br><div style='font-weight: bold;margin-bottom:8px;'>我的行程计划</div>";
					}
					for (Schedule sl : scheduleList) {
						huizong+= sl.getStartDate()+" 至 "+sl.getEndDate()+"<br>"+
								sl.getLessonName()+"<br>" +
								sl.getProvince()+" - "+sl.getCity()+"<br>"+
								"<div style='border-bottom:1px solid #ccc;height:1px;width:100%;margin-bottom:8px;margin-top:5px;'></div>";
					}
					System.out.println(user.getEmail());
					System.out.println(user.getUname()+toEamil+huizong);
					MailUtils.send(user.getEmail(),"行程通知",user.getUname()+toEamil+huizong);
				}
			}
			List<Map<String,String>> historyUser = scheduleColleagueMapper.getMyHistoryUser(schedule.getUpdatedBy());
			if(historyUser.size() > 20){
				for (int i = 20; i < historyUser.size(); i++) {
					scheduleColleagueMapper.deleteHistoryUser(schedule.getUpdatedBy(),historyUser.get(i).get("userId"));
				}
			}

		}
		return num;
	}
	/**
	 * 查询符合条件的行程总条数
	 * @return
	 */
	public Integer getTotalSchedule(Map<String, Object> map){
		Integer num = scheduleMapper.getTotalSchedule(map);

		return num;
	}
	/**
	 * 更新Schedule
	 * @param schedule
	 * @return
	 */
	public Integer updateSchedule(Schedule schedule){
		if(null != schedule && schedule.getId() != null){
			int num = scheduleMapper.updateScheduleById(schedule);

			List<HashMap<String, Object>> scList = scheduleMapper.getScheduleColleagueListBySid(schedule.getId());
			List<Object> scUser = new ArrayList<>();
			for (HashMap<String, Object> sc:scList) {
				scUser.add(sc.get("id"));
			}
			scheduleColleagueMapper.deleteScheduleUserByScheduleId(schedule.getId());
			UserAction userAction = new UserAction();
			userAction.setActionId(schedule.getLoggerId());
			userAction.setActionType("1");
			userAction.setCreateDate(DateUtils.currtime());
			userAction.setCreatedBy(schedule.getUpdatedBy());
			userAction.setActionTitle(" 修改了行程内容，请关注");
			userAction.setActionContent(schedule.getLessonName());
			userAction.setStartDate(schedule.getStartDate());
			userAction.setEndDate(schedule.getEndDate());

			String toMobile =
							"行程类型："+schedule.getLessonType()+"；" +
							"行程名称："+schedule.getLessonName()+"；" +
							"行程时间："+schedule.getStartDate()+"至"+schedule.getEndDate()+"；" +
							"行程地点："+schedule.getProvince()+"-"+schedule.getCity()+"；" +
							"行程人员："+schedule.getTeacherName()+"；" +
									(StringUtils.isBlank(schedule.getScheduleDetail())?"备注：无":(("备注："+schedule.getScheduleDetail())+"。"));
			DC_User loginUser = userMapper.getUserById(schedule.getUpdatedBy());
			String toEamil =
					"行程类型："+schedule.getLessonType()+"<br>" +
					"行程名称："+schedule.getLessonName()+"<br>" +
					"行程时间："+schedule.getStartDate()+"至"+schedule.getEndDate()+"<br>" +
					"行程地点："+schedule.getProvince()+"-"+schedule.getCity()+"<br>" +
					"行程人员："+schedule.getTeacherName()+"<br>" +
							(StringUtils.isBlank(schedule.getScheduleDetail())?"备注：无":(("备注："+schedule.getScheduleDetail())+"。"))+"<br>"+
					"创建人："+loginUser.getUname()+"<br>" +
					"点击以下链接登录可查看详情：<br>" +
					"<a href='"+PropertiesUtil.USER_LOGIN_HREF+"'>"+PropertiesUtil.USER_LOGIN_HREF+"(CRM链接地址)</a><br>";
			if(schedule.getListColleague().size()>0){

				Map<String,Object> historyUserInfo = new HashMap<>();
				historyUserInfo.put("myUserId",schedule.getUpdatedBy());
				for (int i = 0; i < schedule.getListColleague().size(); i++) {
					String id = schedule.getListColleague().get(i);
					historyUserInfo.put("userId",id);
					historyUserInfo.put("time",i);
					scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
					if(i == schedule.getListColleague().size() -1){
						historyUserInfo.put("userId",schedule.getUpdatedBy());
						historyUserInfo.put("time",i+1);
						scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
					}

					ScheduleUser s = new ScheduleUser();
					s.setUserId(id);
					s.setScheduleId(schedule.getId());
					scheduleColleagueMapper.insertScheduleUser(s);
					userAction.setActionUserId(id);
					scheduleColleagueMapper.saveUserAction(userAction);
					scUser.remove(id);

					DC_User user = userMapper.getUserById(id);
					if(!("0").equals(schedule.getRemind()) && null != user && StringUtils.isNotBlank(user.getMobile())){

						System.out.println(user.getMobile());
						System.out.println("【蝌蚪智慧】您有一个行程变更的通知" +toMobile);
						SmsApi.sendSms(user.getMobile(),"【蝌蚪智慧】您有一个行程变更的通知；" +toMobile);
					}
					if(!("0").equals(schedule.getRemind()) && null != user && StringUtils.isNotBlank(user.getEmail())){
						String toEamilTitle = user.getUname()+"，您好！</br>" +
								"您有一个行程变更的通知<br>";

						List<Schedule> scheduleList = scheduleMapper.getUserSchedule(id);
						String huizong = "";
						if(scheduleList.size()>0){
							huizong = "<br><div style='font-weight: bold;margin-bottom:8px;'>我的行程计划</div>";
						}
						for (Schedule sl : scheduleList) {
							huizong+= sl.getStartDate()+" 至 "+sl.getEndDate()+"<br>"+
									sl.getLessonName()+"<br>" +
									sl.getProvince()+" - "+sl.getCity()+"<br>"+
									"<div style='border-bottom:1px solid #ccc;height:1px;width:100%;margin-bottom:8px;margin-top:5px;'></div>";
						}
						System.out.println(user.getEmail());
						System.out.println(user.getUname()+toEamil+huizong);
						MailUtils.send(user.getEmail(),"行程通知",toEamilTitle+toEamil+huizong);
					}
				}
			}
			if(scList.size() > 0){
				for (int i = 0; i < scUser.size(); i++) {
					userAction.setActionTitle(" 修改了 行程 取消 你 为相关人");
					userAction.setActionUserId(scUser.get(i).toString());
					scheduleColleagueMapper.saveUserAction(userAction);

					//邮件短信通知
					DC_User user = userMapper.getUserById(scUser.get(i).toString());
					if(!("0").equals(schedule.getRemind()) && null != user && StringUtils.isNotBlank(user.getMobile())){

						SmsApi.sendSms(user.getMobile(),"【蝌蚪智慧】您有一个行程变更（取消你为行程人员）的通知；" +toMobile);
					}
					if(!("0").equals(schedule.getRemind()) && null != user && StringUtils.isNotBlank(user.getEmail())){
						String toEamilTitle = user.getUname()+"，您好！</br>" +
								"您有一个行程变更（取消你为行程人员）的通知<br>";

						List<Schedule> scheduleList = scheduleMapper.getUserSchedule(scUser.get(i)+"");
						String huizong = "";
						if(scheduleList.size()>0){
							huizong = "<br><div style='font-weight: bold;margin-bottom:8px;'>我的行程计划</div>";
						}
						for (Schedule sl : scheduleList) {
							huizong+= sl.getStartDate()+" 至 "+sl.getEndDate()+"<br>"+
									sl.getLessonName()+"<br>" +
									sl.getProvince()+" - "+sl.getCity()+"<br>"+
									"<div style='border-bottom:1px solid #ccc;height:1px;width:100%;margin-bottom:8px;margin-top:5px;'></div>";
						}
						System.out.println(user.getEmail());
						System.out.println(toEamilTitle+toEamil+huizong);
						MailUtils.send(user.getEmail(),"行程通知",toEamilTitle+toEamil+huizong);
					}
				}
			}



			List<Map<String,String>> historyUser = scheduleColleagueMapper.getMyHistoryUser(schedule.getUpdatedBy());
			if(historyUser.size() > 10){
				for (int i = 10; i < historyUser.size(); i++) {
					scheduleColleagueMapper.deleteHistoryUser(schedule.getUpdatedBy(),historyUser.get(i).get("userId"));
				}
			}
			return num;
		}else{
			return -1;
		}

	}
	/**
	 * 根据ScheduleId 查出详情
	 * @param id
	 * @return
	 */
	public Schedule selectScheduleById(String id,String userId){

		Schedule s =scheduleMapper.selectScheduleById(id,userId);
		List<HashMap<String, Object>> sc = scheduleMapper.getScheduleColleagueListBySid(id);
		s.setListcolleagueIdAndName(sc);
		return s;
	}
	/**
	 * 查询一个月的日程列表
	* @return
	 */
	public List<Schedule> selectScheduleByOneMonth(String createdBy,List<String> teacherId,String startTime,String endTime,String userId){
		Map<String, Object> user = userService.selectUserById(userId);//.get("pcCompanyId");
		String pcCompanyId = user.get("roleId").equals("1") ? null:user.get("pcCompanyId").toString();
		List<Schedule> list = scheduleMapper.selectScheduleByOneMonth(createdBy,  teacherId, startTime,endTime,userId,pcCompanyId);
		for (Schedule schedule : list) {
			List<HashMap<String, Object>> sc = scheduleMapper.getScheduleColleagueListBySid(schedule.getId());
			String colleagues = "";
			if(null != sc && sc.size() > 0){
				for (HashMap<String, Object> hashMap : sc) {
					colleagues+=null != hashMap ?(hashMap.get("name")+"、"):"";
				}
				schedule.setColleagues(colleagues.length() > 1 ? colleagues.substring(0, colleagues.length()-1):"");
			}else{
				schedule.setColleagues("");
			}
		}
		return list;
	}

	public String checkUserCongTu(Schedule schedule) {

		List<String> list_z = schedule.getListColleague();
		List<String> list = null;
		String teacherId = schedule.getTeacherId();

		String scheduleId = schedule.getId();
		if(null != list_z && list_z.size() > 0){//判断讲师跟同行人在同行人的分组里有没有冲突
			list = list_z;
		}else {
			list = new ArrayList<>();
		}
		list.add(teacherId);

		List<HashMap<String, Object>> map = scheduleMapper.checkUserTongXR(list,schedule.getStartDate(),schedule.getEndDate(),scheduleId);
		if(map == null){
			return null;
		}else{
			String name= "";
			for (HashMap<String, Object> hashMap : map) {
				Object id = hashMap.get("id");
				if(null != id && !((id+"").equals(scheduleId+""))){
					name += hashMap.get("name")+",";
				}
			}
			if(StringUtils.isNotBlank(name)){
				return name.toString().substring(0, name.toString().length()-1)+",行程冲突";
			}

		}
		list.remove(teacherId);
		return null;

	}

	//导出excel
	public String scheduleWriteExcel(List<HashMap<String, Object>> list,String userId) {

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("行程管理");
		XSSFCellStyle style_title = wb.createCellStyle();
		Font fontHeader=wb.createFont();
		fontHeader.setFontHeightInPoints((short)12);
		fontHeader.setBold(true);
		//字体名称
		fontHeader.setFontName("宋体");
		style_title.setFont(fontHeader);
		style_title.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);


		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("index","序号");
		map.put("stateName","行程状态");
		map.put("lessonName","行程名称");
		map.put("dayNum","行程天数");
		map.put("lessonType","行程类别");
		map.put("colleagues","行程人");
		map.put("province","行程省份");
		map.put("city","行程城市");
		map.put("uname","行程添加人");
		map.put("startDate","开始时间");
		map.put("endDate","结束时间");
		map.put("scheduleDetail","行程备注");

		XSSFRow row = sheet.createRow((int) 0);
		row.setHeightInPoints(30);
		XSSFCell cell = null;
		int cell_0 = 0;
		for (String key : map.keySet()) {
			cell = row.createCell(cell_0);
			cell.setCellValue(map.get(key));
			cell.setCellStyle(style_title);
			cell_0 ++;
		}
		if(null != list && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(i+1);
				cell_0 = 0;

				for (String key : map.keySet()) {
					cell = row.createCell(cell_0);
					if(key.equals("index")){
						cell.setCellValue((i+1));
					}else{
						Object value = list.get(i).get(key);
						if(null == value){
							cell.setCellValue("--");
						}else{
							cell.setCellValue(value+"");
						}
					}
					cell.setCellStyle(style);
					cell_0 ++;
				}
			}
		}

		String path =PropertiesUtil.FILE_UPLOAD_PATH+userId+"/行程管理.xlsx";
		FileOutputStream outputStream = null;
		try {
			// //给文件夹设置读写修改操作
			File  dir = new File(PropertiesUtil.FILE_UPLOAD_PATH+userId);
			String os = System.getProperty("os.name");
			if (!dir.exists()) {
				dir.mkdirs();  //如果文件不存在  在目录中创建文件夹   这里要注意mkdir()和mkdirs()的区别
				if(!os.toLowerCase().startsWith("win")){
					Runtime.getRuntime().exec("chmod 777 " + dir.getPath());
				}
			}

			outputStream = new FileOutputStream(path);
			wb.write(outputStream);
			wb.close();
			outputStream.close();

			//给文件设置读写修改操作
			File targetFile = new File(path);

			if(targetFile.exists()){

				targetFile.setExecutable(true);//设置可执行权限
				targetFile.setReadable(true);//设置可读权限
				targetFile.setWritable(true);//设置可写权限
				String saveFilename = targetFile.getPath();
				if(!os.toLowerCase().startsWith("win")){

					Runtime.getRuntime().exec("chmod 777 " + saveFilename);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return PropertiesUtil.FILE_HTTP_PATH+userId+"/行程管理.xlsx";
	}

	public List<HashMap<String, Object>> selectScheduleList(Map<String, Object> map) {
		String status = "";
		String state = (String) map.get("state");
		if(state != null && state.contains("预约")){
			status=" (startDate >=  NOW()) or";
		}
		if(state != null && state.contains("进行中")){
			status+=" (startDate <= NOW() and endDate >= NOW()) or";
		}
		if(state != null && state.contains("已结束")){
			status+=" (endDate <= NOW()) or";
		}
		if(state != null && state.contains("已删除")){
			status+=" (status = 2) or";
		}
		if(state != null && status.length()>2){
			state = status.substring(0,status.length()-2);
			if(!state.contains("(status = 2)")){
				state="("+state+") and status = 0 ";
			}
		}else{
			state = "";
		}
		map.put("state", state);
		List<String> less = null;
		Object lessonType = map.get("lessonType");
		if(null != lessonType && StringUtils.isNotBlank(lessonType+"")){
			less =new ArrayList<>();
			String ls [] = (lessonType+"").split(",");
			for (String string : ls) {
				less.add(string);
			}
			map.put("lessonType", less);
		}
		List<HashMap<String, Object>> list = scheduleMapper.getObjScheduleList(map);
		for (HashMap<String, Object> map2 : list) {
			List<HashMap<String, Object>> sc = scheduleMapper.getScheduleColleagueListBySid(map2.get("id").toString());
			String colleagues = "";
			if(null != sc && sc.size() > 0){

				for (HashMap<String, Object> hashMap : sc) {

					colleagues+=hashMap != null ?(hashMap.get("name")+","):"";
				}
				System.out.println(colleagues);
				map2.put("colleagues",StringUtils.isNotBlank(colleagues)?colleagues.substring(0, colleagues.length()-1):"");
			}else{
				map2.put("colleagues", "");
			}
		}
		return list;
	}


	public List<Map<String, Object>> getTeacherAll(String adviserName) {

		return scheduleMapper.getTeacherAll(adviserName);
	}


	public void saveScheduleLogger(Schedule schedule) {
		scheduleMapper.saveScheduleLogger(schedule);
	}

	public List<HashMap<String, String>> getScheduleLogger(String scheduleId, int pageSizeInt, int currentPageInt ) {
		return scheduleMapper.getScheduleLogger(scheduleId,pageSizeInt,currentPageInt);
	}
	public int getScheduleLoggerTotal(String scheduleId) {
		return scheduleMapper.getScheduleLoggerTotal(scheduleId);
	}
	public List<HashMap<String, String>> getCompanyUserLike(String content,String pcCompanyId) {

		return scheduleMapper.getCompanyUserLike(content,pcCompanyId);
	}
	public Schedule getScheduleById(String id) {
		return scheduleMapper.getScheduleById(id);
	}


	public void deleteScheduleById(Schedule schedule,String userId) {

		schedule.setLoggerId(UUID.randomUUID().toString());
		UserAction userAction = new UserAction();
		userAction.setActionId(schedule.getLoggerId());
		userAction.setActionType("1");
		userAction.setCreateDate(DateUtils.currtime());
		userAction.setCreatedBy(schedule.getUpdatedBy());
		userAction.setActionTitle(" 取消了一个行程，请关注");

		userAction.setActionContent(schedule.getLessonName());
		userAction.setStartDate(schedule.getStartDate());
		userAction.setEndDate(schedule.getEndDate());
		List<HashMap<String, Object>> listUser= scheduleMapper.getScheduleColleagueListBySid(schedule.getId());
		String teacherName = "";
		for (HashMap<String, Object> uname:listUser) {
			teacherName +=uname.get("name")+",";
		}
		if(StringUtils.isNotBlank(teacherName)){
			schedule.setTeacherName(teacherName.substring(0,teacherName.length()-1));
		}
		schedule.setStateName("删除");
		schedule.setUpdatedBy(userId);

		scheduleMapper.saveScheduleLogger(schedule);
		String toMobile =
				"【蝌蚪智慧】您有一个行程取消的通知；" +
						"行程类型："+schedule.getLessonType()+"；" +
						"行程名称："+schedule.getLessonName()+"；" +
						"行程时间："+schedule.getStartDate()+"至"+schedule.getEndDate()+"；" +
						"行程地点："+schedule.getProvince()+"-"+schedule.getCity()+"；" +
						"行程人员："+schedule.getTeacherName()+"；" +
						(StringUtils.isBlank(schedule.getScheduleDetail())?"备注：无":(("备注："+schedule.getScheduleDetail())+"。"));
		DC_User loginUser = userMapper.getUserById(schedule.getUpdatedBy());
		String toEamil ="，您好！</br>" +
				"您有一个行程取消的通知<br>" +
				"行程类型："+schedule.getLessonType()+"<br>" +
				"行程名称："+schedule.getLessonName()+"<br>" +
				"行程时间："+schedule.getStartDate()+" 至 "+schedule.getEndDate()+"<br>" +
				"行程地点："+schedule.getProvince()+"-"+schedule.getCity()+"<br>" +
				"行程人员："+schedule.getTeacherName()+"<br>" +
				(StringUtils.isBlank(schedule.getScheduleDetail())?"备注：无":(("备注："+schedule.getScheduleDetail())+"。"))+"<br>"+
				"创建人："+loginUser.getUname()+"<br>" +
				"点击以下链接登录可查看详情：<br>" +
				"<a href='"+PropertiesUtil.USER_LOGIN_HREF+"'>"+PropertiesUtil.USER_LOGIN_HREF+"(CRM链接地址)</a><br>";
		if(null != listUser && listUser.size() > 0){
			for (HashMap<String, Object> hashMap : listUser) {
				userAction.setActionUserId(hashMap.get("id")+"");
				scheduleColleagueMapper.saveUserAction(userAction);

				DC_User user = userMapper.getUserById(hashMap.get("id"));
				if(!("0").equals(schedule.getRemind()) && null != user && StringUtils.isNotBlank(user.getMobile())){

					System.out.println(user.getMobile());
					System.out.println(toMobile);
					SmsApi.sendSms(user.getMobile(),toMobile);
				}
				if(!("0").equals(schedule.getRemind()) && null != user && StringUtils.isNotBlank(user.getEmail())){

					List<Schedule> scheduleList = scheduleMapper.getUserSchedule(hashMap.get("id")+"");
					String huizong = "";
					if(scheduleList.size()>0){
						huizong = "<br><div style='font-weight: bold;margin-bottom:8px;'>我的行程计划</div>";
					}
					for (Schedule sl : scheduleList) {
						huizong+= sl.getStartDate()+" 至 "+sl.getEndDate()+"<br>"+
								sl.getLessonName()+"<br>" +
								sl.getProvince()+" - "+sl.getCity()+"<br>"+
								"<div style='border-bottom:1px solid #ccc;height:1px;width:100%;margin-bottom:8px;margin-top:5px;'></div>";
					}
					MailUtils.send(user.getEmail(),"行程通知",user.getUname()+toEamil+huizong);
				}
			}
		}
		scheduleMapper.deleteScheduleById(schedule.getId(),userId);
		//scheduleColleagueMapper.deleteScheduleUserByScheduleId(schedule.getId());
	}


	public List<Schedule> getScheduleXcx(Map<String, Object> map,boolean queryUser) {
		List<Schedule> scheduleList = scheduleMapper.getScheduleXcx(map);
		if(StringUtils.isNotBlank(map.get("userId")+"") && queryUser){
			for (Schedule schedule : scheduleList) {
				List<HashMap<String, Object>> sc = scheduleMapper.getScheduleColleagueListBySid(schedule.getId());
				String colleagues = "";
				if(null != sc && sc.size() > 0){
					for (HashMap<String, Object> hashMap : sc) {
						colleagues+=null != hashMap ?(hashMap.get("name")+"、"):"";
					}
					schedule.setColleagues(colleagues.length() > 1 ? colleagues.substring(0, colleagues.length()-1):"");
				}else{
					schedule.setColleagues("");
				}
			}
		}
		return scheduleList;
	}





	public List<Map<String, Object>> getXcxScheduleDynamic(String userId,String ttype,int pageNo,int pageSize) {

		return scheduleMapper.getXcxScheduleDynamic(userId,ttype,pageNo,pageSize);
	}


	public Schedule getXcxScheduleLoggerById(String id) {
		return scheduleMapper.getXcxScheduleLoggerById(id);
	}
	public void setUserActionRead(String userId,String id){
		scheduleColleagueMapper.setUserActionRead(userId,id);
	}


	public List<Map<String,String>> getMyHistoryUser(String myUserId) {
		return scheduleColleagueMapper.getMyHistoryUserInfo(myUserId);
	}

    public int getXcxScheduleDynamicTotal(String userId, String ttype) {
		return scheduleMapper.getXcxScheduleDynamicTotal(userId,ttype);
    }

	public List<Map<String,Object>> getScheduleTypeList(Map<String, Object> map) {

		return scheduleMapper.getScheduleTypeList(map);
	}
	public List<Map<String,Object>> getWriteExcelScheduleTypeList(Map<String, Object> map) {
		return scheduleMapper.getWriteExcelScheduleTypeList(map);
	}
	public String writeExcelScheduleTypeList(List<HashMap<String,Object>> tongJi,List<Map<String,Object>> list,Map<String,Integer> title,String topTitle,String ttype,Map<String,Object> map) {
		Map<String,String> userHuiZong = new HashMap<>();
		for (HashMap<String,Object> user:tongJi) {
			userHuiZong.put(user.get("userId")+"",user.get("value")+"");
		}
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(topTitle);
		XSSFCellStyle style_title = wb.createCellStyle();
		Font fontHeader=wb.createFont();
		fontHeader.setFontHeightInPoints((short)12);
		fontHeader.setBold(true);
		//字体名称
		fontHeader.setFontName("宋体");
		style_title.setFont(fontHeader);
		style_title.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);


		XSSFCell cell = null;
		XSSFRow row = sheet.createRow((int) 0);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,title.keySet().size()-1));
		row.setHeightInPoints(30);
		cell = row.createCell(0);
		cell.setCellValue(topTitle);
		cell.setCellStyle(style_title);

		row = sheet.createRow((int) 1);
		for (String key : title.keySet()) {
			int cell_title = title.get(key);
			cell = row.createCell(cell_title);
			System.out.println(cell_title);
			if(cell_title > 1){
				cell.setCellValue(key+"("+ttype+")");
			}else if(cell_title == 1){
				cell.setCellValue(key+ttype+"数");
			}else{
				cell.setCellValue(key);
			}

			cell.setCellStyle(style_title);
		}

		int rownum = 2;
		Map<String,Integer> valueMap = new HashMap<>();
		if(null != list && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object uname = list.get(i).get("uname");

				if(valueMap.get(uname) == null){
					row = sheet.createRow(rownum);
					valueMap.put(uname+"",rownum);

					cell = row.createCell(0);
					cell.setCellValue(uname+"");
					cell = row.createCell(1);
					cell.setCellValue(userHuiZong.get(list.get(i).get("userId")));
					//cell.setCellFormula("SUM(C3:H3)");
					rownum ++;
				}
				cell = row.createCell(title.get(list.get(i).get("name")));
				cell.setCellValue(list.get(i).get("value")+"");
			}
		}
		Object titleDate = map.get("titleDate");
		if(titleDate != null && StringUtils.isNotBlank(titleDate.toString())){
			topTitle = topTitle+" "+titleDate;
		}
		String path =PropertiesUtil.FILE_UPLOAD_PATH+"schedule/"+topTitle+".xlsx";
		FileOutputStream outputStream = null;
		try {
			// //给文件夹设置读写修改操作
			File  dir = new File(PropertiesUtil.FILE_UPLOAD_PATH+"schedule");
			String os = System.getProperty("os.name");
			if (!dir.exists()) {
				dir.mkdirs();  //如果文件不存在  在目录中创建文件夹   这里要注意mkdir()和mkdirs()的区别
				if(!os.toLowerCase().startsWith("win")){
					Runtime.getRuntime().exec("chmod 777 " + dir.getPath());
				}
			}

			outputStream = new FileOutputStream(path);
			wb.write(outputStream);
			wb.close();
			outputStream.close();

			//给文件设置读写修改操作
			File targetFile = new File(path);

			if(targetFile.exists()){

				targetFile.setExecutable(true);//设置可执行权限
				targetFile.setReadable(true);//设置可读权限
				targetFile.setWritable(true);//设置可写权限
				String saveFilename = targetFile.getPath();
				if(!os.toLowerCase().startsWith("win")){

					Runtime.getRuntime().exec("chmod 777 " + saveFilename);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


		return PropertiesUtil.FILE_HTTP_PATH+"schedule/"+topTitle+".xlsx";
	}



	public List<Map<String,Object>> getScheduleComp(Map<String, Object> map) {
		return scheduleMapper.getScheduleComp(map);
	}

	public List<Map<String,Object>> getScheduleTitle(Map<String, Object> map) {
		return scheduleMapper.getScheduleTitle(map);
	}

	public List<Map<String,Object>> getScheduleCompTitle(Map<String, Object> map) {
		return scheduleMapper.getScheduleCompTitle(map);
	}

	public List<Map<String,Object>> getWriteExcelScheduleComp(Map<String, Object> map) {
		return scheduleMapper.getWriteExcelScheduleComp(map);
	}

	public List<HashMap<String,Object>> getWriteExcelCompTongJi(Map<String, Object> map) {
		return scheduleMapper.getWriteExcelCompTongJi(map);
	}

	public List<HashMap<String,Object>> getWriteExcelScheduleTongJi(Map<String, Object> map) {
		return scheduleMapper.getWriteExcelScheduleTongJi(map);
	}

    public void saveUserAction(UserAction userAction) {
		scheduleColleagueMapper.saveUserAction(userAction);
    }

	public List<Map<String,String>> getPCUserIds() {
		return scheduleColleagueMapper.getPCUserIds();
	}

	public void setUserActionNoPrompt(String userId) {
		scheduleColleagueMapper.setUserActionNoPrompt(userId);
	}

    public Map<String,Object> getUserActionById(String id) {

		return  scheduleColleagueMapper.getUserActionById(id);
    }
}
