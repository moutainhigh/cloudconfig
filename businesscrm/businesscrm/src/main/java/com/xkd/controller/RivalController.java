package com.xkd.controller;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.RivalService;
import com.xkd.service.UserDataPermissionService;
import com.xkd.service.UserDynamicService;
import com.xkd.service.UserService;
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
 * Created by dell on 2018/4/28.
 */
@Api(description = "竞争对手相关接口")
@Transactional
@Controller
@RequestMapping("/rival")
public class RivalController extends BaseController {

    @Autowired
    RivalService rivalService;

    @Autowired
    UserService userService;

    @Autowired
    UserDynamicService userDynamicService;

    @Autowired
    UserDataPermissionService userDataPermissionService;

    @ApiOperation(value = "插入竞争对手")
    @ResponseBody
    @RequestMapping(value = "/addRival", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter addRival(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "竞争者名称", required = true) @RequestParam(required = true) String rivalName,
                                        @ApiParam(value = "规模", required = false) @RequestParam(required = false) String size,
                                        @ApiParam(value = "竞争力Id", required = false) @RequestParam(required = false) String rivalDegreeId,
                                        @ApiParam(value = "部门Id", required = false) @RequestParam(required = false) String departmentId,
                                        @ApiParam(value = "优势", required = false) @RequestParam(required = false) String advantage,
                                        @ApiParam(value = "劣势", required = false) @RequestParam(required = false) String disAdvantage,
                                        @ApiParam(value = "策略", required = false) @RequestParam(required = false) String policies,
                                        @ApiParam(value = "销售分析", required = false) @RequestParam(required = false) String saleAnalysis,
                                        @ApiParam(value = "市场分析", required = false) @RequestParam(required = false) String marketAnalysis,
                                        @ApiParam(value = "备注", required = false) @RequestParam(required = false) String remark
    ) {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        if (StringUtils.isBlank(departmentId)){
            departmentId= (String) loginUserMap.get("departmentId");

        }
        try {

            Map<String, Object> existsInDb = rivalService.selectRivalByName(rivalName, (String) loginUserMap.get("pcCompanyId"));
            if (existsInDb != null) {
                return ResponseConstants.RIVAL_NAME_EXISTS;
            }

            Map<String, Object> map = new HashMap<>();
            String id=UUID.randomUUID().toString();
            map.put("id",id );
            map.put("rivalName", rivalName);
            map.put("size", size);
            map.put("rivalDegreeId", rivalDegreeId);
            map.put("departmentId", departmentId);
            map.put("pcCompanyId", loginUserMap.get("pcCompanyId"));
            map.put("advantage", advantage);
            map.put("disAdvantage", disAdvantage);
            map.put("policies", policies);
            map.put("saleAnalysis", saleAnalysis);
            map.put("marketAnalysis", marketAnalysis);
            map.put("remark", remark);
            map.put("createdBy", loginUserId);
            map.put("createDate", new Date());
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());
            map.put("status", 0);
            rivalService.insertRival(map);

            userDynamicService.addUserDynamic(loginUserId, id, "", "添加", "添加了竞争对手：" + map.get("rivalName"), 0, null, null, null);

        } catch (Exception e) {
            log.error("异常栈:", e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
        return responseDbCenter;
    }


    @ApiOperation(value = "更新竞争对手")
    @ResponseBody
    @RequestMapping(value = "/updateRival", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter updateRival(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "id", required = true) @RequestParam(required = true) String id,
                                        @ApiParam(value = "竞争者名称", required = false) @RequestParam(required = false) String rivalName,
                                        @ApiParam(value = "规模", required = false) @RequestParam(required = false) String size,
                                        @ApiParam(value = "竞争力Id", required = false) @RequestParam(required = false) String rivalDegreeId,
                                        @ApiParam(value = "部门Id", required = false) @RequestParam(required = false) String departmentId,
                                        @ApiParam(value = "优势", required = false) @RequestParam(required = false) String advantage,
                                        @ApiParam(value = "劣势", required = false) @RequestParam(required = false) String disAdvantage,
                                        @ApiParam(value = "策略", required = false) @RequestParam(required = false) String policies,
                                        @ApiParam(value = "销售分析", required = false) @RequestParam(required = false) String saleAnalysis,
                                        @ApiParam(value = "市场分析", required = false) @RequestParam(required = false) String marketAnalysis,
                                        @ApiParam(value = "备注", required = false) @RequestParam(required = false) String remark
    ) {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        try {

            Map<String, Object> existsInDb = rivalService.selectRivalByName(rivalName, (String) loginUserMap.get("pcCompanyId"));
            if (existsInDb != null) {
                if (!id.equals(existsInDb.get("id"))) {
                    return ResponseConstants.RIVAL_NAME_EXISTS;
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("rivalName", rivalName);
            map.put("size", size);
            map.put("rivalDegreeId", rivalDegreeId);
            map.put("departmentId", departmentId);
            map.put("advantage", advantage);
            map.put("disAdvantage", disAdvantage);
            map.put("policies", policies);
            map.put("saleAnalysis", saleAnalysis);
            map.put("marketAnalysis", marketAnalysis);
            map.put("remark", remark);
            map.put("createdBy", loginUserId);
            map.put("createDate", new Date());
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());
            map.put("status", 0);
            rivalService.updateRival(map);

            userDynamicService.addUserDynamic(loginUserId, id, "", "更新", "更新了竞争对手：" + map.get("rivalName"), 0, null, null, null);


        } catch (Exception e) {
            log.error("异常栈:", e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
        return responseDbCenter;
    }


    @ApiOperation(value = "删除竞争对手")
    @ResponseBody
    @RequestMapping(value = "/deleteRival", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter deleteRival(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "id  多个用逗号分隔", required = true) @RequestParam(required = true) String ids
    ) {


        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        try {
            String[] idArray = ids.split(",");
            List<String> idList = Arrays.asList(idArray);
            rivalService.deleteByIds(idList);





        } catch (Exception e) {
            log.error("异常栈:", e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
        return responseDbCenter;
    }


    @ApiOperation(value = "搜索竞争对手")
    @ResponseBody
    @RequestMapping(value = "/searchRival", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter searchRival(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "搜索值", required = false) @RequestParam(required = false) String searchValue,
                                        @ApiParam(value = "部门Id", required = false) @RequestParam(required = false) String departmentId,
                                        @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                        @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    ) {

        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Map<String, Object>> list = new ArrayList<>();
        Integer count = 0;
        List<String> departmentIdList = new ArrayList<>();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        try {
            departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId, loginUserId);
            list = rivalService.searchRival(departmentIdList, searchValue, currentPage, pageSize);
            count = rivalService.searchRivalCount(departmentIdList, searchValue);
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(count+"");
        } catch (Exception e) {
            log.error("异常栈:", e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
        return responseDbCenter;
    }

    @ApiOperation(value = "竞争对手详情")
    @ResponseBody
    @RequestMapping(value = "/rivalDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter rivalDetail(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "id", required = false) @RequestParam(required = false) String id

    ) {

         ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        try {
             Map<String,Object>  map=rivalService.selectRivalById(id);
            responseDbCenter.setResModel(map);
         } catch (Exception e) {
            log.error("异常栈:", e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
        return responseDbCenter;
    }

}
