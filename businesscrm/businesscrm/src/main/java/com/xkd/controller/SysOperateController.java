package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.Operate;
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


@Api(description = "权限相关接口")
@Controller
@RequestMapping("/operate")
@Transactional
public class SysOperateController extends BaseController {
    @Autowired
    SysOperateService sysOperateService;

    @Autowired
    SysUserOperateService sysUserOperateService;

    @Autowired
    SysRoleOperateService sysRoleOperateService;

    @Autowired
    RoleService roleService;

    @Autowired
    CustomerService customerService;

    @Autowired
    OperateCacheService operateCacheService;


    /**
     * 添加权限
     *
     * @param req
     * @param rsp
     * @param operate {
     *                "menuId":"1",
     *                "url":"/aaa",
     *                "operateName":"测试333",
     *                "operateCode":"测试333",
     *                "orderNo":"1"
     *                }
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "添加权限")
    @ResponseBody
    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter add(HttpServletRequest req, HttpServletResponse rsp, @ApiParam(value = "  权限Json如 {\n" +
            "                     \"menuId\":\"1\",\n" +
            "                      \"url\":\"/aaa\",\n" +
            "                      \"operateName\":\"测试333\",\n" +
            "                      \"operateCode\":\"测试333\",\n" +
            "                      \"orderNo\":\"1\"\n" +
            "                      }",required = true)  @RequestParam(required = true)   String operate) throws Exception {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> map = JSON.parseObject(operate, new TypeReference<Map<String, Object>>() {
            });

            List<Map<String,Object>> operateList=sysOperateService.selectOperateByOperateCode((String) map.get("operateCode"));
            if (operateList.size()>0){
                return ResponseConstants.DATA_OPERATIONCODE_REPEATED;
            }

            String id = UUID.randomUUID().toString();
            /**
             * 添加权限
             */
            map.put("id", id);
            map.put("updatedBy", loginUserId);
            map.put("createdBy", loginUserId);
            map.put("createDate", new Date());
            map.put("updateDate", new Date());

            sysOperateService.insert(map);
            /**
             * 自动给超级管理员添加权限
             */
            List<Map<String, Object>> idList = new ArrayList<>();
            Map<String, Object> roleOperateMap = new HashMap<>();
            roleOperateMap.put("id", UUID.randomUUID().toString());
            roleOperateMap.put("roleId", "1");
            roleOperateMap.put("operateId", id);

            idList.add(roleOperateMap);
            sysRoleOperateService.insertList(idList);


            /**
             * 更新各客户公司管理员角色对应的权限
             */
            List<Map<String,Object>> pcCompanyMapList=customerService.searchPcCompany(null, null, null, null, null, null, 1, 100000);
            for (int i = 0; i <pcCompanyMapList.size() ; i++) {
                customerService.updateAdminRole((String)pcCompanyMapList.get(i).get("id"),loginUserId);
            }

            //清除旧有权限，便于在拦截器中重新加载
            operateCacheService.clear();

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    /**
     * 修改权限基本信息
     *
     * @param req
     * @param rsp
     * @param operate {
     *                "id":"9e569fbb-774f-4d49-8583-043192db6f37",
     *                "menuId":"1",
     *                "url":"/aaa",
     *                "operateName":"测试333",
     *                "operateCode":"测试333",
     *                "orderNo":"1"
     *                }
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新权限")
    @ResponseBody
    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter update(HttpServletRequest req, HttpServletResponse rsp,@ApiParam(value = "json格式 如:{\n" +
            "                     \"id\":\"9e569fbb-774f-4d49-8583-043192db6f37\",\n" +
            "                     \"menuId\":\"1\",\n" +
            "                      \"url\":\"/aaa\",\n" +
            "                      \"operateName\":\"测试333\",\n" +
            "                      \"operateCode\":\"测试333\",\n" +
            "                    \"orderNo\":\"1\"\n" +
            "                     }",required = true)@RequestParam(required = true)   String operate) throws Exception {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> map = JSON.parseObject(operate, new TypeReference<Map<String, Object>>() {
            });

            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());
            sysOperateService.update(map);

            //清除旧有权限，便于在拦截器中重新加载
            operateCacheService.clear();

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    /**
     * @param req
     * @param rsp
     * @param ids [“1”,”2”]
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除权限")
    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter delete(HttpServletRequest req, HttpServletResponse rsp,
                                   @ApiParam(value = "ids json格式的数组如:[\"1\",\"2\"]",required = true) @RequestParam(required = true)   String ids) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        try {
            List<String> list = JSON.parseObject(ids, new TypeReference<List<String>>() {
            });
            for (int i = 0; i < list.size(); i++) {
                //删除权限
                sysOperateService.delete(list.get(i));
                //删除权限与角色的映射关系
                sysRoleOperateService.deleteByOperateId(list.get(i));
                //删除权限与用户的映射关系
                sysUserOperateService.deleteByOperateId(list.get(i));
            }

            /**
             * 更新各客户公司管理员角色对应的权限
             */
            List<Map<String,Object>> pcCompanyMapList=customerService.searchPcCompany(null, null, null, null, null, null, 1, 100000);
            for (int i = 0; i <pcCompanyMapList.size() ; i++) {
                customerService.updateAdminRole((String)pcCompanyMapList.get(i).get("id"),loginUserId);
            }

            //清除旧有权限，便于在拦截器中重新加载
            operateCacheService.clear();
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

    /**
     * @param req
     * @param rsp
     * @param operateName
     * @param menuId
     * @param currentPage
     * @param pageSize
     * @return {
     * "repCode": "S0000",
     * "repNote": "SUCCESS",
     * "pertain": "CRM",
     * "totalRows": "49",
     * "resModel": [
     * {
     * "id": "18043524-0694-4aaf-9dce-ed9bf38c24b2",
     * "menuId": "102",
     * "menuName": "行程管理",
     * "url": "schedule/getScheduleListOneMonth",
     * "operateName": "查看行程",
     * "operateCode": "C3001",
     * "orderNo": 17,
     * "checked": false,
     * "updateDate": null,
     * "updator": null,
     * "updatedBy": null,
     * "fromRole": false
     * }
     * ]
     * }
     * @throws Exception
     */
    @ApiOperation(value = "检索权限")
    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter list(HttpServletRequest req, HttpServletResponse rsp,
                                 @ApiParam(value = "权限名称",required = false)@RequestParam(required = false)   String operateName,
                                 @ApiParam(value = "菜单Id",required = false)@RequestParam(required = false)  String menuId,
                                 @ApiParam(value = "当前页" ,required = true)@RequestParam(required = true) Integer currentPage,
                                 @ApiParam(value = "每页显示多少条" ,required = true) @RequestParam(required = true) Integer pageSize) throws Exception {
        List<Operate> list = null;
        Integer total = 0;
        try {
            if (StringUtils.isBlank(menuId)) {
                menuId = null;
            }
            if (StringUtils.isBlank(operateName)) {
                operateName = null;
            }
            list = sysOperateService.searchOperate(operateName, menuId, currentPage, pageSize);
            total = sysOperateService.searchOperateCount(operateName, menuId);

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(total + "");
        return responseDbCenter;
    }

    /**
     * @param req
     * @param rsp
     * @param roleId
     * @return {
     * "repCode": "S0000",
     * "repNote": "SUCCESS",
     * "pertain": "CRM",
     * "totalRows": "0",
     * "resModel": [
     * {
     * "operateList": [
     * {
     * "id": "9e569fbb-774f-4d49-8583-043192db6f37",
     * "menuId": "9e569fbb-774f-4d49-8583-043192db6f37",
     * "menuName": "资源",
     * "url": "/aaa",
     * "operateName": "测试333",
     * "operateCode": "测试333",
     * "orderNo": 1,
     * "checked": true,
     * "fromRole": false
     * }
     * ],
     * "menuId": "9e569fbb-774f-4d49-8583-043192db6f37",
     * "menuName": "资源"
     * },
     * {
     * "operateList": [
     * {
     * "id": "c2a8cd69-f30a-4821-b7b3-27330402b0d1",
     * "menuId": "c2a8cd69-f30a-4821-b7b3-27330402b0d1",
     * "menuName": "资源",
     * "url": "/aaa",
     * "operateName": "测试1",
     * "operateCode": "测试2",
     * "orderNo": 1,
     * "checked": false,
     * "fromRole": false
     * }
     * ],
     * "menuId": "c2a8cd69-f30a-4821-b7b3-27330402b0d1",
     * "menuName": "资源"
     * }
     * ]
     * }
     * @throws Exception
     */
    @ApiOperation(value = "列出某个角色下的权限")
    @ResponseBody
    @RequestMapping(value = "/listByRoleId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter listByRoleId(HttpServletRequest req, HttpServletResponse rsp,
                                         @ApiParam(value = "角色Id",required = true)@RequestParam(required = true)   String roleId) throws Exception {
        List<Map<String, Object>> list = null;
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        try {
            list = sysOperateService.selectOperateByRoleId(roleId,loginUserId);

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        return responseDbCenter;
    }

    /**
     * @param req
     * @param rsp
     * @param userId
     * @return {
     * "repCode": "S0000",
     * "repNote": "SUCCESS",
     * "pertain": "CRM",
     * "totalRows": "0",
     * "resModel": [
     * {
     * "operateList": [
     * {
     * "id": "9e569fbb-774f-4d49-8583-043192db6f37",
     * "menuId": "9e569fbb-774f-4d49-8583-043192db6f37",
     * "menuName": "资源",
     * "url": "/aaa",
     * "operateName": "测试333",
     * "operateCode": "测试333",
     * "orderNo": 1,
     * "checked": true,
     * "fromRole": false
     * }
     * ],
     * "menuId": "9e569fbb-774f-4d49-8583-043192db6f37",
     * "menuName": "资源"
     * },
     * {
     * "operateList": [
     * {
     * "id": "c2a8cd69-f30a-4821-b7b3-27330402b0d1",
     * "menuId": "c2a8cd69-f30a-4821-b7b3-27330402b0d1",
     * "menuName": "资源",
     * "url": "/aaa",
     * "operateName": "测试1",
     * "operateCode": "测试2",
     * "orderNo": 1,
     * "checked": false,
     * "fromRole": false
     * }
     * ],
     * "menuId": "c2a8cd69-f30a-4821-b7b3-27330402b0d1",
     * "menuName": "资源"
     * }
     * ]
     * }
     * @throws Exception
     */
    @ApiOperation(value = "列出某个用户下的权限")
    @ResponseBody
    @RequestMapping(value = "/listByUserId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter listByUserId(HttpServletRequest req, HttpServletResponse rsp,
                                         @ApiParam(value = "用户Id",required = true) @RequestParam(required = true)  String userId) throws Exception {
        List<Map<String, Object>> list = null;
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        try {
            list = sysOperateService.selectOperateByUserId(userId,loginUserId);

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        return responseDbCenter;
    }


    /**
     * 查询用户对某一个接口是否有权限
     *
     * @param req
     * @param rsp
     * @param path 如 "solr/search"
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询登录用户对某个接口的权限")
    @ResponseBody
    @RequestMapping(value = "/queryPermission", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter queryPermission(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "路径 如 /solr/search",required = true)@RequestParam(required = true)  String path) throws Exception {
        boolean flag = false;
        try {
            String token = req.getHeader("token");

            List<Operate> operateList = operateCacheService.getUserOperates(token);
            if (operateList != null && operateList.size() > 0) {
                for (int i = 0; i < operateList.size(); i++) {
                    if (operateList.get(i).getUrl().equals(path)) {
                        flag = true;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(flag);
        return responseDbCenter;
    }


}
