package com.wjh.helloController.controller;


import com.wjh.helloController.serviceInterface.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class HelloController {


    @Autowired
    HelloService helloService;







    @RequestMapping(value = "sayHelloC",method = RequestMethod.GET)
    public String sayHelloC(){
        return   helloService.sayHello();
     }

}
