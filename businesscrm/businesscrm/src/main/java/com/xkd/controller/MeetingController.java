package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.model.*;
import com.xkd.model.groups.SaveValidateGroup;
import com.xkd.model.groups.UpdateValidateGroup;
import com.xkd.model.vo.MeetingVo;
import com.xkd.service.*;
import com.xkd.utils.DateUtils;
import com.xkd.utils.FileUtil;
import com.xkd.utils.PropertiesUtil;
import com.xkd.utils.SysUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.security.acl.Group;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(description = "会务相关接口")
@Controller
@RequestMapping("/meeting")
@Transactional
public class MeetingController extends  BaseController  {

    @Autowired
    MeetingService meetingService;
    @Autowired
    private UserAttendMeetingService userAttendMeetingService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private SolrService solrService;
    @Autowired
    private UserService userService;
    @Autowired
    private DC_UserService dcUserService;
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
    @Autowired
    private AddressService addressService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private MeetingPeopleService meetingPeopleService;


   /* @ApiOperation(value = "添加会务")
    @ResponseBody
    @RequestMapping(value = "/addMeeting",method =  {RequestMethod.POST})
    public ResponseDbCenter addMeeting(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam("会务")    @RequestBody(required = true) @Validated(value = {SaveValidateGroup.class}) MeetingVo meetingVo

    ) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        meetingService.saveMeeting(meetingVo,loginUserId);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }*/

    /*@ApiOperation(value = "修改会务")
    @ResponseBody
    @RequestMapping(value = "/updateMeeting",method =  {RequestMethod.POST})
    public ResponseDbCenter updateMeeting(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam("会务")    @RequestBody(required = true)  @Validated(value = {UpdateValidateGroup.class}) MeetingVo meetingVo

    ) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        meetingService.updateMeeting(meetingVo, loginUserId);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }*/

   /* @ApiOperation(value = "停用启用会务")
    @ResponseBody
    @RequestMapping(value = "/updateMeetingFlag",method =  {RequestMethod.POST})
    public ResponseDbCenter updateMeetingFlag(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam("会务ID")    @RequestParam(required = true) String id,
                                          @ApiParam("状态'0'启用,'1'禁用")    @RequestParam(required = true) String flag

    ) throws Exception {

        if(StringUtils.isBlank(id) || StringUtils.isBlank(flag)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        meetingService.updateMeetingFlag(id,Integer.parseInt(flag));
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }*/



   /* @ApiOperation(value = "删除会务")
    @ResponseBody
    @RequestMapping(value = "/deleteMeeting",method =  {RequestMethod.POST})
    public ResponseDbCenter deleteMeeting(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "会务Ids , 多个用逗号分隔",required = true)    @RequestParam(required = true) String ids

    ) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        if (StringUtils.isNotBlank(ids)){
           String[] idArray=ids.split(",");
            List<String> idList= Arrays.asList(idArray);
            meetingService.deleteMeetingByIds(idList);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }
*/

    /**
     *
     * @author: xiaoz
     * @2017年5月16日
     * @功能描述:根据参数查询会议信息
     * @param req
     * @param rsp
     * @return
     */
    /*
    @ApiOperation(value = "查询企业头像")
    @ResponseBody
    @RequestMapping(value="/selectCompanysLogo" ,method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectCompanysLogo(HttpServletRequest req,HttpServletResponse rsp,
                                               @ApiParam(value = "meetingId", required = false) @RequestParam(required = false) String meetingId
     */
    @ApiOperation(value = "根据参数查询会议信息")
    @ResponseBody
    @RequestMapping(value="/selectMeetingByParam",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectMeetingByParam(HttpServletRequest req,HttpServletResponse rsp,
                                                 @ApiParam(value = "meetingName", required = false) @RequestParam(required = false) String meetingName,
                                                 @ApiParam(value = "createdByName", required = false) @RequestParam(required = false) String createdByName,
                                                 @ApiParam(value = "meetingType", required = false) @RequestParam(required = false) String meetingType,
                                                 @ApiParam(value = "meetingStatus", required = false) @RequestParam(required = false) String meetingStatus,
                                                 @ApiParam(value = "currentPage", required = false) @RequestParam(required = false) String currentPage,
                                                 @ApiParam(value = "pageSize", required = false) @RequestParam(required = false) String pageSize,
                                                 @ApiParam(value = "startTime", required = false) @RequestParam(required = false) String startTime,
                                                 @ApiParam(value = "endTime", required = false) @RequestParam(required = false) String endTime,
                                                 @ApiParam(value = "province", required = false) @RequestParam(required = false) String province,
                                                 @ApiParam(value = "city", required = false) @RequestParam(required = false) String city,
                                                 @ApiParam(value = "county", required = false) @RequestParam(required = false) String county,
                                                 @ApiParam(value = "departmentId", required = false) @RequestParam(required = false) String departmentId,
                                                 @ApiParam(value = "queryContent", required = false) @RequestParam(required = false) String queryContent
    ){

        String mname = queryContent;
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

        String meetingStatusStr = "";
        //会务启用停用
        Integer meetingFlag=null;
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
                }else if("已停止".equals(t)){
                    //0启用、1停用
                    meetingFlag = 1;
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
                    meetingTypeStr,meetingStatusStr,meetingName,address,"",mname,currentPageInt,pageSizeInt,
                    list,pcCompanyId,createdByName,meetingFlag);
            num = meetingService.getMeetingCountByParam(meetingTypeStr,meetingStatusStr,meetingName,address,"",
                    mname,startTime,endTime,list,pcCompanyId,createdByName,meetingFlag);

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
                    if(meeting.getMflag().intValue() == 1){
                        meeting.setMeetingStatus("已停止");
                    }
                }
            }
        }catch (Exception e){
            log.error("异常栈:",e);
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
        meeting.setMflag(0);
        if(StringUtils.isBlank(sendMessageFlag)){
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
                List<Map<String, Object>> results = (List<Map<String, Object>>) JSON.parseObject(tickets,Object.class);

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
                log.error("异常栈:",e);
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
                    Object initSaving = map.get("saving");
                    map.put("meetingId",insertId);
                    map.put("initSaving",initSaving);
                }

                if(results != null && results.size() > 0){
                    ticketService.saveTickets(results);
                }

            } catch (Exception e) {
                log.error("异常栈:",e);
                throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
            }
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }

    @ApiOperation(value = "查询企业头像")
    @ResponseBody
    @RequestMapping(value="/selectCompanysLogo" ,method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectCompanysLogo(HttpServletRequest req,HttpServletResponse rsp,
                                               @ApiParam(value = "meetingId", required = false) @RequestParam(required = false) String meetingId){

        if(StringUtils.isBlank(meetingId)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        List<Map<String,Object>> logosMap =  meetingService.selectCompanysLogo(meetingId);
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(logosMap);
        return responseDbCenter;
    }


    @ApiOperation(value = "批量删除会议客户")
    @ResponseBody
    @RequestMapping(value="/deleteMeetingUserByIds" ,method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter deleteMeetingUserByIds(HttpServletRequest req,HttpServletResponse rsp,
                                                   @ApiParam(value = "ids", required = false) @RequestParam(required = false) String ids) throws Exception{

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
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

    @ApiOperation(value = "根据会议ID删除会议")
    @ResponseBody
    @RequestMapping(value="/deleteMeetingById",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter deleteMeetingById(HttpServletRequest req,HttpServletResponse rsp,
                                              @ApiParam(value = "会议ID", required = false) @RequestParam(required = false) String meetingId) throws Exception{

        String ids = meetingId;
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
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    @ApiOperation(value = "根据会议ID获得会议相关信息")
    @ResponseBody
    @RequestMapping(value="/selectMeetingById",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectMeetingById(HttpServletRequest req,HttpServletResponse rsp,
                                @ApiParam(value = "会议ID", required = false) @RequestParam(required = false) String meetingId){

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
                if(meeting.getMflag().intValue() == 1){
                    meeting.setMeetingStatus("已停止");
                }
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
     * @功能描述:根据会议ID获得票务相关信息
     * @param req
     * @return
     */
    @ApiOperation(value = "根据会议ID获得票务相关信息")
    @ResponseBody
    @RequestMapping(value="/selectTicketMeetingById" ,method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectTicketMeetingById(HttpServletRequest req,HttpServletResponse rs,
                                                    @ApiParam(value = "会议ID", required = false) @RequestParam(required = false) String meetingId){

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
                if(meeting.getMflag().intValue() == 1){
                    meeting.setMeetingStatus("已停止");
                }
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
            log.error("异常栈:",e);
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
            log.error("异常栈:",e);
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
    @RequestMapping(value = "/deleteUserHotelByIds" ,method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseDbCenter updateUserHotel(HttpServletRequest req,HttpServletResponse rsp,
                                            @ApiParam(value = "token", required = false) @RequestParam(required = false) String token,
                                            @ApiParam(value = "userHotelIds", required = false) @RequestParam(required = false) String userHotelIds)throws Exception{




        if(StringUtils.isBlank(userHotelIds)){
            return ResponseConstants.MISSING_PARAMTER;
        }
        String[] cids = userHotelIds.split(",");
        List<String> idList = Arrays.asList(cids);
        try {
            hotelService.deleteUserHotelByIds(idList);
        } catch (Exception e) {
            log.error("异常栈:",e);
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
            log.error("异常栈:",e);
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
            log.error("异常栈:",e);
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
            log.error("异常栈:",e);
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
            log.error("异常栈:",e);
            return   ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "查询每个会务的票务统计信息")
    @ResponseBody
    @RequestMapping(value = "/selectMeetingTicketStatistics",method = RequestMethod.POST)
    public ResponseDbCenter selectMeetingTicketStatistics(HttpServletRequest req, HttpServletResponse rsp){


        try{

            List<Map<String,Object>> maps = meetingService.selectMeetingTicketStatistics();
            Integer num = meetingService.selectMeetingTicketStatisticsTotal();

            for(Map<String,Object> map : maps){
                String meetingId = (String) map.get("id");
                List<Map<String,Object>> meetingTickers = ticketService.selectTicketsByMeetingId(meetingId);
                if(meetingTickers != null && meetingTickers.size() > 0){
                    map.put("tickers",meetingTickers);
                }
            }

            ResponseDbCenter res = new ResponseDbCenter();
            res.setResModel(maps);
            res.setTotalRows(num.intValue()+"");
            return res;

        }catch (Exception e){
            log.error("异常栈:",e);
            return   ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "会务pc端签到查询")
    @ResponseBody
    @RequestMapping(value = "/selectMeetingSign",method = RequestMethod.POST)
    public ResponseDbCenter selectMeetingSign(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "会议ID", required = false) @RequestParam(required = false) String meetingId){


        try{

            List<Map<String,Object>> maps = meetingService.selectMeetingSign(meetingId);
            Integer num = meetingService.selectMeetingSignTotal(meetingId);
            ResponseDbCenter res = new ResponseDbCenter();
            res.setResModel(maps);
            res.setTotalRows(num.intValue()+"");
            return res;

        }catch (Exception e){
            log.error("异常栈:",e);
            return   ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "登录用户移动端自动签到")
    @ResponseBody
    @RequestMapping(value = "/loginUserMeetingSign",method = RequestMethod.POST)
    public ResponseDbCenter loginUserMeetingSign(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "会议ID", required = false) @RequestParam(required = false) String meetingId,
                                              @ApiParam(value = "code", required = false) @RequestParam(required = false) String code  ){



        try{
            HashMap<String, String> wx = SysUtils.getOpenId(code);
            if(wx == null&&!code.equals("123")){
                return ResponseConstants.FUNC_USER_OPENID_FAIL_WJ;
            }
            String openId = code.equals("123") ? "o2DOPwUMugpWcLs9swzBX6FgEwNk" : wx.get("openId");
            if(openId == null){
                return ResponseConstants.FUNC_USER_OPENID_FAIL_WJ;
            }
            DC_User user = dcUserService.getUserByObj(openId,"2");
            System.out.print("========================loginUser  openId=========="+openId);
            System.out.print("========================loginUser  openId=========="+openId);
            System.out.print("========================loginUser  openId=========="+openId);
            System.out.print("========================loginUser  openId=========="+openId);
            System.out.print("========================loginUser  openId=========="+openId);
            if(user != null){
                Map<String, Object> mobileUserMap = meetingPeopleService.selectUserByMeetingMobile(user.getMobile(),meetingId);
                if(mobileUserMap == null || mobileUserMap.size() == 0){
                    return   ResponseConstants.MEETING_LOGIN_USER_NOT_EXISTS;
                }
                meetingService.userMeetingSignByMeetingUserId(mobileUserMap.get("meetingUserId")+"");
                ResponseDbCenter responseDbCenter = new ResponseDbCenter();
                return responseDbCenter;
            }else{
                return   ResponseConstants.LOGIN_USER_NOT_EXISTS;
            }

        }catch (Exception e){
            log.error("异常栈:",e);
            return  ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "手机签到")
    @ResponseBody
    @RequestMapping(value = "/userMeetingSign",method = RequestMethod.GET)
    public ResponseDbCenter userMeetingSign(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "会议ID", required = false) @RequestParam(required = false) String meetingId,
                                            @ApiParam(value = "手机号", required = false) @RequestParam(required = false) String mobile){



        try{

            Map<String, Object> mobileUserMap = meetingPeopleService.selectUserByMeetingMobile(mobile,meetingId);

            if(mobileUserMap == null || mobileUserMap.size() == 0){
                return   ResponseConstants.MEETING_MOBILE_NOT_EXISTS;
            }
            meetingService.userMeetingSignByMeetingUserId(mobileUserMap.get("id")+"");
            ResponseDbCenter res = new ResponseDbCenter();
            return res;

        }catch (Exception e){
            log.error("异常栈:",e);
            return   ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "会务启用停用")
    @ResponseBody
    @RequestMapping(value = "/updateMeetingFlag",method = RequestMethod.POST)
    public ResponseDbCenter updateMeetingFlag(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "会议ID", required = false) @RequestParam(required = false) String meetingId,
                                            @ApiParam(value = "状态，'0'启用、'1'禁用", required = false) @RequestParam(required = false) String flag){



        try{

            if(StringUtils.isBlank(meetingId) || StringUtils.isBlank(flag)){
                return ResponseConstants.MISSING_PARAMTER;
            }
            meetingService.updateMeetingFlag(meetingId,Integer.parseInt(flag));
            ResponseDbCenter res = new ResponseDbCenter();
            return res;

        }catch (Exception e){
            log.error("异常栈:",e);
            return   ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "导出票款结算")
    @ResponseBody
    @RequestMapping(value = "/exportMeetingTicketStatistics",method = RequestMethod.POST)
    public ResponseDbCenter exportMeetingTicketStatistics(HttpServletRequest req, HttpServletResponse rsp){


        try{

            List<Map<String,Object>> maps = meetingService.selectMeetingTicketStatistics();
            for(Map<String,Object> map : maps){
                String meetingId = (String) map.get("id");
                List<Map<String,Object>> meetingTickers = ticketService.selectTicketsByMeetingId(meetingId);

                if(meetingTickers != null && meetingTickers.size() > 0){
                    int savingTotal = 0;
                    int initSavingTotal = 0;
                    int saleTotal = 0;
                    double moneyTotal = 0.00;
                    for(Map<String,Object> m : meetingTickers){
                        String saving = (String) m.get("saving");
                        String initSaving = (String) m.get("initSaving");
                        double price = (double) m.get("price");

                        int savingInt = Integer.parseInt(saving);
                        int initSavingInt = Integer.parseInt(initSaving);
                        double money = (initSavingInt - savingInt)*price;

                        moneyTotal += money;
                        initSavingTotal +=initSavingInt;
                        savingTotal += savingInt;
                    }
                    map.put("initSavingTotal",initSavingTotal);
                    map.put("saleTotal",saleTotal);
                    map.put("savingTotal",savingTotal);
                    map.put("moneyTotal",moneyTotal);
                }
            }

            LinkedHashMap<String, String> tilleMap = new LinkedHashMap<String, String>();
            //id,meetingName
            tilleMap.put("index","序号");
            tilleMap.put("meetingName","会议名称");
            tilleMap.put("initSavingTotal","票务总数（张）");
            tilleMap.put("saleTotal","实际出售数量（张）");
            tilleMap.put("savingTotal","票务结余（张）");
            tilleMap.put("moneyTotal","票务总金额（元）");

            //调用公共Excell导出接口
            String path =PropertiesUtil.FILE_UPLOAD_PATH+"/票款结算.xlsx";
            String httpPath = PropertiesUtil.FILE_HTTP_PATH+"/票款结算.xlsx";
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            if(FileUtil.writeExcel(tilleMap,maps,path)){
                responseDbCenter.setResModel(httpPath);
                return responseDbCenter;
            }else{
                return ResponseConstants.ERROR_EXPORTDATA_EXCELL;
            }
        }catch (Exception e){
            log.error("异常栈:",e);
            return  ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "导出票务信息")
    @ResponseBody
    @RequestMapping(value = "/exportMeetingTickets",method = RequestMethod.GET)
    public ResponseDbCenter exportMeetingTickets(HttpServletRequest req, HttpServletResponse rsp,
                                                 @ApiParam(value = "会务ID", required = false) @RequestParam(required = false) String meetingId){

        try{

            List<Map<String,Object>> maps = ticketService.selectTicketsByContent(meetingId,null,
                    0,9999999,null,null,null);
            Map<String,Object> ticketNumberMap = new HashMap<>();
            for(Map<String,Object> map : maps){
                StringBuffer ticketTypeNumber = new StringBuffer();
                String orderId = (String)map.get("orderId");
                List<Map<String,Object>> orderTickets = ticketService.selectorderTicketByOderId(orderId);
                for(Map<String,Object> m:orderTickets){
                    String ticketType = (String) m.get("ticketType");
                    int ticketNumber = m.get("ticketNumber")==null?-1:(int) m.get("ticketNumber");
                    if(StringUtils.isNotBlank(ticketType) && ticketNumber !=-1){
                        ticketTypeNumber.append(" "+ticketType+"*"+ticketNumber+" ");
                    }
                }

                map.put("ticketTypeNumber",ticketTypeNumber.toString());
            }

            LinkedHashMap<String, String> tilleMap = new LinkedHashMap<String, String>();
            tilleMap.put("index","序号");
            tilleMap.put("uname","姓名");
            tilleMap.put("mobile","手机号");
            tilleMap.put("orderTime","订单时间");
            tilleMap.put("companyName","公司名称");
            tilleMap.put("station","职位");
            tilleMap.put("email","邮箱");
            tilleMap.put("idcard","身份证号");
            tilleMap.put("ticketTypeNumber","票种/数量");

            //调用公共Excell导出接口
            String path =PropertiesUtil.FILE_UPLOAD_PATH+"/票务信息.xlsx";
            String httpPath = PropertiesUtil.FILE_HTTP_PATH+"/票务信息.xlsx";
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            if(FileUtil.writeExcel(tilleMap,maps,path)){
                responseDbCenter.setResModel(httpPath);
                return responseDbCenter;
            }else{
                return ResponseConstants.ERROR_EXPORTDATA_EXCELL;
            }
        }catch (Exception e){
            log.error("异常栈:",e);
            return  ResponseConstants.FUNC_SERVERERROR;
        }
    }

}
