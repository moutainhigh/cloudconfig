package com.wjh.helloservice.controller;

 import io.swagger.annotations.Api;
 import io.swagger.annotations.ApiOperation;
 import io.swagger.annotations.ApiParam;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.RestController;
@Api(description = "hello相关接口")
@RestController
@RequestMapping("/hello")
public class HelloController {

    @ApiOperation(value = "示例")
    @RequestMapping(value = "/sayHello")
    public Object hello(@ApiParam(value = "姓名",required = true)@RequestParam(required = true) String name){
        return "你好："+name+"！";
    }

}
