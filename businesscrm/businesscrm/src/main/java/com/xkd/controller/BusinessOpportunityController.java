package com.xkd.controller;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.BusinessOpportunityService;
import com.xkd.service.CompanyService;
import com.xkd.service.PagerFileService;
import com.xkd.service.UserDynamicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by dell on 2017/12/22.
 */


@Api(description = "企业商机相关接口")
@Controller
@RequestMapping("/businessOpportunity")
@Transactional
public class BusinessOpportunityController extends BaseController {

    @Autowired
    PagerFileService pagerFileService;
    @Autowired
    BusinessOpportunityService businessOpportunityService;

    @Autowired
    CompanyService companyService;


    @Autowired
    UserDynamicService userDynamicService;

    @ApiOperation(value = "公司下的商机列表")
    @ResponseBody
    @RequestMapping(value = "selectBusinessOpportunityListByCompanyId", method = RequestMethod.POST)
    public ResponseDbCenter selectBusinessOpportunityListByCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                                                     @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId,
                                                                     @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                                                     @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    ) {
        String loginUserId = (String) req.getAttribute("loginUserId");
        try {
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            Map<String, Object> map = businessOpportunityService.selectBusinessOpportunity(0,null, null, null, companyId, null, null, null, loginUserId, null,null,null, currentPage, pageSize);
            responseDbCenter.setResModel(map.get("list"));
            responseDbCenter.setTotalRows(map.get("count") + "");
            return responseDbCenter;
        } catch (Exception e) {
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


    @ApiOperation(value = "查询商机列表")
    @ResponseBody
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "selectBusinessOpportunity")
    public ResponseDbCenter selectBusinessOpportunity(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "搜索值", required = false) @RequestParam(required = false) String searchValue,
                                                      @ApiParam(value = "阶段Id", required = false) @RequestParam(required = false) String phaseId,
                                                      @ApiParam(value = "商机类型Id", required = false) @RequestParam(required = false) String businessTypeId,
                                                      @ApiParam(value = "商机来源Id", required = false) @RequestParam(required = false) String businessSourceId,
                                                      @ApiParam(value = "公司Id", required = false) @RequestParam(required = false) String companyId,
                                                      @ApiParam(value = "商机名称", required = false) @RequestParam(required = false) String opportunityName,
                                                      @ApiParam(value = "部门Id", required = false) @RequestParam(required = false) String departmentId,
                                                      @ApiParam(value = "时间起 2012-10-11", required = false) @RequestParam(required = false) String startDate,
                                                      @ApiParam(value = "时间止 2012-10-11", required = false) @RequestParam(required = false) String endDate,
                                                      @ApiParam(value = "标志位   1  我负责的客户 2 我参与的客户  3 总监是我的客户    5 全部客户  6 我负责的商机 7 我创建的商机  ", required = false) @RequestParam(required = false) Integer publicFlag,
                                                      @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                                      @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize

    ) {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            Map<String, Object> map = businessOpportunityService.selectBusinessOpportunity(1,searchValue, businessTypeId, businessSourceId, companyId, phaseId, opportunityName, departmentId, loginUserId, publicFlag,startDate,endDate, currentPage, pageSize);
            responseDbCenter.setResModel(map.get("list"));
            responseDbCenter.setTotalRows(map.get("count") + "");
            return responseDbCenter;
        } catch (Exception e) {
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


    @ApiOperation(value = "商机详情")
    @ResponseBody
    @RequestMapping(value = "businessOpportunityDetail", method = RequestMethod.POST)
    public ResponseDbCenter businessOpportunityDetail(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "Id", required = true) @RequestParam(required = true) String id
    ) {
        try {
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            Map<String, Object> map = businessOpportunityService.getBusinessOpportunityById(id);
            responseDbCenter.setResModel(map);
            return responseDbCenter;
        } catch (Exception e) {
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


    @ApiOperation(value = "添加企业商机")
    @ResponseBody
    @RequestMapping(value = "addBusinessOpportunity", method = RequestMethod.POST)
    public ResponseDbCenter addBusinessOpportunity(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId,
                                                   @ApiParam(value = "商机名称", required = true) @RequestParam(required = true) String opportunityName,
                                                   @ApiParam(value = "负责人Id", required = false) @RequestParam(required = false) String responsibleUserId,
                                                   @ApiParam(value = "联系人姓名", required = false) @RequestParam(required = false) String contact,
                                                   @ApiParam(value = "联系人电话", required = false) @RequestParam(required = false) String contactMobile,
                                                   @ApiParam(value = "销售金额", required = false) @RequestParam(required = false) String salesMoney,
                                                   @ApiParam(value = "销售阶段Id", required = false) @RequestParam(required = false) String phaseId,
                                                   @ApiParam(value = "实施时间 2012-10-11" , required = false) @RequestParam(required = false) String implementDate,
                                                   @ApiParam(value = "商机类型Id", required = false) @RequestParam(required = false) String businessTypeId,
                                                   @ApiParam(value = "商机来源Id", required = false) @RequestParam(required = false) String businessSourceId,
                                                   @ApiParam(value = "备注", required = false) @RequestParam(required = false) String remark
    ) {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            String id = UUID.randomUUID().toString();
            businessOpportunityService.insertBusinessOppotunity(id, companyId, opportunityName, responsibleUserId, contact, contactMobile, salesMoney, phaseId, implementDate, remark, businessTypeId, businessSourceId, loginUserId);
            //企业下面动态
            userDynamicService.addUserDynamic(loginUserId, companyId, null, "添加", "添加企业商机\"" + opportunityName + "\"", 0, null, null, null);
            //商机下面动态
            userDynamicService.addUserDynamic(loginUserId, id, null, "添加", "添加企业商机\"" + opportunityName + "\"", 0, null, null, null);

            //初始化企业文件夹跟商机文件夹
            pagerFileService.initializationCompanyFolder(companyId + "__0", loginUserId);
            //初始化当前商机
            pagerFileService.addBusinessOppotunityFolder(companyId, opportunityName, id, loginUserId);


            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


    @ApiOperation(value = "更新企业商机")
    @ResponseBody
    @RequestMapping(value = "updateBusinessOpportunity", method = RequestMethod.POST)
    public ResponseDbCenter updateBusinessOpportunity(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "Id", required = true) @RequestParam(required = true) String id,
                                                      @ApiParam(value = "公司Id", required = false) @RequestParam(required = false) String companyId,
                                                      @ApiParam(value = "商机名称", required = false) @RequestParam(required = false) String opportunityName,
                                                      @ApiParam(value = "负责人Id", required = false) @RequestParam(required = false) String responsibleUserId,
                                                      @ApiParam(value = "联系人姓名", required = false) @RequestParam(required = false) String contact,
                                                      @ApiParam(value = "联系人电话", required = false) @RequestParam(required = false) String contactMobile,
                                                      @ApiParam(value = "销售金额", required = false) @RequestParam(required = false) String salesMoney,
                                                      @ApiParam(value = "销售阶段Id", required = false) @RequestParam(required = false) String phaseId,
                                                      @ApiParam(value = "实施时间 2012-10-11", required = false) @RequestParam(required = false) String implementDate,
                                                      @ApiParam(value = "商机类型Id", required = false) @RequestParam(required = false) String businessTypeId,
                                                      @ApiParam(value = "商机来源Id", required = false) @RequestParam(required = false) String businessSourceId,
                                                      @ApiParam(value = "备注", required = false) @RequestParam(required = false) String remark
    ) {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
            businessOpportunityService.updateBusinessOppotunity(id, companyId, opportunityName, responsibleUserId, contact, contactMobile, salesMoney, phaseId, implementDate, remark, businessTypeId, businessSourceId, null, loginUserId);

            Map<String, Object> businessOpporunityMap = businessOpportunityService.getBusinessOpportunityById(id);
            //企业下面动态
            userDynamicService.addUserDynamic(loginUserId, (String) businessOpporunityMap.get("companyId"), null, "更新", "更新企业商机\"" + businessOpporunityMap.get("opportunityName") + "\"", 0, null, null, null);
            //商机下面动态
            userDynamicService.addUserDynamic(loginUserId, (String) businessOpporunityMap.get("id"), null, "更新", "更新企业商机\"" + businessOpporunityMap.get("opportunityName") + "\"", 0, null, null, null);


            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


    @ApiOperation(value = "删除商机")
    @ResponseBody
    @RequestMapping(value = "deleteBusinessOpportunity", method = RequestMethod.POST)
    public ResponseDbCenter deleteBusinessOpportunity(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "ids,多个值以逗号分隔", required = true) @RequestParam(required = true) String ids
    ) {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            if (StringUtils.isNotBlank(ids)) {
                List<String> idList = new ArrayList<>();
                String[] idArray = ids.split(",");
                for (int i = 0; i < idArray.length; i++) {
                    idList.add(idArray[i]);
                }
                businessOpportunityService.deleteBusinessOpportunities(idList);


                List<Map<String, Object>> mapList = businessOpportunityService.selectBusinessOpportunityByIds(idList);
                for (int i = 0; i < mapList.size(); i++) {
                    userDynamicService.addUserDynamic(loginUserId, (String) mapList.get(i).get("companyId"), null, "删除", "删除企业商机\"" + mapList.get(i).get("opportunityName") + "\"", 0, null, null, null);
                }
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


}
