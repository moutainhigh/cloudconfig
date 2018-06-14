package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.InspectionPlanService;
import com.xkd.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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

/**
 * Created by dell on 2018/2/24.
 */
@Api(description = "巡检计划")
@Controller
@RequestMapping("/inspection")
@Transactional
public class InspectionController extends BaseController {

    @Autowired
    InspectionPlanService inspectionPlanService;

    @Autowired
    UserService userService;


    @ApiOperation(value = "添加巡检计划--服务商")
    @ResponseBody
    @RequestMapping(value = "/addInspection", method = {RequestMethod.POST})
    public ResponseDbCenter addDeviceGroup(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "客户Id", required = true) @RequestParam(required = true) String companyId,
                                           @ApiParam(value = "设备组Ids，多个值中间用逗号隔开", required = true) @RequestParam(required = true) String groupIds,
                                           @ApiParam(value = "班组Id", required = true) @RequestParam(required = true) String departmentId,
                                           @ApiParam(value = "合同Id", required = true) @RequestParam(required = true) String contractId,
                                           @ApiParam(value = "巡检周期 1 每天 2 每周 3 每月 4 每季度  5 每年", required = true) @RequestParam(required = true) Integer period,
                                           @ApiParam(value = "巡检时间,多个时间用逗号分隔  如10:00,12:00   多个值用逗号分隔 ", required = false) @RequestParam(required = true) String inspectionTimePoints,
                                           @ApiParam(value = "开始时间 如2014-10-11", required = true) @RequestParam(required = true) String startTime,
                                           @ApiParam(value = "结束时间 如2014-10-11", required = true) @RequestParam(required = true) String endTime,
                                           @ApiParam(value = "巡检模板Id", required = true) @RequestParam(required = true) String templateId,
                                           @ApiParam(value = "补充说明", required = false) @RequestParam(required = false) String description


    ) throws Exception {
        try {
//                 String loginUserId = "818";
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
            List<String> timepointList = new ArrayList<>();
            if (StringUtils.isNotBlank(inspectionTimePoints)) {
                String[] strs = inspectionTimePoints.split(",");
                for (int i = 0; i < strs.length; i++) {
                    timepointList.add(strs[i].trim());
                }
            }

            List<String> groupIdList = new ArrayList<>();
            if (StringUtils.isNotBlank(groupIds)) {
                String[] strs = groupIds.split(",");
                for (int i = 0; i < strs.length; i++) {
                    groupIdList.add(strs[i].trim());
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("groupIdList", groupIdList);
            map.put("timepointList", timepointList);
            map.put("inspectionTimePoint",inspectionTimePoints);
            map.put("companyId", companyId);
            map.put("contractId", contractId);
            map.put("period", period);
            map.put("departmentId", departmentId);
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("templateId", templateId);
            map.put("description", description);
            map.put("pcCompanyId", loginUserMap.get("pcCompanyId"));


            inspectionPlanService.saveInspectionPlan(map, loginUserId);


            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "检索巡检计划--服务商")
    @ResponseBody
    @RequestMapping(value = "/searchInspection", method = {RequestMethod.POST})
    public ResponseDbCenter searchInspection(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "类型 1  日常巡检 2 每周巡检 3 月度巡检 4 季度巡检 5 年度巡检", required = false) @RequestParam(required = false) Integer period,
                                             @ApiParam(value = "客户", required = false) @RequestParam(required = false) String companyId,
                                             @ApiParam(value = "班组", required = false) @RequestParam(required = false) String departmentId,
                                             @ApiParam(value = "当前多少页", required = true) @RequestParam(required = true) Integer currentPage,
                                             @ApiParam(value = "每页多少条记录", required = true) @RequestParam(required = true) Integer pageSize



    ) throws Exception {
        try {
//            String loginUserId = "818";
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

            Integer total=0;
            List<Map<String,Object>> list=null;

                list=inspectionPlanService.searchInspectionPlan((String) loginUserMap.get("pcCompanyId"),period,companyId,departmentId,currentPage,pageSize);
                total=inspectionPlanService.searchInspectionPlanCount((String) loginUserMap.get("pcCompanyId"),period,companyId,departmentId);



            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setTotalRows(total+"");
            responseDbCenter.setResModel(list);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }





    @ApiOperation(value = "删除巡检计划--服务商")
    @ResponseBody
    @RequestMapping(value = "/deleteInspection", method = {RequestMethod.POST})
    public ResponseDbCenter deleteInspection(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "id", required = true) @RequestParam(required = true) String id



    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");


            inspectionPlanService.deleteInspectionPlan(id,loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();

            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }






    @ApiOperation(value = "查看巡检计划详情")
    @ResponseBody
    @RequestMapping(value = "/inspectionDetail", method = {RequestMethod.POST})
    public ResponseDbCenter inspectionDetail(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "id", required = true) @RequestParam(required = true) String id



    ) throws Exception {
        try {
            Map<String,Object> map=inspectionPlanService.selectInspectionById(id);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(map);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


}
