package com.xkd.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkd.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.User;

/**
 * 创建人：巫建辉
 * 创建时间：2017-11-23
 * 功能描述：项目相关功能
 */
@Api(description = "项目相关接口")
@RequestMapping("/project")
@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    UserDynamicService userDynamicService;

    @Autowired
    SolrService solrService;

    @Autowired
    UserDataPermissionService userDataPermissionService;

    /**
     * 添加项目
     *
     * @param req
     * @param rsp
     * @param project :
     * @return
     */

    @ApiOperation(value = "添加项目")
    @ResponseBody
    @RequestMapping(value = "/addProject", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter addProject(HttpServletRequest req, HttpServletResponse rsp, @ApiParam(value = "" +
            "                  {\n" +
            "                      \"projectName\": \"我的项目222\",\n" +
            "                      \"companyId\": \"0069ce9c-b850-440d-bf86-9dcd2778b507\",\n" +
            "                     \"parentIndustryId\": \"1440\",\n" +
            "                      \"sonIndustry\":\"制造业\",\n" +
            "                      \"projectLevelId\":\"12\",\n" +
            "                      \"description\": \"项目描述\",\n" +
            "                     \"analysis\": \"这个项目靠谱\",\n" +
            "                      \"initUser\": \"818\",\n" +
            "                     \"contactPhone\": \"13100000000\",\n" +
            "                      \"scope\": \"china\",\n" +
            "                      \"teamSize\": 12,\n" +
            "                      \"annualSalesVolume\": \"1000\",\n" +
            "                      \"annualProfit\": \"1222\",\n" +
            "                      \"thisYearSalesVolume\": \"121\",\n" +
            "                      \"nextYearSalesVolume\": \"121\",\n" +
            "                      \"alreadyInvest\": \"1222万\",\n" +
            "                      \"expectTotalInvest\": \"100万\",\n" +
            "                      \"planFinancing\": \"10000万\",\n" +
            "                      \"expectReleaseStockRate\": \"23\",\n" +
            "                      \"status\": \"12\",\n" +
            "                      \"directorId\": \"2\",\n" +
            "                      \"adviserId\": \"1\"\n" +
            "                      }" +
            "",required = true) @RequestParam(required = true) String project) {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        if (StringUtil.isBlank(project)) {
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {


            Map<String, Object> map = JSON.parseObject(project, new TypeReference<Map<String, Object>>() {
            });

            /**
             * 判断企业是否有权限被修改
             */
            boolean hasPermission=userDataPermissionService.hasPermission((String)map.get("companyId"),loginUserId);
            if (!hasPermission){
                return  ResponseConstants.DATA_NOT_PERMITED;
            }


            map.put("sonIndustry", map.get("sonIndustryId"));
            map.put("id", UUID.randomUUID().toString());
            map.put("status", "0");
            map.put("createdBy", loginUserId);
            map.put("createDate", new Date());
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());
            projectService.insertProject(map);
            userDynamicService.addUserDynamic(loginUserId, map.get("companyId").toString(), "", "添加", "添加了项目：" + map.get("projectName"), 0,null,null,null);

            solrService.updateCompanyIndex((String)map.get("companyId"));


            return ResponseConstants.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }


    }


    /**
     * 更新项目
     *
     * @param req
     * @param rsp
     * @param project:
     * @return
     */
    @ApiOperation(value = "更新项目")
    @ResponseBody
    @RequestMapping(value = "/updateProject", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter updateProject(HttpServletRequest req, HttpServletResponse rsp,@ApiParam(value = "{\n" +
            "                     \"id\":\"21545125\",\n" +
            "                      \"projectName\": \"我的项目222\",\n" +
            "                      \"companyId\": \"0069ce9c-b850-440d-bf86-9dcd2778b507\",\n" +
            "                      \"parentIndustryId\": \"1440\",\n" +
            "                      \"sonIndustry\":\"制造业\",\n" +
            "                      \"projectLevelId\":\"12\",\n" +
            "                      \"description\": \"项目描述\",\n" +
            "                      \"analysis\": \"这个项目靠谱\",\n" +
            "                      \"initUser\": \"818\",\n" +
            "                      \"contactPhone\": \"13100000000\",\n" +
            "                      \"scope\": \"china\",\n" +
            "                      \"teamSize\": 12,\n" +
            "                      \"annualSalesVolume\": \"1000\",\n" +
            "                      \"annualProfit\": \"1222\",\n" +
            "                     \"thisYearSalesVolume\": \"121\",\n" +
            "                      \"nextYearSalesVolume\": \"121\",\n" +
            "                      \"alreadyInvest\": \"1222万\",\n" +
            "                     \"expectTotalInvest\": \"100万\",\n" +
            "                      \"planFinancing\": \"10000万\",\n" +
            "                     \"expectReleaseStockRate\": \"23\",\n" +
            "                      \"status\": \"12\",\n" +
            "                     \"directorId\": \"2\",\n" +
            "                      \"adviserId\": \"1\"\n" +
            "                      }",required = true)   @RequestParam(required = true)  String project) {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        if (StringUtil.isBlank(project)) {
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {
            Map<String, Object> map = JSON.parseObject(project, new TypeReference<Map<String, Object>>() {
            });

            /**
             * 判断企业是否有权限被修改
             */
            boolean hasPermission=userDataPermissionService.hasPermission((String)map.get("companyId"),loginUserId);
            if (!hasPermission){
                return  ResponseConstants.DATA_NOT_PERMITED;
            }


            map.put("sonIndustry", map.get("sonIndustryId"));
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());
            projectService.updateProject(map);
            userDynamicService.addUserDynamic(loginUserId, map.get("companyId").toString(), "", "更新", "更新了项目：" + map.get("projectName"), 0,null,null,null);
            solrService.updateCompanyIndex((String)map.get("companyId"));



            return ResponseConstants.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


    /**
     * 删除项目
     *
     * @param req
     * @param rsp
     * @param id  项目Id
     * @return
     * @throws Exception
     */

    @ApiOperation(value = "删除项目")
    @ResponseBody
    @RequestMapping(value = "/deleteProject",method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter deleteProject(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "项目Id" ,required = true) @RequestParam(required = true)  String id) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");

        if (StringUtil.isBlank(id)) {
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            Map<String, Object> projectMap = projectService.selectProjectById(id);
            /**
             * 判断企业是否有权限被修改
             */
            boolean hasPermission=userDataPermissionService.hasPermission((String)projectMap.get("companyId"),loginUserId);
            if (!hasPermission){
                return  ResponseConstants.DATA_NOT_PERMITED;
            }



            projectService.deleteProject(id);
            userDynamicService.addUserDynamic(loginUserId, projectMap.get("companyId").toString(), "", "删除", "删除了项目：" + projectMap.get("projectName"), 0,null,null,null);

            solrService.updateCompanyIndex((String)projectMap.get("companyId"));


            return ResponseConstants.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

    }

    /**
     * 查询项目详情
     *
     * @param req
     * @param rsp
     * @throws Exception
     * @return根据id查询项目
     */
    @ApiOperation(value = "项目详情")
    @ResponseBody
    @RequestMapping(value = "/selectProjectById",method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter selectProjectById(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "项目Id" ,required = true) @RequestParam(required = true)  String id) throws Exception {
         if (StringUtil.isBlank(id)) {
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            Map<String, Object> projectMap = projectService.selectProjectById(id);
            ResponseDbCenter db = new ResponseDbCenter();
            db.setResModel(projectMap);

            return db;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


}
