package com.wjh.usercontroller.controller;


import com.wjh.common.model.ResponseModel;
import com.wjh.usercontroller.service.UserService;
import com.wjh.userservicemodel.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "用户相关接口")
@RefreshScope
@RestController
@RequestMapping("/userInfo")
public class UserController {

    @Autowired
    UserService userService;


    @ApiOperation(value = "用户详情")
    @RequestMapping(value = "/detailByMobile", method = RequestMethod.GET)
    public ResponseModel<User> detailByMobile(@ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile) {
        ResponseModel responseModel = userService.detailByMobile(mobile);
        return responseModel;
    }

}
