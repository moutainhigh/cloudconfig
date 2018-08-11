package com.szjk.base.controller;

import com.szjk.base.model.common.ResponseModel;
import com.szjk.base.model.user.UserVo;
import com.szjk.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Api(value = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;

    @ApiOperation(value = "列出用户")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel<List<UserVo>> list(HttpServletRequest httpServletRequest, Model model) {
        List<UserVo> list = userService.list();

        ResponseModel<List<UserVo>> responseModel = new ResponseModel();

        responseModel.setRows(list);
        return responseModel;
    }







}
