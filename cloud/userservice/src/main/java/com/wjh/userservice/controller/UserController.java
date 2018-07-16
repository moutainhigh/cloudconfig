package com.wjh.userservice.controller;

import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.userservice.service.IdService;
import com.wjh.userservice.service.UserService;
import com.wjh.userservicemodel.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.wjh.common.model.ResponseConstant.LOGIN_FAIL;
import static com.wjh.common.model.ResponseConstant.MOBILE_EXISTS;
import static com.wjh.common.model.ResponseConstant.PASSWORD_NOT_EQUAL;

@Api(description = "用户相关接口")
@RefreshScope
@RestController
@RequestMapping("/user")
public class UserController {
    Logger logger = LogManager.getLogger();
    @Autowired
    private UserService userService;


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

        User userInDb = userService.detailByUser(mobile);
        if (null != userInDb) {
            return MOBILE_EXISTS;
        }

        User user = new User();
        user.setMobile(mobile);
        user.setPassword(password);
        User userR = userService.insert(user);
        ResponseModel<User> responseModel = new ResponseModel();
        responseModel.setResModel(userR);
        return responseModel;

    }


    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel<User> update(@ApiParam(value = "用户") @RequestBody(required = true) User user) {
        User userR = userService.update(user);
        ResponseModel<User> responseModel = new ResponseModel<User>();
        responseModel.setResModel(userR);
        return responseModel;
    }


    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseModel<User> delete(@ApiParam(value = "id") @RequestParam(required = true) String id) {
        long idLong = Long.valueOf(id);
        userService.delete(idLong);
        ResponseModel<User> responseModel = new ResponseModel<User>();
        return responseModel;
    }


    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseModel login(
            @ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile,
            @ApiParam(value = "密码,MD5加密", required = true) @RequestParam(required = true) String password) {
        try {


            User user = userService.selectByLogin(mobile, password);
            if (null == user) {
                return LOGIN_FAIL;
            }
            String token = UUID.randomUUID().toString();
            ResponseModel<String> responseModel = new ResponseModel<String>();
            responseModel.setResModel(token);
            return responseModel;

        } catch (Exception e) {
            ;
            logger.error(ServiceIdConstant.userservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }
    }


}
