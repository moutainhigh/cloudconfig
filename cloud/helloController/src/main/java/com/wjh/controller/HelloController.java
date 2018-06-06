package com.wjh.controller;


import com.netflix.discovery.converters.Auto;
import com.wjh.serviceInterface.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @Autowired
    HelloService helloService;

    @RequestMapping(value = "sayHelloC",method = RequestMethod.GET)
    public String sayHelloC(){
        return helloService.sayHello();
    }

}
