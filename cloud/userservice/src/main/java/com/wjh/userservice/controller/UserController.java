package com.wjh.userservice.controller;

import com.netflix.discovery.converters.Auto;
import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.idserviceapi.api.IdServiceI;
import com.wjh.userservice.service.IdService;
import com.wjh.userservice.service.UserService;
import com.wjh.userserviceapi.api.UserServiceI;
import com.wjh.userserviceapi.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.wjh.common.model.ResponseConstant.PASSWORD_NOT_EQUAL;

@Api(description = "用户相关接口")
@RefreshScope
@RestController
public class UserController implements UserServiceI {
    @Autowired
    private UserService userService;

    @Autowired
    private IdService idService;

    @ApiOperation(value = "查询用户")
    @RequestMapping(value = "/detailByMobile", method = RequestMethod.GET)
    public ResponseModel<User> detailByMobile(@ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile) {


        try {
            User user = userService.detailByUser(mobile);
            ResponseModel<User> responseModel = new ResponseModel<User>();
            responseModel.setResModel(user);
            return responseModel;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseModel register(
            @ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile,
            @ApiParam(value = "密码，MD5加密", required = true) @RequestParam(required = true) String password,
            @ApiParam(value = "重复密码，MD5加密", required = true) @RequestParam(required = true) String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            return PASSWORD_NOT_EQUAL;
        }

        User user = new User();

        user.setId(idService.generateId());
        user.setMobile(mobile);
        user.setPassword(password);
        userService.insert(user);
        return new ResponseModel();


    }


    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User detailByMobile(
            @ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile,
            @ApiParam(value = "加密字段，密码加盐MD5", required = true) @RequestParam(required = true) String encryptPassord) {
        return userService.detailByUser(mobile);
    }


}
