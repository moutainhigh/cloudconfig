package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.mapper.MeetingExerciseMapper;
import com.xkd.model.*;
import com.alibaba.fastjson.TypeReference;
import com.xkd.service.*;
import com.xkd.utils.DateUtils;
import com.xkd.utils.FileUtil;
import com.xkd.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(description = "会务功能接口")
@Controller
@RequestMapping("/meeting")
@Transactional
public class MeetingController  extends BaseController{

	@Autowired
	private MeetingService meetingService;
	@Autowired
	private UserAttendMeetingService userAttendMeetingService;
	@Autowired
	private AdviserService adviserService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private MeetingExerciseMapper meetingExerciseMapper;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private WjUserExamService wjUserExamService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private SolrService solrService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CompanyRelativeUserService companyRelativeUserService;
	@Autowired
	private  TicketService ticketService;
	@Autowired
	private  UserDataPermissionService userDataPermissionService;
	@Autowired
	private  DepartmentService departmentService;

	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月16日 
	 * @功能描述:根据参数查询会议信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectMeetingByParam")
	public ResponseDbCenter selectMeetingByParam(HttpServletRequest req,HttpServletResponse rsp){
		
		//综合查询条件
		String meetingName = req.getParameter("meetingName");
		String meetingType = req.getParameter("meetingType");
		String meetingStatus = req.getParameter("meetingStatus");
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String province = req.getParameter("province");
		String city = req.getParameter("city");
		String county = req.getParameter("county");
		String departmentId = req.getParameter("departmentId");
		//会议名称
		String mname = req.getParameter("mname");

		String loginUserId = (String) req.getAttribute("loginUserId");

		if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		if("all".equalsIgnoreCase(meetingType) || "null".equalsIgnoreCase(meetingType)){
			
			meetingType = "";
		}
		
		if("all".equalsIgnoreCase(meetingStatus) || "null".equalsIgnoreCase(meetingStatus)){
			
			meetingStatus = "";
		}
		
		String address = "";
		
		if(StringUtils.isNotBlank(province)){
			address = " m.province = '"+province+"' ";
		}
		
		if(StringUtils.isNotBlank(city)){
			address += " and m.city = '"+city+"' ";
		}
		
		if(StringUtils.isNotBlank(county)){
			address += " and  m.county = '"+county+"' ";
		}
		
		String meetingTypeStr = "";
		
		if(StringUtils.isNotBlank(meetingType)){
			
			String[] mtypes = meetingType.split(",");
			 
			for(int i = 0;i<mtypes.length;i++){
				
				meetingTypeStr += " m.meetingType = '"+mtypes[i]+"' or ";
			}
			
			if(StringUtils.isNotBlank(meetingTypeStr)){
				
				meetingTypeStr = "("+meetingTypeStr.substring(0, meetingTypeStr.lastIndexOf("or"))+")";
			}
		}

		/*
		 <if test="startTime != null and startTime !='' ">
    	and  m.startTime &gt;= #{startTime}
    </if>
    <if test="endTime != null and endTime !='' ">
    	and  m.startTime &lt;= #{endTime}
    </if>
		 */

		String meetingStatusStr = "";
		if(StringUtils.isNotBlank(meetingStatus)){

			String currentDateStr = DateUtils.getCurrTime("yyyy-MM-dd HH:mm");
			String[] meetingStatusTempt = meetingStatus.split(",");

			for(String t: meetingStatusTempt){
				if("未开始".equals(t)){
					meetingStatusStr += " ( '"+currentDateStr+"' < m.startTime) or ";
				}else if("进行中".equals(t)){
					meetingStatusStr += " ( '"+currentDateStr+"' >= m.startTime and  '"+currentDateStr+"' <= m.endTime ) or ";
				}else if("已结束".equals(t)){
					meetingStatusStr += " ( '"+currentDateStr+"' > m.endTime) or ";
				}
			}

			if(StringUtils.isNotBlank(meetingStatusStr)){
				meetingStatusStr = "("+meetingStatusStr.substring(0,meetingStatusStr.lastIndexOf("or"))+")";
			}
		}
		


		Integer num = 0;
		List<Meeting> meetings = null;

		try{

			int pageSizeInt = Integer.parseInt(pageSize);
			int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
			if(StringUtils.isNotBlank(meetingName)){
				meetingName = "(m.meetingName like '%"+meetingName+"%')";
			}

			String pcCompanyId = null;
			Map<String, Object> mapp = userService.selectUserById(loginUserId);
			if(mapp !=null && mapp.size() > 0){
				String roleId = (String)mapp.get("roleId");
				if(!"1".equals(roleId)){
					pcCompanyId = (String)mapp.get("pcCompanyId");
				}
			}

			List<String> list = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId,loginUserId);
			meetings = meetingService.selectMeetingByParam(startTime,endTime,
					meetingTypeStr,meetingStatusStr,meetingName,address,"",mname,currentPageInt,pageSizeInt,list,pcCompanyId);

			num = meetingService.getMeetingCountByParam(meetingTypeStr,meetingStatusStr,meetingName,address,"",
					mname,startTime,endTime,list,pcCompanyId);

			for(Meeting meeting : meetings){
				if(meeting.getTeacherId() != null){
					String  teacherIdIds = meeting.getTeacherId();
					String[] idStrings = teacherIdIds.split(",");
					String teacherName = "";
					for(int i = 0 ;i<idStrings.length;i++){
						Map<String,Object> map = userService.selectUserById(idStrings[i].trim());
						if(map != null){
							teacherName += map.get("uname")+"";
						}
					}
					meeting.setTeacherName(teacherName);
				}

				Date startTimeDate =  DateUtils.getDateByDateString(meeting.getStartTime());
				Date endTimeDate =  DateUtils.getDateByDateString(meeting.getEndTime());

				Date now = new Date();
				if(startTimeDate != null && now.before(startTimeDate)){
					meeting.setMeetingStatus("未开始");
				}else if(endTimeDate != null && now.after(endTimeDate)){
					meeting.setMeetingStatus("已结束");
				}else{
					meeting.setMeetingStatus("进行中");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return  ResponseConstants.FUNC_SERVERERROR;
		}

		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(meetings);
		responseDbCenter.setTotalRows(num.toString());
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月16日 
	 * @功能描述:更新会议表
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "新增编辑会务")
	@ResponseBody
	@RequestMapping(value = "/changeMeeting",method = RequestMethod.POST)
	public ResponseDbCenter changeMeeting(HttpServletRequest req,HttpServletResponse rsp,
										  @ApiParam(value="会务ID",required = false) @RequestParam(required = false) String id,
										  @ApiParam(value="新增编辑标志",required = false) @RequestParam(required = false) String flag,
										  @ApiParam(value="会务名称",required = false) @RequestParam(required = false) String meetingName,
										  @ApiParam(value="",required = false) @RequestParam(required = false) String introduce,
										  @ApiParam(value="省",required = false) @RequestParam(required = false) String province,
										  @ApiParam(value="市",required = false) @RequestParam(required = false) String city,
										  @ApiParam(value="区",required = false) @RequestParam(required = false) String county,
										  @ApiParam(value="详细地址",required = false) @RequestParam(required = false) String place,
										  @ApiParam(value="开始时间",required = false) @RequestParam(required = false) String startTime,
										  @ApiParam(value="结束时间",required = false) @RequestParam(required = false) String endTime,
										  @ApiParam(value="天数",required = false) @RequestParam(required = false) String dates,
										  @ApiParam(value="会务类型",required = false) @RequestParam(required = false) String meetingType,
										  @ApiParam(value="会务状态",required = false) @RequestParam(required = false) String meetingStatus,
										  @ApiParam(value="会务内容",required = false) @RequestParam(required = false) String meetingContent,
										  @ApiParam(value="会务负责人",required = false) @RequestParam(required = false) String leader,
										  @ApiParam(value="",required = false) @RequestParam(required = false) String contacter,
										  @ApiParam(value="",required = false) @RequestParam(required = false) String mrequire,
										  @ApiParam(value="",required = false) @RequestParam(required = false) String theme,
										  @ApiParam(value="会务行程",required = false) @RequestParam(required = false) String travelArrangement,
										  @ApiParam(value="主讲老师",required = false) @RequestParam(required = false) String teacherId,
										  @ApiParam(value="",required = false) @RequestParam(required = false) String status,
										  @ApiParam(value="备注",required = false) @RequestParam(required = false) String content,
										  @ApiParam(value="",required = false) @RequestParam(required = false) String meetingExercise,
										  @ApiParam(value="token",required = false) @RequestParam(required = false) String token,
										  @ApiParam(value="",required = false) @RequestParam(required = false) String isUpdateExercise,
										  @ApiParam(value="顶部图片",required = false) @RequestParam(required = false) String meetingImage,
										  @ApiParam(value="报名信息填写",required = false) @RequestParam(required = false) String attributeContent,
										  @ApiParam(value="票务大图（票种 ticketType，金额 price，库存 saving）",required = false) @RequestParam(required = false) String tickets,
										  @ApiParam(value="票务权益",required = false) @RequestParam(required = false) String ticketRights,
										  @ApiParam(value="票务详情",required = false) @RequestParam(required = false) String meetingDetail,
										  @ApiParam(value="是否发送手机短信",required = false) @RequestParam(required = false) String sendMessageFlag,
										  @ApiParam(value="接收人的手机号",required = false) @RequestParam(required = false) String mobile,
										  @ApiParam(value="部门ID",required = false) @RequestParam(required = false) String departmentId) throws Exception{


		String loginUserId = (String) req.getAttribute("loginUserId");
		if(StringUtils.isBlank(flag)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		if(StringUtils.isBlank(id) && "update".equals(flag)){
			return ResponseConstants.FUNC_UPDATENOID;
		}

		Meeting meeting = new Meeting();

		meeting.setContacter(contacter);
		meeting.setDates(dates);
		meeting.setEndTime(endTime);
		meeting.setIntroduce(introduce);
		meeting.setLeader(leader);
		meeting.setMeetingName(meetingName);
		meeting.setMrequire(mrequire);
		meeting.setProvince(province);
		meeting.setCity(city);
		meeting.setCounty(county);
		meeting.setPlace(place);
		meeting.setStartTime(startTime);
		meeting.setMeetingType(meetingType);
		meeting.setMeetingContent(meetingContent);
		meeting.setMeetingDetail(meetingDetail);
		meeting.setAttributeContent(attributeContent);
		meeting.setTicketRights(ticketRights);
		meeting.setMeetingImage(meetingImage);
		if(StringUtil.isBlank(sendMessageFlag)){
			meeting.setSendMessageFlag("false");
		}else{
			meeting.setSendMessageFlag(sendMessageFlag);
			meeting.setMobile(mobile);
		}


		if(StringUtils.isBlank(departmentId)){
			Map<String, Object> map = userService.selectUserById(loginUserId);
			if(map.get("departmentId") != null){
				meeting.setDepartmentId((String) map.get("departmentId"));
			}
		}else{
			meeting.setDepartmentId(departmentId);
		}



		if(StringUtils.isNotBlank(status)){
			meeting.setStatus(new Integer(status));
		}else{
			meeting.setStatus(new Integer(0));
		}
		meeting.setContent(content);
		if(StringUtils.isNotBlank(teacherId)){
			meeting.setTeacherId(teacherId);
		}
		meeting.setTheme(theme);
		meeting.setUpdatedBy(loginUserId);
		meeting.setTravelArrangement(travelArrangement);
		
		if("update".equals(flag)){

			meeting.setId(id);
			meeting.setUpdatedBy(loginUserId);

			try {

				meetingService.updateMeetingById(meeting);
				List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(tickets,Object.class);

				List<Map<String, Object>> insertMap = new ArrayList<>();
				for(Map<String, Object> map : results){
					if(map.get("id")==null){
						map.put("meetingId",id);
						insertMap.add(map);
					}else{
						map.put("meetingId",id);
						ticketService.updateTicketById(map);
					}
				}

				if(insertMap != null && insertMap.size() > 0 ){
					ticketService.saveTickets(insertMap);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
			}
			
		}else{

			String insertId = UUID.randomUUID().toString();
			meeting.setId(insertId);
			meeting.setUpdatedBy(loginUserId);
			meeting.setCreatedBy(loginUserId);
			
			try {

				Map<String, Object> mapp = userService.selectUserById(loginUserId);
				if(mapp !=null && mapp.size() > 0){
					String pcCompanyId = (String)mapp.get("pcCompanyId");
					meeting.setPcCompanyId(pcCompanyId);
				}

				meetingService.saveMeeting(meeting);
				List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(tickets,Object.class);
				for(Map<String, Object> map : results){
					map.put("meetingId",insertId);
				}

				if(results != null && results.size() > 0){
					ticketService.saveTickets(results);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
			}
		}
		if(StringUtils.isNotBlank(isUpdateExercise) && isUpdateExercise.equals("1")){
			meetingExerciseMapper.editExerciseMeetingIsNoull(id);
			if(StringUtils.isNotBlank(meetingExercise)){
				List<Map<String, Object>> eMList = (List<Map<String, Object>>) JSON.parse(meetingExercise);
				if(null != eMList && eMList.size()>0 ){
					for (Map<String, Object> map : eMList) {
						if(null != map){
							Object eid = map.get("eid");
							Object ttype = map.get("ttype");
							meetingExerciseMapper.editMeetingExerciseList(eid,ttype,id,req.getSession().getAttribute(token).toString());
						}
					}
				}
			}
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	} 
	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年9月15日 
	 * @功能描述:
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectAdvisersMapsMeetingId")
	public ResponseDbCenter selectAdvisersMapsMeetingId(HttpServletRequest req,HttpServletResponse rsp){
		
		String meetingId = req.getParameter("meetingId");
		
		List<HashMap<String,Object>> adviserDirectorMaps = null;
		
		Map<String,Object> returnMap = new HashMap<>();
		
		Set<Map<String,Object>> returnAdviserList = new HashSet<>();
		Set<Map<String,Object>> returnDirectorList = new HashSet<>();
		
		try {
			
			adviserDirectorMaps = userAttendMeetingService.selectAdviserDirectorMaps(meetingId);
			
			for(HashMap<String,Object> map:adviserDirectorMaps){
				
				Map<String,Object> adviserMap = new HashMap<>();
				Map<String,Object> directorMap = new HashMap<>();
				
				if(map != null && !map.isEmpty()){
					
					String companyAdviserId = map.get("companyAdviserId") == null?"":map.get("companyAdviserId").toString();
					String directorId = map.get("companyDirectorId") == null?"":map.get("companyDirectorId").toString();
					String adviserName = map.get("adviserName") == null?"":map.get("adviserName").toString();
					String directorName = map.get("directorName") == null?"":map.get("directorName").toString();

					if(StringUtils.isNotBlank(companyAdviserId) && StringUtils.isNotBlank(adviserName)){
							adviserMap.put("id", companyAdviserId);
							adviserMap.put("adviserName", adviserName);
							returnAdviserList.add(adviserMap);
					}
					if(StringUtils.isNotBlank(directorId) && StringUtils.isNotBlank(directorName)){
						directorMap.put("id",directorId);
						directorMap.put("directorName",directorName);
						returnDirectorList.add(directorMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		returnMap.put("advisers",returnAdviserList);
		returnMap.put("directors",returnDirectorList);
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(returnMap);
		
		return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年9月15日
	 * @功能描述:
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectProjectAdvisersMeetingId")
	public ResponseDbCenter selectProjectAdvisersMeetingId(HttpServletRequest req,HttpServletResponse rsp){

		String meetingId = req.getParameter("meetingId");

		List<HashMap<String,Object>> adviserDirectorMaps = null;

		Map<String,Object> returnMap = new HashMap<>();

		Set<Map<String,Object>> returnAdviserList = new HashSet<>();
		Set<Map<String,Object>> returnDirectorList = new HashSet<>();

		try {

			adviserDirectorMaps = userAttendMeetingService.selectProjectAdvisersMeetingId(meetingId);

			for(HashMap<String,Object> map:adviserDirectorMaps){

				Map<String,Object> adviserMap = new HashMap<>();
				Map<String,Object> directorMap = new HashMap<>();

				if(map != null && !map.isEmpty()){

					String directorName = map.get("directorName") == null?"":map.get("directorName").toString();
					String adviserId = map.get("adviserId") == null?"":map.get("adviserId").toString();
					String adviserName = map.get("adviserName") == null?"":map.get("adviserName").toString();
					String directorId = map.get("directorId") == null?"":map.get("directorId").toString();

					if(StringUtils.isNotBlank(adviserName) && StringUtils.isNotBlank(adviserId)){
						adviserMap.put("id", adviserId);
						adviserMap.put("adviserName",adviserName);
						returnAdviserList.add(adviserMap);
					}

					if(StringUtils.isNotBlank(directorId) && StringUtils.isNotBlank(directorName)){
						directorMap.put("id",directorId);
						directorMap.put("directorName",directorName);
						returnDirectorList.add(directorMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

		returnMap.put("advisers",returnAdviserList);
		returnMap.put("directors",returnDirectorList);

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(returnMap);

		return responseDbCenter;
	}


	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月16日 
	 * @功能描述:根据会议id获得企业以及企业联系人信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectMeetingUserById")
	public ResponseDbCenter selectMeetingUserById(HttpServletRequest req,HttpServletResponse rsp){
		
		
		String token = req.getParameter("token");
		String content = req.getParameter("content");
		String companyAdviser = req.getParameter("companyAdviser");
		String companyDirector = req.getParameter("companyDirector");
		String mgroup = req.getParameter("mgroup");
		String userType = req.getParameter("userType");
		String needCheck = req.getParameter("needCheck");
		String meetingId = req.getParameter("meetingId");
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		
		int  pageSizeInt = Integer.parseInt(pageSize);
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		if(StringUtils.isBlank(meetingId) || StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize) || "undefined".equals(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		if(StringUtils.isNotBlank(content)){
			content = " and (c.companyName like '%"+content+"%' or u.mobile like '%"+content+"%' or u.uname like '%"+content+"%') ";
		}
		
		String needCheckStr = "";
		if(StringUtils.isNotBlank(needCheck) && "true".equals(needCheck)){
			needCheckStr = " and mu.status = '未审核'";
		}
		
		String companyAdviserStr = "";
		if(StringUtils.isNotBlank(companyAdviser)){
			companyAdviserStr = " and c.companyAdviserId = '"+companyAdviser+"'";
		}
		
		String companyDirectorStr = "";
		if(StringUtils.isNotBlank(companyDirector)){
			companyDirectorStr = " and c.companyDirectorId = '"+companyDirector+"'";
		}
		
		String mgroupStr = "";
		if(StringUtils.isNotBlank(mgroup)){
			mgroupStr = " and mu.mgroup like  '%"+mgroup+"%'";
		}
		
		String userTypeStr = "";
		if(StringUtils.isNotBlank(userType)){
			userTypeStr = " and c.userType = '"+userType+"'";
		}
		
		List<HashMap<String,Object>> userAttendMeetingMaps = null;
		Integer num = 0;
		Integer attend = 0;
		Integer needCheckCount = 0;
		
		boolean superFlag = false;
		try {
			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String, Object>  user =  userService.selectUserById(loginUserId);
			
			if(user != null){
				String roleName = (String)user.get("roleName");
				if("超级管理员".equals(roleName)){
					superFlag = true;
				}
			}
			
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("meetingId",meetingId);
			paramMap.put("content",content);
			paramMap.put("needCheck",needCheckStr);
			paramMap.put("companyAdviserStr",companyAdviserStr);
			paramMap.put("companyDirectorStr",companyDirectorStr);
			paramMap.put("userTypeStr",userTypeStr);
			paramMap.put("mgroupStr",mgroupStr);
			paramMap.put("currentPageInt",currentPageInt);
			paramMap.put("pageSizeInt",pageSizeInt);

			userAttendMeetingMaps = userAttendMeetingService.selectUserInfoMapByMeetingId(paramMap);
			num = userAttendMeetingService.getTotoalByMeetingId(meetingId,content,companyAdviserStr,companyDirectorStr,mgroupStr,userTypeStr);
			attend = userAttendMeetingService.getTotoalByMeetingIdAttend(meetingId,content,companyAdviserStr,companyDirectorStr,mgroupStr,userTypeStr);
			needCheckCount = userAttendMeetingService.getTotoalByMeetingIdNeedCheck(meetingId,content,companyAdviserStr,companyDirectorStr,mgroupStr,userTypeStr);

			List<String> companyIdList=new ArrayList<>();
			for(HashMap<String,Object> map : userAttendMeetingMaps){
				String companyId = map.get("companyId") == null?"":map.get("companyId").toString();
				if(StringUtils.isNotBlank(companyId)){
					companyIdList.add(companyId);
				}
			}
			List<Map<String,Object>> relativeUserList=companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);
			Boolean permission = userService.getPrivatePermission(loginUserId, "meeting/private");
			for(HashMap<String,Object> map : userAttendMeetingMaps){

				String parentIndustryid = map.get("parentIndustryId") == null?"":map.get("parentIndustryId").toString();
				String companyAdviserId = map.get("companyAdviserId") == null?"":map.get("companyAdviserId").toString();
				String companyDirectorId = map.get("companyDirectorId") == null?"":map.get("companyDirectorId").toString();
				String mapCompanyId = map.get("companyId") == null?"":map.get("companyId").toString();
				String enrollTime = map.get("enrollTime") == null?"":map.get("enrollTime").toString();
				String paymentMoney = map.get("paymentMoney") == null?"":map.get("paymentMoney").toString();
				
				if(permission == false  && StringUtils.isBlank(companyAdviserId) && StringUtils.isBlank(companyDirectorId)){
					permission = true;
				}
				if(permission == false  && StringUtils.isNotBlank(companyAdviserId) && loginUserId.equals(companyAdviserId)){
					permission = true;
				}
				if(permission == false  && StringUtils.isNotBlank(companyDirectorId) && loginUserId.equals(companyDirectorId)){
					permission = true;
				}
				if(permission == false  ){
					for (int i = 0; i <relativeUserList.size() ; i++) { //判断相关人员权限
						if (loginUserId.equals(relativeUserList.get(i).get("userId")) && relativeUserList.get(i).get("companyId").equals(mapCompanyId)){
							permission=true;
						}
					}
				}
				//手机号、电话号码保密
				if(permission == false ){
					map.put("umobile", "***");
				map.put("mobile", "***");
					map.put("uphone", "***");
				}


				if(StringUtils.isNotBlank(enrollTime)){
					Date date = DateUtils.getDateByDateString(enrollTime);
					if(date == null){
						map.put("enrollTimeStr",enrollTime);
					}else {
						map.put("enrollTimeStr",DateUtils.dateToString(date, "yyyy-MM-dd"));
					}
				}
				if(StringUtils.isNotBlank(paymentMoney)){
					 Pattern pattern = Pattern.compile("[0-9]*"); 
					 Matcher isNum = pattern.matcher(paymentMoney);
					 if(isNum.matches()){
						 Double paymentMoneyDouble = new Double(paymentMoney);
							double l = 10000;
							DecimalFormat df = new DecimalFormat("0.0000");
							String paymentMoneyStr  = df.format(paymentMoneyDouble.intValue()/l);
							if(StringUtils.isNotBlank(paymentMoneyStr)){
								map.put("paymentMoney",paymentMoneyStr);
							}
					 }
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(userAttendMeetingMaps);
		
		//参会和已参会的
		responseDbCenter.setTotalRows(num.toString()+","+attend.toString()+","+needCheckCount.toString());
		return responseDbCenter;
	} 
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月6日 
	 * @功能描述:查询企业头像
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectCompanysLogo")
	public ResponseDbCenter selectCompanysLogo(HttpServletRequest req,HttpServletResponse rsp){
		
		
		String meetingId = req.getParameter("meetingId");
		
		if(StringUtils.isBlank(meetingId)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		
		List<Map<String,Object>> logosMap =  meetingService.selectCompanysLogo(meetingId);
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(logosMap);
		
		return responseDbCenter;
	} 
	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月18日 
	 * @功能描述:查询在该会议没有分组的企业
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectMeetingNotGropById")
	public ResponseDbCenter selectMeetingNotGropById(HttpServletRequest req,HttpServletResponse rsp){
		
		
		String meetingId = req.getParameter("meetingId");
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		
		int  pageSizeInt = Integer.parseInt(pageSize);
		
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		
		if(StringUtils.isBlank(meetingId) || StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize) || "undefined".equals(meetingId)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String notGroupStr = "and ( mgroup is null or mgroup ='') ";
		
		List<UserAttendMeeting> userAttendMeetings = userAttendMeetingService.selectUserInfoByMeetingId(meetingId,notGroupStr,currentPageInt,pageSizeInt);
		
		Integer num = userAttendMeetingService.getNotGroupCount(meetingId,notGroupStr);
		
		for(UserAttendMeeting userAttendMeeting : userAttendMeetings){
			
			Company company = companyService.selectCompanyInfoById(userAttendMeeting.getCompanyId());
			
			UserInfo userInfo = userInfoService.selectUserInfoById(userAttendMeeting.getUserId());
			
			Address address = addressService.selectAddressByUserId(userAttendMeeting.getUserId());
			
			
			userAttendMeeting.setCompany(company);
			
			userAttendMeeting.setUserInfo(userInfo);
			
			userAttendMeeting.setAddress(address);
		}
		
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(userAttendMeetings);
		responseDbCenter.setTotalRows(num.toString());
		
		return responseDbCenter;
	} 
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年6月2日 
	 * @功能描述:批量给企业用户打星级标志
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateUserStarsByIds")
	public ResponseDbCenter updateUserStarsByIds(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
		
		
		String ids = req.getParameter("ids");
		String stars = req.getParameter("stars");
		
		if(StringUtils.isBlank(ids) || StringUtils.isBlank(stars) ){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] iids = ids.split(",");
		String[] sstars = stars.split(",");
		
		if(iids.length != sstars.length){
			
			return ResponseConstants.ILLEGAL_PARAM;
		}
		
		try {
			
			for(int i = 0;i<iids.length;i++){
				
				userAttendMeetingService.updateUserStarsById(iids[i],sstars[i]);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	} 
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年6月2日 
	 * @功能描述:批量删除会议客户
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteMeetingUserByIds")
	public ResponseDbCenter deleteMeetingUserByIds(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
		/*
		 * delete a1,b1 from a a1 left join b b1 on b1.eid = a1.id where a1.meeting = 1 and a1.uid = 2
		 * 
		 * replace into a(id,) values(id,)
		 */
		
		String ids = req.getParameter("ids");
		
		if(StringUtils.isBlank(ids)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] iids = ids.split(",");
		
		try {
			
			String idString = "";
			for(int i = 0;i<iids.length;i++){
				
				idString += "'"+iids[i]+"',";
			}
			
			idString = "id in (" + idString.substring(0, idString.length()-1) +")";
			
			/*List<UserAttendMeeting> userAttendMeetings = userAttendMeetingService.selectMeetingUserByIds(idString);
			
			for(UserAttendMeeting userAttendMeeting :userAttendMeetings){
				
				String meetingId = userAttendMeeting.getMeetingId()==null?"0":userAttendMeeting.getMeetingId().toString();
				if(StringUtils.isNotBlank(userAttendMeeting.getUserId())){
					
					UserInfo userInfo = userInfoService.selectUserInfoById(userAttendMeeting.getUserId());
				}
			}*/

			List<Map<String,Object>> maps = userAttendMeetingService.selectMeetingUsersByIds(idString);
			List<String> companyIdList = new ArrayList<>();
			for(Map<String,Object> map : maps){
				String  companyId =  map.get("companyId") == null?null:(String) map.get("companyId");
				if(StringUtils.isNotBlank(companyId)){
					companyIdList.add(companyId);
				}
			}

			userAttendMeetingService.deleteMeetingUserByIds(idString);

			if(companyIdList.size() > 0){
				solrService.updateCompanyIndex(companyIdList);
			}

		} catch (Exception e) {
			
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	} 
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月17日 
	 * @功能描述:编辑企业参会对象信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMeetingUser")
	public ResponseDbCenter updateMeetingUser(@RequestBody String result) throws Exception{
			
		List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(result,Object.class);
		
		for(Map<String, Object> map : results){
			
//			Map<String, Object> addressMap =  (Map<String,Object>)map.get("address");
//			Map<String, Object> userInfoMap =  (Map<String,Object>)map.get("userInfo");

			String id = map.get("id")==null?null:(String) map.get("id");
			String meetingId = map.get("meetingId")==null?null:(String) map.get("meetingId");
			String attendmeetingTime = map.get("attendmeetingTime")==null?"":(String) map.get("attendmeetingTime");
			String companyId = map.get("companyId")==null?"":(String) map.get("companyId");
			String manager = map.get("companyAdviserId")==null?"":(String) map.get("companyAdviserId");
			String director = map.get("companyDirectorId")==null?"":(String)map.get("companyDirectorId");
			String learnStatus = map.get("learnStatus")==null?"":(String) map.get("learnStatus");
			String mgroup = map.get("mgroup")==null?"":(String) map.get("mgroup");
			String need =  map.get("need")==null?"":(String)map.get("need");
			
			String userId = map.get("userId")==null?"":(String) map.get("userId");
			String uname = map.get("uname")==null?"":(String) map.get("uname");
			String mobile = map.get("umobile")==null?"":(String) map.get("umobile");
			String uphone = map.get("uphone")==null?"":(String) map.get("uphone");
			String address = map.get("address")==null?"":(String) map.get("address");

			String hotelId = map.get("hotelId")==null?null:(String) map.get("hotelId");
			String hotelName = map.get("hotelName")==null?"":(String) map.get("hotelName");
			String roomType = map.get("roomType")==null?"":(String) map.get("roomType");
			String startTime = map.get("startTime")==null?"":(String) map.get("startTime");
			String endTime = map.get("endTime")==null?"":(String) map.get("endTime");
			
			String paymentId = (map.get("paymentId")==null?null:(String)map.get("paymentId"));
			String userType = map.get("userType")==null?"":(String) map.get("userType");
			String channel = map.get("channel")==null?"":(String) map.get("channel");
			String enrollTime = map.get("enrollTime")==null?"":(String) map.get("enrollTime");
			String paymentMoney = map.get("paymentMoney")==null?"":(String) map.get("paymentMoney");
			
			
			try {
				
				if(StringUtils.isNotBlank(id+"")){
					
					UserAttendMeeting userAttendMeeting = new UserAttendMeeting();
					userAttendMeeting.setId(id);
//					userAttendMeeting.setManager(manager);
//					userAttendMeeting.setDirector(director);
					
					if(mgroup != null && (mgroup.contains(" ") || mgroup.contains(" "))){
						
						mgroup = mgroup.replaceAll(" ", "");
					}
					
					userAttendMeeting.setMgroup(mgroup);
					userAttendMeeting.setAttendMeetingTime(attendmeetingTime);
					userAttendMeeting.setNeed(need);
					userAttendMeeting.setEnrollTime(enrollTime);
					
					Integer xf = 0;
					
					if(StringUtils.isNotBlank(learnStatus)){
						
						if("复训".equals(learnStatus)){
							
							xf = 1;
						}
					}
					
					userAttendMeeting.setLearnStatus(xf);
					
					userAttendMeetingService.updateMeetingUserContentById(userAttendMeeting);
				}
				
				if(StringUtils.isNotBlank(companyId)){
					
					if(manager == null){
						
						manager = "";
					}
					
					if(director == null){
						
						director = "";
					}
					
					
					companyService.updateCompanyBySql("update dc_company set companyAdviserId = '"+manager+"',companyDirectorId = '"+director+"',"
							+ "userType = '"+userType+"',channel='"+channel+"',"
							+ "paymentMoney = '"+paymentMoney+"' where id = '"+companyId+"'");
				}
				
				if(StringUtils.isNotBlank(hotelName) || StringUtils.isNotBlank(endTime) ||
								StringUtils.isNotBlank(roomType) || StringUtils.isNotBlank(startTime) ){
					
					Hotel hotel = new Hotel();
					
					if(hotelId != null){
						
						hotel.setId(hotelId);
						hotel.setEndTime(endTime);
						hotel.setStartTime(startTime);
						
						//hotelService.updateHotelByIdNotNull(hotel);
						
					}else{
						
						hotel.setMeetingUserId(id);
						hotel.setEndTime(endTime);
						hotel.setStartTime(startTime);
						hotel.setPerson(" ");
						
						//hotelService.insertHotel(hotel);
					}
				}
				
				
				/*if(paymentId != null){
					
					if(userType == null || "".equals(userType)){
						userType = "";
					}
					
					if(enrollDate == null || "".equals(enrollDate)){
						enrollDate = "";
					}
					if(paymentMoney == null || "".equals(paymentMoney)){
						paymentMoney = "";
					}
					if(channel == null || "".equals(channel)){
						channel = "";
					}
					
					
					String sql = "update dc_payment set enrollDate='"+enrollDate+"',paymentMoney='"+paymentMoney+"',"+
								"updateDate = Now() where id = '"+paymentId+"'";
					
					paymentService.updatePaymentBySql(sql);
					
				}else{
					
					Payment payment = new Payment();
					
					payment.setEnrollDate(enrollDate);
					payment.setPaymentMoney(paymentMoney);
					payment.setCompanyId(companyId);
					
					paymentService.insertPayment(payment);
				}*/
				
				
				/*
				 * 将该企业维护到solr库中
				 */
				solrService.updateCompanyIndex(companyId);


				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
			}
		}
		
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	} 
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年6月8日 
	 * @功能描述:更新参会人信息以及项目信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMeetingUserProject")
	public ResponseDbCenter updateMeetingUserProject(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
			
			//meetingUserId
			String id = req.getParameter("meetingUserId");
			
			String meetingId = req.getParameter("meetingId");
			String mgroup = req.getParameter("mgroup");
			String companyAdviser = req.getParameter("companyAdviser");
			String director = req.getParameter("companyDirector");

			String userIds = req.getParameter("userIds");
			String unames = req.getParameter("unames");
			
			String projectId = req.getParameter("projectId");
			String projectName = req.getParameter("projectName");
			String projectSuggestion = req.getParameter("projectSuggestion");
			String plevel = req.getParameter("plevel");
			String userNeed = req.getParameter("userNeed");
			String pdesc = req.getParameter("pdesc");
			String following = req.getParameter("following");
			String content = req.getParameter("content");

			String companyId = req.getParameter("companyId");
			String companyName = req.getParameter("companyName");
			String parentIndustry = req.getParameter("parentIndustryId");  
			String sonIndustry = req.getParameter("sonIndustryId");
			
			//修改的是企业的地址
			String addressId = req.getParameter("addressId");
			String province = req.getParameter("province");
			String city = req.getParameter("city");
			String county = req.getParameter("county");

		String loginUserId = (String) req.getAttribute("loginUserId");
			try {

				String pcCompanyId = null;
				Map<String, Object> mapp = userService.selectUserById(loginUserId);
				if(mapp !=null && mapp.size() > 0){
					pcCompanyId = (String)mapp.get("pcCompanyId");
				}

				List<Company>  companys = companyService.selectCompanyByName(companyName,pcCompanyId);
				
				if(companys != null){
					
					for(Company company : companys){
						
						if(!company.getId().equals(companyId)){
							
							return ResponseConstants.FUNC_COMPANY_EXIST;
						}
					}
				}
				
				
				if(StringUtils.isNotBlank(companyId)){
					
					String sql = "update dc_company set province = '"+province+"',city = '"+city+"',county = '"+county+"' where id = '"+companyId+"'";
					
					companyService.updateCompanyBySql(sql);
					
					sql = "update dc_user_address set province = '"+province+"',city = '"+city+"',county = '"+county+"' where userId = '"+companyId+"'";
					
					companyService.updateCompanyBySql(sql);
					
				}
				
				
				/*String userInfos = req.getParameter("userInfos");
				
				if(StringUtils.isNotBlank(userInfos)){
					
					List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(userInfos,Object.class);
					
					for(Map<String, Object> map : results){
						
						String userId = (String) map.get("userId");
						String uname = (String) map.get("uname");
						String mobile = (String) map.get("mobile");
						
						if(StringUtils.isNotBlank(userId)){
							
							UserInfo userInfo = new UserInfo();
							userInfo.setId(userId);
							userInfo.setUname(uname);
							userInfo.setMobile(mobile);
							
							userInfoService.updateUserInfoById(userInfo);
						}
					}
				}*/
				
				/*if(StringUtils.isNotBlank(id)){
					
					UserAttendMeeting userAttendMeeting = new UserAttendMeeting();
					
					userAttendMeeting.setId(id);
					
					if(mgroup != null && (mgroup.contains(" ") || mgroup.contains(" "))){
						
						mgroup = mgroup.replaceAll(" ", "");
					}
					
					userAttendMeeting.setMgroup(mgroup);
					
					userAttendMeetingService.updateMeetingUserContentById(userAttendMeeting);
				}*/
				
				Map<String,Object> projectMap = new HashMap<>();
				
				if(StringUtils.isNotBlank(projectId)){
					
					projectMap.put("id",projectId);
					projectMap.put("projectName",projectName);
					projectMap.put("description",pdesc);
					projectMap.put("companyId",companyId);
					
					if(StringUtils.isNotBlank(meetingId)){
						
						projectMap.put("meetingId",meetingId);
					}
					

					projectService.updateProject(projectMap);

					
				}else{
					
					projectId = UUID.randomUUID().toString();
					
					projectMap.put("id",projectId);
					projectMap.put("projectName",projectName);
					projectMap.put("description",pdesc);
					projectMap.put("companyId",companyId);
					if(StringUtils.isNotBlank(meetingId)){
						
						projectMap.put("meetingId",meetingId);
					}
					
					projectService.insertProject(projectMap);
				}

				
				/*
				 * 将该企业维护到solr库中
				 */
				solrService.updateCompanyIndex(companyId);


				
			} catch (Exception e) {
				
				e.printStackTrace();
				throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
			}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	} 
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月25日 
	 * @功能描述:根据id删除会议
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteMeetingById")
	public ResponseDbCenter deleteMeetingById(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
			
		String ids = req.getParameter("meetingId");
			
		if(StringUtils.isBlank(ids) || "undefined".equals(ids)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] idStrings = ids.split(",");
		String meetingIds = "";
		for(int i = 0;i<idStrings.length;i++){
			
			meetingIds += "'"+idStrings[i]+"',";
		}
		
		if(StringUtils.isNotBlank(meetingIds)){
			
			meetingIds = "("+meetingIds.substring(0, meetingIds.length()-1)+")";
		}
		
		try {
			
			meetingService.deleteMeetingById(meetingIds);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
			
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	} 
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月17日 
	 * @功能描述:根据会议ID获得会议相关信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectMeetingById")
	public ResponseDbCenter selectMeetingById(HttpServletRequest req,HttpServletResponse rsp){
		
		
		String meetingId = req.getParameter("meetingId");
		
		if(StringUtils.isBlank(meetingId) || "undefined".equals(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}


		String loginUserId = (String) req.getAttribute("loginUserId");
		List<String> list = userDataPermissionService.getDataPermissionDepartmentIdList(null,loginUserId);
		Meeting meeting = meetingService.selectMeetingById(meetingId);
		if(StringUtils.isNotBlank(meeting.getDepartmentId())&&!list.contains(meeting.getDepartmentId())) {
			return ResponseConstants.DATA_NOT_PERMITED;
		}


		List<Map<String,Object>> tickers = ticketService.selectTicketsByMeetingId(meetingId);
		String  teacherIdIds = "";
		if(meeting != null){
			teacherIdIds = meeting.getTeacherId();
			meeting.setObject(tickers);

			Date startTimeDate =  DateUtils.getDateByDateString(meeting.getStartTime());
			Date endTimeDate =  DateUtils.getDateByDateString(meeting.getEndTime());

			Date now = new Date();
			if(now.before(startTimeDate)){
				meeting.setMeetingStatus("未开始");
			}else if(now.after(endTimeDate)){
				meeting.setMeetingStatus("已结束");
			}else{
				meeting.setMeetingStatus("进行中");
			}

		}

		if(StringUtils.isNotBlank(teacherIdIds)){

			String[] idStrings = teacherIdIds.split(",");
			String teacherName = "";
			for(int i = 0 ;i<idStrings.length;i++){
				Map<String,Object> map = userService.selectUserById(idStrings[i].trim());
				if(map != null){
					teacherName += map.get("uname")+",";
				}
			}

			if(StringUtils.isNotBlank(teacherName)){
				teacherName = teacherName.substring(0, teacherName.lastIndexOf(","));
			}
			meeting.setTeacherName(teacherName);
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(meeting);
		
		return responseDbCenter;
	}


	/**
	 *
	 * @author: xiaoz
	 * @2017年5月17日
	 * @功能描述:根据会议ID获得票务会议相关信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectTicketMeetingById")
	public ResponseDbCenter selectTicketMeetingById(HttpServletRequest req,HttpServletResponse rsp){


		String meetingId = req.getParameter("meetingId");

		if(StringUtils.isBlank(meetingId) || "undefined".equals(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		Meeting meeting = meetingService.selectMeetingById(meetingId);
		List<Map<String,Object>> tickers = ticketService.selectTicketsByMeetingId(meetingId);
		String  teacherIdIds = "";
		if(meeting != null){
			teacherIdIds = meeting.getTeacherId();
			meeting.setObject(tickers);

			Date startTimeDate =  DateUtils.getDateByDateString(meeting.getStartTime());
			Date endTimeDate =  DateUtils.getDateByDateString(meeting.getEndTime());
			Date now = new Date();
			if(now.before(startTimeDate)){
				meeting.setMeetingStatus("未开始");
			}else if(now.after(endTimeDate)){
				meeting.setMeetingStatus("已结束");
			}else{
				meeting.setMeetingStatus("进行中");
			}
		}

		if(StringUtils.isNotBlank(teacherIdIds)){

			String[] idStrings = teacherIdIds.split(",");
			String teacherName = "";
			for(int i = 0 ;i<idStrings.length;i++){
				Map<String,Object> map = userService.selectUserById(idStrings[i].trim());
				if(map != null){
					teacherName += map.get("uname")+",";
				}
			}

			if(StringUtils.isNotBlank(teacherName)){
				teacherName = teacherName.substring(0, teacherName.lastIndexOf(","));
			}
			meeting.setTeacherName(teacherName);
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(meeting);

		return responseDbCenter;
	}

	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月17日 
	 * @功能描述:根据父行业、子行业、企业名称、企业资源、产品查询企业没有在该会议的企业
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectMeetingUserByParam")
	public ResponseDbCenter selectMeetingUserByParam(HttpServletRequest req,HttpServletResponse rsp){
		
		String token = req.getParameter("token");
		String departmentId = req.getParameter("departmentId");
		String meetingId = req.getParameter("meetingId");
		String content = req.getParameter("content");
		String parentIndustry = req.getParameter("parentIndustryId");
		String sonIndustryId = req.getParameter("sonIndustryId");
		String province = req.getParameter("province");
		String city = req.getParameter("city");
		String county = req.getParameter("county");
		String userlevel = req.getParameter("userlevel");
		String usertype = req.getParameter("usertype");
		String channel = req.getParameter("channel");
		String companyAdviser = req.getParameter("companyAdviser");
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		
		if(StringUtils.isBlank(meetingId) || StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize) || "undefined".equals(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		int  pageSizeInt = Integer.parseInt(pageSize);
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		
		String sqlContent = "";
		if(StringUtils.isNotBlank(content)){
			content = content.trim();
			sqlContent = " and (c.companyName like '%"+content+"%' or u.mobile like '%"+content+"%' or u.uname like '%"+content+"%')";
		}
		
		String parentIndustryStr = "";
		String sonIndustryStr = "";
		if(StringUtils.isNotBlank(parentIndustry)){
			parentIndustryStr = " and c.parentIndustryId like '%"+parentIndustry+"%' ";
		}
		
		if(StringUtils.isNotBlank(sonIndustryId)){
			String[] sonIndustryIds = sonIndustryId.split(",");
			String temp = "";
			for(int i =0 ;i<sonIndustryIds.length ; i++){
				temp +=  " c.sonIndustry like '%"+sonIndustryIds[i]+"%'  or ";
			}
			temp = temp.substring(0, temp.lastIndexOf("or"));
			sonIndustryStr = " and ("+temp+") ";
		}
		
		String provinceStr = "";
		if(StringUtils.isNotBlank(province)){
			provinceStr = " and ( c.province like '%"+province+"%' or c.province like '%"+province+"%' )";
		}
		
		String cityStr = "";
		if(StringUtils.isNotBlank(city)){
			cityStr = " and c.city like '%"+city+"%' ";
		}
		
		String countyStr = "";
		if(StringUtils.isNotBlank(county)){
			countyStr = " and c.county like '%"+county+"%' ";
		}
		
		String userlevelStr = "";
		if(StringUtils.isNotBlank(userlevel)){
			userlevelStr = " and c.userLevel = '"+userlevel.trim()+"' ";
		}
		
		String userTypeStr = "";
		if(StringUtils.isNotBlank(usertype)){
			userTypeStr = " and c.userType = '"+usertype+"' ";
		}
		
		String channelStr = "";
		if(StringUtils.isNotBlank(channel)){
			channelStr = " and c.channel = '"+channel+"' ";
		}
		
		String adviserStr = "";
		if(StringUtils.isNotBlank(companyAdviser)){
			adviserStr = " and c.companyAdviserId = '"+companyAdviser+"' ";
		}
		
		boolean superFlag = false;
		
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String, Object>  user =  userService.selectUserById(loginUserId);
		
		if(user != null){
			String roleName = (String)user.get("roleName");
			if("超级管理员".equals(roleName)){
				superFlag = true;
			}
		}
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("meetingId", meetingId);
		paramMap.put("content", sqlContent);
		paramMap.put("parentIndustryStr", parentIndustryStr);
		paramMap.put("sonIndustryStr", sonIndustryStr);
		paramMap.put("provinceStr", provinceStr);
		paramMap.put("cityStr", cityStr);
		paramMap.put("countyStr", countyStr);
		paramMap.put("userlevelStr", userlevelStr);
		paramMap.put("userTypeStr", userTypeStr);
		paramMap.put("channelStr", channelStr);
		paramMap.put("adviserStr", adviserStr);
		paramMap.put("currentPageInt", currentPageInt);
		paramMap.put("pageSizeInt", pageSizeInt);

		List<String> list = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId,loginUserId);

		paramMap.put("departmentIdList", list);
		List<Map<String,Object>> maps = userAttendMeetingService.selectExcluedCompanyUserMapsByMeetingId(paramMap);
		Integer total = userAttendMeetingService.selectTotalExcluedCompanyUserMapsByMeetingId(paramMap);
		List<String> companyIdList=new ArrayList<>();
		for(Map<String,Object> map : maps){
			String companyId = map.get("companyId") == null?"":map.get("companyId").toString();
			if(StringUtils.isNotBlank(companyId)){
				companyIdList.add(companyId);
			}
		}
		List<Map<String,Object>> relativeUserList=companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);


		Boolean permission = userService.getPrivatePermission(loginUserId, "meeting/private");
		for(Map<String,Object> map : maps){
			boolean mobileFlag = false;
			boolean relativeFlag=false;
			/*String parentIndustryid = map.get("parentIndustryId") == null?"":map.get("parentIndustryId").toString();*/
			String companyAdviserId = map.get("companyAdviserId") == null?"":(String)map.get("companyAdviserId");
			String companyDirectorId = map.get("companyDirectorId") == null?"":(String)map.get("companyDirectorId");
			String mapCompanyId = map.get("companyId") == null?"":map.get("companyId").toString();
			//根据公司id获取用户信息，可以有多个用户，但是取默认用户
			if(permission == false  && StringUtils.isBlank(companyAdviserId) && StringUtils.isBlank(companyDirectorId)){
				permission = true;
			}
			if(permission == false  && StringUtils.isNotBlank(companyAdviserId) && loginUserId.equals(companyAdviserId)){
				permission = true;
			}
			if(permission == false  && StringUtils.isNotBlank(companyDirectorId) && loginUserId.equals(companyDirectorId)){
				permission = true;
			}
			if(permission == false  ){
				for (int i = 0; i <relativeUserList.size() ; i++) { //判断相关人员权限
					if (loginUserId.equals(relativeUserList.get(i).get("userId")) && relativeUserList.get(i).get("companyId").equals(mapCompanyId)){
						permission=true;
					}
				}
			}
			//手机号、电话号码保密
			if(permission == false ){
				map.put("umobile", "***");
				map.put("mobile", "***");
				
				map.put("uphone", "***");
			}
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(maps);
		responseDbCenter.setTotalRows(total.toString());

		return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年6月5日
	 * @功能描述:根据会议获取企业家的订房信息
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectUsersHotelByMeetingId")
	public ResponseDbCenter selectUsersHotelByMeetingId(HttpServletRequest req,String token){


		/*
		select
	  u.uname,u.mobile,c.id as companyId,c.companyName,c.companyAdviserId,companyDirectorId,
      (select uname from dc_user where  id = c.companyAdviserId and status = 0) as companyAdviserName,
      (select uname from dc_user where id = c.companyDirectorId and status = 0) as companyDirectorName,h.startTime,h.endTime,mh.hotelName,TIMESTAMPDIFF(DAY,h.startTime,h.endTime)dates,
	  CASE when h.bigRoomNumber >0 and h.roomNumber >0 then CONCAT(CONCAT('标准间 ',h.roomNumber,' 间'),' ',CONCAT('大床房 ',h.bigRoomNumber,' 间')) when h.bigRoomNumber >0 then CONCAT('标准间 ',h.bigRoomNumber,' 间') when h.roomNumber >0 then CONCAT('标准间 ',h.roomNumber,' 间') end room
<!-- 		CASE when (select r.roleName from dc_user u LEFT JOIN dc_sys_role r on u.roleId = r.id  where u.id=#{userId}) ='超级管理员' then u.mobile when (c.companyAdviserId != '' and (select id from dc_user where id = c.companyAdviserId)=#{userId}) or (c.companyDirectorId != '' and (select id from dc_user where id = c.companyDirectorId)=#{userId})  then u.mobile else '***' end  mobile -->
	 from  dc_user_hotel  h
		LEFT JOIN dc_meeting_user mu on  h.meetingUserId = mu.id
		LEFT join dc_company c on c.id = mu.companyId
		LEFT JOIN dc_user u on u.id = mu.userId
		LEFT JOIN dc_meeting_hotel mh on mh.id = h.hotelId
	 where mu.meetingId = #{meetingId} and u.status = 0 and c.status = 0

	<if test="content != null and content != ''">
    	and (u.mobile like CONCAT('%',#{content},'%') or u.uname like CONCAT('%',#{content},'%'))
    </if>

	 limit #{currentPageInt},#{pageSizeInt}
		 */


		String meetingId = req.getParameter("meetingId");
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		String content = req.getParameter("content");

		if(StringUtils.isBlank(meetingId) || StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize) || "undefined".equals(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		int  pageSizeInt = Integer.parseInt(pageSize);
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;

		//现在手机号隐藏权限先全部放开，任何人可以看

		/*boolean superFlag = false;
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String, Object>  user =  userService.selectUserById(loginUserId);
		if(user != null){
			String roleName = (String)user.get("roleName");
			if("超级管理员".equals(roleName)){
				superFlag = true;
			}
		}*/

		String userId = req.getSession().getAttribute(token).toString();
		List<Map<String,Object>> maps = userAttendMeetingService.selectUsersHotelByMeetingId(userId,meetingId,content,currentPageInt,pageSizeInt);
		Integer total = userAttendMeetingService.selectTotalUsersHotelByMeetingId(meetingId,content);

		//hotelNumber:[{"roomType":"普通房","roomNumber":"2"},{"roomType":"大床房","roomNumber":"2"}]
		//h.roomNumber,h.bigRoomNumber
		//封装成前端想要的结构

		for(Map<String,Object> map : maps){
			String roomNumber = map.get("roomNumber") == null?"":map.get("roomNumber").toString();
			String bigRoomNumber = map.get("bigRoomNumber") == null?"":map.get("bigRoomNumber").toString();
			List<Map<String,Object>> hotelNumber=new ArrayList<>();
			if(StringUtils.isNotBlank(roomNumber)){
				Map<String,Object> mapp = new HashMap<>();
				mapp.put("roomType","普通房");
				mapp.put("roomNumber",roomNumber);
				hotelNumber.add(mapp);
			}else{
				Map<String,Object> mapp = new HashMap<>();
				mapp.put("roomType","普通房");
				mapp.put("roomNumber","0");
				hotelNumber.add(mapp);
			}
			if(StringUtils.isNotBlank(bigRoomNumber)){
				Map<String,Object> mapp = new HashMap<>();
				mapp.put("roomType","大床房");
				mapp.put("roomNumber",bigRoomNumber);
				hotelNumber.add(mapp);
			}else{
				Map<String,Object> mapp = new HashMap<>();
				mapp.put("roomType","大床房");
				mapp.put("roomNumber","0");
				hotelNumber.add(mapp);
			}
			map.put("hotelNumber",hotelNumber);
		}

		/*List<String> companyIdList=new ArrayList<>();
		for(Map<String,Object> map : maps){
			String companyId = map.get("companyId") == null?"":map.get("companyId").toString();
			if(StringUtils.isNotBlank(companyId)){
				companyIdList.add(companyId);
			}
		}
		List<Map<String,Object>> relativeUserList=companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);

		for(Map<String,Object> map : maps){
			boolean mobileFlag = false;
			boolean relativeFlag=false;
			String companyAdviserId = map.get("companyAdviserId") == null?"":map.get("companyAdviserId").toString();
			String companyDirectorId = map.get("companyDirectorId") == null?"":map.get("companyDirectorId").toString();
			String mapCompanyId = map.get("companyId") == null?"":map.get("companyId").toString();

			if(StringUtils.isBlank(companyAdviserId) && StringUtils.isBlank(companyDirectorId)){
				mobileFlag = true;
			}
			if(StringUtils.isNotBlank(companyAdviserId) && loginUserId.equals(companyAdviserId)){
				mobileFlag = true;
			}
			if(StringUtils.isNotBlank(companyDirectorId) && loginUserId.equals(companyDirectorId)){
				mobileFlag = true;
			}
			for (int i = 0; i <relativeUserList.size() ; i++) { //判断相关人员权限
				if (loginUserId.equals(relativeUserList.get(i).get("userId")) && relativeUserList.get(i).get("companyId").equals(mapCompanyId)){
					relativeFlag=true;
				}
			}
			*//*
			 * 手机号、电话号码保密
			 *//*
			if(!mobileFlag && !relativeFlag && !superFlag && permission == false ){
				map.put("mobile", "***");
			}
		}*/

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(maps);
		responseDbCenter.setTotalRows(total.toString());

		return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年6月6日
	 * @功能描述:根据
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectMeetingProjectByMeetingId")
	public ResponseDbCenter selectMeetingProjectByMeetingId(HttpServletRequest req,HttpServletResponse rsp){


		String token = req.getParameter("token");
		String meetingId = req.getParameter("meetingId");
		String content = req.getParameter("content");
		String pparentIndustryId = req.getParameter("parentIndustryId");
		String sonIndustryId = req.getParameter("sonIndustryId");
		String province = req.getParameter("province");
		String city = req.getParameter("city");
		String county = req.getParameter("county");
		String projectLevelId = req.getParameter("projectLevelId");
		String companyAdviser = req.getParameter("companyAdviser");
		String director = req.getParameter("companyDirector");
		String mgroup = req.getParameter("mgroup");
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");

		if(StringUtils.isBlank(meetingId) || StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize) || "undefined".equals(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		int  pageSizeInt = Integer.parseInt(pageSize);
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;

		String sqlContent = "";
		if(StringUtils.isNotBlank(content)){
			sqlContent = " and (c.companyName like '%"+content+"%' or c.label like '%"+content+"%' or u.mobile like '%"+content+"%')";
		}
		String parentIndustryStr = "";
		String sonIndustryStr = "";
		if(StringUtils.isNotBlank(pparentIndustryId)){
			parentIndustryStr = " and p.parentIndustryId like '%"+pparentIndustryId+"%' ";
		}
		if(StringUtils.isNotBlank(sonIndustryId)){
			String[] sonIndustryIds = sonIndustryId.split(",");
			String temp = "";
			for(int i =0 ;i<sonIndustryIds.length ; i++){
				temp +=  " c.sonIndustry like '%"+sonIndustryIds[i]+"%'  or ";
			}
			temp = temp.substring(0, temp.lastIndexOf("or"));
			sonIndustryStr = " and ("+temp+") ";
		}
		String provinceStr = "";
		if(StringUtils.isNotBlank(province)){
			provinceStr = " and ( c.province like '%"+province+"%' or c.address like '%"+province+"%' )";
		}
		String cityStr = "";
		if(StringUtils.isNotBlank(city)){
			cityStr = " and c.city like '%"+city+"%' ";
		}
		String countyStr = "";
		if(StringUtils.isNotBlank(county)){
			countyStr = " and c.county like '%"+county+"%' ";
		}
		String plevelStr = "";
		if(StringUtils.isNotBlank(projectLevelId)){
			plevelStr = " and p.projectLevelId = '"+projectLevelId.trim()+"' ";
		}
		String companyAdviserStr = "";
		/*
		c.companyAdviserId,p.adviserId)  and status = 0) as companyAdviserName,
    (select uname from dc_user where id = IF(p.directorId is null or p.directorId = '',c.companyDirectorId

    IF(p.directorId is null or p.directorId = '',c.companyDirectorId,p.directorId) =
		 */
		if(StringUtils.isNotBlank(companyAdviser)){
			companyAdviserStr = " and IF(p.adviserId is null or p.adviserId = '',c.companyAdviserId,p.adviserId) = '"+companyAdviser+"' ";
		}
		String directorStr = "";
		if(StringUtils.isNotBlank(director)){
			directorStr = " and IF(p.directorId is null or p.directorId = '',c.companyDirectorId,p.directorId) = '"+director+"' ";
		}
		String mgroupStr = "";
		if(StringUtils.isNotBlank(mgroup)){
			mgroupStr = " and mu.mgroup = '"+mgroup+"'";
		}
		boolean superFlag = false;
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String, Object>  user =  userService.selectUserById(loginUserId);
		if(user != null){
			String roleName = (String)user.get("roleName");
			if("超级管理员".equals(roleName)){
				superFlag = true;
			}
		}

		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("meetingId",meetingId);
		paramMap.put("content", sqlContent);
		paramMap.put("parentIndustryStr", parentIndustryStr);
		paramMap.put("sonIndustryStr", sonIndustryStr);
		paramMap.put("provinceStr", provinceStr);
		paramMap.put("cityStr", cityStr);
		paramMap.put("countyStr", countyStr);
		paramMap.put("plevelStr", plevelStr);
		paramMap.put("companyAdviserStr", companyAdviserStr);
		paramMap.put("directorStr", directorStr);
		paramMap.put("mgroupStr", mgroupStr);
		paramMap.put("currentPageInt", currentPageInt);
		paramMap.put("pageSizeInt", pageSizeInt);

		Integer total = 0;
		List<Map<String,Object>> maps = userAttendMeetingService.selectMeetingProjectsMapsByMeetingId(paramMap);
		total = userAttendMeetingService.selectTotalMeetingProjectsMapsByMeetingId(paramMap);

		List<String> companyIdList=new ArrayList<>();
		for(Map<String,Object> map : maps){
			String companyId = map.get("companyId") == null?"":map.get("companyId").toString();
			if(StringUtils.isNotBlank(companyId)){
				companyIdList.add(companyId);
			}
		}
		List<Map<String,Object>> relativeUserList=companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);
		Boolean permission = userService.getPrivatePermission(loginUserId, "meeting/private");
		for(Map<String,Object> map : maps){
			boolean mobileFlag = false;
			boolean relativeFlag=false;
			String parentIndustryId = map.get("parentIndustryId") == null?"":map.get("parentIndustryId").toString();
			String companyId = map.get("companyId") == null?"":map.get("companyId").toString();
			String companyAdviserId = map.get("adviserId") == null?"":map.get("adviserId").toString();
			String companyDirectorId = map.get("directorId") == null?"":map.get("directorId").toString();

			if(permission == false  && StringUtils.isBlank(companyAdviserId) && StringUtils.isBlank(companyDirectorId)){
				permission = true;
			}
			if(permission == false  && StringUtils.isNotBlank(companyAdviserId) && loginUserId.equals(companyAdviserId)){
				permission = true;
			}
			if(permission == false  && StringUtils.isNotBlank(companyDirectorId) && loginUserId.equals(companyDirectorId)){
				permission = true;
			}
			if(permission == false  ){
				for (int i = 0; i <relativeUserList.size() ; i++) { //判断相关人员权限
					if (loginUserId.equals(relativeUserList.get(i).get("userId")) && relativeUserList.get(i).get("companyId").equals(companyId)){
						permission=true;
					}
				}
			}
			//手机号、电话号码保密
			if(permission == false ){
				map.put("umobile", "***");
				map.put("mobile", "***");
				map.put("uphone", "***");
			}
			//将参会的企业人返回前端
			List<Map<String,Object>> userInfoMaps = userAttendMeetingService.selectMeetingUserMapsByCompanyId(companyId,meetingId);
			List<Map<String,Object>> newUserInfoMaps = new ArrayList<>();
			for(Map<String,Object> userMap : userInfoMaps){
				if(userMap != null){
					newUserInfoMaps.add(userMap);
				}
			}
			map.put("userInfos",newUserInfoMaps);
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(maps);
		responseDbCenter.setTotalRows(total.toString());

		return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年6月5日
	 * @功能描述:编辑用户酒店信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateUsersHotel")
	public ResponseDbCenter updateUsersHotel(@RequestBody String result) throws Exception{

	List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(result,Object.class);

	for(Map<String, Object> map : results){

		String hotelId = (String) map.get("hotelId");
		String meetingUserId = (String) map.get("meetingUserId");
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");
		String hotelName = (String) map.get("hotelName");
		String content = (String) map.get("content");
		String roomType = (String) map.get("roomType");
		try {

			Hotel hotel =  new Hotel();

			if(hotelId == null){

				hotel.setStartTime(startTime);
				hotel.setEndTime(endTime);
				hotel.setMeetingUserId(meetingUserId);
				hotel.setContent(content);

				//hotelService.insertHotel(hotel);

			}else{

				hotel.setId(hotelId);
				hotel.setContent(content);
				hotel.setStartTime(startTime);
				hotel.setEndTime(endTime);

				hotelService.updateHotelById(hotel);

			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}


	ResponseDbCenter responseDbCenter = new ResponseDbCenter();

	return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年5月17日
	 * @功能描述:添加参会企业
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveMeetingUser")
	public ResponseDbCenter saveMeetingUser(@RequestBody String result) throws Exception{

	List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(result,Object.class);

	for(Map<String, Object> map : results){

		Map<String, Object> addressMap =  (Map<String,Object>)map.get("address");
		Map<String, Object> userInfoMap =  (Map<String,Object>)map.get("userInfo");
		Map<String, Object> companyMap =  (Map<String,Object>)map.get("company");

		String meetingId = (String) map.get("meetingId");

//		String status = (String) map.get("status");
		String mgroup = (String) map.get("mgroup");
		String manager = (String) map.get("manager");

		String uname = "";
		String mobile = "";
		if(userInfoMap != null){

			uname = (String) userInfoMap.get("uname");
			mobile = (String) userInfoMap.get("mobile");
		}

		String address = "";
		if(addressMap != null){
			address = (String) addressMap.get("address");
		}

		String companyId = "";
		if(companyMap != null){

			companyId = (String) companyMap.get("id");
		}


		String userId = UUID.randomUUID().toString();

		try {

			if(StringUtils.isNotBlank(manager) || StringUtils.isNotBlank(mgroup)){

				UserAttendMeeting userAttendMeeting = new UserAttendMeeting();

				userAttendMeeting.setStatus("已报名");
				userAttendMeeting.setManager(manager);
				userAttendMeeting.setMgroup(mgroup);
				userAttendMeeting.setCompanyId(companyId);
				userAttendMeeting.setUserId(userId);
				userAttendMeeting.setMeetingId(meetingId);

				userAttendMeetingService.saveUserMeeting(userAttendMeeting);
			}


			if(StringUtils.isNotBlank(mobile) || StringUtils.isNotBlank(uname)){

				Map<String,Object> userInfo = new HashMap();
				userInfo.put("userId", userId);
				userInfo.put("mobile", mobile);
				userInfo.put("uname", uname);
				userInfo.put("companyId", companyId);

				userInfo.put("status", 0);


				userInfoService.insertUserInfo(userInfo);
			}

			if(StringUtils.isNotBlank(address)){

				Address userAddress = new Address();

				userAddress.setUserId(userId);
				userAddress.setAddress(address);

				addressService.updateAddressInfoByUserId(userAddress);
			}

			/*
			 * 将该企业维护到solr库中
			 */
			solrService.updateCompanyIndex(companyId);



		} catch (Exception e) {

			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}


	ResponseDbCenter responseDbCenter = new ResponseDbCenter();

	return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年5月17日
	 * @功能描述:企业分组
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/groupMeetingUser")
	public ResponseDbCenter groupMeetingUser(@RequestBody String result) throws Exception{

	List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(result,Object.class);

	for(Map<String, Object> map : results){

		Map<String, Object> addressMap =  (Map<String,Object>)map.get("address");
		Map<String, Object> userInfoMap =  (Map<String,Object>)map.get("userInfo");
		Map<String, Object> companyMap =  (Map<String,Object>)map.get("company");

		String meetingId = (String) map.get("meetingId");

//		String status = (String) map.get("status");
		String mgroup = (String) map.get("mgroup");
		String manager = (String) map.get("manager");
		String uname = (String) userInfoMap.get("uname");
		String mobile = (String) userInfoMap.get("mobile");
		String address = (String) addressMap.get("address");
		String companyId = (String) companyMap.get("id");

		String userId = UUID.randomUUID().toString();

		if(StringUtils.isNotBlank(manager) || StringUtils.isNotBlank(mgroup)){

			UserAttendMeeting userAttendMeeting = new UserAttendMeeting();
//			userAttendMeeting.setId(id);
			userAttendMeeting.setStatus("已报名");
			userAttendMeeting.setManager(manager);
			userAttendMeeting.setMgroup(mgroup);
			userAttendMeeting.setCompanyId(companyId);
			userAttendMeeting.setUserId(userId);
			userAttendMeeting.setMeetingId(meetingId);

			userAttendMeetingService.saveUserMeeting(userAttendMeeting);
		}


		if(StringUtils.isNotBlank(mobile) || StringUtils.isNotBlank(uname)){




			Map<String,Object> userInfo = new HashMap();
			userInfo.put("userId", userId);
			userInfo.put("mobile", mobile);
			userInfo.put("uname", uname);
			userInfo.put("companyId", companyId);

			userInfo.put("status", 0);


			userInfoService.insertUserInfo(userInfo);
		}

		if(StringUtils.isNotBlank(address)){

			Address userAddress = new Address();

			userAddress.setUserId(userId);
			userAddress.setAddress(address);
			try {

				addressService.updateAddressInfoByUserId(userAddress);

			} catch (Exception e) {
				System.out.println(e);
				return ResponseConstants.FUNC_MODULE_SERVERERROR;
			}

		}

		/*
		 * 将该企业维护到solr库中
		 */
		solrService.updateCompanyIndex(companyId);


	}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年5月18日
	 * @功能描述:保存企业用户信息到会议，但是没有分组
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveMeetingCompany")
	public ResponseDbCenter saveMeetingCompany(@RequestBody String result) throws Exception{

	List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(result,Object.class);

	for(Map<String, Object> map : results){


		String meetingId = (String) map.get("meetingId");
		String companyId = (String) map.get("companyId");
		String userId = (String) map.get("userId");
		String learnStatus = map.get("learnStatus")==null?null:(String) map.get("learnStatus");

		try {

			if(userId == null){

				continue;
			}

			UserAttendMeeting userAttendMeeting = new UserAttendMeeting();

			userAttendMeeting.setStatus("已报名");
			userAttendMeeting.setCompanyId(companyId);
			userAttendMeeting.setUpdateTime(new Date());

			Integer learnStatusInt = "新训".equals(learnStatus)?0:1;

			userAttendMeeting.setLearnStatus(learnStatusInt);
			userAttendMeeting.setUserId(userId);

			if(meetingId != null){

				userAttendMeeting.setMeetingId(meetingId);
			}

			userAttendMeetingService.saveUserMeeting(userAttendMeeting);

			/*
			 * 将该企业维护到solr库中
			 */
			solrService.updateCompanyIndex(companyId);



		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.MEETING_ATTEND_USER_REPEAT);
		}catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}

	}

	ResponseDbCenter responseDbCenter = new ResponseDbCenter();

	return responseDbCenter;
  }

	/**
	 *
	 * @author: xiaoz
	 * @2017年6月10日
	 * @功能描述:手机端添加未邀请企业，状态是未审核的，通过这个接口将状态改成已报名
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMeetingUserStatus")
	public ResponseDbCenter updateMeetingUserStatus(HttpServletRequest req,HttpServletResponse rsp) throws Exception{


		String ids = req.getParameter("ids");
		String status = req.getParameter("status");

		if(StringUtils.isBlank(ids) || StringUtils.isBlank(status)){

			return ResponseConstants.MISSING_PARAMTER;
		}


		String[] idStrings = ids.split(",");
		String[] statusStrings = status.split(",");

		if(idStrings.length != statusStrings.length){

			return ResponseConstants.ILLEGAL_PARAM;
		}

		try {

			for(int i = 0;i<idStrings.length;i++){

				userAttendMeetingService.updateMeetingUserStatus(idStrings[i],statusStrings[i]);



			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}


		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年6月19日
	 * @功能描述:参会企业人数据导出
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportMeetingUsers")
	public ResponseDbCenter exportMeetingUsers(HttpServletRequest req,HttpServletResponse rsp){


		String token = req.getParameter("token");
		String content = req.getParameter("content");
		String meetingId = req.getParameter("meetingId");
		String path = req.getParameter("path");
		String companyAdviserr = req.getParameter("companyAdviser");
		String companyDirectorr = req.getParameter("companyDirector");
		String mgroupr = req.getParameter("mgroup");
		String userTyper = req.getParameter("userType");

		if(StringUtils.isBlank(token)){
			return ResponseConstants.FUNC_USER_NOTOKEN;
		}
		String userId = null;
		try {
			userId = req.getSession().getAttribute(token).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

        if(StringUtils.isBlank(userId)){
        	return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
        }

		int  pageSizeInt = 10000000;
		int  currentPageInt = 0;
		if(StringUtils.isBlank(meetingId) || "undefined".equals(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		if(StringUtils.isNotBlank(content)){
			content = " and (c.companyName like '%"+content+"%' or u.mobile like '%"+content+"%' or u.uname like '%"+content+"%') ";
		}

		String companyAdviserStr = "";
		if(StringUtils.isNotBlank(companyAdviserr)){
			companyAdviserStr = " and c.companyAdviser = '"+companyAdviserr+"'";
		}

		String companyDirectorStr = "";
		if(StringUtils.isNotBlank(companyDirectorr)){
			companyDirectorStr = " and c.companyDirector = '"+companyDirectorr+"'";
		}

		String mgroupStr = "";
		if(StringUtils.isNotBlank(mgroupr)){
			mgroupStr = " and m.mgroup = '"+mgroupr+"'";
		}

		String userTypeStr = "";
		if(StringUtils.isNotBlank(userTyper)){
			userTypeStr = " and c.userType = '"+userTyper+"'";
		}

		List<HashMap<String,Object>> userAttendMeetingMaps = null;
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("meetingId",meetingId);
		paramMap.put("content",content);
		paramMap.put("needCheck","");
		paramMap.put("companyAdviserStr","");
		paramMap.put("companyDirectorStr","");
		paramMap.put("userTypeStr","");
		paramMap.put("mgroupStr","");
		paramMap.put("currentPageInt",currentPageInt);
		paramMap.put("pageSizeInt",pageSizeInt);

		boolean superFlag = false;
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String, Object>  user =  userService.selectUserById(loginUserId);

		if(user != null){
			String roleName = (String)user.get("roleName");
			if("超级管理员".equals(roleName)){
				superFlag = true;
			}
		}

		userAttendMeetingMaps = userAttendMeetingService.selectUserInfoMapByMeetingId(paramMap);
		path =PropertiesUtil.FILE_UPLOAD_PATH+userId+"/企业签到表.xlsx";
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("0");
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell0 = row.createCell(0);
        cell0.setCellValue("序号");
        XSSFCellStyle style = wb.createCellStyle();
        Font fontHeader=wb.createFont();

        /*
         * 企业名称、所属行业、经营范围、一句话描述企业、近一年营收、近一年利润、公司类型、公司规模、参会需求
         */
    	//字体号码
    	fontHeader.setFontHeightInPoints((short)12);
    	fontHeader.setBold(true);
    	//字体名称
    	fontHeader.setFontName("宋体");
    	style.setFont(fontHeader);
        cell0.setCellStyle(style);

        XSSFCell cell1 = row.createCell(1);
        cell1.setCellValue("组别");
        cell1.setCellStyle(style);

        XSSFCell cell2 = row.createCell(2);
        cell2.setCellValue("顾问");
        cell2.setCellStyle(style);

        XSSFCell cell3 = row.createCell(3);
        cell3.setCellValue("分组总监");
        cell3.setCellStyle(style);

        XSSFCell cell4 = row.createCell(4);
        cell4.setCellValue("省份");
        cell4.setCellStyle(style);

        XSSFCell cell5 = row.createCell(5);
        cell5.setCellValue("城市");
        cell5.setCellStyle(style);

        XSSFCell cell6 = row.createCell(6);
        cell6.setCellValue("客户类型");
        cell6.setCellStyle(style);

        XSSFCell cell7 = row.createCell(7);
        cell7.setCellValue("企业名称");
        cell7.setCellStyle(style);

        XSSFCell cell8 = row.createCell(8);
        cell8.setCellValue("姓名");
        cell8.setCellStyle(style);

        XSSFCell cell9 = row.createCell(9);
        cell9.setCellValue("电话");
        cell9.setCellStyle(style);

        XSSFCell cell10 = row.createCell(10);
        cell10.setCellValue("渠道来源");
        cell10.setCellStyle(style);

        XSSFCell cell11 = row.createCell(11);
        cell11.setCellValue("成交金额（万）");
        cell11.setCellStyle(style);

        XSSFCell cell12 = row.createCell(12);
        cell12.setCellValue("报名日期");
        cell12.setCellStyle(style);

        XSSFCell cell13 = row.createCell(13);
        cell13.setCellValue("所属行业");
        cell13.setCellStyle(style);

        /*
         * 经营范围、一句话描述企业、近一年营收、近一年利润、公司类型、公司规模
         */
        XSSFCell cell14 = row.createCell(14);
        cell14.setCellValue("主营业务");
        cell14.setCellStyle(style);

        XSSFCell cell15 = row.createCell(15);
        cell15.setCellValue("一句话描述企业");
        cell15.setCellStyle(style);

        XSSFCell cell16 = row.createCell(16);
        cell16.setCellValue("近一年营收");
        cell16.setCellStyle(style);

        XSSFCell cell17 = row.createCell(17);
        cell17.setCellValue("近一年利润");
        cell17.setCellStyle(style);

        XSSFCell cell18 = row.createCell(18);
        cell18.setCellValue("公司类型");
        cell18.setCellStyle(style);

//        XSSFCell cell12 = row.createCell(12);
//        cell12.setCellValue("公司规模");
//        cell12.setCellStyle(style);

        XSSFCell cell19 = row.createCell(19);
        cell19.setCellValue("公司性质");
        cell19.setCellStyle(style);

        XSSFCell cell20 = row.createCell(20);
        cell20.setCellValue("参会需求");
        cell20.setCellStyle(style);

        XSSFCell cell21 = row.createCell(21);
        cell21.setCellValue("详细需求");
        cell21.setCellStyle(style);

        XSSFCell cell22 = row.createCell(22);
        cell22.setCellValue("新训/复训");
        cell22.setCellStyle(style);

//        XSSFCell cell19 = row.createCell(19);
//        cell19.setCellValue("签到时间");
//        cell19.setCellStyle(style);

        XSSFCell cell23 = row.createCell(23);
        cell23.setCellValue("是否订酒店");
        cell23.setCellStyle(style);

        int i= 0;

		List<String> companyIdList=new ArrayList<>();
		for(HashMap<String,Object> map : userAttendMeetingMaps){
			String companyId = map.get("companyId") == null?"":map.get("companyId").toString();
			if(StringUtils.isNotBlank(companyId)){
				companyIdList.add(companyId);
			}
		}
		List<Map<String,Object>> relativeUserList=companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);

		Boolean permission = userService.getPrivatePermission(loginUserId, "meeting/private");
		for(HashMap<String,Object> map : userAttendMeetingMaps){

			boolean mobileFlag = false;
			boolean relativeFlag=false;

			String mapCompanyId = map.get("companyId") == null?"":map.get("companyId").toString();
			String companyAdviserId = map.get("companyAdviserId") == null?"":map.get("companyAdviserId").toString();
			String companyDirectorId = map.get("companyDirectorId") == null?"":map.get("companyDirectorId").toString();
			String hotelId = map.get("hotelId") == null?null:(String)map.get("hotelId");
			String paymentMoney = map.get("paymentMoney") == null?"":map.get("paymentMoney").toString();

			if(StringUtils.isBlank(companyAdviserId) && StringUtils.isBlank(companyDirectorId)){
				mobileFlag = true;
			}

			if(StringUtils.isNotBlank(companyAdviserId) && loginUserId.equals(companyAdviserId)){
				mobileFlag = true;
			}

			if(StringUtils.isNotBlank(companyDirectorId) && loginUserId.equals(companyDirectorId)){
				mobileFlag = true;
			}

			for (int v = 0; v <relativeUserList.size() ; v++) { //判断相关人员权限
				if (loginUserId.equals(relativeUserList.get(v).get("userId")) && relativeUserList.get(v).get("companyId").equals(mapCompanyId)){
					relativeFlag=true;
				}
			}
			//手机号、电话号码保密
			if(permission == false ){
				map.put("umobile", "***");
				map.put("mobile", "***");
				map.put("uphone", "***");
			}

			if(StringUtils.isNotBlank(paymentMoney)) {
				Double paymentMoneyDouble = new Double(paymentMoney);
				double l = 10000;
				DecimalFormat df = new DecimalFormat("0.0000");
				String paymentMoneyStr = df.format(paymentMoneyDouble.intValue() / l);
				if (StringUtils.isNotBlank(paymentMoneyStr)) {
					map.put("paymentMoney", paymentMoneyStr);
				}
			}
			/*
			 * 手机号、电话号码保密
			 */
			if(!mobileFlag && !relativeFlag && !superFlag && permission == false ){
				map.put("umobile", "***");
				map.put("mobile", "***");
//				map.put("phone", "***");
			}

			if(hotelId != null){

				map.put("haveHotel","是");
			}else{

				map.put("haveHotel","否");
			}

			i++;

			map.put("xuhao",i+"");

			row = sheet.createRow(i);
			XSSFCell cell = null;

			for (Map.Entry<String, Object> entry : map.entrySet()) {

				 switch(entry.getKey()){

				 	case "xuhao":

				 	String xuhao = entry.getValue() == null?"":entry.getValue().toString();
				 	cell = row.createCell(0);
				    cell.setCellValue(xuhao);

				    break;

				    case "mgroup":

				    String mgroup = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(1);
				    cell.setCellValue(mgroup);
			    	break;

				    case "companyAdviserName":

				    String companyAdviserName = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(2);
				    cell.setCellValue(companyAdviserName);
			    	break;

				    case "companyDirectorName":

					    String director = entry.getValue() == null?"":entry.getValue().toString();
					    cell = row.createCell(3);
					    cell.setCellValue(director);
				    	break;

				    case "userType":

					    String userType = entry.getValue() == null?"":entry.getValue().toString();
					    cell = row.createCell(6);
					    cell.setCellValue(userType);
				    	break;

				    case "companyName":

					    String company_name = entry.getValue() == null?"":entry.getValue().toString();
					    cell = row.createCell(7);
					    cell.setCellValue(company_name);
				    	break;

				    case "parentIndustryName":

					    String parentIndustryName = entry.getValue() == null?"":entry.getValue().toString();
					    String sonIndustryId = map.get("sonIndustry") == null?"":map.get("sonIndustry").toString();

					    cell = row.createCell(13);
					    cell.setCellValue(parentIndustryName+">"+sonIndustryId);
				    	break;

				    case "manageScope":

					    String manageScope = entry.getValue() == null?"":entry.getValue().toString();

					    cell = row.createCell(14);
					    cell.setCellValue(manageScope);
				    	break;

				    case "companyDesc":

					    String company_desc = entry.getValue() == null?"":entry.getValue().toString();

					    cell = row.createCell(15);
					    cell.setCellValue(company_desc);
				    	break;

				    case "annualSalesVolume":

					    String annual_sales_volume = entry.getValue() == null?"":entry.getValue().toString();

					    cell = row.createCell(16);
					    cell.setCellValue(annual_sales_volume);
				    	break;

				    case "annualProfit":

					    String annual_profit = entry.getValue() == null?"":entry.getValue().toString();

					    cell = row.createCell(17);
					    cell.setCellValue(annual_profit);
				    	break;

				    case "company_type":

					    String company_type = entry.getValue() == null?"":entry.getValue().toString();

					    cell = row.createCell(18);
					    cell.setCellValue(company_type);
				    	break;

				    case "companyProperty":

					    String companyProperty = entry.getValue() == null?"":entry.getValue().toString();

					    cell = row.createCell(19);
					    cell.setCellValue(companyProperty);
				    	break;


				    case "need":

					    String need = entry.getValue() == null?"":entry.getValue().toString();

					    cell = row.createCell(20);
					    cell.setCellValue(need);
				    	break;
				   case "needDetail":

					    String needDetail = entry.getValue() == null?"":entry.getValue().toString();

					    cell = row.createCell(21);
					    cell.setCellValue(needDetail);
				    	break;

				    case "province":

					    String province = entry.getValue() == null?"":entry.getValue().toString();

					    if(province.indexOf("省")>-1){

					    	province = province.substring(0, province.lastIndexOf("省"));

					    }else if (province.indexOf("市")>-1) {

					    	province = province.substring(0, province.lastIndexOf("市"));
						}

					    cell = row.createCell(4);
					    cell.setCellValue(province);
				    	break;

				    case "city":

					    String city = entry.getValue() == null?"":entry.getValue().toString();

					    if(city.indexOf("市")>-1){

				    		city = city.substring(0, city.lastIndexOf("市"));

					    }else if (city.indexOf("省")>-1) {

					    	city = city.substring(0, city.lastIndexOf("省"));
						}
					    cell = row.createCell(5);
					    cell.setCellValue(city);
				    	break;



				    case "uname":

					    String uname = entry.getValue() == null?"":entry.getValue().toString();
					    cell = row.createCell(8);
					    cell.setCellValue(uname);
				    	break;

				    case "umobile":

					    String umobile = entry.getValue() == null?"":entry.getValue().toString();
					    cell = row.createCell(9);
					    cell.setCellValue(umobile);
				    	break;


				    case "channel":

					    String channel = entry.getValue() == null?"":entry.getValue().toString();
					    cell = row.createCell(10);
					    cell.setCellValue(channel);
				    	break;


				    case "enrollTime":

					    String enrollDate = entry.getValue() == null?"":entry.getValue().toString();
					    cell = row.createCell(12);
					    cell.setCellValue(enrollDate);
				    	break;

					 case "paymentMoney":

						    String dealMoney = entry.getValue() == null?"":entry.getValue().toString();
						    cell = row.createCell(11);
						    cell.setCellValue(dealMoney);
					 	break;


					 case "learnStatus":

						    String learnStatuss = entry.getValue() == null?"":entry.getValue().toString();
						    cell = row.createCell(22);
						    cell.setCellValue(learnStatuss);
					 	break;

					 case "haveHotel":

						    String haveHotel = entry.getValue() == null?"":entry.getValue().toString();
						    cell = row.createCell(23);
						    cell.setCellValue(haveHotel);
					 	break;

			    	default: break;

				 }
			}


		}

		 FileOutputStream outputStream = null;

		try {

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
	        outputStream.close();

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

			System.out.println(e);
			return ResponseConstants.ERROR_EXPORTDATA_EXCELL;
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		responseDbCenter.setResModel(PropertiesUtil.FILE_HTTP_PATH+userId+"/企业签到表.xlsx");

		return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年6月19日
	 * @功能描述:参会企业人住房信息数据导出
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportUsersHotel")
	public ResponseDbCenter exportUsersHotel(HttpServletRequest req,String token){



		try {
			String meetingId = req.getParameter("meetingId");
			if(StringUtils.isBlank(meetingId) || "undefined".equals(meetingId)){
				return ResponseConstants.MISSING_PARAMTER;
			}
			String userId = req.getSession().getAttribute(token).toString();
			int  pageSizeInt = 1000000;
			int  currentPageInt = 0;

			boolean superFlag = false;
			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String, Object>  user =  userService.selectUserById(loginUserId);
			if(user != null){
				String roleName = (String)user.get("roleName");
				if("超级管理员".equals(roleName)){
					superFlag = true;
				}
			}

			List<Map<String,Object>> maps = userAttendMeetingService.selectUsersHotelByMeetingId(userId,meetingId,"",currentPageInt,pageSizeInt);

			/*List<String> companyIdList=new ArrayList<>();
			for(Map<String,Object> map : maps){
				String companyId = map.get("companyId") == null?"":map.get("companyId").toString();
				if(StringUtils.isNotBlank(companyId)){
					companyIdList.add(companyId);
				}
			}
			List<Map<String,Object>> relativeUserList=companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);

			for(Map<String,Object> map : maps) {
				boolean mobileFlag = false;
				boolean relativeFlag = false;
				String companyAdviserId = map.get("companyAdviserId") == null ? "" : map.get("companyAdviserId").toString();
				String companyDirectorId = map.get("companyDirectorId") == null ? "" : map.get("companyDirectorId").toString();
				String mapCompanyId = map.get("companyId") == null ? "" : map.get("companyId").toString();

				if (StringUtils.isBlank(companyAdviserId) && StringUtils.isBlank(companyDirectorId)) {
					mobileFlag = true;
				}
				if (StringUtils.isNotBlank(companyAdviserId) && loginUserId.equals(companyAdviserId)) {
					mobileFlag = true;
				}
				if (StringUtils.isNotBlank(companyDirectorId) && loginUserId.equals(companyDirectorId)) {
					mobileFlag = true;
				}
				for (int i = 0; i < relativeUserList.size(); i++) { //判断相关人员权限
					if (loginUserId.equals(relativeUserList.get(i).get("userId")) && relativeUserList.get(i).get("companyId").equals(mapCompanyId)) {
						relativeFlag = true;
					}
				}

				if (!mobileFlag && !relativeFlag && !superFlag && permission == false ) {
					map.put("mobile", "***");
				}
			}*/


//			responseDbCenter.setResModel(userAttendMeetingService.userHotelWriteExcel(maps,userId));

			LinkedHashMap<String, String> tilleMap = new LinkedHashMap<String, String>();
			tilleMap.put("index","序号");
			tilleMap.put("uname","客户");
			tilleMap.put("mobile","电话");
			tilleMap.put("companyName","行企业名称");
			tilleMap.put("hotelName","酒店名称");
			tilleMap.put("room","房型");
			tilleMap.put("startTime","入住日期");
			tilleMap.put("endTime","退房日期");
			tilleMap.put("dates","住宿天数");

			//调用公共Excell导出接口
			String path =PropertiesUtil.FILE_UPLOAD_PATH+userId+"/客户订房信息.xlsx";
			String httpPath = PropertiesUtil.FILE_HTTP_PATH+userId+"/客户订房信息.xlsx";
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			if(FileUtil.writeExcel(tilleMap,maps,path)){
				responseDbCenter.setResModel(httpPath);
				return responseDbCenter;
			}else{
				return ResponseConstants.ERROR_EXPORTDATA_EXCELL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.ERROR_EXPORTDATA_EXCELL;
		}
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年6月27日
	 * @功能描述:
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportCompanyProject")
	public ResponseDbCenter exportCompanyProject(HttpServletRequest req,HttpServletResponse rsp){


		String content = req.getParameter("content");
		String meetingId = req.getParameter("meetingId");
		String path = req.getParameter("path");
		String parentIndustry = req.getParameter("parentIndustryId");
		String sonIndustryId = req.getParameter("sonIndustryId");
		String province = req.getParameter("province");
		String city = req.getParameter("city");
		String county = req.getParameter("county");
		String plevel = req.getParameter("plevel");
		String companyAdviser = req.getParameter("companyAdviser");
		String director = req.getParameter("director");
		String mgroupp = req.getParameter("mgroup");
		String token  = req.getParameter("token");

		if(StringUtils.isBlank(token)){
			//富文本编辑器的格式，要这样返回
			return ResponseConstants.FUNC_USER_NOTOKEN;
		}

		String userId = null;
		try {
			userId = req.getSession().getAttribute(token).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

        if(StringUtils.isBlank(userId)){
        	return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
        }


		int  pageSizeInt = 10000000;
		int  currentPageInt = 0;
		if(StringUtils.isBlank(meetingId) || "undefined".equals(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		String sqlContent = "";
		if(StringUtils.isNotBlank(content)){
			sqlContent = " and (c.companyName like '%"+content+"%' or c.label like '%"+content+"%' or u.mobile like '"+content+"')";
		}
		String parentIndustryStr = "";
		String sonIndustryStr = "";
		if(StringUtils.isNotBlank(parentIndustry)){
			parentIndustryStr = " and c.parentIndustryId like '%"+parentIndustry+"%' ";
		}

		if(StringUtils.isNotBlank(sonIndustryId)){
			String[] sonIndustryIds = sonIndustryId.split(",");
			String temp = "";
			for(int i =0 ;i<sonIndustryIds.length ; i++){
				temp +=  " c.sonIndustry like '%"+sonIndustryIds[i]+"%'  or ";
			}
			temp = temp.substring(0, temp.lastIndexOf("or"));
			sonIndustryStr = " and ("+temp+") ";
		}
		String provinceStr = "";
		if(StringUtils.isNotBlank(province)){
			provinceStr = " and ( c.province like '%"+province+"%' or c.address like '%"+province+"%' )";
		}
		String cityStr = "";
		if(StringUtils.isNotBlank(city)){
			cityStr = " and c.city like '%"+city+"%' ";
		}
		String countyStr = "";
		if(StringUtils.isNotBlank(county)){
			countyStr = " and c.county like '%"+county+"%' ";
		}

		String plevelStr = "";
		if(StringUtils.isNotBlank(plevel)){
			plevelStr = " and c.userLevel = '"+plevel.trim()+"' ";
		}

		String companyAdviserStr = "";
		if(StringUtils.isNotBlank(companyAdviser)){
			companyAdviserStr = " and c.companyAdviser = '"+companyAdviser+"' ";
		}

		String directorStr = "";
		if(StringUtils.isNotBlank(director)){
			directorStr = " and mu.director = '"+director+"' ";
		}

		String mgroupStr = "";
		if(StringUtils.isNotBlank(mgroupp)){
			mgroupStr = " and mu.mgroup = '"+mgroupp+"'";
		}

		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("meetingId",meetingId);
		paramMap.put("content", sqlContent);
		paramMap.put("parentIndustryStr", parentIndustryStr);
		paramMap.put("sonIndustryStr", sonIndustryStr);
		paramMap.put("provinceStr", provinceStr);
		paramMap.put("cityStr", cityStr);
		paramMap.put("countyStr", countyStr);
		paramMap.put("plevelStr", plevelStr);
		paramMap.put("companyAdviserStr", companyAdviserStr);
		paramMap.put("directorStr", directorStr);
		paramMap.put("mgroupStr", mgroupStr);
		paramMap.put("currentPageInt", currentPageInt);
		paramMap.put("pageSizeInt", pageSizeInt);

		boolean superFlag = false;

		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String, Object>  user =  userService.selectUserById(loginUserId);
		if(user != null){
			String roleName = (String)user.get("roleName");
			if("超级管理员".equals(roleName)){
				superFlag = true;
			}
		}

		List<Map<String,Object>> userAttendMeetingMaps = userAttendMeetingService.selectMeetingProjectsMapsByMeetingId(paramMap);
		path =PropertiesUtil.FILE_UPLOAD_PATH+userId+"/项目汇总表.xlsx";

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("0");
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell0 = row.createCell(0);
        cell0.setCellValue("序号");
        XSSFCellStyle style = wb.createCellStyle();
        Font fontHeader=wb.createFont();
    	//字体号码
    	fontHeader.setFontHeightInPoints((short)12);
    	fontHeader.setBold(true);
    	//字体名称
    	fontHeader.setFontName("宋体");
    	style.setFont(fontHeader);
        cell0.setCellStyle(style);

        XSSFCell cell1 = row.createCell(1);
        cell1.setCellValue("组别");
        cell1.setCellStyle(style);

        XSSFCell cell2 = row.createCell(2);
        cell2.setCellValue("企业省份");
        cell2.setCellStyle(style);

        XSSFCell cell3 = row.createCell(3);
        cell3.setCellValue("企业城市");
        cell3.setCellStyle(style);

        XSSFCell cell4 = row.createCell(4);
        cell4.setCellValue("企业名称");
        cell4.setCellStyle(style);

        XSSFCell cell5 = row.createCell(5);
        cell5.setCellValue("项目名称");
        cell5.setCellStyle(style);

        XSSFCell cell6 = row.createCell(6);
        cell6.setCellValue("客户需求");
        cell6.setCellStyle(style);

        XSSFCell cell7 = row.createCell(7);
        cell7.setCellValue("项目等级");
        cell7.setCellStyle(style);

        XSSFCell cell8 = row.createCell(8);
        cell8.setCellValue("行业类别");
        cell8.setCellStyle(style);

        XSSFCell cell9 = row.createCell(9);
        cell9.setCellValue("客户");
        cell9.setCellStyle(style);

        XSSFCell cell10 = row.createCell(10);
        cell10.setCellValue("项目详情");
        cell10.setCellStyle(style);

        XSSFCell cell11 = row.createCell(11);
        cell11.setCellValue("建议");
        cell11.setCellStyle(style);

        XSSFCell cell12 = row.createCell(12);
        cell12.setCellValue("跟进情况");
        cell12.setCellStyle(style);

        XSSFCell cell13 = row.createCell(13);
        cell13.setCellValue("总监");
        cell13.setCellStyle(style);

        XSSFCell cell14 = row.createCell(14);
        cell14.setCellValue("所属顾问");
        cell14.setCellStyle(style);

        XSSFCell cell15 = row.createCell(15);
        cell15.setCellValue("备注");
        cell15.setCellStyle(style);

		int i= 0;

		List<String> companyIdList=new ArrayList<>();
		for(Map<String,Object> map : userAttendMeetingMaps){
			String companyId = map.get("companyId") == null?"":map.get("companyId").toString();
			if(StringUtils.isNotBlank(companyId)){
				companyIdList.add(companyId);
			}
		}
		List<Map<String,Object>> relativeUserList=companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);

		for(Map<String,Object> map : userAttendMeetingMaps){

			boolean mobileFlag = false;
			boolean relativeFlag=false;

			String parentIndustryid = map.get("parentIndustryId") == null?"":map.get("parentIndustryId").toString();
			String companyId = map.get("companyId") == null?"":map.get("companyId").toString();
			String companyAdviserId = map.get("adviserId") == null?"":map.get("adviserId").toString();
			String directorId = map.get("directorId") == null?"":map.get("directorId").toString();
			String projectLevelId = map.get("projectLevelId") == null?"":map.get("projectLevelId").toString();

			if(StringUtils.isNotBlank(companyAdviserId) && loginUserId.equals(companyAdviserId)){
				mobileFlag = true;
			}
			if(StringUtils.isNotBlank(directorId) && loginUserId.equals(directorId)){
				mobileFlag = true;
			}
			for (int v = 0; v <relativeUserList.size() ; v++) { //判断相关人员权限
				if (loginUserId.equals(relativeUserList.get(v).get("userId")) && relativeUserList.get(v).get("companyId").equals(companyId)){
					relativeFlag=true;
				}
			}
			//将参会的企业人返回前端
			List<Map<String,Object>> userInfoMaps = userAttendMeetingService.selectMeetingUserMapsByCompanyId(companyId,meetingId);

			String userMobile = "";

			Boolean permission = userService.getPrivatePermission(loginUserId, "meeting/private");
			for(Map<String,Object> userMap : userInfoMaps ){
				for (Map.Entry<String, Object> entry : userMap.entrySet()) {
					 switch(entry.getKey()){
					 	case "uname":
					 	String uname = entry.getValue() == null?"":entry.getValue().toString();
					 	userMobile += uname +"  ";
					    break;
					    case "mobile":
				    	/*
						 * 手机号、电话号码保密
						 */
						if(!mobileFlag && !relativeFlag && !superFlag && permission == false ){
							userMobile += "***  ";
							break;
						}else {
							String mobile = entry.getValue() == null?"":entry.getValue().toString();
						    userMobile += mobile +"  ";
						    break;
						}
					    default: break;
					 }
				}
			}

			i++;
			map.put("userMobile",userMobile);
			map.put("xuhao",i+"");
			row = sheet.createRow(i);
			XSSFCell cell = null;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				 switch(entry.getKey()){
				 	case "xuhao":
				 	String xuhao = entry.getValue() == null?"":entry.getValue().toString();
				 	cell = row.createCell(0);
				    cell.setCellValue(xuhao);
				    break;
				    case "mgroup":
				    String mgroup = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(1);
				    cell.setCellValue(mgroup);
			    	break;
				    case "province":
				    String provincee = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(2);
				    cell.setCellValue(provincee);
			    	break;
				    case "city":
				    String cityy = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(3);
				    cell.setCellValue(cityy);
			    	break;
				    case "companyName":
				    String company_name = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(4);
				    cell.setCellValue(company_name);
			    	break;
				    case "projectName":
				    String projectname = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(5);
				    cell.setCellValue(projectname);
			    	break;
				    case "userNeed":
				    String userNeed = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(6);
				    cell.setCellValue(userNeed);
			    	break;
				    case "projectLevel":
				    String plevell = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(7);
				    cell.setCellValue(plevell);
			    	break;
				    case "parentIndustryName":
				    String parentIndustryName = entry.getValue() == null?"":entry.getValue().toString();
				    String sonIndustryIdStr = map.get("sonIndustry") == null?"":map.get("sonIndustry").toString();
				    cell = row.createCell(8);
				    cell.setCellValue(parentIndustryName+">"+sonIndustryIdStr);
			    	break;
				    case "userMobile":
				    String userMobileStr = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(9);
				    cell.setCellValue(userMobileStr);
			    	break;
				    case "description":
				    String pdesc = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(10);
				    cell.setCellValue(pdesc);
			    	break;
				    case "advice":
				    String project_suggestion = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(11);
				    cell.setCellValue(project_suggestion);
			    	break;
				    case "following":
				    String following = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(12);
				    cell.setCellValue(following);
				    break;
				    case "companyAdviserName":
				    String projectAdviserNamee = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(13);
				    cell.setCellValue(projectAdviserNamee);
			    	break;
				    case "companyDirectorName":
				    String projectDirectorNamee = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(14);
				    cell.setCellValue(projectDirectorNamee);
			    	break;
				    case "content":
				    String contentStr = entry.getValue() == null?"":entry.getValue().toString();
				    cell = row.createCell(15);
				    cell.setCellValue(contentStr);
				    break;
			    	default: break;
				 }  
			}
		}
		
		 FileOutputStream outputStream = null;
		 
		try {
			
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
	        outputStream.close();
	         
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
			return ResponseConstants.ERROR_EXPORTDATA_EXCELL;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(PropertiesUtil.FILE_HTTP_PATH+userId+"/项目汇总表.xlsx");
		return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年5月16日
	 * @功能描述:新增订房信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value="新增订房信息")
	@ResponseBody
	@RequestMapping(value = "/saveUserHotel" ,method = RequestMethod.POST)
	public ResponseDbCenter saveUserHotel(HttpServletRequest req,HttpServletResponse rsp,
										  @ApiParam(value = "token", required = false) @RequestParam(required = false) String token,
										  @ApiParam(value = "客户名称", required = false) @RequestParam(required = false) String uname,
										  @ApiParam(value = "手机号", required = false) @RequestParam(required = false) String mobile,
										  @ApiParam(value = "酒店名称", required = false) @RequestParam(required = false) String hotelName,
										  @ApiParam(value = "订房开始时间", required = false) @RequestParam(required = false) String startTime,
										  @ApiParam(value = "订房结束时间", required = false) @RequestParam(required = false) String endTime,
										  @ApiParam(value = "房间类型和数量", required = false) @RequestParam(required = false) String hotelNumber,
										  @ApiParam(value = "会务ID", required = false) @RequestParam(required = false) String meetingId
										  )throws Exception{

		String loginUserId = (String) req.getAttribute("loginUserId");

		if(StringUtils.isBlank(mobile) || StringUtils.isBlank(hotelName)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		try {
			hotelService.changeUserHotelMessage(loginUserId,uname,mobile,hotelName,startTime,endTime,hotelNumber,null,null,meetingId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}

	/**
	 *
	 * @author: xiaoz
	 * @2017年5月16日
	 * @功能描述:编辑订房信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value="编辑订房信息")
	@ResponseBody
	@RequestMapping(value = "/updateUserHotel" ,method = RequestMethod.POST)
	public ResponseDbCenter updateUserHotel(HttpServletRequest req,HttpServletResponse rsp,
										  @ApiParam(value = "token", required = false) @RequestParam(required = false) String token,
										  @ApiParam(value = "客户ID", required = false) @RequestParam(required = false) String userId,
										  @ApiParam(value = "客户名称", required = false) @RequestParam(required = false) String uname,
										  @ApiParam(value = "手机号", required = false) @RequestParam(required = false) String mobile,
										  @ApiParam(value = "userHotelId", required = false) @RequestParam(required = false) String userHotelId,
										  @ApiParam(value = "酒店名称", required = false) @RequestParam(required = false) String hotelName,
										  @ApiParam(value = "订房开始时间", required = false) @RequestParam(required = false) String startTime,
										  @ApiParam(value = "订房结束时间", required = false) @RequestParam(required = false) String endTime,
										  @ApiParam(value = "房间类型和数量", required = false) @RequestParam(required = false) String hotelNumber,
										  @ApiParam(value = "会务ID", required = false) @RequestParam(required = false) String meetingId
											)throws Exception{


		String loginUserId = (String) req.getAttribute("loginUserId");

		if(StringUtils.isBlank(mobile) || StringUtils.isBlank(hotelName)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		try {
			hotelService.changeUserHotelMessage(loginUserId,uname,mobile,hotelName,startTime,endTime,hotelNumber,userId,userHotelId,meetingId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}


	/**
	 *
	 * @author: xiaoz
	 * @2017年5月16日
	 * @功能描述:编辑订房信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value="删除订房信息")
	@ResponseBody
	@RequestMapping(value = "/deleteUserHotelByIds")
	public ResponseDbCenter updateUserHotel(HttpServletRequest req,HttpServletResponse rsp,
											@ApiParam(value = "token", required = false) @RequestParam(required = false) String token,
											@ApiParam(value = "userHotelIds", required = false) @RequestParam(required = false) String userHotelIds)throws Exception{




		if(StringUtils.isBlank(userHotelIds)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		List<String> idList = new ArrayList<>();

		String[] cids = userHotelIds.split(",");


		for (int i = 0; i < cids.length; i++) {
			idList.add(cids[i]);
		}

		try {
			hotelService.deleteUserHotelByIds(idList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}

	@ApiOperation(value = "添加推广用户")
	@ResponseBody
	@RequestMapping(value = "/saveUserSpread",method = RequestMethod.POST)
	public ResponseDbCenter saveUserSpread(HttpServletRequest req, HttpServletResponse rsp,
										@ApiParam(value = "用户ID", required = false) @RequestParam(required = false) String userId,
										   @ApiParam(value = "会务ID", required = false) @RequestParam(required = false) String  meetingId)throws Exception{

		String loginUserId = (String)req.getAttribute("loginUserId");

		if(StringUtils.isBlank(userId) || StringUtils.isBlank(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		try{

			Map<String,Object> userMap = meetingService.selectUserSpreadByMeetingIdUserId(meetingId,userId);
			if(userMap != null && userMap.get("id") != null){
				return ResponseConstants.USER_SPREAD_EXISTS;
			}

			Map<String,Object> map = new HashMap<>();
			map.put("id",UUID.randomUUID().toString());
			map.put("userId",userId);
			map.put("meetingId",meetingId);
			map.put("createdBy",loginUserId);
			map.put("updatedBy",loginUserId);
			map.put("createDate",new Date());
			map.put("updateDate",new Date());

			meetingService.saveUserSpread(map);

			return new ResponseDbCenter();

		}catch (Exception e){
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}


	@ApiOperation(value = "查询会务的推广用户")
	@ResponseBody
	@RequestMapping(value = "/selectUserSpreadsByMeetingId",method = RequestMethod.POST)
	public ResponseDbCenter selectUserSpreadsByMeetingId(HttpServletRequest req, HttpServletResponse rsp,
											 @ApiParam(value = "会务ID", required = false) @RequestParam(required = false) String meetingId){

		if(StringUtils.isBlank(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		try{

			Integer num = 0;

			List<Map<String,Object>> maps = meetingService.selectUserSpreadsByMeetingId(meetingId);
			num= meetingService.selectTotalUserSpreadsByMeetingId(meetingId);

			ResponseDbCenter res = new ResponseDbCenter();
			res.setResModel(maps);
			res.setTotalRows(num.intValue()+"");
			return res;

		}catch (Exception e){
			e.printStackTrace();
			return   ResponseConstants.FUNC_SERVERERROR;
		}
	}

	@ApiOperation(value = "删除会务的推广用户")
	@ResponseBody
	@RequestMapping(value = "/deleteUserSpreadsByIds" ,method = RequestMethod.POST)
	public ResponseDbCenter deleteUserSpreadsByIds(HttpServletRequest req, HttpServletResponse rsp,
														 @ApiParam(value = "会务推广人的", required = false)
														 @RequestParam(required = false) String userSpreadIds) throws Exception{

		if(StringUtils.isBlank(userSpreadIds)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		List<String> idList = new ArrayList<>();
		String[] idss = userSpreadIds.split(",");
		for(String id : idss){
			idList.add(id);
		}

		try{
			meetingService.deleteUserSpreadsByIds(idList);
			ResponseDbCenter res = new ResponseDbCenter();
			return res;

		}catch (Exception e){
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	@ApiOperation(value = "根据推广人ID获得推广人的详情")
	@ResponseBody
	@RequestMapping(value = "/selectDetailByspreadId",method = RequestMethod.POST)
	public ResponseDbCenter selectDetailByspreadId(HttpServletRequest req, HttpServletResponse rsp,
												   @ApiParam(value = "推广人ID", required = false) @RequestParam(required = false) String userSpreadId){

		if(StringUtils.isBlank(userSpreadId)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		try{
			List<Map<String,Object>> maps = meetingService.selectDetailByspreadId(userSpreadId);
			Integer num = meetingService.selectCountDetailByspreadId(userSpreadId);

			//因为数据库返回前端为[null]所以要处理下
			List<Map<String,Object>> returnMaps = new ArrayList<>();
			for(Map<String,Object> map : maps){

				if(map != null && map.get("id") != null){
					String orderId = (String)map.get("id");
					List<Map<String,Object>> orderTickets = ticketService.selectorderTicketByOderId(orderId);
					map.put("orderTickets",orderTickets);
				}

				if(map != null){
					returnMaps.add(map);
				}
			}

			ResponseDbCenter res = new ResponseDbCenter();
			res.setResModel(returnMaps);
			res.setTotalRows(num.intValue()+"");
			return res;

		}catch (Exception e){
			e.printStackTrace();
			return   ResponseConstants.FUNC_SERVERERROR;
		}
	}


}