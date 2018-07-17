package com.wjh.usercontroller.controller;


import com.wjh.common.model.ResponseModel;
import com.wjh.usercontroller.service.UserService;
import com.wjh.userservicemodel.model.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;


@Api(description = "用户相关接口")
@RefreshScope
@RestController
@RequestMapping("/userInfo")
public class UserController {


    @Autowired
    UserService userService;


    @ApiOperation(value = "用户详情")
    @RequestMapping(value = "/detailByMobile", method = RequestMethod.GET)
    public ResponseModel detailByMobile(@ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile) {
        ResponseModel responseModel = userService.detailByMobile(mobile);
        return responseModel;
    }



    @ApiOperation(value = "注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseModel register(
            @ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile,
            @ApiParam(value = "密码，MD5加密", required = true) @RequestParam(required = true) String password,
            @ApiParam(value = "重复密码，MD5加密", required = true) @RequestParam(required = true) String repeatPassword) {
        return userService.register(mobile,password,repeatPassword);
    }


    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "用户") @RequestBody(required = true) UserDto user) {
        return userService.update(user);
    }



    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseModel delete(@ApiParam(value = "id") @RequestParam(required = true) String id) {
        return userService.delete(id);
    }

}
