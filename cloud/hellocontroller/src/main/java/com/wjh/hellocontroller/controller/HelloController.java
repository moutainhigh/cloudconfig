package com.wjh.hellocontroller.controller;


import com.wjh.hellocontroller.serviceInterface.HelloService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "hello相关接口")
@RefreshScope
@RestController
public class HelloController {


    @Autowired
    HelloService helloService;


    @ApiOperation(value = "示例")
    @RequestMapping(value = "sayHelloC", method = RequestMethod.GET)
    public String sayHelloC(@ApiParam(value = "姓名", required = true) @RequestParam(required = true) String name) {
        return helloService.sayHello(name);
    }

}
