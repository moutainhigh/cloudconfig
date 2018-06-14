package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.CompanyContactorService;
import com.xkd.service.InspectionTaskService;
import com.xkd.service.ObjectNewsService;
import com.xkd.service.UserService;
import com.xkd.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

/**
 * Created by dell on 2018/2/24.
 */
@Api(description = "巡检任务")
@Controller
@RequestMapping("/inspectionTask")
@Transactional
public class InspectionTaskController extends BaseController {


    @Autowired
    InspectionTaskService inspectionTaskService;
    @Autowired
    CompanyContactorService companyContactorService;

    @Autowired
    ObjectNewsService objectNewsService;


    @Autowired
    UserService userService;

    @ApiOperation(value = "查询某一巡检计划下的历史巡检任务---通用")
    @ResponseBody
    @RequestMapping(value = "/searchHistoryInspectionTask", method = {RequestMethod.POST})
    public ResponseDbCenter searchHistoryInspectionTask(HttpServletRequest req, HttpServletResponse rsp,
                                                        @ApiParam(value = "巡检计划ID", required = true) @RequestParam(required = true) String inspectionPlanId,
                                                        @ApiParam(value = "时间起  如 2012-10-10", required = false) @RequestParam(required = false) String fromDate,
                                                        @ApiParam(value = "时间止 如 2012-10-10", required = false) @RequestParam(required = false) String toDate,
                                                        @ApiParam(value = "是否逾期  1 是  0 否  不传表示全部", required = false) @RequestParam(required = false) Integer isExceedTime,
                                                        @ApiParam(value = "完成人", required = false) @RequestParam(required = false) String completedBy,
                                                        @ApiParam(value = "当前多少页", required = false) @RequestParam(required = false) Integer currentPage,
                                                        @ApiParam(value = "每页多少条记录", required = false) @RequestParam(required = false) Integer pageSize


    ) throws Exception {
        try {
            List<Map<String, Object>> inspectionTaskList = inspectionTaskService.selectHistoryTaskByPlanId(inspectionPlanId, fromDate,toDate,isExceedTime,completedBy, currentPage, pageSize);
            Integer total = inspectionTaskService.selectHistoryTaskCountByPlanId(inspectionPlanId, fromDate, toDate,isExceedTime,completedBy);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setTotalRows(total + "");
            responseDbCenter.setResModel(inspectionTaskList);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "搜索巡检任务--服务端")
    @ResponseBody
    @RequestMapping(value = "/searchInspectionTask", method = {RequestMethod.POST})
    public ResponseDbCenter searchInspectionTask(HttpServletRequest req, HttpServletResponse rsp,
                                                 @ApiParam(value = "类型 1  日常巡检 2 每周巡检 3 月度巡检 4 季度巡检 5 年度巡检", required = false) @RequestParam(required = false) Integer period,
                                                 @ApiParam(value = "客户Id", required = false) @RequestParam(required = false) String companyId,
                                                 @ApiParam(value = "班组Id", required = false) @RequestParam(required = false) String departmentId,
                                                 @ApiParam(value = "开始时间 2011-11-11", required = false) @RequestParam(required = false) String fromDate,
                                                 @ApiParam(value = "结束时间  2011-11-11 ", required = false) @RequestParam(required = false) String toDate,
                                                 @ApiParam(value = "是否已完成 0  未完成  1 已完成   2 已逾期 不传或其它值表示全部", required = false) @RequestParam(required = false) Integer isDone,
                                                 @ApiParam(value = "当前多少页", required = true) @RequestParam(required = true) Integer currentPage,
                                                 @ApiParam(value = "每页多少条记录", required = true) @RequestParam(required = true) Integer pageSize


    ) throws Exception {
        try {


            String loginUserId = (String) req.getAttribute("loginUserId");
//            String loginUserId = "818";

            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

            List<Map<String, Object>> inspectionTaskList = null;
            Integer total = 0;

                inspectionTaskList = inspectionTaskService.searchInspectionTask((String) loginUserMap.get("pcCompanyId"), period, companyId, departmentId,fromDate,toDate,isDone, currentPage, pageSize);
                total = inspectionTaskService.searchInspectionTaskCount((String) loginUserMap.get("pcCompanyId"), period, companyId, departmentId,fromDate,toDate,isDone);




            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setTotalRows(total + "");
            responseDbCenter.setResModel(inspectionTaskList);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "搜索巡检任务--技师端")
    @ResponseBody
    @RequestMapping(value = "/searchInspectionTaskTechnician", method = {RequestMethod.POST})
    public ResponseDbCenter searchInspectionTaskTechnician(HttpServletRequest req, HttpServletResponse rsp,
                                                            @ApiParam(value = "开始时间 2011-11-11", required = false) @RequestParam(required = false) String fromDate,
                                                           @ApiParam(value = "结束时间  2011-11-11 ", required = false) @RequestParam(required = false) String toDate,
                                                           @ApiParam(value = "客户Id ", required = false) @RequestParam(required = false) String companyId,
                                                           @ApiParam(value = "类型 巡检任务类型  1 每天 2 每周 3 每月 4 每季度 5 每年 ", required = false) @RequestParam(required = false) Integer period,
                                                           @ApiParam(value = "是否已完成 0  未完成  1 已完成   2 已逾期 不传或其它值表示全部", required = false) @RequestParam(required = false) Integer isDone,
                                                           @ApiParam(value = "当前多少页", required = true) @RequestParam(required = true) Integer currentPage,
                                                           @ApiParam(value = "每页多少条记录", required = true) @RequestParam(required = true) Integer pageSize


    ) throws Exception {
        try {


            String loginUserId = (String) req.getAttribute("loginUserId");
//            String loginUserId = "818";

            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

            List<Map<String, Object>> inspectionTaskList = null;
            Integer total = 0;
            inspectionTaskList = inspectionTaskService.searchTechnicalInspectionTask((String) loginUserMap.get("departmentId"),period,companyId,fromDate,toDate,isDone, currentPage, pageSize);
            total = inspectionTaskService.searchTechnicalInspectionTaskCount((String) loginUserMap.get("departmentId"),period,companyId,fromDate,toDate,isDone);


            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setTotalRows(total + "");
            responseDbCenter.setResModel(inspectionTaskList);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }





    @ApiOperation(value = "搜索巡检报告--客户端")
    @ResponseBody
    @RequestMapping(value = "/searchInspectionTaskCustomer", method = {RequestMethod.POST})
    public ResponseDbCenter searchInspectionTaskCustomer(HttpServletRequest req, HttpServletResponse rsp,
                                                            @ApiParam(value = "时间  2012-10-11", required = false) @RequestParam(required = false) String dateFrom,
                                                            @ApiParam(value = "时间  2012-10-11", required = false) @RequestParam(required = false) String dateTo,
                                                         @ApiParam(value = "当前多少页", required = true) @RequestParam(required = true) Integer currentPage,
                                                            @ApiParam(value = "每页多少条记录", required = true) @RequestParam(required = true) Integer pageSize


    ) throws Exception {
        try {


            String loginUserId = (String) req.getAttribute("loginUserId");

             List<String> companyIdList=companyContactorService.selectCompanyIdListByContactor(loginUserId,1);
            List<Map<String, Object>> inspectionTaskList = null;
            Integer total = 0;
            inspectionTaskList = inspectionTaskService.searchCustomerInspectionTask(dateFrom,dateTo,companyIdList,currentPage,pageSize);
            total = inspectionTaskService.searchCustomerInspectionTaskCount(dateFrom,dateTo,companyIdList);


            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setTotalRows(total + "");
            responseDbCenter.setResModel(inspectionTaskList);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }




    @ApiOperation(value = "巡检任务详情----通用")
    @ResponseBody
    @RequestMapping(value = "/taskDetail", method = {RequestMethod.POST})
    public ResponseDbCenter taskDetail(HttpServletRequest req, HttpServletResponse rsp,
                                       @ApiParam(value = "任务Id", required = false) @RequestParam(required = false) String id



    ) throws Exception {
        try {

            Map<String,Object> taskMap=inspectionTaskService.selectTaskDetail(id);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(taskMap);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "插入任务备注----服务商，技师端")
    @ResponseBody
    @RequestMapping(value = "/insertTaskNote", method = {RequestMethod.POST})
    public ResponseDbCenter insertTaskNote(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "任务Id", required = false) @RequestParam(required = false) String inspectionTaskId,
                                           @ApiParam(value = "备注", required = false) @RequestParam(required = false) String description
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            Map<String,Object> taskMap=inspectionTaskService.selectTaskDetail(inspectionTaskId);
            Map<String,Object> map=new HashMap<>();
            map.put("id", UUID.randomUUID().toString());
            map.put("inspectionPlanId",taskMap.get("inspectionPlanId"));
            map.put("inspectionTaskId",inspectionTaskId);
            map.put("description",description);
            map.put("createDate",new Date());
            map.put("createdBy",loginUserId);
            inspectionTaskService.insertTaskNote(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "完成任务备注---服务端，技师端")
    @ResponseBody
    @RequestMapping(value = "/completeTaskNote", method = {RequestMethod.POST})
    public ResponseDbCenter completeTaskNote(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "任务Id", required = false) @RequestParam(required = false) String id
     ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            Map<String,Object> map=new HashMap<>();
            map.put("id",id);
             map.put("updateDate",new Date());
            map.put("updatedBy",loginUserId);
            inspectionTaskService.completeTaskNote(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }




    @ApiOperation(value = "按时间查询某一个计划的巡检任务---")
    @ResponseBody
    @RequestMapping(value = "/selectInspectionTaskByInspectionId", method = {RequestMethod.POST})
    public ResponseDbCenter selectInspectionTaskByInspectionId(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "计划Id", required = false) @RequestParam(required = false) String planId,
                                             @ApiParam(value = "开始时间", required = false) @RequestParam(required = false) String fromTime,
                                             @ApiParam(value = "结束时间", required = false) @RequestParam(required = false) String toTime
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
             List<Map<String,Object>> mapList= inspectionTaskService.selectInspectionTaskByInspectionId(planId, fromTime, toTime);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(mapList);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }







    @ApiOperation(value = "完成巡检----技师端")
    @ResponseBody
    @RequestMapping(value = "/completeInspectionTask", method = {RequestMethod.POST})
    public ResponseDbCenter completeInspectionTask(HttpServletRequest req, HttpServletResponse rsp,
                                                               @ApiParam(value = "巡检任务Id", required = false) @RequestParam(required = false) String inspectionTaskId
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String,Object>  map=new HashMap<>();
            map.put("id",inspectionTaskId);
            map.put("updatedBy",loginUserId);
            map.put("updateDate",new Date());
            map.put("completedBy",loginUserId);
            map.put("completionDate",new Date());
            inspectionTaskService.updateInspectionTask(map);



            Map<String,Object> taskMap=inspectionTaskService.getTaskById(inspectionTaskId);

            //推送消息
            List<String> customerUserIdList=  companyContactorService.selectAllUserIdByPcCompanyIdAndCompanyId((String)taskMap.get("pcCompanyId"),(String) taskMap.get("companyId"));

            List<Map<String,Object>>  newsList=new ArrayList<>();
            for (int i = 0; i <customerUserIdList.size(); i++) {
                Map<String,Object>    newsMap=new HashMap<>();
                newsMap.put("id",UUID.randomUUID().toString());
                newsMap.put("objectId",inspectionTaskId);
                newsMap.put("appFlag",4);
                newsMap.put("userId",customerUserIdList.get(i));
                newsMap.put("title","巡检消息");
                newsMap.put("content","编号"+taskMap.get("taskNo")+"巡检任务由"+taskMap.get("completedByName")+"完成");
                newsMap.put("createDate",new Date());
                newsMap.put("createdBy",loginUserId);
                newsMap.put("status",0);
                newsMap.put("flag",0);
                newsMap.put("pushFlag",0);
                newsMap.put("newsType",5);
                newsMap.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/msg.png");
                newsList.add(newsMap);
            }
            objectNewsService.saveObjectNews(newsList);

             ResponseDbCenter responseDbCenter = new ResponseDbCenter();
             return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }






}
