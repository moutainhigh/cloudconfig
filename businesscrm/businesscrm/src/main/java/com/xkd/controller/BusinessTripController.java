package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.BusinessTripService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
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
 * Created by dell on 2018/5/4.
 */
@Api(description = "拜访相关接口")
@Controller
@RequestMapping("/businessTrip")
@Transactional
public class BusinessTripController extends BaseController {

    @Autowired
    BusinessTripService businessTripService;


    @ApiOperation(value = "添加拜访")
    @ResponseBody
    @RequestMapping(value = "/addBusinessTrip", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter addBusinessTrip(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "客户Id ", required = true) @RequestParam(required = true) String companyId,
                                            @ApiParam(value = "主题Id ", required = true) @RequestParam(required = true) String businessTripId,
                                            @ApiParam(value = "开始时间 ", required = true) @RequestParam(required = true) String startDate,
                                            @ApiParam(value = "结束时间 ", required = true) @RequestParam(required = true) String endDate,
                                            @ApiParam(value = "备注 ", required = false) @RequestParam(required = false) String remark,
                                            @ApiParam(value = "负责人Id ", required = false) @RequestParam(required = false) String responsibleUserId
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", UUID.randomUUID().toString());
            map.put("companyId", companyId);
            map.put("businessTripId", businessTripId);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("remark", remark);
            map.put("responsibleUserId", responsibleUserId);
            map.put("createdBy",loginUserId);
            map.put("createDate",new Date());
            map.put("updatedBy",loginUserId);
            map.put("updateDate",new Date());
            businessTripService.insertBusinessTrip(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

    }




    @ApiOperation(value = "更新拜访")
    @ResponseBody
    @RequestMapping(value = "/updateBusinessTrip", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter updateBusinessTrip(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "id ", required = true) @RequestParam(required = true) String id,
                                            @ApiParam(value = "客户Id ", required = true) @RequestParam(required = true) String companyId,
                                            @ApiParam(value = "主题Id ", required = true) @RequestParam(required = true) String businessTripId,
                                            @ApiParam(value = "开始时间 ", required = true) @RequestParam(required = true) String startDate,
                                            @ApiParam(value = "结束时间 ", required = true) @RequestParam(required = true) String endDate,
                                            @ApiParam(value = "备注 ", required = false) @RequestParam(required = false) String remark,
                                            @ApiParam(value = "负责人Id ", required = false) @RequestParam(required = false) String responsibleUserId
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("companyId", companyId);
            map.put("businessTripId", businessTripId);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("remark", remark);
            map.put("responsibleUserId", responsibleUserId);
             map.put("updatedBy",loginUserId);
            map.put("updateDate",new Date());
            businessTripService.insertBusinessTrip(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

    }



    @ApiOperation(value = "删除拜访")
    @ResponseBody
    @RequestMapping(value = "/deleteBusinessTrip", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter deleteBusinessTrip(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "ids  多个用逗号分隔 ", required = true) @RequestParam(required = true) String ids
    ) throws Exception {

        try {
            if (StringUtils.isNotBlank(ids)){
                String[] idArray=ids.split(",");
                List<String> idList=Arrays.asList(idArray);
                businessTripService.deleteBusinessTrip(idList);
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

    }



    @ApiOperation(value = "搜索拜访")
    @ResponseBody
    @RequestMapping(value = "/searchBusinessTrip", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter searchBusinessTrip(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "开始时间 格式如:2012-10-11", required = true) @RequestParam(required = true) String startDate,
                                               @ApiParam(value = "结束时间 格式如:2012-10-11", required = true) @RequestParam(required = true) String endDate,
                                               @ApiParam(value = "人员Id", required = true) @RequestParam(required = true) String responsibleUserId,
                                               @ApiParam(value = "当前第几页", required = true) @RequestParam(required = true) Integer currentPage,
                                               @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    ) throws Exception {

        List<Map<String,Object>>  list=new ArrayList<>();
        Integer count=0;
        try {
            list=businessTripService.searchBusinessTrip(startDate,endDate,responsibleUserId,currentPage,pageSize);
            count=businessTripService.searchBusinessTripCount(startDate,endDate,responsibleUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(count+"");
            return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

    }






}
