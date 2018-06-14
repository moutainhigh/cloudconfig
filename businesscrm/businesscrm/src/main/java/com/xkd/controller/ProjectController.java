package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
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

@Api(description = "项目相关接口")
@Transactional
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {


    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserDynamicService userDynamicService;
    @Autowired
    private PagerFileService pagerFileService;
    @Autowired
    private UserService userService;


    @ApiOperation(value = "查询项目详情")
    @ResponseBody
    @RequestMapping(value = "/selectProjectById", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter selectProjectById(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "id", required = true) @RequestParam(required = true) String id
    ) {
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        try {
            Map<String, Object> map = projectService.selectProjectById(id);
            responseDbCenter.setResModel(map);
        } catch (Exception e) {
            log.error("异常栈:",e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
        return responseDbCenter;
    }


    @ApiOperation(value = "检索项目")
    @ResponseBody
    @RequestMapping(value = "/searchProject", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter searchProject(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "搜索值", required = false) @RequestParam(required = false) String content,
                                          @ApiParam(value = "项目编号", required = false) @RequestParam(required = false) String projectCode,
                                          @ApiParam(value = "开始时间", required = false) @RequestParam(required = false) String startDate,
                                          @ApiParam(value = "结束时间", required = false) @RequestParam(required = false) String endDate,
                                          @ApiParam(value = "项目名称", required = false) @RequestParam(required = false) String projectName,
                                          @ApiParam(value = "项目类型Id", required = false) @RequestParam(required = false) String projectTypeId,
                                          @ApiParam(value = "项目经理", required = false) @RequestParam(required = false) String projectManager,
                                          @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                          @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                          @ApiParam(value = "区", required = false) @RequestParam(required = false) String county,
                                          @ApiParam(value = "客户类型Id", required = false) @RequestParam(required = false) String customerTypeId,
                                          @ApiParam(value = "服务对象Id", required = false) @RequestParam(required = false) String serveObjectTypeId,
                                          @ApiParam(value = "项目负责人ID", required = false) @RequestParam(required = false) String dutyPerson,
                                          @ApiParam(value = "部门对象Id", required = false) @RequestParam(required = false) String departmentTypeId,
                                          @ApiParam(value = "所属客户", required = false) @RequestParam(required = false) String companyId,
                                          @ApiParam(value = "所属部门", required = false) @RequestParam(required = false) String departmentId,
                                          @ApiParam(value = "标志位   1  我负责的客户 2 我参与的客户  3 总监是我的客户    5 全部客户  6 我负责的项目 7 我创建的项目  ", required = false) @RequestParam(required = false) Integer publicFlag,
                                          @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                          @ApiParam(value = "每页多少数", required = true) @RequestParam(required = true) Integer pageSize

    ) {
        String loginUserId = (String) req.getAttribute("loginUserId");

        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        int start = (currentPage - 1) * pageSize;


        if (StringUtils.isBlank(startDate)) {
            startDate = null;
        }
        if (StringUtils.isBlank(endDate)) {
            endDate = null;
        }


        try {

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("content", content);
            paramMap.put("projectCode", projectCode);
            paramMap.put("startDate", startDate);
            paramMap.put("projectName", projectName);
            paramMap.put("projectTypeId", projectTypeId);
            paramMap.put("projectManager", projectManager);
            paramMap.put("province", province);
            paramMap.put("city", city);
            paramMap.put("county", county);
            paramMap.put("customerTypeId", customerTypeId);
            paramMap.put("serveObjectTypeId", serveObjectTypeId);
            paramMap.put("departmentTypeId", departmentTypeId);
            paramMap.put("dutyPerson", dutyPerson);
            paramMap.put("companyId", companyId);
            paramMap.put("departmentId", departmentId);
            paramMap.put("endDate", endDate);
            paramMap.put("publicFlag",publicFlag);
            paramMap.put("start", start);
            paramMap.put("pageSize", pageSize);

            paramMap.put("loginUserId",loginUserId);

            Map<String, Object> resultMap = projectService.selectProjectsByContent(paramMap, loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(resultMap.get("list"));
            responseDbCenter.setTotalRows(resultMap.get("total") + "");
            return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:",e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }














    @ApiOperation(value = "检索企业下的项目")
    @ResponseBody
    @RequestMapping(value = "/searchProjectUnderCompanyId", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter searchProjectUnderCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "所属客户", required = true) @RequestParam(required = true) String companyId,
                                          @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                          @ApiParam(value = "每页多少数", required = true) @RequestParam(required = true) Integer pageSize

    ) {
        String loginUserId = (String) req.getAttribute("loginUserId");

        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        int start = (currentPage - 1) * pageSize;





        try {

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("companyId", companyId);
            paramMap.put("start", start);
            paramMap.put("pageSize", pageSize);
            paramMap.put("loginUserId",loginUserId);
            Map<String, Object> resultMap = projectService.selectProjectsByContent(paramMap, loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(resultMap.get("list"));
            responseDbCenter.setTotalRows(resultMap.get("total") + "");
            return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:",e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }








    @ApiOperation(value = "添加项目")
    @ResponseBody
    @RequestMapping(value = "/addProject", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter addProject(HttpServletRequest req, HttpServletResponse rsp,
                                       @ApiParam(value = "客户Id", required = true) @RequestParam(required = true) String companyId,
                                       @ApiParam(value = "项目编码", required = true) @RequestParam(required = true) String projectCode,
                                       @ApiParam(value = "项目名称", required = true) @RequestParam(required = true) String projectName,
                                       @ApiParam(value = "项目类型Id", required = false) @RequestParam(required = false) String projectTypeId,
                                       @ApiParam(value = "开始时间 2012-10-11", required = false) @RequestParam(required = false) String startDate,
                                       @ApiParam(value = "结束时间 2012-10-12", required = false) @RequestParam(required = false) String endDate,
                                       @ApiParam(value = "地址", required = false) @RequestParam(required = false) String address,
                                       @ApiParam(value = "满意度", required = false) @RequestParam(required = false) String feel,
                                       @ApiParam(value = "项目经理", required = true) @RequestParam(required = true) String projectManager,
                                       @ApiParam(value = "负责人", required = true) @RequestParam(required = true) String dutyPerson,
                                       @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                       @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                       @ApiParam(value = "县", required = false) @RequestParam(required = false) String county,
                                       @ApiParam(value = "部门类型Id", required = false) @RequestParam(required = false) String departmentTypeId,
                                       @ApiParam(value = "服务对象类型Id", required = false) @RequestParam(required = false) String serveObjectTypeId,
                                       @ApiParam(value = "客户类型Id", required = false) @RequestParam(required = false) String customerTypeId
    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        try {


            Map<String, Object> existInDb = projectService.selectProjectUnDeleted(projectCode);
            if (existInDb != null) {
                return ResponseConstants.PROJECTCODE_EXISTS;
            }


            Map<String, Object> map = new HashMap<>();
            String id = UUID.randomUUID().toString();
            map.put("id", id);
            //创建人所在的公司Id
            map.put("pcCompanyId", loginUserMap.get("pcCompanyId"));
            map.put("companyId", companyId);
            map.put("projectCode", projectCode);
            map.put("projectName", projectName);
            map.put("projectTypeId", projectTypeId);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("address", address);
            map.put("feel", feel);
            map.put("projectManager", projectManager);
            map.put("dutyPerson", dutyPerson);
            map.put("province", province);
            map.put("city", city);
            map.put("county", county);
            map.put("departmentTypeId", departmentTypeId);
            map.put("serveObjectTypeId", serveObjectTypeId);
            map.put("customerTypeId", customerTypeId);
            map.put("status", "0");
            map.put("createdBy", loginUserId);
            map.put("createDate", new Date());
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());


            projectService.insertProject(map);
            //企业下面动态
            userDynamicService.addUserDynamic(loginUserId, companyId, "", "添加", "添加了项目：" + map.get("projectName"), 0, null, null, null);
            //项目下面动态
            userDynamicService.addUserDynamic(loginUserId, id, "", "添加", "添加了项目：" + map.get("projectName"), 0, null, null, null);

            pagerFileService.LodingPagerFile(map.get("projectName"), "1", id, loginUserId, map.get("projectTypeValue") + "", map.get("departmentId") + "");

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        return ResponseConstants.SUCCESS;

    }

    @ApiOperation(value = "更新项目")
    @ResponseBody
    @RequestMapping(value = "/updateProject", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter updateProject(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "Id", required = true) @RequestParam(required = true) String id,
                                          @ApiParam(value = "客户Id", required = false) @RequestParam(required = false) String companyId,
                                          @ApiParam(value = "项目编码", required = false) @RequestParam(required = false) String projectCode,
                                          @ApiParam(value = "项目名称", required = false) @RequestParam(required = false) String projectName,
                                          @ApiParam(value = "项目类型Id", required = false) @RequestParam(required = false) String projectTypeId,
                                          @ApiParam(value = "开始时间 2011-10-11", required = false) @RequestParam(required = false) String startDate,
                                          @ApiParam(value = "结束时间 2011-10-11", required = false) @RequestParam(required = false) String endDate,
                                          @ApiParam(value = "地址", required = false) @RequestParam(required = false) String address,
                                          @ApiParam(value = "满意度", required = false) @RequestParam(required = false) String feel,
                                          @ApiParam(value = "项目经理", required = false) @RequestParam(required = false) String projectManager,
                                          @ApiParam(value = "负责人", required = false) @RequestParam(required = false) String dutyPerson,
                                          @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                          @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                          @ApiParam(value = "县", required = false) @RequestParam(required = false) String county,
                                          @ApiParam(value = "部门类型Id", required = false) @RequestParam(required = false) String departmentTypeId,
                                          @ApiParam(value = "服务对象类型Id", required = false) @RequestParam(required = false) String serveObjectTypeId,
                                          @ApiParam(value = "客户类型Id", required = false) @RequestParam(required = false) String customerTypeId
    ) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        try {


            Map<String, Object> existInDb = projectService.selectProjectUnDeleted(projectCode);
            if (existInDb != null) {
                if (!id.equals(existInDb.get("id"))) {
                    return ResponseConstants.PROJECTCODE_EXISTS;
                }
            }


            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            //创建人所在的公司Id
            map.put("companyId", companyId);
            map.put("projectCode", projectCode);
            map.put("projectName", projectName);
            map.put("projectTypeId", projectTypeId);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("address", address);
            map.put("feel", feel);
            map.put("projectManager", projectManager);
            map.put("dutyPerson", dutyPerson);
            map.put("province", province);
            map.put("city", city);
            map.put("county", county);
            map.put("departmentTypeId", departmentTypeId);
            map.put("serveObjectTypeId", serveObjectTypeId);
            map.put("customerTypeId", customerTypeId);
            map.put("status", "0");
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());

            projectService.updateProject(map);

            Map<String, Object> projectMap = projectService.selectProjectById(id);
            //企业下面的动态
            userDynamicService.addUserDynamic(loginUserId, projectMap.get("companyId").toString(), "", "更新", "更新了项目" + projectMap.get("projectName"), 0, null, null, null);
            //项目下面的动态
            userDynamicService.addUserDynamic(loginUserId, projectMap.get("id").toString(), "", "更新", "更新了项目" + projectMap.get("projectName"), 0, null, null, null);


            pagerFileService.updatePagerFileName(map.get("id") + "", loginUserId, map.get("projectName") + "");

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();

            return responseDbCenter;

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

    }

    @ApiOperation(value = "删除项目")
    @ResponseBody
    @RequestMapping(value = "/deleteProjectByIds", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter deleteProjectByIds(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "Ids 多个Id用逗号分隔", required = true) @RequestParam(required = true) String ids

    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");

        String[] cids = ids.split(",");
        List<String> idList = Arrays.asList(cids);

        try {

            projectService.deleteProjectByIdList(idList);

           List<Map<String,Object>> mapList= projectService.selectProjectByIds(idList);

            for (int i = 0; i <mapList.size() ; i++) {
                userDynamicService.addUserDynamic(loginUserId, (String)mapList.get(i).get("companyId"), null, "删除", "删除项目\"" + mapList.get(i).get("projectName") + "\"", 0, null, null, null);
            }


        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;

    }


}
