package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.UserAttendMeeting;
import com.xkd.service.*;
import com.xkd.utils.DateUtils;
import com.xkd.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(description = "有电报修、维修功能接口")
@Controller
@RequestMapping("/YDrepaire")
@Transactional
public class YDrepaireController {

    @Autowired
    private YDrepaireService repaireService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectNewsService objectNewsService;
    @Autowired
    DeviceGroupService deviceGroupService;
    @Autowired
    private CompanyContactorService companyContactorService;
    @Autowired
    private DeviceService deviceService;


    /**
     *
     * @author: xiaoz
     * @2018年2月9日
     * @功能描述:获取报修单详情
     * @param req
     * @return id,
        description,
        statusFlag,
        uname,
        mobile,
        createDate,
        pictures,
        applyDevices
     */
    @ApiOperation(value = "获取报修单详情")
    @ResponseBody
    @RequestMapping(value = "/selectRepaireApplyById",method = RequestMethod.GET)
    public ResponseDbCenter selectRepaireApplyById(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value="报修单ID",required = false) @RequestParam(required = false) String id){

        if(StringUtils.isBlank(id)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            Map<String,Object>  repaireApply = repaireService.selectRepaireApplyById(id);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(repaireApply);

            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    /**
     *
     * @author: xiaoz
     * @2018年2月9日
     * @功能描述:获取维修详情
     * @param req
     * @return
     * id,
    dueTime,
    description,
    dealDescription,
    repaireNo,
    responseScore,
    altitudeScore,
    professionScore,
    customerFeedback,
    summary,
    createdByName,
    updatedBy1Name,
    updatedBy2Name,
    updatedBy3Name,
    updatedBy4Name,
    createDate,
    dueTime,
    updateDate1,
    updateDate2,
    updateDate3,
    updateDate4,
    pictures,
    repaireDevices
     */
    @ApiOperation(value = "获取维修详情")
    @ResponseBody
    @RequestMapping(value = "/selectRepaireDetailById",method = RequestMethod.GET)
    public ResponseDbCenter selectRepaireDetailById(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value="维修ID",required = false) @RequestParam(required = false) String id){


        if(StringUtils.isBlank(id)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            Map<String,Object>  repaireApply = repaireService.selectRepaireDetailById(id);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(repaireApply);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    /**
     *
     * @author: xiaoz
     * @2018年2月9日
     * @功能描述:创建报修单
     * @param req
     * @return
     */
    @ApiOperation(value = "创建报修单")
    @ResponseBody
    @RequestMapping(value = "/createRepaireApply",method = RequestMethod.POST)
    public ResponseDbCenter createRepaireApply(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value="设备ID",required = false) @RequestParam(required = false) String deviceIds,
                                        @ApiParam(value="设备故障描述",required = false) @RequestParam(required = false) String description,
                                        @ApiParam(value="0技师创建，1客户创建",required = false) @RequestParam(required = false) String createFlag,
                                        @ApiParam(value="多张图片url，逗号隔开",required = false) @RequestParam(required = false) String pictures)
            throws Exception{


        if(StringUtils.isBlank(deviceIds) || StringUtils.isBlank(createFlag)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        String loginUserId = (String)req.getAttribute("loginUserId");

        String[] pictureUrls = null;
        List<String> pictureList = new ArrayList<>();
        if(StringUtils.isNotBlank(pictures)){
            pictureUrls = pictures.split(",");
            for(String id : pictureUrls){
                pictureList.add(id);
            }
        }

        try {


            repaireService.createRepaireApply(loginUserId,deviceIds,description,pictureList,createFlag);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    /**
     *
     * @author: xiaoz
     * @2018年2月9日
     * @功能描述:创建维修单
     * @param req
     * @return
     */
    @ApiOperation(value = "创建维修单")
    @ResponseBody
    @RequestMapping(value = "/createRepaire",method = RequestMethod.POST)
    public ResponseDbCenter createRepaire(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value="报修单ID，没有就不传,报修转维修一定要传",required = false) @RequestParam(required = false) String applyId,
                                               @ApiParam(value="设备ID",required = false) @RequestParam(required = false) String deviceIds,
                                               @ApiParam(value="技师ID,多个用逗号隔开",required = false) @RequestParam(required = false) String technicianIds,
                                               @ApiParam(value="维修时间期限",required = false) @RequestParam(required = false) String dueTime,
                                               @ApiParam(value="设备故障描述",required = false) @RequestParam(required = false) String description,
                                               @ApiParam(value="解决方案",required = false) @RequestParam(required = false) String dealDescription,
                                               @ApiParam(value="处理过程",required = false) @RequestParam(required = false) String solution,
                                               @ApiParam(value="多张图片url，逗号隔开",required = false) @RequestParam(required = false) String pictures,
                                               @ApiParam(value="优先级，传1,2,3，代表高中低",required = false) @RequestParam(required = false) String priority) throws Exception{


        if(StringUtils.isBlank(deviceIds) || StringUtils.isBlank(technicianIds)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        String loginUserId = (String)req.getAttribute("loginUserId");

        String[] pictureUrls = null;
        List<String> pictureList = new ArrayList<>();
        if(StringUtils.isNotBlank(pictures)){
            pictureUrls = pictures.split(",");
            for(String id : pictureUrls){
                pictureList.add(id);
            }
        }

        String[] ts = null;
        if(StringUtils.isNotBlank(technicianIds)){
            ts = technicianIds.split(",");
        }
        List<String> technicianList = new ArrayList<>();
        if(ts != null){
            for(String id : ts){
                technicianList.add(id);
            }
        }

        try {

            /*
             *创建维修单
             */
            repaireService.createRepaire(loginUserId,applyId,deviceIds,dueTime,description,dealDescription,priority,pictureList,technicianList,solution);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    /**
     *客户端可以看到自己的报修（当前登录用户），自己所在客户的维修单
     * 技师端只能看到自己（当前登录用户）的报修、维修（自己创建的，和指派的）
     * 服务端只能看到自己所在供应商（pcCompanyId）的报修维修
     * @author: xiaoz
     * @2018年2月9日
     * @功能描述:获取报修单列表
     * @param req
     * @return
     */
    @ApiOperation(value = "获取报修单列表")
    @ResponseBody
    @RequestMapping(value = "/selectRepaireApplys",method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter selectRepaireApplys(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value="2:服务端，3技师端，4客户端，必传",required = false) @RequestParam(required = false) String appFlag,
                                                @ApiParam(value="状态，0 报修状态 1 驳回状态  2 已转工单状态",required = false) @RequestParam(required = false) String statusFlag,
                                                @ApiParam(value="客户名称",required = false) @RequestParam(required = false) String companyName,
                                                @ApiParam(value="开始时间",required = false) @RequestParam(required = false) String startTime,
                                                @ApiParam(value="结束时间",required = false) @RequestParam(required = false) String endTime,
                                                @RequestParam(value = "currentPage",required = false) String currentPage,
                                                @RequestParam(value = "pageSize",required = false) String pageSize){



        if(StringUtils.isBlank(appFlag)){
            return ResponseConstants.MISSING_PARAMTER;
        }


        String loginUserId = (String)req.getAttribute("loginUserId");
        if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
            currentPage = "1";
            pageSize = "10";
        }
        int  pageSizeInt = Integer.parseInt(pageSize);
        int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;

        try {

            Map<String, Object> paramMap = new HashedMap();
            String pcCompanyId = null;

            //服务端
            if("2".equals(appFlag)){
                Map<String, Object> mapp = userService.selectUserById(loginUserId);
                if(mapp !=null && mapp.size() > 0){
                    String roleId = (String)mapp.get("roleId");
                    if(!"1".equals(roleId)){
                        pcCompanyId = (String)mapp.get("pcCompanyId");
                        paramMap.put("pcCompanyId",pcCompanyId);
                    }
                }
            }else{
                paramMap.put("loginUserId",loginUserId);
            }


            paramMap.put("statusFlag",statusFlag);
            paramMap.put("companyName",companyName);
            paramMap.put("startTime",startTime);
            paramMap.put("endTime",endTime);
            paramMap.put("currentPage",currentPageInt);
            paramMap.put("pageSize",pageSizeInt);

            List<Map<String,Object>>  repaireApplyList = repaireService.selectRepaireApplys(paramMap);
            Integer num = repaireService.selectTotalRepaireApplys(paramMap);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(repaireApplyList);
            responseDbCenter.setTotalRows(num.intValue()+"");
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    /**
     *
     * @author: xiaoz
     * @2018年2月9日
     * @功能描述:获取报修单列表
     * @param req
     * @return
     *  id,
    repaireNo,
    description,
    statusFlag,
    uname,
    createDate,
    completeDate,
    statusFlagValue,
    repaireDevices,
    technicians
    *客户端可以看到自己的报修（当前登录用户），自己所在客户的维修单
     * 技师端只能看到自己（当前登录用户）的报修、维修、并且维修要是服务商指派他的
     * 服务端只能看到自己所在供应商（pcCompanyId）的报修维修
     */
    @ApiOperation(value = "获取维修单列表")
    @ResponseBody
    @RequestMapping(value = "/selectRepaires",method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter selectRepaires(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value="2:服务端，3技师端，4客户端，必传",required = false) @RequestParam(required = false) String appFlag,
                                                @ApiParam(value="状态，0 未受理（新建状态） 1 已受理（接单） 2 进行中（出发）   3 进行中（开始） 4 完成 （待评价）   5 完成（结单）",required = false) @RequestParam(required = false) String statusFlag,
                                                @ApiParam(value="客户名称",required = false) @RequestParam(required = false) String companyName,
                                                @ApiParam(value="工程师",required = false) @RequestParam(required = false) String technicianName,
                                                @ApiParam(value="登录用户id（用于移动端—技师，每个用户看到自己的维修单）",required = false) @RequestParam(required = false) String userId,
                                                @ApiParam(value="0:未完成、1已完成",required = false) @RequestParam(required = false) String completeFlag,
                                                @ApiParam(value="创建工单开始时间",required = false) @RequestParam(required = false) String startDate,
                                                @ApiParam(value="创建工单结束时间",required = false) @RequestParam(required = false) String endDate,
                                                @ApiParam(value="截止日期开始时间",required = false) @RequestParam(required = false) String dueTimeStartDate,
                                                @ApiParam(value="截止日期结束时间",required = false) @RequestParam(required = false) String dueTimeEndDate,
                                                @RequestParam(value = "currentPage",required = false) String currentPage,
                                                @RequestParam(value = "pageSize",required = false) String pageSize){

       if(StringUtils.isBlank(appFlag)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        String loginUserId = (String)req.getAttribute("loginUserId");
        if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
            currentPage = "1";
            pageSize = "10";
        }
        int  pageSizeInt = Integer.parseInt(pageSize);
        int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;

        try {

            Map<String, Object> paramMap = new HashedMap();
            String pcCompanyId = null;
            Map<String, Object> mapp = userService.selectUserById(loginUserId);
            //服务端
            if("2".equals(appFlag)){
                if(mapp !=null && mapp.size() > 0){
                    String roleId = (String)mapp.get("roleId");
                    if(!"1".equals(roleId)){
                        pcCompanyId = (String)mapp.get("pcCompanyId");
                        paramMap.put("pcCompanyId",pcCompanyId);
                    }
                }
            }else if("3".equals(appFlag)){
                paramMap.put("loginUserId",loginUserId);
                paramMap.put("pcCompanyId",mapp.get("pcCompanyId")==null?null:(String)mapp.get("pcCompanyId"));
            }else if("4".equals(appFlag)){
                List<String> companyIdList = companyContactorService.selectCompanyIdListByContactor(loginUserId,1);
                if (companyIdList != null && companyIdList.size() > 0) {
                    paramMap.put("companyIdList",companyIdList);
                }
            }


            String[] flags = null;
            String statusFlagStr = "";
            if(StringUtils.isNotBlank(statusFlag)){
                flags = statusFlag.split(",");
                for(int i =0;i<flags.length;i++){
                    statusFlagStr += "r.statusFlag="+flags[i] +"or";
                }
                statusFlagStr = "and ("+statusFlagStr.substring(0,statusFlagStr.indexOf("or"))+")";
            }
            //and r.statusFlag = #{statusFlag}

            paramMap.put("statusFlag",statusFlagStr);
            paramMap.put("companyName",companyName);
            paramMap.put("technicianName",technicianName);

            paramMap.put("userId",userId);
            paramMap.put("completeFlag",completeFlag);
            paramMap.put("startDate",startDate);
            paramMap.put("endDate",endDate);
            paramMap.put("dueTimeStartDate",dueTimeStartDate);
            paramMap.put("dueTimeEndDate",dueTimeEndDate);
            paramMap.put("currentPage",currentPageInt);
            paramMap.put("pageSize",pageSizeInt);

            List<Map<String,Object>>  repaireList = repaireService.selectRepaires(paramMap);
            Integer num = repaireService.selectTotalSelectRepaires(paramMap);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(repaireList);
            responseDbCenter.setTotalRows(num.intValue()+"");
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }



    /**
     *
     * @author: xiaoz
     * @2018年2月9日
     * @功能描述:驳回报修单
     * @param req
     * @return
     */
    @ApiOperation(value = "驳回报修单")
    @ResponseBody
    @RequestMapping(value = "/refuseRepaireApplyById",method = RequestMethod.POST)
    public ResponseDbCenter refuseRepaireApplyById(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value="报修单ID",required = false) @RequestParam(required = false) String id,
                                                @ApiParam(value="驳回原因",required = false) @RequestParam(required = false) String refuseReason
    ) throws Exception{


        if(StringUtils.isBlank(id)){
            return ResponseConstants.MISSING_PARAMTER;
        }


        try {

            Map<String,Object> map = new HashedMap();
            map.put("id",id);
            map.put("refuseReason",refuseReason);
            map.put("statusFlag",1);

            repaireService.updateRepaireApplyById(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    /**
     *
     * @author: xiaoz
     * @2018年2月9日
     * @功能描述:驳回报修单
     * @param req
     * @return
     */
    @ApiOperation(value = "维修工单催办，推送消息")
    @ResponseBody
    @RequestMapping(value = "/pushRepaireNews",method = RequestMethod.POST)
    public ResponseDbCenter pushRepaireNews(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value="用户ID,接收者",required = false) @RequestParam(required = false) String userIds,
                                                   @ApiParam(value="维修单号",required = false) @RequestParam(required = false) String repaireNo,
                                                   @ApiParam(value="维修工单ID",required = false) @RequestParam(required = false) String repaireId
    ) throws Exception{



        String loginUserId = (String)req.getAttribute("loginUserId");
        if(StringUtils.isBlank(repaireId)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            List<Map<String,Object>> technicians = repaireService.selectTechniciansByRepaireId(repaireId);
            List<Map<String,Object>> list = new ArrayList<>();
            for(Map<String,Object> m:technicians){
                String userId = (String)m.get("id");
                Map<String,Object> map = new HashedMap();
                map.put("id",UUID.randomUUID().toString());
                map.put("objectId",repaireId);
                map.put("appFlag",2); //2 服务端，3技师端，4客户端，只有服务端才能推送
                map.put("userId",userId);
                map.put("title","工单待领取");
                map.put("content","接收到新的维修工单任务"+repaireNo+"，请及时领取");
                map.put("createDate",new Date());
                map.put("createdBy",loginUserId);
                map.put("flag",0);
                map.put("newsType",0);//维修报修
                map.put("status",0);
                map.put("pushFlag","0");
                map.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/repaire.png");

                list.add(map);
            }

            objectNewsService.saveObjectNews(list);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    /**
     *
     * @param req
     * @param rsp
     * @param repaireId
     * @return
     * statusFlag,
       statusFlagValue
     */
    @ApiOperation(value = "根据ID查询维修工单当前的状态")
    @ResponseBody
    @RequestMapping(value = "/selectRepaireStatusByRepaireId",method = RequestMethod.POST)
    public ResponseDbCenter selectRepaireStatusByRepaireId(HttpServletRequest req, HttpServletResponse rsp,
                                                    @ApiParam(value="维修工单ID",required = false) @RequestParam(required = false) String repaireId
    ){

        if(StringUtils.isBlank(repaireId)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            Map<String, Object> status = repaireService.selectRepaireStatusById(repaireId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(status);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "更改维修工单状态")
    @ResponseBody
    @RequestMapping(value = "/updateRepaireStatusByRepaireId",method = RequestMethod.POST)
    public ResponseDbCenter updateRepaireStatusByRepaireId(HttpServletRequest req, HttpServletResponse rsp,
                                                           @ApiParam(value="维修工单ID",required = false) @RequestParam(required = false) String repaireId,
                                                           @ApiParam(value="处理过程",required = false) @RequestParam(required = false) String dealDescription,
                                                           @ApiParam(value="完工小结",required = false) @RequestParam(required = false) String summary,
                                                           @ApiParam(value="1 已受理（接单） 2 进行中（出发）   3 进行中（开始） 4 完成 （待评价）   5 完成（结单）",
                                                                   required = false) @RequestParam(required = false) String statusFlag
    ) throws Exception{

        String loginUserId = (String)req.getAttribute("loginUserId");
        if(StringUtils.isBlank(repaireId) || StringUtils.isBlank(statusFlag)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            Integer num = repaireService.updateRepaireStatusByRepaireId(repaireId,statusFlag,loginUserId,dealDescription,summary);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }





    @ApiOperation(value = "删除维修单，只能未接单状态的维修单才可以删除")
    @ResponseBody
    @RequestMapping(value = "/deleteRepaireByIds",method = RequestMethod.POST)
    public ResponseDbCenter deleteRepaireByIds(HttpServletRequest req, HttpServletResponse rsp,
                                                           @ApiParam(value="维修单ID",required = false) @RequestParam(required = false) String ids

    ) throws Exception{

        String loginUserId = (String)req.getAttribute("loginUserId");
        if(StringUtils.isBlank(ids)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        String[] rids = ids.split(",");
        List<String> repaireIds = new ArrayList<>();
        for(String id:rids){
            repaireIds.add(id);
        }
        try {
            Integer num = repaireService.deleteRepaireByIds(repaireIds);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    @ApiOperation(value = "工单详情里添加用户备注")
    @ResponseBody
    @RequestMapping(value = "/saveRepaireUserContent",method = RequestMethod.POST)
    public ResponseDbCenter saveRepaireUserContent(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value="维修单ID",required = false) @RequestParam(required = false) String repaireId,
                                               @ApiParam(value="备注内容",required = false) @RequestParam(required = false) String content

    ) throws Exception{


        if(StringUtils.isBlank(repaireId)){
            return ResponseConstants.MISSING_PARAMTER;
        }
        String loginUserId = (String)req.getAttribute("loginUserId");

        try {

            Map<String,Object> paramMap = new HashMap();
            paramMap.put("id",UUID.randomUUID().toString());
            paramMap.put("repaireId",repaireId);
            paramMap.put("userId",loginUserId);
            paramMap.put("content",content);
            Date now = new Date();
            paramMap.put("createDate",now);
            paramMap.put("status",0);

            Map<String, Object> userMap = userService.selectUserById(loginUserId);
            String uname = userMap.get("uname")==null?"":(String)userMap.get("uname");


            Integer num = repaireService.saveRepaireUserContent(paramMap);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            Map<String,Object> map = new HashedMap();
            map.put("uname",uname);
            map.put("createDate",DateUtils.dateToString(now,"yyyy-MM-dd HH:mm"));
            responseDbCenter.setResModel(map);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    @ApiOperation(value = "客户维修评价")
    @ResponseBody
    @RequestMapping(value = "/saveUserCommentByRepaireId",method = RequestMethod.POST)
    public ResponseDbCenter saveUserCommentByRepaireId(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value="维修单ID",required = false) @RequestParam(required = false) String repaireId,
                                                   @ApiParam(value="响应速度评分,3.5",required = false) @RequestParam(required = false) String responseScore,
                                                   @ApiParam(value="服务态度评分,4",required = false) @RequestParam(required = false) String altitudeScore,
                                                   @ApiParam(value="专业程序评分 2.5",required = false) @RequestParam(required = false) String professionScore,
                                                   @ApiParam(value="客户反馈",required = false) @RequestParam(required = false) String customerFeedback

    ) throws Exception{

        if(StringUtils.isBlank(repaireId)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            Map<String,Object> paramMap = new HashMap();

            paramMap.put("id",repaireId);
            paramMap.put("responseScore",responseScore);
            paramMap.put("altitudeScore",altitudeScore);
            paramMap.put("professionScore",professionScore);
            paramMap.put("customerFeedback",customerFeedback);
            paramMap.put("updateDate",new Date());

            repaireService.updateById(paramMap);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

}
