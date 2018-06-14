package com.xkd.controller;

import com.xkd.mapper.UserMapper;
import com.xkd.model.DepartmentTreeNode;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.DepartmentService;
import com.xkd.service.UserDataPermissionService;
import com.xkd.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/14.
 */
@Api(description = "用户数据权限")
@Controller
@RequestMapping("/userDataPermission")
@Transactional
public class UserDataPermissionController extends  BaseController {

    @Autowired
    UserDataPermissionService userDataPermissionService;

    @Autowired
    UserService userService;

    @Autowired
    DepartmentService departmentService;

    @ApiOperation(value = "查询数据权限配置部门树")
    @ResponseBody
    @RequestMapping(value = "/departmentTree",method = {RequestMethod.POST})
    public ResponseDbCenter departmentTree(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "用户Id",required = true) @RequestParam(required = true) String userId
    ) throws Exception {



        DepartmentTreeNode departmentTreeNode=userDataPermissionService.getCheckedUserDataPermissionDepartmentTree(userId);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(departmentTreeNode);
        return responseDbCenter;
    }



    @ApiOperation(value = "保存数据权限配置数据")
    @ResponseBody
    @RequestMapping(value = "/saveUserDataPermissionDepartmentIds",method = {RequestMethod.POST})
    public ResponseDbCenter saveUserDataPermissionDepartmentIds(HttpServletRequest req, HttpServletResponse rsp,
                                                                @ApiParam(value = "用户Id",required = true) @RequestParam(required = true) String userId,
                                                                @ApiParam(value = "部门Id列表，多个以逗号分隔",required = true) @RequestParam(required = true) String departmentIds
    ) throws Exception {

        // 当前登录用户的Id
         List<String> list=new ArrayList<>();
        if (!StringUtil.isBlank(departmentIds)){
            String[] ids=departmentIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                list.add(ids[i]);
            }
        }


        //如果用户的所在部门也被给了权限，则把它从列表中删除
        Map<String,Object>  userMap=userService.selectUserById(userId);
        if (userMap!=null){
            list.remove(userMap.get("departmentId"));
        }

        userDataPermissionService.saveUserDataPermissionDepartmentIdList(userId,list);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    @ApiOperation(value = "查询数据权限配置部门列表")
    @ResponseBody
    @RequestMapping(value = "/selectUserDataPermissionDepartmentByUserId",method = {RequestMethod.POST})
    public ResponseDbCenter selectUserDataPermissonDepartmentIds(HttpServletRequest req, HttpServletResponse rsp

     ) throws Exception {


        String loginUserId = (String) req.getAttribute("loginUserId");

        List<Map<String,Object>> list= userDataPermissionService.selectDepartmentByUserId(loginUserId);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);

        return responseDbCenter;
    }



    @ApiOperation(value = "查询数据权限配置部门树")
    @ResponseBody
    @RequestMapping(value = "/selectUserDataPermissionDepartmentTreeByUserId",method = {RequestMethod.POST})
    public ResponseDbCenter selectUserDataPermissionDepartmentTreeByUserId(HttpServletRequest req, HttpServletResponse rsp

    ) throws Exception {


        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
        DepartmentTreeNode departmentTreeNode=null;
        if ("1".equals(loginUserMap.get("roleId"))) {//超级管理员只需要展示到公司一层
              departmentTreeNode = departmentService.selectSuperDepartment();
        }else {
              departmentTreeNode = userDataPermissionService.selectPermitedDepartmentTree(loginUserId);

        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(departmentTreeNode);

        return responseDbCenter;
    }



}
